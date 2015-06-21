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

import org.mozkito.core.libs.versions.model.Revision;
import org.mozkito.skeleton.contracts.Requires;
import org.mozkito.skeleton.sequel.AbstractAdapter;
import org.mozkito.skeleton.sequel.Database;

/**
 * @author Sascha Just
 *
 */
public class RevisionAdapter extends AbstractAdapter<Revision> {
	
	private static long currentId = 0l;
	
	/**
	 * Instantiates a new revision adapter.
	 *
	 * @param type
	 *            the type
	 */
	public RevisionAdapter(final Database.Type type) {
		super(type, "revision");
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.IAdapter#create(java.sql.ResultSet)
	 */
	public Revision create(final ResultSet result) {
		// TODO Auto-generated method stub
		// return null;
		throw new RuntimeException("Method 'create' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.IAdapter#delete(java.sql.Connection, java.lang.Object)
	 */
	public void delete(final Connection connection,
	                   final Revision object) {
		// TODO Auto-generated method stub
		//
		throw new RuntimeException("Method 'delete' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.IAdapter#load(java.sql.Connection)
	 */
	public Iterator<Revision> load(final Connection connection) {
		// TODO Auto-generated method stub
		// return null;
		throw new RuntimeException("Method 'load' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.IAdapter#load(java.sql.Connection, long[])
	 */
	public List<Revision> load(final Connection connection,
	                           final long... ids) {
		// TODO Auto-generated method stub
		// return null;
		throw new RuntimeException("Method 'load' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.IAdapter#load(java.sql.Connection, long)
	 */
	public Revision load(final Connection connection,
	                     final long id) {
		// TODO Auto-generated method stub
		// return null;
		throw new RuntimeException("Method 'load' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.IAdapter#nextId()
	 */
	public synchronized long nextId() {
		return ++currentId;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.IAdapter#save(java.sql.PreparedStatement, long, java.lang.Object)
	 */
	public void save(final PreparedStatement saveStatement,
	                 final long id,
	                 final Revision revision) {
		Requires.notNull(saveStatement);
		Requires.positive(id);
		Requires.notNull(revision);
		
		try {
			int index = 0;
			saveStatement.setLong(++index, id);
			saveStatement.setLong(++index, revision.getDepotId());
			saveStatement.setLong(++index, revision.getChangeSetId());
			saveStatement.setShort(++index, revision.getChangeType());
			saveStatement.setLong(++index, revision.getSourceId());
			saveStatement.setLong(++index, revision.getTargetId());
			saveStatement.setShort(++index, revision.getConfidence());
			saveStatement.setInt(++index, revision.getOldMode());
			saveStatement.setInt(++index, revision.getNewMode());
			saveStatement.setString(++index, revision.getOldHash());
			saveStatement.setString(++index, revision.getNewHash());
			saveStatement.setInt(++index, revision.getLinesIn());
			saveStatement.setInt(++index, revision.getLinesOut());
			
			saveStatement.addBatch();
			
			revision.id(id);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.IAdapter#update(java.sql.Connection, java.lang.Object[])
	 */
	public void update(final Connection connection,
	                   final Revision... objects) {
		// TODO Auto-generated method stub
		//
		throw new RuntimeException("Method 'update' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
}
