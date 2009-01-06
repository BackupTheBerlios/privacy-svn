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

import java.awt.BorderLayout;
import java.util.logging.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.jidesoft.swing.JideScrollPane;

import de.jtheuer.jjcomponents.utils.ResourcesContainer;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * 
 */
public class LoggingPanel extends JPanel {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(LoggingPanel.class.getName());

	private DefaultTableModel model;

	/**
	 * Creates a new Logpanel that shows up to 100 LogMessages
	 */
	public LoggingPanel() {
		final LogBufferHandler logbuffer = new LogBufferHandler(100);

		Handler h = new Handler() {

			@Override
			public void close() throws SecurityException {}

			@Override
			public void flush() {}

			@Override
			public void publish(LogRecord record) {
				if (isLoggable(record)) {
					logbuffer.addRecord(record);
					model.fireTableDataChanged();
				}
			}

		};
		h.setLevel(Level.INFO);
		LOGGER.getParent().addHandler(h);

		model = new DefaultTableModel(1, 4) {

			@Override
			public Class<?> getColumnClass(int columnIndex) {
				return columnIndex == 0 ? Icon.class : String.class;
			}

			@Override
			public int getRowCount() {
				return logbuffer.getSize();
			}

			@Override
			public String getColumnName(int column) {
				switch (column) {
				case 1:
					return "time";
				case 2:
					return "message";
				case 3:
					return "component";
				default:
					return "";
				}
			}

			@Override
			public Object getValueAt(int row, int column) {
				LogRecord record = logbuffer.getRecord(row);
				if (record != null) {
					if (column == 0) {
						if (record.getLevel().intValue() <= Level.INFO.intValue()) {
							return ResourcesContainer.INFO.getAsIcon(16);
						} else if (record.getLevel().equals(Level.WARNING)) {
							return ResourcesContainer.WARN.getAsIcon(16);
						} else {
							return ResourcesContainer.ERROR.getAsIcon(16);
						}
					} else if (column == 1) {
						return LogBufferHandler.getLocalTimeOfRecord(record);
					} else if (column == 2) {
						return LogBufferHandler.getMessageOf(record);
					} else if (column == 3) {
						int pos = record.getLoggerName().lastIndexOf(".") + 1;
						return record.getLoggerName().substring(pos);
					}
				}

				return null;
			}

		};

		setLayout(new BorderLayout());
		JTable table = new JTable(model);
		table.getColumnModel().getColumn(0).setMaxWidth(20);	// 20px image
		table.getColumnModel().getColumn(1).setMaxWidth(200);	// size
		
		add(new JideScrollPane(table), BorderLayout.CENTER);
	}

}
