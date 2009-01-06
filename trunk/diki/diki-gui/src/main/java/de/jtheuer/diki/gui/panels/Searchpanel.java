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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.*;

import org.paceproject.diki.elmo.Query;

import de.jtheuer.diki.gui.Messages;
import de.jtheuer.diki.gui.controls.SimpleHScrollPane;
import de.jtheuer.diki.gui.panels.result.ResultContainer;
import de.jtheuer.diki.lib.NetworkConnection;
import de.jtheuer.diki.lib.query.SparqlTagQuery;
import de.jtheuer.diki.lib.query.SwingQueryResult;
import de.jtheuer.jjcomponents.swing.LayoutFactory;
import de.jtheuer.jjcomponents.swing.panels.ButtonPanel;
import de.jtheuer.jjcomponents.swing.panels.FixedSizePanel;
import de.jtheuer.jjcomponents.utils.ResourcesContainer;

/**
 * 
 */
public class Searchpanel extends JPanel {
	private static final long serialVersionUID = 6496446025834155662L;

	private JTextField searchbox;

	private ResultContainer results;
	/* where the actual results will be stored in */
	private JPanel results_parent=new JPanel();

	private javax.swing.Action cancelaction;

	private AbstractAction searchaction;

	private NetworkConnection networkConnection;

	private JProgressBar progress = new JProgressBar(0, 100);

	/**
	 * A Searchpanel is the main interface to the user!
	 */
	public Searchpanel(NetworkConnection connection) {
		networkConnection = connection;
		LayoutFactory.setBoxLayoutV(this);
		LayoutFactory.setEmptyBorder(this);

		add(createSearchline());
		add(LayoutFactory.getButtonGapV());
		add(progress);
		add(LayoutFactory.getButtonGapV());
		results_parent.setBackground(Color.WHITE);
		add(new SimpleHScrollPane(results_parent));
		// add(Box.createHorizontalGlue());
	}

	public JPanel createSearchline() {
		searchaction = new AbstractAction(Messages.getString("Searchpanel.okbutton"), ResourcesContainer.START.getAsIcon()) { //$NON-NLS-1$
			private static final long serialVersionUID = 3634926703919846108L;

			public void actionPerformed(ActionEvent e) {
				search();
			}
		};

		cancelaction = new AbstractAction("cancel", ResourcesContainer.CANCEL.getAsIcon()) {
			private static final long serialVersionUID = 3634926703919846107L;

			public void actionPerformed(ActionEvent e) {
				cancel();
			}
		};

		/* initialize searchbutton state */
		setIsSearching(false);

		searchbox = new JTextField(25);
		searchbox.setAction(searchaction);

		JPanel buttons = new ButtonPanel(LayoutFactory.LINE_AXIS, FixedSizePanel.FIXED_BOTH, ButtonPanel.NONE, searchaction, cancelaction);

		JPanel searchline = new FixedSizePanel(FixedSizePanel.FIXED_HEIGHT, BoxLayout.LINE_AXIS);
		searchline.add(searchbox);
		searchline.add(LayoutFactory.getButtonGapH());
		searchline.add(buttons);
		return searchline;
	}

	/**
	 * changes the button-state of search- and cancel-button.
	 * 
	 * @param b
	 *            if there is a search running, now
	 */
	protected void setIsSearching(boolean b) {
		searchaction.setEnabled(!b);
		cancelaction.setEnabled(b);
		progress.setEnabled(b);
	}

	protected boolean isSearching() {
		return cancelaction.isEnabled();
	}

	protected void search() {
		setIsSearching(true);
		results_parent.removeAll();		
		SwingQueryResult r = new SwingQueryResult() {

			@Override
			public void fireResultFinished() {
				setIsSearching(false);
			}

			@Override
			protected void saveFireNewResult() {
				results.fireNewResults(getQuery());
			}

			@Override
			public void updatePercentage(int percentage) {
				progress.setValue(percentage);
			}
			
		};
		
		Query query = networkConnection.runQuery(new SparqlTagQuery(searchbox.getText()),r);
		results = new ResultContainer(networkConnection);
		results_parent.add(results);
	}
	
	protected void cancel() {
		setIsSearching(false);
	}

	@Override
	/** always returns the width as the preferred height! */
	public Dimension getPreferredSize() {
		return new Dimension(super.getPreferredSize().width, super.getPreferredSize().width);
	}


}
