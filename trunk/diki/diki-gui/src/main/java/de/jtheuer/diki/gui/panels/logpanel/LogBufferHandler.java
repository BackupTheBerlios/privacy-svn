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
import java.text.DateFormat;
import java.text.ParseException;
import java.util.logging.*;

import javax.swing.text.DateFormatter;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 */
public class LogBufferHandler {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(LogBufferHandler.class.getName());

	private static DateFormatter fmt;
	
	private final int MAX;
	private LogRecord records[];
	private int size = 0;
	int first=-1;
	
	static {
		fmt = new DateFormatter(DateFormat.getTimeInstance());
	}

	/**
	 * 
	 */
	public LogBufferHandler(int size) {
		MAX = size;
		records = new LogRecord[MAX];
	}
	
	public synchronized void addRecord(LogRecord record) {
		/* set new size */
		if(size < MAX) {
			size++;
		}
		
		first++;
		if(first == MAX) {
			first = 0;
		}
		records[first] = record;
	}
	
	public synchronized LogRecord getRecord(int i) {
		if(first == -1 ){
			/* empty */
			
			return null;
		}
		
		if(i >= MAX) {
			throw new ArrayIndexOutOfBoundsException(i + " >) " + MAX);
		}
		
		int position = first - i;
		
		if(position < 0) {
			position = MAX + position;
		}

		
		return records[position];
	}
	
	public int getSize() {
		return size;
	}
	
	public static String getLocalTimeOfRecord(LogRecord rec) {
		try {
			return fmt.valueToString(rec.getMillis());
		} catch (ParseException e) {
			LOGGER.log(Level.WARNING,"illegal LogRecord Format", e);
			return "";
		}
	}

	/**
	 * @param record
	 * @return
	 */
	public static String getMessageOf(LogRecord record) {
		String m = record.getMessage();
		if(m==null) {
			if(record.getThrown() != null) {
				Throwable trowable = record.getThrown();
				m = trowable.getMessage();
			}
		}
		
		return m;
	}
}
