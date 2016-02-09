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

package org.mozkito.libraries.sequel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.ibatis.jdbc.ScriptRunner;

import org.mozkito.libraries.logging.Logger;
import org.mozkito.skeleton.contracts.Requires;

/**
 * The Class SequelManager.
 *
 * @author Sascha Just
 */
public class DatabaseManager {
	
	/**
	 * Execute sql.
	 *
	 * @param connection
	 *            the connection
	 * @param in
	 *            the in
	 * @throws SQLException
	 *             the SQL exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void executeSQL(final Connection connection,
	                              final InputStream in) throws SQLException, IOException {
		synchronized (connection) {
			try {
				final ScriptRunner runner = new ScriptRunner(connection);
				runner.setLogWriter(new PrintWriter(Logger.debug));
				runner.setErrorLogWriter(new PrintWriter(Logger.error));
				runner.runScript(new InputStreamReader(in));
				connection.commit();
			} catch (final SQLException e) {
				connection.rollback();
				throw e;
			}
		}
	}
	
	/**
	 * Execute sql.
	 *
	 * @param connection
	 *            the connection
	 * @param type
	 *            the type
	 * @param name
	 *            the name
	 * @throws SQLException
	 *             the SQL exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void executeSQL(final Connection connection,
	                              final Database.Type type,
	                              final String name) throws SQLException, IOException {
		executeSQL(connection, type, name, false);
	}
	
	/**
	 * Execute sql.
	 *
	 * @param connection
	 *            the connection
	 * @param type
	 *            the type
	 * @param name
	 *            the name
	 * @param optional
	 *            the optional
	 * @throws SQLException
	 *             the SQL exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void executeSQL(final Connection connection,
	                              final Database.Type type,
	                              final String name,
	                              final boolean optional) throws SQLException, IOException {
		Requires.notNull(connection);
		Requires.notNull(type);
		Requires.notNull(name);
		
		String path = name + "." + type.name().toLowerCase();
		InputStream stream = ClassLoader.getSystemResourceAsStream(path);
		
		if (stream == null) {
			path = name + ".sql";
			stream = ClassLoader.getSystemResourceAsStream(path);
			if (stream == null) {
				if (!optional) {
					throw new IllegalArgumentException("Could not find statement file: " + path);
				} else {
					return;
				}
			}
		}
		
		executeSQL(connection, stream);
	}
	
	/**
	 * Load query.
	 *
	 * @param type
	 *            the type
	 * @param name
	 *            the name
	 * @return the string
	 */
	public static String loadStatement(final Database.Type type,
	                                   final String name) {
		Requires.notNull(type);
		Requires.notNull(name);
		
		String path = name + "." + type.name().toLowerCase();
		InputStream stream = ClassLoader.getSystemResourceAsStream(path);
		
		final StringBuilder statementBuilder = new StringBuilder();
		
		if (stream == null) {
			path = name + ".sql";
			stream = ClassLoader.getSystemResourceAsStream(path);
			if (stream == null) {
				throw new IllegalArgumentException("Could not locate SQL query for resource path: " + path);
			}
		}
		
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				statementBuilder.append(line).append(System.lineSeparator());
			}
			
			return statementBuilder.toString();
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}
}
