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

import javax.xml.namespace.QName;

import org.paceproject.diki.elmo.QueryResponse;

import de.jtheuer.sesame.QNameURI;
import de.jtheuer.sesame.SesameContextWrapper;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 * A network Query extends a Query to allow additional operations that are necessary to solve
 * the query. This is seperated from the {@link AbstractQuery} because these operations cannot be supplied 
 * at the time the {@link AbstractQuery} is constructed.
 */
public interface NetworkQuery extends QueryInterface {

	/**
	 * a convenience method
	 * @return a unique query identifier
	 */
	public QNameURI getQueryID();
	
	/**
	 * @return the elmo Query representing this NetworkQuery
	 */
	public org.paceproject.diki.elmo.Query getElmoQuery();

	/**
	 * @return the rdf Query representing this NetworkQuery
	 * @throws QueryException 
	 */
	public String getRDFQuery() throws QueryException;

	
	/**
	 * @return the original query this query was created from.
	 */
	public QueryInterface getOriginalQuery();

	/**
	 * It is important to run threads on this {@link Executor} because it automatically
	 * gives feedback to the gui how many threads are left
	 * 
	 * @return the executor that is responsible for this query.
	 */
	public Executor getExecutor();

	
	/**
	 * @return the sesame context for this sepecific query
	 */
	public SesameContextWrapper getSesameContext();
	
	/**
	 * Queries that are forwarded my have to modify the original query to change the distance, etc. Therefore this method provides a
	 * copy of the sesame context that can safely be changed.
	 * @return a copy of the sesame-context.
	 * @throws QueryException 
	 */
	public NetworkQuery createChildQuery() throws QueryException;
	
	/**
	 * creates a new {@link QueryResponse} instance that can be filled.
	 * It must be added by the addResult method, later.
	 * @param a valid namespace or null. If null then the namespace will be generated
	 * @return
	 */
	public QueryResponse createResponse(QName namespace);
	
	/**
	 * creates a new {@link QueryResponse} instance that can be filled.
	 * @return
	 */
	public QueryResponse createResponse();
	
	/**
	 * Adds a QueryResponse to the NetworkQuery.
	 * @param response
	 * @throws QueryException 
	 */
	public void addResponse(String response) throws QueryException;

	/**
	 * Adds a QueryResponse to the NetworkQuery
	 * @param response
	 * @throws QueryException
	 */
	public void addResponse(QueryResponse response) throws QueryException;
	
	/**
	 * @return true if a QueryEnd Statement is in the repository
	 */
	public boolean containsEnd();
	
	/**
	 * Closes the Query and removes all resources. 
	 * @throws QueryException 
	 */
	public void close() throws QueryException;
}
