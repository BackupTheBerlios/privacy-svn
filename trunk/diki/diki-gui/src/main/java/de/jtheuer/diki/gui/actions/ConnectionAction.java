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
package de.jtheuer.diki.gui.actions;

import java.awt.event.ActionEvent;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.JFrame;

import de.jtheuer.diki.gui.Messages;
import de.jtheuer.diki.gui.panels.MainPanel;
import de.jtheuer.diki.gui.utils.Log;
import de.jtheuer.diki.lib.NetworkConnection;
import de.jtheuer.diki.lib.connectors.ConnectorException;
import de.jtheuer.jjcomponents.utils.LocationAwareProperties;
import de.jtheuer.jjcomponents.utils.ResourcesContainer;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * 
 */
public class ConnectionAction {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(ConnectionAction.class.getName());

	private JFrame parent;
	private LocationAwareProperties properties;
	private NetworkConnection connection;

	public ConnectionAction(JFrame parent, LocationAwareProperties properties, NetworkConnection connection) {
		super();
		this.parent = parent;
		this.properties = properties;
		this.connection = connection;
	}

	public AbstractAction getConnectAction() {
		return new AbstractAction(Messages.getString("Main.connect"),ResourcesContainer.OK.getAsIcon()) { //$NON-NLS-1$

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					connection.connect(properties);
				} catch (ConnectorException e1) {
					Log.info(e1.getMessage());
				}
			}

		};
	}

	public AbstractAction getDisconnectAction() {
		return new AbstractAction(Messages.getString("Main.disconnect"),ResourcesContainer.CANCEL.getAsIcon()) { //$NON-NLS-1$

			@Override
			public void actionPerformed(ActionEvent e) {
				connection.disconnect();
			}

		};
	}

	public AbstractAction getReconnectAction() {
		return new AbstractAction(Messages.getString("Main.reconnect"),ResourcesContainer.RESTART.getAsIcon()) { //$NON-NLS-1$

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					connection.disconnect();
					connection.connect(properties);
				} catch (ConnectorException e1) {
					Log.info(e1.getMessage());
				}
			}

		};
	}
	
	public AbstractAction getRedisplayAction() {
		return new AbstractAction(Messages.getString("Main.redisplay")) { //$NON-NLS-1$

			@Override
			public void actionPerformed(ActionEvent e) {
				parent.getContentPane().remove(0);
				parent.getContentPane().add(new MainPanel(connection,parent,properties));
				parent.validate();
			}

		};
	}
}
