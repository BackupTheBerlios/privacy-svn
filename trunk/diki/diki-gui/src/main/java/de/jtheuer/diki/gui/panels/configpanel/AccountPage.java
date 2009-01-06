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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import javax.swing.*;

import de.jtheuer.diki.lib.NetworkConnection;
import de.jtheuer.diki.lib.connectors.Connector;
import de.jtheuer.diki.lib.connectors.ParameterProperties;
import de.jtheuer.diki.lib.connectors.ParameterProperties.Field;
import de.jtheuer.jjcomponents.PropertiesPersistence;
import de.jtheuer.jjcomponents.layout.JJSimpleFormLayout;
import de.jtheuer.jjcomponents.utils.ResourcesContainer;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * 
 */
public class AccountPage extends AbstractDikiConfigPage {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(AccountPage.class.getName());
	private static URL BIBSONOMYAPIURL;

	static {
		 try {
			BIBSONOMYAPIURL = new URL("http://www.bibsonomy.org/settings?seltab=2");
		} catch (MalformedURLException e) {
			LOGGER.severe("static urlstring caused an exception - this IS a real problem!");
		}
	}
	/**
	 * @param title
	 * @param description
	 * @param icon
	 * @param properties
	 */
	public AccountPage(PropertiesPersistence properties, NetworkConnection connection) {
		super("Accounts", "Enter your connector credentials here. The xmpp account is mandatory!", ResourcesContainer.MODULES.getAsIcon(32), properties,
				connection);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jtheuer.diki.gui.panels.configpanel.AbstractDikiConfigPage#constructForm()
	 */
	@Override
	public void constructForm(JJSimpleFormLayout layout) {
		for (Connector connector : getConnection().getConnectors()) {
			if (connector.getParameters() != null) {
				layout.addSeperator(connector.getName());
				LOGGER.info("reading connector properties for " + connector.getName());
				for (Object key : connector.getParameters().keySet()) {
					String keystring = key.toString();
					String parameter = connector.getParameters().get(key).toString();
					
					/* Text or Password ? */
					JComponent cmp;
					if(keystring.contains("password")) {
						cmp = new JPasswordField();
					} else {
						cmp = new JTextField();
					}
					
					layout.add(parameter, getProperties().assignKeyToComponent(keystring, cmp));
				}
			} else if(connector.getProperties() != null) {
				ParameterProperties iterable = connector.getProperties();
				layout.addSeperator(connector.getName());
				for (Field field : iterable) {
					String key = field.getDescription();
					String id = field.getId();
					JComponent component = field.getTypeComponent();
					layout.add(key,getProperties().assignKeyToComponent(id, component));
				}
			}
		}
		//layout.addEmptyRow();
		layout.addDescription(new de.jtheuer.jjcomponents.swing.components.LinkButton(BIBSONOMYAPIURL,"Click here to get your bibsonomy API key",null));
	}
}
