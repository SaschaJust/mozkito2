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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.mozkito.core.libs.versions.model.Root;
import org.mozkito.libraries.sequel.AbstractAdapter;
import org.mozkito.libraries.sequel.Database;
import org.mozkito.skeleton.contracts.Requires;

/**
 * The Class RootAdapter.
 *
 * @author Sascha Just
 */
public class RootAdapter extends AbstractAdapter<Root> {
	
	private static long currentId = 0l;
	
	/**
	 * Instantiates a new Root adapter.
	 *
	 * @param type
	 *            the database
	 */
	public RootAdapter(final Database.Type type, final Database.TxMode mode) {
		super(type, mode, "root");
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.IAdapter#create(java.sql.ResultSet)
	 */
	public Root create(final ResultSet result) {
		// TODO Auto-generated method stub
		// return null;
		throw new RuntimeException("Method 'create' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.IAdapter#delete(java.sql.Connection, java.lang.Object)
	 */
	public void delete(final Connection connection,
	                   final Root object) {
		// TODO Auto-generated method stub
		//
		throw new RuntimeException("Method 'delete' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.IAdapter#load(java.sql.Connection)
	 */
	public Iterator<Root> load(final Connection connection) {
		// TODO Auto-generated method stub
		// return null;
		throw new RuntimeException("Method 'load' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.IAdapter#load(java.sql.Connection, long[])
	 */
	public List<Root> load(final Connection connection,
	                       final long... ids) {
		// TODO Auto-generated method stub
		// return null;
		throw new RuntimeException("Method 'load' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.IAdapter#load(java.sql.Connection, long)
	 */
	public Root load(final Connection connection,
	                 final long id) {
		// TODO Auto-generated method stub
		// return null;
		throw new RuntimeException("Method 'load' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.IAdapter#nextId()
	 */
	public synchronized long nextId() {
		return ++currentId;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.IAdapter#save(java.sql.PreparedStatement, long, java.lang.Object)
	 */
	public void save(final PreparedStatement statement,
	                 final long id,
	                 final Root root) {
		Requires.notNull(statement);
		Requires.notNull(root);
		
		try {
			int index;
			index = 0;
			statement.setInt(++index, (int) id);
			statement.setLong(++index, root.getBranchId());
			statement.setLong(++index, root.getChangeSetId());
			
			schedule(statement);
			
			root.id(id);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.IAdapter#update(java.sql.Connection, java.lang.Object[])
	 */
	public void update(final Connection connection,
	                   final Root... objects) {
		// TODO Auto-generated method stub
		//
		throw new RuntimeException("Method 'update' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
}
