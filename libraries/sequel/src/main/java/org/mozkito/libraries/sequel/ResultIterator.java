/***********************************************************************************************************************
 * MIT License
 *  
 * Copyright (c) 2015 mozkito.org
 *  
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *  
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *  
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * 
 **********************************************************************************************************************/

package org.mozkito.libraries.sequel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

import org.mozkito.libraries.sequel.legacy.IAdapter;
import org.mozkito.skeleton.contracts.Requires;

/**
 * The Class ResultIterator.
 *
 * @author Sascha Just
 * @param <T>
 *            the generic type
 */
public class ResultIterator<T extends IEntity> implements Iterator<T> {
	
	/** The results. */
	private final ResultSet         results;
	
	/** The has next. */
	private boolean                 hasNext;
	
	/** The adapter. */
	private final IAdapter<T> adapter;
	
	/**
	 * Instantiates a new result iterator.
	 *
	 * @param adapter
	 *            the adapter
	 * @param results
	 *            the results
	 */
	public ResultIterator(final IAdapter<T> adapter, final ResultSet results) {
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
