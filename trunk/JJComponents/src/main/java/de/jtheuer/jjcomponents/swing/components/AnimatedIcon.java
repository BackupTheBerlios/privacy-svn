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
package de.jtheuer.jjcomponents.swing.components;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.logging.*;

import javax.swing.*;

import de.jtheuer.jjcomponents.utils.ResourcesContainer;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 */
public class AnimatedIcon extends ImageIcon implements Icon, ActionListener {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(AnimatedIcon.class.getName());
	private static final double ROTATION_THETA = Math.PI/4; //quarter rotation (sic?)
	private Timer timer = new Timer(200,this);
	private Component icon_component;
	private double rotation;

	
	
	public AnimatedIcon() {
		super();
	}

	public AnimatedIcon(byte[] imageData, String description) {
		super(imageData, description);
		
	}

	public AnimatedIcon(byte[] imageData) {
		super(imageData);
		
	}

	public AnimatedIcon(Image image, String description) {
		super(image, description);
		
	}

	public AnimatedIcon(Image image) {
		super(image);
		
	}

	public AnimatedIcon(String filename, String description) {
		super(filename, description);
		
	}

	public AnimatedIcon(String filename) {
		super(filename);
		
	}

	public AnimatedIcon(URL location, String description) {
		super(location, description);
		
	}

	public AnimatedIcon(URL location) {
		super(location);
		
	}

	/* (non-Javadoc)
	 * @see javax.swing.Icon#paintIcon(java.awt.Component, java.awt.Graphics, int, int)
	 */
	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		/* create a copy */
		Graphics2D g2 = (Graphics2D) g.create();
		/* rotate the image */
		rotation += ROTATION_THETA;
		g2.rotate(rotation, x+getIconWidth()/2, y+getIconHeight()/2);
		super.paintIcon(c, g, x, y);
		if(icon_component == null) {
			icon_component = c;
			
			timer.start();
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(icon_component != null) {
			icon_component.repaint();
		}
	}
	
	public static void main(String[] args) {
		URL image = ResourcesContainer.RESTART.getURL();
		JFrame f = new JFrame();
		f.getContentPane().add(new JLabel(new AnimatedIcon(image)));
		f.pack();
		f.setVisible(true);
	}
}
