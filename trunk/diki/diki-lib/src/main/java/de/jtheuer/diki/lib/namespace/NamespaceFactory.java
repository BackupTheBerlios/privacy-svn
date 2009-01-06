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
	
package de.jtheuer.diki.lib.namespace;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.jtheuer.diki.lib.connectors.gate.GateConnector;
import de.jtheuer.diki.lib.connectors.sesame.SesameConnector;
import de.jtheuer.sesame.QNameURI;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 */
public class NamespaceFactory {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(NamespaceFactory.class.getName());
	

	/* we start at the current time to ensure different ids between restarts */
	private long queryid = System.currentTimeMillis();
	/* we start at the current time to ensure different ids between restarts */
	private long resultid = System.currentTimeMillis();

	private GateConnector<?> gate;
	
	public NamespaceFactory(SesameConnector sesame, GateConnector<?> gate) {
		if(sesame == null || gate == null) {
			throw new IllegalArgumentException("Parameters must not be null");
		}
		this.gate = gate;
	}
	
	

	/**
	 * @return a query with an incrementing part like
	 *         xmpp:user@host/query/123/base
	 */
	public QNameURI createQueryIdentifier() {
		/* increment search identifier */
		final long localid = ++queryid;
		return constructNamespace("query/" + localid + "/base");
	}

	/**
	 * @return a unique result id like
	 * xmpp:user@host/result/123
	 */
	public QNameURI createResultIdentifier() {
		/* increment result identifier */
		final long localid = ++resultid;
		return constructNamespace("result/" + localid);
	}
	
	/**
	 * Creates a new URI from the given parameters.
	 * 
	 * @param namespace
	 *            a valid suffix, like /foo
	 * @param localpart
	 *            will be url encoded
	 * @return {@link #getNamespace()} namespace urlencode(localpart)
	 */
	public QNameURI constructNamespace(String namespace, String localpart) {
		return new QNameURI(getNamespace() + namespace, encode(localpart));
	}
	
	public QNameURI constructNamespace(String localpart) {
		return new QNameURI(getNamespace(), localpart);
	}
	
	/**
	 * @return the logged-in users namespace like <em>xmpp:juliet@server.tld/</em>
	 */
	public String getNamespace() {
		return gate.getUserNamespace();
	}
	
	/**
	 * @param string
	 * @return an utf-8 encoded string
	 */
	private static String encode(String string) {
		try {
			return URLEncoder.encode(string, "utf-8");
		} catch (UnsupportedEncodingException e) {
			LOGGER.log(Level.WARNING, e.getMessage());
			return string;
		}
	}



}
