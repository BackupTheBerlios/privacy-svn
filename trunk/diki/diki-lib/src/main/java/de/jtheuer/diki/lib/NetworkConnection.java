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
/**
    (c) by Jan Torben Heuer <jan.heuer@uni-muenster.de

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/
 */
package de.jtheuer.diki.lib;

import java.io.File;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.paceproject.diki.elmo.Query;

import de.jtheuer.diki.lib.connectors.Connector;
import de.jtheuer.diki.lib.connectors.ConnectorException;
import de.jtheuer.diki.lib.connectors.Connector.Status;
import de.jtheuer.diki.lib.connectors.bibsonomy.BibsonomyConnector;
import de.jtheuer.diki.lib.connectors.delicious.DeliciousConnector;
import de.jtheuer.diki.lib.connectors.gate.GateConnector;
import de.jtheuer.diki.lib.connectors.sesame.SesameConnector;
import de.jtheuer.diki.lib.connectors.xmpp.XMPPConnector;
import de.jtheuer.diki.lib.distance.RateingManager;
import de.jtheuer.diki.lib.friends.UserController;
import de.jtheuer.diki.lib.namespace.NamespaceFactory;
import de.jtheuer.diki.lib.pgp.SecurityHandler;
import de.jtheuer.diki.lib.query.*;

/**
 * Main class that holds references to all important other units
 */
public class NetworkConnection implements ConnectionListener {
	public final static String DEFAULT_HOME = ".diki";
	/* auto-generated Logger */@SuppressWarnings("unused")
	private final static Logger LOGGER = Logger.getLogger(NetworkConnection.class.getName());

	/**
	 * Dummy connection listener if none has been instantiated, yet.
	 */
	private List<ConnectionListener> connectionListener = new LinkedList<ConnectionListener>();
	protected SesameConnector connector_sesame;

	protected GateConnector<?> connector_xmpp;
	private List<Connector> connectors;;
	
	private ExecutorService executors = Executors.newCachedThreadPool();
	private NamespaceFactory namespace;

	private QueryFactory runner = new QueryFactory(this,executors);
	private UserController userController;
	private RateingManager rating;
	private SecurityHandler security;

	/**
	 * Creates a new NetworkConnection
	 */
	public NetworkConnection() {
		this((Connector[]) null);
	}

