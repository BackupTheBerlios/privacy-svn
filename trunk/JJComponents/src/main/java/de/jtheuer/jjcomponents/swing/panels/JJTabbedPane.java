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
import java.util.logging.Logger;

import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.jidesoft.swing.JideTabbedPane;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 */
public class JJTabbedPane extends JideTabbedPane {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(JJTabbedPane.class.getName());

	public JJTabbedPane() {
		super();
		init();
	}

	public JJTabbedPane(int tabPlacement, int tabLayoutPolicy) {
		super(tabPlacement, tabLayoutPolicy);
		init();
	}

	public JJTabbedPane(int tabPlacement) {
		super(tabPlacement);
		init();
	}
	
	private void init() {
		addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				if(getSelectedComponent() instanceof JJActivateableTab) {
					SwingUtilities.invokeLater(new Runnable() {

						@Override
						public void run() {
							((JJActivateableTab) getSelectedComponent()).initializeOnce();
							((JJActivateableTab) getSelectedComponent()).activate();

						}
						
					});
				}
			}
			
		});
	}
	
	/**
	 * adds the given {@link JJActivateableTab} instance and reads the properties: 
	 * {@link JJActivateableTab#getName()}
	 * {@link JJActivateableTab#getDescription()}
	 * {@link JJActivateableTab#getIcon()}
	 * @param tag
	 */
	public void addTab(JJActivateableTab tag) {
		addTab(tag.getName(),tag.getIcon(), tag, tag.getDescription());
	}
	
	
}
