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

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.text.JTextComponent;

import com.jidesoft.hints.FileIntelliHints;

import de.jtheuer.jjcomponents.swing.LayoutFactory;
import de.jtheuer.jjcomponents.utils.FaviconLoader;

/**
 * 
 */
public class URLPanel extends JPanel { //implements DocumentListener {

	private static final long serialVersionUID = 5096910568201103333L;

	private JLabel icon;

	private boolean is_icon_loading;

	private JTextField url;

	public URLPanel(String initial_url) {
		LayoutFactory.setBoxLayoutH(this);
		setBackground(Color.WHITE);

		icon = new JLabel();
		Dimension iconsize = new Dimension(22,22);
		/* show at least a 16x16 icon */
		icon.setMinimumSize(iconsize);
		icon.setPreferredSize(iconsize);
		icon.setBorder(BorderFactory.createLineBorder(Color.WHITE));

		url = new JTextField(initial_url);
		/* put border to Panel */
		Border border = url.getBorder();
		setBorder(border);
		url.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		//url.getDocument().addDocumentListener(this);
		
		//assign auto-complete
		new FileIntelliHints(url);

		add(icon);
		add(url);
		
		new FaviconLoader(url,icon);
	}
	
	@Override
	public Dimension getMaximumSize() {
		return new Dimension(Integer.MAX_VALUE,getPreferredSize().height);
	}
	
	public void setText(String text) {
		url.setText(text);
	}
	
	public String getText() {
		return url.getText();
	}
	
	public JTextComponent getTextComponent() {
		return url;
	}

}
