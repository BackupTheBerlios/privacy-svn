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
	
package de.jtheuer.diki.lib;
import java.io.*;
import java.util.Properties;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import junit.framework.TestCase;
import de.jtheuer.diki.lib.connectors.bibsonomy.BibsonomyConnector;
import de.jtheuer.diki.lib.connectors.sesame.SesameConnector;
import de.jtheuer.diki.lib.connectors.xmpp.XMPPConnector;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 */
public abstract class ConnectorTestCase extends TestCase {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(ConnectorTestCase.class.getName());

	/**
	 * @param name
	 */
	public ConnectorTestCase(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public static Properties getDefaults() {
		Properties p = new Properties();
		p.setProperty(SesameConnector.STORAGETYPE,SesameConnector.STORAGETYPE_RAM);
		p.setProperty(XMPPConnector.KEY_HOST, "v-tml.uni-muenster.de");
		p.setProperty(XMPPConnector.KEY_USER, "debugger");
		p.setProperty(XMPPConnector.KEY_PASSWORD, "debugger");
		p.setProperty(BibsonomyConnector.KEY_PASSWORD, "debugger");
		p.setProperty(BibsonomyConnector.KEY_USER, "debugger");
		return p;
	}
	
	protected static <V> ExecutorCompletionService<V> getECS() {
		ExecutorCompletionService<V> ecs = new ExecutorCompletionService<V>(Executors.newCachedThreadPool());
		return ecs;
	}
	
	public String getRDFDummy() {
		BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/rdfresult1.xml")));
		StringBuilder b = new StringBuilder();
		try {
			while(br.ready()) {
				b.append(br.readLine());
			}
		} catch (IOException e) {
			fail(e.getMessage());
		}
		
		return b.toString();
	}
}
