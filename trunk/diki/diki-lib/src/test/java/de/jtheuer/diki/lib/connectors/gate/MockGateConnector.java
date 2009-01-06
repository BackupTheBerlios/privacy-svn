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
import java.awt.Image;
import java.util.*;
import java.util.logging.Logger;

import de.jtheuer.diki.lib.connectors.ConnectorException;
import de.jtheuer.diki.lib.connectors.ParameterProperties;
import de.jtheuer.diki.lib.query.NetworkQuery;
import de.jtheuer.sesame.QNameURI;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 */
public class MockGateConnector implements GateConnector<String> {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(MockGateConnector.class.getName());

	/* (non-Javadoc)
	 * @see de.jtheuer.diki.lib.connectors.gate.GateConnector#addFriend(de.jtheuer.sesame.QNameURI)
	 */
	@Override
	public void addFriend(QNameURI friend) throws ConnectorException {}

	/* (non-Javadoc)
	 * @see de.jtheuer.diki.lib.connectors.gate.GateConnector#deleteFriend(de.jtheuer.sesame.QNameURI)
	 */
	@Override
	public void deleteFriend(QNameURI friend) throws ConnectorException {}

	/* (non-Javadoc)
	 * @see de.jtheuer.diki.lib.connectors.gate.GateConnector#forwardQueryTo(java.lang.Object, de.jtheuer.diki.lib.query.NetworkQuery)
	 */
	@Override
	public Runnable forwardQueryTo(QNameURI to, NetworkQuery query) {
		return null;
	}

	/* (non-Javadoc)
	 * @see de.jtheuer.diki.lib.connectors.gate.GateConnector#getFriends()
	 */
	@Override
	public List<String> getFriends() {
		return Collections.emptyList();
	}

	/* (non-Javadoc)
	 * @see de.jtheuer.diki.lib.connectors.gate.GateConnector#getFriendsURIs()
	 */
	@Override
	public List<QNameURI> getFriendsURIs() {
		return Collections.emptyList();
	}

	/* (non-Javadoc)
	 * @see de.jtheuer.diki.lib.connectors.gate.GateConnector#getUserNamespace()
	 */
	@Override
	public String getUserNamespace() {
		return "urn:junit/local";
	}

	/* (non-Javadoc)
	 * @see de.jtheuer.diki.lib.connectors.gate.GateConnector#isOnline(de.jtheuer.sesame.QNameURI)
	 */
	@Override
	public boolean isOnline(QNameURI username) {
		return false;
	}

	/* (non-Javadoc)
	 * @see de.jtheuer.diki.lib.connectors.Connector#connect(java.util.Properties)
	 */
	@Override
	public void connect(Properties prop) throws ConnectorException {}

	/* (non-Javadoc)
	 * @see de.jtheuer.diki.lib.connectors.Connector#disconnect()
	 */
	@Override
	public void disconnect() {}

	/* (non-Javadoc)
	 * @see de.jtheuer.diki.lib.connectors.Connector#distance()
	 */
	@Override
	public double distance() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see de.jtheuer.diki.lib.connectors.Connector#evaluate(de.jtheuer.diki.lib.query.NetworkQuery)
	 */
	@Override
	public void evaluate(NetworkQuery query) {}

	/* (non-Javadoc)
	 * @see de.jtheuer.diki.lib.connectors.Connector#getIcon()
	 */
	@Override
	public Image getIcon() {
		return null;
	}

	/* (non-Javadoc)
	 * @see de.jtheuer.diki.lib.connectors.Connector#getName()
	 */
	@Override
	public String getName() {
		return "junit-mock";
	}

	/* (non-Javadoc)
	 * @see de.jtheuer.diki.lib.connectors.Connector#getParameters()
	 */
	@Override
	public Hashtable<Object, Object> getParameters() {
		return null;
	}

	/* (non-Javadoc)
	 * @see de.jtheuer.diki.lib.connectors.Connector#getProperties()
	 */
	@Override
	public ParameterProperties getProperties() {
		return null;
	}

	/* (non-Javadoc)
	 * @see de.jtheuer.diki.lib.connectors.Connector#getStatus()
	 */
	@Override
	public Status getStatus() {
		return Status.Connected;
	}
}
