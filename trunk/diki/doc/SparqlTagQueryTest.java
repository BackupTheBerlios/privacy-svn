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
import java.util.*;
import java.util.logging.Logger;

import junit.framework.TestCase;

import org.openrdf.elmo.*;
import org.openrdf.elmo.sesame.SesameManagerFactory;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.sail.memory.MemoryStore;

import uk.co.holygoat.tag.concepts.Tag;
import uk.co.holygoat.tag.concepts.Tagging;
import de.jtheuer.diki.lib.util.SimpleSet;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 */
public class SparqlTagQueryTest extends TestCase {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(SparqlTagQueryTest.class.getName());
	private SesameManagerFactory factory;
	private final static String[] TAGNAMES = new String[]{"foo","bar"};
	private final static String[] INVALIDTAGS = new String[]{"invalid"};
	
	/**
	 * @param name
	 */
	public SparqlTagQueryTest(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		ElmoModule elmo = new ElmoModule();
		SailRepository sail = new SailRepository(new MemoryStore());
		sail.initialize();
		factory = new SesameManagerFactory(elmo,sail);
	}

	/**
	 * Test method for {@link de.jtheuer.diki.lib.query.SparqlTagQuery#SparqlTagQuery(java.lang.String[])}.
	 */
	public void testSparqlTagQueryStringArray() {
		ElmoManager elmo = factory.createElmoManager();
		Tagging tagging = elmo.create(Tagging.class);
		
		Tag tag1 = elmo.create(Tag.class);
		tag1.setTagsNames(SimpleSet.create(TAGNAMES[0]));
		Tag tag2 = elmo.create(Tag.class);
		tag2.setTagsNames(SimpleSet.create(TAGNAMES[1]));

		Set<Tag> tags= new HashSet<Tag>();
		tags.add(tag1);
		tags.add(tag2);
		tagging.setTagsAssociatedTags(tags);

		SparqlTagQuery stq = new SparqlTagQuery(TAGNAMES);
		String query = stq.getQuery();
		
		ElmoQuery results = (ElmoQuery) elmo.createQuery(query);
		Iterator<Tagging> iterator = (Iterator<Tagging>) results.evaluate();
		
		assertTrue(iterator.hasNext());
		assertTrue(iterator.next().getTagsAssociatedTags().contains(tag1));
		
		SparqlTagQuery invalid = new SparqlTagQuery(INVALIDTAGS);
		Iterator<?> results_invalid = elmo.createQuery(invalid.getQuery()).evaluate();
		assertFalse(results_invalid.hasNext());
			
	}

}
