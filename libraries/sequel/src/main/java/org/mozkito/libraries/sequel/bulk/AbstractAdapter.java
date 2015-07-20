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

package org.mozkito.libraries.sequel.bulk;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicLong;

import org.mozkito.libraries.logging.Logger;
import org.mozkito.libraries.sequel.Database;
import org.mozkito.libraries.sequel.Database.TxMode;
import org.mozkito.libraries.sequel.Database.Type;
import org.mozkito.libraries.sequel.DatabaseManager;
import org.mozkito.libraries.sequel.IEntity;
import org.mozkito.skeleton.contracts.Requires;

/**
 * The Class AbstractPGAdapter.
 *
 * @author Sascha Just
 * @param <T>
 *            the generic type
 */
public abstract class AbstractAdapter<T extends IEntity> implements IAdapter<T> {
	
	/** The copy statement. */
	private String           insertStatement;
	
	/** The writer. */
	protected IWriter        writer;
	
	/** The current id. */
	private final AtomicLong currentId = new AtomicLong(0);
	
	/** The create schema resource. */
	private final String     createSchemaResource;
	
	/** The create indexes resource. */
	private final String     createIndexesResource;
	
	/** The create constraints resource. */
	private final String     createConstraintsResource;
	
	/** The create primary keys resource. */
	private final String     createPrimaryKeysResource;
	
	/** The create foreign keys resource. */
	private final String     createForeignKeysResource;
	
	/** The type. */
	private final Type       type;
	
	/** The mode. */
	private final TxMode     mode;
	
	/** The identifier. */
	private final String     identifier;
	
	/**
	 * Instantiates a new PG branch adapter.
	 *
	 * @param type
	 *            the type
	 * @param mode
	 *            the mode
	 * @param identifier
	 *            the entity tag
	 */
	public AbstractAdapter(final Database.Type type, final Database.TxMode mode, final String identifier) {
		Requires.notNull(type);
		Requires.notNull(mode);
		this.type = type;
		this.mode = mode;
		this.identifier = identifier;
		
		this.createSchemaResource = identifier + "_create_schema";
		this.createIndexesResource = identifier + "_create_indexes";
		this.createConstraintsResource = identifier + "_create_constraints";
		this.createPrimaryKeysResource = identifier + "_create_pkeys";
		this.createForeignKeysResource = identifier + "_create_fkeys";
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.bulk.IAdapter#createConstraints(java.sql.Connection)
	 */
	@Override
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
	 * @see org.mozkito.libraries.sequel.bulk.IAdapter#createForeignKeys(java.sql.Connection)
	 */
	@Override
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
	 * @see org.mozkito.libraries.sequel.bulk.IAdapter#createIndexes(java.sql.Connection)
	 */
	@Override
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
	 * @see org.mozkito.libraries.sequel.bulk.IAdapter#createPrimaryKeys(java.sql.Connection)
	 */
	@Override
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
	 * @see org.mozkito.libraries.sequel.bulk.IAdapter#createScheme(java.sql.Connection)
	 */
	@Override
	public void createScheme(final Connection connection) {
		try {
			DatabaseManager.executeSQL(connection, this.type, this.createSchemaResource);
		} catch (final SQLException | IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.bulk.IAdapter#flush()
	 */
	@Override
	public void flush() {
		this.writer.flush();
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.bulk.IAdapter#init(java.sql.Connection)
	 */
	public void init(final Connection connection) {
		switch (this.type) {
			case POSTGRES:
				assert TxMode.COPY.equals(this.mode);
				this.insertStatement = "COPY " + this.identifier + " FROM STDIN";
				this.writer = new PostgresWriter(this.insertStatement, connection);
				break;
			default:
				this.insertStatement = DatabaseManager.loadStatement(this.type, this.identifier + "_save");
				this.writer = new BulkWriter(this.insertStatement, connection);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.bulk.IAdapter#nextId()
	 */
	@Override
	public long nextId() {
		return this.currentId.incrementAndGet();
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.bulk.IAdapter#save(org.mozkito.libraries.sequel.IEntity)
	 */
	@Override
	public abstract void save(final T entity);
	
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
