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

import org.mozkito.core.libs.versions.model.Branch;
import org.mozkito.skeleton.contracts.Contract;
import org.mozkito.skeleton.contracts.Requires;
import org.mozkito.skeleton.sequel.ISequelAdapter;
import org.mozkito.skeleton.sequel.SequelDatabase;
import org.mozkito.skeleton.sequel.SequelManager;

// TODO: Auto-generated Javadoc
/**
 * The Class BranchAdapter.
 *
 * @author Sascha Just
 */
public class BranchAdapter implements ISequelAdapter<Branch> {
	
	/** The database. */
	private final SequelDatabase database;
	private final String         nextIdStatement;
	private final String         saveStatement;
	
	/**
	 * Instantiates a new branch adapter.
	 *
	 * @param database
	 *            the database
	 */
	public BranchAdapter(final SequelDatabase database) {
		this.database = database;
		this.saveStatement = SequelManager.loadStatement(database, "branch_save");
		this.nextIdStatement = SequelManager.loadStatement(database, "branch_nextid");
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#create(java.sql.ResultSet)
	 */
	public Branch create(final ResultSet result) {
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
				SequelManager.executeSQL(this.database, "branch_create_schema");
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
	public void delete(final Branch object) {
		// TODO Auto-generated method stub
		//
		throw new RuntimeException("Method 'delete' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#load()
	 */
	public Iterator<Branch> load() {
		// TODO Auto-generated method stub
		// return null;
		throw new RuntimeException("Method 'load' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#load(java.lang.Object[])
	 */
	public List<Branch> load(final Object... ids) {
		// TODO Auto-generated method stub
		// return null;
		throw new RuntimeException("Method 'load' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#load(java.lang.Object)
	 */
	public Branch load(final Object id) {
		// TODO Auto-generated method stub
		// return null;
		throw new RuntimeException("Method 'load' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * @param monitoredBranchString
	 * @return
	 */
	public Branch loadByName(final String monitoredBranchString) {
		// TODO Auto-generated method stub
		// return null;
		throw new RuntimeException("Method 'loadByName' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#save(java.lang.Object[])
	 */
	public void save(final Branch... branches) {
		Requires.notNull(branches);
		
		try {
			synchronized (this.database) {
				
				final Connection connection = this.database.getConnection();
				final PreparedStatement statement = connection.prepareStatement(this.saveStatement);
				final PreparedStatement idStatement = connection.prepareStatement(this.nextIdStatement);
				
				for (final Branch branch : branches) {
					final ResultSet idResult = idStatement.executeQuery();
					final boolean result = idResult.next();
					Contract.asserts(result);
					final int id = idResult.getInt(1);
					
					save(statement, id, branch);
				}
			}
			
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Save.
	 *
	 * @param statement
	 *            the statement
	 * @param id
	 *            the id
	 * @param branch
	 *            the branch
	 * @throws SQLException
	 *             the SQL exception
	 */
	public void save(final PreparedStatement statement,
	                 final Object id,
	                 final Branch branch) throws SQLException {
		
		Requires.notNull(statement);
		Requires.notNull(id);
		Requires.isInteger(id);
		Requires.notNull(branch);
		
		try {
			int index = 0;
			statement.setInt(++index, (int) id);
			
			statement.setInt(++index, branch.getDepotId());
			statement.setString(++index, branch.getName());
			statement.setLong(++index, branch.getHeadId());
			statement.setLong(++index, branch.getRootId());
			
			statement.executeUpdate();
			
			branch.id(id);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#update(java.lang.Object[])
	 */
	public void update(final Branch... objects) {
		// TODO Auto-generated method stub
		//
		throw new RuntimeException("Method 'update' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
}
