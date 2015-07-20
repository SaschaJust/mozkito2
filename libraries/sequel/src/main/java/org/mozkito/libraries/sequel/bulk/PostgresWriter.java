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
import java.sql.SQLException;

import org.postgresql.PGConnection;
import org.postgresql.copy.CopyIn;
import org.postgresql.copy.CopyManager;

import org.mozkito.skeleton.contracts.Requires;

/**
 * The Class PostgresWriter.
 *
 * @author Sascha Just
 */
public class PostgresWriter extends BulkWriter {
	
	/** The builder. */
	private final StringBuilder builder;
	
	/** The manager. */
	private CopyManager         manager;
	
	/** The last flush. */
	private int                 lastFlush = 0;
	
	/** The is constructing. */
	private boolean             isConstructing;
	
	/** The copy in. */
	private CopyIn              copyIn;
	
	/**
	 * Instantiates a new postgres writer.
	 *
	 * @param query
	 *            the query
	 * @param connection
	 *            the connection
	 */
	public PostgresWriter(final String query, final Connection connection) {
		this(query, connection, 10000);
		// COPY measurement FROM STDIN WITH CSV
	}
	
	/**
	 * Instantiates a new postgres writer.
	 *
	 * @param query
	 *            the query
	 * @param connection
	 *            the connection
	 * @param batchSize
	 *            the batch size
	 */
	public PostgresWriter(final String query, final Connection connection, final int batchSize) {
		super(query, connection, batchSize);
		this.builder = new StringBuilder();
		
		try {
			this.manager = ((PGConnection) connection).getCopyAPI();
			this.copyIn = this.manager.copyIn(query);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.bulk.BulkWriter#flush()
	 */
	@Override
	public void flush() {
		try {
			this.copyIn.writeToCopy(this.builder.toString().getBytes(), 0, this.builder.length());
			this.copyIn.endCopy();
			this.copyIn = this.manager.copyIn(this.query);
			this.builder.delete(0, this.builder.length());
			this.lastFlush = this.writes;
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.bulk.BulkWriter#write(java.lang.Object[])
	 */
	@Override
	public void write(final Object... params) {
		Requires.notNull(params);
		
		this.isConstructing = false;
		
		for (final Object param : params) {
			if (this.isConstructing) {
				this.builder.append(',');
			} else {
				this.isConstructing = true;
			}
			this.builder.append(param.toString());
		}
		this.builder.append('\n');
		this.isConstructing = false;
		
		if ((++this.writes - this.lastFlush) % this.batchSize == 0) {
			flush();
		}
	}
}
