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
	
package de.jtheuer.diki.server;
import java.io.*;
import java.util.Properties;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.apache.commons.cli.*;

import de.jtheuer.diki.lib.NetworkConnection;
import de.jtheuer.diki.lib.connectors.ConnectorException;
import de.jtheuer.diki.lib.connectors.simple.RDFImporter;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 */
public class DikiServer {
	/* automatically generated Logger */@SuppressWarnings("unused") //$NON-NLS-1$
	private static Logger LOGGER;
	private static final String PROPERTIES = "properties"; //$NON-NLS-1$
	private static final String RDFFILE = "rdffile"; //$NON-NLS-1$
	
	public static void main(String[] args) {
		new DikiServer(args);
	}
	
	public DikiServer(String[] args) {
		/* parse arguments */
		CommandLineParser parser = new GnuParser();
		String file=""; //$NON-NLS-1$
		try {
			LogManager.getLogManager().readConfiguration(getClass().getResourceAsStream("/logging.properties")); //$NON-NLS-1$
			
			LOGGER = Logger.getLogger(DikiServer.class.getName());
			LOGGER.info("logging set to " + LOGGER.getParent().getLevel()); //$NON-NLS-1$
			
			CommandLine result = parser.parse(getCommandLineOptions(), args);
			Properties properties = new Properties();
			
			file = result.getOptionValue("p"); //$NON-NLS-1$
			LOGGER.fine("got option Value: "+ file); //$NON-NLS-1$
			properties.load(new FileInputStream(file));
			
			/* read rdf file and overwrite if necessary */
			file = result.getOptionValue("r"); //$NON-NLS-1$
			if(file!=null) {
				properties.setProperty(RDFImporter.FILE, file);
			}
			NetworkConnection connection = new NetworkConnection();
			connection.addConnector(new RDFImporter("rdfimporter",null,connection)); //$NON-NLS-1$
			connection.addAllConnectors();
			connection.connect(properties);
			
			/* wait until any key */
			System.in.read();
			
			connection.disconnect();
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		} catch (FileNotFoundException e) {
			System.out.println(Messages.getString("DikiServer.filenotfound") + file); //$NON-NLS-1$
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (ConnectorException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		System.exit(0);
	}
	
	private static Options getCommandLineOptions() {
		Options o = new Options();
		o.addOption("p", PROPERTIES, true, "location of the properties file"); //$NON-NLS-1$ //$NON-NLS-2$
		o.addOption("r", RDFFILE, true, "location of a initial rdf file"); //$NON-NLS-1$ //$NON-NLS-2$
		return o;
	}
}
