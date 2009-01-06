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
import org.openrdf.concepts.foaf.Person;

import de.jtheuer.sesame.QNameURI;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * 
 * @see Person
 *
 */
public interface FoafPrefixes {
	
	public static final String NAMEPSPACE = "http://xmlns.com/foaf/0.1/";

	public static final String FIRSTNAME = NAMEPSPACE+"firstName";
	public static final QNameURI FIRSTNAME_URI = new QNameURI(FIRSTNAME);
	
	public static final String SURNAME = NAMEPSPACE+"surname";
	public static final QNameURI SURNAME_URI = new QNameURI(SURNAME);

	public static final String IMG = NAMEPSPACE+"img";
	public static final QNameURI IMG_URI = new QNameURI(IMG);

}
