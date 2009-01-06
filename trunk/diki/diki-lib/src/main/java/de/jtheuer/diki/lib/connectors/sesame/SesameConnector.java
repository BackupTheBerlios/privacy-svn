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
package de.jtheuer.diki.lib.connectors.sesame;

import java.io.File;
import java.io.Writer;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.QName;

import org.openrdf.elmo.ElmoModule;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.query.*;
import org.openrdf.query.impl.DatasetImpl;
import org.openrdf.repository.*;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.repository.sail.SailRepositoryConnection;
import org.openrdf.rio.rdfxml.RDFXMLWriter;
import org.openrdf.sail.SailException;
import org.openrdf.sail.memory.MemoryStore;
import org.paceproject.diki.elmo.*;

import de.jtheuer.diki.lib.NetworkConnection;
import de.jtheuer.diki.lib.connectors.*;
import de.jtheuer.diki.lib.query.NetworkQuery;
import de.jtheuer.diki.lib.query.QueryException;
import de.jtheuer.sesame.*;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * 
 */
public class SesameConnector extends AbstractSimpleConnector {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(SesameConnector.class.getName());

	private SailRepository repository = null;
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	public final static String PREFIXDB = "urn:local/";

	public final static QNameURI LOCAL_DATABASE = new QNameURI(PREFIXDB);
	public final static QName NULLCONTEXT = new QName(PREFIXDB, "NULL");
	private SesameContextFactory contextFactory;
	private SesameContextWrapper localDatabase;

	private static final String DEFAULT_DIR = "database";
	public static final String STORAGETYPE = "database.storagetype";
	public static final String STORAGETYPE_RAM = "ram";

	private ElmoModule elmo_init_module;

	private MemoryStore store;

