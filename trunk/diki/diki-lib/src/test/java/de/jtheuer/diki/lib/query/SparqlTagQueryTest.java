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
import java.util.List;
import java.util.logging.Logger;

import javax.xml.namespace.QName;

import junit.framework.TestCase;

import org.openrdf.concepts.foaf.Person;
import org.openrdf.elmo.ElmoModule;
import org.openrdf.elmo.sesame.SesameManager;
import org.openrdf.elmo.sesame.SesameManagerFactory;
import org.openrdf.query.*;
import org.openrdf.query.parser.sparql.ast.ParseException;
import org.openrdf.query.parser.sparql.ast.TokenMgrError;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.ntriples.NTriplesWriter;
import org.openrdf.sail.memory.MemoryStore;

import uk.co.holygoat.tag.concepts.Tag;
import uk.co.holygoat.tag.concepts.Tagging;
import de.jtheuer.sesame.SimpleSet;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 */
public class SparqlTagQueryTest extends TestCase {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(SparqlTagQueryTest.class.getName());
	private static final String[] TAGS = new String[]{"news","it", "foo","bar"};
	private SparqlTagQuery sparql;
	private SailRepository repository;

	/**
	 * @param name/r
	 */
	public SparqlTagQueryTest(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		this.sparql = new SparqlTagQuery(TAGS);
		
		MemoryStore store = new MemoryStore();
		repository = new SailRepository(store);
		repository.initialize();
	}
	
	public final void test() throws MalformedQueryException, TokenMgrError, ParseException, RDFParseException, RepositoryException, IOException, QueryEvaluationException {
		String[] tags = sparql.getTags();
		String query = sparql.getQueryString();
		
		List<String> parsedtags = SparqlTagQuery.getTagsFromQuery(sparql);
		
		/* assert same amount of tags */
		assertEquals(TAGS.length, parsedtags.size());
		
		/* assert all TAGS are in parsedtags */ 
		for (String string : TAGS) {
			assertTrue(parsedtags.contains(string));
		}
	}
	
	public final void testConstruct() throws Exception {
		SesameManagerFactory factory = new SesameManagerFactory(new ElmoModule(), repository);
		
		SesameManager manager = factory.createElmoManager();
		
		Tagging tagging = manager.designate(Tagging.class,new QName("http://www.example.com"));
		Tag tag1 = manager.designate(Tag.class);
		tag1.setTagsNames(SimpleSet.create("foo"));
		Tag tag2 = manager.designate(Tag.class);
		tag2.setTagsNames(SimpleSet.create("bar"));
		Person person = manager.designate(Person.class);
		person.setFoafNames(SimpleSet.create("horst"));
		tagging.setTagsTaggedBy(SimpleSet.create(person));
		tagging.setTagsAssociatedTags(SimpleSet.create(tag1,tag2));
		
		manager.close();
		String query = new SparqlTagQuery("foo").getQueryString();
		LOGGER.info(query);
		
		StringWriter w = new StringWriter();
		repository.getConnection().export(new NTriplesWriter(w));
		w.write("--\n");
		repository.getConnection().prepareGraphQuery(QueryLanguage.SPARQL, query).evaluate(new NTriplesWriter(w));
		
		LOGGER.info(w.toString());
		
		
		
		
	}
	
	
		
		
}
