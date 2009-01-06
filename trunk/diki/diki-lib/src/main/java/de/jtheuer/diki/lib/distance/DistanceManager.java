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
import java.util.logging.Logger;

import org.openrdf.elmo.ElmoManager;
import org.paceproject.diki.elmo.Query;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 */
public class DistanceManager {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(DistanceManager.class.getName());
	
	
	/**
	 * Removes the given amount from the first Query instance that is found in the manager's context.
	 * @param manager
	 * @param amount
	 */
	public static void remove(ElmoManager manager, int amount) {
		Iterator<Query> resultset = manager.findAll(Query.class).iterator();
		
		if(resultset.hasNext()) {
			Query q = resultset.next();
			double d = q.getDistance();
			q.setDistance(d-amount);
		} else {
			throw new IllegalArgumentException("No query available in the manager's context");
		}
	}
}
