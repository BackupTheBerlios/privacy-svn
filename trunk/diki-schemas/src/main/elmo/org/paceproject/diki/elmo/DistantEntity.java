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

package org.paceproject.diki.elmo;

import org.openrdf.concepts.owl.Class;
import org.openrdf.elmo.annotations.rdf;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * 
 * This entity is used to store a specific direct distance.
 * 
 * For distance values must be true that:
 * 
 * Sum(i=0 to n, i) = n. That means: The sum of all distances must be the amount
 * of distances.
 */
@rdf("http://rdf.pace-project.org/diki#DistanceNetworkEntity")
public interface DistantEntity extends Class {

	/**
	 * A network distance. Between 0 and 2. 1 is default
	 * 
	 * @return the distance to this subject.
	 */
	@rdf("http://rdf.pace-project.org/diki#distance")
	public abstract double getDistance();

	/**
	 * Sets the distance to the supplied value
	 * @param distance
	 */
	public abstract void setDistance(double distance);

}
