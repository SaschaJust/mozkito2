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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.ibatis.jdbc.ScriptRunner;

import org.mozkito.libraries.logging.Logger;
import org.mozkito.skeleton.contracts.Asserts;
import org.mozkito.skeleton.contracts.Requires;

/**
 * The Class SequelManager.
 *
 * @author Sascha Just
 */
public class SequelManager {
	
	/**
	 * Execute sql.
	 *
	 * @param database
	 *            the database
	 * @param in
	 *            the in
	 * @throws SQLException
	 *             the SQL exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void executeSQL(final SequelDatabase database,
	                              final InputStream in) throws SQLException, IOException {
		synchronized (database) {
			final Connection connection = database.getConnection();
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
	 * @param database
	 *            the database
	 * @param name
	 *            the name
	 * @throws SQLException
	 *             the SQL exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void executeSQL(final SequelDatabase database,
	                              final String name) throws SQLException, IOException {
		Requires.notNull(database);
		Requires.notNull(name);
		
		Asserts.notNull(database.getType());
		
		String path = name + "." + database.getType().name().toLowerCase();
		InputStream stream = ClassLoader.getSystemResourceAsStream(path);
		
		if (stream == null) {
			path = name + ".sql";
			stream = ClassLoader.getSystemResourceAsStream(path);
			if (stream == null) {
				throw new IllegalArgumentException("Could not find statement file: " + path);
			}
		}
		
		executeSQL(database, stream);
	}
	
	/**
	 * Load query.
	 *
	 * @param database
	 *            the database
	 * @param name
	 *            the name
	 * @return the string
	 */
	public static String loadStatement(final SequelDatabase database,
	                                   final String name) {
		Requires.notNull(database);
		Requires.notNull(name);
		
		Asserts.notNull(database.getType());
		
		String path = name + "." + database.getType().name().toLowerCase();
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
