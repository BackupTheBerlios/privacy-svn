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
	
package de.jtheuer.diki.gui.utils;
import java.awt.event.KeyEvent;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.KeyStroke;
import javax.swing.text.JTextComponent;

import com.jidesoft.hints.ListDataIntelliHints;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 */
public class TagAutocomplete extends ListDataIntelliHints {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(TagAutocomplete.class.getName());

	/**
	 * @param textComponent
	 * @param tags 
	 */
	public TagAutocomplete(JTextComponent textComponent, Vector<String> tags) {
		super(textComponent,tags);
		setCaseSensitive(false);
	}

	/* (non-Javadoc)
	 * @see com.jidesoft.hints.IntelliHints#updateHints(java.lang.Object)
	 */
	@Override
	public boolean updateHints(Object context) {
		return super.updateHints(context);
	}
	
    /**
     * Gets the keystroke that will trigger the hint popup. Usually the hints popup will be shown
     * automatically when user types. Only when the hint popup is hidden accidentally, this
     * keystroke will show the popup again.
     * <p/>
     * By default, it's the DOWN key for JTextField and CTRL+SPACE for JTextArea.
     *
     * @return the keystroek that will trigger the hint popup.
     */
    protected KeyStroke getShowHintsKeyStroke() {
         return KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, KeyEvent.CTRL_MASK);
    }
}
