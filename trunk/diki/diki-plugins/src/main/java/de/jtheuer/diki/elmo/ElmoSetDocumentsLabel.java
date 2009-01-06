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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.xml.namespace.QName;

import org.openrdf.concepts.foaf.Document;
import org.openrdf.elmo.ElmoManager;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * 
 */
public class ElmoSetDocumentsLabel extends ElmoObjectPanel<Document> {
	private static final long serialVersionUID = -7899313891879576991L;
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(ElmoSetDocumentsLabel.class.getName());
	private URL value;
	private Document document;
	private JTextField textField;

	/**
	 * @param string
	 * @param foafHomepages
	 */
	public ElmoSetDocumentsLabel(String label, Set<Document> documents, ElmoManager manager) {
		super(label, documents, manager);
		document = firstOrNull(documents,Document.class);
		if (document != null) {
			try {
				value = new URL(document.getQName().getNamespaceURI() + document.getQName().getLocalPart());
			} catch (MalformedURLException e) {
				value = null;
			}
		} else {
			value = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jtheuer.diki.elmo.ElmoObjectPanel#getEditComponent()
	 */
	@Override
	protected JComponent getEditComponent() {
		this.textField = new JTextField(value != null ? value.toString() : "");
		return textField;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jtheuer.diki.elmo.ElmoObjectPanel#getViewComponent()
	 */
	@Override
	protected JComponent getViewComponent() {
		return value == null ? null : new de.jtheuer.jjcomponents.swing.components.LinkButton(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jtheuer.diki.elmo.ElmoObjectPanel#getValue()
	 */
	@Override
	public Document getValue() {
		if (textField.getText() != null && textField.getText().length() > 0) {
			try {
				return getManager().designate(Document.class, new QName(textField.getText()));
			} catch (RuntimeException e) {
				LOGGER.log(Level.WARNING, "not saveing document!", e);
				return null;
			}
		}
		return null;
	}
}
