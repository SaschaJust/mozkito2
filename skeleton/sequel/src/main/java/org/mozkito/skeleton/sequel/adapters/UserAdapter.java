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

package org.mozkito.skeleton.sequel.adapters;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

import org.mozkito.skeleton.contracts.Asserts;
import org.mozkito.skeleton.contracts.Contract;
import org.mozkito.skeleton.contracts.Ensures;
import org.mozkito.skeleton.contracts.Requires;
import org.mozkito.skeleton.logging.Logger;
import org.mozkito.skeleton.sequel.ISequelAdapter;
import org.mozkito.skeleton.sequel.ResultIterator;
import org.mozkito.skeleton.sequel.SequelDatabase;
import org.mozkito.skeleton.sequel.model.User;

/**
 * The Class UserSequelAdapter.
 *
 * @author Sascha Just
 */
public class UserAdapter implements ISequelAdapter<User> {

	/** The Constant ID_SEQUENCE. */
	private static final String  ID_SEQUENCE = "seq_users_id";

	/** The database. */
	private final SequelDatabase database;

	/**
	 * Instantiates a new user sequel adapter.
	 *
	 * @param database
	 *            the database
	 */
	public UserAdapter(final SequelDatabase database) {
		Requires.notNull(database);

		this.database = database;

		Ensures.notNull(database);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws SQLException
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#create(java.sql.ResultSet)
	 */
	@Override
	public User create(final ResultSet result) throws SQLException {
		final User user = new User(result.getString(2), result.getString(3), result.getString(4));
		user.id()[0] = result.getInt(1);
		return user;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws SQLException
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#createScheme()
	 */
	@Override
	public void createScheme() throws SQLException {
		synchronized (this.database) {
			final Connection connection = this.database.getConnection();
			final Statement statement = connection.createStatement();
			statement.execute("CREATE TABLE users (" + "id int," + "username varchar(256)," + "email varchar(256),"
					+ "fullname varchar(256))");
			statement.execute("CREATE SEQUENCE " + UserAdapter.ID_SEQUENCE + " START WITH 1 INCREMENT BY 1");
			connection.commit();

		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws SQLException
	 *
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#delete(java.lang.Object)
	 */
	@Override
	public void delete(final User user) throws SQLException {
		synchronized (this.database) {
			final Connection connection = this.database.getConnection();
			final PreparedStatement statement = connection.prepareStatement("DELETE FROM users WHERE id = ?");

			final Object[] ids = user.id();

			Asserts.notNull(ids);
			Asserts.validIndex(0, ids);
			Asserts.isInteger(ids[0]);

			final int id = (int) ids[0];

			if (id > 0) {
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

			} else {
				// TODO fix this
				Logger.warn.println("Cannot delete users with id <= 0.");
			}

			user.id()[0] = -1;
		}
	}

	/**
	 * Gets the next id.
	 *
	 * @return the next id
	 */
	private String getNextId() {
		switch (this.database.getType()) {
			case MSSQL:
				return "SELECT NEXT VALUE FOR " + UserAdapter.ID_SEQUENCE;
			case DERBY:
				return "VALUES (NEXT VALUE FOR " + UserAdapter.ID_SEQUENCE + ")";
			case POSTGRES:
				return "SELECT nextval'" + UserAdapter.ID_SEQUENCE + "'";
		}

		return "SELECT NEXT VALUE FOR " + UserAdapter.ID_SEQUENCE;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws SQLException
	 *
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#load()
	 */
	@Override
	public Iterator<User> load() throws SQLException {
		final Connection connection = this.database.getConnection();
		final Statement statement = connection.createStatement();
		final ResultSet results = statement.executeQuery("SELECT id, username, email, fullname FROM users");
		return new ResultIterator<User>(this, results);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws SQLException
	 *
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#load(java.lang.Object, java.lang.Object[])
	 */
	@Override
	public User load(final Object id,
	                 final Object... ids) throws SQLException {
		Requires.notNull(id);
		Requires.isInteger(id);

		final int theId = (int) id;

		Requires.positive(theId);

		final Connection connection = this.database.getConnection();
		final PreparedStatement statement = connection.prepareStatement("SELECT id, username, email, fullname FROM users WHERE id = ?");
		statement.setInt(1, theId);

		final ResultSet result = statement.executeQuery();

		if (result.next()) {
			final User user = create(result);

			Contract.asserts(!result.next());

			return user;
		} else {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws SQLException
	 *
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#save(java.lang.Object)
	 */
	@Override
	public void save(final User user) throws SQLException {
		Requires.notNull(user);

		synchronized (this.database) {
			final Connection connection = this.database.getConnection();
			final Statement idquery = connection.createStatement();
			final ResultSet result = idquery.executeQuery(getNextId());

			Contract.asserts(result.next());

			final int id = result.getInt(1);

			final PreparedStatement statement = connection.prepareStatement("INSERT INTO users (id, username, email, fullname) VALUES (?, ?, ?, ?)");
			statement.setInt(1, id);
			statement.setString(2, user.getUserName());
			statement.setString(3, user.getEmail());
			statement.setString(4, user.getFullName());

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

	/**
	 * {@inheritDoc}
	 *
	 * @throws SQLException
	 *
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#update(java.lang.Object)
	 */
	@Override
	public void update(final User user) throws SQLException {
		Requires.notNull(user);

		final Object[] ids = user.id();

		Asserts.notNull(ids);
		Asserts.validIndex(0, ids);
		Asserts.isInteger(ids[0]);

		int id = (int) ids[0];

		synchronized (this.database) {
			final Connection connection = this.database.getConnection();

			PreparedStatement statement = null;
			if (id <= 0) {
				statement = connection.prepareStatement(getNextId());
				statement.setString(1, UserAdapter.ID_SEQUENCE);
				final ResultSet result = statement.executeQuery();

				Contract.asserts(result.next());

				id = result.getInt(1);
				statement = connection.prepareStatement("INSERT INTO users (id, username, email, fullname) VALUES (?, ?, ?, ?)");
				statement.setInt(1, id);
				statement.setString(2, user.getUserName());
				statement.setString(3, user.getEmail());
				statement.setString(4, user.getFullName());
			} else {
				statement = connection.prepareStatement("UPDATE users SET (username, email, fullname) = (?, ?, ?) WHERE id = ?");
				statement.setString(1, user.getUserName());
				statement.setString(2, user.getEmail());
				statement.setString(3, user.getFullName());
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

}
