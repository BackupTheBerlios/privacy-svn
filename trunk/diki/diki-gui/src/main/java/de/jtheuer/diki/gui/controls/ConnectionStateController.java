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
package de.jtheuer.diki.gui.controls;

import java.util.List;
import java.util.logging.Logger;

import javax.swing.*;

import de.jtheuer.diki.gui.panels.MainPanel;
import de.jtheuer.diki.lib.ConnectionListener;
import de.jtheuer.diki.lib.NetworkConnection;
import de.jtheuer.diki.lib.connectors.Connector;
import de.jtheuer.diki.lib.connectors.Connector.Status;
import de.jtheuer.jjcomponents.utils.ResourcesContainer;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * 
 */
public class ConnectionStateController extends JMenu implements ConnectionListener {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(ConnectionStateController.class.getName());

	private NetworkConnection connection;
	private MainPanel mainPanel;
	private JMenuItem[] menuitems;

	/**
	 * @param mainPanel 
	 * 
	 */
	public ConnectionStateController(MainPanel mainPanel, NetworkConnection connection) {
		super();
		this.connection = connection;
		this.mainPanel = mainPanel;
		connection.addConnectionListener(this);
		checkState();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jtheuer.diki.lib.ConnectionListener#fireConnected()
	 */
	@Override
	public void fireStatusUpdate() {
		checkState();
	}

	private void checkState() {
		List<Connector> connectors = connection.getConnectors();

		/* check if we have the right amount of submenus for or connectors! */
		if (menuitems != null && connectors.size() != menuitems.length) {
			removeAll();
			menuitems = null;
		}
		if (menuitems == null) {
			menuitems = new JMenuItem[connectors.size()];
			
			for (int i=0;i<connectors.size();i++) {
				menuitems[i] = new JMenuItem();
				add(menuitems[i]);
			}
		}
		/* we check how many items are still disconnected:
		 * if = total: everything connected
		 * if < total: connecting
		 * if = 0 : disconnected
		 * 
		 * remember: we ignore disabled status!
		 */
		int connected_items=0;
		int total_item=connectors.size();
		for (int i=0;i<connectors.size();i++) {
			menuitems[i].setText(connectors.get(i).getName());

			Status status = connectors.get(i).getStatus();
			if (connectors.get(i).getStatus().equals(Status.Disconnected)) {
				setDisconnected(menuitems[i]);
			} else if (connectors.get(i).getStatus().equals(Status.Connecting)) {
				setConnecting(menuitems[i]);
			} else if(status.equals(Status.Disabled)) {
				total_item--;
				setDisabled(menuitems[i]);
			} else if (connectors.get(i).getStatus().equals(Status.Connected)) {
				connected_items++;
				setConnected(menuitems[i]);
			} else {
				throw new AssertionError("wrong Status recieved!");
			}
		}
		
		if(connection.getStatus().equals(Status.Disconnected)) {
			setDisconnected(this);
			setText("disconnected");	
			mainPanel.setEnabled(false);
		} else if(connection.getStatus().equals(Status.Connected)) {
			setConnected(this);
			setText("connected");
			mainPanel.setEnabled(true);
		} else {
			setConnecting(this);
			setText("connecting");
			mainPanel.setEnabled(false);
		}
			
	}
	private static void setConnecting(AbstractButton b) {
		b.setIcon(ResourcesContainer.RESTART.getAsAnimatedIcon(16));
	}
	
	private static void setConnected(AbstractButton b) {
		b.setIcon(ResourcesContainer.OK.getAsIcon(16));
	}
	
	private static void setDisconnected(AbstractButton b) {
		b.setIcon(ResourcesContainer.CANCEL.getAsIcon(16));
	}

	private static void setDisabled(AbstractButton b) {
		b.setIcon(ResourcesContainer.CLOSE.getAsIcon(16));
	}
}
