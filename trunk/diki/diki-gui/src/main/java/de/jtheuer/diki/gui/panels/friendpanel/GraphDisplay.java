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

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.SwingConstants;

import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.assignment.ColorAction;
import prefuse.action.filter.GraphDistanceFilter;
import prefuse.action.layout.Layout;
import prefuse.action.layout.graph.ForceDirectedLayout;
import prefuse.controls.*;
import prefuse.data.*;
import prefuse.data.event.TupleSetListener;
import prefuse.data.tuple.TupleSet;
import prefuse.render.*;
import prefuse.util.*;
import prefuse.util.display.DisplayLib;
import prefuse.util.force.ForceSimulator;
import prefuse.util.ui.JFastLabel;
import prefuse.util.ui.JSearchPanel;
import prefuse.visual.VisualGraph;
import prefuse.visual.VisualItem;
import prefuse.visual.expression.InGroupPredicate;
import de.jtheuer.jjcomponents.geom.JRectangle2D;

/**
 * This class creates a display for a graph. <p/> Project OWL2Prefuse <br/>
 * SimpleGraphjava created 3 januari 2007, 11:17 <p/> Copyright &copy 2006
 * Jethro Borsje
 * 
 * @author Jan Torben Heuer
 * @author <a href="mailto:info@jborsje.nl">Jethro Borsje</a>
 * 
 */
public class GraphDisplay extends Display {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(GraphDisplay.class.getName());

	/** default id */
	private static final long serialVersionUID = 1L;

	/**
	 * Create data description of labels, setting colors, and fonts ahead of
	 * time
	 */
	private static final Schema DECORATOR_SCHEMA = PrefuseLib.getVisualItemSchema();
	static {
		DECORATOR_SCHEMA.setDefault(VisualItem.INTERACTIVE, false);
		DECORATOR_SCHEMA.setDefault(VisualItem.TEXTCOLOR, ColorLib.gray(0));
		DECORATOR_SCHEMA.setDefault(VisualItem.FONT, FontLib.getFont("Tahoma", 10));
	}

	/**
	 * The searchpanel, used for the keyword search in the graph.
	 */
	private JSearchPanel m_search;

	/**
	 * The label which displays the URI of the node under the mouse.
	 */
	private JFastLabel m_URILabel;

	/**
	 * The GraphDistanceFilter, which makes sure that only the nodes with a
	 * certain number of hops away from the currently selected node, are
	 * displayed.
	 */
	private GraphDistanceFilter m_filter;

	/**
	 * The focus control of the graph.
	 */
	private FocusControl m_focusControl;

	/**
	 * The force directed layout.
	 */
	private Layout m_layout;

	/**
	 * The force simulator of the force directed layout.
	 */
	private ForceSimulator m_fsim;

	private Image[] images;

	/**
	 * Creates a new instance of GraphDisplay
	 * 
	 * @param p_graph
	 *            The Prefuse Graph to be displayed.
	 * @param p_distancefilter
	 *            A boolean, indicating whether or not a GraphDistance filter
	 *            should be used with this display.
	 */
	public GraphDisplay(Graph p_graph, boolean p_distancefilter) {
		this(p_graph, p_distancefilter,new Image[0]);
	}

	/**
	 * Creates a new instance of GraphDisplay
	 * 
	 * @param p_graph
	 * @param p_distancefilter
	 * @param images
	 *            a list of images that will be added to the Renderers. You can
	 *            finally use the images toString as image key
	 */
	public GraphDisplay(Graph p_graph, boolean p_distancefilter, Image... images) {
		// Create a new Display with an empty visualization.
		super(new Visualization());
		
		this.images = images;
		
		setHighQuality(true);
		initVisualization(p_graph, p_distancefilter);
		initDisplay();

		// Create the search panel.
		// createSearchPanel();

		// Create the title label.
		createTitleLabel();

		m_vis.run("draw");

		/* get total bounds and buffer them */
		Rectangle2D bounds = m_vis.getBounds(Visualization.ALL_ITEMS);
		JRectangle2D buffered = new JRectangle2D(bounds);
		buffered.buffer(100);

		DisplayLib.fitViewToBounds(this, buffered, 0);
	}

