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
package de.jtheuer.jjcomponents.swing.panels;
import java.awt.BorderLayout;
import java.util.logging.Logger;

import javax.swing.JPanel;

import de.jtheuer.jjcomponents.swing.LayoutFactory;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 */
public class JJPanel extends JPanel {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(JJPanel.class.getName());
	
	/**
	 * Constructs a empty JPanel
	 */
	public JJPanel() {

	}

	/**
	 * Constructs a JPanel with a BoxLayout and the given orientation. See {@link BorderLayout}
	 * @param orientation
	 */
	public JJPanel(int orientation) {
		this();
		LayoutFactory.setBoxLayout(this, orientation);
	}


}
