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
	
package de.jtheuer.diki.gui.panels.tagpanel;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 */
public interface ElmoModelNode {
	
	public abstract int getChildCount();
	
	public abstract Object getChildAt(int index);
	
	public abstract ElmoNode getParent();

	public abstract void lazyInitialize();
}