	/**
	 * 
	 */
	public SesameConnector(NetworkConnection connection) {
		super("Local database", null, connection);

		/* add a shutdown hook */
		Runtime.getRuntime().addShutdownHook(new Thread("shutdown hook") {

			@Override
			public void run() {
				
			}

		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jtheuer.diki.lib.connectors.Connector#connect(de.jtheuer.diki.lib.NetworkConnection,
	 *      java.util.Properties)
	 */
	@Override
	public void connect(Properties prop) throws ConnectorException {

		/* leave if already connected (reconnect is not supported */
		if (repository != null) {
			return;
		}

		lock.writeLock().lock();
		/* check if MemoryStore is forced */
		final boolean force_memorystore = STORAGETYPE_RAM.equals(prop.getProperty(STORAGETYPE));

		/* construct and create datadir */
		File home = getConnection().getHomeDirectory();

		if (!force_memorystore && home != null) {
			/* persistent store */
			File dataDir = new File(home.getAbsolutePath() + File.separator + DEFAULT_DIR);
			if (dataDir.exists() || dataDir.mkdir()) {
				store = new MemoryStore(dataDir);
				repository = new SailRepository(store);
				LOGGER.info("opened repository in " + dataDir);
			} else {
				repository = new SailRepository(new MemoryStore());
				LOGGER.info("could not open repository in " + dataDir + " using volatile in ram");
			}
		} else {
			/* no access to file system - volatile store in RAM */
			repository = new SailRepository(new MemoryStore());
			LOGGER.info("using ram repository as requestd");
		}

		RepositoryConnection conn = null;
		try {
			try {
				repository.initialize();
				if (!repository.isWritable()) {
					throw new ConnectorException("Cannot write to repository!");
				}

				elmo_init_module = new ElmoModule();
				elmo_init_module.setGraph(NULLCONTEXT);
				elmo_init_module.addRole(org.paceproject.diki.elmo.Query.class);
				elmo_init_module.addRole(QueryEnd.class);
				elmo_init_module.addRole(QueryResponse.class);
				elmo_init_module.addRole(Rateing.class);

				/* we change it at runtime */
				contextFactory = new SesameContextFactory(repository, elmo_init_module, new QNameURI("urn:/dikistore"));

				localDatabase = contextFactory.createWrapper(LOCAL_DATABASE);
				// deprecated! localDatabase.addReadContexts((QNameURI)null);

				/* clear all other namespaces */
				conn = repository.getConnection();
				RepositoryResult<Resource> i = conn.getContextIDs();
				while (i.hasNext()) {
					Resource namespace = i.next();
					if (!namespace.equals(LOCAL_DATABASE)) {
						conn.clear(namespace);
						LOGGER.fine("Cleared: " + namespace);
					}
				}
			} finally {
				if (conn != null) {
					conn.close();
				}
			}
		} catch (RepositoryException e) {
			repository = null;
			throw new ConnectorException(e);
		}

		/* set defaults ... */
		lock.writeLock().unlock();
	}

	@Override
	public void disconnect() {
	// we never disconnect! It would break the system at the moment because
	// existing elmo proxy instance would become null
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jtheuer.diki.lib.connectors.Connector#getParameters()
	 */
	@Override
	public Properties getParameters() {
		return null;
	}

	@Override
	public ParameterProperties getProperties() {
		return null;
	}

	/**
	 * Evaluates the given query and stores results
	 * 
	 * @param networkQuery
	 * @throws QueryException
	 */
	private void solver(NetworkQuery networkQuery) throws QueryException {
		try {
			SailRepositoryConnection conn = repository.getConnection();
			try {

				/* iterate over results and assign them to the store */

				QueryResponse response = networkQuery.createResponse();

				String query = networkQuery.getQueryString();
				query = query.replace("[]", "<" + QNameURI.toString(response.getQName()) + ">"); // replace
				// anonymous
				// node
				// with
				// a
				// concrete
				// one
				// from
				// our
				// namespace

				GraphQuery prepared = conn.prepareGraphQuery(QueryLanguage.SPARQL, query);

				DatasetImpl dataset = new DatasetImpl();
				dataset.addDefaultGraph(LOCAL_DATABASE);
				prepared.setDataset(dataset);

				GraphQueryResult iterator = prepared.evaluate();

				/* copy and return results if applicable */
				final boolean hasResponse = iterator.hasNext();
				if (hasResponse) {
					while (iterator.hasNext()) {
						Statement statement = iterator.next();
						networkQuery.getSesameContext().add(statement);
					}

					/* link the added statements with the created response */
					// assignResults(networkQuery.getQueryString(), response);
					networkQuery.addResponse(response);
				}

			} catch (Exception e) {
				throw new QueryException(e);
			} finally {
				conn.close();
			}

		} catch (RepositoryException e) {
			throw new QueryException("unable to open/close the query connection", e);
		}
	}

	/**
	 * Solves the given SPARQL query and returns an Iterator
	 * 
	 * @param query
	 * @return
	 * @throws RepositoryException
	 * @throws MalformedQueryException
	 * @throws QueryEvaluationException
	 */
	@SuppressWarnings("unchecked")
	public GraphQueryResult solver(String query) throws MalformedQueryException, RepositoryException, QueryEvaluationException {
		// String replaced = query.replace("WHERE", "FROM <" +
		// localDatabase.getQName() + "> WHERE");
		SailRepositoryConnection conn = repository.getConnection();
		try {
			GraphQuery prepared = conn.prepareGraphQuery(QueryLanguage.SPARQL, query);

			DatasetImpl dataset = new DatasetImpl();
			dataset.addDefaultGraph(LOCAL_DATABASE);
			prepared.setDataset(dataset);

			GraphQueryResult result = prepared.evaluate();
			return result;
		} finally {
			conn.close();
		}
	}

	/**
	 * Export the Database as xml.
	 * 
	 * @param export
	 *            a {@link Writer}.
	 * @throws ConnectorException
	 */
	public void exportDatabase(Writer export) throws ConnectorException {
		SailRepositoryConnection conn;
		try {
			conn = repository.getConnection();
			conn.export(new RDFXMLWriter(export));
			conn.close();
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Couldn't export database", e);
			throw new ConnectorException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jtheuer.diki.lib.connectors.Connector#getStatus()
	 */
	@Override
	public synchronized Status getStatus() {
		return repository == null ? Status.Disconnected : Status.Connected;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jtheuer.diki.lib.connectors.AbstractSimpleConnector#getQuerySolver(de.jtheuer.diki.lib.query.Query)
	 */
	@Override
	protected Runnable innerEvaluate(final NetworkQuery networkQuery) {
		return new Runnable() {

			@Override
			public void run() {
				try {
					solver(networkQuery);
				} catch (QueryException e) {
					LOGGER.log(Level.SEVERE, "sesame query failed", e);
				}
			}

			@Override
			public String toString() {
				return "Sesame connector";
			}
		};
	}

	/**
	 * @return the store for the local entities
	 */
	public SesameContextWrapper getLocalStore() {
		return localDatabase;
	}

	/**
	 * Creates a new sesamefactory for the given namespace
	 * 
	 * @param uri
	 *            the namespace for the factory
	 * @return
	 */
	public SesameContextFactory createSesameFactory(QNameURI uri) {
		if (repository == null) {
			throw new IllegalStateException("must initialize first!");
		}
		return new SesameContextFactory(repository, elmo_init_module, uri);
	}
	
	
	/**
	 * frees all resources and gracefully closes the database 
	 */
	public void shutdown() {
		if (repository != null) {
			try {
				if (localDatabase != null) {
					localDatabase.close();
				}
				repository.shutDown();
				store.shutDown();
				LOGGER.info("Sesame database gracefully closed");
			} catch (RepositoryException e) {
				LOGGER.log(Level.SEVERE, "shutdown exception", e);
			} catch (SailException e) {
				LOGGER.log(Level.SEVERE, "autogenerated catch-block", e);
			}
		}
	}

}
