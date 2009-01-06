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

import java.awt.Color;

import prefuse.util.ColorLib;

/**
 * This class contains all the instances which are used in the program. <p/>
 * Project OWL2Prefuse <br/> Constants.java created 3 januari 2007, 15:05 <p/>
 * Copyright &copy 2006 Jethro Borsje
 * 
 * @author <a href="mailto:info@jborsje.nl">Jethro Borsje</a>
 * @version $$Revision:$$, $$Date:$$
 */
public final class Constants {
	/**
	 * The color of nodes in the Prefuse visualization which represent OWL
	 * Classes.
	 */
	public final static int NODE_COLOR_CLASS = ColorLib.rgb(253, 211, 100);

	/**
	 * The color of nodes in the Prefuse visualization which represent OWL
	 * Individuals.
	 */
	public final static int NODE_COLOR_INDIVIDUAL = ColorLib.rgb(220, 70, 217);

	/**
	 * The color of selected nodes in the Prefuse visualization.
	 */
	public final static int NODE_COLOR_SELECTED = ColorLib.rgb(255, 100, 100);

	/**
	 * The color of highlighted nodes in the Prefuse visualization.
	 */
	public final static int NODE_COLOR_HIGHLIGHTED = ColorLib.rgb(144, 253, 126);

	/**
	 * The color of nodes in the keyword search resultset in the Prefuse
	 * visualization.
	 */
	public final static int NODE_COLOR_SEARCH = ColorLib.rgb(255, 190, 190);

	/**
	 * The default color for nodes (if nothing else applies).
	 */
	public final static int NODE_DEFAULT_COLOR = ColorLib.rgb(200, 200, 255);

	/**
	 * The name of the data group which represents a graph in Prefuse.
	 */
	public static final String GRAPH = "graph";

	/**
	 * The name of the data group which represents nodes in a graph in Prefuse.
	 */
	public static final String GRAPH_NODES = "graph.nodes";

	/**
	 * The name of the data group which represents edges in a graph in Prefuse.
	 */
	public static final String GRAPH_EDGES = "graph.edges";

	/**
	 * The name of the data group which represents decorators of edges in a
	 * graph in Prefuse.
	 */
	public static final String EDGE_DECORATORS = "edgeDecorators";

	/**
	 * The label of nodes in a tree.
	 */
	public static final String TREE_NODE_LABEL = "name";

	/**
	 * The name of the data group which represents a tree in Prefuse.
	 */
	public static final String TREE = "tree";

	/**
	 * The name of the data group which represents nodes in a tree in Prefuse.
	 */
	public static final String TREE_NODES = "tree.nodes";

	/**
	 * The name of the data group which represents edges in a tree in Prefuse.
	 */
	public static final String TREE_EDGES = "tree.edges";

	/**
	 * The background color of the visualizations.
	 */
	public static final Color BACKGROUND = Color.WHITE;

	/**
	 * The foreground color of the visualizations.
	 */
	public static final Color FOREGROUND = Color.BLACK;
	
	/**
	 * Marker for the boolean value fixed
	 */
	public static final String FIXED = "fixed";
	
	/**
	 * Marker for the user uri 
	 */
	public static final String URI = "name";

	public static final String IMAGE = "image";
}