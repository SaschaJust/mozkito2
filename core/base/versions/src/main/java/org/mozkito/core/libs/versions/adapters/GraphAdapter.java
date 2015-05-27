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

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.mozkito.core.libs.versions.DepotGraph;
import org.mozkito.core.libs.versions.DepotGraph.Edge;
import org.mozkito.libraries.sequel.ISequelAdapter;
import org.mozkito.libraries.sequel.SequelDatabase;
import org.mozkito.libraries.sequel.SequelManager;
import org.mozkito.skeleton.contracts.Contract;
import org.mozkito.skeleton.contracts.Requires;

// TODO: Auto-generated Javadoc
/**
 * The Class GraphAdapter.
 *
 * @author Sascha Just
 */
public class GraphAdapter implements ISequelAdapter<DepotGraph> {
	
	/** The database. */
	private final SequelDatabase database;
	
	/** The edge save statement. */
	private final String         edgeSaveStatement;
	
	/** The edge next id statement. */
	private final String         edgeNextIdStatement;
	
	/** The branch edge next id statement. */
	private final String         branchEdgeNextIdStatement;
	
	/** The branch edge save statement. */
	private final String         branchEdgeSaveStatement;
	
	/** The integration edge next id statement. */
	private final String         integrationEdgeNextIdStatement;
	
	/** The integration edge save statement. */
	private final String         integrationEdgeSaveStatement;
	
