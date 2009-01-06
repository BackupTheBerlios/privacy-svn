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
package del.icio.us.tagext;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import del.icio.us.DeliciousException;
import del.icio.us.cache.DeliciousCache;

/**
 * JSP tag to get a list of recent posts from the del.icio.us service.
 *
 * @author Simon Brown
 * @version $Id: GetRecentPostsTag.java,v 1.5 2007/01/19 00:14:43 czarneckid Exp $
 */
public class GetRecentPostsTag extends TagSupport {

    /**
     * the name of the variable to inject back into the JSP page
     */
    private String var;

    /**
     * the del.icio.us username
     */
    private String username;

    /**
     * the del.icio.us password
     */
    private String password;

    /**
     * the count of recent posts to retrieve
     */
    private int count = 10;

    /**
     * a filtering tag (optional)
     */
    private String tag = null;

    /**
     * the timeout after which cached del.icio.us should be re-retrieved
     */
    private long timeout = THIRTY_MINUTES;

    private static final int THIRTY_MINUTES = 1000 * 60 * 30;

    /**
     * Called when the opening tag is encountered on the page.
     *
     * @return Tag.SKIP_BODY, to skip (empty) body execution
     * @throws JspException if something goes wrong
     */
    public int doStartTag() throws JspException {
        List posts = new ArrayList();
        try {
            posts = DeliciousCache.getInstance().getRecentPosts(username, password, count, tag, timeout);
        } catch (DeliciousException de) {
          // do nothing, the error has already been logged by the Delicious class
        }
        pageContext.setAttribute(var, posts, PageContext.PAGE_SCOPE);

        return Tag.EVAL_BODY_INCLUDE;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

}
