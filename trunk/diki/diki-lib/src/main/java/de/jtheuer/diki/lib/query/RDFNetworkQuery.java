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

import java.io.*;
import java.util.Iterator;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openrdf.concepts.foaf.Agent;
import org.openrdf.elmo.sesame.SesameManager;
import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.ntriples.NTriplesWriter;
import org.paceproject.diki.elmo.*;

import de.jtheuer.diki.lib.NetworkConnection;
import de.jtheuer.sesame.*;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * 
 */
public class RDFNetworkQuery extends AbstractNetworkQuery<String> {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(RDFNetworkQuery.class.getName());

	/**
	 * @param connection
	 * @param executor
	 * @param result
	 * @throws QueryException
	 */
	public RDFNetworkQuery(NetworkConnection connection, QueryResultListener<String> result) throws QueryException {
		super(null,connection, result);
	}

	/**
	 * @param originalQuery
	 * @param connection
	 * @param result
	 * @param child
	 */
	protected RDFNetworkQuery(QueryInterface originalQuery, NetworkConnection connection, QueryResultListener<String> result, SesameContextWrapper child,QNameURI queryID,Executor executor) {
		super(originalQuery, connection, result, child, queryID, executor);
	}

	/**
	 * sets the query for this {@link RDFNetworkQuery}.
	 * 
	 * @param message
	 * @throws QueryException
	 * @returns true if the message contained a query, otherwise false
	 */
	public boolean setQuery(String message) throws QueryException {
		try {
			getSesameContext().add(new StringReader(message), "", RDFFormat.NTRIPLES);
			
			/* search the QueryID in the added query */
			Iterator<Query> i = getSesameContext().createManager().findAll(Query.class).iterator();
			if(i.hasNext()) {
				setQueryID(new QNameURI(i.next().getQName()));
				return true;
			} else {
				return false;
			}
			
		} catch(RDFParseException e) {
			LOGGER.log(Level.WARNING,"unparsable query:" + e.getMessage());
			return false;
		} catch (RepositoryException e) {
			throw new QueryException(e);
		} catch (IOException e) {
			throw new QueryException(e);
		}
	}

	@Override
	public String getQueryString() {
		return getElmoQuery().getSparql();
	}

	@Override
	public void addResponse(String response) throws QueryException {
		result.fireNewResults(response);
	}

	@Override
	public void addResponse(QueryResponse response) throws QueryException {
		//TODO: remove query from context?
		result.fireNewResults(getSesameContext().dump());
	}

	/**
	 * stores a new End-Marker in the repository
	 * 
	 * @return the EndMarker
	 * @throws QueryException
	 */
	public String createEnd() throws QueryException {
		SesameManager manager = getSesameContext().createManager();
		QNameURI endid = getQueryID().createNewLocalPart("end");
		QueryEnd end = manager.designate(QueryEnd.class, endid.toQName());
		
		/* set parameters */
		end.setAnswersQuery(getElmoQuery());
		end.setCreatedBy(SimpleSet.create((Agent)getConnection().getUserFactory().getLocalUser()));
		
		StringWriter writer = new StringWriter();
		try {
			getSesameContext().export(new NTriplesWriter(writer), endid);
			return writer.toString();
		} catch (Exception e) {
			throw new QueryException(e);
		}
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
			RDFNetworkQuery q = new RDFNetworkQuery(getOriginalQuery(), getConnection(), getResultListener(), child, getQueryID(),getExecutor());
			q.setExecutor(getExecutor());
			return q;
		} catch (Exception e) {
			throw new QueryException(e);
		}
	}

}
