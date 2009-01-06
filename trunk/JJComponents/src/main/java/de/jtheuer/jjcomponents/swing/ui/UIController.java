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
package de.jtheuer.jjcomponents.swing.ui;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.metal.MetalLookAndFeel;

import com.jgoodies.looks.plastic.*;
import com.jgoodies.looks.windows.WindowsLookAndFeel;
import com.jidesoft.plaf.LookAndFeelFactory;

import de.jtheuer.jjcomponents.properties.PropertyObject;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * 
 * Controls the different UIs. It shall display the most appropriate UI in
 * default-mode.
 * 
 * If the user wants to override it, the controller tries to load that ui - and
 * falls back if it doesn't work.
 */
public class UIController {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(UIController.class.getName());

	private static Vector<UIInstance> lnfs = new Vector<UIInstance>();

	static {
		/* in order of preference ... */

		/* mac os? */
		try {
			LookAndFeel lnf = (LookAndFeel) Class.forName(LookAndFeelFactory.AQUA_LNF).newInstance();
			lnfs.add(new UIInstance(lnf));
		} catch (Exception e) {

		}

		lnfs.add(new UIInstance(new WindowsLookAndFeel()));
		try {
			lnfs.add(new UIInstance((LookAndFeel) Class.forName("com.sun.java.swing.plaf.gtk.GTKLookAndFeel").newInstance()));
		}catch(Exception e) {
			//ignore
		}
		lnfs.add(new UIInstance(new Plastic3DLookAndFeel()));
		lnfs.add(new UIInstance(new PlasticLookAndFeel()));
		lnfs.add(new UIInstance(new PlasticXPLookAndFeel()));
		lnfs.add(new UIInstance(new MetalLookAndFeel()));

	}

	/**
	 * @param classname
	 *            the full classname of the ui class
	 * @return true if the change was successful
	 */
	public static boolean loadUI(LookAndFeel lnf) {
		LOGGER.info("requesting ui: " + lnf.getID());
		if (!lnf.equals(LookAndFeelFactory.getLookAndFeel())) {
			try {
				UIManager.setLookAndFeel(lnf);
				LookAndFeelFactory.installJideExtension();
				UIManager.put("TextArea.border", UIManager.get("TextField.border"));
				return true;
			} catch (Exception e) {
				LOGGER.warning(e.getMessage());
				return false;
			}
		} else {
			LOGGER.info("Ignoring change to the current Look'n'Feel!");
			return true;
		}
	}

	public static boolean loadDefault(String userlnf) {
		if (userlnf != null) {
			try {
				LookAndFeel z = (LookAndFeel) Class.forName(userlnf).newInstance();
				return loadUI(z);
			} catch (Exception e) {
				return false;
			}
		} else {
			for (UIInstance lnf : lnfs) {
				if (loadUI(lnf.getLookAndFeel())) {
					return true;
				}
			}
			/* wow, NO lnf was loadable? strange */
			return false;

		}
	}

	public static Vector<UIInstance> getAvailableLnFs() {
		return lnfs;
	}

	public static class UIInstance implements PropertyObject {
		private LookAndFeel lookAndFeel;

		public UIInstance(LookAndFeel lookAndFeel) {
			this.lookAndFeel = lookAndFeel;
		}

		@Override
		public String toString() {
			return lookAndFeel == null ? "" : lookAndFeel.getDescription();
		}

		public LookAndFeel getLookAndFeel() {
			return lookAndFeel;
		}

		@Override
		public String getProperty() {
			return lookAndFeel.getClass().getName();
		}
	}

	/**
	 * A Listener that changes the UI of the updateRoot componenets and its
	 * children upon {@link ItemListener#itemStateChanged(ItemEvent)} event.
	 * 
	 * Simply add it to a combobox!
	 * 
	 * @param box
	 * @param updateRoot
	 * @return
	 */
	public static ListSelectionListener getListener(final JList box, final Component... updateRoot) {
		return new ListSelectionListener() {
			// @Override
			// public void itemStateChanged(ItemEvent e) {
			// if (e.getStateChange() == ItemEvent.SELECTED) {
			// UIInstance ui = (UIInstance) box.getSelectedValue();
			// loadUI(ui.getLookAndFeel());
			// for (Component component : updateRoot) {
			// SwingUtilities.updateComponentTreeUI(component);
			// }
			// }
			// }

			@Override
			public void valueChanged(ListSelectionEvent e) {
				UIInstance ui = (UIInstance) box.getSelectedValue();
				if (!e.getValueIsAdjusting() && ui != null) {
					loadUI(ui.getLookAndFeel());
					for (Component component : updateRoot) {
						SwingUtilities.updateComponentTreeUI(component);
					}
				}
			}
		};
	}

}
