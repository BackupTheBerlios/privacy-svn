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
package del.icio.us;

/**
 * DeliciousConstants
 *
 * @author David Czarnecki
 * @version $Id: DeliciousConstants.java,v 1.19 2007/01/19 00:14:43 czarneckid Exp $
 * @since 1.0
 */
public class DeliciousConstants {

    // Default del.icio.us API endpoint
    public static final String API_ENDPOINT = "https://api.del.icio.us/v1/";

    public static final String USER_AGENT_HEADER = "User-Agent";
    public static final String USER_AGENT_VALUE = "del.icio.us Java/1.14 ex";

    // API service endpoints
    public static final String TAGS_GET = "tags/get?";
    public static final String POSTS_DATES = "posts/dates?";
    public static final String POSTS_RECENT = "posts/recent?";
    public static final String POSTS_ALL = "posts/all?";
    public static final String POSTS_GET = "posts/get?";
    public static final String POSTS_ADD = "posts/add?";
    public static final String POSTS_DELETE = "posts/delete?";
    public static final String POSTS_UPDATE = "posts/update?";
    public static final String TAGS_RENAME = "tags/rename?";
    public static final String INBOX_SUBS = "inbox/subs?";
    public static final String INBOX_SUB = "inbox/sub?";
    public static final String INBOX_UNSUB = "inbox/unsub?";
    public static final String INBOX_GET = "inbox/get?";
    public static final String INBOX_DATES = "inbox/dates?";
    public static final String BUNDLES_ALL = "tags/bundles/all?";
    public static final String BUNDLES_SET = "tags/bundles/set?";
    public static final String BUNDLES_DELETE = "tags/bundles/delete?";

    // Various URL parameters for sending data to del.icio.us
    public static final String TAG_PARAMETER = "tag";
    public static final String COUNT_PARAMETER = "count";
    public static final String DT_PARAMETER = "dt";
    public static final String URL_PARAMETER = "url";
    public static final String DESCRIPTION_PARAMETER = "description";
    public static final String EXTENDED_PARAMETER = "extended";
    public static final String TAGS_PARAMETER = "tags";
    public static final String OLD_PARAMETER = "old";
    public static final String NEW_PARAMETER = "new";
    public static final String USER_PARAMETER = "user";
    public static final String REPLACE_PARAMETER = "replace";
    public static final String SHARED_PARAMETER = "shared";
    public static final String NO = "no";
    public static final String YES = "yes"; 

    // Return codes from the service
    public static final String CODE_DONE = "done";
    public static final String CODE_OK = "<ok />";
    public static final String CODE_OK_STANDALONE = "ok";

    // <post .../> tag and attributes
    public static final String POSTS_TAG = "posts";
    public static final String POST_TAG = "post";
    public static final String HREF_ATTRIBUTE = "href";
    public static final String DESCRIPTION_ATTRIBUTE = "description";
    public static final String HASH_ATTRIBUTE = "hash";
    public static final String TAG_ATTRIBUTE = "tag";
    public static final String TIME_ATTRIBUTE = "time";
    public static final String EXTENDED_ATTRIBUTE = "extended";
    public static final String UPDATE_ATTRIBUTE = "update";

    // <date .../> tag and attributes
    public static final String DATE_TAG = "date";
    public static final String COUNT_ATTRIBUTE = "count";
    public static final String DATE_ATTRIBUTE = "date";

    // <sub .../> tag and attributes
    public static final String SUB_TAG = "sub";
    public static final String USER_ATTRIBUTE = "user";

    // <tag .../> tag and attributes
    public static final String TAG_TAG = "tag";

    // <update .../> tag and attributes
    public static final String UPDATE_TAG = "update";

    // <bundle .../> tag and attributes
    public static final String BUNDLE_TAG = "bundle";
    public static final String BUNDLE_PARAMETER = "bundle";
    public static final String BUNDLE_NAME_ATTRIBUTE = "name";
    public static final String BUNDLE_TAGS_ATTRIBUTE = "tags";

    public static final int DEFAULT_POST_COUNT = 15;
    public static final int MAXIMUM_POST_COUNT = 100;
}