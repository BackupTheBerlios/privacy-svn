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
package de.jtheuer.jjcomponents.swing.dialog;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Locale;
import java.util.logging.Logger;

import javax.swing.*;

import com.jidesoft.dialog.*;
import com.jidesoft.plaf.UIDefaultsLookup;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * 
 */
public class JJMultiplePageDialog extends MultiplePageDialog {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(JJMultiplePageDialog.class.getName());
	private JButton okButton;
	private JButton cancelButton;
	private JButton applyButton;

	/**
	 * @throws HeadlessException
	 */
	public JJMultiplePageDialog() throws HeadlessException {}

	/**
	 * @param owner
	 * @throws HeadlessException
	 */
	public JJMultiplePageDialog(Frame owner) throws HeadlessException {
		super(owner);

	}

	/**
	 * @param owner
	 * @throws HeadlessException
	 */
	public JJMultiplePageDialog(Dialog owner) throws HeadlessException {
		super(owner);

	}

	/**
	 * @param owner
	 * @param modal
	 * @throws HeadlessException
	 */
	public JJMultiplePageDialog(Frame owner, boolean modal) throws HeadlessException {
		super(owner, modal);

	}

	/**
	 * @param owner
	 * @param title
	 * @throws HeadlessException
	 */
	public JJMultiplePageDialog(Frame owner, String title) throws HeadlessException {
		super(owner, title);

	}

	/**
	 * @param owner
	 * @param modal
	 * @throws HeadlessException
	 */
	public JJMultiplePageDialog(Dialog owner, boolean modal) throws HeadlessException {
		super(owner, modal);

	}

	/**
	 * @param owner
	 * @param title
	 * @throws HeadlessException
	 */
	public JJMultiplePageDialog(Dialog owner, String title) throws HeadlessException {
		super(owner, title);

	}

	/**
	 * @param owner
	 * @param title
	 * @param modal
	 * @throws HeadlessException
	 */
	public JJMultiplePageDialog(Frame owner, String title, boolean modal) throws HeadlessException {
		super(owner, title, modal);

	}

	/**
	 * @param owner
	 * @param title
	 * @param modal
	 * @throws HeadlessException
	 */
	public JJMultiplePageDialog(Dialog owner, String title, boolean modal) throws HeadlessException {
		super(owner, title, modal);

	}

	/**
	 * @param owner
	 * @param title
	 * @param modal
	 * @param style
	 * @throws HeadlessException
	 */
	public JJMultiplePageDialog(Frame owner, String title, boolean modal, int style) throws HeadlessException {
		super(owner, title, modal, style);

	}

	/**
	 * @param owner
	 * @param title
	 * @param modal
	 * @param style
	 * @throws HeadlessException
	 */
	public JJMultiplePageDialog(Dialog owner, String title, boolean modal, int style) throws HeadlessException {
		super(owner, title, modal, style);

	}

	/**
	 * Creates the button panel. It has three buttons - OK, Cancel and Apply. If
	 * you want to create your own button panel, just override this method.
	 * 
	 * @return button panel
	 */
	@Override
	public ButtonPanel createButtonPanel() {
		ButtonPanel bpanel = super.createButtonPanel();
		Locale l = getLocale();

		final AbstractAction okaction = getOkAction();
		if (okaction != null) {
			getOkButton().setAction(new AbstractAction(UIDefaultsLookup.getString("OptionPane.okButtonText", l)) {
				public void actionPerformed(ActionEvent e) {
					okaction.actionPerformed(e);
				}
			});
		}

		final Action cancelaction = getCancelAction();
		if (cancelaction != null) {
			getCancelButton().setAction(new AbstractAction(UIDefaultsLookup.getString("OptionPane.cancelButtonText", l)) {
				public void actionPerformed(ActionEvent e) {
					cancelaction.actionPerformed(e);
				}
			});
		}

		final Action applyaction = getApplyAction();
		if (applyaction != null) {
			getApplyButton().setAction(new AbstractAction(ButtonResources.getResourceBundle(Locale.getDefault()).getString("Button.apply")) {
				public void actionPerformed(ActionEvent e) {
					if (getCurrentPage() != null) {
						getCurrentPage().fireButtonEvent(ButtonEvent.DISABLE_BUTTON, APPLY);
					}
				}
			});
		}

		return bpanel;
	}

	/**
	 * overwrite this method if you want to supply your own ok action.
	 * Note, that icon and name will not be used.
	 * @return
	 */
	public AbstractAction getOkAction() {
		return null;
	}

	/**
	 * overwrite this method if you want to supply your own cancel action
	 * Note, that icon and name will not be used.
	 * @return
	 */
	public Action getCancelAction() {
		return null;
	}

	/**
	 * overwrite this method if you want to supply your own apply action
	 * Note, that icon and name will not be used.
	 * @return
	 */
	public Action getApplyAction() {
		return null;
	}

}
