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
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 */
public abstract class ElmoNode implements ElmoModelNode {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(ElmoNode.class.getName());
	
	private ElmoNode parent;

	private List<?> nodes;

	private String title;
	
	public ElmoNode(ElmoNode parent) {
		this.parent = parent;
	}

	/**
	 * @param parent
	 * @param title
	 */
	public ElmoNode(ElmoNode parent, String title) {
		this(parent);
		this.title = title;
	}

	public void setNodes(List<? extends ElmoNode> nodes) {
		this.nodes = nodes;
	}

	public void setNodesString(List<String> nodes) {
		this.nodes = nodes;
	}
	
	public ElmoNode getParent() {
		return parent;
	}

	@Override
	public Object getChildAt(int index) {
		return nodes.get(index);
	}

	@Override
	public int getChildCount() {
		return nodes == null? 0:nodes.size();
	}

	/**
	 * @param root
	 */
	public void setParent(ElmoNode root) {
		parent = root;
	}

	@Override
	public String toString() {
		return title;
	}
	
}
