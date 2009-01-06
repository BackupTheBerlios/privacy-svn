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
/**
    (c) by Jan Torben Heuer <jan.heuer@uni-muenster.de

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/
 */
package de.jtheuer.diki.lib.connectors;
import java.awt.Image;
import java.util.Hashtable;
import java.util.Properties;
import java.util.concurrent.ExecutorService;

import de.jtheuer.diki.lib.query.AbstractQuery;
import de.jtheuer.diki.lib.query.NetworkQuery;

/**
 * Interface for connectors.
 * 
 * After constructing a connector you have to call the connected Method.
 * If getStatus() returns anything else than Connected, other methods may fail. 
 */
public interface Connector {
	
	/** The current status of the connector	 */
	public enum Status {Disconnected, Connecting, Connected, Disabled};
	
	/**
	 * @return a descriptive name for the connector
	 */
	public String getName();
	
	/**
	 * @return an icon representing the connector
	 */
	public Image getIcon();
	
	/**
	 * @return the connectors connection status. {@link Connector.Status}
	 */
	public Status getStatus();
	
	/**
	 * Tells the connector to run the given {@link AbstractQuery} with a new Thread on the given {@link ExecutorService}
	 * @param query 
	 * @param result
	 */
	public void evaluate(NetworkQuery query);
	
	/**
	 * @return a Properties instance with keys that represent configuration parameters for this connector
	 */
	@Deprecated
	public Hashtable<Object,Object> getParameters();

	/**
	 * @return an Iterator over the parameters that this {@link Connector} supplies;
	 */
	public ParameterProperties getProperties();
	
	/**
	 * Tells the Connector to initialize the connection
	 * @param prop
	 * @throws ConnectorException
	 */
	public void connect(Properties prop) throws ConnectorException;
	
	/**
	 * release any resources assigned to this connector (files, logins, ...)
	 */
	public void disconnect();
	
	/**
	 * @return the distance to this Connector that must be substracted before calling this connector
	 */
	public double distance();
	
}
