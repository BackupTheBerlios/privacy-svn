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
	
package de.jtheuer.diki.lib.friends;
import java.util.logging.Logger;

import junit.framework.TestCase;

import org.openrdf.concepts.foaf.Person;
import org.openrdf.concepts.rdfs.Resource;
import org.openrdf.elmo.sesame.SesameManager;

import de.jtheuer.diki.lib.ConnectorTestCase;
import de.jtheuer.diki.lib.MockNetworkConnection;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 */
public class UserControllerTestCase extends TestCase {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(UserControllerTestCase.class.getName());
	private UserController uc;

	/**
	 * @param name
	 */
	public UserControllerTestCase(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		MockNetworkConnection nc = new MockNetworkConnection();
		nc.connect(ConnectorTestCase.getDefaults());
		SesameManager manager = nc.getSesame().getLocalStore().createManager();
		Resource any = manager.designate(Resource.class);
		Person p1 = manager.designate(Person.class);
		Person p2 = manager.designate(Person.class);
		nc.getRatingManager().setRatingFor(any,-1);
		this.uc = new UserController(nc);
	}
	
	public void testCalculateDistances() {
		
	}
}
