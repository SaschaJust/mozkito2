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

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.mozkito.libraries.logging.Logger;
import org.mozkito.skeleton.contracts.Requires;
import org.mozkito.skeleton.sequel.Database.Type;

/**
 * The Class AbstractSequelAdapter.
 *
 * @author Sascha Just
 * @param <T>
 *            the generic type
 */
public abstract class AbstractAdapter<T extends IEntity> implements IAdapter<T> {
	
	/** The save statement. */
	private final String saveStatement;
	
	/** The create schema resource. */
	private final String createSchemaResource;
	
	/** The create indexes resource. */
	private final String createIndexesResource;
	
	/** The create constraints resource. */
	private final String createConstraintsResource;
	
	/** The create primary keys resource. */
	private final String createPrimaryKeysResource;
	
	/** The type. */
	private final Type   type;
	
	/**
	 * Instantiates a new abstract sequel adapter.
	 *
	 * @param type
	 *            the type
	 * @param identifier
	 *            the identifier
	 */
	public AbstractAdapter(final Database.Type type, final String identifier) {
		this.type = type;
		this.saveStatement = DatabaseManager.loadStatement(type, identifier + "_save");
		this.createSchemaResource = identifier + "_create_schema";
		this.createIndexesResource = identifier + "_create_indexes";
		this.createConstraintsResource = identifier + "_create_constraints";
		this.createPrimaryKeysResource = identifier + "_create_pkeys";
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.IAdapter#createConstraints(java.sql.Connection)
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
	 * @see org.mozkito.skeleton.sequel.IAdapter#createIndexes(java.sql.Connection)
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
	 * @see org.mozkito.skeleton.sequel.IAdapter#createPrimaryKeys(java.sql.Connection)
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
	 * @see org.mozkito.skeleton.sequel.IAdapter#createScheme(java.sql.Connection)
	 */
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
	 * @see org.mozkito.skeleton.sequel.IAdapter#prepareSaveStatement(java.sql.Connection)
	 */
	public final PreparedStatement prepareSaveStatement(final Connection connection) {
		try {
			return connection.prepareStatement(this.saveStatement);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.IAdapter#save(java.sql.Connection, org.mozkito.skeleton.sequel.IEntity[])
	 */
	public void save(final Connection connection,
	                 @SuppressWarnings ("unchecked") final T... entities) {
		Requires.notNull(entities);
		
		try {
			final PreparedStatement statement = prepareSaveStatement(connection);
			
			for (final T entity : entities) {
				final long id = nextId();
				entity.id(id);
				save(statement, id, entity);
			}
			
			statement.getConnection().commit();
		} catch (final SQLException e) {
			throw new RuntimeException(e);
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
