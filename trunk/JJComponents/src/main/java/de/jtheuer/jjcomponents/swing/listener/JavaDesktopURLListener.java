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
package de.jtheuer.jjcomponents.swing.listener;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.logging.*;

import javax.naming.OperationNotSupportedException;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * Calls the Platform default URL Handler.
 * @see Desktop#browse(URI)
 */
public class JavaDesktopURLListener extends AbstractURLListener {
	private static final long serialVersionUID = 7483099040203503727L;

	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(JavaDesktopURLListener.class.getName());

	/**
	 * Creates a new link listener that opens links in the desktop default browser.
	 * @throws OperationNotSupportedException if this is not supported by the current platform
	 */
	public JavaDesktopURLListener() throws OperationNotSupportedException {
		if(! Desktop.isDesktopSupported()) {
			throw new OperationNotSupportedException("Sorry, but the current platform is not supported by Java");
		}
	}
	
	/* (non-Javadoc)
	 * @see de.jtheuer.jjcomponents.swing.listener.AbstractURLListener#call(java.net.URI)
	 */
	@Override
	protected void call(URI link) throws IOException {
		Desktop.getDesktop().browse(link);
	}
}
