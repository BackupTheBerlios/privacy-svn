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
package de.jtheuer.diki.gui.utils;
import java.awt.Dimension;

/**
 * Extends the java.awt.Dimension class with methods for gemetric operations.
 */
public class Dimension2 extends Dimension {

	public Dimension2(int i, int j) {
		super(i,j);
	}

	public Dimension2 max(Dimension other) {
		return max(this,other);
	}
	
	public static Dimension2 max(Dimension one, Dimension other) {
		return new Dimension2(Math.max(one.width, other.width),Math.max(one.height, other.height));
	}
}