	/**
	 * Add the graph to the visualization as the data group "graph" nodes and
	 * edges are accessible as "graph.nodes" and "graph.edges". A renderer is
	 * created to render the edges and the nodes in the graph.
	 * 
	 * @param p_graph
	 *            The Prefuse Graph to be displayed.
	 * @param p_distancefilter
	 *            A boolean, indicating whether or not a GraphDistance filter
	 *            should be used with this display.
	 */
	private void initVisualization(Graph p_graph, boolean p_distancefilter) {
		// Add the graph to the visualization.
		VisualGraph vg = m_vis.addGraph(Constants.GRAPH, p_graph);

		// Set up a label renderer for the labels on the nodes and add it to the
		// visualization.
		LabelRenderer nodeRenderer = new LabelImageRenderer(Constants.URI, Constants.IMAGE);
		if (images != null) {
			for (Image image : images) {
				nodeRenderer.getImageFactory().addImage(image.toString(), image);
			}
		}
		nodeRenderer.setRoundedCorner(8, 8);

		DefaultRendererFactory drf = new DefaultRendererFactory();
		drf.add(new InGroupPredicate(Constants.GRAPH_NODES), nodeRenderer);
		drf.add(new InGroupPredicate(Constants.EDGE_DECORATORS), new LabelRenderer("label"));
		drf.add(new InGroupPredicate(Constants.GRAPH_EDGES), new EdgeRenderer(prefuse.Constants.EDGE_TYPE_LINE, prefuse.Constants.EDGE_ARROW_FORWARD));
		m_vis.setRendererFactory(drf);

		// Add the decorator for the labels.
		m_vis.addDecorators(Constants.EDGE_DECORATORS, Constants.GRAPH_EDGES, DECORATOR_SCHEMA);

		// Set the interactive value of the edges to false.
		m_vis.setValue(Constants.GRAPH_EDGES, null, VisualItem.INTERACTIVE, Boolean.FALSE);

		for (int i = 0; i < vg.getNodeCount(); i++) {
			/* check if FIXED is set */
			if (vg.getNode(i) instanceof VisualItem) {
				VisualItem vitem = (VisualItem) vg.getNode(i);
				if (Constants.FIXED.equals(vg.getNode(i).getString(Constants.FIXED))) {
					vitem.setFixed(true);
					LOGGER.fine("Setting " + vitem.getString(Constants.URI) + " set to fixed");
				}
			}
		}

		// Get the first node and give it focus, this triggers the distance
		// filter
		// to at least show all nodes with a maximum of 4 hops away from this
		// one.
		if (p_distancefilter) {
			VisualItem f = (VisualItem) vg.getNode(0);
			m_vis.getGroup(Visualization.FOCUS_ITEMS).setTuple(f);

			// The position of the first node is not fixed.
//			f.setFixed(false);
		}

		// Create a focus listener which fixex the position of the selected
		// nodes.
		TupleSet focusGroup = m_vis.getGroup(Visualization.FOCUS_ITEMS);
		focusGroup.addTupleSetListener(getFocusedItemsListner());

		// Finally, we register our ActionList with the Visualization.
		// We can later execute our Actions by invoking a method on our
		// Visualization,
		// using the name we have chosen below.
		m_vis.putAction("draw", getDrawActions(p_distancefilter));
		m_vis.putAction("layout", getLayoutActions());
		m_vis.runAfter("draw", "layout");
	}

	/**
	 * Initialize this display. This method adds several control listeners, sets
	 * the size and sets the foreground and background colors.
	 */
	private void initDisplay() {
		setSize(500, 500);// Set the display size.
		setForeground(Constants.FOREGROUND);// Set the foreground color.
		setBackground(Constants.BACKGROUND);	// Set the background color.
		addControlListener(new DragControl());// Drag items around.
		addControlListener(new PanControl());// Pan with background left-drag.
		addControlListener(new ZoomControl());// Zoom with vertical right-drag.
		addControlListener(new WheelZoomControl());// Zoom using the scroll wheel.
		addControlListener(new ZoomToFitControl());	// Double click for zooming to fit the graph.
		addControlListener(new NeighborHighlightControl());// Highlight the neighbours.
		m_focusControl = new FocusControl(1);// Conrol which nodes are in focus.
		addControlListener(m_focusControl);

	}

	/**
	 * Get the focus control of the graph, so it can be adjusted or even removed
	 * to implement a custom made focus control.
	 * 
	 * @return The focus control of the graph.
	 */
	public FocusControl getFocusControl() {
		return m_focusControl;
	}

