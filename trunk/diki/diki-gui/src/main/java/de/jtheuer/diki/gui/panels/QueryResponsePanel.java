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
	
package de.jtheuer.diki.gui.panels;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.paceproject.diki.elmo.QueryResponse;

import uk.co.holygoat.tag.concepts.Tagging;
import de.jtheuer.jjcomponents.swing.LayoutFactory;
import de.jtheuer.jjcomponents.swing.panels.JJPanel;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 */
public class QueryResponsePanel extends JJPanel {

	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(QueryResponsePanel.class.getName());
	
	/**
	 * @param t
	 */
	public QueryResponsePanel(QueryResponse qr) {
		setOpaque(false);
		LayoutFactory.setBoxLayoutH(this);
		add(new JLabel(qr.getDistance()+" "));
	
		JPanel results = new JPanel();
		results.setOpaque(false);
		LayoutFactory.setBoxLayoutV(results);
		Iterator<Object> i = qr.getResult().iterator();
		while(i.hasNext()) {
			Object t = i.next();
			if (t instanceof Tagging) {
				results.add(new TagResultPanel((Tagging) t));
			}
		}
		add(results);
	}

}
