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
import java.util.Set;

import org.openrdf.concepts.foaf.Agent;
import org.openrdf.concepts.owl.Class;
import org.openrdf.elmo.annotations.rdf;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * superclass for instances that are exchanged over the network
 */
@rdf("http://rdf.pace-project.org/diki#BaseNetworkPacket")
public interface BaseNetworkPacket extends Class {

	@rdf("http://rdf.pace-project.org/diki#createdBy")
	public abstract Set<Agent> getCreatedBy();
	public abstract void setCreatedBy(Set<Agent> creator);

}
