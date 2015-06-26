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

import org.mozkito.core.libs.versions.IntegrationType;
import org.mozkito.core.libs.versions.model.ChangeSetIntegration;
import org.mozkito.libraries.sequel.AbstractAdapter;
import org.mozkito.libraries.sequel.Database;
import org.mozkito.libraries.sequel.DatabaseManager;
import org.mozkito.skeleton.contracts.Requires;

// TODO: Auto-generated Javadoc
/**
 * The Class ChangeSetIntegrationAdapter.
 *
 * @author Sascha Just
 */
public class ChangeSetIntegrationAdapter extends AbstractAdapter<ChangeSetIntegration> {
	
	/** The type statement. */
	private final String typeStatement;
	
	/**
	 * Instantiates a new change set integration adapter.
	 *
	 * @param type
	 *            the type
	 * @param mode
	 *            the mode
	 */
	public ChangeSetIntegrationAdapter(final Database.Type type, final Database.TxMode mode) {
		super(type, mode, "changeset_integrationtype");
		this.typeStatement = DatabaseManager.loadStatement(type, "integration_type_save");
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see org.mozkito.libraries.sequel.IAdapter#create(java.sql.ResultSet)
	 */
	public ChangeSetIntegration create(final ResultSet result) {
		// TODO Auto-generated method stub
		// return null;
		throw new RuntimeException("Method 'create' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see org.mozkito.libraries.sequel.AbstractAdapter#createScheme(java.sql.Connection)
	 */
	@Override
	public void createScheme(final Connection connection) {
		super.createScheme(connection);
		try {
			final PreparedStatement statement = connection.prepareStatement(this.typeStatement);
			int index;
			for (final IntegrationType type : IntegrationType.values()) {
				index = 0;
				statement.setShort(++index, type.getValue());
				statement.setString(++index, type.name());
				schedule(statement);
			}
			execute(statement);
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
	                   final ChangeSetIntegration object) {
		// TODO Auto-generated method stub
		//
		throw new RuntimeException("Method 'delete' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see org.mozkito.libraries.sequel.IAdapter#load(java.sql.Connection)
	 */
	public Iterator<ChangeSetIntegration> load(final Connection connection) {
		// TODO Auto-generated method stub
		// return null;
		throw new RuntimeException("Method 'load' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see org.mozkito.libraries.sequel.IAdapter#load(java.sql.Connection, long[])
	 */
	public List<ChangeSetIntegration> load(final Connection connection,
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
	public ChangeSetIntegration load(final Connection connection,
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
	public void save(final PreparedStatement saveStatement,
	                 final long id,
	                 final ChangeSetIntegration integration) {
		Requires.notNull(saveStatement);
		Requires.notNull(integration);
		
		try {
			
			int index = 0;
			saveStatement.setLong(++index, integration.id());
			
			saveStatement.setShort(++index, integration.getIntegrationType());
			
			schedule(saveStatement);
			
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
	                   final ChangeSetIntegration... objects) {
		// TODO Auto-generated method stub
		//
		throw new RuntimeException("Method 'update' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
}
