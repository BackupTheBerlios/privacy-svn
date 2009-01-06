/**
 *     This file is part of Diki.
 *
 *     Copyright (C) 2009 jtheuer
 *     Please refer to the documentation for a complete list of contributors
 *
 *     Diki is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Diki is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Diki.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.jtheuer.diki.lib.friends;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.QName;

import org.openrdf.concepts.foaf.Person;
import org.openrdf.elmo.ElmoManager;
import org.openrdf.elmo.sesame.SesameManager;
import org.openrdf.elmo.sesame.roles.SesameEntity;
import org.openrdf.repository.RepositoryException;
import org.paceproject.diki.elmo.DistantEntity;
import org.paceproject.diki.elmo.Rateing;

import de.jtheuer.diki.lib.EntityUpdatedListener;
import de.jtheuer.diki.lib.NetworkConnection;
import de.jtheuer.diki.lib.connectors.ConnectorException;
import de.jtheuer.diki.lib.connectors.gate.GateConnector;
import de.jtheuer.diki.lib.distance.RateingManager;
import de.jtheuer.diki.lib.friends.FriendStatusListener.Status;
import de.jtheuer.diki.lib.query.*;
import de.jtheuer.sesame.*;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de> the
 *         {@link UserController} keeps track of all defined relations to other
 *         network members. It synchronizes the database and the
 *         {@link GateConnector}
 * 
 * From the sync point on, the class guarantees that both, database and gate
 * connector, are synchronized as long as all friend-operations are triggered
 * here. You SHOULD NOT delete or add any friends directly in the database or
 * the gate connector.
 */
public class UserController {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(UserController.class.getName());

	private NetworkConnection connection;
	private List<FriendStatusListener> listeners = new LinkedList<FriendStatusListener>();

	private Person localAgent;

	/**
	 * @param connection
	 * @param gate
	 */
	public UserController(NetworkConnection connection) {
		super();
		this.connection = connection;

	}

	public Person getLocalUser() {
		if (localAgent == null) {
			String gateuser_basenamespace = connection.getNamespaceFactory().getNamespace();
			if (gateuser_basenamespace != null) {
				SesameManager manager = connection.getSesame().getLocalStore().createManager();
				localAgent = manager.designate(new QName(gateuser_basenamespace, ""), Person.class);
			} else {
				LOGGER.info("Local user not initalized: No namespace found");
			}
		}

		return localAgent;
	}

	public void syncFriends() {
		/* get manager and get all known friends */
		SesameManager sesame = connection.getSesame().getLocalStore().createManager();
		Set<Person> friends_sesame = getFriendsFromDatabase();
		LOGGER.info("local Person " + getLocalUser().getQName() + " has " + friends_sesame.size() + " friends");

		/* create sesame entities for all friends known by the gate */
		List<QNameURI> friends_gate = getFriendsFromGate();
		for (QNameURI friend : friends_gate) {
			QName qname = friend.toQName();

			Person candidate = sesame.designate(qname, Person.class);
			Iterator<?> nicknames = candidate.getFoafNicks().iterator(); 
			if (nicknames.hasNext() == false || nicknames.next().toString().startsWith(":")) {
				candidate.setFoafNicks(SimpleSet.create(guessNick(friend)));
			}
			LOGGER.fine("loading friend " + friend + " as " + qname);

			/* sync friends */
			friends_sesame.add(candidate);
		}

		/* create gate friends for all known sesame friends */
		for (Person person : friends_sesame) {
			QName person_qname = person.getQName();

			if (!hasFriendInGate(person_qname)) {
				QNameURI uri = new QNameURI(person_qname);
				addFriendGate(uri);
			}
		}

		sesame.close();

	}

	/**
	 * Tries to extract the nickname from ther user's URI.
	 * 
	 * @param uri
	 *            somethink like protocol:userid@host
	 * @return userid
	 */
	private String guessNick(QNameURI uri) {
		int from = uri.getNamespace().indexOf(":") + 1;
		int to = uri.getNamespace().indexOf("@");
		try {
			return uri.getNamespace().substring(from, to);
		} catch (RuntimeException e) {
			return uri.getNamespace();
		}
	}

	/**
	 * @return something like <em>Local Name &lt;local.name@jabber.org&gt;</em>
	 *         where local name is taken from
	 *         {@link UserController#getRepresentationOf(Person)}
	 */
	public String getEmailRepresentation() {
		return getRepresentationOf(localAgent) + " <" + getUserWithoutProtocol() + ">";
	}

	/**
	 * @return the user URI without the protocol prefix (i.e. xmpp:)
	 */
	private String getUserWithoutProtocol() {
		String user = QNameURI.toString(localAgent.getQName());
		int start = user.indexOf(":") + 1;
		user = user.substring(start);
		if (user.endsWith("/")) {
			return user.substring(0, user.length() - 1);
		} else {
			return user;
		}
	}

