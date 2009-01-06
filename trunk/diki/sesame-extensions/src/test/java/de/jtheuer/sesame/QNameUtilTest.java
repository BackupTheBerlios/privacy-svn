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

import junit.framework.TestCase;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 */
public class QNameUtilTest extends TestCase {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(QNameUtilTest.class.getName());

	/**
	 * @param name
	 */
	public QNameUtilTest(String name) {
		super(name);
	}

	public void testSplit() {
		final String ns = "xmpp://foo.example.com/test/foo#";
		final String lp = "bar";
		QNameURI qname = new QNameURI(ns+lp);
		assertEquals(ns,qname.getNamespace());
		assertEquals(lp,qname.getLocalName());
	}
	
	public void testEmptyLocalpart() {
		final String ns = "xmpp://foo.example.com/";
		final String lp = "";
		QNameURI qname = new QNameURI(ns+lp);
		assertEquals(ns,qname.getNamespace());
		assertEquals(lp,qname.getLocalName());
	}
	
}
