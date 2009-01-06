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

import de.jtheuer.diki.gui.ConfigurationWindow;
import de.jtheuer.diki.gui.Messages;
import de.jtheuer.diki.lib.NetworkConnection;
import de.jtheuer.jjcomponents.utils.LocationAwareProperties;
import de.jtheuer.jjcomponents.utils.ResourcesContainer;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * 
 */
public class ConfigureAction extends AbstractAction {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(ConfigureAction.class.getName());
	private JFrame parent;
	private LocationAwareProperties properties;
	private NetworkConnection connection;

	/**
	 * 
	 */
	public ConfigureAction(JFrame parent, LocationAwareProperties properties, NetworkConnection connection) {
		super(Messages.getString("Main.configure"), ResourcesContainer.CONFIGURE.getAsIcon()); //$NON-NLS-1$
		this.parent = parent;
		this.properties = properties;
		this.connection = connection;
	}

	/** auto generated */
	private static final long serialVersionUID = 2472725980580559832L;

	public void actionPerformed(ActionEvent e) {
		new ConfigurationWindow(parent, properties, connection);
	}

}
