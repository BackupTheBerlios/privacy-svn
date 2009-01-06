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
import java.util.logging.Logger;

import junit.framework.TestCase;

import org.openrdf.concepts.rss.RssResource;
import org.openrdf.model.Statement;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.RepositoryResult;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 */
public class WriteableContextImplTestCase extends TestCase implements Statements {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(WriteableContextImplTestCase.class.getName());
	private WriteableContextImpl wcon;

	/**
	 * @param name
	 */
	public WriteableContextImplTestCase(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		REPOSITORY.initialize();
		wcon = new WriteableContextImpl(REPOSITORY,null,CONTEXT1);
	}
	
	public final void testAdd() throws RepositoryException {
		assertTrue(wcon.size()==0l);
		
		wcon.add(STATEMENT1);
		assertTrue(wcon.size()==1l);
		
		wcon.clear();
		assertTrue(wcon.size()==0l);
		
		wcon.add(STATEMENT1);
		assertNotNull(wcon.createManager().find(SUBJECT1));
		
		wcon.clear();
		RssResource res = wcon.createManager().designate(CONTEXT1.toQName(),RssResource.class);
		res.setDcDescription("foo");
		assertTrue(wcon.size()==2);
	}
	
	
}
