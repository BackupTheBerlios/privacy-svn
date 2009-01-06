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
package de.jtheuer.diki.lib.connectors.sesame;

import java.util.Iterator;
import java.util.logging.Logger;

import javax.xml.namespace.QName;

import junit.framework.TestCase;

import org.openrdf.concepts.foaf.Agent;
import org.openrdf.concepts.foaf.Person;
import org.openrdf.elmo.*;
import org.openrdf.elmo.sesame.SesameManagerFactory;
import org.openrdf.repository.Repository;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.sail.memory.MemoryStore;

import uk.co.holygoat.tag.concepts.Tag;
import uk.co.holygoat.tag.concepts.Tagging;
import de.jtheuer.sesame.SimpleSet;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * 
 */
public class ElmoTest extends TestCase {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(ElmoTest.class.getName());
	private SesameManagerFactory factory;

	/**
	 * @param name
	 */
	public ElmoTest(String name) {
		super(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		ElmoModule module = new ElmoModule();
		MemoryStore store = new MemoryStore();

		Repository repository = new SailRepository(store);
		repository.initialize();

		factory = new SesameManagerFactory(module, repository);
	}

	public final void testCreateConcept() {
		ElmoManager manager = factory.createElmoManager();
		QName qname = new QName("http://example.com/", "tagging1");
		Tag tag = manager.designate(Tag.class, qname);
		tag.setTagsNames(SimpleSet.create("Test"));

		Agent myself = manager.designate(Agent.class);
		myself.setFoafGivennames(SimpleSet.create((Object) "Jan"));

		Tagging tagging = manager.designate(Tagging.class);
		tagging.setTagsAssociatedTags(new SimpleSet<Tag>(tag));
		tagging.setTagsTaggedBy(new SimpleSet<Agent>(myself));

		Entity found = manager.find(qname);
		assertTrue(found.getQName().equals(qname));
	}
	
	public final void testSparql() {
		ElmoManager manager = factory.createElmoManager();
		String Tagname = "Test";
		QName qname = new QName("http://example.com/", "tagging1");

		Tag tag = manager.designate(Tag.class, qname);
		tag.setTagsNames(SimpleSet.create(Tagname));
		
		ElmoQuery query = manager.createQuery("SELECT ?tag WHERE { ?tag <http://www.holygoat.co.uk/owl/redwood/0.1/tags/name> ?x } ");
		Iterator<?> iterator = query.evaluate();

		while (iterator.hasNext()) {
			Object object = iterator.next();
			assertTrue(object instanceof Tag);
			assertTrue(object.equals(tag));
			assertTrue(((Tag) object).getTagsNames().contains(Tagname));
		}		
		
	}

	public final void testQuery() {
		ElmoManager manager = factory.createElmoManager();
		String Tagname = "Test";
		QName qname = new QName("http://example.com/", "tagging1");

		Tag tag = manager.designate(Tag.class, qname);
		//Set<String> names = new HashSet<String>();
		//names.add(Tagname);
		//tag.setTagsNames(names);
		tag.setTagsNames(SimpleSet.create(Tagname));
		assertTrue(tag.getTagsNames().contains(Tagname));

		Iterator<Tag> iterator = manager.findAll(Tag.class).iterator();

		while (iterator.hasNext()) {
			Object object = iterator.next();
			assertFalse(object instanceof Boolean);
			assertTrue(object instanceof Tag);
			assertTrue(object.equals(tag));
			System.out.println(object);
			assertTrue(((Tag) object).getTagsNames().contains(Tagname));
		}
	}
	
	public final void testAddToSet() {
		ElmoManager manager = factory.createElmoManager();
		QName qname = new QName("http://example.com/", "myself");
		
		Person person = manager.designate(Person.class,qname);
		Person friend = manager.designate(Person.class);
		person.setFoafKnows(SimpleSet.create(friend));
		assertTrue(person.getFoafKnows().contains(friend));
		
		Person should_be_person = (Person) manager.find(qname);
		assertEquals(person, should_be_person);
		
		Person another_friend = manager.designate(Person.class);
		should_be_person.getFoafKnows().add(another_friend);
		
		person=null;
		friend=null;
		should_be_person=null;
		another_friend=null;
		System.gc();
		
		Person person2 = (Person) manager.find(qname);
		assertTrue(person2.getFoafKnows().size() == 2);
		
	}
}
