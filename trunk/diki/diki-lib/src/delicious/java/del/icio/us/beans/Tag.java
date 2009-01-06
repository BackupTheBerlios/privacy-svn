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
 * Tag
 * 
 * @author David Czarnecki
 * @version $Id: Tag.java,v 1.4 2007/01/19 00:14:43 czarneckid Exp $
 * @since 1.0
 */
public class Tag {

    private int count;
    private String tag;

    /**
     * Construct a new tag
     *
     * @param count Number of links for tag
     * @param tag Tag name
     */
    public Tag(int count, String tag) {
        this.count = count;
        this.tag = tag;
    }

    /**
     * Get number of links for tag
     *
     * @return Number of links for tag
     */
    public int getCount() {
        return count;
    }

    /**
     * Set number of links for tag
     *
     * @param count Number of links for tag
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * Get tag name
     *
     * @return Tag name
     */
    public String getTag() {
        return tag;
    }

    /**
     * Set tag name
     *
     * @param tag Tag name
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * Object as count:tag string
     *
     * @return count:tag
     */
    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append(count).append(":").append(tag);

        return result.toString();
    }
}