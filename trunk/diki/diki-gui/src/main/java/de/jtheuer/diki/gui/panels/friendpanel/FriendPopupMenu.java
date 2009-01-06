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

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.JPopupMenu;

import prefuse.controls.ControlAdapter;
import prefuse.visual.VisualItem;
import de.jtheuer.jjcomponents.utils.ResourcesContainer;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * 
 */
public class FriendPopupMenu extends ControlAdapter {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(FriendPopupMenu.class.getName());
	private JPopupMenu popupmenu;
	private VisualItem currentitem;

	/**
	 * 
	 */
	public FriendPopupMenu(final FriendsGraph fgraph) {
		popupmenu = new JPopupMenu();
		
		popupmenu.add(new AbstractAction("show user info",ResourcesContainer.INFO.getAsIcon()) {
			private static final long serialVersionUID = 1693798154908210494L;

			@Override
			public void actionPerformed(ActionEvent e) {
				fgraph.info(currentitem);
			}

		});

		popupmenu.add(new AbstractAction("delete friend",ResourcesContainer.CANCEL.getAsIcon()) {
			private static final long serialVersionUID = -944414691384992715L;

			@Override
			public void actionPerformed(ActionEvent e) {
				fgraph.deleteNode(currentitem);
			}

		});

		popupmenu.add(new AbstractAction("refresh user",ResourcesContainer.RESTART.getAsIcon()) {
			private static final long serialVersionUID = 4447326747036052926L;

			@Override
			public void actionPerformed(ActionEvent e) {
				fgraph.requestFriends(currentitem);
			}

		});

	}

	@Override
	public void itemPressed(VisualItem item, MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON3) {
			this.currentitem = item;
			popupmenu.show(e.getComponent(), e.getX(), e.getY());
		}
	}

}
