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
import java.util.Set;
import java.util.logging.Logger;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.StringUtils;
import org.paceproject.diki.elmo.Query;

import de.jtheuer.diki.lib.NetworkConnection;
import de.jtheuer.diki.lib.connectors.gate.AbstractGateQueryResultListener;
import de.jtheuer.sesame.QNameURI;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 * This class collects all query results from friends
 */
public class XMPPQueryResult extends AbstractGateQueryResultListener implements PacketListener {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(XMPPQueryResult.class.getName());
	
	private XMPPChat chat;

	private XMPPConnector xmpp;
	
	public XMPPQueryResult(XMPPConnector xmpp, XMPPChat chat, NetworkConnection connection, Set<QNameURI> querySet) {
		super(connection, querySet);

		this.xmpp = xmpp;
		this.chat = chat;
		this.chat.setListener(this);
	}

	/* (non-Javadoc)
	 * @see de.jtheuer.diki.lib.query.result.QueryResult#finished()
	 */
	@Override
	public void fireResultFinished() {
		super.fireResultFinished();
		this.chat.close();
	}
	

	/* (non-Javadoc)
	 * @see org.jivesoftware.smack.PacketListener#processPacket(org.jivesoftware.smack.packet.Packet)
	 */
	@Override
	public void processPacket(Packet packet) {
		String from = StringUtils.parseName(packet.getFrom()) + "@"+ StringUtils.parseServer(packet.getFrom());
		QNameURI uri = xmpp.toURI(from);
		if(packet instanceof Message) {
			processIncommingMessage(((Message) packet).getBody(), uri);
		}
	}

	/* (non-Javadoc)
	 * @see de.jtheuer.diki.lib.connectors.gate.AbstractGateQueryResult#send(java.lang.String)
	 */
	@Override
	protected void send(String message) {
		LOGGER.fine("Sending message to " + chat.getTo() + ": " + message);
		this.chat.sendMessage(message);
	}

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
}
