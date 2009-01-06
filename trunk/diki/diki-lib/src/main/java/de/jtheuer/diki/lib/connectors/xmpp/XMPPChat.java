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

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * 
 */
public class XMPPChat {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(XMPPChat.class.getName());

	public static final XMPPChat CLOSED = new ClosedChat();

	private XMPPConnector xmpp;
	private String threadid;
	private String to;
	private PacketListener listener;

	protected XMPPChat() {}

	public XMPPChat(XMPPConnector xmpp, String threadid, String to, PacketListener listener) {
		super();
		this.xmpp = xmpp;
		this.threadid = threadid;
		this.to = to;
		this.listener = listener;
	}

	public void sendMessage(String message) {
		Message xm = new Message();
		xm.setProperty("X-dikimessage", "true");
		xm.setThread(threadid);
		xm.setBody(message);
		xm.setFrom(xmpp.getUser());
		xm.setTo(to);
		xm.setType(Message.Type.chat);
		xmpp.sendPacket(xm);
	}

	public void close() {
		LOGGER.fine("Closeing chat [" + threadid + "]");
		xmpp.closeChat(this);
	}

	/**
	 * @param message
	 */
	public void processMessage(Message message) {
		if (listener != null) {
			listener.processPacket(message);
		} else {
			LOGGER.warning("Listener not set, yet");
		}

	}

	public String getThreadid() {
		return threadid;
	}

	public String getTo() {
		return to;
	}

	/**
	 * @param listener
	 */
	public void setListener(PacketListener listener) {
		this.listener = listener;
	}

	public static class ClosedChat extends XMPPChat {

	}
}
