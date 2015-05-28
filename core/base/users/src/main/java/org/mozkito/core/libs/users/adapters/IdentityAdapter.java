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
import org.mozkito.skeleton.contracts.Requires;
import org.mozkito.skeleton.sequel.AbstractSequelAdapter;
import org.mozkito.skeleton.sequel.ResultIterator;
import org.mozkito.skeleton.sequel.SequelDatabase;

/**
 * The Class UserSequelAdapter.
 *
 * @author Sascha Just
 */
public class IdentityAdapter extends AbstractSequelAdapter<Identity> {
	
	/**
	 * Instantiates a new user sequel adapter.
	 *
	 * @param database
	 *            the database
	 */
	public IdentityAdapter(final SequelDatabase database) {
		super(database, "identity");
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
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#delete(java.lang.Object)
	 */
	public void delete(final Identity object) {
		// TODO Auto-generated method stub
		//
		throw new RuntimeException("Method 'delete' has not yet been implemented."); //$NON-NLS-1$
		
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
			final ResultSet results = statement.executeQuery("SELECT id, username, email, fullname FROM "
			        + "identities");
			return new ResultIterator<Identity>(this, results);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public Identity load(final long id) {
		Requires.notNull(id);
		Requires.isInteger(id);
		final int theId = (int) id;
		Requires.positive(theId);
		
		try {
			final Connection connection = this.database.getConnection();
			final PreparedStatement statement = connection.prepareStatement("SELECT id, username, email, fullname FROM "
			        + "identities" + " WHERE id = ?");
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
	
	@Override
	public List<Identity> load(final long... ids) {
		Requires.notNull(ids);
		
		try {
			
			final List<Identity> list = new LinkedList<>();
			for (final Object id : ids) {
				Requires.isInteger(id);
				
				final int theId = (int) id;
				
				Requires.positive(theId);
				
				final Connection connection = this.database.getConnection();
				final PreparedStatement statement = connection.prepareStatement("SELECT id, username, email, fullname FROM "
				        + "identities" + " WHERE id = ?");
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
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#save(java.sql.PreparedStatement, long, java.lang.Object)
	 */
	public void save(final PreparedStatement statement,
	                 final long id,
	                 final Identity identity) {
		Requires.notNull(statement);
		Requires.notNull(identity);
		
		try {
			
			int index = 0;
			statement.setLong(++index, id);
			
			statement.setString(++index, identity.getUserName());
			statement.setString(++index, identity.getEmail());
			statement.setString(++index, identity.getFullName());
			
			statement.executeUpdate();
			
			identity.id(id);
			Asserts.positive(identity.id());
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
			final Connection connection = this.database.getConnection();
			final PreparedStatement idStatement = prepareNextIdStatement();
			
			for (final Identity identity : identities) {
				
				long id = nextId(idStatement);
				
				PreparedStatement statement = null;
				if (id <= 0) {
					final ResultSet idResult = idStatement.executeQuery();
					final boolean result = idResult.next();
					Contract.asserts(result);
					id = idResult.getInt(1);
					
					statement = connection.prepareStatement("INSERT INTO " + "identities"
					        + " (id, username, email, fullname) VALUES (?, ?, ?, ?)");
					statement.setLong(1, id);
					statement.setString(2, identity.getUserName());
					statement.setString(3, identity.getEmail());
					statement.setString(4, identity.getFullName());
					identity.id(id);
				} else {
					statement = connection.prepareStatement("UPDATE " + "identities"
					        + " SET (username, email, fullname) = (?, ?, ?) WHERE id = ?");
					statement.setString(1, identity.getUserName());
					statement.setString(2, identity.getEmail());
					statement.setString(3, identity.getFullName());
					statement.setLong(4, id);
				}
				
				final int updates = statement.executeUpdate();
				
				Asserts.equalTo(1, updates);
			}
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
