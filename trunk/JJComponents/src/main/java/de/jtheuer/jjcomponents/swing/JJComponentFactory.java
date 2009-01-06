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
package de.jtheuer.jjcomponents.swing;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.logging.*;

import javax.swing.*;

import de.jtheuer.jjcomponents.utils.Dimension2;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * 
 * A factory constructs {@link JComponent} instances that all return the same
 * maximum value for {@link JComponent#getPreferredSize()}.
 */
public class JJComponentFactory {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(JJComponentFactory.class.getName());
	
	private ArrayList<JComponent> cmps = new ArrayList<JComponent>();

	/* start with minimal size */
	private Dimension2 max = new Dimension2(0,0);
	
	public JButton createButton(Action a) {
		JButton btn = new JButton(a) {

			@Override
			public Dimension getPreferredSize() {
				max = max.max(super.getPreferredSize());
				return max;
			}
		};

		cmps.add(btn);
		return btn;

	}
}
