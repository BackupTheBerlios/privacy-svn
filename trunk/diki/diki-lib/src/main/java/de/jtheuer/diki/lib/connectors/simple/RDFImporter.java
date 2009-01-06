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
package de.jtheuer.diki.lib.connectors.simple;

import java.awt.Image;
import java.io.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openrdf.rio.RDFFormat;

import de.jtheuer.diki.lib.NetworkConnection;
import de.jtheuer.diki.lib.connectors.*;
import de.jtheuer.diki.lib.query.NetworkQuery;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * 
 */
public class RDFImporter extends AbstractSimpleConnector implements Connector {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(RDFImporter.class.getName());
	public static final String FILE = "rdfimporter.file";

	/**
	 * @param name
	 * @param icon
	 * @param connection
	 */
	public RDFImporter(String name, Image icon, NetworkConnection connection) {
		super(name, icon, connection);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jtheuer.diki.lib.connectors.Connector#connect(java.util.Properties)
	 */
	@Override
	public void connect(Properties prop) throws ConnectorException {
		String file = prop.getProperty(FILE);
		LOGGER.log(Level.INFO, "Loading rdf importer");
		if (file != null) {
			try {
				getConnection().getSesame().getLocalStore().add(new BufferedReader(new InputStreamReader(new FileInputStream(file))), "", RDFFormat.RDFXML);
			} catch (Exception e) {
				LOGGER.log(Level.WARNING, "Cannot parse document" + file, e);
				throw new ConnectorException(e);
			}
		} else {
			LOGGER.log(Level.WARNING, "No rdf to specified to import");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jtheuer.diki.lib.connectors.Connector#getParameters()
	 */
	@Override
	public Properties getParameters() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jtheuer.diki.lib.connectors.Connector#getStatus()
	 */
	@Override
	public Status getStatus() {
		return Status.Connected;
	}

	/* (non-Javadoc)
	 * @see de.jtheuer.diki.lib.connectors.AbstractSimpleConnector#getQuerySolver(de.jtheuer.diki.lib.query.Query)
	 */
	@Override
	protected Runnable innerEvaluate(NetworkQuery networkQuery) {
		return null;
	}
}
