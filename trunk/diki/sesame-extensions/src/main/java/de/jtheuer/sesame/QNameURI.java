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
package de.jtheuer.sesame;
import java.util.logging.Logger;

import javax.xml.namespace.QName;

import org.openrdf.model.URI;
import org.openrdf.model.impl.URIImpl;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 */
public class QNameURI extends URIImpl implements URI {
	/** generated */
	private static final long serialVersionUID = -5506528022260863151L;

	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(QNameURI.class.getName());
	
	public QNameURI(String namespaceURI, String localPart) {
		super(namespaceURI+localPart);
	}
	
	public QNameURI(String fullPath) {
		super(fullPath);
	}

	/**
	 * @param person_qname
	 */
	public QNameURI(QName qname) {
		super(qname.getNamespaceURI()+ qname.getLocalPart());
	}

	/**
	 * @param newLocalPart
	 * @return a new instance with the given localPart
	 */
	public QNameURI replaceLocalPart(String newLocalPart) {
		return new QNameURI(getNamespace(),newLocalPart);
	}
	
	/**
	 * Adds the supplied value to this {@link QNameURI}. (Does not affect this instance)
	 * @param newLocalPart
	 * @return the new instance like this + "/" + newLocalPart
	 */
	public QNameURI createNewLocalPart(String newLocalPart) {
		return new QNameURI(toString()+"/"+newLocalPart);
	}
	
	
	/**
	 * @return a corresponding QName instance 
	 */
	public QName toQName() {
		return new QName(getNamespace(), getLocalName());
	}
	
	/**
	 * @param qname the {@link QName} to test
	 * @return true if both namespace and localpart are equal to the qname's namespace and localpart.
	 */
	public boolean equalsQName(QName qname) {
		return qname.getNamespaceURI().equals(getNamespace()) && qname.getLocalPart().equals(getLocalName());
	}

	/**
	 * @param name
	 * @return
	 */
	public static String toString(QName name) {
		return name.getNamespaceURI()+name.getLocalPart();
	}
}
