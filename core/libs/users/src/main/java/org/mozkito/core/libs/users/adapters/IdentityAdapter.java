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
import java.sql.Statement;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.mozkito.core.libs.users.model.Identity;
import org.mozkito.skeleton.contracts.Asserts;
import org.mozkito.skeleton.contracts.Contract;
import org.mozkito.skeleton.contracts.Ensures;
import org.mozkito.skeleton.contracts.Requires;
import org.mozkito.skeleton.logging.Logger;
import org.mozkito.skeleton.sequel.ISequelAdapter;
import org.mozkito.skeleton.sequel.ResultIterator;
import org.mozkito.skeleton.sequel.SequelDatabase;

/**
 * The Class UserSequelAdapter.
 *
 * @author Sascha Just
 */
public class IdentityAdapter implements ISequelAdapter<Identity> {
	
	/** The Constant TABLE_NAME. */
	public static final String   TABLE_NAME  = "identities";
	
	/** The Constant ID_SEQUENCE. */
	private static final String  ID_SEQUENCE = "seq_" + TABLE_NAME + "_id";
	
	/** The database. */
	private final SequelDatabase database;
	
	/**
	 * Instantiates a new user sequel adapter.
	 *
	 * @param database
	 *            the database
	 */
	public IdentityAdapter(final SequelDatabase database) {
		Requires.notNull(database);
		
		this.database = database;
		
		Ensures.notNull(database);
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#create(java.sql.ResultSet)
	 */
	@Override
	public Identity create(final ResultSet result) {
		try {
			final Identity identity = new Identity(result.getString(2), result.getString(3), result.getString(4));
			identity.id(result.getInt(1));
			return identity;
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#createConstraints()
	 */
	@Override
	public void createConstraints() {
		try {
			synchronized (this.database) {
				final Connection connection = this.database.getConnection();
				final Statement statement = connection.createStatement();
				statement.execute("ALTER TABLE " + TABLE_NAME + " ADD CONSTRAINT uq_" + TABLE_NAME
				        + "_username UNIQUE(username)");
				statement.execute("ALTER TABLE " + TABLE_NAME + " ADD CONSTRAINT uq_" + TABLE_NAME
				        + "_email UNIQUE(email)");
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
	@Override
	public void createIndexes() {
		try {
			synchronized (this.database) {
				final Connection connection = this.database.getConnection();
				final Statement statement = connection.createStatement();
				statement.execute("CREATE INDEX idx_" + TABLE_NAME + "_id ON " + TABLE_NAME + "(id)");
				statement.execute("CREATE INDEX idx_" + TABLE_NAME + "_email ON " + TABLE_NAME + "(email)");
				statement.execute("CREATE INDEX idx_" + TABLE_NAME + "_username ON " + TABLE_NAME + "(username)");
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
	@Override
	public void createScheme() {
		try {
			synchronized (this.database) {
				final Connection connection = this.database.getConnection();
				final Statement statement = connection.createStatement();
				statement.execute("CREATE TABLE " + TABLE_NAME + " (" + "id smallint PRIMARY KEY,"
				        + "username varchar(64)," + "email varchar(64)," + "fullname varchar(64))");
				statement.execute("CREATE SEQUENCE " + IdentityAdapter.ID_SEQUENCE + " START WITH 1 INCREMENT BY 1");
				connection.commit();
			}
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#delete(java.lang.Object)
	 */
	@Override
	public void delete(final Identity identity) {
		try {
			final Integer id = identity.id();
			
			Asserts.notNull(id);
			
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
						Logger.error(e, "Executing DELETE failed on user %s.", identity);
						try {
							connection.rollback();
						} catch (final SQLException e2) {
							Logger.error(e2, "Rolling back DELETE attempt failed on user %s.", identity);
							throw e2;
						}
						throw e;
					}
					
				}
				identity.id(-1);
			} else {
				if (Logger.logWarn()) {
					Logger.warn("Cannot delete" + TABLE_NAME + "with id <= 0.");
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
	@Override
	public Iterator<Identity> load() {
		try {
			final Connection connection = this.database.getConnection();
			final Statement statement = connection.createStatement();
			final ResultSet results = statement.executeQuery("SELECT id, username, email, fullname FROM " + TABLE_NAME);
			return new ResultIterator<Identity>(this, results);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#load(java.lang.Object)
	 */
	@Override
	public Identity load(final Object id) {
		Requires.notNull(id);
		Requires.isInteger(id);
		final int theId = (int) id;
		Requires.positive(theId);
		
		try {
			final Connection connection = this.database.getConnection();
			final PreparedStatement statement = connection.prepareStatement("SELECT id, username, email, fullname FROM "
			        + TABLE_NAME + " WHERE id = ?");
			statement.setInt(1, theId);
			
			final ResultSet result = statement.executeQuery();
			
			if (result.next()) {
				final Identity identity = create(result);
				
				Contract.asserts(!result.next());
				
				return identity;
			} else {
				return null;
			}
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#load(java.lang.Object[])
	 */
	@Override
	public List<Identity> load(final Object... ids) {
		Requires.notNull(ids);
		Requires.notEmpty(ids);
		
		try {
			
			final List<Identity> list = new LinkedList<>();
			for (final Object id : ids) {
				Requires.isInteger(id);
				
				final int theId = (int) id;
				
				Requires.positive(theId);
				
				final Connection connection = this.database.getConnection();
				final PreparedStatement statement = connection.prepareStatement("SELECT id, username, email, fullname FROM "
				        + TABLE_NAME + " WHERE id = ?");
				statement.setInt(1, theId);
				
				final ResultSet result = statement.executeQuery();
				
				while (result.next()) {
					final Identity identity = create(result);
					
					Contract.asserts(!result.next());
					list.add(identity);
				}
			}
			return list;
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#save(java.lang.Object[])
	 */
	@Override
	public void save(final Identity... identities) {
		Requires.notNull(identities);
		
		try {
			synchronized (this.database) {
				final Connection connection = this.database.getConnection();
				final Statement idquery = connection.createStatement();
				
				final PreparedStatement statement = connection.prepareStatement("INSERT INTO " + TABLE_NAME
				        + " (id, username, email, fullname) VALUES (?, ?, ?, ?)");
				
				for (final Identity identity : identities) {
					final ResultSet result = idquery.executeQuery(ISequelAdapter.getNextId(this.database.getType(),
					                                                                       ID_SEQUENCE));
					
					Contract.asserts(result.next());
					
					final int id = result.getInt(1);
					statement.setInt(1, id);
					statement.setString(2, identity.getUserName());
					statement.setString(3, identity.getEmail());
					statement.setString(4, identity.getFullName());
					
					final int updates = statement.executeUpdate();
					Asserts.equalTo(1, updates);
					identity.id(id);
				}
				
				try {
					connection.commit();
				} catch (final SQLException e) {
					for (final Identity identity : identities) {
						identity.id(-1);
					}
					
					connection.rollback();
					
				}
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
	@Override
	public void update(final Identity... identities) {
		Requires.notNull(identities);
		
		try {
			for (final Identity identity : identities) {
				final Object idO = identity.id();
				
				Asserts.notNull(idO);
				Asserts.isInteger(idO);
				
				int id = (int) idO;
				
				synchronized (this.database) {
					final Connection connection = this.database.getConnection();
					
					PreparedStatement statement = null;
					if (id <= 0) {
						statement = connection.prepareStatement(ISequelAdapter.getNextId(this.database.getType(),
						                                                                 ID_SEQUENCE));
						statement.setString(1, IdentityAdapter.ID_SEQUENCE);
						final ResultSet result = statement.executeQuery();
						
						Contract.asserts(result.next());
						
						id = result.getInt(1);
						statement = connection.prepareStatement("INSERT INTO " + TABLE_NAME
						        + " (id, username, email, fullname) VALUES (?, ?, ?, ?)");
						statement.setInt(1, id);
						statement.setString(2, identity.getUserName());
						statement.setString(3, identity.getEmail());
						statement.setString(4, identity.getFullName());
						identity.id(id);
					} else {
						statement = connection.prepareStatement("UPDATE " + TABLE_NAME
						        + " SET (username, email, fullname) = (?, ?, ?) WHERE id = ?");
						statement.setString(1, identity.getUserName());
						statement.setString(2, identity.getEmail());
						statement.setString(3, identity.getFullName());
						statement.setInt(4, id);
					}
					
					final int updates = statement.executeUpdate();
					
					Asserts.equalTo(1, updates);
					
					try {
						connection.commit();
					} catch (final SQLException e) {
						try {
							connection.rollback();
						} catch (final SQLException e2) {
							throw e2;
						}
						throw e;
					}
				}
			}
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
}
