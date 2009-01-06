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
package de.jtheuer.diki.lib.connectors.bibsonomy;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.QName;

import org.bibsonomy.common.enums.GroupingEntity;
import org.bibsonomy.model.*;
import org.bibsonomy.rest.client.Bibsonomy;
import org.bibsonomy.rest.client.exception.ErrorPerformingRequestException;
import org.bibsonomy.rest.client.queries.get.GetPostsQuery;
import org.openrdf.concepts.dc.DcResource;
import org.openrdf.concepts.foaf.Person;
import org.openrdf.elmo.ElmoManager;
import org.openrdf.repository.RepositoryException;
import org.paceproject.diki.elmo.QueryResponse;

import uk.co.holygoat.tag.concepts.Tag;
import uk.co.holygoat.tag.concepts.Tagging;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;

import de.jtheuer.diki.lib.NetworkConnection;
import de.jtheuer.diki.lib.connectors.AbstractHttpConnector;
import de.jtheuer.diki.lib.connectors.ConnectorException;
import de.jtheuer.diki.lib.query.*;
import de.jtheuer.sesame.QNameURI;
import de.jtheuer.sesame.SimpleSet;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * 
 */
public class BibsonomyConnector extends AbstractHttpConnector {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(BibsonomyConnector.class.getName());

	public static final String KEY_USER = "bibsonomy.username";
	public static final String KEY_PASSWORD = "bibsonomy.apikey";
	public static final String KEY_RESULTS = "bibsonomy.results";

	private static final String FAVICONURL = "http://bibsonomy.org/favicon.ico";
	private static final String NAME = "bibsonomy";
	private static final String QNAME_TAG = "http://bibsonomy.org/tag/";
	private static final String QNAME_TAGGING = "http://bibsonomy.org/post/";
	private static final String BIBSONOMYURL = "http://www.bibsonomy.org/api";
	public static final String ALL_RESULTS = "ALL";
	// private static final String TAGSEARCHURL =
	// "http://www.bibsonomy.org/rss/concept/tag/";

	private Bibsonomy bib;

	private String username;

	private boolean getAllResults = false;

	/**
	 * @param connection
	 */
	public BibsonomyConnector(NetworkConnection connection) {
		super(NAME, FAVICONURL, connection);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jtheuer.diki.lib.connectors.Connector#connect(java.util.Properties)
	 */
	@Override
	public void connect(Properties prop) throws ConnectorException {
		setStatus(Status.Connecting);
		username = prop.getProperty(KEY_USER);
		if (username != null && !"".equals(username)) {
			try {
				bib = new Bibsonomy(username, prop.getProperty(KEY_PASSWORD));
				bib.setApiURL(BIBSONOMYURL);

				getAllResults = ALL_RESULTS.equals(prop.getProperty(KEY_RESULTS));
				LOGGER.info("Bibsonomy connector is working" + (getAllResults ? " in get-all mode" : ""));

				setStatus(Status.Connected);
			} catch (Exception e) {
				bib = null;
				setStatus(Status.Disconnected);
				throw new ConnectorException(e);
			}
		} else {
			setStatus(Status.Disabled);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jtheuer.diki.lib.connectors.Connector#getParameters()
	 */
	@Override
	public Properties getParameters() {
		Properties properties = new Properties();
		properties.setProperty(KEY_USER, "username");
		properties.setProperty(KEY_PASSWORD, "API-key");
		return properties;
	}

	protected void queryBibsonomy(NetworkQuery networkQuery) throws RepositoryException {
		QueryInterface query = networkQuery.getOriginalQuery();

		Person localAgent = getConnection().getUserFactory().getLocalUser();
		List<String> tags;

		if (query instanceof SparqlTagQuery) {
			tags = Arrays.asList(((SparqlTagQuery) query).getTags());
		} else {
			tags = SparqlTagQuery.getTagsFromQuery(networkQuery);
		}

		if (tags != null && tags.size() > 0) {
			QueryResponse queryResponse = networkQuery.createResponse();
			ElmoManager manager = queryResponse.getElmoManager();

			// instantiate query object
			GetPostsQuery gpq = new GetPostsQuery();
			if (!getAllResults) {
				gpq.setGrouping(GroupingEntity.USER, username);
			}

			// some queries can be parameterized
			// -> in this example we want to fetch only bibtex entries

			gpq.setResourceType(Bookmark.class);
			gpq.setTags(tags);

			try {
				// perform query
				bib.executeQuery(gpq);

				// on success, read results
				if (gpq.getHttpStatusCode() == 200) {
					List<Post<? extends Resource>> posts = gpq.getResult();
					Set<Object> resultset = new HashSet<Object>(posts.size());
					for (Post<?> post : posts) {
						Bookmark bookmark = (Bookmark) post.getResource();

						/*
						 * we have to store href, Description and Tags in the
						 * ontology!
						 */
						DcResource d = manager.designate(DcResource.class, new QNameURI(bookmark.getUrl()).toQName());
						d.setDcDescription(clean(post.getDescription()));
						d.setDcTitle(bookmark.getTitle());

						/* define tagging concept */
						Tagging tagging = manager.designate(Tagging.class, new QName(QNAME_TAGGING, bookmark.getHash()));
						tagging.setTagsTaggedResources(new SimpleSet<Object>(d));

						Set<Tag> taglist = new SimpleSet<Tag>();
						for (org.bibsonomy.model.Tag tag : post.getTags()) {
							String string = tag.getName();
							Tag newTag = manager.designate(Tag.class, new QName(QNAME_TAG, string));
							newTag.setTagsNames(new SimpleSet<String>(string));
							taglist.add(newTag);
						}
						tagging.setTagsAssociatedTags(taglist);
						tagging.setTagsTaggedBy(SimpleSet.create(localAgent));
						
						/* set posting date */
						Date date = post.getDate();
						GregorianCalendar cal = new GregorianCalendar();
						cal.setTime(date);
						tagging.setTagsTaggedOn(SimpleSet.create(new XMLGregorianCalendarImpl(cal)));
						
						/* add tagging to response */
						resultset.add(tagging);
					}

					/* finally send the query */
					queryResponse.setResult(resultset);
					networkQuery.addResponse(queryResponse);
				}
			} catch (ErrorPerformingRequestException e) {
				/*
				 * happens on network failure for example
				 */
			} catch (QueryException e) {
				LOGGER.log(Level.SEVERE,"autogenerated catch-block", e);
			}

		}
	}
	
	/**
	 * Removes \n and \t characters
	 * @param string
	 * @return
	 */
	private String clean(String string) {
		return string.replaceAll("[\n\t]", " ");
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
					queryBibsonomy(networkQuery);
				} catch (RepositoryException e) {
					LOGGER.log(Level.SEVERE, "cannot store concepts", e);
				}
			}

		};

	}
}
