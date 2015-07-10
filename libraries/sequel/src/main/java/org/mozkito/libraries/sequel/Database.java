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

import java.io.Closeable;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

import org.apache.derby.jdbc.EmbeddedDataSource;
import org.postgresql.ds.PGSimpleDataSource;

import org.mozkito.libraries.logging.Logger;
import org.mozkito.skeleton.contracts.Requires;

/**
 * The Class SequelDatabase.
 *
 * @author Sascha Just
 */
public class Database implements DataSource, Closeable {
	
	/**
	 * The Enum TxMode.
	 */
	public static enum TxMode {
		
		/** The transaction. */
		TRANSACTION,
		/** The batch. */
		BATCH;
	}
	
	/**
	 * The Enum Type.
	 */
	public static enum Type {
		
		/** The derby. */
		DERBY,
		/** The postgres. */
		POSTGRES,
		/** The mssql. */
		MSSQL,
		
		/** The azure. */
		AZURE;
	}
	
	/** The data source. */
	private final DataSource                 dataSource;
	
	/** The type. */
	private final Type                       type;
	
	/** The connection. */
	private final Connection                 connection;
	
	/** The adapters. */
	private final Map<Class<?>, IAdapter<?>> adapters    = new HashMap<>();
	
	/** The tx mode. */
	private final TxMode                     txMode;
	
	// /**
	// * Instantiates a new database.
	// *
	// * @param type
	// * the type
	// * @param connectionString
	// * the connection string
	// * @throws SQLException
	// * the SQL exception
	// */
	// public Database(final Type type, final String connectionString) throws SQLException {
	// final HikariConfig config = new HikariConfig();
	// config.setJdbcUrl(connectionString);
	// config.setAutoCommit(false);
	//
	// this.type = type;
	// this.dataSource =
	// this.dataSource.setTransactionIsolation("TRANSACTION_READ_UNCOMMITTED");
	// // this.dataSource.setConnectionTimeout(5000);
	// this.dataSource.setLoginTimeout(3000);
	//
	// this.txMode = TxMode.TRANSACTION;
	// this.dataSource.setAutoCommit(false);
	//
	// this.connection = this.dataSource.getConnection();
	// }
	
	/** The connections. */
	private final List<Connection>           connections = new LinkedList<>();
	
	/**
	 * Instantiates a new sequel database.
	 *
	 * @param type
	 *            the type
	 * @param name
	 *            the name
	 * @param host
	 *            the host
	 * @param username
	 *            the username
	 * @param password
	 *            the password
	 * @param port
	 *            the port
	 * @param txMode
	 *            the tx mode
	 * @param additionalArgs
	 *            the additional args
	 * @throws SQLException
	 *             the SQL exception
	 */
	public Database(final Type type, final String name, final String host, final String username,
	        final String password, final Integer port, final TxMode txMode, final String additionalArgs)
	        throws SQLException {
		switch (type) {
			case POSTGRES:
				this.dataSource = setupPostgres(name, host, username, password, port, additionalArgs);
				break;
			case MSSQL:
			case AZURE:
				this.dataSource = setupMSSQL(name, host, username, password, port, additionalArgs);
				break;
			case DERBY:
				this.dataSource = setupDerby(name, host, username, password, port, additionalArgs);
				break;
			default:
				throw new RuntimeException("Unsupported database type: " + type);
		}
		
		this.type = type;
		this.txMode = txMode;
		
		// this.dataSource.setConnectionTimeout(5000);
		this.dataSource.setLoginTimeout(3000);
		this.connection = this.dataSource.getConnection();
		this.connection.setAutoCommit(false);
		this.connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
	}
	
	/**
	 * Close.
	 */
	public void close() {
		for (final Connection connection : this.connections) {
			try {
				connection.close();
			} catch (final SQLException e) {
				Logger.warn(e, "Closing connection failed.");
			}
		}
	}
	
