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

package org.mozkito.core.libs.versions.adapters.bulk;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.mozkito.core.libs.versions.model.Static;
import org.mozkito.core.libs.versions.model.enums.BranchMarker;
import org.mozkito.core.libs.versions.model.enums.ChangeType;
import org.mozkito.core.libs.versions.model.enums.IntegrationMarker;
import org.mozkito.core.libs.versions.model.enums.IntegrationType;
import org.mozkito.core.libs.versions.model.enums.NavigationMarker;
import org.mozkito.core.libs.versions.model.enums.ReferenceType;
import org.mozkito.libraries.sequel.Database.TxMode;
import org.mozkito.libraries.sequel.Database.Type;
import org.mozkito.libraries.sequel.bulk.AbstractAdapter;

/**
 * The Class StaticAdapter.
 *
 * @author Sascha Just
 */
public class StaticAdapter extends AbstractAdapter<Static> {
	
	/**
	 * Instantiates a new static adapter.
	 *
	 * @param type
	 *            the type
	 * @param mode
	 *            the mode
	 */
	public StaticAdapter(final Type type, final TxMode mode) {
		super(type, mode, "static");
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.bulk.AbstractAdapter#createScheme(java.sql.Connection)
	 */
	@Override
	public void createScheme(final Connection connection) {
		super.createScheme(connection);
		
		try {
			int index;
			PreparedStatement statement = connection.prepareStatement("INSERT INTO static_integration_types (value, name) VALUES (?, ?)");
			
			for (final IntegrationType type : IntegrationType.values()) {
				index = 0;
				statement.setShort(++index, type.getValue());
				statement.setString(++index, type.name());
				statement.addBatch();
			}
			statement.executeBatch();
			statement.close();
			
			statement = connection.prepareStatement("INSERT INTO static_integration_markers (value, name) VALUES (?, ?)");
			
			for (final IntegrationMarker type : IntegrationMarker.values()) {
				index = 0;
				statement.setShort(++index, type.getValue());
				statement.setString(++index, type.name());
				statement.addBatch();
			}
			statement.executeBatch();
			statement.close();
			
			statement = connection.prepareStatement("INSERT INTO static_change_types (value, name) VALUES (?, ?)");
			
			for (final ChangeType type : ChangeType.values()) {
				index = 0;
				statement.setShort(++index, type.getValue());
				statement.setString(++index, type.name());
				statement.addBatch();
			}
			statement.executeBatch();
			statement.close();
			
			statement = connection.prepareStatement("INSERT INTO static_reference_types (value, name) VALUES (?, ?)");
			
			for (final ReferenceType type : ReferenceType.values()) {
				index = 0;
				statement.setShort(++index, type.getValue());
				statement.setString(++index, type.name());
				statement.addBatch();
			}
			statement.executeBatch();
			statement.close();
			
			statement = connection.prepareStatement("INSERT INTO static_branch_markers (value, name) VALUES (?, ?)");
			
			for (final BranchMarker type : BranchMarker.values()) {
				index = 0;
				statement.setShort(++index, type.getValue());
				statement.setString(++index, type.name());
				statement.addBatch();
			}
			statement.executeBatch();
			statement.close();
			
			statement = connection.prepareStatement("INSERT INTO static_navigation_markers (value, name) VALUES (?, ?)");
			
			for (final NavigationMarker type : NavigationMarker.values()) {
				index = 0;
				statement.setShort(++index, type.getValue());
				statement.setString(++index, type.name());
				statement.addBatch();
			}
			statement.executeBatch();
			statement.close();
			
			connection.commit();
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.bulk.AbstractAdapter#save(org.mozkito.libraries.sequel.IEntity)
	 */
	@Override
	public void save(final Static entity) {
		// TODO Auto-generated method stub
		//
		throw new RuntimeException("Method 'save' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
}
