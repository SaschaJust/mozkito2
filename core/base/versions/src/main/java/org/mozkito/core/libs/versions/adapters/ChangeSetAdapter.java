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

package org.mozkito.core.libs.versions.adapters;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

import org.mozkito.core.libs.versions.model.ChangeSet;
import org.mozkito.libraries.sequel.ISequelAdapter;
import org.mozkito.libraries.sequel.SequelDatabase;
import org.mozkito.libraries.sequel.SequelManager;
import org.mozkito.skeleton.contracts.Contract;
import org.mozkito.skeleton.contracts.Requires;

/**
 * The Class ChangeSetAdapter, which is used to load and store {@link ChangeSet} entities from/to a database.
 *
 * @author Sascha Just
 */
public class ChangeSetAdapter implements ISequelAdapter<ChangeSet> {
	
	/** The save statement. */
	private final String         saveStatement;
	
	/** The database. */
	private final SequelDatabase database;
	
	/** The next id statement. */
	private final String         nextIdStatement;
	
	/**
	 * Instantiates a new change set adapter.
	 *
	 * @param database
	 *            the database
	 */
	public ChangeSetAdapter(final SequelDatabase database) {
		this.database = database;
		this.saveStatement = SequelManager.loadStatement(database, "changeset_save");
		this.nextIdStatement = SequelManager.loadStatement(database, "changeset_nextid");
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see org.mozkito.libraries.sequel.ISequelAdapter#create(java.sql.ResultSet)
	 */
	
	public ChangeSet create(final ResultSet result) {
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see org.mozkito.libraries.sequel.ISequelAdapter#createConstraints()
	 */
	
	public void createConstraints() {
		// TODO Auto-generated method stub
		//
		throw new RuntimeException("Method 'createConstraints' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see org.mozkito.libraries.sequel.ISequelAdapter#createIndexes()
	 */
	
	public void createIndexes() {
		// TODO Auto-generated method stub
		//
		throw new RuntimeException("Method 'createIndexes' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see org.mozkito.libraries.sequel.ISequelAdapter#createScheme()
	 */
	
	public void createScheme() {
		try {
			synchronized (this.database) {
				SequelManager.executeSQL(this.database, "changeset_create_schema");
			}
		} catch (final SQLException | IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see org.mozkito.libraries.sequel.ISequelAdapter#delete(java.lang.Object)
	 */
	
	public void delete(final ChangeSet object) {
		// TODO Auto-generated method stub
		//
		throw new RuntimeException("Method 'delete' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * @return the nextIdStatement
	 */
	public final PreparedStatement getNextIdStatement() {
		try {
			return this.database.getConnection().prepareStatement(this.nextIdStatement);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * @return the saveStatement
	 */
	public final PreparedStatement getSaveStatement() {
		try {
			return this.database.getConnection().prepareStatement(this.saveStatement);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see org.mozkito.libraries.sequel.ISequelAdapter#load()
	 */
	
	public Iterator<ChangeSet> load() {
		// TODO Auto-generated method stub
		// return null;
		throw new RuntimeException("Method 'load' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see org.mozkito.libraries.sequel.ISequelAdapter#load(java.lang.Object[])
	 */
	
	public List<ChangeSet> load(final Object... ids) {
		// TODO Auto-generated method stub
		// return null;
		throw new RuntimeException("Method 'load' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see org.mozkito.libraries.sequel.ISequelAdapter#load(java.lang.Object)
	 */
	
	public ChangeSet load(final Object id) {
		// TODO Auto-generated method stub
		// return null;
		throw new RuntimeException("Method 'load' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see org.mozkito.libraries.sequel.ISequelAdapter#save(java.lang.Object[])
	 */
	
	public void save(final ChangeSet... changeSets) {
		Requires.notNull(changeSets);
		
		try {
			final Connection connection = this.database.getConnection();
			final PreparedStatement statement = connection.prepareStatement(this.saveStatement);
			final PreparedStatement idStatement = connection.prepareStatement(this.nextIdStatement);
			
			for (final ChangeSet changeSet : changeSets) {
				save(statement, idStatement, changeSet);
			}
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Save.
	 *
	 * @param saveStatement
	 *            the statement
	 * @param id
	 *            the id
	 * @param changeSet
	 *            the change set
	 * @throws SQLException
	 *             the SQL exception
	 */
	public void save(final PreparedStatement saveStatement,
	                 final PreparedStatement idStatement,
	                 final ChangeSet changeSet) {
		Requires.notNull(saveStatement);
		Requires.notNull(idStatement);
		Requires.notNull(changeSet);
		
		try {
			final ResultSet idResult = idStatement.executeQuery();
			final boolean result = idResult.next();
			Contract.asserts(result);
			
			final long id = idResult.getLong(1);
			
			int index = 0;
			saveStatement.setLong(++index, id);
			
			saveStatement.setInt(++index, changeSet.getDepotId());
			
			saveStatement.setString(++index, changeSet.getCommitHash());
			
			saveStatement.setString(++index, changeSet.getTreeHash());
			
			saveStatement.setTimestamp(++index, Timestamp.from(changeSet.getAuthoredTime()));
			saveStatement.setInt(++index, changeSet.getAuthorId());
			
			saveStatement.setTimestamp(++index, Timestamp.from(changeSet.getCommitTime()));
			saveStatement.setInt(++index, changeSet.getCommitterId());
			
			saveStatement.setString(++index, changeSet.getSubject());
			saveStatement.setString(++index, changeSet.getBody());
			
			saveStatement.executeUpdate();
			
			changeSet.id(id);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see org.mozkito.libraries.sequel.ISequelAdapter#update(java.lang.Object[])
	 */
	
	public void update(final ChangeSet... objects) {
		// TODO Auto-generated method stub
		//
		throw new RuntimeException("Method 'update' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
}
