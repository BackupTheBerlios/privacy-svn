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

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.concurrent.Semaphore;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.JTextComponent;

import nl.ikarus.nxt.priv.imageio.icoreader.lib.ICOReaderSpi;
import nl.ikarus.nxt.priv.imageio.icoreader.obj.ICOFile;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * 
 */
public class FaviconLoader implements DocumentListener {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(FaviconLoader.class.getName());
	private static final String FAVICON = "/favicon.ico";
	
	private static final int HAS_URL=2;
	private static final int IS_EMTPY=1;
	/* private static final int LOCKED=0; */
	
	private JTextComponent text;
	private JLabel icon;
	private URL lastFaviconURL;
	private String sync;
	private Semaphore lock = new Semaphore(2);
	
	static {
		/* register ICO Reader for ImageIO */
		ICOReaderSpi.registerIcoReader();
	}

	public FaviconLoader(final JTextComponent text, final JLabel icon) {
		this.text = text;
		this.icon = icon;
		
		text.getDocument().addDocumentListener(this);
		lock.acquireUninterruptibly(IS_EMTPY);
		
		new Thread() {

			@Override
			public void run() {
//				setDaemon(true);
				setName(text.getName() + " favicon loader");
				String new_string;
				String old_string="";
				while (true) {

					lock.acquireUninterruptibly(HAS_URL);
					new_string = sync;
					lock.release(IS_EMTPY);
					
					/* check if strings have changed */
					if(!old_string.equals(new_string)) {
						updateIcon(new_string);
					}
					old_string = new_string;
				}
			}

		}.start();
		
		/* inital event */
		insertUpdate(null);
	}
	
	/**
	 * Updates the icon depending on the supplied string. It can be a local file or a remote URL.
	 * @param url_string
	 */
	protected void updateIcon(String url_string) {
		Icon tmp_icon=null;
		try {
			File file = new File(url_string);
			if (file.exists()) {
				FileSystemView view = FileSystemView.getFileSystemView();
				tmp_icon = view.getSystemIcon(file);
			} else {
				URL url = getFavicon(url_string);
				
				/* if there is an url and it is not same one as before ... */
				if(url != null && ! url.equals(lastFaviconURL)) {
					tmp_icon = readIconFromURL(url);
					lastFaviconURL = url;
				}
			}
		} catch (MalformedURLException e) {
			LOGGER.info(e.getMessage());
		} catch (IOException e) {
			LOGGER.info(e.getMessage());
		}

		/* set icon if there is any... */
		if(tmp_icon != null) {
			icon.setIcon(tmp_icon);
		} else {
			icon.setIcon(ResourcesContainer.EMPTY.getAsIcon(16));
		}
	}
	
	URL getFavicon(String url_string) throws MalformedURLException {
		URL url = new URL(url_string);
		if(url.getHost().length() > 0) {
			return new URL(url.getProtocol() + "://" + url.getHost() + FAVICON);
		}
		return null;
	}
	
	/**
	 * Reads a (favicon) URL and returns the ico file. There is a read timeout of 2 seconds!
	 * @param imageurl
	 * @return the icon as ImageIcon or null if it is not an icon
	 * @throws IOException 
	 */
	public static ImageIcon readIconFromURL(URL imageurl) throws IOException {
		URLConnection conn = imageurl.openConnection();
		conn.setReadTimeout(2000); // 2 seconds
		ImageInputStream in = ImageIO.createImageInputStream(conn.getInputStream());
		ICOFile ico = new ICOFile(in);
		if(ico.getEntryCount() > 0) {
			return new ImageIcon(ico.getEntry(0).getBitmap().getImage());
		} else {
			return null;
		}
	}
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
	 */
	@Override
	public void changedUpdate(DocumentEvent e) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
	 */
	@Override
	public void insertUpdate(DocumentEvent e) {
		/* lock  before writeing */
		lock.acquireUninterruptibly(IS_EMTPY);
		sync = text.getText();
		lock.release(HAS_URL);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
	 */
	@Override
	public void removeUpdate(DocumentEvent e) {}

}
