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

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

import de.jtheuer.diki.lib.connectors.gate.Conversation;
import de.jtheuer.diki.lib.connectors.gate.GateCommunicationException;
import de.jtheuer.diki.lib.query.NetworkQuery;
import de.jtheuer.sesame.QNameURI;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * 
 */
public class XMPPConversation extends Conversation implements PacketListener {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(XMPPConversation.class.getName());
	private XMPPChat chat;

	/**
	 * @param query
	 * @param threadID
	 */
	public XMPPConversation(NetworkQuery container, XMPPChat chat, QNameURI to) {
		super(container,to);
		this.chat = chat;
		chat.setListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jtheuer.diki.lib.connectors.gate.Conversation#sendQuery(java.lang.String)
	 */
	@Override
	public void sendQuery(String message) throws GateCommunicationException {
		LOGGER.fine("[chat: [" + chat.getTo() + "] threadid [" + getSubqueryID().toString() + "] sending message: " + message);
		chat.sendMessage(message);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jivesoftware.smack.PacketListener#processPacket(org.jivesoftware.smack.packet.Packet)
	 */
	@Override
	public void processPacket(Packet packet) {
		if (packet instanceof Message) {
			processMessage((Message) packet);
		} else {
			LOGGER.info("thread [" + getSubqueryID().toString() + "] " + packet.toString());
		}
	}

	/**
	 * Processes te incomming message. First checks if it is from the correct opponent.
	 * @param message
	 */
	private void processMessage(Message message) {
		if (getSubqueryID().toString().equals(message.getThread())) {
			LOGGER.fine("response from " + message.getFrom() + "Content: " + message.getBody());
			try {
				if (message.getFrom().startsWith(chat.getTo())) {
					super.processQueryResultMessage(message.getBody());
				} else {
					LOGGER.warning("discard message from: " + message.getFrom() + ". Expected: " + chat.getTo());
				}

			} catch (GateCommunicationException e) {
				LOGGER.warning(e.getCause().getMessage());
				chat.sendMessage(e.getCause().getMessage());
			}
		} else {
			LOGGER.warning("wired: wrong threadid! Expected: " + getSubqueryID().toString());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jtheuer.diki.lib.connectors.gate.Conversation#fireFinished()
	 */
	@Override
	protected void terminateQuery() {
		this.chat.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jtheuer.diki.lib.connectors.gate.Conversation#fireQueryFinished()
	 */
	@Override
	protected void fireQueryFinished() {
		terminateQuery();
	}

}
