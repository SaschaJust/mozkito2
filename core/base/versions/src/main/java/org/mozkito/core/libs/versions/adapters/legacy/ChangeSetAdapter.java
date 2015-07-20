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