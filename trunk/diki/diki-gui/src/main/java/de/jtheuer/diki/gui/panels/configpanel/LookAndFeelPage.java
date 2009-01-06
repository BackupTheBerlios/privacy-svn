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
import java.awt.Component;
import java.util.logging.Logger;

import javax.swing.JList;

import com.jidesoft.swing.JideScrollPane;

import de.jtheuer.diki.gui.Main;
import de.jtheuer.diki.lib.NetworkConnection;
import de.jtheuer.jjcomponents.PropertiesPersistence;
import de.jtheuer.jjcomponents.layout.JJSimpleFormLayout;
import de.jtheuer.jjcomponents.swing.ui.UIController;
import de.jtheuer.jjcomponents.utils.ResourcesContainer;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 */
public class LookAndFeelPage extends AbstractDikiConfigPage {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(LookAndFeelPage.class.getName());
	private Component[] windows;

	/**
	 * @param title
	 * @param description
	 * @param icon
	 * @param properties
	 * @param connection
	 * @param windows the components that should be updated when another UI is selected
	 */
	public LookAndFeelPage(PropertiesPersistence properties, NetworkConnection connection, Component ...windows) {
		super("Themes", "Select your preferred userinterface here. Note that some interfaces are not available on all platforms! Some of them are Windows, Linux or Mac only.",ResourcesContainer.LOOKNFEEL.getAsIcon(32), properties, connection);
		this.windows = windows;
	}

	/* (non-Javadoc)
	 * @see de.jtheuer.diki.gui.panels.configpanel.AbstractDikiConfigPage#constructForm(de.jtheuer.jjcomponents.layout.JJSimpleFormLayout)
	 */
	@Override
	public void constructForm(JJSimpleFormLayout layout) {

		JList listbox = new JList(UIController.getAvailableLnFs());
		JideScrollPane scroll = new JideScrollPane(listbox,JideScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JideScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		listbox.addListSelectionListener((UIController.getListener(listbox, this.windows)));
		/* add box */
		layout.add("look and feel",scroll);
		
		/* set configstring */
		getProperties().assignKeyToComponent(Main.LOOKANDFEEL, listbox);

	}
}
