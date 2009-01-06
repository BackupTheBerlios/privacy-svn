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
package de.jtheuer.diki.gui.panels.logpanel;

/***************************************************************
 Copyright 2007 52North Initiative for Geospatial Open Source Software GmbH

  Author: jtheuer, University of Muenster

  Contact: Andreas Wytzisk, 
  52North Initiative for Geospatial Open Source SoftwareGmbH, 
  Martin-Luther-King-Weg 24,
  48155 Muenster, Germany, 
  info@52north.org

  This program is free software; you can redistribute it and/or
  modify it under the terms of the GNU General Public License
  version 2 as published by the Free Software Foundation.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; even without the implied WARRANTY OF
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program (see gnu-gpl v2.txt). If not, write to
  the Free Software Foundation, Inc., 59 Temple Place - Suite 330,
  Boston, MA 02111-1307, USA or visit the Free
  Software Foundations web page, http://www.fsf.org.

  ***************************************************************/

import java.util.logging.*;

import junit.framework.TestCase;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * 
 */
public class LogBufferHandlerTestCase extends TestCase {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(LogBufferHandlerTestCase.class.getName());
	private LogBufferHandler b;
	private int size;

	/**
	 * @param name
	 */
	public LogBufferHandlerTestCase(String name) {
		super(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		size = 10;
		b = new LogBufferHandler(size);
	}

	public final void testEmpty() {
		assertNull(b.getRecord(0));
	}

	public final void testTwo() {
		LogRecord rec[] = new LogRecord[2];

		rec[0] = newRecord(0);
		rec[1] = newRecord(1);

		b.addRecord(rec[0]);
		b.addRecord(rec[1]);
		
		assertSame(rec[1],b.getRecord(0));
		assertSame(rec[0],b.getRecord(1));
	}

	public final void testAdd() {
		LogRecord rec[] = new LogRecord[size * 2];

		/* add 2 times the buffersize of logmessages */
		for (int i = 0; i < size * 2; i++) {
			rec[i] = newRecord(i);
			b.addRecord(rec[i]);
		}
		
		for(int i=0;i<size; i++) {
			assertSame(rec[size*2-i-1],b.getRecord(i));
		}
	}

	public final void testConvert() {
		String val = LogBufferHandler.getLocalTimeOfRecord(newRecord(0));
		assertFalse(val.equals(""));
	}
	

	private static LogRecord newRecord(int i) {
		return new LogRecord(Level.ALL, "Message " + i);
	}

}
