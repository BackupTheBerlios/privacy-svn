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
package de.jtheuer.diki.lib.connectors;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Logger;

import javax.swing.*;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * 
 */
public class ParameterProperties implements Iterable<ParameterProperties.Field> {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(ParameterProperties.class.getName());

	public enum TYPE {
		DEFAULT, PASSWORD, CHECKBOX;
	}

	public static class Field {
		private String id;
		private String description;
		private boolean mandatory;
		private TYPE type;
		private String[] options;

		public Field(String id, String description, boolean mandatory, TYPE type, String... options) {
			this.id = id;
			this.description = description;
			this.mandatory = mandatory;
			this.type = type;
			this.options = options;
		}

		public Field(String id, String description, boolean mandatory, String... options) {
			this(id, description, mandatory, TYPE.DEFAULT, options);
		}

		public TYPE getType() {
			return type;
		}

		public void setType(TYPE type) {
			this.type = type;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean isMandatory() {
			return mandatory;
		}

		public void setMandatory(boolean mandatory) {
			this.mandatory = mandatory;
		}

		public String[] getOptions() {
			return options;
		}

		public void setOptions(String[] options) {
			this.options = options;
		}

		/**
		 * @return depending on the type and options:
		 * a JComboBox if options exist
		 * a JPasswordField (PASSWORD)
		 * a JCheckBox (CHECKBOX)
		 * otherwise a JTextField
		 * 
		 */
		public JComponent getTypeComponent() {
			if(options != null && options.length > 0) {
				JComboBox box = new JComboBox(options);
				box.setEditable(true);
				return box;
			}
			
			switch (type) {
			
			case PASSWORD:
				return new JPasswordField();
			case CHECKBOX:
				return new JCheckBox();
			}
			
			return new JTextField();
		}
	}

	private LinkedList<Field> fields = new LinkedList<Field>();

	public void addField(Field field) {
		fields.add(field);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Field> iterator() {
		return fields.iterator();
	}

}
