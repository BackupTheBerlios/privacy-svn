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

import de.jtheuer.sesame.QNameURI;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 * Creates a SPARQL query that returns a Query from the repository
 */
public class SparqlQueryQuery extends SparqlQuery {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(SparqlQueryQuery.class.getName());

	/**
	 * 
	 */
	public SparqlQueryQuery(QNameURI uri) {
		super(init(uri));
	}
	
	/**
	 * @param split
	 */
	private static String init(QNameURI uri) {
		StringBuilder b = new StringBuilder();
		b.append("DESCRIBE ?s ?o WHERE {\n"); //?tag1 ?level1
		b.append("<" + uri.toString() + "> ?p ?o .\n");
		b.append("}");
		return b.toString();
	}
}
