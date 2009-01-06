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
/***************************************************************
Copyright 2007 52North Initiative for Geospatial Open Source Software GmbH

 Author: jtheuer, University of Muenster

 Contact: Andreas Wytzisk, 
 52North Initiative for Geospatial Open Source SoftwareGmbH, 
 Martin-Luther-King-Weg 24,
 48155 Muenster, Germany, 
 info@52north.org

 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 version 2 as published by the Free Software Foundation.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; even without the implied WARRANTY OF
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program (see gnu-gpl v2.txt). If not, write to
 the Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 Boston, MA 02111-1307, USA or visit the Free
 Software Foundations web page, http://www.fsf.org.

 ***************************************************************/

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import org.openrdf.elmo.ElmoManager;
import org.openrdf.elmo.ElmoModule;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.sail.memory.MemoryStore;


/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * 
 * A {@link SesameContextFactory} provides access to a sesame database based on
 * a given context.
 * 
 * It provides an {@link ElmoManager} and a {@link Repository} access
 * that both operate within the same context.
 */
public class SesameContextFactory {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(SesameContextFactory.class.getName());
	
	private final static String PREFIX = "urn:sesame/";
	private AtomicInteger counter = new AtomicInteger(0);
	
	private Repository repository;
	private ElmoModule module;
	private QNameURI baseuri;

	public SesameContextFactory(Repository repository) {
		this(repository,null,new QNameURI("urn:/sesame"));
	}
	
	public SesameContextFactory(Repository repository, QNameURI baseuri) {
		this(repository, null, baseuri);
	}

	public SesameContextFactory(Repository repository, ElmoModule module, QNameURI baseuri) {
		this.repository = repository;
		this.module = module;
		this.baseuri = baseuri;
	}
	
	public SesameContextWrapper createWrapper() throws RepositoryException {
		SesameContextWrapper scw = new SesameContextWrapper(repository,getNextId(), module);
		if(scw.size() > 0) {
			LOGGER.warning("There are already " + scw.size() + " statements in the context!");
		}
		return scw;
	}

	/**
	 * @param localDatabase
	 * @return
	 * @throws RepositoryException 
	 */
	public SesameContextWrapper createWrapper(QNameURI localDatabase) throws RepositoryException {
		return new SesameContextWrapper(repository,localDatabase,module);
	}
	
	
	
	private QNameURI getNextId() throws RepositoryException {
		return baseuri.createNewLocalPart(Integer.toString(counter.incrementAndGet()));
	}

	
	/**
	 * @return a new {@link Repository} that uses a {@link MemoryStore} in the RAM.
	 * @throws RepositoryException
	 */
	public static Repository createDefaultRepository() throws RepositoryException {
		MemoryStore store = new MemoryStore();
		Repository repository = new SailRepository(store);
		repository.initialize();
		return repository;
	}

	public QNameURI getBaseuri() {
		return baseuri;
	}

	public void setBaseuri(QNameURI baseuri) {
		this.baseuri = baseuri;
	}

}
