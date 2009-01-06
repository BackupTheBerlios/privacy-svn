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
package de.jtheuer.jjcomponents.swing;

import java.awt.Frame;
import java.util.logging.Logger;

import javax.swing.*;

import com.jidesoft.dialog.*;

import de.jtheuer.jjcomponents.swing.panels.JJBannerPanel;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * 
 */
public abstract class JJDialog extends StandardDialog {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(JJDialog.class.getName());
	private String bannertext;
	private String title;

	/**
	 * 
	 */
	public JJDialog(String title, String bannertext, Frame owner) {
		super(owner, title);
		this.bannertext = bannertext;
		this.title = title;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jidesoft.dialog.StandardDialog#createBannerPanel()
	 */
	@Override
	public JComponent createBannerPanel() {
		BannerPanel headerPanel1 = new JJBannerPanel(title, bannertext);
		return headerPanel1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jidesoft.dialog.StandardDialog#createButtonPanel()
	 */
	@Override
	public ButtonPanel createButtonPanel() {
		ButtonPanel buttonPanel = new ButtonPanel();
		JButton okButton = new JButton(getOkAction());
		JButton cancelButton = new JButton(getCancelAction());

		okButton.setName(OK);
		cancelButton.setName(CANCEL);

		buttonPanel.addButton(okButton, ButtonPanel.AFFIRMATIVE_BUTTON);
		buttonPanel.addButton(cancelButton, ButtonPanel.CANCEL_BUTTON);

		setDefaultCancelAction(cancelButton.getAction());
		setDefaultAction(okButton.getAction());
		getRootPane().setDefaultButton(okButton);
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		return buttonPanel;
	}
	
	protected Action getOkAction() {
		return null;
	}

	protected Action getCancelAction() {
		return null;
	}

}