	/**
	 * Instantiates a new graph adapter.
	 *
	 * @param database
	 *            the database
	 */
	public GraphAdapter(final SequelDatabase database) {
		this.database = database;
		this.edgeSaveStatement = SequelManager.loadStatement(database, "graph_edge_save");
		this.edgeNextIdStatement = SequelManager.loadStatement(database, "graph_edge_nextid");
		this.branchEdgeSaveStatement = SequelManager.loadStatement(database, "graph_branch_edge_save");
		this.branchEdgeNextIdStatement = SequelManager.loadStatement(database, "graph_branch_edge_nextid");
		this.integrationEdgeSaveStatement = SequelManager.loadStatement(database, "graph_integration_edge_save");
		this.integrationEdgeNextIdStatement = SequelManager.loadStatement(database, "graph_integration_edge_nextid");
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.ISequelAdapter#create(java.sql.ResultSet)
	 */
	public DepotGraph create(final ResultSet result) {
		// TODO Auto-generated method stub
		// return null;
		throw new RuntimeException("Method 'create' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.ISequelAdapter#createConstraints()
	 */
	public void createConstraints() {
		// TODO Auto-generated method stub
		//
		throw new RuntimeException("Method 'createConstraints' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.ISequelAdapter#createIndexes()
	 */
	public void createIndexes() {
		// TODO Auto-generated method stub
		//
		throw new RuntimeException("Method 'createIndexes' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.ISequelAdapter#createScheme()
	 */
	public void createScheme() {
		try {
			synchronized (this.database) {
				SequelManager.executeSQL(this.database, "graph_edge_create_schema");
				SequelManager.executeSQL(this.database, "graph_branch_edge_create_schema");
				SequelManager.executeSQL(this.database, "graph_integration_edge_create_schema");
			}
		} catch (final SQLException | IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.ISequelAdapter#delete(java.lang.Object)
	 */
	public void delete(final DepotGraph object) {
		// TODO Auto-generated method stub
		//
		throw new RuntimeException("Method 'delete' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.ISequelAdapter#load()
	 */
	public Iterator<DepotGraph> load() {
		// TODO Auto-generated method stub
		// return null;
		throw new RuntimeException("Method 'load' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.ISequelAdapter#load(java.lang.Object[])
	 */
	public List<DepotGraph> load(final Object... ids) {
		// TODO Auto-generated method stub
		// return null;
		throw new RuntimeException("Method 'load' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.ISequelAdapter#load(java.lang.Object)
	 */
	public DepotGraph load(final Object id) {
		// TODO Auto-generated method stub
		// return null;
		throw new RuntimeException("Method 'load' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.ISequelAdapter#save(java.lang.Object[])
	 */
	public void save(final DepotGraph... depotGraphs) {
		Requires.notNull(depotGraphs);
		
		try {
			final Connection connection = this.database.getConnection();
			final PreparedStatement edgeStatement = connection.prepareStatement(this.edgeSaveStatement);
			final PreparedStatement edgeIdStatement = connection.prepareStatement(this.edgeNextIdStatement);
			final PreparedStatement branchEdgeStatement = connection.prepareStatement(this.branchEdgeSaveStatement);
			final PreparedStatement branchEdgeIdStatement = connection.prepareStatement(this.branchEdgeNextIdStatement);
			final PreparedStatement integrationEdgeStatement = connection.prepareStatement(this.integrationEdgeSaveStatement);
			final PreparedStatement integrationEdgeIdStatement = connection.prepareStatement(this.integrationEdgeNextIdStatement);
			
			for (final DepotGraph depotGraph : depotGraphs) {
				save(edgeStatement, edgeIdStatement, branchEdgeStatement, branchEdgeIdStatement,
				     integrationEdgeStatement, integrationEdgeIdStatement, depotGraph);
			}
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Save.
	 *
	 * @param edgeStatement
	 *            the edge statement
	 * @param edgeIdStatement
	 *            the edge id statement
	 * @param branchEdgeStatement
	 *            the branch edge statement
	 * @param branchEdgeIdStatement
	 *            the branch edge id statement
	 * @param integrationEdgeStatement
	 *            the integration edge statement
	 * @param integrationEdgeIdStatement
	 *            the integration edge id statement
	 * @param depotGraph
	 *            the depot graph
	 */
	private void save(final PreparedStatement edgeStatement,
	                  final PreparedStatement edgeIdStatement,
	                  final PreparedStatement branchEdgeStatement,
	                  final PreparedStatement branchEdgeIdStatement,
	                  final PreparedStatement integrationEdgeStatement,
	                  final PreparedStatement integrationEdgeIdStatement,
	                  final DepotGraph depotGraph) {
		Requires.notNull(edgeStatement);
		Requires.notNull(edgeIdStatement);
		Requires.notNull(branchEdgeStatement);
		Requires.notNull(branchEdgeIdStatement);
		Requires.notNull(integrationEdgeStatement);
		Requires.notNull(integrationEdgeIdStatement);
		
		try {
			ResultSet edgeIdResult;
			boolean result;
			long edgeId, branchEdgeId;
			long integrationEdgeId;
			int index = 0;
			
			for (final Edge edge : depotGraph.getEdges()) {
				edgeIdResult = edgeIdStatement.executeQuery();
				result = edgeIdResult.next();
				Contract.asserts(result);
				edgeId = edgeIdResult.getLong(1);
				index = 0;
				
				edgeStatement.setLong(++index, edgeId);
				edgeStatement.setInt(++index, depotGraph.getDepot().id());
				edgeStatement.setLong(++index, edge.getSourceId());
				edgeStatement.setLong(++index, edge.getTargetId());
				edgeStatement.setShort(++index, edge.getType());
				edgeStatement.executeUpdate();
				
				for (final Integer branchId : edge.getBranchIds()) {
					edgeIdResult = branchEdgeIdStatement.executeQuery();
					result = edgeIdResult.next();
					Contract.asserts(result);
					branchEdgeId = edgeIdResult.getLong(1);
					index = 0;
					
					branchEdgeStatement.setLong(++index, branchEdgeId);
					branchEdgeStatement.setInt(++index, depotGraph.getDepot().id());
					branchEdgeStatement.setLong(++index, edgeId);
					branchEdgeStatement.setInt(++index, branchId);
					branchEdgeStatement.executeUpdate();
				}
				
				for (final Integer branchId : edge.getIntegrationPathIds()) {
					edgeIdResult = integrationEdgeIdStatement.executeQuery();
					result = edgeIdResult.next();
					Contract.asserts(result);
					integrationEdgeId = edgeIdResult.getLong(1);
					index = 0;
					
					integrationEdgeStatement.setLong(++index, integrationEdgeId);
					integrationEdgeStatement.setInt(++index, depotGraph.getDepot().id());
					integrationEdgeStatement.setLong(++index, edgeId);
					integrationEdgeStatement.setInt(++index, branchId);
					integrationEdgeStatement.executeUpdate();
				}
			}
			
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.ISequelAdapter#update(java.lang.Object[])
	 */
	public void update(final DepotGraph... objects) {
		// TODO Auto-generated method stub
		//
		throw new RuntimeException("Method 'update' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
}
