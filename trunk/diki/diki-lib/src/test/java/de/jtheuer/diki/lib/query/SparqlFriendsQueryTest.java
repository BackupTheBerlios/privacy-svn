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

import javax.xml.namespace.QName;

import junit.framework.TestCase;

import org.openrdf.elmo.ElmoModule;
import org.openrdf.elmo.sesame.SesameManagerFactory;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.sail.memory.MemoryStore;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 */
public class SparqlFriendsQueryTest extends TestCase {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(SparqlFriendsQueryTest.class.getName());
	private SailRepository repository;
	private SesameManagerFactory factory;
	private final static QName MYSELF = new QName("http://example.com/","myself");

	/**
	 * @param name
	 */
	public SparqlFriendsQueryTest(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		repository = new SailRepository(new MemoryStore());
		repository.initialize();
		factory = new SesameManagerFactory(new ElmoModule(),repository);
		
	}
	
	public final void testFriendQuery() {
//		SesameManager manager = factory.createElmoManager();
//		
//		Person a1 = manager.designate(Person.class);
//		Person a2 = manager.designate(Person.class);
//		Person a3 = manager.designate(Person.class);
//		
//		Person myself = manager.designate(Person.class,MYSELF);
//		myself.setFoafKnows(new SimpleSet<Person>(a1,a2,a3));
//		
//		SparqlUserInformationQuery query = new SparqlUserInformationQuery(MYSELF.getNamespaceURI()+MYSELF.getLocalPart());
//		String querystring = query.getQueryString().replaceFirst("DESCRIBE", "SELECT");
//		SesameQuery result = manager.createQuery(querystring);
//		List<?> list = result.getResultList();
//		assertTrue(list.contains(a1));
//		assertTrue(list.contains(a2));
//		assertTrue(list.contains(a3));
//		assertFalse(list.contains(myself));
	}
}
