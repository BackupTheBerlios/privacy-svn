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
	
package de.jtheuer.sesame;
import org.openrdf.model.*;
import org.openrdf.model.impl.StatementImpl;
import org.openrdf.repository.Repository;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.sail.memory.MemoryStore;



/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 */
public interface Statements {
	
	public static final String NAMESPACE = "http://www.example.com/";
	
	public static final Resource SUBJECT1 = new QNameURI(NAMESPACE + "subject1");
	public static final Resource SUBJECT2 = new QNameURI(NAMESPACE + "subject2");
	public static final Resource SUBJECT3 = new QNameURI(NAMESPACE + "subject3");

	public static final URI PREDICATE1 = new QNameURI(NAMESPACE + "PREDICATE1");
	public static final URI PREDICATE2 = new QNameURI(NAMESPACE + "PREDICATE2");
	public static final URI PREDICATE3 = new QNameURI(NAMESPACE + "PREDICATE3");
	
	public static final Value OBJECT1 = new QNameURI(NAMESPACE + "OBJECT1");
	public static final Value OBJECT2 = new QNameURI(NAMESPACE + "OBJECT2");
	public static final Value OBJECT3 = new QNameURI(NAMESPACE + "OBJECT3");
	
	public static final QNameURI CONTEXT1 = new QNameURI(NAMESPACE + "CONTEXT1");
	public static final QNameURI CONTEXT2 = new QNameURI(NAMESPACE + "CONTEXT2");
	public static final QNameURI CONTEXT3 = new QNameURI(NAMESPACE + "CONTEXT3");
	
	public static final Statement STATEMENT1 = new StatementImpl(SUBJECT1,PREDICATE1,OBJECT1);
	public static final Statement STATEMENT2 = new StatementImpl(SUBJECT2,PREDICATE2,OBJECT2);
	public static final Statement STATEMENT3 = new StatementImpl(SUBJECT3,PREDICATE3,OBJECT3);
	
	public static final Repository REPOSITORY = new SailRepository(new MemoryStore()); 
	
}
