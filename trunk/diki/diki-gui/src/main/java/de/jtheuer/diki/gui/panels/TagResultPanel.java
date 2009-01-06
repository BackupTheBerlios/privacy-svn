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
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.text.DateFormatter;
import javax.xml.datatype.XMLGregorianCalendar;

import org.openrdf.concepts.dc.DcResource;
import org.openrdf.concepts.foaf.Agent;

import uk.co.holygoat.tag.concepts.Tag;
import uk.co.holygoat.tag.concepts.Tagging;
import de.jtheuer.diki.gui.controls.DescriptionLabel;
import de.jtheuer.diki.gui.controls.TagsLabel;
import de.jtheuer.jjcomponents.swing.LayoutFactory;
import de.jtheuer.jjcomponents.swing.components.LinkButton;
import de.jtheuer.sesame.QNameURI;
import de.jtheuer.sesame.SimpleSet;

/**
 * Displays a tag
 */
public class TagResultPanel extends MaxHeightPanel {
	/** */
	private static final long serialVersionUID = 1L;

	/* auto generated Logger */@SuppressWarnings("unused")
	private final static Logger LOGGER = Logger.getLogger(TagResultPanel.class.getName());

	public TagResultPanel(Tagging tagging) {
		LayoutFactory.setBoxLayoutV(this);
		setOpaque(false);

		/* set Title */
		Iterator<Object> iterator = tagging.getTagsTaggedResources().iterator();
		if (iterator.hasNext()) {
			final Object resource = iterator.next();

			if (resource instanceof DcResource) {
				DcResource post = (DcResource) resource;

				/* description */
				String description = post.getDcDescription();

				/* url and title */
				String url = post.getQName().getNamespaceURI() + post.getQName().getLocalPart();
				String title = post.getDcTitle();
				String from = "";
				String nick = null;

				/* creator */
				try {
					from = tagging.getTagsTaggedBy().toString();

					Set<Agent> agents = tagging.getTagsTaggedBy();
					if (agents != null) {
						Agent agent = SimpleSet.first(agents);
						if (agent != null) {
							Set<Object> nicks = agent.getFoafNames();
							nick = SimpleSet.firstString(nicks);
						}
					}
				} catch (Exception e) {
					LOGGER.fine(e.getMessage());
				}
				
				/* date created */
				String date="(unknown)";
				XMLGregorianCalendar xml = SimpleSet.first(tagging.getTagsTaggedOn());
				if(xml != null) {
					GregorianCalendar cal = xml.toGregorianCalendar();
					try {
						date = new DateFormatter().valueToString(cal.getTime());
					} catch (ParseException e1) {
						date = "(unknown)";
						LOGGER.log(Level.FINE,"date parsing exception", e1);
					}
				}
				JLabel label_date = new JLabel("tagged on: " + date + (nick != null ? " by " + nick : "" ));
				label_date.setForeground(Color.GRAY);


				/* tagging id */
				String id = QNameURI.toString(tagging.getQName());

				if (title == null) {
					title = url;
				}
				final Set<Tag> tags = tagging.getTagsAssociatedTags();

				JComponent label_url;
				try {
					label_url = new LinkButton(new URL(url), title, null);
				} catch (MalformedURLException e) {
					label_url = new JLabel(url);
				}
				label_url.setToolTipText("<html>" + url + " <br /> tagged by:" + from + "<br /> id:" + id + "</html>");
				JLabel tagslabel = new TagsLabel(tags);
				JComponent label_description = new DescriptionLabel(description);
				LayoutFactory.left(label_url, label_date, label_description, tagslabel);
				add(label_url);
				add(label_date);
				add(label_description);
				add(tagslabel);
			}

		} else {
			LOGGER.fine("Tagging " + tagging.getQName() + "doesn't have a taggedResource - ignoreing");
		}
	}

}
