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
import java.util.Collection;
import java.util.logging.Logger;

import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;

import de.jtheuer.diki.lib.friends.FriendStatusListener;
import de.jtheuer.diki.lib.friends.UserController;
import de.jtheuer.diki.lib.friends.FriendStatusListener.Status;
import de.jtheuer.sesame.QNameURI;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 */
public class XMPPRosterListener implements RosterListener {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(XMPPRosterListener.class.getName());
	private UserController catalogue;
	private XMPPConnector connector;

	/**
	 * 
	 */
	public XMPPRosterListener(UserController catalogue, XMPPConnector connector) {
		if(catalogue == null || connector == null) {
			throw new IllegalArgumentException("You must not supply null in the constructor!");
		}
		this.catalogue = catalogue;
		this.connector = connector;
	}
	
	/* (non-Javadoc)
	 * @see org.jivesoftware.smack.RosterListener#entriesAdded(java.util.Collection)
	 */
	@Override
	public void entriesAdded(Collection<String> addresses) {}

	/* (non-Javadoc)
	 * @see org.jivesoftware.smack.RosterListener#entriesDeleted(java.util.Collection)
	 */
	@Override
	public void entriesDeleted(Collection<String> addresses) {}

	/* (non-Javadoc)
	 * @see org.jivesoftware.smack.RosterListener#entriesUpdated(java.util.Collection)
	 */
	@Override
	public void entriesUpdated(Collection<String> addresses) {}

	/* (non-Javadoc)
	 * @see org.jivesoftware.smack.RosterListener#presenceChanged(org.jivesoftware.smack.packet.Presence)
	 */
	@Override
	public void presenceChanged(Presence presence) {
		String bare = StringUtils.parseBareAddress(presence.getFrom());
		QNameURI uri = connector.toURI(bare);
		Status status = presence.isAvailable() ? FriendStatusListener.Status.ONLINE : FriendStatusListener.Status.OFFLINE;
		catalogue.fireStatusUpdate(uri,status);
	}
}
