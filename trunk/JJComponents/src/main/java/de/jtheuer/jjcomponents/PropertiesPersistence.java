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
package de.jtheuer.jjcomponents;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import de.jtheuer.jjcomponents.properties.PropertyObject;
import de.jtheuer.jjcomponents.utils.LocationAwareProperties;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * 
 */
public class PropertiesPersistence {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(PropertiesPersistence.class.getName());

	private static final String TRUE = "TRUE";
	private static final String FALSE = "FALSE";

	private LocationAwareProperties properties;
	private HashMap<String, JComponent> components;

	public PropertiesPersistence(LocationAwareProperties properties) {
		super();
		this.properties = properties;
		this.components = new HashMap<String, JComponent>(properties.size());
	}

	/**
	 * Assigns the key to the given component
	 * 
	 * @param key
	 * @param component
	 * @return the supplied component (for convenience)
	 */
	public JComponent assignKeyToComponent(String key, JComponent component) {
		components.put(key, component);
		return component;
	}

	/**
	 * reads the properties and fills the components
	 */
	public void loadProperties() {
		for (String key : components.keySet()) {
			setValueOf(components.get(key), properties.getProperty(key));
		}
	}

	/**
	 * @param component
	 * @param object
	 */
	private void setValueOf(JComponent component, String value) {
		if (value != null) {
			if (component instanceof JTextField) {
				JTextField jtext = (JTextField) component;
				jtext.setText(value == null ? "" : value);
			} else if (component instanceof JPasswordField) {
				((JPasswordField) component).setText(value == null ? "" : value);
			} else if (component instanceof JCheckBox) {
				((JCheckBox) component).setSelected(TRUE.equalsIgnoreCase(value));
			} else if (component instanceof JComboBox) {
				((JComboBox) component).setSelectedItem(value);
			} else if (component instanceof JList) {
				JList list = (JList) component;
				for (int i = 0; i < list.getModel().getSize(); i++) {
					Object elem = list.getModel().getElementAt(i);
					String elem_value = null;

					/*
					 * check if the value is supplied by the getProperty method
					 * rather then toString
					 */
					if (elem instanceof PropertyObject) {
						elem_value = ((PropertyObject) elem).getProperty();
					} else {
						elem_value = elem.toString();
					}
					if (elem_value.equals(value)) {
						list.setSelectedIndex(i);
					}
				}
			} else {
				throw new RuntimeException("Cannot handle the JComponent " + component.getClass().getName());
			}
		}
	}

	/**
	 * reads the field values and puts them into the Properties
	 */
	public void saveProperties() {
		for (String key : components.keySet()) {
			JComponent comp = components.get(key);
			properties.setProperty(key, getValueOf(comp));
		}

		try {
			properties.save();
		} catch (IOException e) {
			LOGGER.warning("Cannot save properties!");
		}
	}

	private String getValueOf(JComponent comp) {
		String value;
		if (comp instanceof JTextField) {
			JTextField jtext = (JTextField) comp;
			value = jtext.getText();
		} else if (comp instanceof JPasswordField) {
			value = new String(((JPasswordField) comp).getPassword());
		} else if (comp instanceof JCheckBox) {
			value = ((JCheckBox) comp).isSelected() ? TRUE : FALSE;
		} else if (comp instanceof JComboBox) {
			value = ((JComboBox) comp).getSelectedItem().toString();
		} else if (comp instanceof JList) {
			JList list = (JList) comp;

			Object elem = list.getSelectedValue();

			/*
			 * check if the value is supplied by the getProperty method rather
			 * then toString
			 */
			if (elem instanceof PropertyObject) {
				value = ((PropertyObject) elem).getProperty();
			} else {
				value = elem.toString();
			}
		} else {
			throw new RuntimeException("Cannot handle the JComponent " + comp.getClass().getName());
		}
		return value;

	}

	/**
	 * @return
	 */
	public boolean haveChanged() {
		for (String key : components.keySet()) {
			JComponent comp = components.get(key);
			String value = properties.getProperty(key);
			if (value != null && !value.equals(getValueOf(comp))) {
				return true;
			}
		}

		return false;
	}
}
