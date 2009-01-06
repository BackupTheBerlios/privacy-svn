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
package de.jtheuer.jjcomponents.swing.components;
import java.awt.Dimension;
import java.util.logging.Logger;

import javax.swing.JTextField;
import javax.swing.text.Document;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 */
public class JJMaxHeightTextField extends JTextField {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(JJMaxHeightTextField.class.getName());

	/**
	 * 
	 */
	public JJMaxHeightTextField() {}

	/**
	 * @param text
	 */
	public JJMaxHeightTextField(String text) {
		super(text);

	}

	/**
	 * @param columns
	 */
	public JJMaxHeightTextField(int columns) {
		super(columns);

	}

	/**
	 * @param text
	 * @param columns
	 */
	public JJMaxHeightTextField(String text, int columns) {
		super(text, columns);

	}

	/**
	 * @param doc
	 * @param text
	 * @param columns
	 */
	public JJMaxHeightTextField(Document doc, String text, int columns) {
		super(doc, text, columns);

	}
	
	@Override
	public Dimension getMaximumSize() {
		return new Dimension(Integer.MAX_VALUE,getPreferredSize().height);
	}
}
