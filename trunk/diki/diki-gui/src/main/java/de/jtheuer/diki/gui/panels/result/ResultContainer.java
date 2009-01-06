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
package de.jtheuer.diki.gui.panels.result;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.swing.*;

import org.paceproject.diki.elmo.*;

import uk.co.holygoat.tag.concepts.Tagging;
import de.jtheuer.diki.gui.panels.TagResultPanel;
import de.jtheuer.diki.lib.NetworkConnection;
import de.jtheuer.jjcomponents.swing.panels.JJPanel;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * 
 */
public class ResultContainer extends JJPanel {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(ResultContainer.class.getName());
	private GridBagConstraints distanceContraints;
	private GridBagConstraints resultConstraints;
	private GridBagConstraints rateContraints;
	private NetworkConnection connection;
	private GridBagConstraints gapConstraints;
	private JPanel contentPanel = new JPanel();

	/**
	 * Creates an empty result container.
	 * 
	 * A typical result container as two coloums: | distance | result objects |
	 */
	public ResultContainer(NetworkConnection connection) {
		this.connection = connection;
		this.setLayout(new BorderLayout());
		setBackground(Color.WHITE);

		contentPanel.setLayout(new GridBagLayout());
		contentPanel.setOpaque(false);
		add(contentPanel,BorderLayout.NORTH);
		setupLayout();
	}
	
	public void fireNewResults(Query query) {
		removeAll();
		
		Iterator<QueryResponse> i = query.getHasResults().iterator();
		while(i.hasNext()) {
			addResponse(i.next(), query);
		}
	}

	protected void addResponse(QueryResponse response, Query query) {
		double distance = query.getDistance() - response.getDistance();

		Iterator<Object> it = response.getResult().iterator();
		while (it.hasNext()) {
			/* check type of result */
			Object o = it.next();
			Tagging t;
			if (o instanceof Tagging) {
				t = (Tagging) o;

				final Rateing rating = connection.getRatingManager().getRatingFor(t);

				/* rating */
				final RatePanel ratepanel = new RatePanel();
				ratepanel.setAction(new AbstractAction() {

					@Override
					public void actionPerformed(ActionEvent e) {
						rating.setRateing(ratepanel.getCurrentValue());
					}

				});
				ratepanel.setCurrentValue((int) rating.getRateing());


				contentPanel.add(new JLabel(Double.toString(distance)), distanceContraints);
				contentPanel.add(ratepanel, rateContraints);
				contentPanel.add(new TagResultPanel(t), resultConstraints);
				
				nextRow();
			}

		}
		revalidate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Container#removeAll()
	 * 
	 * Overwritten for faster development. Doesn't harm
	 */
	@Override
	public void removeAll() {
		contentPanel.removeAll();
		setupLayout();
	}

	private void setupLayout() {
		/* general padding between components */
		Insets in = new Insets(5, 5, 10, 5);
		/* column 1 */
		distanceContraints = new GridBagConstraints();
		distanceContraints.anchor = GridBagConstraints.NORTHEAST;
		distanceContraints.insets = in;
		distanceContraints.gridx = 0;
		distanceContraints.gridy = 0;
		distanceContraints.weighty = 0;

		rateContraints = new GridBagConstraints();
		rateContraints.anchor = GridBagConstraints.NORTH;
		rateContraints.insets = in;
		rateContraints.gridx = 1;
		rateContraints.gridy = 0;
		rateContraints.weighty = 0;

		/* column 2 */
		resultConstraints = new GridBagConstraints();
		resultConstraints.fill = GridBagConstraints.HORIZONTAL;
		resultConstraints.insets = in;
		resultConstraints.gridx = 2;
		resultConstraints.gridy = 0;
		resultConstraints.weightx = 1; // get all extra space!
		resultConstraints.weighty = 0;
		
//		/* gap - is filled as last row to span everything */
//		gapConstraints = new GridBagConstraints();
//		gapConstraints.gridx = 3;
//		gapConstraints.weighty = 1;

		contentPanel.add(new JLabel("distance"), distanceContraints);
		contentPanel.add(new JLabel("rating"), rateContraints);
		contentPanel.add(new JLabel("search result"), resultConstraints);
		
		nextRow();
	}
	
	/**
	 * Adds a gap row and switches to the next row
	 */
	private void nextRow() {
		/* next row */
		distanceContraints.gridy++;
		resultConstraints.gridy++;
		rateContraints.gridy++;
//		gapConstraints.gridy++;
//		add(new JLabel(),gapConstraints);
	}
}
