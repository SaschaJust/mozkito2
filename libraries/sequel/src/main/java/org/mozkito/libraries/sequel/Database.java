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
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import org.mozkito.libraries.logging.Logger;
import org.mozkito.skeleton.contracts.Requires;

/**
 * The Class SequelDatabase.
 *
 * @author Sascha Just
 */
public class Database implements DataSource, Closeable {
	
	/**
	 * The Enum IdMode.
	 */
	public static enum IdMode {
		
		/** The sequence. */
		SEQUENCE,
		/** The local. */
		LOCAL;
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
		MSSQL;
	}
	
	/** The data source. */
	private final HikariDataSource           dataSource;
	
	/** The type. */
	private final Type                       type;
	
	/** The connection. */
	private final Connection                 connection;
	
	/** The adapters. */
	private final Map<Class<?>, IAdapter<?>> adapters = new HashMap<>();
	
	/** The id mode. */
	private IdMode                           idMode;
	
	/**
	 * Instantiates a new database.
	 *
	 * @param type
	 *            the type
	 * @param connectionString
	 *            the connection string
	 * @throws SQLException
	 *             the SQL exception
	 */
	public Database(final Type type, final String connectionString) throws SQLException {
		final HikariConfig config = new HikariConfig();
		config.setJdbcUrl(connectionString);
		this.type = type;
		this.idMode = IdMode.LOCAL;
		this.dataSource = new HikariDataSource(config);
		this.dataSource.setAutoCommit(false);
		this.dataSource.setConnectionTimeout(5000);
		this.dataSource.setLoginTimeout(3000);
		
		this.connection = this.dataSource.getConnection();
	}
	
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
	 * @param additionalArgs
	 *            the additional args
	 * @throws SQLException
	 *             the SQL exception
	 */
	public Database(final Type type, final String name, final String host, final String username,
	        final String password, final Integer port, final String additionalArgs) throws SQLException {
		HikariConfig config;
		switch (type) {
			case POSTGRES:
				config = setupPostgres(name, host, username, password, port, additionalArgs);
				break;
			case MSSQL:
				config = setupMSSQL(name, host, username, password, port, additionalArgs);
				break;
			case DERBY:
				config = setupDerby(name, host, username, password, port, additionalArgs);
				break;
			default:
				throw new RuntimeException("Unsupported database type: " + type);
		}
		
		Logger.info("Connecting to database using: " + config.getJdbcUrl());
		
		this.type = type;
		this.idMode = IdMode.LOCAL;
		
		this.dataSource = new HikariDataSource(config);
		if (port != null) {
			this.dataSource.addDataSourceProperty("port", 1433);
		}
		this.dataSource.setAutoCommit(false);
		this.connection = this.dataSource.getConnection();
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see java.io.Closeable#close()
	 */
	@Override
	public void close() throws IOException {
		this.dataSource.close();
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
		return this.dataSource.getConnection();
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
	 * Gets the id mode.
	 *
	 * @return the idMode
	 */
	public final IdMode getIdMode() {
		return this.idMode;
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
		this.dataSource.setMaximumPoolSize(this.adapters.size() + 2);
	}
	
	/**
	 * Sets the id mode.
	 *
	 * @param idMode
	 *            the idMode to set
	 */
	public final void setIdMode(final IdMode idMode) {
		this.idMode = idMode;
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
	 * @return the hikari config
	 */
	private HikariConfig setupDerby(final String name,
	                                final String host,
	                                final String username,
	                                final String password,
	                                final Integer port,
	                                final String additionalArgs) {
		final HikariConfig config = new HikariConfig();
		config.setJdbcUrl("jdbc:derby:" + name + (additionalArgs != null
		                                                                ? additionalArgs
		                                                                : ""));
		config.addDataSourceProperty("cachePrepStmts", "true");
		config.addDataSourceProperty("prepStmtCacheSize", "250");
		config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
		
		return config;
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
	 * @return the hikari config
	 */
	private HikariConfig setupMSSQL(final String name,
	                                final String host,
	                                final String username,
	                                final String password,
	                                final Integer port,
	                                final String additionalArgs) {
		Requires.notNull(name);
		
		final HikariConfig config = new HikariConfig();
		config.setJdbcUrl("jdbc:sqlserver://" + (host != null
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
		
		return config;
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
	 * @return the hikari config
	 */
	private HikariConfig setupPostgres(final String name,
	                                   final String host,
	                                   final String username,
	                                   final String password,
	                                   final Integer port,
	                                   final String additionalArgs) {
		Requires.notNull(name);
		
		final Properties props = new Properties();
		props.setProperty("dataSourceClassName", "org.postgresql.ds.PGSimpleDataSource");
		props.setProperty("dataSource.databaseName", name);
		
		if (username != null) {
			props.setProperty("dataSource.user", username);
		}
		
		if (password != null) {
			props.setProperty("dataSource.password", password);
		}
		
		final HikariConfig config = new HikariConfig(props);
		config.setJdbcUrl("jdbc:postgresql://" + (host != null
		                                                      ? host
		                                                      : "localhost") + (port != null
		                                                                                    ? ":" + port
		                                                                                    : "") + "/" + name
		        + (additionalArgs != null
		                                 ? additionalArgs
		                                 : ""));
		
		return config;
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
