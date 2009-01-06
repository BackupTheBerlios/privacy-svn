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
package de.jtheuer.diki.lib.connectors.gate;

import java.util.Iterator;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.openrdf.concepts.foaf.Agent;
import org.openrdf.elmo.sesame.SesameManager;
import org.paceproject.diki.elmo.Query;
import org.paceproject.diki.elmo.QueryEnd;

import de.jtheuer.diki.lib.query.NetworkQuery;
import de.jtheuer.diki.lib.query.QueryException;
import de.jtheuer.sesame.*;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * 
 * This class is for sending queries and receiving responses!
 */
public abstract class Conversation {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(Conversation.class.getName());

	private NetworkQuery query;
	private Semaphore semaphore = new Semaphore(1);
	private QNameURI to;

	/**
	 * Creates a new MessageTracker.
	 * 
	 * @param query
	 *            The Query that will be sent to the user
	 * @param subqueryID
	 *            a unique thread identifier
	 */
	public Conversation(NetworkQuery networkQuery, QNameURI to) {
		this.query = networkQuery;
		this.to = to;
		/* initial block */
		semaphore.acquireUninterruptibly();
	}

	public final void sendQuery() throws GateCommunicationException {
		try {
			sendQuery(query.getRDFQuery());
		} catch (QueryException e) {
			throw new GateCommunicationException(e);
		}
	}

	protected abstract void sendQuery(String message) throws GateCommunicationException;

	protected void processQueryResultMessage(String message) throws GateCommunicationException {

		/* add result to context */
		SesameManager manager = query.getSesameContext().createManager();
		Query originalQuery = query.getElmoQuery();
		Agent partner = manager.designate(Agent.class, to.toQName());
		try {
			query.addResponse(message);

			/* check if there is an end document from our partner that answers this query */
			Iterator<QueryEnd> iterator = manager.findAll(QueryEnd.class).iterator();
			while(iterator.hasNext()) {
				QueryEnd end = iterator.next();
				/* check if queries are similar */
				if(end.getAnswersQuery().equals(originalQuery)) {
					/* check if creator matches */
					if(end.getCreatedBy().contains(partner)) {
						fireQueryFinished();
						semaphore.release();
					}
				}
			}
		} catch (QueryException e) {
			throw new GateCommunicationException(e);
		} finally {
			manager.close();
		}

	}

	/**
	 * Release all resources, Conversation has ended... This is called if the
	 * query ended gracefully
	 */
	protected void fireQueryFinished() {

	}

	/**
	 * Overwrite it to release all resources. Called if the blockUntilFinished
	 * method timed out
	 */
	protected void terminateQuery() {

	}

	/**
	 * blocks until the chat recipient sent the end package or the timeout
	 * accurs.
	 * 
	 * @param timeout
	 *            Timeout in seconds
	 * @throws InterruptedException
	 */
	public boolean blockUntilFinisched(int timeout) throws InterruptedException {
		if (semaphore.tryAcquire(timeout, TimeUnit.SECONDS)) {
			return true;
		} else {
			LOGGER.fine("timeout of " + timeout + " seconds exceeded for [" + query.getQueryID().toString() + "]");
			terminateQuery();
			return false;
		}
	}

	/**
	 * @return the identifier of the current thread. It is the same as the
	 *         associated {@link SesameContextWrapper}
	 */
	protected QNameURI getSubqueryID() {
		return query.getSesameContext().getQName();
	}

}
