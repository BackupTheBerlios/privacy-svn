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
import java.util.logging.*;

import javax.swing.Icon;
import javax.swing.JLabel;

import org.openrdf.concepts.foaf.Person;

import com.jidesoft.dialog.AbstractDialogPage;
import com.xmlns.wot.PubKey;
import com.xmlns.wot.User;

import de.jtheuer.diki.elmo.PersonPanel;
import de.jtheuer.diki.elmo.PubKeyPanel;
import de.jtheuer.diki.lib.NetworkConnection;
import de.jtheuer.diki.lib.connectors.Connector.Status;
import de.jtheuer.jjcomponents.PropertiesPersistence;
import de.jtheuer.jjcomponents.layout.JJSimpleFormLayout;
import de.jtheuer.jjcomponents.utils.ResourcesContainer;
import de.jtheuer.sesame.SimpleSet;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 */
public class PGPPage extends AbstractDikiConfigPage {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(PGPPage.class.getName());

	/**
	 * @param title
	 * @param description
	 * @param icon
	 * @param properties
	 * @param connection
	 */
	public PGPPage(NetworkConnection connection) {
		super("Security", "Your automatically created PGP keys for message encryption and digital signatures.", ResourcesContainer.PGP.getAsIcon(32), null, connection);

	}

	/* (non-Javadoc)
	 * @see de.jtheuer.diki.gui.panels.configpanel.AbstractDikiConfigPage#constructForm(de.jtheuer.jjcomponents.layout.JJSimpleFormLayout)
	 */
	@Override
	public void constructForm(JJSimpleFormLayout layout) {
		if(getConnection().getStatus().equals(Status.Connected) && getConnection().getUserFactory() != null) {
			Person user = getConnection().getUserFactory().getLocalUser();
			if (user != null) {
				/* check if the Person is also a pgp User */
				if (user instanceof User) {
					User pgpuser = (User) user;
					PubKey key = SimpleSet.first(pgpuser.getHasKey());
					if(key != null) {
						layout.addSeperator("PGP Public key information");
						PubKeyPanel pubkeypanel = new PubKeyPanel(key);
						pubkeypanel.view();
						layout.add(pubkeypanel);
					}
				}
			
				return;
			}
		}
		add(new JLabel("Your pgp key will be created at the first successful login."));
	}
}
