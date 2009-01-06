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
	
package de.jtheuer.diki.gui.panels.configpanel;
import java.awt.BorderLayout;
import java.util.logging.Logger;

import javax.swing.Icon;
import javax.swing.JPanel;

import com.jidesoft.dialog.AbstractDialogPage;

import de.jtheuer.diki.lib.NetworkConnection;
import de.jtheuer.jjcomponents.PropertiesPersistence;
import de.jtheuer.jjcomponents.layout.JJSimpleFormLayout;
import de.jtheuer.jjcomponents.swing.panels.JJBannerPanel;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 */
public abstract class AbstractDikiConfigPage extends AbstractDialogPage {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(AbstractDikiConfigPage.class.getName());
	private PropertiesPersistence properties;
	private NetworkConnection connection;

	/**
	 * @param title
	 * @param description
	 * @param icon
	 */
	public AbstractDikiConfigPage(String title, String description, Icon icon, PropertiesPersistence properties, NetworkConnection connection) {
		this(title, description, icon, null, properties, connection);
	}

	/**
	 * @param title
	 * @param description
	 * @param icon
	 * @param parentPage
	 */
	public AbstractDikiConfigPage(String title, String description, Icon icon, AbstractDialogPage parentPage, PropertiesPersistence properties, NetworkConnection connection) {
		super(title, description, icon, parentPage);
		this.properties = properties;
		this.connection = connection;
	}

	/* (non-Javadoc)
	 * @see com.jidesoft.dialog.AbstractPage#lazyInitialize()
	 */
	@Override
	public final void lazyInitialize() {
		setLayout(new BorderLayout());
		add(new JJBannerPanel(getTitle(),getDescription()),BorderLayout.NORTH);
		
		JPanel content = new JPanel();
		JJSimpleFormLayout layout = new JJSimpleFormLayout(content);
		add(content,BorderLayout.CENTER);
		constructForm(layout);
		
	}
	
	/**
	 * load all necessary form elements here
	 */
	public abstract void constructForm(JJSimpleFormLayout layout);

	/**
	 * @return the properties that have been supplied with the constructor
	 */
	public PropertiesPersistence getProperties() {
		return properties;
	}

	public NetworkConnection getConnection() {
		return connection;
	}
}
