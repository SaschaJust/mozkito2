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

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.logging.Logger;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

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
	
	/** The data source. */
	private final HikariDataSource                 dataSource;
	
	/** The type. */
	private final Type                             type;
	
	/** The connection. */
	private final Connection                       connection;
	
	/** The adapters. */
	private final Map<Class<?>, ISequelAdapter<?>> adapters = new HashMap<>();
	
	/**
	 * Instantiates a new sequel database.
	 *
	 * @param connectionString
	 *            the connection string
	 * @param properties
	 *            the properties
	 * @throws SQLException
	 *             the SQL exception
	 */
	public SequelDatabase(final String connectionString, final Properties props) throws SQLException {
		props.setProperty("dataSourceClassName", "org.postgresql.ds.PGSimpleDataSource");
		props.setProperty("dataSource.user", "test");
		props.setProperty("dataSource.password", "test");
		props.setProperty("dataSource.databaseName", "mydb");
		final HikariConfig config = new HikariConfig(props);
		config.setJdbcUrl(connectionString);
		config.setUsername(props.getProperty("user"));
		config.setPassword(props.getProperty("password"));
		config.addDataSourceProperty("cachePrepStmts", "true");
		config.addDataSourceProperty("prepStmtCacheSize", "250");
		config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
		
		this.type = Type.DERBY;
		
		for (final Entry<Object, Object> entry : props.entrySet()) {
			config.addDataSourceProperty((String) entry.getKey(), entry.getValue());
		}
		
		this.dataSource = new HikariDataSource(config);
		this.dataSource.getConnection().setAutoCommit(false);
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
		return this.connection;
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
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
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
	 * {@inheritDoc}
	 *
	 * @see java.sql.Wrapper#unwrap(java.lang.Class)
	 */
	@Override
	public <T> T unwrap(final Class<T> iface) throws SQLException {
		return this.dataSource.unwrap(iface);
	}
	
}
