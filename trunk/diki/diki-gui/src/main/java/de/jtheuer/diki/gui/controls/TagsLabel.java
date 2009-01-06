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
package de.jtheuer.diki.gui.controls;

import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import uk.co.holygoat.tag.concepts.Tag;

public class TagsLabel extends JLabel {
	/* auto generated Logger */@SuppressWarnings("unused")
	private final static Logger LOGGER = Logger.getLogger(TagsLabel.class.getName());

	public TagsLabel(Set<Tag> tags) {
		super();
		setTags(tags);
		setHorizontalAlignment(SwingConstants.LEFT);
	}

	private void setTags(Set<Tag> tags) {
		StringBuffer w = new StringBuffer();
		try {
			for (Tag tag : tags) {
				if (w.length() > 0) {
					w.append(" - ");
				}

				/* only add first tagname */
				Iterator<String> names = tag.getTagsNames().iterator();
				if (names.hasNext()) {
					w.append(names.next());
				}
			}
		} catch (Exception e) {
			LOGGER.warning(e.getMessage());
			w.append("!x!");
		}

		setText("<html>tags: <font color=#008800>" + w.toString() + "</font>");
	}
}
