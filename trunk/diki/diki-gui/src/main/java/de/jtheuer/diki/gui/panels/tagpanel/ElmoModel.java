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
	
package de.jtheuer.diki.gui.panels.tagpanel;
import java.util.logging.Logger;

import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import de.jtheuer.diki.lib.NetworkConnection;
import de.jtheuer.jjcomponents.swing.tree.AbstractTreeModel;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 * An elmo contains three different types of nodes:
 * <ol>
 * <li>ElmoQueryNodes which contains searchresults as Children</li>
 * <li>ElmoInstance nodes which represent concrete instances</li>
 * <li>ElmoProperty nodes which represent properties</li>
 * <li>ElmoValueNodes which are simple values like strings</li>
 * </ol>
 */
public class ElmoModel extends AbstractTreeModel implements TreeModel {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(ElmoModel.class.getName());
	private NetworkConnection connection;

	ElmoNode[] nodeslist;
	ElmoNode root;
	/**
	 * @param connection
	 */
	public ElmoModel(NetworkConnection connection, ElmoNode ...rootlist) {
		this.connection = connection;
		this.nodeslist = rootlist;
		
		/* construct the invisible root */
		this.root = new ElmoNode(null) {

			@Override
			public ElmoModelNode getChildAt(int index) {
				return nodeslist[index];
			}

			@Override
			public int getChildCount() {
				return nodeslist.length;
			}

			@Override
			public void lazyInitialize() {}
			
			@Override
			public String toString() {
				return "root";
			}
			
		};
		
		for (ElmoNode elmoNode : rootlist) {
			elmoNode.setParent(root);
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#getChild(java.lang.Object, int)
	 */
	@Override
	public Object getChild(Object parent, int index) {
		if (parent instanceof ElmoNode) {
			ElmoNode elmoNode = (ElmoNode) parent;
			return elmoNode.getChildAt(index);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#getChildCount(java.lang.Object)
	 */
	@Override
	public int getChildCount(Object parent) {
		if (parent instanceof ElmoNode) {
			ElmoNode elmoNode = (ElmoNode) parent;
			
			return elmoNode.getChildCount();
		}
		return 0;
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#getIndexOfChild(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int getIndexOfChild(Object parent, Object child) {
		return 0;
	}


	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#getRoot()
	 */
	@Override
	public Object getRoot() {
		return root;
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#isLeaf(java.lang.Object)
	 */
	@Override
	public boolean isLeaf(Object node) {
		if (node instanceof ElmoNode) {
			ElmoNode elmonode = (ElmoNode) node;

			/* init */
			elmonode.lazyInitialize();
			return elmonode.getChildCount() == 0;
		}
		
		return true;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#valueForPathChanged(javax.swing.tree.TreePath, java.lang.Object)
	 */
	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {}

}
