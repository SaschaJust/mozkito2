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
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * The Class BulkWriter.
 *
 * @author Sascha Just
 */
public class BulkWriter implements IWriter {
	
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
	 * @see org.mozkito.libraries.sequel.bulk.IWriter#close()
	 */
	public void close() {
		try {
			this.statement.close();
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
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.bulk.IWriter#flush()
	 */
	@Override
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
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.bulk.IWriter#write(java.lang.Object[])
	 */
	@Override
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
