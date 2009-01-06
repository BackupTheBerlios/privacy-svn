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
package de.jtheuer.diki.lib.query;

import java.util.concurrent.Executor;
import java.util.logging.Logger;

import org.openrdf.concepts.foaf.Agent;
import org.openrdf.elmo.ElmoManager;
import org.openrdf.repository.RepositoryException;
import org.paceproject.diki.elmo.Query;
import org.paceproject.diki.elmo.QueryResponse;

import de.jtheuer.diki.lib.NetworkConnection;
import de.jtheuer.sesame.*;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * 
 * A query that sends Concepts to the QueryResultListener
 */
public class ConceptNetworkQuery extends AbstractNetworkQuery<Object> implements NetworkQuery {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(ConceptNetworkQuery.class.getName());

	/**
	 * @param query
	 * @param result
	 * @param connection
	 * @throws QueryException
	 * @throws RepositoryException
	 */
	public ConceptNetworkQuery(QueryInterface query, QueryResultListener<Object> result, NetworkConnection connection, double max_distance)
			throws QueryException, RepositoryException {
		super(query, connection, result);

		ElmoManager manager = getSesameContext().createManager();
		Query elmoQuery;
		try {
			manager.getTransaction().begin();
			final QNameURI id = getSesameContext().getQName();
			final Agent agent = connection.getUserFactory().getLocalUser();

			elmoQuery = manager.designate(org.paceproject.diki.elmo.Query.class, id.toQName());
			elmoQuery.setSparql(query.getQueryString());
			elmoQuery.setCreatedBy(SimpleSet.create(agent));
			elmoQuery.setDistance(max_distance);

			manager.getTransaction().commit();
		} catch (Exception e) {
			manager.getTransaction().rollback();
			throw new QueryException("could not create query instance", e);

		}
	}

	private ConceptNetworkQuery(QueryInterface query, QueryResultListener<Object> result, NetworkConnection connection, SesameContextWrapper child, QNameURI queryID, Executor executor)
			throws QueryException {
		super(query, connection, result, child, queryID,executor);
	}

	@Override
	public void addResponse(QueryResponse response) throws QueryException {
		super.addResponse(response);
		result.fireNewResults(response);
	}

	@Override
	public void addResponse(String response) throws QueryException {
		super.addResponse(response);
		result.fireNewResults(response);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jtheuer.diki.lib.query.AbstractNetworkQuery#createChildQuery()
	 */
	@Override
	public NetworkQuery createChildQuery() throws QueryException {
		try {
			SesameContextWrapper child = getSesameContext().createChild();
			return new ConceptNetworkQuery(getOriginalQuery(), getResultListener(), getConnection(), child,getQueryID(),getExecutor());
		} catch (Exception e) {
			throw new QueryException(e);
		}
	}

}
