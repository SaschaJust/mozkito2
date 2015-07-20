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

package org.mozkito.libraries.sequel.legacy;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicLong;

import org.mozkito.libraries.logging.Logger;
import org.mozkito.libraries.sequel.Database;
import org.mozkito.libraries.sequel.DatabaseManager;
import org.mozkito.libraries.sequel.IEntity;
import org.mozkito.libraries.sequel.Database.TxMode;
import org.mozkito.libraries.sequel.Database.Type;
import org.mozkito.skeleton.contracts.Requires;

/**
 * The Class AbstractSequelAdapter.
 *
 * @author Sascha Just
 * @param <T>
 *            the generic type
 */
public abstract class AbstractAdapter<T extends IEntity> implements IAdapter<T> {
	
	/** The save statement. */
	private final String     saveStatement;
	
	/** The create schema resource. */
	private final String     createSchemaResource;
	
	/** The create indexes resource. */
	private final String     createIndexesResource;
	
	/** The create constraints resource. */
	private final String     createConstraintsResource;
	
	/** The create primary keys resource. */
	private final String     createPrimaryKeysResource;
	
	/** The type. */
	private final Type       type;
	
	/** The tx mode. */
	private final TxMode     txMode;
	
	/** The current id. */
	private final AtomicLong currentId = new AtomicLong(0);
	
	/** The create foreign keys resource. */
	private final String     createForeignKeysResource;
	
	/**
	 * Instantiates a new abstract sequel adapter.
	 *
	 * @param type
	 *            the type
	 * @param mode
	 *            the mode
	 * @param identifier
	 *            the identifier
	 */
	public AbstractAdapter(final Database.Type type, final Database.TxMode mode, final String identifier) {
		this.type = type;
		this.txMode = mode;
		this.saveStatement = DatabaseManager.loadStatement(type, identifier + "_save");
		this.createSchemaResource = identifier + "_create_schema";
		this.createIndexesResource = identifier + "_create_indexes";
		this.createConstraintsResource = identifier + "_create_constraints";
		this.createPrimaryKeysResource = identifier + "_create_pkeys";
		this.createForeignKeysResource = identifier + "_create_fkeys";
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.legacy.IAdapter#createConstraints(java.sql.Connection)
	 */
	public void createConstraints(final Connection connection) {
		try {
			DatabaseManager.executeSQL(connection, this.type, this.createConstraintsResource, true);
		} catch (final SQLException | IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.legacy.IAdapter#createForeignKeys(java.sql.Connection)
	 */
	public void createForeignKeys(final Connection connection) {
		try {
			DatabaseManager.executeSQL(connection, this.type, this.createForeignKeysResource, true);
		} catch (final SQLException | IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.legacy.IAdapter#createIndexes(java.sql.Connection)
	 */
	public void createIndexes(final Connection connection) {
		try {
			DatabaseManager.executeSQL(connection, this.type, this.createIndexesResource, true);
		} catch (final SQLException | IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.legacy.IAdapter#createPrimaryKeys(java.sql.Connection)
	 */
	public void createPrimaryKeys(final Connection connection) {
		try {
			DatabaseManager.executeSQL(connection, this.type, this.createPrimaryKeysResource);
		} catch (final SQLException | IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.legacy.IAdapter#createScheme(java.sql.Connection)
	 */
	public void createScheme(final Connection connection) {
		try {
			DatabaseManager.executeSQL(connection, this.type, this.createSchemaResource);
		} catch (final SQLException | IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Execute.
	 *
	 * @param statement
	 *            the statement
	 * @throws SQLException
	 *             the SQL exception
	 */
	public final void execute(final PreparedStatement statement) throws SQLException {
		switch (getTxMode()) {
			case TRANSACTION:
				statement.getConnection().commit();
				break;
			case BATCH:
				statement.executeBatch();
				break;
			default:
				throw new RuntimeException("Unsupported TxMode: " + getTxMode().name());
		}
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
	public final Type getType() {
		return this.type;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.legacy.IAdapter#nextId()
	 */
	public long nextId() {
		return this.currentId.incrementAndGet();
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.legacy.IAdapter#prepareSaveStatement(java.sql.Connection)
	 */
	public PreparedStatement prepareSaveStatement(final Connection connection) {
		try {
			return connection.prepareStatement(this.saveStatement);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.legacy.IAdapter#save(java.sql.Connection, org.mozkito.libraries.sequel.IEntity[])
	 */
	public void save(final Connection connection,
	                 @SuppressWarnings ("unchecked") final T... entities) {
		Requires.notNull(entities);
		
		try {
			final PreparedStatement statement = prepareSaveStatement(connection);
			
			for (final T entity : entities) {
				final long id = nextId();
				entity.setId(id);
				save(statement, id, entity);
			}
			
			execute(statement);
			statement.close();
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.legacy.IAdapter#saveStatement()
	 */
	public String saveStatement() {
		return this.saveStatement;
	}
	
	/**
	 * Execute.
	 *
	 * @param statement
	 *            the statement
	 * @throws SQLException
	 *             the SQL exception
	 */
	protected final void schedule(final PreparedStatement statement) throws SQLException {
		switch (getTxMode()) {
			case TRANSACTION:
				statement.executeUpdate();
				break;
			case BATCH:
				statement.addBatch();
				break;
			default:
				throw new RuntimeException("Unsupported TxMode: " + getTxMode().name());
		}
	}
	
	/**
	 * Truncate.
	 *
	 * @param string
	 *            the string
	 * @param length
	 *            the length
	 * @return the string
	 */
	protected final String truncate(final String string,
	                                final int length) {
		if (string == null || string.length() < length) {
			return string;
		}
		
		Logger.warn("Truncating '%s' to fit %s characters to persist to database.", string, length);
		return string.substring(0, length);
	}
}
