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

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.QName;

import org.openrdf.elmo.ElmoManager;
import org.openrdf.elmo.sesame.SesameManager;
import org.openrdf.query.QueryLanguage;
import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.ntriples.NTriplesWriter;
import org.paceproject.diki.elmo.*;

import de.jtheuer.diki.lib.NetworkConnection;
import de.jtheuer.sesame.*;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * 
 * abstract implementation of the {@link NetworkQuery}.
 */
public abstract class AbstractNetworkQuery<T> implements NetworkQuery {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(AbstractNetworkQuery.class.getName());

	private NetworkConnection connection;
	private Executor executor;
	private QueryInterface originalQuery;
	protected QueryResultListener<T> result;
	private SesameContextWrapper contextStore;

	private QNameURI queryID;
	
	/**
	 * Instantiates a new {@link AbstractNetworkQuery}.
	 * @param query
	 * @param connection
	 * @param executer
	 * @param result
	 * @throws QueryException 
	 */
	public AbstractNetworkQuery(QueryInterface query, NetworkConnection connection, QueryResultListener<T> result) throws QueryException {
		this(query, connection, result, createContext(connection),null,null);
		result.setQuery(this);
	}
	
	/**
	 * For creating children
	 * @param query
	 * @param connection
	 * @param executor
	 * @param result
	 * @param contextStore
	 */
	protected AbstractNetworkQuery(QueryInterface query, NetworkConnection connection, QueryResultListener<T> result, SesameContextWrapper contextStore,QNameURI queryID, Executor executor) {
		this.originalQuery = query;
		this.connection = connection;
		this.result = result;
		this.contextStore = contextStore;
		this.executor = executor;
		/* check if we already have a queryID: then this is a subquery! */
		this.queryID = queryID != null? queryID: contextStore.getQName();
	}

	/**
	 * A wrapper to hide the RepositoryException
	 * @param connection2
	 * @return
	 * @throws QueryException 
	 */
	private static SesameContextWrapper createContext(NetworkConnection connection) throws QueryException {
		try {
			return connection.getQueryFactory().createQueryContext();
		} catch (RepositoryException e) {
			throw new QueryException(e);
		}
	}
	/* (non-Javadoc)
	 * @see de.jtheuer.diki.lib.query.NetworkQuery#createResponse()
	 */
	@Override
	public QueryResponse createResponse() {
		return createResponse(null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jtheuer.diki.lib.query.NetworkQuery#createResponse()
	 */
	@Override
	public QueryResponse createResponse(QName namespace) {
		if (namespace == null) {
			namespace = connection.getNamespaceFactory().createResultIdentifier().toQName();
		}
		ElmoManager manager = getSesameContext().createManager();
		QueryResponse qr = manager.designate(QueryResponse.class, namespace);
		Query q = getElmoQuery();
		qr.setDistance(q.getDistance());
		qr.setAnswersQuery(q);
		return qr;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jtheuer.diki.lib.query.NetworkQuery#getElmoQuery()
	 */
	@Override
	public Query getElmoQuery() {
		return getSesameContext().createManager().find(Query.class, queryID.toQName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jtheuer.diki.lib.query.NetworkQuery#getExecutor()
	 */
	@Override
	public Executor getExecutor() {
		return executor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jtheuer.diki.lib.query.NetworkQuery#getOriginalQuery()
	 */
	@Override
	public QueryInterface getOriginalQuery() {
		return originalQuery;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jtheuer.diki.lib.query.NetworkQuery#getQueryID()
	 */
	@Override
	public QNameURI getQueryID() {
		return queryID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jtheuer.diki.lib.query.AbstractQuery#getQueryLanguage()
	 */
	@Override
	public QueryLanguage getQueryLanguage() {
		return originalQuery.getQueryLanguage();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jtheuer.diki.lib.query.AbstractQuery#getQuery()
	 */
	@Override
	public String getQueryString() {
		return originalQuery.getQueryString();
	}

	/* (non-Javadoc)
	 * @see de.jtheuer.diki.lib.query.QueryInterface#isRecipient(de.jtheuer.diki.lib.util.QNameURI)
	 */
	@Override
	public boolean isRecipient(QNameURI recipient) {
		return originalQuery.isRecipient(recipient);
	}

	/* (non-Javadoc)
	 * @see de.jtheuer.diki.lib.query.NetworkQuery#getSesameContext()
	 */
	@Override
	public SesameContextWrapper getSesameContext() {
		return contextStore;
	}

	@Override
	public abstract NetworkQuery createChildQuery() throws QueryException;

	/* (non-Javadoc)
	 * @see de.jtheuer.diki.lib.query.NetworkQuery#getRDFQuery()
	 */
	@Override
	public String getRDFQuery() throws QueryException {
		StringWriter writer = new StringWriter();
		try {
			contextStore.export(new NTriplesWriter(writer), getQueryID());
		} catch (Exception e) {
			
			throw new QueryException("cannot get RDF query",e);
		}
		String s = writer.toString();
		
		/* for debugging .. */
		if(s.length() < "<xmpp:dikijan1@v-tml.uni-muenster.de/query/1219075792093/base/4> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://rdf.pace-project.org/diki#Query> .".length() * 2) {
			throw new AssertionError("query too short!");
		}
		return s;
	}

	
	/* (non-Javadoc)
	 * @see de.jtheuer.diki.lib.query.NetworkQuery#addResponse(java.lang.String)
	 */
	@Override
	public void addResponse(String response) throws QueryException {
		try {
			contextStore.add(new StringReader(response), "", RDFFormat.NTRIPLES);
			updateResponseReferences();
		} catch (Exception e) {
			throw new QueryException(e);
		}
	}


	/* (non-Javadoc)
	 * @see de.jtheuer.diki.lib.query.NetworkQuery#addResponse(org.paceproject.diki.elmo.QueryResponse)
	 */
	@Override
	public void addResponse(QueryResponse response) throws QueryException {
		Query query = getElmoQuery();
		SimpleSet<QueryResponse> newset = new SimpleSet<QueryResponse>(query.getHasResults(),response);
		query.setHasResults(newset);
	}
	
	/**
	 * Called after adding new responses to the repository: for each QueryResponse -> answersQuery -> Query triple it 
	 * generates the corresponding Query -> hasResult -> QueryResponse triple
	 */
	private void updateResponseReferences() {
		SesameManager manager = contextStore.createManager();
		
		Query query = getElmoQuery();
		
		Iterator<QueryResponse> response_iterator = manager.findAll(QueryResponse.class).iterator();
		while(response_iterator.hasNext()) {
			query.getHasResults().add(response_iterator.next());
		}
	}
	

	
	@Override
	public boolean containsEnd() {
		Query query = getElmoQuery();
		Iterator<QueryEnd> ends = contextStore.createManager().findAll(QueryEnd.class).iterator();
		while(ends.hasNext()) {
			QueryEnd end = ends.next();
			if(end.getAnswersQuery().equals(query) ) {
				return true;
			}
		}
		
		return false;
	}

	protected void setExecutor(Executor executor) {
		this.executor = executor;
	}

	public QueryResultListener<T> getResultListener() {
		return result;
	}

	protected NetworkConnection getConnection() {
		return connection;
	}
	
	protected void setQueryID(QNameURI uri) {
		this.queryID = uri;
	}

	@Override
	public void close() throws QueryException {
		try {
			getSesameContext().clearAll();
			getSesameContext().closeAll();
			
			// release dependency to the result - thus removing circular dependency 
			result = null;
		} catch (RepositoryException e) {
			LOGGER.log(Level.WARNING,"unable to free resources", e);
		}
	}
	
	
}
