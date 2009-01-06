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
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 * A {@link QueryExecutor} is responsible for handling Queries and collecting responses.
 * It also manages an {@link ExecutorService} in case that new Threads are necessary to solve 
 * the query.
 */
public class QueryExecutor implements Executor {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(QueryExecutor.class.getName());

	private boolean finished=false;
	
	/* we need atomic integers to ensure thread safety */
	protected AtomicInteger solvercounter = new AtomicInteger(0);
	protected AtomicInteger solvedcounter = new AtomicInteger(0);

	private ExecutorService executor;
	private QueryResultListener<?> listener;
	
	
	/**
	 * A query must have a query id!
	 * @param query
	 * @param result
	 * @param executor
	 */
	public QueryExecutor(ExecutorService executor,QueryResultListener<?> listener) {
		this.executor = executor;
		this.listener = listener;
	}
	
	/**
	 * executes the given {@link Runnable} in the context of this container.
	 * That means, the container stores, when the query has been finished.
	 */
	public void execute(Runnable runnable) {
		if(runnable != null) { 
			executor.execute(new InnerRunnable(runnable));
		}
	}
	
	protected class InnerRunnable implements Runnable {
		
		Runnable inner;
		
		/**
		 * @param inner the {@link Runnable} that will be started
		 */
		public InnerRunnable(Runnable inner) {
			this.inner = inner;
		}

		@Override
		public void run() {
			try{
				solvercounter.incrementAndGet();
				//LOGGER.info("Starting thread " + i + ":" + inner.toString());
				inner.run();
			} catch(Exception e) {
				LOGGER.log(Level.WARNING,e.getMessage(),e);
			} finally {
				solvedcounter.incrementAndGet();
				checkFinished();
			}
		}
		
	}

	public boolean isFinished() {
		return finished;
	}
	
	private synchronized void checkFinished() {
		listener.updatePercentage((solvedcounter.intValue()*100) / solvercounter.intValue());
		if(solvedcounter.intValue()==solvercounter.intValue()) {
			listener.fireResultFinished();
			finished = true;
		}
	}

}