	/**
	 * Tries to find the most suitable String representation of the Person.
	 * <ol>
	 * <li>Name</li>
	 * <li>Nick name</li>
	 * <li>First Name Surname</li>
	 * <li>First Name Family Name</li>
	 * <li>uri</li>
	 * </ol>
	 * 
	 * @param person
	 * @return the best available representation
	 */
	public String getRepresentationOf(Person person) {
		String name;

		name = getContentOfIterator(person.getFoafNames().iterator());

		if (name == null) {
			name = getContentOfIterator(person.getFoafNicks().iterator());
			if (name == null) {
				name = getContentOfIterator(person.getFoafFirstNames().iterator());
				if (name == null) {
					name = person.toString();
				} else {
					String surname = getContentOfIterator(person.getFoafSurnames().iterator());
					if (surname == null) {
						surname = getContentOfIterator(person.getFoafGivennames().iterator());
						if (surname == null) {
							name = person.toString();
						} else {
							name = name + " " + surname;
						}
					} else {
						name = name + " " + surname;
					}
				}
			}
		}

		return name;
	}

	/**
	 * @param iterator
	 * @return the toString value if there is anything in the iterator -
	 *         otherwise null
	 */
	private String getContentOfIterator(Iterator<Object> iterator) {
		if (iterator.hasNext()) {
			return iterator.next().toString();
		}
		return null;
	}

	/**
	 * @return all {@link Person} instances that are stored in the database and
	 *         linked as FoafKnows to the LocalUser
	 */
	public Set<Person> getFriendsFromDatabase() {
		/* get all known friends */
		Set<Person> friendlist = getLocalUser().getFoafKnows();
		return friendlist;
	}

	public Iterable<Person> getAllPersonsFromDatabase() {
		return connection.getSesame().getLocalStore().createManager().findAll(Person.class);
	}

	/**
	 * checks if a given user is online
	 * 
	 * @param username
	 *            a username in the form "xmpp:user@example.com"
	 * @return true if the user is online, false if the user in offline or
	 *         unknown.
	 */
	public boolean isOnline(QNameURI username) {
		return connection.getGate().isOnline(username);
	}

	/**
	 * checks if a given user is online
	 * 
	 * @param username
	 *            a username in the form "xmpp:user@example.com"
	 * @return true if the user is online, false if the user in offline or
	 *         unknown.
	 */
	public boolean isOnline(Person person) {
		return connection.getGate().isOnline(new QNameURI(person.getQName()));
	}

	/**
	 * Check if a friend is already stored in the database
	 * 
	 * @param friend
	 * @return
	 */
	private boolean hasFriendInDatabase(QName friend) {
		SesameManager sesame = connection.getSesame().getLocalStore().createManager();
		SesameEntity result = sesame.find(friend);
		return result != null;
	}

