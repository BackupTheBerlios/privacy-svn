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
package de.jtheuer.diki.elmo;

import java.awt.Dimension;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.xml.namespace.QName;

import org.openrdf.concepts.foaf.Image;
import org.openrdf.elmo.ElmoManager;

import de.jtheuer.jjcomponents.swing.panels.JJImagePanel;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * 
 */
public class ElmoSetImageLabel extends ElmoObjectPanel<Image> {
	private static final long serialVersionUID = 1065497589527220812L;
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(ElmoSetImageLabel.class.getName());
	private Image image;
	private JJImagePanel imagePanel;

	/**
	 * @param label
	 */
	public ElmoSetImageLabel(String label, Set<Image> images, ElmoManager manager) {
		super(label, images, manager);
		this.image = firstOrNull(images,Image.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jtheuer.diki.elmo.ElmoObjectPanel#getEditComponent()
	 */
	@Override
	protected JComponent getEditComponent() {
		URL url = null;
		if (image != null) {
			try {
				url = new URL(image.getQName().getNamespaceURI() + image.getQName().getLocalPart());
			} catch (MalformedURLException e) {
				LOGGER.log(Level.INFO, image.getQName() + "is not a valid image URL");
			}
		}
		imagePanel = new JJImagePanel(new Dimension(120, 90), true, url);
		return imagePanel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jtheuer.diki.elmo.ElmoObjectPanel#getViewComponent()
	 */
	@Override
	protected JComponent getViewComponent() {
		URL url = null;
		if (image != null) {
			try {
				url = new URL(image.getQName().getNamespaceURI() + image.getQName().getLocalPart());
			} catch (MalformedURLException e) {
				LOGGER.log(Level.INFO, image.getQName() + "is not a valid image URL");
			}
		}
		return new JJImagePanel(new Dimension(90, 120), false, url);
	}

	@Override
	public Image getValue() {
		return getManager().designate(Image.class, new QName(imagePanel.getURL(), ""));
	}

}
