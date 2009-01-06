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
import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

import javax.xml.namespace.QName;

import org.openrdf.concepts.foaf.Agent;
import org.openrdf.elmo.ElmoModule;
import org.openrdf.elmo.Entity;
import org.openrdf.elmo.sesame.SesameManager;
import org.openrdf.elmo.sesame.SesameManagerFactory;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.rio.rdfxml.RDFXMLWriter;
import org.openrdf.sail.memory.MemoryStore;

import de.jtheuer.diki.lib.ConnectorTestCase;
import de.jtheuer.diki.lib.query.result.ConceptQueryResult;
import de.jtheuer.diki.lib.util.QNameURI;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 */
public class RDFQueryResultTest extends ConnectorTestCase {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(ConceptQueryResult.class.getName());
	private SesameManagerFactory factory;
	private StringWriter xmlrdf;
	private Agent agent;
	private QNameURI defaultNamespace;
	private int id=0;
	private SailRepository repository;
	private static final QNameURI QAgent = new QNameURI("http://example.com/","agent1");

	/**
	 * @param name
	 */
	public RDFQueryResultTest(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {

		defaultNamespace = new QNameURI("http://example.com/","default");
		ElmoModule module = new ElmoModule();
		module.setContext(defaultNamespace.toQName());
		
		MemoryStore store = new MemoryStore();
		repository = new SailRepository(store);
		repository.initialize();

		factory = new SesameManagerFactory(module, repository);

		SesameManager manager = factory.createElmoManager();
		agent = manager.designate(Agent.class, QAgent.toQName());
		agent.setFoafGender("male");
		
		xmlrdf = new StringWriter();
		manager.getConnection().export(new RDFXMLWriter(xmlrdf));
		LOGGER.info(xmlrdf.toString());
				
	}
	
	public void testInit() {
		Iterable<Entity> result = factory.createElmoManager().findAll(Entity.class);

		Iterator<Entity> i = result.iterator();
		
		assertTrue(i.next().getQName().equals(QAgent.toQName()));
		
		try {
			LOGGER.severe("found more elements: " +i.next().toString());
			fail("didn't expect more elements!");
		} catch(NoSuchElementException e) {
			
		}
	}
	
	public final void testEmptyContext() {
		ElmoModule empty = new ElmoModule();
		QName emptyQ = new QName("http://example.com/","new");
		empty.setContext(emptyQ);
		
		SesameManagerFactory fnew = new SesameManagerFactory(empty,repository);
		Iterable<Entity> result = fnew.createElmoManager().findAll(Entity.class);
		
		assertFalse(result.iterator().hasNext());
	}
	
	public void testQuery() throws QueryException {
		ElmoModule empty = new ElmoModule();
		
		QNameURI emptyQ1 = new QNameURI("http://example.com/","rdfquery");
		empty.setContext(emptyQ1.toQName());

		SesameManagerFactory fnew = new SesameManagerFactory(empty,repository);
		
		ConceptQueryResult q = new ConceptQueryResult(fnew.createElmoManager(), emptyQ1);
		q.add(xmlrdf.toString());

		Iterator<Entity> i = q.iterator();
		assertTrue(i.hasNext());
		assertEquals(agent,i.next());
	}
	
	public final void testStream() throws QueryException, IOException {

		ConceptQueryResult q = new ConceptQueryResult(factory.createElmoManager(), defaultNamespace);
		
		q.add(getRDFDummy());

		for (Entity entity : q) {
			LOGGER.info(entity.toString());
		}
		
	}
}
