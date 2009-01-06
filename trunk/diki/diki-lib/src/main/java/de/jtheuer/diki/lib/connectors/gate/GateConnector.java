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
import java.util.List;

import de.jtheuer.diki.lib.connectors.Connector;
import de.jtheuer.diki.lib.connectors.ConnectorException;
import de.jtheuer.diki.lib.query.NetworkQuery;
import de.jtheuer.sesame.QNameURI;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * 
 * A GateConnector gets queries and responses and is in charge of managing them.
 * 
 * Implementations MAY provide the getFriends or addFriend methods or MUST return empty lists.
 *
 */
public interface GateConnector<T> extends Connector {
	
	
	public abstract Runnable forwardQueryTo(QNameURI to, NetworkQuery query);
	
	/**
	 * @return a list of friends (if supported)
	 */
	public abstract List<T> getFriends();
	
	/**
	 * adds a friend to the connector (if supported)
	 * @param friend
	 */
	public abstract void addFriend(QNameURI friend) throws ConnectorException;

	/**
	 * @return a list of friends as uris
	 */
	public abstract List<QNameURI> getFriendsURIs();
	
	/**
	 * @return a uri derrived from the user's account
	 */
	public abstract String getUserNamespace();

	/**
	 * @param friend
	 * @throws ConnectorException
	 */
	void deleteFriend(QNameURI friend) throws ConnectorException;

	/**
	 * @param username
	 */
	public abstract boolean isOnline(QNameURI username);

}
