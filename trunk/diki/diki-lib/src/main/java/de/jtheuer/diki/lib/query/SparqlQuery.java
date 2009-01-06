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
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import org.openrdf.query.QueryLanguage;

import de.jtheuer.sesame.QNameURI;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 */
public class SparqlQuery extends AbstractQuery {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(SparqlQuery.class.getName());

	private String query;
	private Set<QNameURI> blacklist;

	public SparqlQuery() {
		this.blacklist = new HashSet<QNameURI>();
	}
	
	public SparqlQuery(String query) {
		this();
		this.query = query;
	}
	
	public SparqlQuery(String query, Set<QNameURI> blacklist) {
		super();
		this.query = query;
		this.blacklist = blacklist;
	}
	
	/**
	 * Creates a new SPARQL query with the given querystring
	 * @param query
	 * @param blacklist recipients that are not allowed to get this query. Useful in recurisive queries where the sender shouldn't get the query back
	 */
	public SparqlQuery(String query, QNameURI ...blacklist) {
		super();
		this.query = query;
		
		this.blacklist = new HashSet<QNameURI>(blacklist.length);
		for (QNameURI string : blacklist) {
			this.blacklist.add(string);
		}
	}

	
	/* (non-Javadoc)
	 * @see de.jtheuer.diki.lib.query.AbstractQuery#getQueryString()
	 */
	@Override
	public String getQueryString() {
		return query;
	}

	/* (non-Javadoc)
	 * @see de.jtheuer.diki.lib.query.Query#getQueryLanguage()
	 */
	@Override
	public QueryLanguage getQueryLanguage() {
		return QueryLanguage.SPARQL;
	}

	/* (non-Javadoc)
	 * @see de.jtheuer.diki.lib.query.AbstractQuery#isRecipient(de.jtheuer.diki.lib.util.QNameURI)
	 */
	@Override
	public boolean isRecipient(QNameURI recipient) {
		return ! blacklist.contains(recipient);
	}

}
