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
package de.jtheuer.diki.lib.connectors;

import java.awt.Image;

import de.jtheuer.diki.lib.NetworkConnection;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 */
public abstract class AbstractConnector implements Connector {

	private NetworkConnection connection;
	private String name;
	private Image icon;
	private Status connectionstatus=Status.Disconnected;

	public AbstractConnector(NetworkConnection connection) {
		this.connection = connection;
	}
	
	/**
	 * A new {@link AbstractSimpleConnector} with the given name and icon
	 * @param name
	 * @param icon
	 * @param connection
	 */
	public AbstractConnector(String name, Image icon, NetworkConnection connection) {
		this(connection);
		this.name = name;
		this.icon = icon;
	}

	@Override
	public Image getIcon() {
		return icon;
	}

	@Override
	public String getName() {
		return name;
	}

	/**
	 * @return the associated NetworkConnection
	 */
	protected NetworkConnection getConnection() {
		return connection;
	}

	public void disconnect() {
		setStatus(Status.Disconnected);
	}
		
	protected void setStatus(Status newstatus) {
		connectionstatus = newstatus;
		connection.fireStatusUpdate();
	}

	public Status getStatus() {
		return connectionstatus;
	}

	@Override
	public ParameterProperties getProperties() {
		/* empty list */
		return new ParameterProperties();
	}

	@Override
	public double distance() {
		return 0;
	}
	

	
}