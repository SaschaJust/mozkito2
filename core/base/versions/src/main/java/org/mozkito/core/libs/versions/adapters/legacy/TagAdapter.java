/***********************************************************************************************************************
 * MIT License
 *  
 * Copyright (c) 2015 mozkito.org
 *  
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *  
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *  
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * 
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
