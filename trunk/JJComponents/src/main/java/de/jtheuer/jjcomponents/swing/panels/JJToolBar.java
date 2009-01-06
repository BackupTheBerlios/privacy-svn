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

import java.awt.Dimension;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.logging.*;

import javax.swing.*;

import de.jtheuer.jjcomponents.swing.JJComponentFactory;
import de.jtheuer.jjcomponents.swing.LayoutFactory;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * 
 * This class extends the normal Swing {@link JToolBar}. It changed how actions
 * are added: JButtons are created that all share the same width and display
 * their Action name below.
 */
public class JJToolBar extends JToolBar {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(JJToolBar.class.getName());

	private ArrayList<JButton> buttons = new ArrayList<JButton>();

	private JJComponentFactory factory;
	public JJToolBar() {
		super();
	}

	public JJToolBar(int orientation) {
		super(orientation);
	}

	public JJToolBar(String name, int orientation) {
		super(name, orientation);
	}

	public JJToolBar(String name) {
		super(name);
	}

	@Override
	protected JButton createActionComponent(Action a) {
		if(factory==null) {
			factory=new JJComponentFactory();
		}
		
		/* from superclass */
		JButton b = factory.createButton(a);
		
//		JButton b = new JButton() {
//			protected PropertyChangeListener createActionPropertyChangeListener(Action a) {
//				PropertyChangeListener pcl = createActionChangeListener(this);
//				if (pcl == null) {
//					pcl = super.createActionPropertyChangeListener(a);
//				}
//				return pcl;
//			}
//		};

		b.setHorizontalTextPosition(JButton.CENTER);
		b.setVerticalTextPosition(JButton.BOTTOM);
		
		buttons.add(b);
		
//		/* set same width for all buttons that have been added this way */
//		int pref = 0;
//		for (JComponent component : buttons) {
//			pref = Math.max(component.getPreferredSize().width, pref);
//		}
//
//		for (JComponent component : buttons) {
//				Dimension dim = new Dimension(pref, component.getPreferredSize().height);
//				component.setPreferredSize(dim);
//				component.setMaximumSize(dim);
//		}
		
		return b;
	}

}
