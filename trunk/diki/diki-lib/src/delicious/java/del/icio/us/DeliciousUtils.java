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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * DeliciousUtilities
 *
 * @author David Czarnecki
 * @version $Id: DeliciousUtils.java,v 1.8 2007/01/19 00:14:43 czarneckid Exp $
 * @since 1.0
 */
public class DeliciousUtils {

    public static final String UTF_8 = "UTF-8";
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
    public static final String UTC_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String DELICIOUS_DATE_FORMAT = "yyyy-MM-dd";

    /**
     * UTC format
     * <p/>
     * SimpleDateFormats are not threadsafe, but we should not need more than one per
     * thread.
     */
    private static final ThreadLocal UTC_DATE_FORMAT_OBJECT = new ThreadLocal() {
        protected Object initialValue() {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(UTC_DATE_FORMAT);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            return simpleDateFormat;
        }
    };

    /**
     * del.icio.us date format (yyyy-MM-dd)
     * SimpleDateFormats are not threadsafe, but we should not need more than one per
     * thread.
     */
    private static final ThreadLocal DELICIOUS_DATE_FORMAT_OBJECT = new ThreadLocal() {
        protected Object initialValue() {
            return new SimpleDateFormat(DELICIOUS_DATE_FORMAT);
        }
    };

    /**
     * Return a date in UTC style
     *
     * @param date Date
     * @return Date formatted as ISO 8601
     */
    public static String getUTCDate(Date date) {
        return ((SimpleDateFormat) UTC_DATE_FORMAT_OBJECT.get()).format(date);
    }

    /**
     * Return a date in del.icio.us style (yyyy-MM-dd)
     *
     * @param date Date
     * @return Date formatted as yyyy-MM-dd
     */
    public static String getDeliciousDate(Date date) {
        return ((SimpleDateFormat) DELICIOUS_DATE_FORMAT_OBJECT.get()).format(date);
    }

    /**
     * Parse a date from a UTC string
     *
     * @param time Input string of form yyyy-MM-ddTHH:mm:ssZ
     * @return Date parsed from UTC string or <code>null</code> if error in
     *         parsing
     */
    public static Date getDateFromUTCString(String time) {
        Date result = null;

        try {
            ((SimpleDateFormat) UTC_DATE_FORMAT_OBJECT.get()).setCalendar(Calendar.getInstance(TimeZone.getTimeZone("UTC")));
            result = ((SimpleDateFormat) UTC_DATE_FORMAT_OBJECT.get()).parse(time);
        } catch (ParseException e) {
        }

        return result;
    }

    /**
     * Check to see if the input is <code>null</code> or blank
     *
     * @param input Input
     * @return <code>true</code> if input is null or blank, <code>false</code> otherwise
     */
    public static boolean checkNullOrBlank(String input) {
        return (input == null || "".equals(input));
    }

    /**
     * Encode input using UTF-8
     *
     * @param input Input
     * @return Input encoded using UTF-8 or <code>null</code> if input was null
     */
    public static String encodeUTF8(String input) {
        return encodeUTF8(input, false);
    }

    /**
     * Encod input as UTF-8 while converting %20 (space) to a space
     *
     * @param input Input
     * @param keepSpaces <code>true</code> if spaces should be preserved, <code>false</code> otherwise
     * @return Input encoded using UTF-8 or <code>null</code> if input was null
     */
    public static String encodeUTF8(String input, boolean keepSpaces) {
        if (input == null) {
            return null;
        }

        String encodedInput;

        try {
             encodedInput = URLEncoder.encode(input, UTF_8);
        } catch (UnsupportedEncodingException e) {
            return input;
        }

        if (keepSpaces) {
            encodedInput = encodedInput.replaceAll("[+]", " ");
        }

        return encodedInput;
    }

    /**
     * Encode input with only for ?, &amp;, and # characters
     *
     * @param input Input
     * @return Input encoded changing ? to %3F, &amp; to %26, and # to %23
     * @since 1.3
     */
    public static String encodeURLForDelicious(String input) {
        if (input == null) {
            return null;
        }

        String encodedInput;
        encodedInput = input.replaceAll("\\?", "%3F");
        encodedInput = input.replaceAll("&", "%26");
        encodedInput = input.replaceAll("#", "%23");

        return encodedInput;
    }
}