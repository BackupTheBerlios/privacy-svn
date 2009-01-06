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
package de.jtheuer.sesame;

import java.util.*;
import java.util.logging.Logger;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * 
 * This is a simple Set implementation with allows a easy instantiation with the
 * create method.
 */
public class SimpleSet<T> extends HashSet<T> implements Set<T> {
	/** * */
	private static final long serialVersionUID = -541710004119079873L;
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(SimpleSet.class.getName());

	public SimpleSet() {
		super();
	}

	public SimpleSet(T... elements) {
		this();

		for (T t : elements) {
			add(t);
		}
	}
	
	public SimpleSet(Set<T> set, T... elements) {
		this();
		
		addAll(set);
		
		for (T t : elements) {
			add(t);
		}
	}

	public SimpleSet(List<T> elements) {
		addAll(elements);
	}

	public static <L> SimpleSet<L> create(L... elements) {
		SimpleSet<L> set = new SimpleSet<L>(elements);
		return set;
	}

	/**
	 * Retrieves the first element of the list (the first element returned by
	 * the iterator) that ist NOT null. Note, that the Set van have any order.
	 * 
	 * @param <E>
	 * @param set
	 *            any element of the set
	 * @return
	 */
	public static <E> E first(Set<E> set) {
		if (set != null) {
			Iterator<E> i = set.iterator();
			while (i.hasNext()) {
				E o = i.next();
				if(o != null) {
					return o;
				}
			}
		}
		return null;
	}

	/**
	 * Retrieves the first element as String of the list (the first element
	 * returned by the iterator). Note, that the Set van have any order.
	 * 
	 * @param <E>
	 * @param set
	 *            any element of the set
	 * @return
	 */
	public static <E> String firstString(Set<E> set) {
		Object o = first(set);
		if (o != null) {
			return o.toString();
		} else {
			return null;
		}
	}
}
