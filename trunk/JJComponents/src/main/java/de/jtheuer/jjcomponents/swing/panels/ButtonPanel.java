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
import java.awt.Component;
import java.awt.Dimension;
import java.util.logging.Logger;

import javax.swing.Action;
import javax.swing.JButton;

import de.jtheuer.jjcomponents.swing.LayoutFactory;
import de.jtheuer.jjcomponents.utils.Dimension2;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 */
public class ButtonPanel extends SameSizeControlsPanel {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(ButtonPanel.class.getName());

	protected int orientation;
	
	/**
	 * A 
	 * @param orientation
	 * @param component
	 */
	public ButtonPanel(int orientation, int sizeFlag, int align, Component... component) {
		super(orientation,sizeFlag, align, component);
		
		this.orientation=orientation;
	}

	/**
	 * @param orientation
	 * @param gap if before or after the actions
	 * @param component
	 */
	public ButtonPanel(int orientation, int sizeFlag, int align, Action... actions) {
		super(orientation,sizeFlag,align);
		
		this.orientation=orientation;
		
		Component[] components = createButtonsfromActions(actions);
		
		Dimension2 max = getMaximumDimension(components);

		addComponentsAndGap(components, align, max);

	}

	
	@Override
	protected void addComponents(Component[] components, Dimension size) {
		/* set size and add components */ 
		for (int i = 0; i < components.length; i++) {
			components[i].setPreferredSize(size);
			
			if(i>0) {
				add(LayoutFactory.getButtonGap(orientation));
			}
			add(components[i]);
		}
	}
	
	protected Component[] createButtonsfromActions(Action... actions) {
		JButton[] buttons = new JButton[actions.length];
		for (int i=0; i<actions.length; i++) {
			buttons[i] = new JButton(actions[i]);
		}
		
		return buttons;
	}
}
