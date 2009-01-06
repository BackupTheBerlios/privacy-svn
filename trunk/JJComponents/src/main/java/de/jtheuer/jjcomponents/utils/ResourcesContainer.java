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
package de.jtheuer.jjcomponents.utils;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.InputStream;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import de.jtheuer.jjcomponents.swing.components.AnimatedIcon;

/**
 * 
 */
public enum ResourcesContainer implements Resource {
	
	ADDFRIEND("actions/add_user.png", "add friend"),
	CANCEL("actions/button_cancel.png", "cancel"),
	CLOSE("actions/fileclose.png","close"),
	CONFIGURE("actions/configure.png", "configure"),
	COPY("actions/editcopy.png", "copy"),
	CUT("actions/editcut.png", "cut"),
	DELETE("actions/editdelete.png", "delete"),
	DRAGDROP("actions/compfile.png", "drag'n'drop"),
	EDIT("actions/edit.png", "edit"),
	EMPTY("mimetypes/empty.png"),
	ERROR("actions/messagebox_critical.png","error"),
	//TAG("actions/rss_tag.png"),
	EXIT("actions/exit.png", "exit"),
	FIND("actions/find.png", "find"),
	FORWARD("actions/2rightarrow.png", "forward"),
	INFO("actions/info.png", "info"),
	LOOKNFEEL("apps/looknfeel.png","themes"),
	MODULES("filesystems/blockdevice.png", "modules"),
	NEW("actions/filenew.png", "new"),
	//ARROW_DOWN("kdevelop_down.png", "down"),
	OK("actions/button_ok.png", "Ok"),
	PASTE("actions/editpaste.png", "paste"),
	PGP("apps/gpg.png","pgp key"),
	RESTART("actions/quick_restart.png","restart"),
	SAVE("actions/filesave.png", "save"),
	START("actions/redo.png","start"),
	WARN("actions/messagebox_warning.png","warning");

	private final static String PREFIX  = "/crystal_project";
	private final static String x22 = "/16x16";
	
	private String caption;
	private String resource;

	private ResourcesContainer(String param) {
		this(param, "");
	}
	
	private ResourcesContainer(String param, String caption) {
		this.resource  = param;
		this.caption = caption;
		
		/* fail fast */
		if(getClass().getResourceAsStream(getPath(param)) == null) {
			throw new NullPointerException("Resource "+ getPath(param) + " does not exist");
		}
	}
	
	/* (non-Javadoc)
	 * @see de.jtheuer.jjcomponents.utils.Resource#getAsAnimatedIcon(int)
	 */
	@Override
	public Icon getAsAnimatedIcon(int size) {
		return new AnimatedIcon(getURL());
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
		return PREFIX + "/" + resoloution +"x"+ resoloution + "/" + item;
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
		URL url = getClass().getResource(getPath(resource,size));
		if(url==null) {
			throw new NullPointerException(getPath(resource,size));
		}
		return url;
	}	

}
