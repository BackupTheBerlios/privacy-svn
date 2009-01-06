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

import org.openrdf.elmo.Entity;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 */
public abstract class ElmoClassNode extends ElmoNode {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(ElmoClassNode.class.getName());
	private Entity entity;
	private String title;

	/**
	 * @param parent
	 * @param manager
	 */
	public ElmoClassNode(ElmoNode parent, Entity entity) {
		super(parent, entity.getQName().toString());
		this.entity = entity;
	}
	
	/**
	 * @param parent
	 * @param manager
	 */
	public ElmoClassNode(ElmoNode parent, Entity entity, String title) {
		super(parent, title);
		this.entity = entity;
	}

	/**
	 * @return
	 */
	public Entity getEntity() {
		return entity;
	}
}
