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
	
package de.jtheuer.diki.elmo;
import java.util.Set;
import java.util.logging.Logger;

import javax.swing.*;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * This class takes any Set of objects and reads the first one. On write it puts a string instance back.
 */
public class ElmoObjectSetPanel extends ElmoObjectPanel<Object> {
	/**	 */
	private static final long serialVersionUID = -5624375956009643571L;
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(ElmoObjectSetPanel.class.getName());
	private String content;
	private JTextField textField;

	public ElmoObjectSetPanel(String label,Set<Object> object) {
		super(label,object,null);
		Object first = firstOrNull(object);
		content = first == null ? "" :first.toString();
	}

	/* (non-Javadoc)
	 * @see de.jtheuer.diki.ElmoPanel#getEditComponent()
	 */
	@Override
	protected JComponent getEditComponent() {
		textField = new JTextField(content);
		return textField;
	}

	/* (non-Javadoc)
	 * @see de.jtheuer.diki.ElmoPanel#getViewComponent()
	 */
	@Override
	protected JComponent getViewComponent() {
		return new JLabel(content);
	}

	@Override
	public Object getValue() {
		return textField.getText();
	}

	
}
