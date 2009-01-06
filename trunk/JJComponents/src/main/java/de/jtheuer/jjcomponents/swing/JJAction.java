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
package de.jtheuer.jjcomponents.swing;
import java.awt.event.ActionEvent;
import java.util.logging.Logger;

import javax.swing.Icon;

import de.jtheuer.jjcomponents.utils.Resource;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 */
public abstract class JJAction extends javax.swing.AbstractAction {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(JJAction.class.getName());

	
	public JJAction() {
		super();
	}


	public JJAction(String name, Icon icon) {
		super(name, icon);
	}


	public JJAction(String name) {
		super(name);
	}
	
	public JJAction(Resource resource) {
		super(resource.getCaption(), resource.getAsIcon());
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public abstract void actionPerformed(ActionEvent e);

}
