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

package org.mozkito.core.libs.versions.adapters.legacy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Iterator;
import java.util.List;

import org.mozkito.core.libs.versions.model.Tag;
import org.mozkito.libraries.sequel.Database;
import org.mozkito.libraries.sequel.legacy.AbstractAdapter;
import org.mozkito.skeleton.contracts.Requires;

/**
 * The Class TagAdapter.
 *
 * @author Sascha Just
 */
public class TagAdapter extends AbstractAdapter<Tag> {
	
	/**
	 * Instantiates a new tag adapter.
	 *
	 * @param type
	 *            the database
	 * @param mode
	 *            the mode
	 */
	public TagAdapter(final Database.Type type, final Database.TxMode mode) {
		super(type, mode, "tags");
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see org.mozkito.libraries.sequel.legacy.IAdapter#create(java.sql.ResultSet)
	 */
	public Tag create(final ResultSet result) {
		// TODO Auto-generated method stub
		// return null;
		throw new RuntimeException("Method 'create' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see org.mozkito.libraries.sequel.legacy.IAdapter#delete(java.sql.Connection, java.lang.Object)
	 */
	public void delete(final Connection connection,
	                   final Tag object) {
		// TODO Auto-generated method stub
		//
		throw new RuntimeException("Method 'delete' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see org.mozkito.libraries.sequel.legacy.IAdapter#load(java.sql.Connection)
	 */
	public Iterator<Tag> load(final Connection connection) {
		// TODO Auto-generated method stub
		// return null;
		throw new RuntimeException("Method 'load' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see org.mozkito.libraries.sequel.legacy.IAdapter#load(java.sql.Connection, long[])
	 */
	public List<Tag> load(final Connection connection,
	                      final long... ids) {
		// TODO Auto-generated method stub
		// return null;
		throw new RuntimeException("Method 'load' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see org.mozkito.libraries.sequel.legacy.IAdapter#load(java.sql.Connection, long)
	 */
	public Tag load(final Connection connection,
	                final long id) {
		// TODO Auto-generated method stub
		// return null;
		throw new RuntimeException("Method 'load' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see org.mozkito.libraries.sequel.legacy.IAdapter#save(java.sql.PreparedStatement, long, java.lang.Object)
	 */
	public void save(final PreparedStatement statement,
	                 final long id,
	                 final Tag entity) {
		Requires.notNull(statement);
		Requires.notNull(entity);
		
		try {
			int index = 0;
			statement.setInt(++index, (int) id);
			statement.setLong(++index, entity.getDepotId());
			statement.setLong(++index, entity.getHeadId());
			
			if (entity.getName() == null) {
				statement.setNull(++index, Types.VARCHAR);
			} else {
				statement.setString(++index, truncate(entity.getName(), 900));
			}
			
			if (entity.getHash() == null) {
				statement.setNull(++index, Types.VARCHAR);
			} else {
				statement.setString(++index, entity.getHash());
			}
			
			if (entity.getMessage() == null) {
				statement.setNull(++index, Types.VARCHAR);
			} else {
				statement.setString(++index, entity.getMessage());
			}
			
			if (entity.getIdentityId() == null) {
				statement.setNull(++index, Types.BIGINT);
			} else {
				statement.setLong(++index, entity.getIdentityId());
			}
			
			if (entity.getTimestamp() == null) {
				statement.setNull(++index, Types.TIMESTAMP);
			} else {
				statement.setTimestamp(++index, Timestamp.from(entity.getTimestamp()));
			}
			
			schedule(statement);
			
			entity.setId(id);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see org.mozkito.libraries.sequel.legacy.IAdapter#update(java.sql.Connection, java.lang.Object[])
	 */
	public void update(final Connection connection,
	                   final Tag... objects) {
		// TODO Auto-generated method stub
		//
		throw new RuntimeException("Method 'update' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
}
