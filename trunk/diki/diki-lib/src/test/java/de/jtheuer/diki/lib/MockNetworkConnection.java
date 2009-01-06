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
import java.util.logging.Logger;

import de.jtheuer.diki.lib.connectors.ConnectorException;
import de.jtheuer.diki.lib.connectors.gate.GateConnector;
import de.jtheuer.diki.lib.connectors.gate.MockGateConnector;
import de.jtheuer.diki.lib.connectors.sesame.SesameConnector;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 */
public class MockNetworkConnection extends NetworkConnection {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(MockNetworkConnection.class.getName());
	
	/**
	 * simple Mock Class with only a SesameMemoryStore 
	 * @throws ConnectorException 
	 */
	public MockNetworkConnection() throws ConnectorException {
		super();
	}
	
	/* (non-Javadoc)
	 * @see de.jtheuer.diki.lib.NetworkConnection#newSesame()
	 */
	@Override
	protected SesameConnector newSesame() {
		return super.newSesame();
	}
	
	/* (non-Javadoc)
	 * @see de.jtheuer.diki.lib.NetworkConnection#newXMPP()
	 */
	@Override
	protected GateConnector<String> newXMPP()  {
		return new MockGateConnector();
	}
	
	@Override
	public void connect(Properties p) throws ConnectorException {
		super.connectSynchronosly(p);
	}

	public String getRDFDummy() throws RuntimeException {
		BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/rdfresult1.xml")));
		StringBuilder b = new StringBuilder();
		try {
			while(br.ready()) {
				b.append(br.readLine());
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		return b.toString();
	}

	
	
	
	

	
	
}
