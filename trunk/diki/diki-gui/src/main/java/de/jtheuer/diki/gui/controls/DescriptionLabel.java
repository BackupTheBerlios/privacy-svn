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
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JTextArea;

public class DescriptionLabel extends JTextArea {

	public DescriptionLabel(String text) {
		super();
		setWrapStyleWord(true);
		setLineWrap(true);
		setEditable(false);
		setBorder(BorderFactory.createEmptyBorder());
		setOpaque(false);
		
		/* triggers invalidate. Last command */
		setColumns(30);
		
		/* set minimum one line and maximum 300chars. we otherwise break at 250chars */
		if(text == null || text.length() == 0) {
			//setText("\n");
		} else if(text.length() < 300) {
			setText(text);
		} else {
			setText(text.substring(0, 250) + "[...]");
		}
	}
	
	public Dimension getMaximumSize() {
		return new Dimension(getPreferredSize().width,super.getMaximumSize().height);
	}
}
