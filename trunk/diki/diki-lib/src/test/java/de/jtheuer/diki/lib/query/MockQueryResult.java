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
import java.util.logging.Logger;

import org.paceproject.diki.elmo.Query;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 */
public class MockQueryResult implements QueryResultListener<Object> {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(MockQueryResult.class.getName());

	/* (non-Javadoc)
	 * @see de.jtheuer.diki.lib.query.QueryResultListener#fireNewResults(java.lang.Object)
	 */
	@Override
	public void fireNewResults(Object response) {}

	/* (non-Javadoc)
	 * @see de.jtheuer.diki.lib.query.QueryResultListener#fireResultFinished()
	 */
	@Override
	public void fireResultFinished() {}

	/* (non-Javadoc)
	 * @see de.jtheuer.diki.lib.query.QueryResultListener#getQuery()
	 */
	@Override
	public Query getQuery() {
		return null;
	}

	/* (non-Javadoc)
	 * @see de.jtheuer.diki.lib.query.QueryResultListener#updatePercentage(int)
	 */
	@Override
	public void updatePercentage(int i) {}

	/* (non-Javadoc)
	 * @see de.jtheuer.diki.lib.query.QueryResultListener#setQuery(de.jtheuer.diki.lib.query.AbstractNetworkQuery)
	 */
	@Override
	public void setQuery(AbstractNetworkQuery<Object> query) {}

}
