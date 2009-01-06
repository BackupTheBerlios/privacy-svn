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
import java.util.Set;
import java.util.logging.Logger;

import javax.swing.JComponent;

import org.openrdf.elmo.ElmoManager;

import de.jtheuer.diki.ElmoPanel;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 * generic class for all ElmoObjects (this are all Objects which do not act as top-level collection for forms. Examples are Image or Document)
 */
public abstract class ElmoObjectPanel<T> extends ElmoPanel<T> {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(ElmoObjectPanel.class.getName());
	private String label;
	private Set<T> set;
	private ElmoManager manager;
	
	/**
	 * @param label the used label for this property object
	 */
	public ElmoObjectPanel(String label) {
		this.label = label;
	}

	/**
	 * @param label the used label for this property object
	 */
	public ElmoObjectPanel(String label,Set<T> value, ElmoManager manager) {
		this.label = label;
		this.set = value;
		this.manager = manager;
	}

	/**
	 * @param label the used label for this property object
	 */
	public ElmoObjectPanel(String label,Set<T> value) {
		this(label,value,null);
	}

	protected abstract JComponent getViewComponent();
	
	protected abstract JComponent getEditComponent();

	/**
	 * Saves the value returned by getValue into the set supplied at the constructor (if). Deletes all other elements!
	 */
	public void save() {
		if(set != null) {
			/* retrieve value from implementation */
			T value = getValue();
			set.clear();
			if(value != null) {
				LOGGER.fine("Saveing " + label + "="+value);
				set.add(value);
			}
		} else {
			LOGGER.fine("Ignoreing " + label);
		}
	}
	
	/**
	 * @return
	 */
	public String getLabel() {
		return label;
	}

	public abstract T getValue();

	public ElmoManager getManager() {
		return manager;
	}

}
