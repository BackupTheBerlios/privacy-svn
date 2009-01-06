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

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import org.openrdf.elmo.ElmoModule;
import org.openrdf.model.Statement;
import org.openrdf.repository.*;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * 
 * Extends a {@link WriteableContext} with methods to create children. Therefore
 * hierarchical structures can be generated
 */
public class SesameContextWrapper extends WriteableContextImpl implements CloneableContext {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(SesameContextWrapper.class.getName());

	private ElmoModule baseModule; // a module that is included in the
									// writemodule and all childrens and clones;
	private List<SesameContextWrapper> children = new LinkedList<SesameContextWrapper>();

	/* identifier counter for query clones */
	private AtomicInteger subqueryCounter = new AtomicInteger();

	/**
	 * Creates a new wrapper
	 * 
	 * @param repository
	 *            The repository
	 * @param uri
	 *            The main read/write context. You can define more read contexts
	 *            later
	 * @throws RepositoryException
	 */
	public SesameContextWrapper(Repository repository, QNameURI uri) throws RepositoryException {
		this(repository, uri, null);
	}

	/**
	 * Creates a new wrapper
	 * 
	 * @param repository
	 *            The repository
	 * @param uri
	 *            The main read/write context. You can define more read contexts
	 *            later
	 * @param module
	 *            a module that is added to this wrapper
	 * @throws RepositoryException
	 */
	public SesameContextWrapper(Repository repository, QNameURI uri, ElmoModule module) throws RepositoryException {
		super(repository, module, uri);
		this.baseModule = module;
	}

	/**
	 * Creates a new {@link SesameContextWrapper} and copies all Statements from
	 * this instance to the newly created one identified by the uri.
	 * 
	 * @param uri
	 *            a new uri. Make shure the uri doesn't already exists!
	 *            Otherwiese results my be not the expected one
	 * @return a new {@link SesameContextWrapper} with a copy of all statements
	 * @throws RepositoryException
	 */
	protected synchronized SesameContextWrapper clone(QNameURI uri) throws RepositoryException {
		RepositoryConnection conn = repository.getConnection();
		try {
			/* read all statements from this repository */
			RepositoryResult<Statement> statements = conn.getStatements(null, null, null, true, context);
			List<Statement> list = statements.asList();
			if (conn.size(context) != list.size()) {
				throw new AssertionError("Expected " + conn.size(context) + "elements, but only got " + list.size());
			}

			/* write into new context */
			for (Statement statement : list) {
				conn.add(statement, uri);
			}

			/* create a new SesameContextWrapper with the given uri and  baseModule */
			SesameContextWrapper scw = new SesameContextWrapper(repository, uri, baseModule);

			return scw;
		} finally {
			conn.close();
		}
	}

	/**
	 * Creates a new {@link SesameContextWrapper} so that this instance contains
	 * all values that are either in this instance or the newly created child.
	 * 
	 * @param uri
	 * @return
	 * @throws RepositoryException
	 */
	public synchronized SesameContextWrapper createChild() throws RepositoryException {
		SesameContextWrapper child = clone(createNewChildIdentifier());
		children.add(child);
		return child;
	}

	/**
	 * adds a child identifier to the supplied {@link QNameURI}
	 * 
	 * @param query
	 * @return a unique, thread-id-valid query identifier
	 */
	protected synchronized QNameURI createNewChildIdentifier() {
		return getQName().createNewLocalPart("" + subqueryCounter.incrementAndGet());
	}

	/**
	 * retrieves recursively all children's uris
	 * 
	 * @return
	 */
	protected synchronized List<QNameURI> getChildURIs() {
		List<QNameURI> uris = new ArrayList<QNameURI>();

		/* add this */
		uris.add(getQName());
		if (children.size() == 0) {
			// nothing more
		} else {
			for (SesameContextWrapper scw : children) {
				uris.addAll(scw.getChildURIs());
			}
		}

		return uris;
	}

	/**
	 * @return all childrens' uris as array
	 */
	protected QNameURI[] getChildURIArray() {
		List<QNameURI> list = getChildURIs();
		QNameURI[] array = new QNameURI[list.size()];
		return list.toArray(array);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jtheuer.sesame.CloneableContext#createFullContext()
	 */
	@Override
	public SimpleContext createFullContext() {
		SimpleContextImpl newContext = new SimpleContextImpl(repository, baseModule, getChildURIArray());
		return newContext;
	}

	/* (non-Javadoc)
	 * @see de.jtheuer.sesame.CloneableContext#closeAll()
	 */
	@Override
	public void closeAll() throws RepositoryException {
		for (SesameContextWrapper scw : children) {
			scw.closeAll();
		}
		
		close();
	}

	/* (non-Javadoc)
	 * @see de.jtheuer.sesame.CloneableContext#clearAll()
	 */
	@Override
	public void clearAll() throws RepositoryException {
		for (SesameContextWrapper scw : children) {
			scw.clearAll();
		}
		
		clear();
	}
	
	

}
