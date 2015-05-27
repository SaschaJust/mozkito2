/***********************************************************************************************************************
 * Copyright 2015 mozkito.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 **********************************************************************************************************************/

package org.mozkito.libraries.sequel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

import org.mozkito.skeleton.contracts.Requires;

/**
 * The Class ResultIterator.
 *
 * @author Sascha Just
 * @param <T>
 *            the generic type
 */
public class ResultIterator<T extends ISequelEntity> implements Iterator<T> {
	
	/** The results. */
	private final ResultSet         results;
	
	/** The has next. */
	private boolean                 hasNext;
	
	/** The adapter. */
	private final ISequelAdapter<T> adapter;
	
	/**
	 * Instantiates a new result iterator.
	 *
	 * @param adapter
	 *            the adapter
	 * @param results
	 *            the results
	 */
	public ResultIterator(final ISequelAdapter<T> adapter, final ResultSet results) {
		Requires.notNull(adapter);
		Requires.notNull(results);
		
		this.adapter = adapter;
		this.results = results;
		try {
			this.hasNext = results.next();
		} catch (final SQLException e) {
			throw new RuntimeException("Failed fetching next set of data for adapter " + this.adapter, e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see java.util.Iterator#hasNext()
	 */
	@Override
	public boolean hasNext() {
		return this.hasNext;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see java.util.Iterator#next()
	 */
	@Override
	public T next() {
		try {
			final T result = this.adapter.create(this.results);
			this.hasNext = this.results.next();
			return result;
		} catch (final SQLException e) {
			throw new RuntimeException("Failed fetching next set of data for adapter " + this.adapter, e);
		}
	}
	
}
