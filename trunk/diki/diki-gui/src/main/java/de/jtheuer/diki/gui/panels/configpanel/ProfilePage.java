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

import java.util.logging.Logger;

import javax.swing.JLabel;

import org.openrdf.concepts.foaf.Person;

import com.xmlns.wot.PubKey;
import com.xmlns.wot.User;

import de.jtheuer.diki.elmo.PersonPanel;
import de.jtheuer.diki.elmo.PubKeyPanel;
import de.jtheuer.diki.lib.NetworkConnection;
import de.jtheuer.diki.lib.connectors.Connector.Status;
import de.jtheuer.jjcomponents.layout.JJSimpleFormLayout;
import de.jtheuer.jjcomponents.utils.ResourcesContainer;
import de.jtheuer.sesame.SimpleSet;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * 
 */
public class ProfilePage extends AbstractDikiConfigPage {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(ProfilePage.class.getName());

	/**
	 * @param title
	 * @param description
	 * @param icon
	 * @param properties
	 * @param connection
	 */
	public ProfilePage(NetworkConnection connection) {
		super("Your profile", "Enter the personal details that you want to share with friends.", ResourcesContainer.INFO.getAsIcon(32), null, connection);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jtheuer.diki.gui.panels.configpanel.AbstractDikiConfigPage#constructForm(de.jtheuer.jjcomponents.layout.JJSimpleFormLayout)
	 */
	@Override
	public void constructForm(JJSimpleFormLayout layout) {
		if(getConnection().getStatus().equals(Status.Connected) && getConnection().getUserFactory() != null) {
			Person user = getConnection().getUserFactory().getLocalUser();
			if (user != null) {
				PersonPanel ppanel = new PersonPanel(user);
				ppanel.edit();
				layout.add(ppanel);
				
			} else {
				add(new JLabel("You have to log in before you can change your account details!"));
			}
		} else {
			add(new JLabel("You have to log in before you can change your account details!"));
		}
	}
}
