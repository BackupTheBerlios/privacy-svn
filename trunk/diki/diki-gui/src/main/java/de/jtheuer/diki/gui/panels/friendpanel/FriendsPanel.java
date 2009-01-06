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
package de.jtheuer.diki.gui.panels.friendpanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.logging.Logger;

import javax.swing.*;

import org.openrdf.concepts.foaf.Person;

import com.jidesoft.dialog.JideOptionPane;

import de.jtheuer.diki.gui.utils.DikiResourceContainer;
import de.jtheuer.diki.lib.NetworkConnection;
import de.jtheuer.diki.lib.connectors.Connector.Status;
import de.jtheuer.jjcomponents.swing.panels.JJActivateableTab;
import de.jtheuer.jjcomponents.swing.panels.JJToolBar;
import de.jtheuer.jjcomponents.utils.ResourcesContainer;
import de.jtheuer.sesame.QNameURI;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * 
 * A Panel displaying all persons available in a {@link NetworkConnection}
 */
public class FriendsPanel extends JJActivateableTab {
	/**	 */
	private static final long serialVersionUID = -6923873602768852179L;

	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(FriendsPanel.class.getName());

	private NetworkConnection connection;
	private FriendsGraph graph;
	private JComponent display;

	

	/**
	 * 
	 */
	public FriendsPanel(NetworkConnection connection) {
		setLayout(new BorderLayout());
		
		/* toolbar */
		JJToolBar toolbar = new JJToolBar(JToolBar.HORIZONTAL);
		toolbar.add(getRenderAction()).setHideActionText(false);
		toolbar.add(getAddAction()).setHideActionText(false);
		add(toolbar,BorderLayout.NORTH);
		
		this.connection = connection;
	}

	
	
	@Override
	public void lazyInitialize() {
		if (connection.getStatus() == Status.Connected) {
			graph = new FriendsGraph(connection);
			graph.init();
			
			GraphDisplay dis = new GraphDisplay(graph, true,FriendsGraph.OFFLINE,FriendsGraph.ONLINE,FriendsGraph.FOF,FriendsGraph.RELOAD);
			dis.addControlListener(new FriendPopupMenu(graph));
			newDisplay(dis);
		} else {
			LOGGER.info("not connected");
		}
	}



	/**
	 * Returns an Action that pops up a message box where new friends can be added to
	 * @return
	 */
	private Action getAddAction() {
		return new AbstractAction("add friend",ResourcesContainer.ADDFRIEND.getAsIcon()) {
			/** auto generated	 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				if (connection.getStatus() == Status.Connected) {
					String question = "<html>Please enter your buddy's xmpp user id as (<b>username@server.example.com</b></html>";			
	                String result = JideOptionPane.showInputDialog(question);
	                if ((result != null) && (result.length() > 0)) {
						connection.getUserFactory().addFriend(new QNameURI("xmpp:"+result+"/"));
						graph.refreshFriends();
	                }

				}
			}
			
		};
	}


	protected AbstractAction getRenderAction() {
		return new AbstractAction("reload",ResourcesContainer.RESTART.getAsIcon()) {
			/** auto generated	 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				lazyInitialize();
			}
			
		};
		
	}

	public void newDisplay(JComponent c) {
		if(display!=null) {
			remove(display);
		}
		display = c;
		add(display, BorderLayout.CENTER);
		this.revalidate();
	}

	@Override
	public Icon getIcon() {
		return ResourcesContainer.ADDFRIEND.getAsIcon();
	}

	@Override
	public String getName() {
		return "friends";
	}
}
