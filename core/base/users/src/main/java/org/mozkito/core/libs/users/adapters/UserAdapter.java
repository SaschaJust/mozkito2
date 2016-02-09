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
import org.mozkito.libraries.sequel.Database;
import org.mozkito.libraries.sequel.legacy.AbstractAdapter;
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
	 * @see org.mozkito.libraries.sequel.legacy.IAdapter#create(java.sql.ResultSet)
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
	 * @see org.mozkito.libraries.sequel.legacy.IAdapter#delete(java.sql.Connection, java.lang.Object)
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
	 * @see org.mozkito.libraries.sequel.legacy.IAdapter#load(java.sql.Connection)
	 */
	public Iterator<User> load(final Connection connection) {
		// TODO Auto-generated method stub
		// return null;
		throw new RuntimeException("Method 'load' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.legacy.IAdapter#load(java.sql.Connection, long[])
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
	 * @see org.mozkito.libraries.sequel.legacy.IAdapter#load(java.sql.Connection, long)
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
	 * @see org.mozkito.libraries.sequel.legacy.IAdapter#save(java.sql.PreparedStatement, long, java.lang.Object)
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
	 * @see org.mozkito.libraries.sequel.legacy.IAdapter#update(java.sql.Connection, java.lang.Object[])
	 */
	public void update(final Connection connection,
	                   final User... objects) {
		// TODO Auto-generated method stub
		//
		throw new RuntimeException("Method 'update' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
}