	/**
	 * Commit.
	 */
	public void commit() {
		try {
			getConnection().commit();
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Creates the constraints.
	 */
	public void createConstraints() {
		for (final IAdapter<?> adapter : this.adapters.values()) {
			adapter.createConstraints(this.connection);
		}
	}
	
	/**
	 * Creates the indexes.
	 */
	public void createIndexes() {
		for (final IAdapter<?> adapter : this.adapters.values()) {
			adapter.createIndexes(this.connection);
		}
	}
	
	/**
	 * Creates the primary keys.
	 */
	public void createPrimaryKeys() {
		for (final IAdapter<?> adapter : this.adapters.values()) {
			adapter.createPrimaryKeys(this.connection);
		}
	}
	
	/**
	 * Creates the scheme.
	 */
	public void createScheme() {
		for (final IAdapter<?> adapter : this.adapters.values()) {
			adapter.createScheme(this.connection);
		}
	}
	
	/**
	 * Gets the adapter.
	 *
	 * @param <T>
	 *            the generic type
	 * @param managedEntityType
	 *            the managed entity type
	 * @return the adapter
	 */
	@SuppressWarnings ("unchecked")
	public <T extends IEntity> IAdapter<T> getAdapter(final Class<T> managedEntityType) {
		return (IAdapter<T>) this.adapters.get(managedEntityType);
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see javax.sql.DataSource#getConnection()
	 */
	@Override
	public Connection getConnection() throws SQLException {
		final Connection connection = this.dataSource.getConnection();
		connection.setAutoCommit(false);
		connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
		this.connections.add(connection);
		return connection;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @deprecated
	 * @see javax.sql.DataSource#getConnection(java.lang.String, java.lang.String)
	 */
	@Override
	@Deprecated
	public Connection getConnection(final String username,
	                                final String password) throws SQLException {
		return this.dataSource.getConnection(username, password);
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see javax.sql.CommonDataSource#getLoginTimeout()
	 */
	@Override
	public int getLoginTimeout() throws SQLException {
		return this.dataSource.getLoginTimeout();
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see javax.sql.CommonDataSource#getLogWriter()
	 */
	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return this.dataSource.getLogWriter();
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see javax.sql.CommonDataSource#getParentLogger()
	 */
	@Override
	public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return this.dataSource.getParentLogger();
	}
	
	/**
	 * Gets the tx mode.
	 *
	 * @return the txMode
	 */
	public final TxMode getTxMode() {
		return this.txMode;
	}
	
	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public Type getType() {
		return this.type;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see java.sql.Wrapper#isWrapperFor(java.lang.Class)
	 */
	@Override
	public boolean isWrapperFor(final Class<?> iface) throws SQLException {
		return this.dataSource.isWrapperFor(iface);
	}
	
	/**
	 * Register.
	 *
	 * @param <T>
	 *            the generic type
	 * @param managedEntityType
	 *            the managed entity type
	 * @param adapter
	 *            the adapter
	 */
	public <T extends IEntity> void register(final Class<T> managedEntityType,
	                                         final IAdapter<T> adapter) {
		this.adapters.put(managedEntityType, adapter);
	}
	
	/**
	 * Release.
	 *
	 * @param connection
	 *            the connection
	 */
	public void release(final Connection connection) {
		this.connections.remove(connection);
		try {
			connection.close();
		} catch (final SQLException e) {
			Logger.warn(e, "Closing connection failed.");
		}
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see javax.sql.CommonDataSource#setLoginTimeout(int)
	 */
	@Override
	public void setLoginTimeout(final int seconds) throws SQLException {
		this.dataSource.setLoginTimeout(seconds);
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see javax.sql.CommonDataSource#setLogWriter(java.io.PrintWriter)
	 */
	@Override
	public void setLogWriter(final PrintWriter out) throws SQLException {
		this.dataSource.setLogWriter(out);
	}
	
	/**
	 * Setup derby.
	 *
	 * @param name
	 *            the name
	 * @param host
	 *            the host
	 * @param username
	 *            the username
	 * @param password
	 *            the password
	 * @param port
	 *            the port
	 * @param additionalArgs
	 *            the additional args
	 * @return the hikari config
	 */
	private DataSource setupDerby(final String name,
	                              final String host,
	                              final String username,
	                              final String password,
	                              final Integer port,
	                              final String additionalArgs) {
		final EmbeddedDataSource dataSource = new EmbeddedDataSource();
		dataSource.setDatabaseName(name);
		if (username != null) {
			dataSource.setUser(username);
		}
		
		if (password != null) {
			dataSource.setPassword(password);
		}
		
		dataSource.setConnectionAttributes(additionalArgs);
		
		return dataSource;
	}
	
	/**
	 * Setup mssql.
	 *
	 * @param name
	 *            the name
	 * @param host
	 *            the host
	 * @param username
	 *            the username
	 * @param password
	 *            the password
	 * @param port
	 *            the port
	 * @param additionalArgs
	 *            the additional args
	 * @return the hikari config
	 */
	private DataSource setupMSSQL(final String name,
	                              final String host,
	                              final String username,
	                              final String password,
	                              final Integer port,
	                              final String additionalArgs) {
		Requires.notNull(name);
		
		final SQLServerDataSource dataSource = new SQLServerDataSource();
		if (username != null) {
			dataSource.setUser(username);
		}
		
		if (password != null) {
			dataSource.setPassword(password);
		}
		dataSource.setURL("jdbc:sqlserver://" + (host != null
		                                                     ? host
		                                                     : "localhost") + ":" + (port != null
		                                                                                         ? port
		                                                                                         : "1433")
		        + ";databaseName=" + name + (username != null
		                                                     ? ";user=" + username
		                                                     : "") + (password != null
		                                                                              ? ";password=" + password
		                                                                              : "")
		        + (additionalArgs != null
		                                 ? additionalArgs
		                                 : ""));
		
		return dataSource;
	}
	
	/**
	 * Setup postgres.
	 *
	 * @param name
	 *            the name
	 * @param host
	 *            the host
	 * @param username
	 *            the username
	 * @param password
	 *            the password
	 * @param port
	 *            the port
	 * @param additionalArgs
	 *            the additional args
	 * @return the hikari config
	 */
	private DataSource setupPostgres(final String name,
	                                 final String host,
	                                 final String username,
	                                 final String password,
	                                 final Integer port,
	                                 final String additionalArgs) {
		
		Requires.notNull(name);
		
		final PGSimpleDataSource dataSource = new PGSimpleDataSource();
		dataSource.setUrl("jdbc:postgresql://" + (host != null
		                                                      ? host
		                                                      : "localhost") + (port != null
		                                                                                    ? ":" + port
		                                                                                    : "") + "/" + name
		        + (additionalArgs != null
		                                 ? additionalArgs
		                                 : ""));
		
		if (username != null) {
			dataSource.setUser(username);
		}
		
		if (password != null) {
			dataSource.setPassword(password);
		}
		return dataSource;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see java.sql.Wrapper#unwrap(java.lang.Class)
	 */
	@Override
	public <T> T unwrap(final Class<T> iface) throws SQLException {
		return this.dataSource.unwrap(iface);
	}
	
}
