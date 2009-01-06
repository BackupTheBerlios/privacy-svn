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
package de.jtheuer.jjcomponents.utils;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JComponent;

/**
 * Extends the java.awt.Dimension class with methods for gemetric operations.
 */
public class Dimension2 extends Dimension {

	public Dimension2(Dimension dimension) {
		super(dimension);
	}
	
	public Dimension2(int i, int j) {
		super(i,j);
	}

	public Dimension2 max(Dimension other) {
		return max(this,other);
	}
	
	public static Dimension2 max(Dimension ...dims) {
		Dimension2 max=null;
		if(dims ==null) {
			return null;
		} else if(dims.length==0) {
			return null;
		} else 	if(dims.length >= 1) {
			max=new Dimension2(dims[0]);
			for (int i=1; i < dims.length; i++) {
				max = new Dimension2(Math.max(max.width, dims[i].width),Math.max(max.height, dims[i].height));
			}
		}
		return max;
	}
	
	public static Dimension2 maxPref(List<JComponent> cmps) {
		Dimension[] dims = new Dimension[cmps.size()];
		int i=0;
		for (JComponent component : cmps) {
			dims[i] = component.getPreferredSize();
			i++;
		}
		return max(dims);
	}

}
