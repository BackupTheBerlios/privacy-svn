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

import java.io.IOException;
import java.io.Reader;

import org.openrdf.elmo.ElmoModule;
import org.openrdf.elmo.sesame.SesameManager;
import org.openrdf.model.*;
import org.openrdf.repository.*;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFParseException;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 */
public class WriteableContextImpl extends SimpleContextImpl implements WriteableContext {

	
	/**
	 * Creates a new read-write context (except for the {@link SesameManager})
	 */
	public WriteableContextImpl(Repository repository, QNameURI contexturi) {
		super(repository,contexturi);
	}
	
	/**
	 * Creates a new read-write context (except for the {@link SesameManager})
	 */
	public WriteableContextImpl(Repository repository, ElmoModule module, QNameURI contexturi) {
		super(repository,module,contexturi);
	}

	/* (non-Javadoc)
	 * @see de.jtheuer.sesame.WriteableContext#add(java.io.Reader, java.lang.String, org.openrdf.rio.RDFFormat)
	 */
	public synchronized void add(Reader reader, String baseURI, RDFFormat dataFormat) throws RDFParseException, RepositoryException, IOException {
		RepositoryConnection connection = repository.getConnection();
		try {
			connection.add(reader, baseURI, dataFormat, context);
		} finally {
			connection.close();
		}
	}

	/* (non-Javadoc)
	 * @see de.jtheuer.sesame.WriteableContext#add(org.openrdf.model.Resource, org.openrdf.model.URI, org.openrdf.model.Value)
	 */
	public synchronized void add(Resource subject, URI predicate, Value object) throws RepositoryException {
		RepositoryConnection connection = repository.getConnection();
		try {
			connection.add(subject, predicate, object, context);
		} finally {
			connection.close();
		}
	}

	/* (non-Javadoc)
	 * @see de.jtheuer.sesame.WriteableContext#clear()
	 */
	public synchronized void clear() throws RepositoryException {
		RepositoryConnection connection = repository.getConnection();
		try {
			connection.clear(context);
		} finally {
			connection.close();
		}
	}

	/* (non-Javadoc)
	 * @see de.jtheuer.sesame.WriteableContext#add(org.openrdf.model.Statement)
	 */
	public synchronized void add(Statement statement) throws RepositoryException {
		RepositoryConnection connection = repository.getConnection();
		try {
			connection.add(statement, context);
		} finally {
			connection.close();
		}
	}

	/* (non-Javadoc)
	 * @see de.jtheuer.sesame.WriteableContext#removeStatements(org.openrdf.model.Resource, org.openrdf.model.URI, org.openrdf.model.Value)
	 */
	public void removeStatements(Resource subject, URI predicate, Value object) throws RepositoryException {
		RepositoryConnection connection = repository.getConnection();
		try {
			connection.remove(subject, predicate, object, context);
		} finally {
			connection.close();
		}
	}

}