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
import java.util.logging.Logger;

import junit.framework.TestCase;

import org.openrdf.model.Statement;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.sail.memory.MemoryStore;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 */
public class SesameContextWrapperTest extends TestCase implements Statements{
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(SesameContextWrapperTest.class.getName());

	private SesameContextWrapper wrapper;
	private Repository repository;

	/**
	 * @param name
	 */
	public SesameContextWrapperTest(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		repository =  new SailRepository(new MemoryStore());
		repository.initialize();
		wrapper = new SesameContextWrapper(repository,CONTEXT1);
	}

	
	public final void testCreateChild() throws RepositoryException {
		wrapper.add(STATEMENT1);
		assertTrue(wrapper.size()==1);
		
		SesameContextWrapper child = wrapper.createChild();
		assertTrue(child.size()==1);
		assertTrue(wrapper.size()==1);
		
		child.add(STATEMENT2);
		assertTrue(child.size()==2);
		assertTrue(wrapper.size()==1);
		
		SesameContextWrapper child2 = wrapper.createChild();
		child2.add(STATEMENT3);
		assertTrue(child.size()==2);
		assertTrue(child2.size()==2);
		assertTrue(wrapper.size()==1);
		
		/* create full context */
		SimpleContext full = wrapper.createFullContext();

		List<Statement> statements = full.getStatements(null).asList();
		assertTrue(statements.contains(STATEMENT1));
		assertTrue(statements.contains(STATEMENT2));
		assertTrue(statements.contains(STATEMENT3));
		
		assertNotNull(full.createManager().find(SUBJECT1));
		assertNotNull(full.createManager().find(SUBJECT2));
		assertNotNull(full.createManager().find(SUBJECT3));
	}
}
