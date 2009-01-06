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
package del.icio.us.cache;

import java.util.*;

import del.icio.us.Delicious;

/**
 * A cache for del.icio.us requests to get recent posts.
 *
 * @author Simon Brown
 * @version $Id: DeliciousCache.java,v 1.3 2007/01/19 00:14:43 czarneckid Exp $
 */
public class DeliciousCache {

    /**
     * the map of cached post lists
     */
    private Map map;

    /**
     * the singleton instance of this class
     */
    private static final DeliciousCache INSTANCE = new DeliciousCache();

    /**
     * the maximum number of recent posts
     */
    private static final int MAX_RECENT_POSTS = 100;

    /**
     * Gets the singleton instance.
     *
     * @return a DeliciousCache instance
     */
    public static DeliciousCache getInstance() {
        return INSTANCE;
    }

    /**
     * Creates a new instance, private to enfore singleton pattern.
     */
    private DeliciousCache() {
        this.map = new HashMap();
    }

    /**
     * Gets a list of recent posts for ths given criteria. If the posts that were
     * last retrieved are more than timeout milliseconds old, they are refreshed
     * from del.icio.us.
     *
     * @param username the del.icio.us username
     * @param password the del.icio.us password
     * @param count    the number of recent posts to get
     * @param tag      a filter tag (optional, can be null)
     * @param timeout  the timeout in milliseconds of the cached data
     * @return a Collection of Post objects
     */
    public synchronized List getRecentPosts(String username, String password, int count, String tag, long timeout) {
        CachedPosts posts = (CachedPosts) map.get(buildKey(username, tag));
        if (posts == null || posts.hasExpired(timeout)) {
            Delicious delicious = new Delicious(username, password);
            List coll = delicious.getRecentPosts(tag, MAX_RECENT_POSTS);
            posts = new CachedPosts(coll);
            map.put(buildKey(username, tag), posts);
        }

        return posts.getPosts(count);
    }

    /**
     * Builds a key to be used when storing/retrieving items from the cache, of
     * the form : username[/tag]
     *
     * @param username the del.icio.us username
     * @param tag      the filter tag
     * @return a unique string identifying a username and tag
     */
    private String buildKey(String username, String tag) {
        if (tag == null || tag.trim().length() == 0) {
            return username;
        } else {
            return username + "/" + tag;
        }
    }
}
