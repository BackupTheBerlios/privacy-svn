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

import org.openrdf.elmo.sesame.SesameManager;
import org.openrdf.model.*;
import org.openrdf.repository.*;
import org.openrdf.rio.RDFHandler;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.ntriples.NTriplesWriter;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 */
public interface SimpleContext {

	/**
	 * @return a {@link SesameManager} that reads and writes to this context
	 */
	public abstract SesameManager createManager();

	/**
	 * Exports the content of the writeContext
	 * @param handler
	 * @throws RepositoryException
	 * @throws RDFHandlerException
	 * @see RepositoryConnection#export(RDFHandler, Resource...)
	 */
	public abstract void export(RDFHandler handler) throws RepositoryException, RDFHandlerException;

	/**
	 * Exports all Statements with the given subject
	 * @param handler
	 * @param subject
	 * @throws RDFHandlerException 
	 * @throws RepositoryException 
	 */
	public abstract void export(RDFHandler handler, QNameURI subject) throws RepositoryException, RDFHandlerException;

	/**
	 * Exports all Statements with the given subject
	 * @param handler
	 * @param subject
	 * @return 
	 * @throws RepositoryException 
	 */
	public abstract RepositoryResult<Statement> getStatements(QNameURI subject) throws RepositoryException;

	/**
	 * Exports all Statements that match the given subject, predicate, object
	 * @param subject
	 * @param predicate
	 * @param object
	 * @return 
	 * @throws RepositoryException 
	 */
	public abstract RepositoryResult<Statement> getStatements(Resource subject, URI predicate, Value object) throws RepositoryException;

	/**
	 * @return the repository used for this context.
	 */
	public abstract Repository getRepository();

	/**
	 * @return the number of statements in the associated context.
	 * @throws RepositoryException
	 */
	public abstract long size() throws RepositoryException;

	/**
	 * @return the identifier for this wrapper.
	 */
	public abstract QNameURI getQName();
	
	/**
	 * @return the content in the context. Written with the {@link NTriplesWriter}. If an exception occurs its message is returned.
	 */
	public abstract String dump();

	/**
	 * @return all statements with the given <em>subject,predicate,object</em> in the context. (You can use null as wildcard). Written with the {@link NTriplesWriter}. If an exception occurs its message is returned.
	 */
	public abstract String dump(Resource subject, URI predicate, Value object);

	/**
	 * Frees all resources associated with this context (but does not close the repository itself)
	 * @throws RepositoryException 
	 */
	public abstract void close() throws RepositoryException;
	

}