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
package de.jtheuer.diki.lib.query;

import javax.swing.SwingUtilities;

import org.paceproject.diki.elmo.QueryResponse;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * 
 * A {@link QueryResultListener} implementation suitable for swing applications.
 * It keeps track of Swing thread-saftey-issues by calling all gui operations
 * for the EDT (SwingUtilites.invokeLater())
 * 
 * Note that the saveFireNewResult method does NOT return the new results - it rather informs the GUI that new
 * results exist. The GUI is therefore responsible to re-render and retrieve the results.
 * 
 */
public abstract class SwingQueryResult extends AbstractQueryResult<Object> implements QueryResultListener<Object> {

	/**
	 * @param connection
	 */
	public SwingQueryResult() {}

	@Override
	public void fireNewResults(Object dummy) {
		SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					saveFireNewResult();
				}
			});
	}

	/**
	 * Fires a new {@link QueryResponse}. This method is guaranteed to be only
	 * called by the Swing EDT
	 */
	protected abstract void saveFireNewResult();

	@Override
	public abstract void updatePercentage(int percentage);

	@Override
	public abstract void fireResultFinished();

}
