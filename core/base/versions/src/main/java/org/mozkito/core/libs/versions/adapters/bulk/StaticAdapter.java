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
