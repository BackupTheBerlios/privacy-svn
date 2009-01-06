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
import java.awt.Color;
import java.util.logging.Logger;

import javax.swing.*;
import javax.swing.border.BevelBorder;

import com.jidesoft.dialog.BannerPanel;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 */
public class JJBannerPanel extends BannerPanel {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(JJBannerPanel.class.getName());

	/**
	 * @param title
	 * @param subtitle
	 */
	public JJBannerPanel(String title, String subtitle) {
		super(title, subtitle);
		// headerPanel1.setFont(new Font("Tahoma", Font.PLAIN, 11));
		setBackground(Color.WHITE);
		setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
	}

	/**
	 * @param title
	 * @param subtitle
	 * @param titleIcon
	 */
	public JJBannerPanel(String title, String subtitle, ImageIcon titleIcon) {
		super(title, subtitle, titleIcon);
		setBackground(Color.WHITE);
		setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
	}

	/**
	 * @param title
	 * @param subtitle
	 * @param iconComponent
	 */
	public JJBannerPanel(String title, String subtitle, JComponent iconComponent) {
		super(title, subtitle, iconComponent);
		setBackground(Color.WHITE);
		setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
	}
}
