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

package org.mozkito.libraries.sequel.legacy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.mozkito.libraries.logging.Logger;
import org.mozkito.libraries.sequel.IDumper;
import org.mozkito.libraries.sequel.IEntity;
import org.mozkito.libraries.sequel.MozkitoHandler;

/**
 * The Class DatabaseDumper.
 *
 * @author Sascha Just
 * @param <T>
 *            the generic type
 */
public class LegacyDumper<T extends IEntity> extends Thread implements IDumper<T> {
	
	/** The batch size. */
	private static int                     BATCH_SIZE = 10000;
	
	/** The adapter. */
	private final IAdapter<T>              adapter;
	
	/** The save. */
	private final PreparedStatement        save;
	
	/** The queue. */
	private final ConcurrentLinkedQueue<T> queue      = new ConcurrentLinkedQueue<>();
	
	/** The terminate. */
	private volatile boolean               terminate  = false;
	
	/** The processed. */
	private long                           processed  = 0;
	
	/**
	 * Instantiates a new database dumper.
	 */
	protected LegacyDumper() {
		this.adapter = null;
		this.save = null;
	}
	
	/**
	 * Instantiates a new database dumper.
	 *
	 * @param adapter
	 *            the adapter
	 * @param connection
	 *            the connection
	 */
	public LegacyDumper(final IAdapter<T> adapter, final Connection connection) {
		super(adapter.getClass().getSimpleName() + "->DatabaseDumper");
		Thread.setDefaultUncaughtExceptionHandler(new MozkitoHandler());
		
		this.adapter = adapter;
		this.save = adapter.prepareSaveStatement(connection);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.IDumper#run()
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
					this.adapter.save(this.save, entity.getId(), entity);
				} catch (final Throwable e) {
					Logger.error("Could not save '%s'.", entity);
					throw e;
				}
				if (counter % BATCH_SIZE == 0) {
					this.adapter.execute(this.save);
					counter = 0;
					this.save.getConnection().commit();
				}
			}
			
			Logger.info("Persisting remaining entities.");
			
			for (final T entity2 : this.queue) {
				++this.processed;
				this.adapter.save(this.save, entity2.getId(), entity2);
			}
			this.adapter.execute(this.save);
			Logger.info("Processed " + this.processed + " entities.");
			
			this.save.close();
			
			this.save.getConnection().commit();
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
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
