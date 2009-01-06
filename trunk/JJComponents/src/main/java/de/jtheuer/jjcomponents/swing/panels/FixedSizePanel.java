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
import java.util.logging.Logger;

import de.jtheuer.jjcomponents.swing.LayoutFactory;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 * A JPanel that returns its preferred size as maximum size.
 */
public class FixedSizePanel extends JJPanel {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(FixedSizePanel.class.getName());

	public static final int NONE=0;
	public static final int FIXED_WIDTH=LayoutFactory.PAGE_AXIS;
	public static final int FIXED_HEIGHT=LayoutFactory.LINE_AXIS;
	public static final int FIXED_BOTH=FIXED_HEIGHT+FIXED_WIDTH;

	private int sizeFlag;
	
	/**
	 * @param sizeFlag
	 */
	public FixedSizePanel(int sizeFlag) {
		setSizeFlag(sizeFlag);
	}

	/**
	 * Constructs a BoxLayout panel
	 * @param sizeFlag
	 * @param orientation
	 */
	public FixedSizePanel(int sizeFlag, int orientation) {
		super(orientation);
		setSizeFlag(sizeFlag);
	}
	
	/**
	 * @param sizeFlag
	 */
	protected void setSizeFlag(int sizeFlag) {
		this.sizeFlag=sizeFlag;
	}


	@Override
	public Dimension getMaximumSize() {
		if(sizeFlag==FIXED_BOTH) {
			return getPreferredSize();
		} else if(sizeFlag==FIXED_HEIGHT) {
			return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
		} else if(sizeFlag==FIXED_WIDTH) {
			return new Dimension(getPreferredSize().width,Integer.MAX_VALUE);			
		} else {
			return super.getMaximumSize();
		}

	}
}
