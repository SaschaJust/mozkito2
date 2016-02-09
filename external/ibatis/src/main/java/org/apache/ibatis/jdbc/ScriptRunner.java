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

package org.apache.ibatis.jdbc;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * The Class ScriptRunner.
 *
 * @author Clinton Begin
 */
public class ScriptRunner {
	
	/** The Constant LINE_SEPARATOR. */
	private static final String LINE_SEPARATOR    = System.getProperty("line.separator", "\n");
	
	/** The Constant DEFAULT_DELIMITER. */
	private static final String DEFAULT_DELIMITER = ";";
	
	/** The connection. */
	private final Connection    connection;
	
	/** The stop on error. */
	private boolean             stopOnError;
	
	/** The auto commit. */
	private boolean             autoCommit;
	
	/** The send full script. */
	private boolean             sendFullScript;
	
	/** The remove c rs. */
	private boolean             removeCRs;
	
	/** The escape processing. */
	private boolean             escapeProcessing  = true;
	
	/** The log writer. */
	private PrintWriter         logWriter         = new PrintWriter(System.out);
	
	/** The error log writer. */
	private PrintWriter         errorLogWriter    = new PrintWriter(System.err);
	
	/** The delimiter. */
	private String              delimiter         = DEFAULT_DELIMITER;
	
	/** The full line delimiter. */
	private boolean             fullLineDelimiter = false;
	
	/**
	 * Instantiates a new script runner.
	 *
	 * @param connection
	 *            the connection
	 */
	public ScriptRunner(final Connection connection) {
		this.connection = connection;
	}
	
	/**
	 * Check for missing line terminator.
	 *
	 * @param command
	 *            the command
	 */
	private void checkForMissingLineTerminator(final StringBuilder command) {
		if (command != null && command.toString().trim().length() > 0) {
			throw new RuntimeSqlException("Line missing end-of-line terminator (" + this.delimiter + ") => " + command);
		}
	}
	
	/**
	 * Close connection.
	 */
	public void closeConnection() {
		try {
			this.connection.close();
		} catch (final Exception e) {
			// ignore
		}
	}
	
	/**
	 * Command ready to execute.
	 *
	 * @param trimmedLine
	 *            the trimmed line
	 * @return true, if successful
	 */
	private boolean commandReadyToExecute(final String trimmedLine) {
		// issue #561 remove anything after the delimiter
		return !this.fullLineDelimiter && trimmedLine.contains(this.delimiter) || this.fullLineDelimiter
		        && trimmedLine.equals(this.delimiter);
	}
	
	/**
	 * Commit connection.
	 */
	private void commitConnection() {
		try {
			if (!this.connection.getAutoCommit()) {
				this.connection.commit();
			}
		} catch (final Throwable t) {
			throw new RuntimeSqlException("Could not commit transaction. Cause: " + t, t);
		}
	}
	
	/**
	 * Execute full script.
	 *
	 * @param reader
	 *            the reader
	 */
	private void executeFullScript(final Reader reader) {
		final StringBuilder script = new StringBuilder();
		try {
			final BufferedReader lineReader = new BufferedReader(reader);
			String line;
			while ((line = lineReader.readLine()) != null) {
				script.append(line);
				script.append(LINE_SEPARATOR);
			}
			final String command = script.toString();
			println(command);
			executeStatement(command);
			commitConnection();
		} catch (final Exception e) {
			final String message = "Error executing: " + script + ".  Cause: " + e;
			printlnError(message);
			throw new RuntimeSqlException(message, e);
		}
	}
	
	/**
	 * Execute line by line.
	 *
	 * @param reader
	 *            the reader
	 */
	private void executeLineByLine(final Reader reader) {
		StringBuilder command = new StringBuilder();
		try {
			final BufferedReader lineReader = new BufferedReader(reader);
			String line;
			while ((line = lineReader.readLine()) != null) {
				command = handleLine(command, line);
			}
			commitConnection();
			checkForMissingLineTerminator(command);
		} catch (final Exception e) {
			final String message = "Error executing: " + command + ".  Cause: " + e;
			printlnError(message);
			throw new RuntimeSqlException(message, e);
		}
	}
	
	/**
	 * Execute statement.
	 *
	 * @param command
	 *            the command
	 * @throws SQLException
	 *             the SQL exception
	 */
	private void executeStatement(final String command) throws SQLException {
		boolean hasResults = false;
		final Statement statement = this.connection.createStatement();
		statement.setEscapeProcessing(this.escapeProcessing);
		String sql = command;
		if (this.removeCRs) {
			sql = sql.replaceAll("\r\n", "\n");
		}
		if (this.stopOnError) {
			hasResults = statement.execute(sql);
		} else {
			try {
				hasResults = statement.execute(sql);
			} catch (final SQLException e) {
				final String message = "Error executing: " + command + ".  Cause: " + e;
				printlnError(message);
			}
		}
		printResults(statement, hasResults);
		try {
			statement.close();
		} catch (final Exception e) {
			// Ignore to workaround a bug in some connection pools
		}
	}
	
	/**
	 * Handle line.
	 *
	 * @param command
	 *            the command
	 * @param line
	 *            the line
	 * @return the string builder
	 * @throws SQLException
	 *             the SQL exception
	 * @throws UnsupportedEncodingException
	 *             the unsupported encoding exception
	 */
	private StringBuilder handleLine(final StringBuilder command,
	                                 final String line) throws SQLException, UnsupportedEncodingException {
		final String trimmedLine = line.trim();
		if (lineIsComment(trimmedLine)) {
			final String cleanedString = trimmedLine.substring(2).trim().replaceFirst("//", "");
			if (cleanedString.toUpperCase().startsWith("@DELIMITER")) {
				this.delimiter = cleanedString.substring(11, 12);
				return command;
			}
			println(trimmedLine);
		} else if (commandReadyToExecute(trimmedLine)) {
			command.append(line.substring(0, line.lastIndexOf(this.delimiter)));
			command.append(LINE_SEPARATOR);
			println(command);
			executeStatement(command.toString());
			command.setLength(0);
		} else if (trimmedLine.length() > 0) {
			command.append(line);
			command.append(LINE_SEPARATOR);
		}
		return command;
	}
	
