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
package de.jtheuer.diki.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.logging.Logger;

import javax.swing.*;

import com.jidesoft.dialog.JideOptionPane;
import com.jidesoft.dialog.PageList;
import com.jidesoft.swing.JideSwingUtilities;

import de.jtheuer.diki.gui.panels.configpanel.*;
import de.jtheuer.diki.lib.NetworkConnection;
import de.jtheuer.jjcomponents.PropertiesPersistence;
import de.jtheuer.jjcomponents.swing.dialog.JJMultiplePageDialog;
import de.jtheuer.jjcomponents.utils.LocationAwareProperties;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * 
 * Configuration window for diki
 */
public class ConfigurationWindow extends JJMultiplePageDialog implements DikiStrings {
	/** automatically generated serial */
	private static final long serialVersionUID = -8040407513209817188L;
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(ConfigurationWindow.class.getName());
	private PropertiesPersistence properties;
	private final Component rootFrame;
	private NetworkConnection connection;
	protected ConfigurationWindow thisframe;

	/**
	 * 
	 */
	public ConfigurationWindow(Frame owner, LocationAwareProperties properties, NetworkConnection connection) {
		super(owner, Messages.getString("ConfigurationWindow.1"), true, ICON_STYLE); //$NON-NLS-1$
		rootFrame = owner;
		this.thisframe = this;
		this.connection = connection;
		this.properties = new PropertiesPersistence(properties);

		setTitle(Messages.getString("ConfigurationWindow.1"));
		setPageList(new PageList());
		getPageList().append(new AccountPage(this.properties, connection));
		getPageList().append(new LookAndFeelPage(this.properties,connection, this,owner));
		getPageList().append(new ProfilePage(connection));
		getPageList().append(new PGPPage(connection));

		
		/* init */
		for(int i=0;i<getPageList().getPageCount();i++) {
			getPageList().getPage(i).initialize();
		}
		
		/* read properties and fill gui */
		this.properties.loadProperties();

		/* display and layout window */
		pack();
		JideSwingUtilities.globalCenterWindow(this);
		setVisible(true);
	}

	@Override
	protected void initComponents() {
		super.initComponents();
		getContentPanel().setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
		getIndexPanel().setBackground(Color.white);
		getButtonPanel().setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
		getPagesPanel().setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(800, 600);
	}

	@Override
	public Action getCancelAction() {
		return new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(properties.haveChanged()) {
					int result = JideOptionPane.showConfirmDialog(null, "Do you want to save your changes?", DikiStrings.APPLICATION_TITLE, JideOptionPane.YES_NO_OPTION, JideOptionPane.QUESTION_MESSAGE);
					if(result == JideOptionPane.YES_OPTION) {
						getOkButton().doClick();
					}
				}
                setVisible(false);
                dispose();
			}
		
		};
	}

	@Override
	public AbstractAction getOkAction() {
		return new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				properties.saveProperties();
                setVisible(false);
                dispose();
			}
		
		};
	}

	public static void main(String[] args) {
		new ConfigurationWindow(null, new LocationAwareProperties(), null);
	}
}
