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
package del.icio.us.beans;

/**
 * Subscription
 *
 * @author David Czarnecki
 * @version $Id: Subscription.java,v 1.4 2007/01/19 00:14:43 czarneckid Exp $
 * @since 1.0
 */
public class Subscription {

    private String tag;
    private String user;

    /**
     * Construct a new subscription
     *
     * @param tag Tag
     * @param user Username
     */
    public Subscription(String tag, String user) {
        this.tag = tag;
        this.user = user;
    }

    /**
     * Get tag for subscription
     *
     * @return Tag
     */
    public String getTag() {
        return tag;
    }

    /**
     * Set tag for subscription
     *
     * @param tag Tag
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * Get user for subscription
     *
     * @return User
     */
    public String getUser() {
        return user;
    }

    /**
     * Set user for subscription
     *
     * @param user User
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Object as tag:user string
     *
     * @return tag:user
     */
    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append(tag).append(":").append(user);

        return result.toString();
    }
}