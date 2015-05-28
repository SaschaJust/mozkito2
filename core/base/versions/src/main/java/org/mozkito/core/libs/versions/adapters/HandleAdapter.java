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

import org.mozkito.core.libs.versions.model.Handle;
import org.mozkito.skeleton.contracts.Contract;
import org.mozkito.skeleton.contracts.Requires;
import org.mozkito.skeleton.sequel.ISequelAdapter;
import org.mozkito.skeleton.sequel.SequelDatabase;
import org.mozkito.skeleton.sequel.SequelManager;

/**
 * @author Sascha Just
 *
 */
public class HandleAdapter implements ISequelAdapter<Handle> {
	
	private final SequelDatabase database;
	private final String         nextIdStatement;
	private final String         saveStatement;
	
	/**
	 * @param database
	 */
	public HandleAdapter(final SequelDatabase database) {
		this.database = database;
		this.saveStatement = SequelManager.loadStatement(database, "handle_save");
		this.nextIdStatement = SequelManager.loadStatement(database, "handle_nextid");
		
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#create(java.sql.ResultSet)
	 */
	public Handle create(final ResultSet result) {
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
				SequelManager.executeSQL(this.database, "handle_create_schema");
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
	public void delete(final Handle object) {
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
	public Iterator<Handle> load() {
		// TODO Auto-generated method stub
		// return null;
		throw new RuntimeException("Method 'load' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#load(java.lang.Object[])
	 */
	public List<Handle> load(final Object... ids) {
		// TODO Auto-generated method stub
		// return null;
		throw new RuntimeException("Method 'load' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#load(java.lang.Object)
	 */
	public Handle load(final Object id) {
		// TODO Auto-generated method stub
		// return null;
		throw new RuntimeException("Method 'load' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#save(java.lang.Object[])
	 */
	public void save(final Handle... handles) {
		Requires.notNull(handles);
		
		try {
			final Connection connection = this.database.getConnection();
			final PreparedStatement saveStatement = connection.prepareStatement(this.saveStatement);
			final PreparedStatement idStatement = connection.prepareStatement(this.nextIdStatement);
			
			for (final Handle handle : handles) {
				save(saveStatement, idStatement, handle);
			}
			
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * @param saveStatement2
	 * @param idStatement
	 * @param handle
	 */
	private void save(final PreparedStatement saveStatement,
	                  final PreparedStatement idStatement,
	                  final Handle handle) {
		Requires.notNull(saveStatement);
		Requires.notNull(idStatement);
		Requires.notNull(handle);
		
		try {
			final ResultSet idResult = idStatement.executeQuery();
			final boolean result = idResult.next();
			Contract.asserts(result);
			
			final long id = idResult.getLong(1);
			
			int index = 0;
			saveStatement.setLong(++index, id);
			saveStatement.setInt(++index, handle.getDepotId());
			saveStatement.setString(++index, handle.getPath());
			saveStatement.executeUpdate();
			
			handle.id(id);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#update(java.lang.Object[])
	 */
	public void update(final Handle... objects) {
		// TODO Auto-generated method stub
		//
		throw new RuntimeException("Method 'update' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
}
