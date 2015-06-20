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

package org.mozkito.skeleton.sequel;

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
public class DatabaseDumper<T extends ISequelEntity> extends Thread {
	
	/** The batch size. */
	private static int                     BATCH_SIZE = 100000;
	
	/** The adapter. */
	private final ISequelAdapter<T>        adapter;
	
	/** The save. */
	private final PreparedStatement        save;
	
	/** The next id. */
	private final PreparedStatement        nextId;
	
	/** The queue. */
	private final ConcurrentLinkedQueue<T> queue      = new ConcurrentLinkedQueue<>();
	
	/** The terminate. */
	private volatile boolean               terminate  = false;
	
	/**
	 * Instantiates a new database dumper.
	 *
	 * @param adapter
	 *            the adapter
	 */
	public DatabaseDumper(final ISequelAdapter<T> adapter) {
		super(Thread.currentThread().getName() + "->DatabaseDumper");
		this.adapter = adapter;
		this.save = adapter.prepareSaveStatement();
		this.nextId = adapter.prepareNextIdStatement();
	}
	
	/**
	 * Next id.
	 *
	 * @return the object
	 */
	private long nextId() {
		return this.adapter.nextId(this.nextId);
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
						this.save.executeBatch();
						counter = 0;
						continue;
					}
					
					// still nothing, go to sleep for 10s
					try {
						Thread.sleep(10000);
					} catch (final InterruptedException e) {
						this.terminate = true;
					}
					continue;
				}
				++counter;
				try {
					this.adapter.save(this.save, entity.id(), entity);
				} catch (final Throwable e) {
					if (Logger.logError()) {
						Logger.error("Could not save '%s'.", entity);
					}
					throw e;
				}
				if (counter % BATCH_SIZE == 0) {
					this.save.executeBatch();
					counter = 0;
				}
			}
			
			if (Logger.logInfo()) {
				Logger.info("Persisting remaining entities.");
			}
			
			for (final T entity2 : this.queue) {
				this.adapter.save(this.save, entity2.id(), entity2);
			}
			
			this.save.executeBatch();
		} catch (final SQLException e) {
			if (Logger.logError()) {
				Logger.error(e);
			}
		}
	}
	
	/**
	 * Save later.
	 *
	 * @param entity
	 *            the entity
	 */
	public void saveLater(final T entity) {
		entity.id(nextId());
		this.queue.add(entity);
	}
	
	/**
	 * Terminate.
	 */
	public void terminate() {
		this.terminate = true;
	}
	
}
