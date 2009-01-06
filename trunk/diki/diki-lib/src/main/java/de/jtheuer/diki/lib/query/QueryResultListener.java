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

import org.paceproject.diki.elmo.Query;
import org.paceproject.diki.elmo.QueryResponse;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * 
 * A resultListener is informes whenever a new {@link QueryResponse} is available.
 */
public interface  QueryResultListener<T> {
	
	/**
	 * @return the query where this listener belongs to
	 */
	public abstract Query getQuery();
	
	/**
	 * Sets the query (should only be done internally)
	 */
	public abstract void setQuery(AbstractNetworkQuery<T> query);
	
	/**
	 * @param response the new response that has been added
	 */
	public abstract void fireNewResults(T response);


	/**
	 * @param the current part of 100 that has already been solved
	 */
	public abstract void updatePercentage(int i);


	/**
	 * All queryresults are collected (or the query timed out)
	 */
	public abstract void fireResultFinished();
	
}
