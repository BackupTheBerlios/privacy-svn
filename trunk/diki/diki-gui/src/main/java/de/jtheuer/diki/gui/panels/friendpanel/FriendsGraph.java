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
package de.jtheuer.diki.gui.panels.friendpanel;

import java.awt.Image;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.QName;

import org.openrdf.concepts.foaf.Person;

import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.visual.VisualItem;
import de.jtheuer.diki.lib.EntityUpdatedListener;
import de.jtheuer.diki.lib.NetworkConnection;
import de.jtheuer.diki.lib.connectors.ConnectorException;
import de.jtheuer.diki.lib.friends.FriendStatusListener;
import de.jtheuer.jjcomponents.utils.ResourcesContainer;
import de.jtheuer.sesame.QNameURI;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * 
 */
/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * 
 */
public class FriendsGraph extends Graph implements FriendStatusListener {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(FriendsGraph.class.getName());
	private NetworkConnection connection;
	private Node node_myself;
	private HashMap<Person, Node> personhash = new HashMap<Person, Node>();
	private HashMap<QName, Node> qnamehash = new HashMap<QName, Node>();

	public static final Image OFFLINE = ResourcesContainer.CLOSE.getAsImage(16);
	public static final Image ONLINE = ResourcesContainer.OK.getAsImage(16);
	public static final Image FOF = ResourcesContainer.NEW.getAsImage(16);
	public static final Image RELOAD = ResourcesContainer.RESTART.getAsImage(16);
	

	/**
	 * A new Graph of direct friends
	 */
	public FriendsGraph(NetworkConnection connection) {
		this.connection = connection;
		connection.getUserFactory().addListener(this);
		/* init columns */
		addColumn(Constants.URI, String.class);
		addColumn(Constants.FIXED, String.class);
		addColumn(Constants.IMAGE, String.class);

	}

	/**
	 * initializes the graph
	 */
	public void init() {
		/* explicit add the local user */
		Person myself = connection.getUserFactory().getLocalUser();
		node_myself = newNode(myself, ONLINE.toString());
		node_myself.setString(Constants.FIXED, Constants.FIXED); // Only

		refreshFriends();
	}
	
	/**
	 * reads all Persons and their relationship from the database
	 */
	public void refreshFriends() {
		/* add all known persons */
		addAllPersons();
		addAllEdges();
	}

	/**
	 * reads all Persons from the local store
	 */
	private void addAllPersons() {
		for (Person person : connection.getUserFactory().getAllPersonsFromDatabase()) {
			newNode(person, getOnlineState(person));
		}
	}

	/**
	 * reads all foaf:knows properties from the local store
	 */
	private void addAllEdges() {
		for (Person person : connection.getUserFactory().getAllPersonsFromDatabase()) {
			for (Person friend : person.getFoafKnows()) {
				connectPersons(person, friend);
			}
		}
	}

	/**
	 * connects to previously added persons in the graph
	 * 
	 * @param p1
	 * @param p2
	 */
	private void connectPersons(Person p1, Person p2) {
		Node node1 = personhash.get(p1);
		Node node2 = personhash.get(p2);
		if (node1 != null && node2 != null) {
			addEdge(node1, node2);
		} else {
			LOGGER.warning("attemp to create edge between not-existing persons: " + p1.toString() + " and " + p2.toString());
		}
	}

	/**
	 * Adds a new node to the Graph if the node doesn't already exist
	 * 
	 * @param person
	 *            the Person
	 * @param onlineStatus
	 *            the current status
	 * @return the added node
	 */
	private Node newNode(Person person, String onlineStatus) {
		Node node = personhash.get(person);
		if (node == null) {
			node = addNode();
			node.setString(Constants.URI, " " + connection.getUserFactory().getRepresentationOf(person) + " ");
			node.setString(Constants.IMAGE, onlineStatus);
			personhash.put(person, node);
			qnamehash.put(person.getQName(), node);
		}
		return node;
	}

