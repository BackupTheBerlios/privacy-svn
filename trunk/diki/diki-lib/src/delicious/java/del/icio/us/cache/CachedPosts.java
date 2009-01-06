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

import java.util.ArrayList;
import java.util.List;

/**
 * A wrapper for posts back from the del.icio.us service.
 *
 * @author Simon Brown
 * @version $Id: CachedPosts.java,v 1.3 2007/01/19 00:14:43 czarneckid Exp $
 */
class CachedPosts {

    /**
     * the List of Post objects
     */
    private List posts;

    /**
     * the date/time that the posts were retrieved
     */
    private long date;

    /**
     * Creates a new instance.
     *
     * @param posts the List of Post objects
     */
    CachedPosts(List posts) {
        if (posts == null) {
            this.posts = new ArrayList();
        } else {
            this.posts = posts;
        }
        this.date = System.currentTimeMillis();
    }

    /**
     * Gets the collection of Post objects.
     *
     * @return a List of Post objects
     */
    List getPosts() {
        return new ArrayList(posts);
    }

    /**
     * Gets the collection of Post objects.
     *
     * @param count the number of posts to retrieve
     * @return a List of Post objects
     */
    List getPosts(int count) {
        if (count > 0 && posts.size() > count) {
            return posts.subList(0, count);
        } else {
            return getPosts();
        }
    }

    /**
     * Determines whether this list of posts has expired.
     *
     * @param timeout the timeout in milliseconds
     * @return true if the current time > posts retrieved time + timeout
     */
    boolean hasExpired(long timeout) {
        return System.currentTimeMillis() > (this.date + timeout);
    }
}
