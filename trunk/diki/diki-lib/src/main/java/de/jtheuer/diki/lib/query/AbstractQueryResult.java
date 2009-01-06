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

import org.openrdf.elmo.sesame.SesameManager;
import org.paceproject.diki.elmo.Query;

import de.jtheuer.sesame.SimpleContext;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 * Abstract class that factory some generic methods out.
 */
public abstract class AbstractQueryResult<T> implements QueryResultListener<T> {

	private AbstractNetworkQuery<T> query;


	/* (non-Javadoc)
	 * @see de.jtheuer.diki.lib.query.QueryResultListener#getQuery()
	 */
	public Query getQuery() {
		SesameManager manager = query.getSesameContext().createFullContext().createManager();
		return manager.find(Query.class,query.getQueryID().toQName());
	}
	
	public SimpleContext getContextWrapper() {
		return query.getSesameContext().createFullContext();
	}

	/* (non-Javadoc)
	 * @see de.jtheuer.diki.lib.query.QueryResultListener#setQuery(de.jtheuer.diki.lib.query.AbstractNetworkQuery)
	 */
	public void setQuery(AbstractNetworkQuery<T> query) {
		this.query = query;
	}

}