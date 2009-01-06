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
package de.jtheuer.jjcomponents.swing;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.*;

/**
 * Factory for some often-used layout utilities
 */
public class LayoutFactory {

	public static int LINE_AXIS = BoxLayout.LINE_AXIS;
	public static int PAGE_AXIS = BoxLayout.PAGE_AXIS;

	/** default gap between Buttons */
	private static final int BUTTONGAP = 10;

	public static Component getButtonGapH() {
		return Box.createHorizontalStrut(BUTTONGAP);
	}

	public static Component getButtonGapV() {
		return Box.createVerticalStrut(BUTTONGAP);
	}

	public static JComponent getSpacer(int size) {
		JComponent p = new JPanel();
		p.setSize(size, size);
		return p;
	}

	public static Component getButtonGap(int orientation) {
		if (orientation == LINE_AXIS) {
			return getButtonGapH();
		} else if (orientation == PAGE_AXIS) {
			return getButtonGapV();
		} else {
			throw new IllegalArgumentException("orientation must be LINE_AXIS or PAGE_AXIS");
		}
	}

	public static void setBoxLayoutH(JComponent component) {
		component.setLayout(new BoxLayout(component, LINE_AXIS));
	}

	public static void setBoxLayoutV(JComponent component) {
		component.setLayout(new BoxLayout(component, PAGE_AXIS));
	}

	public static void setBoxLayout(JComponent component, int orientation) {
		component.setLayout(new BoxLayout(component, orientation));
	}

	/**
	 * Sets all components to the same (maximum) preferred width
	 * 
	 * @param components
	 */
	public static void setSameWidth(JComponent... components) {
		int pref = 0;
		for (JComponent component : components) {
			pref = Math.max(component.getPreferredSize().width, pref);
		}

		for (JComponent component : components) {
				Dimension dim = new Dimension(pref, component.getPreferredSize().height);
				component.setPreferredSize(dim);
				component.setMaximumSize(dim);
		}
	}

	/**
	 * Sets all components to the same (maximum) preferred height
	 * 
	 * @param components
	 */
	public static void setSameHeight(JComponent... components) {
		int pref = 0;
		for (JComponent component : components) {
			pref = Math.max(component.getPreferredSize().height, pref);
		}

		for (JComponent component : components) {
			Dimension dim = new Dimension(component.getPreferredSize().width, pref);
			component.setPreferredSize(dim);
			component.setMaximumSize(dim);
		}
	}

	/**
	 * Sets the x-alignment of the supplied components to
	 * {@link Component#CENTER_ALIGNMENT}
	 * 
	 * @param components
	 */
	public static void center(JComponent... components) {
		for (JComponent component : components) {
			component.setAlignmentX(Component.CENTER_ALIGNMENT);
		}
	}

	/**
	 * Sets the x-alignment of the supplied components to
	 * {@link Component#LEFT_ALIGNMENT}
	 * 
	 * @param components
	 */
	public static void left(JComponent... components) {
		for (JComponent component : components) {
			component.setAlignmentX(Component.LEFT_ALIGNMENT);
		}
	}

	/**
	 * Sets the x-alignment of the supplied components to
	 * {@link Component#RIGHT_ALIGNMENT}
	 * 
	 * @param components
	 */
	public static void right(JComponent... components) {
		for (JComponent component : components) {
			component.setAlignmentX(Component.RIGHT_ALIGNMENT);
		}
	}

	public static void setTitledBorder(JPanel panel, String title) {
		panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(title), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
	}

	public static void setEmptyBorder(JComponent panel) {
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
	}

	/**
	 * Moves all contained JComponents to the left. It ignores {@link Component}
	 * objects!
	 * 
	 * @param panel
	 */
	public static void leftAll(JPanel panel) {
		for (Component comp : panel.getComponents()) {
			if (comp instanceof JComponent) {
				left((JComponent) comp);
			}
		}
	}
}
