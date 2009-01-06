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
package de.jtheuer.diki.gui.utils;

import java.awt.datatransfer.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.logging.Logger;

import javax.swing.TransferHandler;

/**
 * 
 */
public class BookmarkTransferHandler extends TransferHandler {
	private final static Logger LOGGER = Logger
			.getLogger(BookmarkTransferHandler.class.getName());

	DataFlavor stringFlavor;

	private DataFlavor fileFlavor;

	private BookmarkTransferComponent calling_component;

	/**
	 * 
	 */
	public BookmarkTransferHandler(BookmarkTransferComponent component) {
		super();
		calling_component = component;
		stringFlavor = DataFlavor.stringFlavor;
		fileFlavor = DataFlavor.javaFileListFlavor;
	}

	@Override
	public boolean canImport(TransferSupport support) {
		DataFlavor[] flavors = support.getDataFlavors();
		if (hasFileFlavor(flavors)) {
			return true;
		}
		if (hasStringFlavor(flavors)) {
			return true;
		}
		return false;
	}

	private boolean hasFileFlavor(DataFlavor[] flavors) {
		for (int i = 0; i < flavors.length; i++) {
			if (fileFlavor.equals(flavors[i])) {
				return true;
			}
		}
		return false;
	}

	private boolean hasStringFlavor(DataFlavor[] flavors) {
		for (int i = 0; i < flavors.length; i++) {
			if (stringFlavor.equals(flavors[i])) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean importData(TransferSupport support) {
			return importData(support.getTransferable());
	}
	
	public boolean importData(Transferable trans) {
		
		try {
			List<URL> urllist;
			if (hasFileFlavor(trans.getTransferDataFlavors())) {
				List<File> filelist = (List<File>) trans.getTransferData(fileFlavor);
				urllist = new ArrayList<URL>(filelist.size());
				for (File file : filelist) {
					urllist.add(file.toURI().toURL());
				}
			} else if (hasStringFlavor(trans.getTransferDataFlavors())) {
				String data = (String) trans.getTransferData(stringFlavor);
				urllist = textURIListToFileList(data);
			} else {
				/* content-type not allowed */
				return false;
			}
			/* check if content is valid */
			if(urllist != null) {
				calling_component.newBookmark(urllist);			
				/* successful imported */
				return true;
			} else {
				return false;
			}
		} catch (UnsupportedFlavorException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
    private static List<URL> textURIListToFileList(String data) {
        List<URL> list = new java.util.ArrayList<URL>(1);
        for (StringTokenizer st = new StringTokenizer(data, "\r\n");
                st.hasMoreTokens();) {
            String s = st.nextToken();
            if (s.startsWith("#")) {
                // the line is a comment (as per the RFC 2483)
                continue;
            }
            try {
                java.net.URI uri = new java.net.URI(s);
                list.add(uri.toURL());
                return list;
            } catch (java.net.URISyntaxException e) {
            } catch (IllegalArgumentException e) {
            } catch (MalformedURLException e) {
			}
            Log.info("<html>\"<b>"+s+"</b>\"<br /><br />is not a valid url</html>");
        }
        return null;
    }
}
