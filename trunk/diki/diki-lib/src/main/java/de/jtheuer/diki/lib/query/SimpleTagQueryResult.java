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
	
package de.jtheuer.diki.lib.query;
import java.net.URL;
import java.util.*;
import java.util.logging.Logger;

import uk.co.holygoat.tag.concepts.Tagging;


/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 */
@Deprecated
public class SimpleTagQueryResult {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(SimpleTagQueryResult.class.getName());

	private List<Tagging> tags;

	private String name;
	
	/**
	 * A new instances with an ArrayList of default 10 items. 
	 */
	public SimpleTagQueryResult(String name) {
		 this(name,10);
	}
	
	public SimpleTagQueryResult(String name, List<Tagging> tags) {
		this.tags = tags;
	}

	/**
	 * A new instances with an ArrayList as data-structure 
	 * @param size the initial size.
	 */
	public SimpleTagQueryResult(String name, int size) {
		tags = new ArrayList<Tagging>(size);
		this.name = name;
	}
	

	/**
	 * adds a Resource to the List. Take care if you supplied your own list in the constructor. This method may throw a {@link NullPointerException}.
	 * @param resource
	 */
	public void add(Tagging resource) {
		tags.add(resource);
	}
	
	public void add(URL link, String[] tags, String description) {
		//TODO: add new Resource....
		add(null);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<Tagging> iterator() {
		return tags.iterator();
	}
	
	@Override
	public String toString() {
		return name+" [" + tags.size() + "] items";
	}
}
