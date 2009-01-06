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
	
package de.jtheuer.diki.lib.query.elmo;
import java.util.logging.Logger;

import junit.framework.TestCase;

import org.openrdf.concepts.foaf.Agent;
import org.openrdf.concepts.foaf.Person;
import org.openrdf.concepts.rss.RssResource;
import org.openrdf.elmo.ElmoModule;
import org.openrdf.elmo.sesame.SesameManager;
import org.openrdf.elmo.sesame.SesameManagerFactory;
import org.openrdf.repository.Repository;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.sail.memory.MemoryStore;
import org.paceproject.diki.elmo.Query;
import org.paceproject.diki.elmo.QueryResponse;

import de.jtheuer.sesame.SimpleSet;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 */
public class DikiElmoQueryTestCase extends TestCase {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(DikiElmoQueryTestCase.class.getName());
	private SesameManagerFactory factory;

	/**
	 * @param name
	 */
	public DikiElmoQueryTestCase(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		MemoryStore store = new MemoryStore();
		Repository rep = new SailRepository(store);
		rep.initialize();
		ElmoModule module = new ElmoModule();
		module.addRole(Query.class);
		module.addRole(QueryResponse.class);
		factory = new SesameManagerFactory(module,rep);
	}
	
	public void testAvailable() {
		SesameManager manager = factory.createElmoManager();
		Agent person = manager.designate(Person.class);
		Query query = manager.designate(Query.class);
		query.setCreatedBy(SimpleSet.create(person));
		query.setDistance(10);
		query.setSparql("SELECT FOO BAR");
		manager.close();
	}

	public void testQueryAndResponse() {
		SesameManager manager = factory.createElmoManager();
		Agent person = manager.designate(Person.class);
		
		Query query = manager.designate(Query.class);
		query.setCreatedBy(SimpleSet.create(person));
		query.setDistance(10);
		query.setSparql("SELECT FOO BAR");

		Object something = manager.designate(RssResource.class); 
		QueryResponse response = manager.designate(QueryResponse.class);
		response.setAnswersQuery(query);
		response.setResult(SimpleSet.create(something));
		
		assertEquals(response.getAnswersQuery(), query);
		
		manager.close();
	}

	
}
