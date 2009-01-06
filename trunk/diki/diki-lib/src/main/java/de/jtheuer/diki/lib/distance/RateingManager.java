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
package de.jtheuer.diki.lib.distance;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import javax.xml.namespace.QName;

import org.openrdf.concepts.foaf.Person;
import org.openrdf.elmo.*;
import org.paceproject.diki.elmo.Rateing;

import de.jtheuer.diki.lib.namespace.NamespaceFactory;
import de.jtheuer.diki.lib.query.SPARQLPrefix;
import de.jtheuer.sesame.QNameURI;
import de.jtheuer.sesame.SesameContextWrapper;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * 
 * Allows for rating {@link Entity} objects that are in the database.
 */
public class RateingManager {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(RateingManager.class.getName());
	private SesameContextWrapper localstore;
	private String namespace;
	private AtomicInteger id = new AtomicInteger();

	public RateingManager(SesameContextWrapper localstore, NamespaceFactory namespace) {
		if (localstore == null) {
			throw new IllegalArgumentException("localstore must not be null");
		}
		this.localstore = localstore;
		this.namespace = namespace.getNamespace() + "rateing/";
	}

	/**
	 * Gets the rateing for the supplied entity
	 * 
	 * @param entity
	 * @return an existing rateing instance or null
	 */
	public Rateing getRatingFor(Entity entity) {
		if (entity.getQName() != null) {
			ElmoManager manager = localstore.createManager();
			String object = QNameURI.toString(entity.getQName());
			ElmoQuery it = manager.createQuery("SELECT ?r WHERE {?r <http://rdf.pace-project.org/diki#ratedItem> <" + object + ">}");
			Iterator<?> iterator = it.evaluate();
			if (iterator.hasNext()) {
				Object instance = iterator.next();
				if (instance instanceof Rateing) {
					return (Rateing) instance;
				}
			} else {
				/*
				 * creates a new rating instance. We use an incrementing integer
				 * with the base 16
				 */
				Rateing r = manager.designate(new QName(namespace, Integer.toString(id.incrementAndGet(), 16)), Rateing.class);
				r.setRatedItem(entity);
				return r;
			}
		}
		return null;
	}

	/**
	 * Sets the rateing for the given Entity to the supplied value. If a rating
	 * already exists it will be overwritten. Otherwise a new one will be
	 * created
	 * 
	 * @param entity
	 * @param rating
	 */
	public void setRatingFor(Entity entity, double rating) {
		Rateing r = getRatingFor(entity);
		if (r == null) {
			ElmoManager manager = localstore.createManager();
			/*
			 * creates a new rating instance. We use an incrementing integer
			 * with the base 16
			 */
			r = manager.designate(new QName(namespace, Integer.toString(id.incrementAndGet(), 16)), Rateing.class);
		}
		r.setRateing(rating);

	}

	/**
	 * @param person
	 * @return all ratings that are assigned to this person
	 */
	@SuppressWarnings("unchecked")
	public List<Rateing> getRatings(Person person) {
		ElmoManager manager = localstore.createManager();
		try {
			String personQ = QNameURI.toString(person.getQName());

			ElmoQuery it = manager.createQuery(SPARQLPrefix.PREFIX_TAGGING
					+ "SELECT ?r WHERE {?r <http://rdf.pace-project.org/diki#ratedItem> ?tagging . ?tagging tag:taggedBy <" + personQ + "> }");
			return it.getResultList();
		} finally {
			manager.close();
		}
	}
}
