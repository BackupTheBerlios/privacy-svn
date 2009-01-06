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
package de.jtheuer.jjcomponents.swing.listener;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.*;
import java.util.logging.*;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import de.jtheuer.jjcomponents.swing.components.LinkButton;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de> Abstract wrapper around
 *         several link-based event listener. The current implementation covers
 *         the {@link LinkButton} and {@link JEditorPane}.
 */
public abstract class AbstractURLListener extends AbstractAction implements HyperlinkListener {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(AbstractURLListener.class.getName());

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.event.HyperlinkListener#hyperlinkUpdate(javax.swing.event.HyperlinkEvent)
	 */
	@Override
	public void hyperlinkUpdate(HyperlinkEvent e) {
		if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			call(e.getURL());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof LinkButton) {
			LinkButton linkbutton = (LinkButton) e.getSource();
			call(linkbutton.getLinkURL());
		}
	}

	protected void call(URL link) {
		if (link != null) {
			try {
				call(link.toURI());
			} catch (Exception e) {
				LOGGER.log(Level.WARNING, link.toString() + " is not a valid URI ", e);
			}
		}
	}

	protected abstract void call(URI link) throws IOException;
}
