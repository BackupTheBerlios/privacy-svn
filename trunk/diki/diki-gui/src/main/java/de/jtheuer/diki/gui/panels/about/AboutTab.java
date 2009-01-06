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
package de.jtheuer.diki.gui.panels.about;

import java.awt.Dimension;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.OperationNotSupportedException;
import javax.swing.*;

import org.apache.commons.io.IOUtils;

import com.jidesoft.swing.JideScrollPane;

import de.jtheuer.diki.gui.utils.DikiResourceContainer;
import de.jtheuer.jjcomponents.swing.LayoutFactory;
import de.jtheuer.jjcomponents.swing.listener.JavaDesktopURLListener;
import de.jtheuer.jjcomponents.swing.panels.JJActivateableTab;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * 
 */
public class AboutTab extends JJActivateableTab {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5957229386013919997L;
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(AboutTab.class.getName());
	private static final int BORDER = 10;

	@Override
	public void lazyInitialize() {
		LayoutFactory.setBoxLayoutV(this);
		
		JEditorPane about = new JEditorPane("text/html",getAbout());
		try {
			about.addHyperlinkListener(new JavaDesktopURLListener());
		} catch (OperationNotSupportedException e) {
			LOGGER.log(Level.INFO,"opening hyperlinks is not supported!", e);
		}
		about.setEditable(false);
		about.setBorder(BorderFactory.createEmptyBorder(BORDER, BORDER, BORDER, BORDER));
		about.setPreferredSize(new Dimension(400,400));
		add(new JideScrollPane(about,JideScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JideScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
	}

	@Override
	public Icon getIcon() {
		return DikiResourceContainer.DIKI_SMALL.getAsIcon();
	}

	@Override
	public String getName() {
		return "about";
	}

	private String getAbout() {
		InputStream stream = getClass().getResourceAsStream("/readme.html");
		StringWriter about = new StringWriter();
		try {
			IOUtils.copy(stream, about);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return about.toString();
	}

}
