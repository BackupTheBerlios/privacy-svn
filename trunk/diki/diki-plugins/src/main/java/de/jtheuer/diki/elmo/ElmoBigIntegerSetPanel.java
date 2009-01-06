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
import java.math.BigInteger;
import java.util.Set;
import java.util.logging.*;

import javax.swing.*;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 */
public class ElmoBigIntegerSetPanel extends ElmoObjectPanel<BigInteger> {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(ElmoBigIntegerSetPanel.class.getName());

	private BigInteger content;
	private JTextField textField;

	public ElmoBigIntegerSetPanel(String label,Set<BigInteger> bigInteger) {
		super(label,bigInteger,null);
		content = firstOrNull(bigInteger);
	}

	/* (non-Javadoc)
	 * @see de.jtheuer.diki.ElmoPanel#getEditComponent()
	 */
	@Override
	protected JComponent getEditComponent() {
		textField = new JTextField(content.toString());
		return textField;
	}

	/* (non-Javadoc)
	 * @see de.jtheuer.diki.ElmoPanel#getViewComponent()
	 */
	@Override
	protected JComponent getViewComponent() {
		return new JLabel(content.toString());
	}

	@Override
	public BigInteger getValue() {
		return new BigInteger(textField.getText());
	}

}
