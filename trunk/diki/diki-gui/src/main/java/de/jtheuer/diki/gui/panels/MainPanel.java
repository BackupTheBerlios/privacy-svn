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
package de.jtheuer.diki.gui.panels;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;

import com.jidesoft.swing.JideTabbedPane;

import de.jtheuer.diki.gui.ConfigurationWindow;
import de.jtheuer.diki.gui.DikiStrings;
import de.jtheuer.diki.gui.actions.*;
import de.jtheuer.diki.gui.controls.ConnectionStateController;
import de.jtheuer.diki.gui.http.HttpServer;
import de.jtheuer.diki.gui.panels.about.AboutTab;
import de.jtheuer.diki.gui.panels.friendpanel.FriendsPanel;
import de.jtheuer.diki.gui.panels.logpanel.LoggingPanel;
import de.jtheuer.diki.gui.panels.tagpanel.DragPanel;
import de.jtheuer.diki.gui.utils.DikiResourceContainer;
import de.jtheuer.diki.lib.NetworkConnection;
import de.jtheuer.diki.lib.connectors.ConnectorException;
import de.jtheuer.diki.lib.connectors.xmpp.XMPPConnector;
import de.jtheuer.jjcomponents.swing.panels.JJTabbedPane;
import de.jtheuer.jjcomponents.utils.LocationAwareProperties;
import de.jtheuer.jjcomponents.utils.ResourcesContainer;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * 
 */
public class MainPanel extends JPanel implements DikiStrings {
	/** generated */
	private static final long serialVersionUID = 3164169844564974282L;
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(MainPanel.class.getName());
	private final NetworkConnection networkConnection;
	private JFrame frame;
	private LocationAwareProperties properties;
	JJTabbedPane tabbedPane;

	/**
	 * 
	 */
	public MainPanel(NetworkConnection connection, JFrame frame, LocationAwareProperties properties) {
		this.networkConnection = connection;
		this.frame = frame;
		this.properties = properties;

		setLayout(new BorderLayout());
		createSystemTray(createPopupMenu());

		DragPanel dragpanel = new DragPanel(frame,connection);
		{ // CENTER
			tabbedPane = new JJTabbedPane(JideTabbedPane.TOP);
			tabbedPane.setOpaque(false);
			tabbedPane.setTabShape(JideTabbedPane.SHAPE_VSNET);
			
			JPanel centerpanel = new JPanel();
			centerpanel.setLayout(new BoxLayout(centerpanel, BoxLayout.PAGE_AXIS));

			tabbedPane.addTab("search", ResourcesContainer.FIND.getAsIcon(), new Searchpanel(connection));
			tabbedPane.addTab(dragpanel);
			tabbedPane.addTab(new FriendsPanel(connection));
			tabbedPane.addTab("logger", ResourcesContainer.INFO.getAsIcon(), new LoggingPanel());
			tabbedPane.addTab(new AboutTab());
			
			add(tabbedPane, BorderLayout.CENTER);
		}

		{ // SOUTH
			JMenuBar bar = new JMenuBar();
			bar.add(createMainMenu());
			bar.add(Box.createHorizontalGlue());
			bar.add(new ConnectionStateController(this,connection));
			add(bar, BorderLayout.SOUTH);
		}
		
		setEnabled(false);
		
		/* start with configuration settings if necessary
		 * otherwise login */
		String prop = properties.getProperty(XMPPConnector.KEY_USER);
		if(prop==null || "".equals(prop)) {
			new ConfigurationWindow(frame,properties, connection);
		} else {
			try {
				connection.connect(properties);
			} catch (ConnectorException e) {
				LOGGER.log(Level.WARNING,"autologin failed", e);
			}
		}
		
		/* start http server */
		try {
			new HttpServer(30013,dragpanel);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE,"cannot run http server for remote control!", e);
		}
	}

	/**
	 * toggles the visibility of the Mainwindow
	 */
	private void toggleShow() {

		if (frame.getExtendedState() == Frame.ICONIFIED || frame.isVisible() == false) {
			frame.setExtendedState(Frame.NORMAL);
			frame.setVisible(true);
			frame.toFront();
			frame.requestFocus();
		} else {
			frame.setExtendedState(Frame.ICONIFIED);
		}
	}

	@Override
	public void setEnabled(boolean enabled){
		super.setEnabled(enabled);

		for(int i=0; i<tabbedPane.getTabCount();i++) {
			if(tabbedPane.getComponentAt(i) instanceof LoggingPanel || tabbedPane.getComponentAt(i) instanceof AboutTab) {
				tabbedPane.setEnabledAt(i,true);
			} else {
				tabbedPane.setEnabledAt(i,enabled);
			}
		}
		
		/* Select about Tab if current Tab has been disabled */
		if(tabbedPane.isEnabledAt(tabbedPane.getSelectedIndex())==false) {
			tabbedPane.setSelectedIndex(tabbedPane.getTabCount()-1);
		}
	}
	
	/**
	 * The Main (and only) application menu
	 */
	private JMenu createMainMenu() {
		JMenu m = new JMenu();
		m.setIcon(DikiResourceContainer.DIKI_SMALL.getAsIcon());
		m.setText("Menu");
		addMainMenuItems(m);
		return m;
	}

	/**
	 * The Popup application menu
	 */
	private JPopupMenu createPopupMenu() {
		JPopupMenu m = new JPopupMenu();
		m.addSeparator();
		addMainMenuItems(m);
		return m;
	}

	/**
	 * adds the mainmenu to the supplied JComponent. Only makes sense for
	 * {@link JMenu} and {@link JPopupMenu}
	 */
	private void addMainMenuItems(JComponent component) {
		ConnectionAction a = new ConnectionAction(frame, properties, networkConnection);

		component.add(new JMenuItem(a.getConnectAction()));
		component.add(new JMenuItem(a.getDisconnectAction()));
		component.add(new JMenuItem(a.getReconnectAction()));
		component.add(new JPopupMenu.Separator());
		component.add(new JMenuItem(new ConfigureAction(frame, properties, networkConnection)));
//		component.add(new JMenuItem(a.getRedisplayAction()));
//		component.add(new JPopupMenu.Separator());
		component.add(new JMenuItem(new ExportAction(networkConnection)));
		component.add(new JPopupMenu.Separator());
		component.add(new JMenuItem(new CloseAction(frame, networkConnection)));
	}

	/**
	 * configures the SystemTray
	 * 
	 * @return the TrayIcon
	 */
	private TrayIcon createSystemTray(final JPopupMenu popmenu) {
		if (SystemTray.isSupported()) {

			SystemTray tray = SystemTray.getSystemTray();

			// remove all existing trayicons from our application
			while (tray.getTrayIcons().length > 0) {
				tray.remove(tray.getTrayIcons()[0]);
			}
			
			Image image = DikiResourceContainer.DIKI_GIF.getAsImage();

			final TrayIcon trayIcon = new TrayIcon(image, APPLICATION_TITLE);
			trayIcon.setToolTip(APPLICATION_TITLE);

			MouseAdapter madapter = new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if (e.getButton() == MouseEvent.BUTTON1) {
						toggleShow();
					} else if (e.getButton() == MouseEvent.BUTTON3) {
						popmenu.setInvoker(frame);
						popmenu.setLocation(e.getX(), e.getY());
						popmenu.setVisible(true);
					}
				}
			};

			trayIcon.addMouseListener(madapter);
			trayIcon.addMouseMotionListener(madapter);
			trayIcon.setImageAutoSize(false);

			try {
				tray.add(trayIcon);
				return trayIcon;
			} catch (AWTException e) {
				return null;
			}

		}
		return null;
	}


}
