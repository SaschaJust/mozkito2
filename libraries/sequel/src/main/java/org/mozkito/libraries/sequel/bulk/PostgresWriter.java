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

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.util.regex.Pattern;

import org.postgresql.PGConnection;
import org.postgresql.copy.CopyIn;
import org.postgresql.copy.CopyManager;

import org.mozkito.libraries.logging.Logger;
import org.mozkito.skeleton.contracts.Requires;

/**
 * The Class PostgresWriter.
 *
 * @author Sascha Just
 */
public class PostgresWriter implements IWriter {
	
	/** The Constant TAB_STRING. */
	private static final String  TAB_STRING           = "\\t";
	
	/** The Constant NEWLINE_STRING. */
	private static final String  NEWLINE_STRING       = "\\n";
	
	/** The Constant BACKSPACE_STRING. */
	private static final String  BACKSPACE_STRING     = "\\b";
	
	/** The null string. */
	private static final String  NULL_STRING          = "\\N";
	
	/** The Constant QUOTE_STRING. */
	private static final String  QUOTE_STRING         = "\"";
	
	/** The Constant ESCAPED_QUOTE_STRING. */
	private static final String  ESCAPED_QUOTE_STRING = QUOTE_STRING + QUOTE_STRING;
	
	/** The Constant BACKSPACE_PATTERN. */
	private static final Pattern BACKSPACE_PATTERN    = Pattern.compile("\\", Pattern.LITERAL);
	
	/** The Constant TAB_PATTERN. */
	private static final Pattern TAB_PATTERN          = Pattern.compile("\t", Pattern.LITERAL);
	
	/** The Constant NEWLINE_PATTERN. */
	private static final Pattern NEWLINE_PATTERN      = Pattern.compile(System.lineSeparator(), Pattern.LITERAL);
	
	/** The Constant QUOTE_PATTERN. */
	private static final Pattern QUOTE_PATTERN        = Pattern.compile(QUOTE_STRING, Pattern.LITERAL);
	
	/** The builder. */
	private final StringBuilder  builder;
	
	/** The manager. */
	private CopyManager          manager;
	
	/** The last flush. */
	private int                  lastFlush            = 0;
	
	/** The is constructing. */
	private boolean              isConstructing;
	
	/** The copy in. */
	private CopyIn               copyIn;
	
	/** The statement string. */
	private final String         statementString;
	
	/** The writes. */
	private int                  writes               = 0;
	
	/** The batch size. */
	private final int            batchSize;
	
	/** The delimiter char. */
	private final char           delimiterChar        = '\t';
	
	/** The spent. */
	Duration                     spent                = Duration.ZERO;
	
	private Connection           connection;
	
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
		this.builder = new StringBuilder();
		this.statementString = query;
		this.batchSize = batchSize;
		
		try {
			this.manager = ((PGConnection) connection).getCopyAPI();
			this.connection = connection;
			this.copyIn = this.manager.copyIn(this.statementString);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Close.
	 */
	public void close() {
		try {
			flush();
			this.copyIn.flushCopy();
			this.copyIn.endCopy();
			this.connection.commit();
			this.connection.close();
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
			if (this.writes > this.lastFlush) {
				this.builder.delete(this.builder.length() - 1, this.builder.length());
				final byte[] bytes = this.builder.toString().getBytes("UTF-8");
				this.copyIn.writeToCopy(bytes, 0, bytes.length);
				this.copyIn.endCopy();
				this.copyIn = this.manager.copyIn(this.statementString);
				this.builder.delete(0, this.builder.length());
				this.lastFlush = this.writes;
			}
		} catch (final SQLException | UnsupportedEncodingException e) {
			Logger.error("Writing " + (this.writes - this.lastFlush) + " entries failed: " + System.lineSeparator()
			        + this.builder.toString());
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
		String entry;
		
		for (final Object param : params) {
			if (this.isConstructing) {
				this.builder.append(this.delimiterChar);
			} else {
				this.isConstructing = true;
			}
			
			if (param == null) {
				entry = NULL_STRING;
			} else {
				entry = param.toString();
				entry = BACKSPACE_PATTERN.matcher(entry).replaceAll(BACKSPACE_STRING);
				entry = TAB_PATTERN.matcher(entry).replaceAll(TAB_STRING);
				entry = NEWLINE_PATTERN.matcher(entry).replaceAll(NEWLINE_STRING);
				entry = QUOTE_PATTERN.matcher(entry).replaceAll(ESCAPED_QUOTE_STRING);
				entry = QUOTE_STRING + entry + QUOTE_STRING;
			}
			this.builder.append(entry);
		}
		this.builder.append('\n');
		this.isConstructing = false;
		
		if ((++this.writes - this.lastFlush) % this.batchSize == 0) {
			flush();
		}
	}
}
