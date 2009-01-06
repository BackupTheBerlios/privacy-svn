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
	
package de.jtheuer.diki.lib.connectors.gate;
import java.util.concurrent.Executor;
import java.util.logging.Logger;

import de.jtheuer.diki.lib.NetworkConnection;
import de.jtheuer.diki.lib.query.*;
import de.jtheuer.sesame.QNameURI;
import de.jtheuer.sesame.SesameContextWrapper;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 * A GateQuery instance will be created if a message arrived from a {@link GateConnector}. (I.e. over xmpp)
 */
public class GateQuery extends RDFNetworkQuery {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(GateQuery.class.getName());
	private QNameURI sender;

	public GateQuery(NetworkConnection connection, QueryResultListener<String> result, QNameURI sender) throws QueryException {
		super(connection, result);
		this.sender = sender;
	}

	/**
	 * @param originalQuery
	 * @param connection
	 * @param resultListener
	 * @param child
	 * @param queryID
	 * @param executor
	 */
	public GateQuery(QueryInterface originalQuery, NetworkConnection connection, QueryResultListener<String> resultListener, SesameContextWrapper child,
			QNameURI queryID, Executor executor, QNameURI sender) {
		super(originalQuery, connection, resultListener, child, queryID, executor);
		this.sender = sender;
	}

	@Override
	public boolean isRecipient(QNameURI recipient) {
		return ! recipient.equals(sender);
	}

	@Override
	public NetworkQuery createChildQuery() throws QueryException {
		try {
			SesameContextWrapper child = getSesameContext().createChild();
			GateQuery q = new GateQuery(getOriginalQuery(), getConnection(), getResultListener(), child, getQueryID(),getExecutor(),sender);
			q.setExecutor(getExecutor());
			return q;
		} catch (Exception e) {
			throw new QueryException(e);
		}
	}
	
	


}
