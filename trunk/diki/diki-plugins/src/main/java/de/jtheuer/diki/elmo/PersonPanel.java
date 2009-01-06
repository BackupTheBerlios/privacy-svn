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

import org.openrdf.concepts.foaf.Person;
import org.openrdf.elmo.ElmoManager;

import de.jtheuer.jjcomponents.layout.JJSimpleFormLayout;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * 
 */
public class PersonPanel extends ElmoSubjectPanel<Person> {
	/**	 */
	private static final long serialVersionUID = 5116092100875054094L;

	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(PersonPanel.class.getName());

	private ElmoObjectPanel firstname;
	private ElmoObjectPanel lastname;
	private ElmoObjectPanel homepage;
	private ElmoObjectPanel icq;
	private ElmoObjectPanel jabber;
	private ElmoObjectPanel gender;
	private ElmoObjectPanel photo;
	private ElmoObjectSetPanel nickname;

	/**
	 * @param entity
	 */
	public PersonPanel(Person entity) {
		super(entity);
	}

	/* (non-Javadoc)
	 * @see de.jtheuer.diki.elmo.ElmoSubjectPanel#initialize(de.jtheuer.jjcomponents.layout.JJSimpleFormLayout)
	 */
	@Override
	protected void initialize(JJSimpleFormLayout layout) {
		ElmoManager manager = getEntity().getElmoManager();
		photo = add(new ElmoSetImageLabel("Photo",getEntity().getFoafImgs(),manager));
		firstname = add(new ElmoObjectSetPanel("First name",getEntity().getFoafFirstNames()));
		lastname = add(new ElmoObjectSetPanel("Last name",getEntity().getFoafSurnames()));
		nickname = add(new ElmoObjectSetPanel("Nickname",getEntity().getFoafNames()));
		homepage = add(new ElmoSetDocumentsLabel("Homepage",getEntity().getFoafHomepages(),manager));
		icq = add(new ElmoObjectSetPanel("ICQ",getEntity().getFoafIcqChatIDs()));
		jabber = add(new ElmoObjectSetPanel("Jabber",getEntity().getFoafJabberIDs()));
		gender = add(new ElmoSimpleOption<Object>("Gender",getEntity().getFoafGender(),"","male","female"));
	}

	@Override
	protected void save() {
		super.save();
		getEntity().setFoafGender(gender.getValue());
	}
	
	
}
