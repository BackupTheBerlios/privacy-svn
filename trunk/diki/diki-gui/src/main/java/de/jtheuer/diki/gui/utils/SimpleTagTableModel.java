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
package de.jtheuer.diki.gui.utils;

import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.table.AbstractTableModel;

public abstract class SimpleTagTableModel extends AbstractTableModel {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(SimpleTagTableModel.class
				.getName());
	
	private List<String> tags = new Vector<String>(10);

	public SimpleTagTableModel() {
		super();
	}

	public void setTags(List<String> tags) {
		clear();
		tags.addAll(tags);
		addNewTag();
		fireTableStructureChanged();
	}

	protected void addNewTag() {
		tags.add(null);
	}

	protected void clear() {
		tags.clear();
	}

	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	public int getColumnCount() {
		return 1;
	}

	public String getColumnName(int columnIndex) {
		return null;
	}

	public int getRowCount() {
		return tags.size();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		if(rowIndex < tags.size()) {
			return tags.get(rowIndex);
		} else {
			return "";	
		}
	}

	public void setValueAt(Object value, int index, int unused_columnIndex) {
		if(value instanceof String && index >= 0) {
			tags.set(index,(String) value );
			
			if(index == tags.size()-1) {
				addNewTag();
				fireTableRowsInserted(index, index);
			}
		}
	}

	public void addTag(Object value) {
		setValueAt(value, tags.size()-1, 0);
	}
	
	public List<String> getTags() {
		return tags;
	}

}