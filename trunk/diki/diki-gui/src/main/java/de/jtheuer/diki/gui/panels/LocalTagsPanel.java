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
package de.jtheuer.diki.gui.panels;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.List;

import javax.swing.*;

import de.jtheuer.diki.gui.utils.BookmarkTransferComponent;
import de.jtheuer.diki.gui.utils.BookmarkTransferHandler;
import de.jtheuer.diki.lib.NetworkConnection;
import de.jtheuer.jjcomponents.utils.ResourcesContainer;

/**
 * 
 */
@Deprecated
public class LocalTagsPanel extends JPanel implements BookmarkTransferComponent {

	private BookmarkTransferHandler handler;

	private Clipboard clipboard;
	private JFrame parent;

	private NetworkConnection connection;

	public LocalTagsPanel(JFrame parent, NetworkConnection connection) {
		this.parent = parent;
		this.connection = connection;
		setLayout(new BorderLayout());
		
		/* the drag'n'drop handler */
		handler = new BookmarkTransferHandler(this);
		clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

		initgui();
	}

	/**
	 * shows the EditTagPanel
	 */
	public void newBookmark(List<URL> bookmarkUrlList) {

		if (bookmarkUrlList != null && bookmarkUrlList.size() >= 1) {
			String url = bookmarkUrlList.get(0).toExternalForm();
			new EditTagPanel(parent,connection,url);
		} else {
			new EditTagPanel(parent,connection);
		}
	}

	/**
	 * The Dragpanel contains the controls to create new, drop down or import
	 * from clipboard.
	 */
	private void initgui() {
		JToolBar toolbar = new JToolBar(JToolBar.HORIZONTAL);


		JLabel dragdrop = new JLabel("drag'n'drope here! ");
		dragdrop.setIcon(ResourcesContainer.DRAGDROP.getAsIcon());
		dragdrop.setTransferHandler(handler);



		Action newResource = new AbstractAction("create new", ResourcesContainer.NEW.getAsIcon()) {
			private static final long serialVersionUID = 4529181620321631446L;

			public void actionPerformed(ActionEvent e) {
				newBookmark(null);
			}
		};


		Action pasteResource = new AbstractAction("paste", ResourcesContainer.PASTE.getAsIcon()) {
			private static final long serialVersionUID = -6976104956131406164L;

			public void actionPerformed(ActionEvent e) {
				handler.importData(clipboard.getContents(null));
			}
		};

		toolbar.add(dragdrop);
		toolbar.add(newResource);
		toolbar.add(pasteResource);
		
		add(toolbar,BorderLayout.NORTH);
	}

}
