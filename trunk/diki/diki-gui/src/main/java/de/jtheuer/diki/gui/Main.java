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
package de.jtheuer.diki.gui;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.io.*;
import java.util.logging.*;

import javax.swing.*;

import org.apache.commons.io.IOUtils;

import de.jtheuer.diki.gui.panels.MainPanel;
import de.jtheuer.diki.gui.utils.DikiResourceContainer;
import de.jtheuer.diki.gui.utils.Log;
import de.jtheuer.diki.lib.NetworkConnection;
import de.jtheuer.diki.lib.connectors.ConnectorException;
import de.jtheuer.jjcomponents.swing.ui.UIController;
import de.jtheuer.jjcomponents.utils.LocationAwareProperties;

/**
 * Main entrance point for the program
 */
public class Main implements DikiStrings {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
	public static final String LOOKANDFEEL = "looknfeel";
	private static final String SCRIPTS = "scripts";
	private static final String BOOKMARKSCRIPT = "newBookmark.sh";

	/**
	 * @param args
	 *            commandline arguments are not supported.
	 * @throws IOException 
	 * @throws SecurityException 
	 */
	public static void main(String[] args) throws SecurityException, IOException {
		new Main();
	}

	/** The Mainwindow */
	private JFrame mainwindow;
	protected JMenu mainmenu;
	protected JPopupMenu popmenu;
	private NetworkConnection networkConnection;
	private LocationAwareProperties properties;

	/**
	 * Constructs the Mainwindow
	 * @throws IOException 
	 * @throws SecurityException 
	 */
	private Main() throws SecurityException, IOException {
		setUpLogger();
		/* disable security - there is a bug in sesame! */
		System.setSecurityManager(null);
		
		LOGGER.fine("booting diki");

		NetworkConnection networkConnection;
		/* configure backend */
		try {
			/* configure network */
			networkConnection = configureNetwork();

			/* set a nice look'n'feel */
			if (!configurePLAF()) {
				Log.error(Messages.getString("Main.noLAF")); //$NON-NLS-1$
			}
			
			/* create mainwindow */
			mainwindow = getMainwindow(networkConnection);

			/* check if urls can be opened */
			if (!Desktop.isDesktopSupported()) {
				Log.error(Messages.getString("Main.DesktopUnsupported")); //$NON-NLS-1$
			}

		} catch (ConnectorException e) {
			Log.error(e);
		} catch (IOException e) {
			Log.error(e);
		}
	}

	/**
	 * @throws IOException 
	 * @throws SecurityException 
	 * 
	 */
	private void setUpLogger() throws SecurityException, IOException {
		InputStream logconfig = getClass().getResourceAsStream("/logging.properties");
		if(logconfig != null) {
			LogManager.getLogManager().readConfiguration(logconfig);
		}
		LOGGER.info("loglevel is set to " + LOGGER.getParent().getLevel());
	}

	/**
	 * The main window
	 * 
	 * @param connection
	 * @return
	 */
	private JFrame getMainwindow(NetworkConnection connection) {
		JFrame frame = new JFrame(APPLICATION_TITLE);
		frame.setIconImage(DikiResourceContainer.DIKI.getAsImage());
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(new MainPanel(networkConnection,frame,properties),BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
		return frame;
	}

	/**
	 * sets the jgoodies look'n'feel
	 */
	private boolean configurePLAF() {
		LOGGER.fine("setting LnF");
		try {
			String ui = properties.getProperty(LOOKANDFEEL);
			UIController.loadDefault(ui);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	private NetworkConnection configureNetwork() throws ConnectorException, IOException {
		LOGGER.fine("starting network configuration");
		networkConnection = new NetworkConnection();
		networkConnection.addAllConnectors();
		properties = loadConfiguration(networkConnection.getHomeDirectory());
// we do not want to connect, yet...
		//		networkConnection.connect(properties);
		return networkConnection;
	}

	private LocationAwareProperties loadConfiguration(File homedirectory) throws IOException {
		File configfile = new File(homedirectory.getAbsoluteFile() + File.separator + CONFIGFILE);
		File scriptdir = new File(homedirectory.getAbsoluteFile() + File.separator + SCRIPTS);
		File bookmarkscript = new File(scriptdir.getAbsoluteFile() + File.separator + BOOKMARKSCRIPT);
		
		/* copy template if it doesn't exist */
		if (!configfile.exists()) {
			/* load from resources */
			InputStream stream = getClass().getResourceAsStream("/" + CONFIGFILE); //$NON-NLS-1$
			IOUtils.copy(stream, new FileOutputStream(configfile));
		}
		
		/* copy scripts */
		if(!scriptdir.exists()) {
			if(scriptdir.mkdir()) {
				LOGGER.log(Level.WARNING, "Cannot create directory " + scriptdir.getAbsolutePath() + " further exceptions may follow!");
			}
		}
		InputStream stream = getClass().getResourceAsStream("/" + SCRIPTS + "/" + BOOKMARKSCRIPT); //$NON-NLS-1$
		IOUtils.copy(stream, new FileOutputStream(bookmarkscript));				

		LocationAwareProperties p = new LocationAwareProperties(configfile);

		if (p.getProperty(CONFIGFILE_VERSION) == null) {
			throw new IOException(Messages.getString("Main.invalidConfigfile")); //$NON-NLS-1$
		}
		return p;

	}
	


}
