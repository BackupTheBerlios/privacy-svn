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
package de.jtheuer.diki;

import java.util.Set;
import java.util.logging.Logger;

import de.jtheuer.jjcomponents.swing.panels.JJPanel;
import de.jtheuer.sesame.SimpleSet;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * 
 * generic panel that displays an ElmoInstance
 * 
 */
public abstract class ElmoPanel<T> extends JJPanel {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(ElmoPanel.class.getName());

	/**
	 * 
	 */
	public ElmoPanel() {

	}

	/**
	 * Extracts the first value of a set. If null was supplied or there is no
	 * first value then it returns null
	 * 
	 * @param <T>
	 * @param object
	 * @return
	 */
	protected static <T> T firstOrNull(Set<T> object) {
		return firstOrNull(object, null);
	}

	/**
	 * Extracts the first value of a set. If null was supplied or there is no
	 * first value then it returns null This method checks against the supplied
	 * class at runtime thus preventing {@link ClassCastException}s
	 * 
	 * @param <T>
	 * @param object
	 * @param clazz
	 * @return
	 */
	protected static <T> T firstOrNull(Set<T> object, Class<T> clazz) {
		if (object != null) {
			T first = SimpleSet.first(object);
			if (first != null) {
				if (clazz == null || clazz.isAssignableFrom(first.getClass())) {
					return first;
				}
			}
		}
		return null;
	}

}
