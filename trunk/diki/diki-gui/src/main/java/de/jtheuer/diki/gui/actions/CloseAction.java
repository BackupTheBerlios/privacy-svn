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
import de.jtheuer.diki.lib.NetworkConnection;
import de.jtheuer.jjcomponents.utils.ResourcesContainer;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * 
 */
public class CloseAction extends AbstractAction {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(CloseAction.class.getName());

	private JFrame parent;
	private NetworkConnection connection;

	public CloseAction(JFrame parent, NetworkConnection connection) {
		super(Messages.getString("Main.exit"), ResourcesContainer.EXIT.getAsIcon()); //$NON-NLS-1$
		this.parent = parent;
		this.connection = connection;
	}

	public void actionPerformed(ActionEvent e) {
		parent.setVisible(false);
		connection.shutdown();
		System.exit(0);
	}

}
