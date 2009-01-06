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

import prefuse.Visualization;
import prefuse.action.assignment.ColorAction;
import prefuse.visual.VisualItem;

/**
 * This class is a specific ColorAction for the nodes in the graph.
 * <p/>
 * Project OWL2Prefuse <br/>
 * NodeColorAction.java created 3 januari 2007, 13:37
 * <p/>
 * Copyright &copy 2006 Jethro Borsje
 * @author <a href="mailto:info@jborsje.nl">Jethro Borsje</a>
 * @version $$Revision:$$, $$Date:$$
 */

public class NodeColorAction extends ColorAction
{
    /**
     * Creates a new instance of NodeColorAction.
     * @param p_group The data group for which this ColorAction provides the colors.
     * @param p_vis A reference to the visualization processed by this Action.
     */
    public NodeColorAction(String p_group, Visualization p_vis)
    {
        super(p_group, VisualItem.FILLCOLOR);
        m_vis = p_vis;
    }

    /**
     * This method returns the color of the given VisualItem.
     * @param p_item The node for which the color needs to be retreived.
     * @return The color of the given node.
     */
    public int getColor(VisualItem p_item)
    {
        int retval = Constants.NODE_DEFAULT_COLOR;
        
        if (m_vis.isInGroup(p_item, Visualization.SEARCH_ITEMS)) retval = Constants.NODE_COLOR_SEARCH;
        else if (p_item.isHighlighted()) retval = Constants.NODE_COLOR_HIGHLIGHTED;
        else if (p_item.isFixed()) retval = Constants.NODE_COLOR_SELECTED;
        else if (p_item.canGetString("type"))
        {
            if (p_item.getString("type") != null)
            {
                if (p_item.getString("type").equals("class")) retval = Constants.NODE_COLOR_CLASS;
                else if (p_item.getString("type").equals("individual")) retval = Constants.NODE_COLOR_INDIVIDUAL;
            }
        }
        
        return retval;
    }
}