	/**
	 * Check if a friend is already stored in the gate connector
	 * 
	 * @param friend
	 * @return
	 */
	private boolean hasFriendInGate(QName friend) {
		List<QNameURI> friends_gate = getFriendsFromGate();

		/* check all gate friends if there is a match */
		for (QNameURI nameURI : friends_gate) {
			if (nameURI.equalsQName(friend)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return all friends that are stored in the gate connector
	 */
	public List<QNameURI> getFriendsFromGate() {
		return connection.getGate().getFriendsURIs();
	}

	public void deleteFriend(Person person) throws ConnectorException {
		connection.getGate().deleteFriend(new QNameURI(person.getQName()));
		deleteFriendFromDatabase(person);
	}

	/**
	 * Adds the given QName to the Database and the Gate Connector if not
	 * already done.
	 * 
	 * @param newfriend
	 */
	public Person addFriend(QNameURI newfriend) {
		if (!hasFriendInGate(newfriend.toQName())) {
			addFriendGate(newfriend);
		}

		return addFriendDatabase(newfriend);
	}

	private void addFriendGate(QNameURI newfriend) {
		try {
			connection.getGate().addFriend(newfriend);
		} catch (ConnectorException e) {
			LOGGER.log(Level.WARNING, "could not add " + newfriend.toString() + " to the gate connector", e);
		}
	}

	private Person addFriendDatabase(QNameURI newfriend) {
		ElmoManager sesame = getLocalUser().getElmoManager();
		Person friend = sesame.designate(newfriend.toQName(), Person.class);
		getLocalUser().getFoafKnows().add(friend);
		friend.setFoafNicks(SimpleSet.create(guessNick(newfriend)));
		return friend;
	}

	private void deleteFriendFromDatabase(Person p) {
		getLocalUser().getFoafKnows().remove(p);
	}

	public void addListener(FriendStatusListener listener) {
		listeners.add(listener);
	}

	public boolean removeListener(FriendStatusListener listener) {
		return listeners.remove(listener);
	}

	/**
	 * @param uri
	 * @param status
	 */
	public void fireStatusUpdate(QNameURI uri, Status status) {
		for (FriendStatusListener listener : listeners) {
			listener.onlineStatusChanged(uri, status);
		}
	}

	/**
	 * @param friend
	 * @return a value 0 < value < 2 and 1 as default
	 */
	public double getDistanceToFriend(Person friend) {
		/* we remember, there is multiple inheritance in rdfs ... */
		if (friend instanceof DistantEntity) {
			return ((DistantEntity) friend).getDistance();
		} else {
			return 1;
		}
	}
	
	/**
	 * Sets the distance to the friend
	 * @param friend the friend. If it isn't an instance of {@link DistantEntity} yet, it will be designated.
	 * @param distance a value between 0 and 2
	 */
	protected void setDistanceToFriend(Person friend, double distance) {
		
		if(distance < 0 || distance > 2) {
			throw new IllegalArgumentException("Distance must not be less that zero or more than two!");
		}
		
		DistantEntity df;
		if(friend instanceof DistantEntity) {
			df = (DistantEntity) friend;
		} else {
			df = friend.getElmoManager().designateEntity(friend,DistantEntity.class);
		}
		
		df.setDistance(distance);
	}
	
	/**
	 * @param person
	 * @return the average rating of all items from the given person
	 */
	public double getRatingFor(Person person) {
		RateingManager rating = connection.getRatingManager();
		
		List<Rateing> ratingiterator = rating.getRatings(person);
		double sum_of_user_rating = 0;
		double num_of_user_rating = 0;

		for (Rateing rateing : ratingiterator) {
			
			/* validity check */
			if(Math.abs(rateing.getRateing()) > 2) {
				LOGGER.warning("Unexpected rateing value: "+ rateing.getRateing());
			}
			
			sum_of_user_rating += rateing.getRateing();
			num_of_user_rating++;
		}

		return sum_of_user_rating / num_of_user_rating;
	}

	/**
	 * Updates the distance information for each friend basing on the available ratings
	 */
	public void calculateDistances() {

		/* put all friends in one array */
		int friendssize = localAgent.getFoafKnows().size();
		Person[] userarray = new Person[friendssize];
		userarray = localAgent.getFoafKnows().toArray(userarray);

		Double[] userratingarray = new Double[friendssize];
		double sum_of_all_rateings = 0; // the overall rating sum
		double number_of_all_rateings = 0; // the overall rating count
		
		double minimum = Double.MAX_VALUE;
		double maximum = Double.MIN_VALUE;

		/* iterate over all persons and summarizer their ratings */
		for (int i = 0; i < userarray.length; i++) {

			userratingarray[i] = getRatingFor(userarray[i]);
			sum_of_all_rateings += userratingarray[i];
			number_of_all_rateings++;

			/* assign new minimum value */
			minimum = Math.min(minimum,userratingarray[i]);
			
			/* assign new maximum value */
			maximum = Math.max(maximum,userratingarray[i]);
			
			LOGGER.fine("Rateing for user " + getRepresentationOf(userarray[i]) + "=" + userratingarray[i]);
		}

		/*
		 * situation: sum_of_all_rateings is a natural number between -00 and
		 * +00 number_of_all_rateings is between 0 and +00
		 * 
		 * we will first calculate the average. This value will be mapped to 1.
		 * Everything else will be mapped in a way that it fits in the interval
		 * 0...1...2
		 */

		double avg_of_all_rateings = sum_of_all_rateings / number_of_all_rateings;
		
		/* set to zero on average */
		for (int i=0; i<userratingarray.length;i++) {
			userratingarray[i] -= avg_of_all_rateings;
		}
		
		/* also adjust min and max values */
		maximum -= avg_of_all_rateings;
		minimum -= avg_of_all_rateings;
		
		/* normalize to values between -1...0...+1 */
		double factor = Math.max(Math.abs(minimum),Math.abs(minimum));

		for (int i=0; i<userratingarray.length;i++) {
			double distance = userratingarray[i] / factor;
			setDistanceToFriend(userarray[i], distance);
		}
	}

	/**
	 * retrieves new Information about the given Person. Does not block
	 * 
	 * @param person
	 * @param listener
	 */
	public void requestInformationFor(final Person person, final EntityUpdatedListener<Person> listener) {
		final QNameURI person_uri = new QNameURI(person.getQName());
		AbstractQueryResult<Object> qlistener = new AbstractQueryResult<Object>() {

			@Override
			public void fireNewResults(Object response) {}

			@Override
			public void fireResultFinished() {
				/*
				 * copy all user-specific results (subject = person_uri) to the
				 * local store
				 */
				try {
					/*
					 * synchronizer between the temporal context and the local
					 * store
					 */
					// TODO: include children!
					Synchronizer sync = new Synchronizer(getContextWrapper(), connection.getSesame().getLocalStore());
					sync.replaceStatementsRecursive(person_uri);
				} catch (RepositoryException e) {
					LOGGER.log(Level.SEVERE, "autogenerated catch-block", e);
				}

				listener.updated(person);
			}

			@Override
			public void updatePercentage(int i) {}

		};

		QueryInterface q = new SparqlUserInformationQuery(person_uri);
		connection.runQuery(q, qlistener);
	}
}
