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
 * DeliciousDate
 *
 * @author David Czarnecki
 * @version $Id: DeliciousDate.java,v 1.4 2007/01/19 00:14:43 czarneckid Exp $
 * @since 1.0
 */
public class DeliciousDate {

    private int count;
    private String date;

    /**
     * Construct a new date
     *
     * @param count Number of links on date
     * @param date Date (should be yyyy-MM-dd format, but not enforced)
     */
    public DeliciousDate(int count, String date) {
        this.count = count;
        this.date = date;
    }

    /**
     * Get number of links for date
     *
     * @return Number of links for date
     */
    public int getCount() {
        return count;
    }

    /**
     * Set number of links for date
     *
     * @param count Number of links for date
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * Get date
     *
     * @return Date
     */
    public String getDate() {
        return date;
    }

    /**
     * Set date (should be yyyy-MM-dd format, but not enforced)
     *
     * @param date Date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Object as count:date string
     *
     * @return count:date
     */
    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append(count).append(":").append(date);

        return result.toString();
    }
}