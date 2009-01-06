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

import org.openrdf.concepts.foaf.Person;
import org.openrdf.elmo.ElmoModule;
import org.openrdf.elmo.sesame.SesameManager;
import org.openrdf.elmo.sesame.SesameManagerFactory;
import org.openrdf.model.Statement;
import org.openrdf.query.GraphQueryResult;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.rio.ntriples.NTriplesWriter;
import org.openrdf.sail.memory.MemoryStore;

import de.jtheuer.sesame.QNameURI;
import de.jtheuer.sesame.SimpleSet;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 */
public class SparqlFriendsQueryTestCase extends TestCase {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(SparqlFriendsQueryTestCase.class.getName());
	private static final QNameURI MYSELF = new QNameURI("http://example.com/myself");
	private static final QNameURI OTHER = new QNameURI("http://example.com/other");
	
	private SparqlUserInformationQuery sparql;
	private SailRepository repository;
	
	/**
	 * @param name
	 */
	public SparqlFriendsQueryTestCase(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		this.sparql = new SparqlUserInformationQuery(MYSELF);
		
		MemoryStore store = new MemoryStore();
		repository = new SailRepository(store);
		repository.initialize();
	}
	
	public void testFriends() throws Throwable {
		SesameManager manager = new SesameManagerFactory(new ElmoModule(),repository).createElmoManager();
		Person other = manager.designate(Person.class,OTHER.toQName());
		other.setFoafNicks(SimpleSet.create("other nickname"));
		manager.designate(Person.class,MYSELF.toQName()).setFoafKnows(SimpleSet.create(other));
		
		String query =  sparql.getQueryString(); // SPARQLPrefix.PREFIX_QUERY + SPARQLPrefix.PREFIX_FOAF + "CONSTRUCT { <http://example.com/result> query:hasResult <http://example.com/myself>. <http://example.com/myself> foaf:knows ?other. <http://example.com/myself> ?p ?o. } WHERE { <http://example.com/myself> ?p ?o. <http://example.com/myself> foaf:knows ?other. }";
		LOGGER.info(query);
		repository.getConnection().export(new NTriplesWriter(System.out));
		repository.getConnection().prepareGraphQuery(sparql.getQueryLanguage(), query).evaluate(new NTriplesWriter(System.out));
		
		GraphQueryResult result = repository.getConnection().prepareGraphQuery(sparql.getQueryLanguage(), sparql.getQueryString()).evaluate();
		
		while(result.hasNext()) {
			Statement s = result.next();
			if(s.getSubject().equals(OTHER)) {
				return;
			}
		}
		
		fail();
	}
}
