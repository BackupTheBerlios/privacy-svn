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
import java.awt.LayoutManager;
import java.util.logging.*;

import javax.swing.Icon;
import javax.swing.JPanel;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 */
public class JJActivateableTab extends JPanel {
	/**	 */
	private static final long serialVersionUID = 1713236106648490310L;

	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(JJActivateableTab.class.getName());

	private boolean initialized = false;
	
	/**
	 * 
	 */
	public JJActivateableTab() {}

	/**
	 * @param layout
	 */
	public JJActivateableTab(LayoutManager layout) {
		super(layout);

	}

	/**
	 * @param isDoubleBuffered
	 */
	public JJActivateableTab(boolean isDoubleBuffered) {
		super(isDoubleBuffered);

	}

	/**
	 * @param layout
	 * @param isDoubleBuffered
	 */
	public JJActivateableTab(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);

	}
	
	/**
	 * Executed whenever this Tab gets activated 
	 */
	public void activate() {
		
	}

	/**
	 * Executed the first time this Tab gets activated 
	 */
	public void lazyInitialize() {
		
	}
	
	protected final void initializeOnce() {
		if(!initialized) {
			initialized = true;
			lazyInitialize();
		}
	}

	
	@Override
	public String getName() {
		return "";
	}
	
	public String getDescription() {
		return "";
	}
	
	public Icon getIcon() {
		return null;
	}
	
}
