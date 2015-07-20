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
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * The Class BulkWriter.
 *
 * @author Sascha Just
 */
public class BulkWriter {
	
	/** The statement. */
	private PreparedStatement statement;
	
	/** The connection. */
	protected Connection      connection;
	
	/** The batch size. */
	protected int             batchSize;
	
	/** The writes. */
	protected int             writes = 0;
	
	protected String          query;
	
	/**
	 * Instantiates a new bulk writer.
	 *
	 * @param query
	 *            the query
	 * @param connection
	 *            the connection
	 */
	public BulkWriter(final String query, final Connection connection) {
		this(query, connection, 10000);
	}
	
	/**
	 * Instantiates a new bulk writer.
	 *
	 * @param query
	 *            the query
	 * @param connection
	 *            the connection
	 * @param batchSize
	 *            the batch size
	 */
	public BulkWriter(final String query, final Connection connection, final int batchSize) {
		try {
			this.batchSize = batchSize;
			this.connection = connection;
			this.connection.setAutoCommit(false);
			this.query = query;
			this.statement = this.connection.prepareStatement(this.query);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		if (!this.connection.isClosed()) {
			flush();
		}
	}
	
	/**
	 * Flush.
	 */
	public void flush() {
		if (this.writes > 0) {
			try {
				this.statement.executeBatch();
			} catch (final SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	/**
	 * Write.
	 *
	 * @param params
	 *            the params
	 */
	public void write(final Object... params) {
		try {
			for (int i = 0; i < params.length; ++i) {
				this.statement.setObject(i, params[i]);
			}
			this.statement.addBatch();
			if (++this.writes == this.batchSize) {
				this.statement.executeBatch();
			}
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
