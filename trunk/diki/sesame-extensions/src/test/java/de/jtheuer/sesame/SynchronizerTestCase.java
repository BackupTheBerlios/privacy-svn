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
import java.util.List;
import java.util.logging.*;

import org.openrdf.sail.memory.MemoryStore;

import org.openrdf.model.Statement;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailRepository;

import junit.framework.TestCase;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 */
public class SynchronizerTestCase extends TestCase implements Statements {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(SynchronizerTestCase.class.getName());
	private SailRepository repository;
	private Synchronizer sync;
	private WriteableContextImpl source;
	private WriteableContextImpl destination;

	/**
	 * @param name
	 */
	public SynchronizerTestCase(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		this.repository = new SailRepository(new MemoryStore());
		repository.initialize();
		source=new WriteableContextImpl(repository,CONTEXT1);
		destination=new WriteableContextImpl(repository,CONTEXT2);
		sync = new Synchronizer(source,destination);
		source.add(STATEMENT1);
		destination.add(STATEMENT2);
	}

	/**
	 * Test method for {@link de.jtheuer.sesame.Synchronizer#copyStatements(org.openrdf.model.Resource, org.openrdf.model.URI, org.openrdf.model.Value)}.
	 * @throws RepositoryException 
	 */
	public void testCopyStatements() throws RepositoryException {
		assertEquals(1,source.size());
		assertEquals(1,destination.size());
		sync.copyStatements(SUBJECT1, PREDICATE1, OBJECT1);
		assertEquals(1,source.size());
		assertEquals(2,destination.size());

	}

	/**
	 * Test method for {@link de.jtheuer.sesame.Synchronizer#replaceStatements(org.openrdf.model.Resource, org.openrdf.model.URI)}.
	 */
	public void testReplaceStatementsResourceURI() {
		//fail("Not yet implemented");
	}

	/**
	 * Test method for {@link de.jtheuer.sesame.Synchronizer#replaceStatements(org.openrdf.model.Resource)}.
	 * @throws RepositoryException 
	 */
	public void testReplaceStatementsResource() throws RepositoryException {
		destination.add(SUBJECT1, PREDICATE1, OBJECT2);
		destination.add(SUBJECT1, PREDICATE1, OBJECT3);
		
		sync.replaceStatements(SUBJECT1, PREDICATE1);
		List<Statement> list = destination.getAllStatements().asList();
		assertEquals(2,list.size());
		assertTrue(list.contains(STATEMENT1));
		assertTrue(list.contains(STATEMENT2));
	}

	/**
	 * Test method for {@link de.jtheuer.sesame.Synchronizer#replaceStatementsRecursive(org.openrdf.model.Resource)}.
	 */
	public void testReplaceStatementsRecursive() {
		//fail("Not yet implemented");
	}
}
