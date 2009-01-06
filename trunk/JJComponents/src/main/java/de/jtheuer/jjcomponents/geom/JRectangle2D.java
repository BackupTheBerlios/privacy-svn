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
package de.jtheuer.jjcomponents.geom;
import java.awt.geom.Rectangle2D;
import java.util.logging.Logger;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 */
public class JRectangle2D extends Rectangle2D.Double {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(JRectangle2D.class.getName());

	public JRectangle2D() {
		super();
	}
	
	public JRectangle2D(Rectangle2D rect) {
		super(rect.getX(),rect.getY(),rect.getWidth(),rect.getHeight());
	}
	
	public JRectangle2D(double arg0, double arg1, double arg2, double arg3) {
		super(arg0, arg1, arg2, arg3);
	}
	
	/**
	 * Adds (or removes) the given amount of (pixels) to each corner
	 * @param amount
	 */
	public void buffer(int amount) {
		setRect(x-amount, y-amount, width+amount*2, height+amount*2);
	}
}
