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
	
package de.jtheuer.diki.gui.utils;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.InputStream;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import de.jtheuer.jjcomponents.utils.Resource;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 */
public enum DikiResourceContainer implements Resource {
	
	LOADER("loader.gif", "load"),
	DIKI("32x32-diki.png"),
	DIKI_SMALL("16x16-diki.png"),
	DIKI_GIF("24x24-diki.png");

	private final static String PREFIX  = "";
	private final static String x22 = "";
	
	private String resource;
	private String caption;

	private DikiResourceContainer(String param) {
		this(param, "");
	}
	
	private DikiResourceContainer(String param, String caption) {
		this.resource  = param;
		this.caption = caption;
		
		/* fail fast */
		if(getClass().getResourceAsStream(getPath(param)) == null) {
			throw new NullPointerException("Resource "+ param + " does not exist");
		}
	}
	
	/* (non-Javadoc)
	 * @see de.jtheuer.jjcomponents.utils.Resource#getAsIcon()
	 */
	public Icon getAsIcon() {
		return new ImageIcon(getURL());
	}
	
	/* (non-Javadoc)
	 * @see de.jtheuer.jjcomponents.utils.Resource#getAsIcon(int)
	 */
	public Icon getAsIcon(int size) {
		return new ImageIcon(getURL(size));
	}
	
	/* (non-Javadoc)
	 * @see de.jtheuer.jjcomponents.utils.Resource#getAsImage()
	 */
	public Image getAsImage() {
		return Toolkit.getDefaultToolkit().getImage(getURL());
	}
	
	/* (non-Javadoc)
	 * @see de.jtheuer.jjcomponents.utils.Resource#getAsImage(int)
	 */
	public Image getAsImage(int size) {
		return Toolkit.getDefaultToolkit().getImage(getURL(size));
	}
	
	public String getCaption() {
		return caption;
	}
	
	private final String getPath(String item) {
		return PREFIX + x22 + "/" + item ;
	}
	
	/**
	 * @param item for example exit.png
	 * @param resoloution for example 64 for the "64x64" package
	 * @return "/crystalsvg/64x64/exit.png"
	 */
	private String getPath(String item, int resoloution) {
		return PREFIX + resoloution +"x"+ resoloution + "/" + item;
	}

	/* (non-Javadoc)
	 * @see de.jtheuer.jjcomponents.utils.Resource#getStream()
	 */
	public InputStream getStream() {
		return getClass().getResourceAsStream(getPath(resource));
	}

	/* (non-Javadoc)
	 * @see de.jtheuer.jjcomponents.utils.Resource#getURL()
	 */
	public URL getURL() {
		return getClass().getResource(getPath(resource));
	}

	/* (non-Javadoc)
	 * @see de.jtheuer.jjcomponents.utils.Resource#getURL(int)
	 */
	public URL getURL(int size) {
		return getClass().getResource(getPath(resource,size));
	}

	/* (non-Javadoc)
	 * @see de.jtheuer.jjcomponents.utils.Resource#getAsAnimatedIcon(int)
	 */
	@Override
	public Icon getAsAnimatedIcon(int size) {
		return null;
	}
	
	


}
