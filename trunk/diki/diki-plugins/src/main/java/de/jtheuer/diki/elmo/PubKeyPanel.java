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
import java.util.logging.Logger;

import org.openrdf.elmo.Entity;

import com.xmlns.wot.PubKey;

import de.jtheuer.jjcomponents.layout.JJSimpleFormLayout;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 */
public class PubKeyPanel extends ElmoSubjectPanel<Entity> {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(PubKeyPanel.class.getName());

	
	public PubKeyPanel(PubKey entity) {
		super((Entity) entity);
	}


	/* (non-Javadoc)
	 * @see de.jtheuer.diki.elmo.ElmoSubjectPanel#initialize(de.jtheuer.jjcomponents.layout.JJSimpleFormLayout)
	 */
	@Override
	protected void initialize(JJSimpleFormLayout layout) {
		PubKey k = (PubKey) getEntity();
		
		add(new ElmoStringSetPanel("Key ID",k.getHex_id()));
		add(new ElmoStringSetPanel("Fingerprint",k.getFingerprints()));
		add(new ElmoBigIntegerSetPanel("Key lenght",k.getLengths()));
	}
}
