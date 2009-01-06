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
import java.util.logging.Logger;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 */
public class SparqlFriendsOfQuery extends SparqlQuery implements SPARQLPrefix {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(SparqlFriendsOfQuery.class.getName());
	
	/**
	 * @param query
	 */
	public SparqlFriendsOfQuery(String myself) {
		super(getQuery(myself));
	}
	
	public static String getQuery(String myself) {
		/*
		 * query template:
		 * SELECT ?tagging
		 * WHERE {
		 * ?tagging tag:associatedTag ?tag1 .
		 * ?tagging tag:associatedTag ?tag2 .
		 * ?tag1 tag:name "tagname1" .
		 * ?tag2 tag:name "tagname2" .
		 * }
		 */
		StringBuilder b = new StringBuilder();
		b.append(PREFIX_FOAF);
		b.append("DESCRIBE ?knows WHERE {\n");
		b.append("<" + myself + "> foaf:knows ?knows.\n");
		b.append("}");
		return b.toString();
	}
	
}
