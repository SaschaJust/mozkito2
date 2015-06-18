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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.mozkito.skeleton.contracts.Asserts;
import org.mozkito.skeleton.contracts.Contract;
import org.mozkito.skeleton.contracts.Requires;

/**
 * The Class AbstractSequelAdapter.
 *
 * @author Sascha Just
 * @param <T>
 *            the generic type
 */
public abstract class AbstractSequelAdapter<T> implements ISequelAdapter<T> {
	
	/** The database. */
	protected final SequelDatabase database;
	
	/** The save statement. */
	private final String           saveStatement;
	
	/** The next id statement. */
	private final String           nextIdStatement;
	
	/** The create schema resource. */
	private final String           createSchemaResource;
	
	/** The create indexes resource. */
	private final String           createIndexesResource;
	
	/** The create constraints resource. */
	private final String           createConstraintsResource;
	
	/** The current id. */
	private long                   currentId = 0l;
	
	private final String           createSequencesResource;
	
	/**
	 * Instantiates a new abstract sequel adapter.
	 *
	 * @param database
	 *            the database
	 * @param identifier
	 *            the identifier
	 */
	public AbstractSequelAdapter(final SequelDatabase database, final String identifier) {
		this.database = database;
		this.saveStatement = SequelManager.loadStatement(database, identifier + "_save");
		this.nextIdStatement = SequelManager.loadStatement(database, identifier + "_nextid");
		this.createSchemaResource = identifier + "_create_schema";
		this.createIndexesResource = identifier + "_create_indexes";
		this.createConstraintsResource = identifier + "_create_constraints";
		this.createSequencesResource = identifier + "_create_sequences";
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#createConstraints()
	 */
	public void createConstraints() {
		try {
			synchronized (this.database) {
				SequelManager.executeSQL(this.database, this.createConstraintsResource, true);
			}
		} catch (final SQLException | IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#createIndexes()
	 */
	public void createIndexes() {
		try {
			synchronized (this.database) {
				SequelManager.executeSQL(this.database, this.createIndexesResource, true);
			}
		} catch (final SQLException | IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#createScheme()
	 */
	public void createScheme() {
		try {
			synchronized (this.database) {
				SequelManager.executeSQL(this.database, this.createSchemaResource);
				switch (this.database.getIdMode()) {
					case SEQUENCE:
						SequelManager.executeSQL(this.database, this.createSequencesResource, true);
						break;
					default:
						// nothing
				}
			}
		} catch (final SQLException | IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#nextId(java.sql.PreparedStatement)
	 */
	public synchronized long nextId(final PreparedStatement nextIdStatement) {
		try {
			switch (this.database.getIdMode()) {
				case SEQUENCE:
					Asserts.notNull(nextIdStatement);
					final ResultSet idResult = nextIdStatement.executeQuery();
					final boolean result = idResult.next();
					Contract.asserts(result);
					return idResult.getLong(1);
				case LOCAL:
					return ++this.currentId;
				default:
					throw new RuntimeException("Unsupported ID mode: " + this.database.getIdMode().name());
			}
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#prepareNextIdStatement()
	 */
	public final PreparedStatement prepareNextIdStatement() {
		try {
			switch (this.database.getIdMode()) {
				case SEQUENCE:
					return this.database.getConnection().prepareStatement(this.nextIdStatement);
				case LOCAL:
					return null;
				default:
					throw new RuntimeException("Unsupported ID mode: " + this.database.getIdMode().name());
			}
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#prepareSaveStatement()
	 */
	public final PreparedStatement prepareSaveStatement() {
		try {
			return this.database.getConnection().prepareStatement(this.saveStatement);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#save(java.sql.PreparedStatement, java.sql.PreparedStatement,
	 *      java.lang.Object)
	 */
	public final void save(final PreparedStatement saveStatement,
	                       final PreparedStatement idStatement,
	                       final T entity) {
		Requires.notNull(saveStatement);
		Requires.notNull(entity);
		save(saveStatement, nextId(idStatement), entity);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#save(java.lang.Object[])
	 */
	public void save(@SuppressWarnings ("unchecked") final T... entities) {
		Requires.notNull(entities);
		
		try {
			final PreparedStatement statement = prepareSaveStatement();
			final PreparedStatement idStatement = prepareNextIdStatement();
			
			for (final T entity : entities) {
				save(statement, idStatement, entity);
			}
			
			statement.getConnection().commit();
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
