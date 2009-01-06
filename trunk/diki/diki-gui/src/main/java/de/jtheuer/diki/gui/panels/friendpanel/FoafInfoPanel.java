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
	
package de.jtheuer.diki.gui.panels.friendpanel;
import java.util.logging.Logger;

import javax.swing.JDialog;

import org.openrdf.concepts.foaf.Person;

import de.jtheuer.diki.elmo.PersonPanel;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 */
public class FoafInfoPanel extends PersonPanel {
	private static final long serialVersionUID = 2643317279939361267L;
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(FoafInfoPanel.class.getName());
	
	public FoafInfoPanel(Person person) {
		super(person);
		view();
	}
	
	
	public static void newWindow(Person p) {
		JDialog j = new JDialog();
		j.getContentPane().add(new FoafInfoPanel(p));
		j.pack();
		j.setVisible(true);
	}
		
}
