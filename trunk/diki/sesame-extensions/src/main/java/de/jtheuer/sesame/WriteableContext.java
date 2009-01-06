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

import org.openrdf.model.*;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFParseException;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 */
public interface WriteableContext extends SimpleContext {

	/**
	 * Adds the rdf statements from the reader to the repository. It is written into the writeContexts supplied in the constructor
	 * @param reader
	 * @param baseURI
	 * @param dataFormat
	 * @throws RDFParseException
	 * @throws RepositoryException
	 * @throws IOException
	 * @see {@link RepositoryConnection#add(Reader, String, RDFFormat, Resource...)}
	 */
	public abstract void add(Reader reader, String baseURI, RDFFormat dataFormat) throws RDFParseException, RepositoryException, IOException;

	/**
	 * Adds the parameters to this context wrapper
	 * @see RepositoryConnection#add(Resource, URI, Value, Resource...)
	 * @param subject
	 * @param predicate
	 * @param object
	 * @throws RepositoryException
	 */
	public abstract void add(Resource subject, URI predicate, Value object) throws RepositoryException;

	/**
	 * deletes all statements/entites from this primary context
	 * @throws RepositoryException 
	 */
	public abstract void clear() throws RepositoryException;

	/**
	 * @param next
	 * @throws RepositoryException 
	 */
	public abstract void add(Statement statement) throws RepositoryException;

	/**
	 * @param subject
	 * @param predicate
	 * @param object
	 * @throws RepositoryException 
	 */
	public abstract void removeStatements(Resource subject, URI predicate, Value object) throws RepositoryException;
	
}