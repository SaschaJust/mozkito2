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
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import org.mozkito.skeleton.contracts.Requires;
import org.mozkito.skeleton.logging.Logger;

/**
 * The Class SequelDatabase.
 *
 * @author Sascha Just
 */
public class SequelDatabase implements DataSource, Closeable {
	
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
	
	/** The connection count. */
	private int                                    connectionCount = 5;
	
	/** The data source. */
	private final HikariDataSource                 dataSource;
	
	/** The type. */
	private final Type                             type;
	
	/** The adapters. */
	private final Map<Class<?>, ISequelAdapter<?>> adapters        = new HashMap<>();
	
	/** The connections. */
	private final Connection[]                     connections;
	
	/** The random. */
	private final Random                           random          = new Random(Instant.now().toEpochMilli());
	
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
	 * @param connectionCount
	 *            the connection count
	 * @throws SQLException
	 *             the SQL exception
	 */
	public SequelDatabase(final Type type, final String name, final String host, final String username,
	        final String password, final Integer port, final int connectionCount) throws SQLException {
		HikariConfig config;
		
		switch (type) {
			case POSTGRES:
				config = setupPostgres(name, host, username, password, port);
				break;
			case MSSQL:
				config = setupMSSQL(name, host, username, password, port);
				break;
			case DERBY:
				config = setupDerby(name, host, username, password, port);
				break;
			default:
				throw new RuntimeException("Unsupported database type: " + type);
		}
		
		this.type = type;
		
		this.dataSource = new HikariDataSource(config);
		this.connectionCount = connectionCount;
		
		this.connections = new Connection[this.connectionCount];
		
		for (int i = 0; i < this.connectionCount; ++i) {
			this.connections[i] = this.dataSource.getConnection();
			this.connections[i].setAutoCommit(false);
		}
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see java.io.Closeable#close()
	 */
	@Override
	public void close() {
		for (final Connection connection : this.connections) {
			try {
				connection.close();
			} catch (final SQLException e) {
				Logger.warn("Could not close SQL connection. " + e.getMessage());
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
	 * Creates the scheme.
	 */
	public void createScheme() {
		for (final ISequelAdapter<?> adapter : this.adapters.values()) {
			adapter.createScheme();
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
	public <T> ISequelAdapter<T> getAdapter(final Class<T> managedEntityType) {
		return (ISequelAdapter<T>) this.adapters.get(managedEntityType);
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see javax.sql.DataSource#getConnection()
	 */
	@Override
	public Connection getConnection() throws SQLException {
		return this.connections[this.random.nextInt(this.connectionCount)];
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
	public <T> void register(final Class<T> managedEntityType,
	                         final ISequelAdapter<T> adapter) {
		this.adapters.put(managedEntityType, adapter);
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
	                                final Integer port) {
		final HikariConfig config = new HikariConfig();
		config.setJdbcUrl("jdbc:derby:" + name);
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
	                                final Integer port) {
		// TODO Auto-generated method stub
		//
		throw new RuntimeException("Method 'setupMSSQL' has not yet been implemented."); //$NON-NLS-1$
		
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
	                                   final Integer port) {
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
		                                                                                    : "") + "/" + name);
		
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
