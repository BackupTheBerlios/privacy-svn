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
 * Interface for queries. Abstracts from the concrete implementation, e.g. SPARQL.
 *
 */
public abstract class AbstractQuery implements QueryInterface {
	
	/* (non-Javadoc)
	 * @see de.jtheuer.diki.lib.query.QueryInterface#getQueryString()
	 */
	public abstract String getQueryString();
	
	/* (non-Javadoc)
	 * @see de.jtheuer.diki.lib.query.QueryInterface#getQueryLanguage()
	 */
	public abstract QueryLanguage getQueryLanguage();

	/* (non-Javadoc)
	 * @see de.jtheuer.diki.lib.query.QueryInterface#isRecipient(de.jtheuer.diki.lib.util.QNameURI)
	 */
	public abstract boolean isRecipient(QNameURI recipient);
	
}
