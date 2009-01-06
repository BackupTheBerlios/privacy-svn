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
	
package de.jtheuer.diki.lib.connectors;
import java.awt.Image;
import java.util.logging.Logger;

import de.jtheuer.diki.lib.NetworkConnection;
import de.jtheuer.diki.lib.query.NetworkQuery;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 */
public abstract class AbstractSimpleConnector extends AbstractConnector implements Connector {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(AbstractSimpleConnector.class.getName());

	public AbstractSimpleConnector(NetworkConnection connection) {
		super(connection);
	}
	
	/**
	 * A new {@link AbstractSimpleConnector} with the given name and icon
	 * @param name
	 * @param icon
	 * @param connection
	 */
	public AbstractSimpleConnector(String name, Image icon, NetworkConnection connection) {
		super(name,icon,connection);
	}
	
	/* (non-Javadoc)
	 * @see de.jtheuer.diki.lib.connectors.Connector#evaluate(de.jtheuer.diki.lib.query.NetworkQuery)
	 */
	@Override
	public void evaluate(NetworkQuery query) {
		query.getExecutor().execute(innerEvaluate(query));
	}

	/**
	 * called by {@link AbstractSimpleConnector#evaluate(NetworkQuery)}.
	 * 
	 * @param networkQuery
	 * @return
	 */
	protected abstract Runnable innerEvaluate(NetworkQuery networkQuery);

}
