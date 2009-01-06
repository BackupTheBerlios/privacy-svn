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
package de.jtheuer.jjcomponents.swing.tree;

import java.io.*;
import javax.swing.event.*;
import javax.swing.tree.*;

/**
 * <CODE>AbstractTableModel</CODE> is an abstract implementation of the
 * <CODE>TreeModel</CODE> interface. It simply provides several methods for
 * handling <CODE>TreeModelListener</CODE>s.
 * 
 * @author Stephan Friedrichs
 * @version 1.0.0
 */
public abstract class AbstractTreeModel implements TreeModel, Serializable {
	/**
	 * An <CODE>EventListenerList</CODE> used to hold references to the added
	 * <CODE>TreeModelListener</CODE>s. Declared <CODE>protected</CODE> to
	 * enable use for other listeners in subclasses.
	 * 
	 * @since 1.0.0
	 */
	protected EventListenerList listenerList = new EventListenerList();

	/**
	 * Sole constructor.
	 * 
	 * @since 1.0.0
	 */
	public AbstractTreeModel() {}

	/**
	 * Adds a listener for the <code>TreeModelEvent</code> posted after the
	 * tree changes.
	 * 
	 * @param tml
	 *            the listener to add
	 * @see #removeTreeModelListener(TreeModelListener)
	 * @since 1.0.0
	 */
	public void addTreeModelListener(TreeModelListener tml) {
		if (tml != null) {
			listenerList.add(TreeModelListener.class, tml);
		}
	}

	/**
	 * Removes a listener previously added with
	 * <CODE>addTreeModelListener(TreeModelListener)</CODE>.
	 * 
	 * @param tml
	 *            The <CODE>TreeModelListener</CODE> to be removed.
	 * @see #addTreeModelListener(TreeModelListener)
	 * @since 1.0.0
	 */
	public void removeTreeModelListener(TreeModelListener tml) {
		if (tml != null) {
			listenerList.remove(TreeModelListener.class, tml);
		}
	}

	/**
	 * Notifies all added <CODE>TreeModelListener</CODE>s that tree nodes
	 * have changed.
	 * 
	 * @param event
	 *            The actual <CODE>TreeModelEvent</CODE>.
	 * @see #fireTreeNodesChanged(Object[], int[], Object[])
	 */
	protected void fireTreeNodesChanged(TreeModelEvent event) {
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == TreeModelListener.class) {
				((TreeModelListener) listeners[i + 1]).treeNodesChanged(event);
			}
		}
	}

	/**
	 * Notifies all added <CODE>TreeModelListener</CODE>s that tree nodes
	 * have been inserted.
	 * 
	 * @param event
	 *            The actual <CODE>TreeModelEvent</CODE>.
	 * @see #fireTreeNodesInserted(Object[], int[], Object[])
	 */
	protected void fireTreeNodesInserted(TreeModelEvent event) {
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == TreeModelListener.class) {
				((TreeModelListener) listeners[i + 1]).treeNodesInserted(event);
			}
		}
	}

	/**
	 * Notifies all added <CODE>TreeModelListener</CODE>s that tree nodes
	 * been removed.
	 * 
	 * @param event
	 *            The actual <CODE>TreeModelEvent</CODE>.
	 * @see #fireTreeNodesRemoved(Object[], int[], Object[])
	 */
	protected void fireTreeNodesRemoved(TreeModelEvent event) {
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == TreeModelListener.class) {
				((TreeModelListener) listeners[i + 1]).treeNodesRemoved(event);
			}
		}
	}

	/**
	 * Notifies all added <CODE>TreeModelListener</CODE>s that the tree
	 * structure has changed.
	 * 
	 * @param event
	 *            The actual <CODE>TreeModelEvent</CODE>.
	 * @see #fireTreeStructureChanged(Object[])
	 */
	protected void fireTreeStructureChanged(TreeModelEvent event) {
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == TreeModelListener.class) {
				TreeModelListener tml = (TreeModelListener) listeners[i + 1];
				tml.treeStructureChanged(event);
			}
		}
	}

	/**
	 * Notifies all added <CODE>TreeModelListener</CODE>s that tree nodes
	 * have changed.
	 * 
	 * @param path
	 *            The path to the root node.
	 * @param childIndices
	 *            The indices of the children that have changed.
	 * @param children
	 *            The changed elements.
	 * @see #fireTreeNodesChanged(TreeModelEvent)
	 */
	protected void fireTreeNodesChanged(Object[] path, int[] childIndices, Object[] children) {
		TreeModelEvent event;
		event = new TreeModelEvent(this, path, childIndices, children);
		fireTreeNodesChanged(event);
	}

	/**
	 * Notifies all added <CODE>TreeModelListener</CODE>s that tree nodes
	 * have been inserted.
	 * 
	 * @param path
	 *            The path to the root node.
	 * @param childIndices
	 *            The indices of the children that have been inserted.
	 * @param children
	 *            The inserted elements.
	 * @see #fireTreeNodesInserted(TreeModelEvent)
	 */
	protected void fireTreeNodesInserted(Object[] path, int[] childIndices, Object[] children) {
		TreeModelEvent event;
		event = new TreeModelEvent(this, path, childIndices, children);
		fireTreeNodesInserted(event);
	}

	/**
	 * Notifies all added <CODE>TreeModelListener</CODE>s that tree nodes
	 * been removed.
	 * 
	 * @param path
	 *            The path to the root node.
	 * @param childIndices
	 *            The indices of the children that have been removed.
	 * @param children
	 *            The removed elements.
	 * @see #fireTreeNodesRemoved(TreeModelEvent)
	 */
	protected void fireTreeNodesRemoved(Object[] path, int[] childIndices, Object[] children) {
		TreeModelEvent event;
		event = new TreeModelEvent(this, path, childIndices, children);
		fireTreeNodesRemoved(event);
	}

	/**
	 * Notifies all added <CODE>TreeModelListener</CODE>s that the tree
	 * structure has changed.
	 * 
	 * @param path
	 *            The path to the root node.
	 * @see #fireTreeStructureChanged(TreeModelEvent)
	 */
	protected void fireTreeStructureChanged(Object[] path) {
		fireTreeStructureChanged(new TreeModelEvent(this, path));
	}

}