	/**
	 * Creates a new NetworkConnection
	 * 
	 * @param connectors.
	 */
	public NetworkConnection(Connector... connectors) {
		this.connectors = new LinkedList<Connector>();
		connector_sesame = newSesame();
		connector_xmpp = newXMPP();
		addConnector(connector_sesame);
		addConnector(connector_xmpp);
		if (connectors != null) {
			for (Connector connector : connectors) {
				addConnector(connector);
			}
		}
		
		/* initialize namespaces */
		namespace = new NamespaceFactory(connector_sesame,connector_xmpp);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jtheuer.diki.lib.INetworkConnection#addAllConnectors()
	 */
	public void addAllConnectors() {
		addConnector(new BibsonomyConnector(this));
		addConnector(new DeliciousConnector(this));
	}

	/**
	 * adds a ConnectionListener
	 * @param connectionListener
	 */
	public void addConnectionListener(ConnectionListener connectionListener) {
		this.connectionListener.add(connectionListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jtheuer.diki.lib.INetworkConnection#addConnector(de.jtheuer.diki.lib.connectors.Connector)
	 */
	public void addConnector(Connector c) {
		connectors.add(c);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jtheuer.diki.lib.INetworkConnection#connect(java.util.Properties)
	 */
	public void connect(final Properties properties) throws ConnectorException {
		executors.submit(new Callable<Object>() {
			@Override
			public Object call() throws Exception {
				try {
					connectSynchronosly(properties);
				} catch (NoClassDefFoundError e) {
					LOGGER.log(Level.SEVERE,"Got unhandled exception! Program may hang",e);
				}
				return true;
			}
		});
	}

	
	/**
	 * Connects all available {@link Connector} instances.
	 * 
	 * @param properties
	 * @throws ConnectorException
	 */
	protected void connectSynchronosly(Properties properties) throws ConnectorException {
		this.userController = new UserController(this);
		
		for (Connector c : connectors) {
			try {
				c.connect(properties);
			} catch (Exception e) {
				LOGGER.log(Level.WARNING, e.getMessage(), e);
				// TODO: gui feedback!
				// throw new ConnectorException(e);
			}
		}
		/* synchronize friends */
		this.userController.syncFriends();
		
		/* create rating manager */
		if(rating == null) {
			rating = new RateingManager(connector_sesame.getLocalStore(),namespace);
		}
		
		/* add SecurityHandler for pgp crypto functionalities */
		if(security==null) {
			security = new SecurityHandler(this,properties);
		}
		
	}

	/**
	 * disconnects all connectors from the network
	 */
	public void disconnect() {
		executors.submit(new Runnable() {
			@Override
			public void run() {
				disconnect_synchron();
			}
		});
	}

	protected void disconnect_synchron() {
		for (Connector c : connectors) {
			try {
				c.disconnect();
			} catch (Exception e) {
				LOGGER.log(Level.WARNING, "", e);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jtheuer.diki.lib.ConnectionListener#fireStatusUpdate()
	 */
	@Override
	public void fireStatusUpdate() {
		for (ConnectionListener l : connectionListener) {
			l.fireStatusUpdate();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jtheuer.diki.lib.INetworkConnection#getConnectors()
	 */
	public List<Connector> getConnectors() {
		return connectors;
	}

	public GateConnector<?> getGate() {
		return connector_xmpp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jtheuer.diki.lib.INetworkConnection#getHomeDirectory()
	 */
	public File getHomeDirectory() {
		File userhome = new File(System.getProperty("user.home"));
		if (userhome.exists()) {
			File apphome = new File(userhome + File.separator + DEFAULT_HOME);
			/* if apphome exists or is NOW created... */
			if (apphome.exists() || apphome.mkdir()) {
				return apphome;
			}
		} else {
			LOGGER.severe("Homedirectory " + userhome.getAbsolutePath() + " does not exsist!");
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see de.jtheuer.diki.lib.INetworkConnection#getNamespaceFactory()
	 */
	public NamespaceFactory getNamespaceFactory() {
		return namespace;
	}

	public QueryFactory getQueryFactory() {
		return runner;
	}

	public SesameConnector getSesame() {
		return connector_sesame;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jtheuer.diki.lib.INetworkConnection#getStatus()
	 */
	public Status getStatus() {
		if(connector_sesame.getStatus() == Status.Connected && connector_xmpp.getStatus() == Status.Connected) {
			return Status.Connected;
		} else if(connector_sesame.getStatus() == Status.Connecting || connector_xmpp.getStatus() == Status.Connecting) {
			return Status.Connecting;
		} else {
			return Status.Disconnected;
		}
	}

	/**
	 * @return the associates instance of the {@link UserController}.
	 */
	public UserController getUserFactory() {
		return userController;
	}
	
	/**
	 * @return the manager responsible for handling ratings
	 */
	public RateingManager getRatingManager() {
		return rating;
	}

	/**
	 * overwrite to use another {@link SesameConnector}
	 * @return
	 */
	protected SesameConnector newSesame() {
		return new SesameConnector(this);
	}

	/**
	 * overwrite to use another {@link GateConnector}
	 * @return
	 */
	protected GateConnector<?> newXMPP() {
		return new XMPPConnector(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jtheuer.diki.lib.INetworkConnection#runQuery(de.jtheuer.diki.lib.query.Query,
	 *      de.jtheuer.diki.lib.query.result.AbstractQueryResult)
	 */
	public Query runQuery(final QueryInterface query, final QueryResultListener<Object> result) {
		return runner.forwardQueries(query, result);
	}
	
	/**
	 * @return the handler responsible for encrypting and decrypting messages
	 */
	public SecurityHandler getSecurityHandler() {
		return security;
	}

	public void shutdown() {
		disconnect_synchron();
		connector_sesame.shutdown();
	}
}
