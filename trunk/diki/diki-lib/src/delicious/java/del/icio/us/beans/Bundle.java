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
 * Bundle
 *
 * @author David Czarnecki
 * @version $Id: Bundle.java,v 1.6 2007/01/19 00:14:43 czarneckid Exp $
 * @since 1.9
 */
public class Bundle {

    private String name;
    private String tags;

    /**
     * Create a new bundle with a bundle name and a space separated list of tags
     *
     * @param name Bundle name
     * @param tags List of tags
     */
    public Bundle(String name, String tags) {
        this.name = name;
        this.tags = tags;
    }

    /**
     * Get the name of this bundle
     *
     * @return Name of this bundle
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of this bundle
     *
     * @param name Name of this bundle
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the space separated list of tags for this bundle
     *
     * @return Space separated list of tags
     */
    public String getTags() {
        return tags;
    }

    /**
     * Set the list of tags for this bundle
     *
     * @param tags List of tags for this bundle
     */
    public void setTags(String tags) {
        this.tags = tags;
    }

    /**
     * Get the list of tags as a <code>String[]</code>
     *
     * @return List of tags as a <code>String []</code>
     */
    public String[] getTagsAsArray() {
        if (tags == null) {
            return new String[0];
        }

        return tags.split(" ");
    }

    /**
     * Object as name:tags string
     *
     * @return name:tags
     */
    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append(name).append(":").append(tags);

        return result.toString();
    }
}
