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
package de.jtheuer.jjcomponents.layout;

import java.awt.Component;
import java.util.logging.Logger;

import javax.swing.*;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * 
 */
public class JJSimpleFormLayout {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(JJSimpleFormLayout.class.getName());

	private static final int COLS = 3; //must fit to FormLayout!

	private FormLayout layout;
	private DefaultFormBuilder builder;

	public JJSimpleFormLayout(JPanel parent) {
		layout = new FormLayout("pref, 4dlu, max(pref;80dlu), 4dlu,pref,4dlu", // 1st major column
				""); // add rows dynamically
		builder = new DefaultFormBuilder(layout,parent);
		builder.setDefaultDialogBorder();
	}

	public void addSeperator(String string) {
		builder.appendSeparator(string);
		builder.nextLine();
	}

	public void add(JComponent... components) {
		for (JComponent component : components) {
			builder.append(component);
		}
		builder.nextLine();
	}
	
	public void addEmptyRow() {
		builder.nextLine();
	}
	
	public void add(String label, JComponent... components) {
		builder.append(label);
		for (JComponent component : components) {
			if(component != null) {
				builder.append(component);
			} else {
				builder.nextColumn();
			}
		}
		builder.nextLine();
	}
	
	public void addDescription(String label) {
		addDescription(new JLabel(label));
		builder.nextLine();
	}
	
	public void addDescription(JComponent component) {
		builder.append(component,5);
		builder.nextLine();
		
	}

	/**
	 * 
	 */
	public void addSpanningComponent(JComponent component) {
		LOGGER.info("Row: "+builder.getRow()+"/"+builder.getRowCount());
//		if(builder.getRow() >= builder.getRowCount()) {
//			builder.nextLine();
//			builder.setRow(builder.getRowCount());
//		}
		builder.append(component, COLS);
		builder.nextLine();
	}
}
