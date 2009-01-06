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

import java.io.*;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.QName;

import org.openrdf.elmo.ElmoModule;
import org.openrdf.elmo.Entity;
import org.openrdf.elmo.sesame.SesameManagerFactory;
import org.openrdf.model.*;
import org.openrdf.query.*;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.*;
import org.openrdf.rio.ntriples.NTriplesWriter;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * 
 */
public class ElmoGraphCreator {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(ElmoGraphCreator.class.getName());

	private SesameContextWrapper wrapper;

	public ElmoGraphCreator(SesameContextWrapper wrapper) {
		this.wrapper = wrapper;
	}

	/**
	 * Transform the Graph of the entity to a N3-representation String with the given depth.
	 * @param response
	 * @param depth
	 * @param writer the result
	 */
	public void writeGraph(Entity response, int depth, Writer writer) {
		QName id = response.getQName();
		RepositoryConnection con;
		try {
			con = wrapper.getRepository().getConnection();
			GraphQuery sesameQuery = con.prepareGraphQuery(QueryLanguage.SPARQL, createQuery(id));
			
			RDFHandler h = new NTriplesWriter(writer);
			sesameQuery.evaluate(h);
			
		} catch (RepositoryException e) {
			LOGGER.log(Level.SEVERE,"autogenerated catch-block", e);
		} catch (MalformedQueryException e) {
			LOGGER.log(Level.SEVERE,"autogenerated catch-block", e);
		} catch (QueryEvaluationException e) {
			LOGGER.log(Level.SEVERE,"autogenerated catch-block", e);
		} catch (RDFHandlerException e) {
			LOGGER.log(Level.SEVERE,"autogenerated catch-block", e);
		}
	}
	
	public Entity createEntity(String input, Class<? extends Entity> type) {
		StringReader r = new StringReader(input);
		return createEntity(r,type);
	}
	
	/**
	 * Stores an rdf graph in the database. Returns an entity as reference of the first entity that matches the given classtype
	 * @param input
	 * @param type
	 * @return the first found element or null
	 */
	public Entity createEntity(Reader input, Class<? extends Entity> type) {

		RepositoryConnection con=null;
		QNameURI store = createTemroraryResource();
		try {
			con = wrapper.getRepository().getConnection();
			con.add(input, null, RDFFormat.NTRIPLES, (Resource) null, store);

			ElmoModule em = new ElmoModule();
			em.setContext(store.toQName());
			SesameManagerFactory factoryStore = new SesameManagerFactory(em,con.getRepository());
			Iterable<? extends Entity> iterable = factoryStore.createElmoManager().findAll(type);
			Iterator<? extends Entity> iterator = iterable.iterator();
			
			if(iterator.hasNext()) {
				return iterator.next();
			}
	
		} catch (RDFParseException e) {
			LOGGER.log(Level.SEVERE,"autogenerated catch-block", e);
		} catch (RepositoryException e) {
			LOGGER.log(Level.SEVERE,"autogenerated catch-block", e);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE,"autogenerated catch-block", e);
		} finally {
			if(con != null) {
				try {
					con.remove((Resource)null, (URI)null, (Value) null, store);
				} catch (RepositoryException e) {
					LOGGER.log(Level.INFO,"cleanup failed.", e);
				}
			}
		}

		return null;
		
	}
	
	private QNameURI createTemroraryResource() {
		return new QNameURI("http://example.com/"+System.currentTimeMillis());
	}
	
	/**
	 * Transform the Graph of the entity to a N3-representation String with the given depth.
	 * @param response
	 * @param depth
	 * @return the result
	 */
	public String writeGraph(Entity response, int depth) {
		StringWriter output = new StringWriter();
		writeGraph(response, depth, output);
		return output.toString();
	}

	private static String createQuery(QName id) {
		String entity = "<" +id.getNamespaceURI()+id.getLocalPart()+">"; 
		return "DESCRIBE "+ entity + "?s ?o  WHERE {?s ?p ?o}";
	}

}