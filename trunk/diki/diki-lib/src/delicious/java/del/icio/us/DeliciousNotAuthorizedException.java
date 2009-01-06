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
 * Exception indicating failed authorization with del.icio.us service
 *
 * @author David Czarnecki
 * @version $Id: DeliciousNotAuthorizedException.java,v 1.3 2007/01/19 00:14:43 czarneckid Exp $
 * @since 1.4
 */
public class DeliciousNotAuthorizedException extends RuntimeException {

    /**
     * Construct a new not authorized exception with no message
     */
    public DeliciousNotAuthorizedException() {
    }

    /**
     * Construct a new not authorized exception with message
     *
     * @param s Error message to send with the exception
     */
    public DeliciousNotAuthorizedException(String s) {
        super(s);
    }
}