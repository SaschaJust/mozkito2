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
import java.util.Iterator;
import java.util.List;

import org.mozkito.core.libs.versions.model.ChangeSet;
import org.mozkito.libraries.sequel.Database;
import org.mozkito.libraries.sequel.legacy.AbstractAdapter;
import org.mozkito.skeleton.contracts.Requires;

/**
 * The Class ChangeSetAdapter, which is used to load and store {@link ChangeSet} entities from/to a database.
 *
 * @author Sascha Just
 */
public class ChangeSetAdapter extends AbstractAdapter<ChangeSet> {
	
	/**
	 * Instantiates a new change set adapter.
	 *
	 * @param type
	 *            the type
	 * @param mode
	 *            the mode
	 */
	public ChangeSetAdapter(final Database.Type type, final Database.TxMode mode) {
		super(type, mode, "changesets");
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see org.mozkito.libraries.sequel.legacy.IAdapter#create(java.sql.ResultSet)
	 */
	public ChangeSet create(final ResultSet result) {
		// TODO Auto-generated method stub
		// return null;
		throw new RuntimeException("Method 'create' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see org.mozkito.libraries.sequel.legacy.IAdapter#delete(java.sql.Connection,
	 *      org.mozkito.libraries.sequel.IEntity)
	 */
	public void delete(final Connection connection,
	                   final ChangeSet object) {
		// TODO Auto-generated method stub
		//
		throw new RuntimeException("Method 'delete' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see org.mozkito.libraries.sequel.legacy.IAdapter#load(java.sql.Connection)
	 */
	public Iterator<ChangeSet> load(final Connection connection) {
		// TODO Auto-generated method stub
		// return null;
		throw new RuntimeException("Method 'load' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see org.mozkito.libraries.sequel.legacy.IAdapter#load(java.sql.Connection, long[])
	 */
	public List<ChangeSet> load(final Connection connection,
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
	public ChangeSet load(final Connection connection,
	                      final long id) {
		// TODO Auto-generated method stub
		// return null;
		throw new RuntimeException("Method 'load' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see org.mozkito.libraries.sequel.legacy.IAdapter#save(java.sql.PreparedStatement, long,
	 *      org.mozkito.libraries.sequel.IEntity)
	 */
	public void save(final PreparedStatement saveStatement,
	                 final long id,
	                 final ChangeSet changeSet) {
		Requires.notNull(saveStatement);
		Requires.notNull(changeSet);
		
		try {
			
			int index = 0;
			saveStatement.setLong(++index, id);
			
			saveStatement.setString(++index, changeSet.getCommitHash());
			
			saveStatement.setString(++index, changeSet.getTreeHash());
			
			saveStatement.setTimestamp(++index, Timestamp.from(changeSet.getAuthoredTime()));
			saveStatement.setLong(++index, changeSet.getAuthorId());
			
			saveStatement.setTimestamp(++index, Timestamp.from(changeSet.getCommitTime()));
			saveStatement.setLong(++index, changeSet.getCommitterId());
			
			saveStatement.setString(++index, changeSet.getSubject());
			saveStatement.setString(++index, changeSet.getBody());
			
			schedule(saveStatement);
			
			changeSet.setId(id);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see org.mozkito.libraries.sequel.legacy.IAdapter#update(java.sql.Connection,
	 *      org.mozkito.libraries.sequel.IEntity[])
	 */
	public void update(final Connection connection,
	                   final ChangeSet... objects) {
		// TODO Auto-generated method stub
		//
		throw new RuntimeException("Method 'update' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
}
