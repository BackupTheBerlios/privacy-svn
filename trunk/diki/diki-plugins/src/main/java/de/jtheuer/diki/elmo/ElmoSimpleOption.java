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
import java.util.logging.Logger;

import javax.swing.*;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 */
public class ElmoSimpleOption<T> extends ElmoObjectPanel<T> {
	private static final long serialVersionUID = -999952359586626955L;
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(ElmoSimpleOption.class.getName());
	private T value;
	private T[] options;
	private JComboBox box;

	
	/**
	 * @param string
	 * @param foafGender
	 * @param string2
	 * @param string3
	 */
	public ElmoSimpleOption(String label, T initialItem, T ...options) {
		super(label);
		value = initialItem;
		this.options = options;
	}
	
	

	@SuppressWarnings("unchecked")
	@Override
	public T getValue() {
		return (T) box.getSelectedItem();
	}

	/* (non-Javadoc)
	 * @see de.jtheuer.diki.elmo.ElmoObjectPanel#getEditComponent()
	 */
	@Override
	protected JComponent getEditComponent() {
		box = new JComboBox(options);
		box.setSelectedItem(value);
		return box;
	}

	/* (non-Javadoc)
	 * @see de.jtheuer.diki.elmo.ElmoObjectPanel#getViewComponent()
	 */
	@Override
	protected JComponent getViewComponent() {
		return new JLabel(value == null ? "" : value.toString());
	}
}
