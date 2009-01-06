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

import java.util.*;
import java.util.logging.Logger;

import javax.swing.*;
import javax.xml.namespace.QName;

import org.openrdf.elmo.sesame.SesameManager;

import uk.co.holygoat.tag.concepts.Tag;
import de.jtheuer.diki.gui.controls.SimpleHScrollPane;
import de.jtheuer.diki.gui.controls.SimpleTable;
import de.jtheuer.diki.gui.utils.SimpleTagTableModel;
import de.jtheuer.diki.gui.utils.TagTableModel;
import de.jtheuer.diki.lib.NetworkConnection;
import de.jtheuer.jjcomponents.swing.LayoutFactory;

/**
 * 
 */
public class TagTablePanel extends JPanel {
	/* auto generated Logger */@SuppressWarnings("unused")
	private final static Logger LOGGER = Logger.getLogger(TagTablePanel.class.getName());

	private JTable tags;
	private NetworkConnection connection;
	private SimpleTagTableModel tags_model = new TagTableModel();

	public TagTablePanel(NetworkConnection connection) {
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.connection = connection;

		Iterable<Tag> taglabels = connection.getSesame().getLocalStore().createManager().findAll(Tag.class);

		Vector<String> l = new Vector<String>();
		for (Tag tag : taglabels) {
			for (String string : tag.getTagsNames()) {
				l.add(string);
			}
		}

		tags = new SimpleTable(tags_model, l);
		add(new JLabel("tags for this url"));
		add(new SimpleHScrollPane(tags));
		LayoutFactory.leftAll(this);
	}

	public void setTags(List<String> tags) {
		tags_model.setTags(tags);
	}

	public Set<Tag> getTags() {
		SesameManager manager = connection.getSesame().getLocalStore().createManager();

		Set<Tag> resultset = new HashSet<Tag>();
		for (String string : tags_model.getTags()) {
			if (string != null) {
				QName q = connection.getNamespaceFactory().constructNamespace("tag/", string).toQName();
				Tag tag = manager.designate(Tag.class, q);

				/* add tag name(s) */
				Set<String> names = tag.getTagsNames();
				names.add(string);
				tag.setTagsNames(names);

				resultset.add(tag);
			}
		}

		return resultset;
	}
}
