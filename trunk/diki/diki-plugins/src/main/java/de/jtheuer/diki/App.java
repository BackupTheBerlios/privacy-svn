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
package de.jtheuer.diki;

import javax.swing.JFrame;

import org.openrdf.concepts.foaf.Person;
import org.openrdf.elmo.ElmoModule;
import org.openrdf.elmo.sesame.SesameManagerFactory;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.sail.memory.MemoryStore;

import de.jtheuer.diki.elmo.PersonPanel;
import de.jtheuer.sesame.SimpleSet;

/**
 * Demo app for Elmo concept views
 *
 */
public class App 
{
    public static void main( String[] args ) throws RepositoryException
    {
    	SailRepository repository = new SailRepository(new MemoryStore());
    	repository.initialize();
		ElmoModule module = new ElmoModule();
		SesameManagerFactory factory = new SesameManagerFactory(module, repository);
		Person person = factory.createElmoManager().designate(Person.class);
		
		person.setFoafFirstNames(SimpleSet.create("Horst"));
		
		JFrame f = new JFrame();
		f.getContentPane().add(new PersonPanel(person));
		f.pack();
		f.setVisible(true);
    }
}
