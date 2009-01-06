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

import java.io.StringWriter;
import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

import org.openrdf.elmo.ElmoModule;
import org.openrdf.elmo.sesame.SesameManager;
import org.openrdf.elmo.sesame.SesameManagerFactory;
import org.openrdf.model.*;
import org.openrdf.repository.*;
import org.openrdf.rio.RDFHandler;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.ntriples.NTriplesWriter;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * 
 * Implementation of the {@link SimpleContext}
 */
public class SimpleContextImpl implements SimpleContext {

	protected SesameManagerFactory factory;
	protected Repository repository;
	protected ElmoModule contextModule;
	protected QNameURI context[];
	List<WeakReference<SesameManager>> managers;

	protected RepositoryConnection transientConnection; //this connection is used for all commands that need an open connection

	/**
	 * @param repository
	 * @param module
	 *            a {@link ElmoModule} that is included in this context.
	 * @param contexturi
	 */
	public SimpleContextImpl(Repository repository, ElmoModule module, QNameURI contexturi) {
		this(repository, module, new QNameURI[] { contexturi });
	}

	/**
	 * @param repository
	 * @param contexturi
	 */
	public SimpleContextImpl(Repository repository, QNameURI contexturi) {
		this(repository, null, new QNameURI[] { contexturi });
	}
	
	protected SimpleContextImpl(Repository repository, ElmoModule module, QNameURI[] contexturi) {
		if (repository == null) {
			throw new IllegalArgumentException("Repository must not be null");
		}
		if (contexturi == null) {
			throw new IllegalArgumentException("Context must not be null");
		}

		this.repository = repository;
		try {
			this.transientConnection = repository.getConnection();
		} catch (RepositoryException e) {
			throw new IllegalArgumentException("Repository must be readable!", e);
		}
		this.context = contexturi;

		contextModule = new ElmoModule();

		/*
		 * set first context as write context and add all other as secondary
		 * modules
		 */
		contextModule.setGraph(contexturi[0].toQName());
		for (int i = 1; i < contexturi.length; i++) {
			ElmoModule temp = new ElmoModule();
			temp.setGraph(contexturi[i].toQName());
			contextModule.includeModule(temp);
		}

		/* include basic module */
		if (module != null) {
			contextModule.includeModule(module);
		}

		factory = new SesameManagerFactory(contextModule, repository);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jtheuer.sesame.SimpleContext#createManager()
	 */
	public synchronized SesameManager createManager() {
		if(managers == null) {
			managers = new LinkedList<WeakReference<SesameManager>>();
		}
		
		/* store a weak reference to the manager */
		SesameManager manager = factory.createElmoManager();
		managers.add(new WeakReference<SesameManager>(manager));
		
		return manager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jtheuer.sesame.SimpleContext#export(org.openrdf.rio.RDFHandler)
	 */
	public synchronized void export(RDFHandler handler) throws RepositoryException, RDFHandlerException {
		RepositoryConnection connection = repository.getConnection();
		try {
			connection.export(handler, context);
		} finally {
			connection.close();
		}
	}

	public synchronized void export(RDFHandler handler, QNameURI subject) throws RepositoryException, RDFHandlerException {
		RepositoryConnection connection = repository.getConnection();
		try {
			connection.exportStatements(subject, null, null, true, handler, context);
		} finally {
			connection.close();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jtheuer.sesame.SimpleContext#getStatements(de.jtheuer.sesame.QNameURI)
	 */
	public synchronized RepositoryResult<Statement> getStatements(QNameURI subject) throws RepositoryException {
		RepositoryConnection connection = transientConnection;
			return connection.getStatements(subject, null, null, true, context);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jtheuer.sesame.SimpleContext#getStatements(org.openrdf.model.Resource,
	 *      org.openrdf.model.URI, org.openrdf.model.Value)
	 */
	public RepositoryResult<Statement> getStatements(Resource subject, URI predicate, Value object) throws RepositoryException {
		RepositoryConnection connection = transientConnection;
		return connection.getStatements(subject, predicate, object, true, context);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jtheuer.sesame.SimpleContext#getRepository()
	 */
	public Repository getRepository() {
		return repository;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jtheuer.sesame.SimpleContext#size()
	 */
	public synchronized long size() throws RepositoryException {
		RepositoryConnection connection = repository.getConnection();
		try {
			return connection.size(context);
		} finally {
			connection.close();
		}

	}

	/**
	 * @return all statements that are in the primary context of this
	 *         {@link SesameContextWrapper}
	 * @throws RepositoryException
	 */
	protected synchronized RepositoryResult<Statement> getAllStatements() throws RepositoryException {
		RepositoryConnection connection = transientConnection;
		return connection.getStatements(null, null, null, true, context);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jtheuer.sesame.SimpleContext#getQName()
	 */
	public synchronized QNameURI getQName() {
		return context[0];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jtheuer.sesame.SimpleContext#dump()
	 */
	public synchronized String dump() {
		try {
			StringWriter w = new StringWriter();
			export(new NTriplesWriter(w));
			return w.toString();
		} catch (RepositoryException e) {
			return e.getMessage();
		} catch (RDFHandlerException e) {
			return e.getMessage();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jtheuer.sesame.SimpleContext#dump(org.openrdf.model.Resource,
	 *      org.openrdf.model.URI, org.openrdf.model.Value)
	 */
	@Override
	public String dump(Resource subject, URI predicate, Value object) {
		try {
			StringWriter w = new StringWriter();
			NTriplesWriter writer = new NTriplesWriter(w);
			RepositoryResult<Statement> results = getStatements(subject, predicate, object);
			writer.startRDF();
			while (results.hasNext()) {
				writer.handleStatement(results.next());
			}
			writer.endRDF();
			return w.toString();
		} catch (RepositoryException e) {
			return e.getMessage();
		} catch (RDFHandlerException e) {
			return e.getMessage();
		}
	}

	/**
	 * @param class1
	 * @return true if a concept of the given classtype is contained in this
	 *         store
	 */
	public synchronized boolean contains(Class<?> class1) {
		return createManager().findAll(class1).iterator().hasNext();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jtheuer.sesame.SimpleContext#close()
	 */
	@Override
	public void close() throws RepositoryException {
		if(managers != null) {
			for (WeakReference<SesameManager> wref : managers) {
				SesameManager manager = wref.get();
				/* if still open, then close */
				if(manager != null && manager.isOpen()) {
					
					manager.close();
				}
			}
		}
		transientConnection.close();
		factory.close();
	}

}