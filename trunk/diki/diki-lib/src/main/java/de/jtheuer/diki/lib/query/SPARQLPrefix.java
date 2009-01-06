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

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 */
public interface SPARQLPrefix {

	public static final String PREFIX_TAGGING = "PREFIX tag: <http://www.holygoat.co.uk/owl/redwood/0.1/tags/> ";
	public static final String PREFIX_FOAF = "PREFIX foaf: <http://xmlns.com/foaf/0.1/> ";
	public static final String PREFIX_RSS = "PREFIX rss: <http://purl.org/rss/1.0/> ";
	public static final String PREFIX_RDF = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ";
	public static final String PREFIX_QUERY = "PREFIX query: <http://rdf.pace-project.org/diki#> ";
	public static final String ASSOCIATEDTAG = "associatedTag";
	public static final String TAGNAME = "name";
}
