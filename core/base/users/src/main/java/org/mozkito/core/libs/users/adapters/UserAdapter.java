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

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import org.mozkito.core.libs.users.model.Identity;
import org.mozkito.core.libs.users.model.User;
import org.mozkito.skeleton.contracts.Asserts;
import org.mozkito.skeleton.contracts.Contract;
import org.mozkito.skeleton.contracts.Ensures;
import org.mozkito.skeleton.contracts.Requires;
import org.mozkito.skeleton.logging.Logger;
import org.mozkito.skeleton.sequel.ISequelAdapter;
import org.mozkito.skeleton.sequel.SequelDatabase;
import org.mozkito.skeleton.sequel.SequelManager;

/**
 * The Class UserAdapter.
 *
 * @author Sascha Just
 */
public class UserAdapter implements ISequelAdapter<User> {
	
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
					this.tmpUser.id(id);
					
					this.identity = new Identity(this.results.getString(3), this.results.getString(4),
					                             this.results.getString(5));
					this.identity.id(this.results.getInt(2));
					this.tmpUser.addIdentity(this.identity);
					if (doReturn) {
						return;
					}
				} else {
					this.identity = new Identity(this.results.getString(3), this.results.getString(4),
					                             this.results.getString(5));
					this.identity.id(this.results.getInt(2));
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
	
	/** The Constant TABLE_NAME. */
	private static final String  TABLE_NAME  = "users";
	
	/** The Constant ID_SEQUENCE. */
	private static final String  ID_SEQUENCE = "seq_" + TABLE_NAME + "_id";
	
	/** The database. */
	private final SequelDatabase database;
	
	private final String         saveStatement;
	
	private final String         nextIdStatement;
	
	/**
	 * Instantiates a new user adapter.
	 *
	 * @param database
	 *            the database
	 */
	public UserAdapter(final SequelDatabase database) {
		Requires.notNull(database);
		
		this.database = database;
		this.saveStatement = SequelManager.loadStatement(database, "user_save");
		this.nextIdStatement = SequelManager.loadStatement(database, "user_nextid");
		
		Ensures.notNull(database);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#create(java.sql.ResultSet)
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
					user.id(id);
					
					identity = new Identity(results.getString(3), results.getString(4), results.getString(5));
					identity.id(results.getInt(2));
					user.addIdentity(identity);
				} else {
					identity = new Identity(results.getString(3), results.getString(4), results.getString(5));
					identity.id(results.getInt(2));
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
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#createConstraints()
	 */
	public void createConstraints() {
		try {
			synchronized (this.database) {
				final Connection connection = this.database.getConnection();
				final Statement statement = connection.createStatement();
				statement.execute("CREATE TABLE " + TABLE_NAME + " (user_id smallint, identity_id smallint)");
				statement.execute("CREATE SEQUENCE " + ID_SEQUENCE + " MINVALUE 1" + " START WITH 1"
				        + " INCREMENT BY 1");
				connection.commit();
			}
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#createIndexes()
	 */
	public void createIndexes() {
		try {
			synchronized (this.database) {
				final Connection connection = this.database.getConnection();
				final Statement statement = connection.createStatement();
				statement.execute("CREATE INDEX idx_" + TABLE_NAME + "_user_id_identity_id ON " + TABLE_NAME
				        + "(user_id, identity_id)");
				statement.execute("CREATE INDEX idx_" + TABLE_NAME + "_identity_id ON " + TABLE_NAME + "(identity_id)");
				connection.commit();
			}
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#createScheme()
	 */
	public void createScheme() {
		try {
			synchronized (this.database) {
				SequelManager.executeSQL(this.database, "user_create_schema");
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
	public void delete(final User user) {
		Requires.notNull(user);
		
		final Object idO = user.id();
		
		Asserts.notNull(idO);
		Asserts.isInteger(idO);
		
		try {
			final int id = (int) idO;
			
			if (id > 0) {
				synchronized (this.database) {
					final Connection connection = this.database.getConnection();
					final PreparedStatement statement = connection.prepareStatement("DELETE FROM " + TABLE_NAME
					        + " WHERE id = ?");
					statement.setInt(1, id);
					statement.executeUpdate();
					
					try {
						connection.commit();
					} catch (final SQLException e) {
						Logger.error(e, "Executing DELETE failed on user %s.", user);
						try {
							connection.rollback();
						} catch (final SQLException e2) {
							Logger.error(e2, "Rolling back DELETE attempt failed on user %s.", user);
							throw e2;
						}
						throw e;
					}
					
				}
				user.id(-1);
			} else {
				if (Logger.logWarn()) {
					Logger.warn("Cannot delete user from table " + TABLE_NAME + " with id <= 0.");
				}
			}
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#load()
	 */
	public Iterator<User> load() {
		Asserts.notNull(this.database);
		try {
			final Connection connection = this.database.getConnection();
			final Statement statement = connection.createStatement();
			final ResultSet results = statement.executeQuery("SELECT user_id, identity_id, username, email, fullname FROM "
			        + TABLE_NAME + " ut INNER JOIN " + IdentityAdapter.TABLE_NAME + " it ON (ut.identity_id = it.id)");
			
			return new UserIterator(results);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#load(java.lang.Object)
	 */
	public User load(final Object id) {
		Asserts.notNull(id);
		Asserts.isInteger(id);
		try {
			final int theId = (int) id;
			final Connection connection = this.database.getConnection();
			final PreparedStatement statement = connection.prepareStatement("SELECT identity_id FROM " + TABLE_NAME
			        + " WHERE user_id = ?");
			
			statement.setInt(1, theId);
			
			final User user = new User();
			final ResultSet results = statement.executeQuery();
			final ISequelAdapter<Identity> adapter = new IdentityAdapter(this.database);
			while (results.next()) {
				user.addIdentity(adapter.load(results.getInt(1)));
			}
			
			user.id(theId);
			
			return user;
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#load(java.lang.Object[])
	 */
	public List<User> load(final Object... ids) {
		Asserts.notNull(this.database);
		
		try {
			final Connection connection = this.database.getConnection();
			
			final StringBuilder builder = new StringBuilder();
			builder.append("SELECT user_id, identity_id FROM, username, email, password ");
			builder.append(TABLE_NAME);
			builder.append(" ut INNER JOIN ");
			builder.append(IdentityAdapter.TABLE_NAME);
			builder.append(" it ON (ut.identity_id = it.id) WHERE user_id IN (");
			final StringBuilder marks = new StringBuilder();
			for (@SuppressWarnings ("unused")
			final Object id : ids) {
				if (marks.length() != 0) {
					marks.append(", ");
				}
				marks.append("?");
			}
			builder.append(")");
			
			final String query = builder.toString();
			final PreparedStatement statement = connection.prepareStatement(query);
			
			for (int i = 0; i < ids.length; ++i) {
				Asserts.validIndex(i, ids);
				Requires.isInteger(ids[i]);
				statement.setInt(i + 1, (Integer) ids[i]);
			}
			
			final ResultSet results = statement.executeQuery();
			
			User user = new User();
			int id;
			int lastId = -1;
			Identity identity;
			final List<User> list = new LinkedList<>();
			
			while (results.next()) {
				id = results.getInt(1);
				if (id != lastId) {
					lastId = id;
					list.add(user);
					user = new User();
					user.id(id);
					
					identity = new Identity(results.getString(3), results.getString(4), results.getString(5));
					identity.id(results.getInt(2));
					user.addIdentity(identity);
				} else {
					identity = new Identity(results.getString(3), results.getString(4), results.getString(5));
					identity.id(results.getInt(2));
					user.addIdentity(identity);
				}
			}
			
			return list;
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * @param statement
	 * @param id
	 * @param users
	 */
	private void save(final PreparedStatement statement,
	                  final int id,
	                  final User user) {
		Requires.notNull(statement);
		Requires.notNull(id);
		Requires.isInteger(id);
		Requires.notNull(user);
		
		try {
			int index = 0;
			statement.setInt(++index, id);
			
			for (final Identity identity : user.getIdentities()) {
				statement.setInt(++index, identity.id());
			}
			
			statement.executeUpdate();
			
			user.id(id);
			Asserts.positive(user.id());
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#save(java.lang.Object[])
	 */
	public void save(final User... users) {
		Requires.notNull(users);
		
		try {
			final Connection connection = this.database.getConnection();
			final PreparedStatement statement = connection.prepareStatement(this.saveStatement);
			final PreparedStatement idStatement = connection.prepareStatement(this.nextIdStatement);
			
			for (final User user : users) {
				final ResultSet idResult = idStatement.executeQuery();
				final boolean result = idResult.next();
				Contract.asserts(result);
				final int id = idResult.getInt(1);
				
				save(statement, id, user);
			}
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#update(java.lang.Object[])
	 */
	public void update(final User... objects) {
		throw new RuntimeException("Method 'update' has not yet been implemented."); //$NON-NLS-1$
	}
	
}
