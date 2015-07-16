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

/**
 * The Class BulkWriter.
 *
 * @author Sascha Just
 */
public class BulkWriter {
	
	/** The statement. */
	private PreparedStatement statement;
	
	/** The connection. */
	private Connection        connection;
	
	/** The batch size. */
	private int               batchSize;
	
	/** The writes. */
	private int               writes = 0;
	
	/**
	 * Instantiates a new bulk writer.
	 *
	 * @param query
	 *            the query
	 * @param database
	 *            the database
	 */
	public BulkWriter(final String query, final Database database) {
		this(query, database, 1000);
	}
	
	/**
	 * Instantiates a new bulk writer.
	 *
	 * @param query
	 *            the query
	 * @param database
	 *            the database
	 * @param batchSize
	 *            the batch size
	 */
	public BulkWriter(final String query, final Database database, final int batchSize) {
		try {
			this.batchSize = batchSize;
			this.connection = database.getConnection();
			this.connection.setAutoCommit(false);
			this.statement = this.connection.prepareStatement(query);
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
