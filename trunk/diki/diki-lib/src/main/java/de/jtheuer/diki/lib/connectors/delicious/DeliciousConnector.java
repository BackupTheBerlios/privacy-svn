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
/**
    (c) by Jan Torben Heuer <jan.heuer@uni-muenster.de

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/
 */
package de.jtheuer.diki.lib.connectors.delicious;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.QName;

import org.openrdf.concepts.dc.DcResource;
import org.openrdf.concepts.foaf.Agent;
import org.openrdf.elmo.ElmoManager;
import org.openrdf.repository.RepositoryException;
import org.paceproject.diki.elmo.QueryResponse;

import uk.co.holygoat.tag.concepts.Tag;
import uk.co.holygoat.tag.concepts.Tagging;
import de.jtheuer.diki.lib.NetworkConnection;
import de.jtheuer.diki.lib.connectors.AbstractHttpConnector;
import de.jtheuer.diki.lib.connectors.ConnectorException;
import de.jtheuer.diki.lib.query.*;
import de.jtheuer.sesame.QNameURI;
import de.jtheuer.sesame.SimpleSet;
import del.icio.us.Delicious;

/**
 * 
 */
public class DeliciousConnector extends AbstractHttpConnector {
	/* auto-generated Logger */@SuppressWarnings("unused")
	final static Logger LOGGER = Logger.getLogger(DeliciousConnector.class.getName());
	private Delicious deliciousconnection;

	// this.connection = new Delicious("frogger333","kmmzgoB5");
	private static final String KEY_USER = "delicious.username";
	private static final String KEY_PASSWORD = "delicious.password";
	private static final String FAVICONURL = "http://de.icio.us/favicon.ico";
	private static final String NAME = "del.icio.us";
	private static final String QNAME_TAG = "http://del.icio.us/tag/";
	private static final String QNAME_TAGGING = "http://del.icio.us/post/";


	public DeliciousConnector(NetworkConnection connection) {
		super(NAME, FAVICONURL, connection);
	}

	@SuppressWarnings({ "unchecked" })
	private void queryDelicious(final NetworkQuery networkQuery) throws RepositoryException {
		QueryInterface query = networkQuery.getOriginalQuery();
		if (query instanceof SparqlTagQuery) {

			final Set<Agent> agents = SimpleSet.create((Agent) getConnection().getUserFactory().getLocalUser());
			final String[] tags = ((SparqlTagQuery) query).getTags();
			
			ElmoManager manager = networkQuery.getElmoQuery().getElmoManager();
			QueryResponse queryResponse = networkQuery.createResponse();

			List<del.icio.us.beans.Post> result = deliciousconnection.getPostsForTags(tags);
			
			Set<Object> results = new HashSet<Object>(result.size());
			for (del.icio.us.beans.Post post : result) {
				
				/* set dublin core */
				QNameURI href = new QNameURI(post.getHref());
				DcResource newPost = manager.designate(DcResource.class, href.toQName());
				newPost.setDcTitle(post.getDescription());
				newPost.setDcDescription(post.getExtended());

				/* set tag ontology */
				Tagging tagging = manager.designate(Tagging.class, new QName(QNAME_TAGGING, post.getHash()));
				tagging.setTagsTaggedResources(new SimpleSet<Object>(newPost));
				tagging.setTagsTaggedBy(agents);
				
				/* set tags */
				Set<Tag> rtags = new SimpleSet<Tag>();
				for (String string : post.getTag().split(" ")) {
					Tag newTag = manager.designate(Tag.class, new QName(QNAME_TAG, string));
					newTag.setTagsNames(new SimpleSet(string));
					rtags.add(newTag);
				}
				tagging.setTagsAssociatedTags(rtags);
				results.add(tagging);
			}
			queryResponse.setResult(results);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jtheuer.diki.lib.connectors.Connector#connect(de.jtheuer.diki.lib.NetworkConnection,
	 *      java.util.Properties)
	 */
	@Override
	public void connect(Properties prop) throws ConnectorException {
		setStatus(Status.Connecting);
		if(prop.getProperty(KEY_USER) != null) {
			try {
			this.deliciousconnection = new Delicious(prop.getProperty(KEY_USER), prop.getProperty(KEY_PASSWORD));
			setStatus(Status.Connected);
			} catch (Exception e) {
				setStatus(Status.Disconnected);
				throw new ConnectorException(e);
			}
		} else {
			setStatus(Status.Disabled);
			LOGGER.info("not starting del.icio.us connector: username is null");
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jtheuer.diki.lib.connectors.Connector#getParameters()
	 */
	@Override
	public Properties getParameters() {
		Properties prop = new Properties();
		prop.setProperty(KEY_USER, "username");
		prop.setProperty(KEY_PASSWORD, "password");
		return prop;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jtheuer.diki.lib.connectors.AbstractConnector#getQuerySolver(de.jtheuer.diki.lib.query.Query)
	 */
	@Override
	protected Runnable innerEvaluate(final NetworkQuery networkQuery) {
		return new Runnable() {

			@Override
			public void run() {
				try {
					queryDelicious(networkQuery);
				} catch (RepositoryException e) {
					LOGGER.log(Level.SEVERE,"cannot store concepts", e);
				}
			}
			
			@Override
			public String toString() {
				return NAME;
			}

		};
	}

}
