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

import java.util.Date;

import del.icio.us.DeliciousUtils;

/**
 * Post
 *
 * @author David Czarnecki
 * @version $Id: Post.java,v 1.7 2007/01/19 00:14:43 czarneckid Exp $
 * @since 1.0
 */
public class Post {

    private String href;
    private String description;
    private String hash;
    private String tag;
    private String time;
    private String extended;
    private boolean shared;

    /**
     * Construct a new Post
     *
     * @param href        Link
     * @param description Description of link
     * @param hash        Hash of link
     * @param tag         Space-delimited set of tags
     * @param time        Time when link added
     * @param shared      Whether or not the post is shared
     */
    public Post(String href, String description, String extended, String hash, String tag, String time, boolean shared) {
        this.href = href;
        this.description = description;
        this.extended = extended;
        this.hash = hash;
        this.tag = tag;
        this.time = time;
        this.shared = shared;
    }

    /**
     * Get link of post
     *
     * @return Link
     */
    public String getHref() {
        return href;
    }

    /**
     * Set link for post
     *
     * @param href Link
     */
    public void setHref(String href) {
        this.href = href;
    }

    /**
     * Get description of link
     *
     * @return Description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set description for post
     *
     * @param description Description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get hash of link
     *
     * @return Hash
     */
    public String getHash() {
        return hash;
    }

    /**
     * Set hash for post
     *
     * @param hash Hash
     */
    public void setHash(String hash) {
        this.hash = hash;
    }

    /**
     * Get tag(s) for link
     *
     * @return Space-delimited set of tag(s) for link
     */
    public String getTag() {
        return tag;
    }

    /**
     * Set tag(s) for post
     *
     * @param tag Space-delimited set of tag(s) for link
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * Get time link posted
     *
     * @return Link posted time
     */
    public String getTime() {
        return time;
    }

    /**
     * Set time posted as UTC-time (yyyy-MM-ddTHH:mm:ssZ)
     *
     * @param time Posted time as UTC-time (yyyy-MM-ddTHH:mm:ssZ)
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * Return a {@link Date} from the UTC time for this post
     *
     * @return {@link Date} from the UTC time for this post
     *         or <code>null</code> if the time is not in proper UTC format
     */
    public Date getTimeAsDate() {
        return DeliciousUtils.getDateFromUTCString(time);
    }

    /**
     * Get extended description for post
     *
     * @return Extended description
     */
    public String getExtended() {
        return extended;
    }

    /**
     * Set extended description for post
     *
     * @param extended Extended description
     */
    public void setExtended(String extended) {
        this.extended = extended;
    }

    /**
     * Get whether or not the post is shared
     *
     * @return <code>true</code> if shared, <code>false</code> otherwise
     */
    public boolean isShared() {
        return shared;
    }

    /**
     * Set whether or not the post is shared
     *
     * @param shared <code>true</code> if shared, <code>false</code> otherwise
     */
    public void setShared(boolean shared) {
        this.shared = shared;
    }

    /**
     * Object as href:description:extended:hash:tag:time string
     *
     * @return href:description:extended:hash:tag:time
     */
    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append(href).append(":").append(description).append(":").append(extended).append(":").append(hash).append(":")
                .append(tag).append(":").append(time).append(":").append(shared);

        return result.toString();
    }

    /**
     * Split the tags
     *
     * @param splitRegex Split regular expression
     * @return Tags as <code>String[]</code>
     * @since 1.6
     */
    public String[] getTagsAsArray(String splitRegex) {
        return tag.split(splitRegex);
    }

    /**
     * Check to see if this {@link Post} object equals another (uses href for equality)
     *
     * @param obj {@link Post} object
     * @return <code>true</code> if the objects are equal, <code>false</code> otherwise
     */
    public boolean equals(Object obj) {
        if (obj instanceof Post) {
            return getHref().equals(((Post) obj).getHref());
        }

        return super.equals(obj);
    }
}