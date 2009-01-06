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
package de.jtheuer.jjcomponents.utils;

import java.awt.Image;
import java.io.InputStream;
import java.net.URL;

import javax.swing.Icon;

import de.jtheuer.jjcomponents.swing.components.AnimatedIcon;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 */
public interface Resource {

	public abstract InputStream getStream();

	/**
	 * @return the URL of the resource
	 */
	public abstract URL getURL();

	/**
	 * @return the URL of the resource
	 * @param size The image size. Supported values are 16, 22, 32, 64 
	 */
	public abstract URL getURL(int size);
	
	/**
	 * This method may fail if the resource is not an icon
	 * @return the Resource as Icon
	 */
	public abstract Icon getAsIcon();

	/**
	 * This method may fail if the resource is not an icon
	 * @param size The image size. Supported values are 16, 22, 32, 64 
	 * @return the Resource as Icon
	 */
	public abstract Icon getAsIcon(int size);
	
	/**
	 * Returns the image as an animated icon ({@link AnimatedIcon}). 
	 * @param size The image size. Supported values are 16, 22, 32, 64
	 * @return the Resource as Icon
	 */
	public abstract Icon getAsAnimatedIcon(int size);
	
	/**
	 * This method may fail if the resource is not an image
	 * @return the Resource as Image
	 */
	public abstract Image getAsImage();

	/**
	 * This method may fail if the resource is not an image
	 * @param size The image size. Supported values are 16, 22, 32, 64 
	 * @return the Resource as Image
	 */
	public abstract Image getAsImage(int size);

	/**
	 * @return the caption for the Resource
	 */
	public abstract String getCaption();

	
}