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
package de.jtheuer.diki.gui.http;

import java.util.Map;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import de.jtheuer.diki.gui.panels.tagpanel.DragPanel;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * 
 */
public class NewBookmarkServlet extends AbstractAPIServlet {

	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(NewBookmarkServlet.class.getName());
	private static final String URL = "url";
	private static final String TITLE = "title";
	private static final String DESCRIPTION = "description";
	private DragPanel dragpanel;

	/**
	 * @param name
	 */
	public NewBookmarkServlet(DragPanel dragpanel) {
		super("newBookmark");
		this.dragpanel = dragpanel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jtheuer.diki.gui.http.AbstractAPIServlet#call(java.util.Map)
	 */
	@Override
	protected String call(Map parameterMap) {
		final String urlstring = getParameter(parameterMap, URL);
		final String title = getParameter(parameterMap, TITLE);
		final String description= getParameter(parameterMap, DESCRIPTION);
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				dragpanel.newBookmark(urlstring,title,description);
			}
			
		});
		return OK;
	}

}
