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
package de.jtheuer.jjcomponents.swing.panels;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.logging.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import de.jtheuer.jjcomponents.utils.ResourcesContainer;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * 
 */
public class JJImagePanel extends JPanel {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(JJImagePanel.class.getName());

	private Image displayimage;
	private JPanel graphic;
	private String currentstring="";

	/**
	 * 
	 */
	public JJImagePanel(Dimension dimension) {
		this(dimension, false);
	}

	public JJImagePanel(Dimension dimension, boolean editable) {
		this(dimension, editable, null);
	}

	public JJImagePanel(Dimension dimension, boolean editable, URL imageurl) {

		currentstring = imageurl != null ? imageurl.toString() : "http://";

		/* init graphic Panel */
		graphic = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				g.setColor(Color.WHITE);
				g.fillRect(0, 0, getWidth(), getHeight());
				if (displayimage != null) {
					double imgwidth = displayimage.getWidth(this);
					double imgheight = displayimage.getHeight(this);

					double scalefactor = Math.min((double) getWidth() / imgwidth, (double) getHeight() / imgheight);
					g.drawImage(displayimage, 00, 0, (int) (scalefactor * imgwidth), (int) (scalefactor * imgheight), this);
				}
			}
		};
		graphic.setPreferredSize(dimension);

		setBorder(BorderFactory.createLoweredBevelBorder());
		setLayout(new BorderLayout());
		add(graphic, BorderLayout.CENTER);

		if (editable) {
			add(new JButton(new AbstractAction("change...") {
				@Override
				public void actionPerformed(ActionEvent e) {
					String result = JOptionPane.showInputDialog("Enter new Image url", currentstring);
					if(result != null) {
						currentstring = result;
						try {
							loadImage(new URL(result));
						} catch (Exception ex) {
							LOGGER.fine("Cannot read picture url" + result);
							loadImage(null);
						}
					}
				}
			}), BorderLayout.SOUTH);
		}
		
		loadBackground(imageurl);
	}

	public void loadImage(URL image) {
		loadBackground(image);
	}

	private void loadBackground(final URL image) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				try {
					displayimage = Toolkit.getDefaultToolkit().getImage(image);
				} catch (RuntimeException e) {
					displayimage = ResourcesContainer.CANCEL.getAsImage(64);
				}
				repaint();
			}

		});
	}

	/**
	 * @param textComponent
	 */
	public void setTextComponent(final JTextComponent textComponent) {
		textComponent.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(DocumentEvent e) {

			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				try {
					URL url = new URL(textComponent.getText());
					loadBackground(url);
				} catch (Exception ex) {
					// do nothing
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				try {
					URL url = new URL(textComponent.getText());
					loadBackground(url);
				} catch (Exception ex) {
					// do nothing
				}
			}

		});
	}
	
	public String getURL() {
		return currentstring;
	}
}
