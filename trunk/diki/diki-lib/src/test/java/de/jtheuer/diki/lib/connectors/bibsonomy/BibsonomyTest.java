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
	
package de.jtheuer.diki.lib.connectors.bibsonomy;
import java.io.IOException;
import java.util.logging.Logger;

import org.apache.commons.httpclient.HttpException;
import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.RDFParseException;

import de.jtheuer.diki.lib.ConnectorTestCase;
import de.jtheuer.diki.lib.MockNetworkConnection;
import de.jtheuer.diki.lib.connectors.ConnectorException;
import de.jtheuer.diki.lib.query.QueryException;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 */
public class BibsonomyTest extends ConnectorTestCase {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(BibsonomyTest.class.getName());
	private BibsonomyConnector conn;
	private MockNetworkConnection nc;

	/**
	 * @param name
	 */
	public BibsonomyTest(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		nc = new MockNetworkConnection();
		conn = new BibsonomyConnector(nc);
		nc.addConnector(conn);
		
	}
	
	public void testDummy() {
		
	}
	
	public void externalTestConnectionToServer() throws HttpException, RDFParseException, RepositoryException, IOException, QueryException, ConnectorException {
		nc.connect(getDefaults());
		
		conn.evaluate(null);
		
	
	}
}
