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
import java.io.*;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 * {@link LocationAwareProperties} can be saved to the file from which they have been loaded. This only works if the {@link File} was
 * loaded by the constructor!
 */
public class LocationAwareProperties extends Properties {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(LocationAwareProperties.class.getName());
	private File file;

	/**
	 * Initializes a new Properties instance. Note, that you'll never be able to run the {@link #save()} method!
	 */
	public LocationAwareProperties() {}
	
	/**
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * 
	 */
	public LocationAwareProperties(File filelocation) throws FileNotFoundException, IOException {
		load(new FileInputStream(filelocation));
		this.file = filelocation;
	}

	/**
	 * @param defaults
	 */
	public LocationAwareProperties(Properties defaults) {
		super(defaults);
	}
	
	/**
	 * saves the properties to the file that has been supplied with the constructor. Throws a {@link NullPointerException} if the properties have
	 * been loaded with another command.
	 * 
	 * @throws IOException
	 */
	public void save() throws IOException {
		FileWriter fw = new FileWriter(file);
		store(fw,"saved on " + System.currentTimeMillis());
	}

}
