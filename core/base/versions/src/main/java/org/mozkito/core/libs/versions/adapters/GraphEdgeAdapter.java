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

import org.mozkito.core.libs.versions.model.GraphEdge;
import org.mozkito.libraries.sequel.AbstractAdapter;
import org.mozkito.libraries.sequel.Database;
import org.mozkito.skeleton.contracts.Requires;

// TODO: Auto-generated Javadoc
/**
 * The Class GraphEdgeAdapter.
 *
 * @author Sascha Just
 */
public class GraphEdgeAdapter extends AbstractAdapter<GraphEdge> {

	/**
	 * Instantiates a new graph edge adapter.
	 *
	 * @param type
	 *            the type
	 * @param mode
	 *            the mode
	 */
	public GraphEdgeAdapter(final Database.Type type, final Database.TxMode mode) {
		super(type, mode, "edge");
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.mozkito.libraries.sequel.IAdapter#create(java.sql.ResultSet)
	 */
	public GraphEdge create(final ResultSet result) {
		// TODO Auto-generated method stub
		// return null;
		throw new RuntimeException("Method 'create' has not yet been implemented."); //$NON-NLS-1$

	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.mozkito.libraries.sequel.IAdapter#delete(java.sql.Connection, java.lang.Object)
	 */
	public void delete(final Connection connection,
	                   final GraphEdge object) {
		// TODO Auto-generated method stub
		//
		throw new RuntimeException("Method 'delete' has not yet been implemented."); //$NON-NLS-1$

	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.mozkito.libraries.sequel.IAdapter#load(java.sql.Connection)
	 */
	public Iterator<GraphEdge> load(final Connection connection) {
		// TODO Auto-generated method stub
		// return null;
		throw new RuntimeException("Method 'load' has not yet been implemented."); //$NON-NLS-1$

	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.mozkito.libraries.sequel.IAdapter#load(java.sql.Connection, long[])
	 */
	public List<GraphEdge> load(final Connection connection,
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
	public GraphEdge load(final Connection connection,
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
	                 final GraphEdge edge) {
		Requires.notNull(saveStatement);
		Requires.notNull(edge);

		try {

			int index = 0;
			saveStatement.setLong(++index, id);

			saveStatement.setLong(++index, edge.depotId);
			saveStatement.setLong(++index, edge.sourceId);
			saveStatement.setLong(++index, edge.targetId);

			schedule(saveStatement);

			edge.id(id);
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
	                   final GraphEdge... objects) {
		// TODO Auto-generated method stub
		//
		throw new RuntimeException("Method 'update' has not yet been implemented."); //$NON-NLS-1$

	}

}
