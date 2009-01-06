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

import java.util.Iterator;

import prefuse.Visualization;
import prefuse.action.Action;
import prefuse.data.tuple.TupleSet;
import prefuse.visual.DecoratorItem;
import prefuse.visual.VisualItem;

/**
 * This action takes care of the visibility of edge decorators. These should be
 * hidden when the edge they are decorating is hided. These edges get hidden
 * because of the GraphDistanceFilter. <p/> Project OWL2Prefuse <br/>
 * HideDecoratorAction.java created 3 januari 2007, 22:25 <p/> Copyright &copy
 * 2006 Jethro Borsje
 * 
 * @author <a href="mailto:info@jborsje.nl">Jethro Borsje</a>
 * @version $$Revision:$$, $$Date:$$
 */
public class HideDecoratorAction extends Action {
	/**
	 * Creates a new instance of HideDecoratorAction.
	 * 
	 * @param p_vis
	 *            A reference to the visualization processed by this Action.
	 */
	public HideDecoratorAction(Visualization p_vis) {
		m_vis = p_vis;
	}

	/**
	 * This method is an abstract method from Action which is overloaded here.
	 * It takes care of hiding the decorators of which the visual items they are
	 * decorating are hidden.
	 * 
	 * @param frac
	 *            The fraction of this Action's duration that has elapsed.
	 */
	public void run(double frac) {
		TupleSet decorators = m_vis.getGroup(Constants.EDGE_DECORATORS);

		// If there are decorators present, the visibility of them is handled
		// here.
		if (decorators != null) {
			Iterator<DecoratorItem> it = decorators.tuples();

			while (it.hasNext()) {
				DecoratorItem decorator = it.next();
				VisualItem edge = decorator.getDecoratedItem();
				decorator.setVisible(edge.isVisible() && edge.isHighlighted());
			}
		}
	}
}