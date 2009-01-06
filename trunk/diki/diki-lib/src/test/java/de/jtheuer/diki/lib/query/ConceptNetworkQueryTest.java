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

import junit.framework.TestCase;

import org.openrdf.repository.RepositoryException;

import de.jtheuer.diki.lib.ConnectorTestCase;
import de.jtheuer.diki.lib.MockNetworkConnection;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * 
 */
public class ConceptNetworkQueryTest extends TestCase {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(ConceptNetworkQueryTest.class.getName());
	private MockNetworkConnection nc;
	private MockQuery q;
	private ConceptNetworkQuery c;

	/**
	 * @param name
	 */
	public ConceptNetworkQueryTest(String name) {
		super(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		nc = new MockNetworkConnection();
		nc.connect(ConnectorTestCase.getDefaults());
		
		q = new MockQuery();
		c = new ConceptNetworkQuery(q, new MockQueryResult(), nc, 10);
	}

	public final void testCreate() throws Exception {

		
		assertTrue(c.getElmoQuery().getDistance()==10);
		assertEquals(q.getQueryString(),c.getElmoQuery().getSparql());
	}
	
	public final void testRDF() throws Exception {
		String rdf = c.getRDFQuery();
		assertTrue(rdf.contains("10")); //distance
		assertTrue(rdf.contains("DESCRIBE")); //query
	}
	
	public final void testChild() throws RepositoryException, QueryException {
		NetworkQuery child = c.createChildQuery();
		
		assertEquals(child.getQueryString(),c.getQueryString());
		assertEquals(child.getQueryID(),c.getQueryID());
		assertEquals(child.getRDFQuery(),c.getRDFQuery());
	}
}
