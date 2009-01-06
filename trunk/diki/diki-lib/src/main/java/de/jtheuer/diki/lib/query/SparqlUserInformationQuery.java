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

import org.openrdf.concepts.foaf.Person;

import de.jtheuer.sesame.QNameURI;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * 
 * A Sparql query that returns information about a given {@link Person} and all of his Friends ({@link Person#getFoafKnows()}).
 * 
 */
public class SparqlUserInformationQuery extends SparqlQuery implements SPARQLPrefix {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(SparqlUserInformationQuery.class.getName());
	private QNameURI to;

	/**
	 * @param query
	 */
	public SparqlUserInformationQuery(String myself) {
		super(getQuery(myself));
	}

	/**
	 * Creates a query that is only sent to one single recipient. Normally, both
	 * parameters should point to the same entity.
	 * 
	 * @param to
	 *            the only recipient who may get the query
	 */
	public SparqlUserInformationQuery(QNameURI to) {
		super(getQuery(to));
		this.to = to;
	}

	@Override
	public boolean isRecipient(QNameURI recipient) {
		return recipient.equals(to);
	}

	public static String getQuery(String person) {
		/*
		 * query template: SELECT ?tagging WHERE { ?tagging tag:associatedTag
		 * ?tag1 . ?tagging tag:associatedTag ?tag2 . ?tag1 tag:name "tagname1" .
		 * ?tag2 tag:name "tagname2" . }
		 */
		final String p = "<" + person + ">";
		StringBuilder b = new StringBuilder();
		b.append(PREFIX_FOAF + "\n");
		b.append(PREFIX_QUERY + "\n");
		b.append("CONSTRUCT { [] query:result " + p + ".");
		b.append("      " + p + " a foaf:Person. ");
//		b.append("		" + p + " foaf:knows ?other. ");
		b.append("		" + p + " ?p2 ?o2. ");
		b.append("		?img a foaf:Image. ");
		b.append("		?homepage a foaf:Document. ");
		b.append("		?other a foaf:Person. "); // Add information about their friends
		b.append("		?other foaf:nick ?o. ");
		b.append("      } ");
		b.append("WHERE { ");
		b.append(p + " ?p2 ?o2. ");
		b.append("OPTIONAL { ");
		b.append(p + " foaf:img ?img. ");
		b.append("} OPTIONAL { ");
		b.append(p + " foaf:homepage ?homepage. ");
		b.append("} OPTIONAL { ");
		b.append(p + " foaf:knows ?other. ");
		b.append("?other foaf:nick ?othernick. ");
		b.append("}}");
		return b.toString();
	}

	public static String getQuery(QNameURI to) {
		return getQuery(to.toString());
	}
}
