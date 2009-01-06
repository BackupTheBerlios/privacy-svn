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
import java.util.logging.Logger;

import org.openrdf.model.*;
import org.openrdf.repository.RepositoryException;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * 
 * A synchronizer can handle conflicts when copying statements from one context
 * into another.
 */
public class Synchronizer {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(Synchronizer.class.getName());

	private SimpleContext source;
	private WriteableContext destination;
	private Resource readonly;

	/**
	 * Creates a new {@link Synchronizer} with the given source and destination
	 * 
	 * @param source
	 * @param destination
	 */
	public Synchronizer(SimpleContext source, WriteableContext destination) {
		this.source = source;
		this.destination = destination;
	}

	/**
	 * Creates a new {@link Synchronizer} with the given source and destination
	 * 
	 * @param source
	 * @param destination
	 * @param readonly marks a resource as readonly: Statements with this {@link Resource} as subject are never written into the destination
	 */
	public Synchronizer(SimpleContext source, WriteableContext destination, Resource readonly) {
		this.source = source;
		this.destination = destination;
		this.readonly = readonly;
	}
	
	/**
	 * replaces all existing triples which have the same subject and predicate
	 * as the given sourceStatement with the supplied sourceStatement. The
	 * supplied statement may be from the source.
	 * 
	 * @param sourceStatement
	 *            the {@link Statement} which is added to the repository.
	 * @throws RepositoryException
	 */
	protected void replaceStatement(Statement sourceStatement) throws RepositoryException {
		destination.removeStatements(sourceStatement.getSubject(), sourceStatement.getPredicate(), (Value) null);
		destination.add(sourceStatement);
	}

	/**
	 * Adds the given statement unless readonly is set and equals the statement's subject.
	 * @param sourceStatement
	 * @throws RepositoryException
	 */
	private void addStatement(Statement sourceStatement) throws RepositoryException {
		if(readonly==null || !readonly.equals(sourceStatement.getSubject())) {
			destination.add(sourceStatement);
		}
	}

	/**
	 * removes all statements from the destination that have the given subject
	 * and predicate
	 * 
	 * @param sourceStatement
	 * @throws RepositoryException
	 */
	private void removeMatchingStatements(Resource subject, URI predicate) throws RepositoryException {
		destination.removeStatements(subject, predicate, (Value) null);
	}

	/**
	 * Copies all statement from the the source to the destination that matches
	 * the given subject, predicate and object. Null may be provided as
	 * wildcard.
	 * 
	 * @param subject
	 * @param predicate
	 * @param object
	 * @throws RepositoryException
	 * @return how many statements have been copied
	 */
	public int copyStatements(Resource subject, URI predicate, Value object) throws RepositoryException {
		List<Statement> statements_source = source.getStatements(subject, predicate, object).asList();
		int i = 0;
		for (Statement statement : statements_source) {
			addStatement(statement);
			i++;
		}

		return i;
	}

	/**
	 * Removes all statements from the destination with the given subject and
	 * predicate and copies all matching statements from the source do the
	 * destination.
	 * 
	 * @param subject
	 * @param predicate
	 * @throws RepositoryException
	 */
	public void replaceStatements(Resource subject, URI predicate) throws RepositoryException {
		removeMatchingStatements(subject, predicate);
		copyStatements(subject, predicate, (Value) null);
	}

	/**
	 * Searches all Statements from the source. From those Statemets the
	 * predicate is read and all statement from the destination that match the
	 * subject and predicate are deleted. Then all Statements are inserted into
	 * the destination.
	 * 
	 * 
	 * @param subject
	 * @param predicate
	 * @throws RepositoryException
	 * @return how many statements have been copied;
	 */
	public int replaceStatements(Resource subject) throws RepositoryException {
		/* get all statements */
		List<Statement> statements_source = source.getStatements(subject, null, null).asList();
		/* remove matching statements */
		for (Statement statement : statements_source) {
			URI predicate = statement.getPredicate();
			removeMatchingStatements(subject, predicate);
		}

		int i = 0;
		for (Statement statement : statements_source) {
			addStatement(statement);
			i++;
		}

		return i;
	}

	/**
	 * Searches all Statements from the source that match the given Subject.
	 * Then the method searches all statements' object as subject recursively,
	 * too.
	 * 
	 * 
	 * @param subject
	 * @param predicate
	 * @throws RepositoryException
	 */
	public void replaceStatementsRecursive(Resource subject) throws RepositoryException {
		HashSet<Resource> set = new HashSet<Resource>();
		_recSearch(subject, set);
		for (Resource resource : set) {
			replaceStatements(resource);
		}
	}

	/**
	 * Taking the subject as starting point, this method fills the given set with all other subject that are reachable in the graph.
	 * @param subject starting node in the graph
	 * @param finalSubjects the {@link Set} where all subjects are stored into
	 * @throws RepositoryException
	 */
	private void _recSearch(Resource subject, Set<Resource> finalSubjects) throws RepositoryException {
		List<Statement> statements_source = source.getStatements(subject, null, null).asList();
		if (statements_source.size() > 0) {
			LOGGER.info("adding " + subject.toString());
			finalSubjects.add(subject);
			for (Statement statement : statements_source) {
				if (statement.getObject() instanceof Resource) {
					/* if not already dived into */
					if (!finalSubjects.contains(((Resource) statement.getObject()))) {
						_recSearch((Resource) statement.getObject(), finalSubjects);
					}
				}
			}
		}
	}

}