	/**
	 * This metodh creates a TupleSetListener which listens for changes in a
	 * tuple set.
	 * 
	 * @return A TupleSetListener.
	 */
	private TupleSetListener getFocusedItemsListner() {
		TupleSetListener listner = new TupleSetListener() {
			public void tupleSetChanged(TupleSet ts, Tuple[] add, Tuple[] rem) {
				// Set the fixed attribute for the nodes that are no longer in
				// focus
				// to false.
				for (int i = 0; i < rem.length; ++i)
					((VisualItem) rem[i]).setFixed(false);

				// Set the fixed attribute for the nodes that are added to the
				// focus
				// to true.
				for (int i = 0; i < add.length; ++i) {
					((VisualItem) add[i]).setFixed(false);
					((VisualItem) add[i]).setFixed(true);
				}

				// If there are no nodes in focus, get the first one that is to
				// be removed, add it to the tuple set en set it' fixed
				// attribute
				// to false. Thereby intializing the tupleset with one node.
				if (ts.getTupleCount() == 0) {
					ts.addTuple(rem[0]);
					((VisualItem) rem[0]).setFixed(false);
				}

				// Run the draw action.
				m_vis.run("draw");
			}
		};

		return listner;
	}

	/**
	 * Returns the actionlist for the colors of our graph.
	 * 
	 * @param p_distancefilter
	 *            A boolean, indicating whether or not a GraphDistance filter
	 *            should be used with this display.
	 * @return The actionlist containing the actions for the colors.
	 */
	private ActionList getDrawActions(boolean p_distancefilter) {
		// Create an action list containing all color assignments.
		ActionList draw = new ActionList();

		// Create the graph distance filter, if wanted.
		if (p_distancefilter) {
			m_filter = new GraphDistanceFilter(Constants.GRAPH, 4);
			draw.add(m_filter);
		}

		// Use black for the text on the nodes.
		draw.add(new ColorAction(Constants.GRAPH_NODES, VisualItem.TEXTCOLOR, ColorLib.rgb(0, 0, 0)));
		draw.add(new ColorAction(Constants.GRAPH_NODES, VisualItem.STROKECOLOR, ColorLib.gray(0)));

		// Use light grey for edges.
		draw.add(new ColorAction(Constants.GRAPH_EDGES, VisualItem.STROKECOLOR, ColorLib.gray(200)));
		draw.add(new ColorAction(Constants.GRAPH_EDGES, VisualItem.FILLCOLOR, ColorLib.gray(200)));

		return draw;
	}

	/**
	 * This method sets the parameter of the force directed layout that
	 * determines the distance between the nodes (e.g. the length of the edges).
	 * 
	 * @param p_value
	 *            The new value of the force directed layout.
	 */
	private void setForceDirectedParameter(float p_value) {
		m_fsim.getForces()[0].setParameter(0, p_value);
	}

	/**
	 * Returns the actionlist for the layout of our graph.
	 * 
	 * @return The actionlist containing the layout of the graph.
	 */
	private ActionList getLayoutActions() {
		// Make sure the nodes to not get to close together.
		m_layout = new ForceDirectedLayout(Constants.GRAPH);
		m_fsim = ((ForceDirectedLayout) m_layout).getForceSimulator();
		m_fsim.getForces()[0].setParameter(0, -10f);

		// m_layout = new CircleLayout(Constants.GRAPH);
		// Create an action list with an animated layout, the INFINITY parameter
		// tells the action list to run indefinitely.
		ActionList layout = new ActionList(Integer.MAX_VALUE);

		// Add the force directed layout.
		layout.add(m_layout);

		layout.add(new HideDecoratorAction(m_vis));

		// Add the repaint action.
		layout.add(new RepaintAction());

		// Create te action which takes care of the coloring of the nodes which
		// are under the mouse.
		layout.add(new NodeColorAction(Constants.GRAPH_NODES, m_vis));

		// Add the LabelLayout for the labels of the edges.
		layout.add(new LabelLayout(Constants.EDGE_DECORATORS, m_vis));

		return layout;
	}

	/**
	 * This method creates the title label.
	 */
	private void createTitleLabel() {
		// Create a label for the title of the nodes.
		m_URILabel = new JFastLabel("                 ");
		m_URILabel.setPreferredSize(new Dimension(350, 20));
		m_URILabel.setVerticalAlignment(SwingConstants.BOTTOM);
		m_URILabel.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));
		m_URILabel.setFont(FontLib.getFont("Verdana", Font.PLAIN, 12));
		m_URILabel.setBackground(Constants.BACKGROUND);
		m_URILabel.setForeground(Constants.FOREGROUND);

		// The control listener for changing the title of a node at a mouseover
		// event.
		this.addControlListener(new ControlAdapter() {
			@Override
			public void itemEntered(VisualItem item, MouseEvent e) {
				if (item.canGetString("URI"))
					m_URILabel.setText(item.getString("URI"));
			}

			@Override
			public void itemExited(VisualItem item, MouseEvent e) {
				m_URILabel.setText(null);
			}
		});
	}
	
}