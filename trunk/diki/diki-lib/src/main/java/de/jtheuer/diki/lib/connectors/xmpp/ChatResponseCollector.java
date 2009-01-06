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
import java.util.concurrent.*;
import java.util.logging.Logger;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 */
public class ChatResponseCollector implements MessageListener, Callable<String> {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(ChatResponseCollector.class.getName());

	/**
	 * indicated the end of a transmission. This is a hack, now but necessary.
	 */
	private static final String END = "<END />";

	private Semaphore semaphore = new Semaphore(1);
	private int timeout;
	private StringBuffer buffer = new StringBuffer();
	
	public ChatResponseCollector(int timeout) {
		super();
		this.timeout = timeout;
		
		/* lock semaphore */
		try {
			semaphore.acquire();
		} catch (InterruptedException e) {
			LOGGER.info("Locking failed - what happened?");
		}
	}

	/* (non-Javadoc)
	 * @see org.jivesoftware.smack.MessageListener#processMessage(org.jivesoftware.smack.Chat, org.jivesoftware.smack.packet.Message)
	 */
	@Override
	public void processMessage(Chat chat, Message message) {
		if(message.getBody().startsWith("<")) {
			if(message.getBody().startsWith(END)) {
				chat.removeMessageListener(this);
				/* flag that we read everything! */
				semaphore.release();				
			} else {
				buffer.append(message.getBody());
			}
		}
		/* discard other content crap */
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public String call() throws InterruptedException {
		semaphore.tryAcquire(timeout, TimeUnit.SECONDS);
		/* read what ever has been sent, now. StringBuffer is threadsafe! */
		return buffer.toString();			
	}
}
