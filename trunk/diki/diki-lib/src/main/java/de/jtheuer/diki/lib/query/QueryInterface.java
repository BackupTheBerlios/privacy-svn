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

import org.openrdf.query.QueryLanguage;

import de.jtheuer.sesame.QNameURI;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 * A simple container for QueryStrings. Subclasses allow building of pre-defined Queries.
 * Although mainly SPARQL is supported, it is independant.
 * 
 * Recipients may be further defined. 
 */
public interface QueryInterface {

	/**
	 * @return a valid query that can be passed to a resolver (e.g. a database)
	 */
	public abstract String getQueryString();

	/**
	 * @return the Query Language if any
	 */
	public abstract QueryLanguage getQueryLanguage();

	/**
	 * @param recipient
	 * @return true if the supplied recipient is intended for this query
	 */
	public abstract boolean isRecipient(QNameURI recipient);

}