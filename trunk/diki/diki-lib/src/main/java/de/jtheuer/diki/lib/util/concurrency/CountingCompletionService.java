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
package de.jtheuer.diki.lib.util.concurrency;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * 
 * Do NOT use poll! Results are undefined!
 */
public class CountingCompletionService<V> extends ExecutorCompletionService<V> {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(CountingCompletionService.class.getName());

	AtomicInteger counter_submitted = new AtomicInteger();
	AtomicInteger counter_taken = new AtomicInteger();

	/**
	 * @param executor
	 */
	public CountingCompletionService(Executor executor) {
		super(executor);
	}

	@Override
	public Future<V> submit(Callable<V> task) {
		counter_submitted.incrementAndGet();
		return super.submit(task);
	}

	@Override
	public Future<V> submit(Runnable task, V result) {
		counter_submitted.incrementAndGet();
		return super.submit(task, result);
	}

	@Override
	public Future<V> take() throws InterruptedException {
		counter_taken.incrementAndGet();
		return super.take();
	}

	public boolean isEmpty() {
		return counter_submitted.intValue() == counter_taken.intValue();
	}

	/**
	 * How many tasks are left
	 * 
	 * @return submitted tasks - taken tasks
	 */
	public int size() {
		return counter_submitted.intValue() - counter_taken.intValue();
	}

	public int percent() {
		return counter_submitted.intValue() == 0 ? 100 : (counter_taken.intValue() * 100) / counter_submitted.intValue();
	}
	
	@Override
	public String toString() {
		return "submitted: "+ counter_submitted.intValue() + "  taken "+  counter_taken.intValue() + " percent: " + percent();
	}

}
