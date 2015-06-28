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

package org.mozkito.core.libs.users.adapters;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import org.mozkito.core.libs.users.model.User;
import org.mozkito.core.libs.versions.model.Identity;
import org.mozkito.libraries.sequel.AbstractAdapter;
import org.mozkito.libraries.sequel.Database;
import org.mozkito.skeleton.contracts.Asserts;
import org.mozkito.skeleton.contracts.Requires;

/**
 * The Class UserAdapter.
 *
 * @author Sascha Just
 */
public class UserAdapter extends AbstractAdapter<User> {
	
	/**
	 * The Class UserIterator.
	 */
	public static final class UserIterator implements Iterator<User> {
		
		/** The results. */
		private final ResultSet results;
		
		/** The user. */
		private User            user;
		
		/** The tmp user. */
		private User            tmpUser;
		
		/** The last id. */
		private int             lastId = -1;
		
		/** The identity. */
		private Identity        identity;
		
		/**
		 * Instantiates a new user iterator.
		 *
		 * @param results
		 *            the results
		 */
		public UserIterator(final ResultSet results) {
			this.results = results;
			try {
				fetchNext();
			} catch (final SQLException e) {
				throw new RuntimeException(e);
			}
		}
		
		/**
		 * Fetch next.
		 *
		 * @throws SQLException
		 *             the SQL exception
		 */
		private void fetchNext() throws SQLException {
			int id = -1;
			boolean doReturn = false;
			
			while (this.results.next()) {
				id = this.results.getInt(1);
				if (id != this.lastId) {
					if (this.lastId != -1) {
						doReturn = true;
					}
					
					this.lastId = id;
					this.user = this.tmpUser;
					this.tmpUser = new User();
					this.tmpUser.setId(id);
					
					this.identity = new Identity(this.results.getString(3), this.results.getString(4));
					this.identity.setId(this.results.getInt(2));
					this.tmpUser.addIdentity(this.identity);
					if (doReturn) {
						return;
					}
				} else {
					this.identity = new Identity(this.results.getString(3), this.results.getString(4));
					this.identity.setId(this.results.getInt(2));
					this.tmpUser.addIdentity(this.identity);
				}
			}
		}
		
		/**
		 * {@inheritDoc}
		 * 
		 * @see java.util.Iterator#hasNext()
		 */
		@Override
		public boolean hasNext() {
			return this.user != null;
		}
		
		/**
		 * Next.
		 * 
		 * final * @return the user
		 *
		 * @return the user
		 */
		@Override
		public User next() {
			if (hasNext()) {
				final User ret = this.user;
				try {
					fetchNext();
				} catch (final SQLException e) {
					throw new RuntimeException(e);
				}
				return ret;
			}
			throw new NoSuchElementException();
		}
	}
	
	private static long currentId = 0l;
	
	/**
	 * Instantiates a new user adapter.
	 *
	 * @param type
	 *            the database
	 */
	public UserAdapter(final Database.Type type, final Database.TxMode mode) {
		super(type, mode, "user");
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.IAdapter#create(java.sql.ResultSet)
	 */
	public User create(final ResultSet results) {
		Requires.notNull(results);
		
		try {
			
			User user = new User();
			int id;
			int lastId = -1;
			Identity identity;
			final List<User> list = new LinkedList<>();
			
			while (results.next()) {
				id = results.getInt(1);
				
				if (id != lastId) {
					if (lastId != -1) {
						return user;
					}
					lastId = id;
					list.add(user);
					user = new User();
					user.setId(id);
					
					identity = new Identity(results.getString(3), results.getString(4));
					identity.setId(results.getInt(2));
					user.addIdentity(identity);
				} else {
					identity = new Identity(results.getString(3), results.getString(4));
					identity.setId(results.getInt(2));
					user.addIdentity(identity);
				}
			}
			
			return user;
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.IAdapter#delete(java.sql.Connection, java.lang.Object)
	 */
	public void delete(final Connection connection,
	                   final User object) {
		// TODO Auto-generated method stub
		//
		throw new RuntimeException("Method 'delete' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.IAdapter#load(java.sql.Connection)
	 */
	public Iterator<User> load(final Connection connection) {
		// TODO Auto-generated method stub
		// return null;
		throw new RuntimeException("Method 'load' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.IAdapter#load(java.sql.Connection, long[])
	 */
	public List<User> load(final Connection connection,
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
	public User load(final Connection connection,
	                 final long id) {
		// TODO Auto-generated method stub
		// return null;
		throw new RuntimeException("Method 'load' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.IAdapter#save(java.sql.PreparedStatement, long, java.lang.Object)
	 */
	public void save(final PreparedStatement statement,
	                 final long id,
	                 final User user) {
		Requires.notNull(statement);
		Requires.notNull(user);
		
		try {
			for (final Identity identity : user.getIdentities()) {
				int index = 0;
				statement.setLong(++index, id);
				statement.setLong(++index, identity.getId());
				index = 0;
			}
			
			schedule(statement);
			
			user.setId(id);
			Asserts.positive(user.getId());
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
	                   final User... objects) {
		// TODO Auto-generated method stub
		//
		throw new RuntimeException("Method 'update' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
}
