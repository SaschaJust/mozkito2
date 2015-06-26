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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.mozkito.libraries.logging.Logger;

/**
 * The Class DatabaseDumper.
 *
 * @author Sascha Just
 * @param <T>
 *            the generic type
 */
public class DatabaseDumper<T extends IEntity> extends Thread {
	
	/** The batch size. */
	private static int                     BATCH_SIZE = 1000;
	
	/** The adapter. */
	private final IAdapter<T>              adapter;
	
	/** The save. */
	private final PreparedStatement        save;
	
	/** The queue. */
	private final ConcurrentLinkedQueue<T> queue      = new ConcurrentLinkedQueue<>();
	
	/** The terminate. */
	private volatile boolean               terminate  = false;
	
	private long                           processed  = 0;
	
	/**
	 * Instantiates a new database dumper.
	 *
	 * @param adapter
	 *            the adapter
	 * @param connection
	 *            the connection
	 */
	public DatabaseDumper(final IAdapter<T> adapter, final Connection connection) {
		super(adapter.getClass().getSimpleName() + "->DatabaseDumper");
		Thread.setDefaultUncaughtExceptionHandler(new MozkitoHandler());
		
		this.adapter = adapter;
		this.save = adapter.prepareSaveStatement(connection);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		try {
			int counter = 0;
			T entity;
			
			while (!this.terminate) {
				entity = this.queue.poll();
				if (entity == null) {
					// nothing to store right now. Persist pending stuff.
					if (counter > 0) {
						this.adapter.execute(this.save);
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
					this.adapter.save(this.save, entity.id(), entity);
				} catch (final Throwable e) {
					Logger.error("Could not save '%s'.", entity);
					throw e;
				}
				if (counter % BATCH_SIZE == 0) {
					this.adapter.execute(this.save);
					counter = 0;
				}
			}
			
			Logger.info("Persisting remaining entities.");
			
			for (final T entity2 : this.queue) {
				++this.processed;
				this.adapter.save(this.save, entity2.id(), entity2);
			}
			this.adapter.execute(this.save);
			Logger.info("Processed " + this.processed + " entities.");
			
			this.save.getConnection().close();
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Save later.
	 *
	 * @param entity
	 *            the entity
	 */
	public void saveLater(final T entity) {
		entity.id(this.adapter.nextId());
		this.queue.add(entity);
	}
	
	/**
	 * Terminate.
	 */
	public void terminate() {
		this.terminate = true;
	}
	
}
