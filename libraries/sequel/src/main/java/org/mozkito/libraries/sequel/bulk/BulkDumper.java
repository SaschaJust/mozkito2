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
				Logger.error("Could not save '%s'.", entity);
				throw e;
			}
		}
		
		Logger.info("Persisting remaining entities.");
		
		for (final T entity2 : this.queue) {
			++this.processed;
			this.adapter.save(entity2);
		}
		this.adapter.flush();
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
