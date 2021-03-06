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
/**
    (c) by Jan Torben Heuer <jan.heuer@uni-muenster.de

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/
 */
package de.jtheuer.jjcomponents.swing.panels;

import java.awt.Component;
import java.awt.Dimension;
import java.util.logging.Logger;

import javax.swing.Box;

import de.jtheuer.jjcomponents.swing.LayoutFactory;
import de.jtheuer.jjcomponents.utils.Dimension2;

/**
 * A simple OK - Cancel Panel
 */
public class SameSizeControlsPanel extends FixedSizePanel {
	/* autogenerated Logger */@SuppressWarnings("unused")
	private final static Logger LOGGER = Logger.getLogger(SameSizeControlsPanel.class.getName());

	public static int NO_ALIGN = 0;
	public static int ALIGN_END = 1;
	public static int ALIGN_START = 2;
	
	
	/**
	 * An empty panel without a LayoutManager and without fixed maximum size
	 */
	public SameSizeControlsPanel() {
		super(NO_ALIGN);
	}

	/**
	 * @param orientation
	 *            See {@link LayoutFactory}
	 * @param sizeFlag
	 *            See {@link FixedSizePanel}
	 * @param components
	 *            the components which should be displayed
	 */
	public SameSizeControlsPanel(int orientation, int sizeFlag, int align, Component... components) {
		super(sizeFlag);

		LayoutFactory.setBoxLayout(this, orientation);
		
		if (components.length > 0) {
			Dimension2 max = getMaximumDimension(components);

			addComponentsAndGap(components, align, max);

		}
	}

	/**
	 * @param components
	 * @param align
	 */
	protected void addComponentsAndGap(Component[] components, int align, Dimension max) {
		if(align==ALIGN_END) {
			add(Box.createGlue());
		}
		addComponents(components, max);
		if(align==ALIGN_START) {
			add(Box.createGlue());
		}
	}

	/**
	 * A JPanel with no fixed maximum width or height
	 * 
	 * @param orientation
	 *            See {@link LayoutFactory}
	 * @param components
	 *            the components which should be displayed
	 */
	public SameSizeControlsPanel(int orientation, int align, Component... component) {
		this(orientation, NO_ALIGN,align, component);
	}

	/**
	 * adds the supplied components to the panel and sets their preferredSize to
	 * size
	 * 
	 * @param components
	 * @param size
	 */
	protected void addComponents(Component[] components, Dimension size) {
		/* set size and add components */
		for (int i = 0; i < components.length; i++) {
			components[i].setPreferredSize(size);
			add(components[i]);
		}
	}

	/**
	 * @param components
	 * @return the maximum {@link Dimension2} of the components.
	 */
	protected static Dimension2 getMaximumDimension(Component[] components) {
		/* find the maximum of the preferred sizes */
		Dimension2 max = new Dimension2(components[0].getPreferredSize());
		for (int i = 1; i < components.length; i++) {
			max = max.max(components[i].getPreferredSize());
		}

		return max;
	}

}