	/**
	 * Line is comment.
	 *
	 * @param trimmedLine
	 *            the trimmed line
	 * @return true, if successful
	 */
	private boolean lineIsComment(final String trimmedLine) {
		return trimmedLine.startsWith("//") || trimmedLine.startsWith("--");
	}
	
	/**
	 * Prints the.
	 *
	 * @param o
	 *            the o
	 */
	private void print(final Object o) {
		if (this.logWriter != null) {
			this.logWriter.print(o);
			this.logWriter.flush();
		}
	}
	
	/**
	 * Println.
	 *
	 * @param o
	 *            the o
	 */
	private void println(final Object o) {
		if (this.logWriter != null) {
			this.logWriter.println(o);
			this.logWriter.flush();
		}
	}
	
	/**
	 * Println error.
	 *
	 * @param o
	 *            the o
	 */
	private void printlnError(final Object o) {
		if (this.errorLogWriter != null) {
			this.errorLogWriter.println(o);
			this.errorLogWriter.flush();
		}
	}
	
	/**
	 * Prints the results.
	 *
	 * @param statement
	 *            the statement
	 * @param hasResults
	 *            the has results
	 */
	private void printResults(final Statement statement,
	                          final boolean hasResults) {
		try {
			if (hasResults) {
				final ResultSet rs = statement.getResultSet();
				if (rs != null) {
					final ResultSetMetaData md = rs.getMetaData();
					final int cols = md.getColumnCount();
					for (int i = 0; i < cols; i++) {
						final String name = md.getColumnLabel(i + 1);
						print(name + "\t");
					}
					println("");
					while (rs.next()) {
						for (int i = 0; i < cols; i++) {
							final String value = rs.getString(i + 1);
							print(value + "\t");
						}
						println("");
					}
				}
			}
		} catch (final SQLException e) {
			printlnError("Error printing results: " + e.getMessage());
		}
	}
	
	/**
	 * Rollback connection.
	 */
	private void rollbackConnection() {
		try {
			if (!this.connection.getAutoCommit()) {
				this.connection.rollback();
			}
		} catch (final Throwable t) {
			// ignore
		}
	}
	
	/**
	 * Run script.
	 *
	 * @param reader
	 *            the reader
	 */
	public void runScript(final Reader reader) {
		setAutoCommit();
		
		try {
			if (this.sendFullScript) {
				executeFullScript(reader);
			} else {
				executeLineByLine(reader);
			}
		} finally {
			rollbackConnection();
		}
	}
	
	/**
	 * Sets the auto commit.
	 */
	private void setAutoCommit() {
		try {
			if (this.autoCommit != this.connection.getAutoCommit()) {
				this.connection.setAutoCommit(this.autoCommit);
			}
		} catch (final Throwable t) {
			throw new RuntimeSqlException("Could not set AutoCommit to " + this.autoCommit + ". Cause: " + t, t);
		}
	}
	
	/**
	 * Sets the auto commit.
	 *
	 * @param autoCommit
	 *            the new auto commit
	 */
	public void setAutoCommit(final boolean autoCommit) {
		this.autoCommit = autoCommit;
	}
	
	/**
	 * Sets the delimiter.
	 *
	 * @param delimiter
	 *            the new delimiter
	 */
	public void setDelimiter(final String delimiter) {
		this.delimiter = delimiter;
	}
	
	/**
	 * Sets the error log writer.
	 *
	 * @param errorLogWriter
	 *            the new error log writer
	 */
	public void setErrorLogWriter(final PrintWriter errorLogWriter) {
		this.errorLogWriter = errorLogWriter;
	}
	
	/**
	 * Sets the escape processing.
	 *
	 * @param escapeProcessing
	 *            the new escape processing
	 * @since 3.1.1
	 */
	public void setEscapeProcessing(final boolean escapeProcessing) {
		this.escapeProcessing = escapeProcessing;
	}
	
	/**
	 * Sets the full line delimiter.
	 *
	 * @param fullLineDelimiter
	 *            the new full line delimiter
	 */
	public void setFullLineDelimiter(final boolean fullLineDelimiter) {
		this.fullLineDelimiter = fullLineDelimiter;
	}
	
	/**
	 * Sets the log writer.
	 *
	 * @param logWriter
	 *            the new log writer
	 */
	public void setLogWriter(final PrintWriter logWriter) {
		this.logWriter = logWriter;
	}
	
	/**
	 * Sets the removes the c rs.
	 *
	 * @param removeCRs
	 *            the new removes the c rs
	 */
	public void setRemoveCRs(final boolean removeCRs) {
		this.removeCRs = removeCRs;
	}
	
	/**
	 * Sets the send full script.
	 *
	 * @param sendFullScript
	 *            the new send full script
	 */
	public void setSendFullScript(final boolean sendFullScript) {
		this.sendFullScript = sendFullScript;
	}
	
	/**
	 * Sets the stop on error.
	 *
	 * @param stopOnError
	 *            the new stop on error
	 */
	public void setStopOnError(final boolean stopOnError) {
		this.stopOnError = stopOnError;
	}
	
}
