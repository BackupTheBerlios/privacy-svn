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
package de.jtheuer.diki.gui.panels.tagpanel;

import java.awt.BorderLayout;
import java.awt.event.*;
import java.util.*;
import java.util.logging.Logger;

import javax.swing.*;
import javax.swing.tree.TreeModel;

import org.openrdf.concepts.dc.DcResource;
import org.openrdf.concepts.foaf.Agent;
import org.openrdf.concepts.foaf.Person;
import org.openrdf.elmo.sesame.SesameManager;

import uk.co.holygoat.tag.concepts.Tag;
import uk.co.holygoat.tag.concepts.Tagging;

import com.jidesoft.swing.JidePopupMenu;
import com.jidesoft.swing.JideScrollPane;

import de.jtheuer.diki.lib.NetworkConnection;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * 
 */
public class DiscoveryPanel extends JPanel {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(DiscoveryPanel.class.getName());
	private JTree tree;

	/**
	 * 
	 */
	public DiscoveryPanel(final NetworkConnection connection) {

		setLayout(new BorderLayout());

		SesameManager manager = connection.getSesame().getLocalStore().createManager();
		ElmoRootClassNode<Tagging> ercn = new ElmoRootClassNode<Tagging>(null, Tagging.class, manager) {

			@Override
			protected ElmoClassNode renderChild(final Tagging child) {
				final String title;
				Iterator<?> it = child.getTagsTaggedResources().iterator();
				if (it.hasNext()) {
					Object next = it.next();
					if (next instanceof DcResource) {
						DcResource dcresource = (DcResource) next;
						title = dcresource.getDcTitle();
					} else {
						title = next.toString();
					}
				} else {
					title = child.toString();
				}

				return new ElmoClassNode(this, child, title) {

					@Override
					public void lazyInitialize() {
						List<ElmoNode> nodes = new ArrayList<ElmoNode>();
						nodes.add(new ElmoNode(this, "creator") {
							@Override
							public void lazyInitialize() {
								List<String> creators = new ArrayList<String>();
								for (Agent a : child.getTagsTaggedBy()) {
									creators.add(a.toString());
								}
								setNodesString(creators);
							}
						});
						nodes.add(new ElmoNode(this, "tags") {
							@Override
							public void lazyInitialize() {
								List<String> tags = new ArrayList<String>();
								for (Object t : child.getTagsAssociatedTags()) {
									if (t instanceof Tag) {
										tags.add(((Tag) t).getTagsNames().iterator().next());
									} else {
										LOGGER.warning("Not a Tag: " + t.toString());
									}
								}
								setNodesString(tags);
							}
						});
						setNodes(nodes);
					}

				};
			}

		};

		ElmoRootClassNode<Tag> tags = new ElmoRootClassNode<Tag>(null, Tag.class, manager) {
			@Override
			protected ElmoClassNode renderChild(final Tag child) {
				return new ElmoClassNode(this, child, child.getTagsNames().iterator().next()) {

					@Override
					public void lazyInitialize() {}
				};
			}
		};

		ElmoRootClassNode<Person> persons = new ElmoRootClassNode<Person>(null, Person.class, manager) {
			@Override
			protected ElmoClassNode renderChild(final Person child) {
				return new ElmoClassNode(this, child, child.getQName().toString()) {

					@Override
					public void lazyInitialize() {}
				};
			}
		};

		TreeModel model = new ElmoModel(connection, ercn, tags, persons);
		tree = new JTree(model);

		tree.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					if (tree.getSelectionPath().getLastPathComponent() instanceof ElmoClassNode) {
						ElmoClassNode elmo = (ElmoClassNode) tree.getSelectionPath().getLastPathComponent();
						new Popup(elmo.toString(), elmo, connection).show(tree, e.getX(), e.getY());
					}
				}
			}

		});
		JideScrollPane p = new JideScrollPane(tree, JideScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JideScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		add(p, BorderLayout.CENTER);
	}

	static class Popup extends JidePopupMenu {

		public Popup(String title, final ElmoClassNode instance, final NetworkConnection connection) {
			super(title);
			add(new AbstractAction("delete") {

				@Override
				public void actionPerformed(ActionEvent e) {
					SesameManager manager = connection.getSesame().getLocalStore().createManager();
					manager.remove(instance.getEntity());
					manager.close();
				}
			});
		}

	}

}
