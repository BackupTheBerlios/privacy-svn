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

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.openrdf.query.parser.sparql.ast.*;

import uk.co.holygoat.tag.concepts.Tagging;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * 
 * A Sparql query for tags.
 */
public class SparqlTagQuery extends SparqlQuery implements SPARQLPrefix {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(SparqlTagQuery.class.getName());

	private String[] tags;

	/**
	 * Constructs a query where all the passed tags (delimited by space) are
	 * combined with "AND"
	 */
	public SparqlTagQuery(String tags) {
		this(tags.split(" "));
	}

	/**
	 * Constructs a query where the passed tags are combined with "AND"
	 */
	public SparqlTagQuery(String[] tags) {
		super(init(tags));
		this.tags = tags;
	}

	/**
	 * @param split
	 */
	private static String init(String[] tags) {
		/*
		 * query template: CONSTRUCT { [] query:hasResult ?tagging. ?tagging
		 * tag:taggedBy ?whom. ?whom foaf:name ?name. ?tagging ?p ?o }
		 * 
		 * WHERE { ?tagging tag:associatedTag ?tag1 . ?tagging tag:associatedTag
		 * ?tag2 . ?tag1 tag:name "tagname1" . ?tag2 tag:name "tagname2" . }
		 */
		StringBuilder b = new StringBuilder();
		b.append(PREFIX_TAGGING + "\n");
		b.append(PREFIX_FOAF + "\n");
		b.append(PREFIX_QUERY + "\n");
		b.append("CONSTRUCT { [] query:result ?tagging .");
		b.append("      ?tagging a tag:Tagging . ");
		b.append("      ?creator a foaf:Person . ");
		b.append("		?tagging ?p ?o . ");
		b.append("		?o ?p1 ?o1 . ");
		b.append("      } ");
		b.append("WHERE { ");
		b.append("?tagging a tag:Tagging .");
		for (int i = 0; i < tags.length; i++) {
			String string = tags[i];
			b.append("?tagging tag:associatedTag ?tag" + i + ". ");
			b.append("?tag" + i + " tag:name \"" + string + "\" . ");
		}
		b.append("?tagging ?p ?o . ");
		b.append("OPTIONAL { ");
		b.append("?tagging tag:taggedBy ?creator . ");
		b.append("?o ?p1 ?o1 . ");
		b.append("}}");

		return b.toString();
	}

	/**
	 * @return a list of tags that represent the query.
	 */
	public String[] getTags() {
		return tags;
	}

	/**
	 * Method to retrieve Tags from any query. The query is analyzed and the
	 * WHERE clause is extracted. May fail if the Query is not in the expected
	 * format.
	 * 
	 * @param query
	 * @return a list of tag strings or an empty list if no tags can be found
	 */
	public static List<String> getTagsFromQuery(QueryInterface query) {
		List<String> tags = new LinkedList<String>();
		String s = query.getQueryString();

		try {
			ASTQueryContainer qc = SyntaxTreeBuilder.parseQuery(s);

			ASTGraphPatternGroup x = qc.getQuery().getWhereClause().getGraphPatternGroup();

			ASTBasicGraphPattern graph = x.jjtGetChild(ASTBasicGraphPattern.class);

			List<ASTTriplesSameSubject> tripleslist = graph.jjtGetChildren(ASTTriplesSameSubject.class);
			for (ASTTriplesSameSubject triplesSameSubject : tripleslist) {
				ASTPropertyList predicate = triplesSameSubject.jjtGetChild(ASTPropertyList.class);
				if (predicate.getVerb().toString().contains(TAGNAME)) {
					ASTObjectList objects = predicate.getObjectList();
					List<ASTRDFLiteral> values = objects.jjtGetChildren(ASTRDFLiteral.class);
					for (ASTRDFLiteral literal : values) {
						tags.add(literal.getLabel().getValue());
					}
				}
			}
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
			/* cannot parse the query. Ignore it and return whatever we have */
		}
		return tags;

	}
}
