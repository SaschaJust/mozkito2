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

package org.mozkito.libraries.sequel.bulk;

import java.sql.Connection;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.mozkito.libraries.logging.Logger;
import org.mozkito.libraries.sequel.IDumper;
import org.mozkito.libraries.sequel.IEntity;

/**
 * The Class PostgresDumper.
 *
 * @author Sascha Just
 * @param <T>
 *            the generic type
 */
public class BulkDumper<T extends IEntity> extends Thread implements IDumper<T> {
	
	/** The queue. */
	private final ConcurrentLinkedQueue<T> queue     = new ConcurrentLinkedQueue<>();
	
	/** The terminate. */
	private volatile boolean               terminate = false;
	
	/** The processed. */
	private long                           processed = 0;
	
	/** The adapter. */
	private final IAdapter<T>              adapter;
	
	/**
	 * Instantiates a new postgres dumper.
	 *
	 * @param adapter
	 *            the adapter
	 * @param connection
	 *            the connection
	 */
	public BulkDumper(final IAdapter<T> adapter, final Connection connection) {
		super(adapter.getClass().getSimpleName() + "->" + BulkDumper.class.getSimpleName());
		this.adapter = adapter;
		adapter.init(connection);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		int counter = 0;
		T entity;
		
		while (!this.terminate) {
			entity = this.queue.poll();
			if (entity == null) {
				// nothing to store right now. Persist pending stuff.
				if (counter > 0) {
					this.adapter.flush();
					counter = 0;
					continue;
				}
				
				// still nothing, go to sleep for 10s
				try {
					Thread.sleep(5000);
				} catch (final InterruptedException e) {
					this.terminate = true;
				}
				continue;
			}
			++counter;
			try {
				++this.processed;
				this.adapter.save(entity);
			} catch (final Throwable e) {
				Logger.error(e, "Could not save '%s'.", entity);
				throw e;
			}
		}
		
		Logger.info("Persisting remaining entities.");
		
		for (final T entity2 : this.queue) {
			++this.processed;
			this.adapter.save(entity2);
		}
		this.adapter.close();
		Logger.info("Processed " + this.processed + " entities.");
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.IDumper#saveLater(org.mozkito.libraries.sequel.IEntity)
	 */
	@Override
	public void saveLater(final T entity) {
		entity.setId(this.adapter.nextId());
		this.queue.add(entity);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.IDumper#terminate()
	 */
	@Override
	public void terminate() {
		this.terminate = true;
	}
	
}