	@Deprecated
	private void addFriendTo(Node parent, Person person) {
		Node node_friend = personhash.get(person);
		if (node_friend == null) {
			/* create a new Node with the given online status */
			node_friend = newNode(person, getOnlineState(new QNameURI(person.getQName())));
		} else {
			LOGGER.fine("ignoring " + person.getQName() + " because a node already exists.");
		}
		/* check if an edge exist */
		if (getEdge(parent, node_friend) == null) {
			addEdge(parent, node_friend);
		}
	}

	/**
	 * reads the foaf:knows property of the supplied person and adds all of them
	 * to the graph
	 * 
	 * @param person
	 */
	@Deprecated
	public void addFriendsOf(Person person) {
		Set<Person> friends = person.getFoafKnows();
		Node node = personhash.get(person);
		if (node != null) {
			for (Person friend : friends) {
				addFriendTo(node, friend);
			}
		} else {
			LOGGER.severe("Cannot draw friends of a person that hasn't been added, yet!");
		}
	}

	/**
	 * add the friend to the local users friends. If the friend is already a
	 * friend of someone else, the edge is moved.
	 * 
	 * @param addedFriend
	 */
	@Deprecated
	public void addNewFriend(Person addedFriend) {
		Node node = personhash.get(addedFriend);
		if (node == null) {
			addFriendTo(node_myself, addedFriend);
		} else {
			addEdge(node_myself, node);
		}
	}

	private Person getPersonForVItem(VisualItem vitem) {
		for (Person p : personhash.keySet()) {
			if (personhash.get(p).getRow() == vitem.getRow()) {
				return p;
			}
		}

		return null;
	}

	public void deleteNode(VisualItem vitem) {
		for (Person p : personhash.keySet()) {
			if (personhash.get(p).getRow() == vitem.getRow()) {
				try {
					connection.getUserFactory().deleteFriend(p);
					personhash.remove(p);
					removeNode(vitem.getRow());
					return;
				} catch (ConnectorException e) {
					LOGGER.log(Level.SEVERE, "autogenerated catch-block", e);
				}
			}
		}
	}

	public void requestFriends(VisualItem item) {
		requestFriends(getPersonForVItem(item));
	}

	private void requestFriends(final Person friend) {
		if (friend == null) {
			LOGGER.warning("null was supplied as friend, ignoreing");
			return;
		}

		final Node parentnode = personhash.get(friend);
		// setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		EntityUpdatedListener<Person> listener = new EntityUpdatedListener<Person>() {

			@Override
			public void updated(Person person) {
				refreshFriends();
				parentnode.setString(Constants.IMAGE, getOnlineState(friend));
			}

		};
		parentnode.setString(Constants.IMAGE, RELOAD.toString());
		connection.getUserFactory().requestInformationFor(friend, listener);
	}

	private String getOnlineState(QNameURI person) {
		return connection.getUserFactory().isOnline(person) ? ONLINE.toString() : OFFLINE.toString();
	}
	
	private String getOnlineState(Person person) {
		return connection.getUserFactory().isOnline(person) ? ONLINE.toString() : OFFLINE.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jtheuer.diki.lib.friends.FriendStatusListener#onlineStatusChanged(de.jtheuer.diki.lib.util.QNameURI,
	 *      de.jtheuer.diki.lib.friends.FriendStatusListener.Status)
	 */
	@Override
	public void onlineStatusChanged(QNameURI friend, Status status) {
		Node node = qnamehash.get(friend.toQName());
		if (node != null) {
			node.set(Constants.IMAGE, status == Status.ONLINE ? ONLINE.toString() : OFFLINE.toString());
		}
	}

	/**
	 * @param currentitem
	 */
	public void info(VisualItem currentitem) {
		Person person = getPersonForVItem(currentitem);
		FoafInfoPanel.newWindow(person);
	}
}
