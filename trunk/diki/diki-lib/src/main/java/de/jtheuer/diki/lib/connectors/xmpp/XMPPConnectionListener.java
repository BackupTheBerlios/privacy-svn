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
	
package de.jtheuer.diki.lib.connectors.xmpp;
import java.util.logging.Logger;

import de.jtheuer.diki.lib.ConnectionListener;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 */
public class XMPPConnectionListener implements org.jivesoftware.smack.ConnectionListener {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(XMPPConnectionListener.class.getName());
	private ConnectionListener listener;

	/**
	 * @param connection
	 */
	public XMPPConnectionListener(ConnectionListener connection, XMPPConnector connector) {
		this.listener=connection;
	}

	/* (non-Javadoc)
	 * @see org.jivesoftware.smack.ConnectionListener#connectionClosed()
	 */
	@Override
	public void connectionClosed() {
		listener.fireStatusUpdate();
	}

	/* (non-Javadoc)
	 * @see org.jivesoftware.smack.ConnectionListener#connectionClosedOnError(java.lang.Exception)
	 */
	@Override
	public void connectionClosedOnError(Exception e) {
		listener.fireStatusUpdate();
	}

	/* (non-Javadoc)
	 * @see org.jivesoftware.smack.ConnectionListener#reconnectingIn(int)
	 */
	@Override
	public void reconnectingIn(int seconds) {}

	/* (non-Javadoc)
	 * @see org.jivesoftware.smack.ConnectionListener#reconnectionFailed(java.lang.Exception)
	 */
	@Override
	public void reconnectionFailed(Exception e) {}

	/* (non-Javadoc)
	 * @see org.jivesoftware.smack.ConnectionListener#reconnectionSuccessful()
	 */
	@Override
	public void reconnectionSuccessful() {
		listener.fireStatusUpdate();
	}
}
