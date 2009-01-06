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

import java.util.Vector;
import java.util.logging.Logger;

import org.openrdf.elmo.ElmoManager;
import org.openrdf.elmo.Entity;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * 
 * This node is a rootnode that contains all elmo instances of a given
 * classtype.
 * 
 */
public abstract class ElmoRootClassNode<T extends Entity> extends ElmoNode {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(ElmoRootClassNode.class.getName());

	private Class<T> classtype;

	private ElmoManager manager;

	public ElmoRootClassNode(ElmoNode parent, Class<T> classtype, ElmoManager manager) {
		super(parent);
		this.manager = manager;
		this.classtype = classtype;
	}

	private void fetchChildren() {
		if (manager != null) {
			Iterable<T> list = manager.findAll(classtype);
			Vector<ElmoNode> children = new Vector<ElmoNode>();
			for (T t : list) {
				children.add(renderChild(t));
			}
			setNodes(children);
		}
	}

	@Override
	public void lazyInitialize() {
		fetchChildren();
	}

	protected abstract ElmoClassNode renderChild(T child);

	public String toString() {
		return classtype.getSimpleName();
	}
}
