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
import java.util.Iterator;
import java.util.List;

import org.mozkito.core.libs.versions.model.Revision;
import org.mozkito.skeleton.contracts.Contract;
import org.mozkito.skeleton.contracts.Requires;
import org.mozkito.skeleton.sequel.ISequelAdapter;
import org.mozkito.skeleton.sequel.SequelDatabase;
import org.mozkito.skeleton.sequel.SequelManager;

/**
 * @author Sascha Just
 *
 */
public class RevisionAdapter implements ISequelAdapter<Revision> {
	
	private final SequelDatabase database;
	private final String         saveStatement;
	private final String         nextIdStatement;
	
	/**
	 * @param database
	 */
	public RevisionAdapter(final SequelDatabase database) {
		this.database = database;
		this.saveStatement = SequelManager.loadStatement(database, "revision_save");
		this.nextIdStatement = SequelManager.loadStatement(database, "revision_nextid");
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#create(java.sql.ResultSet)
	 */
	public Revision create(final ResultSet result) {
		// TODO Auto-generated method stub
		// return null;
		throw new RuntimeException("Method 'create' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#createConstraints()
	 */
	public void createConstraints() {
		// TODO Auto-generated method stub
		//
		throw new RuntimeException("Method 'createConstraints' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#createIndexes()
	 */
	public void createIndexes() {
		// TODO Auto-generated method stub
		//
		throw new RuntimeException("Method 'createIndexes' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#createScheme()
	 */
	public void createScheme() {
		try {
			synchronized (this.database) {
				SequelManager.executeSQL(this.database, "revision_create_schema");
			}
		} catch (final SQLException | IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#delete(java.lang.Object)
	 */
	public void delete(final Revision object) {
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
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#load()
	 */
	public Iterator<Revision> load() {
		// TODO Auto-generated method stub
		// return null;
		throw new RuntimeException("Method 'load' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#load(java.lang.Object[])
	 */
	public List<Revision> load(final Object... ids) {
		// TODO Auto-generated method stub
		// return null;
		throw new RuntimeException("Method 'load' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#load(java.lang.Object)
	 */
	public Revision load(final Object id) {
		// TODO Auto-generated method stub
		// return null;
		throw new RuntimeException("Method 'load' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * @param statement
	 * @param idStatement
	 * @param revision
	 */
	private void save(final PreparedStatement saveStatement,
	                  final PreparedStatement idStatement,
	                  final Revision revision) {
		Requires.notNull(saveStatement);
		Requires.notNull(idStatement);
		Requires.notNull(revision);
		
		try {
			final ResultSet idResult = idStatement.executeQuery();
			final boolean result = idResult.next();
			Contract.asserts(result);
			
			final long id = idResult.getLong(1);
			
			int index = 0;
			saveStatement.setLong(++index, id);
			saveStatement.setInt(++index, revision.getDepotId());
			saveStatement.setLong(++index, revision.getChangeSetId());
			saveStatement.setShort(++index, revision.getChangeType());
			saveStatement.setLong(++index, revision.getSourceId());
			saveStatement.setLong(++index, revision.getTargetId());
			saveStatement.setShort(++index, revision.getConfidence());
			saveStatement.setInt(++index, revision.getOldMode());
			saveStatement.setInt(++index, revision.getNewMode());
			saveStatement.setString(++index, revision.getOldHash());
			saveStatement.setString(++index, revision.getNewHash());
			
			saveStatement.executeUpdate();
			
			revision.id(id);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#save(java.lang.Object[])
	 */
	public void save(final Revision... revisions) {
		Requires.notNull(revisions);
		
		try {
			final Connection connection = this.database.getConnection();
			final PreparedStatement statement = connection.prepareStatement(this.saveStatement);
			final PreparedStatement idStatement = connection.prepareStatement(this.nextIdStatement);
			
			for (final Revision revision : revisions) {
				save(statement, idStatement, revision);
			}
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#update(java.lang.Object[])
	 */
	public void update(final Revision... objects) {
		// TODO Auto-generated method stub
		//
		throw new RuntimeException("Method 'update' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
}
