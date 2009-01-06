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

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.persistence.EntityTransaction;
import javax.swing.*;

import org.openrdf.elmo.Entity;

import com.jidesoft.dialog.ButtonNames;
import com.jidesoft.dialog.ButtonPanel;

import de.jtheuer.diki.ElmoPanel;
import de.jtheuer.jjcomponents.layout.JJSimpleFormLayout;
import de.jtheuer.jjcomponents.utils.ResourcesContainer;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * 
 */
public abstract class ElmoSubjectPanel<T extends Entity> extends ElmoPanel<T> implements ButtonNames {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(ElmoSubjectPanel.class.getName());
	private T entity;
	private JJSimpleFormLayout layout;
	private ArrayList<ElmoObjectPanel<?>> panels = new ArrayList<ElmoObjectPanel<?>>();

	/**
	 * @param entity
	 */
	public ElmoSubjectPanel(T entity) {
		this.entity = entity;
		newLayout();
		initialize(layout);
	}

	protected void newLayout() {
		layout = new JJSimpleFormLayout(this);
	}

	protected T getEntity() {
		return entity;
	}

	protected abstract void initialize(JJSimpleFormLayout layout);

	/**
	 * for convenience the supplied component is also returned!
	 * 
	 * @param panel
	 * @return the supplied panel
	 */
	protected <S extends ElmoObjectPanel<?>> S add(S panel) {
		panels.add(panel);
		return panel;
	}



	/**
	 * @return a {@link JComponent} that contains OK and Cancel buttons
	 */
	private JComponent createButtonPanel() {
		ButtonPanel buttons = new ButtonPanel();
		JButton _okButton = new JButton();
		JButton _cancelButton = new JButton();

		buttons.addButton(_okButton, ButtonPanel.AFFIRMATIVE_BUTTON);
		buttons.addButton(_cancelButton, ButtonPanel.CANCEL_BUTTON);

		_okButton.setAction(new AbstractAction("Save", ResourcesContainer.SAVE.getAsIcon()) {
			private static final long serialVersionUID = 8642484624814231242L;

			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		_cancelButton.setAction(new AbstractAction("Cancel", ResourcesContainer.CANCEL.getAsIcon()) {
			private static final long serialVersionUID = -5419180404642948798L;

			public void actionPerformed(ActionEvent e) {
				edit();
			}
		});

		return buttons;
	}

	protected void save() {
		EntityTransaction trans = getEntity().getElmoManager().getTransaction();
		trans.begin();
		try {
			for (ElmoObjectPanel<?> panel : panels) {
				panel.save();
			}
			trans.commit();
		} catch (Exception e) {
			LOGGER.warning("Could not save transaction, changes are los!" + e.getMessage());
			trans.rollback();
		}
	}

	public void edit() {
		removeAll();
		newLayout();
		for (ElmoObjectPanel<?> panel : panels) {
			JComponent edit_component = panel.getEditComponent();
			if (edit_component == null) {
				layout.add(panel.getLabel());
			} else {
				layout.add(panel.getLabel(), edit_component);
			}
		}
		layout.addSeperator("");
		layout.addSpanningComponent(createButtonPanel());
		revalidate();
	}
	
	public void view() {
		removeAll();
		newLayout();
		for (ElmoObjectPanel<?> panel : panels) {
			JComponent view = panel.getViewComponent();
			if (view == null) {
				layout.add(panel.getLabel());
			} else {
				layout.add(panel.getLabel(), view);
			}
		}

		// layout.addSpanningComponent(createButtonPanel());
		revalidate();
	}

}
