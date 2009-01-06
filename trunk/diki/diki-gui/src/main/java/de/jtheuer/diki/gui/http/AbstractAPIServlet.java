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
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.*;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 */
public abstract class AbstractAPIServlet extends HttpServlet {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(AbstractAPIServlet.class.getName());
	private String name;
	protected static final String OK = "<html><head><title>OK</title><body>press back if you finished tagging your bookmark</body></html>";

	/**
	 * @param name the name of the api call (for example newBookmark)
	 */
	public AbstractAPIServlet(String name) {
		this.name = name;
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String response = call(req.getParameterMap());
		
		PrintWriter writer = resp.getWriter();
		writer.write(response);
		writer.close();
	}



	/**
	 * The incoming request's parameters
	 * @param parameterMap
	 */
	protected abstract String call(Map parameterMap);

	public String getName() {
		return name;
	}
	
	public static String getParameter(Map map, String key) {
		if(map.containsKey(key)) {
			String[] string = (String[]) map.get(key);
			if(string.length>=1) {
				return string[0];
			}
		}
		
		return null;
	}
	
	
}
