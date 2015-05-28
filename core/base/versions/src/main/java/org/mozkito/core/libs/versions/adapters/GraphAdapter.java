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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.mozkito.core.libs.versions.DepotGraph;
import org.mozkito.core.libs.versions.DepotGraph.Edge;
import org.mozkito.core.libs.versions.model.BranchEdge;
import org.mozkito.core.libs.versions.model.GraphEdge;
import org.mozkito.skeleton.contracts.Requires;
import org.mozkito.skeleton.sequel.AbstractSequelAdapter;
import org.mozkito.skeleton.sequel.SequelDatabase;

/**
 * The Class GraphAdapter.
 *
 * @author Sascha Just
 */
public class GraphAdapter extends AbstractSequelAdapter<DepotGraph> {
	
	/** The edge adapter. */
	private final GraphEdgeAdapter   edgeAdapter;
	
	/** The branch adapter. */
	private final GraphBranchAdapter branchAdapter;
	
	/** The integration adapter. */
	private final GraphBranchAdapter integrationAdapter;
	
	/**
	 * Instantiates a new graph adapter.
	 *
	 * @param database
	 *            the database
	 */
	public GraphAdapter(final SequelDatabase database) {
		super(database, "graph");
		
		this.edgeAdapter = new GraphEdgeAdapter(database);
		this.branchAdapter = new GraphBranchAdapter(database, "graph_branch_edge");
		this.integrationAdapter = new GraphBranchAdapter(database, "graph_integration_edge");
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#create(java.sql.ResultSet)
	 */
	public DepotGraph create(final ResultSet result) {
		// TODO Auto-generated method stub
		// return null;
		throw new RuntimeException("Method 'create' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#createConstraints()
	 */
	@Override
	public void createConstraints() {
		super.createConstraints();
		
		this.edgeAdapter.createConstraints();
		this.branchAdapter.createConstraints();
		this.integrationAdapter.createConstraints();
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#createIndexes()
	 */
	@Override
	public void createIndexes() {
		super.createIndexes();
		
		this.edgeAdapter.createIndexes();
		this.branchAdapter.createIndexes();
		this.integrationAdapter.createIndexes();
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#createScheme()
	 */
	@Override
	public void createScheme() {
		super.createScheme();
		
		this.edgeAdapter.createScheme();
		this.branchAdapter.createScheme();
		this.integrationAdapter.createScheme();
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#delete(java.lang.Object)
	 */
	public void delete(final DepotGraph object) {
		// TODO Auto-generated method stub
		//
		throw new RuntimeException("Method 'delete' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#load()
	 */
	public Iterator<DepotGraph> load() {
		// TODO Auto-generated method stub
		// return null;
		throw new RuntimeException("Method 'load' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#load(long[])
	 */
	public List<DepotGraph> load(final long... ids) {
		// TODO Auto-generated method stub
		// return null;
		throw new RuntimeException("Method 'load' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#load(long)
	 */
	public DepotGraph load(final long id) {
		// TODO Auto-generated method stub
		// return null;
		throw new RuntimeException("Method 'load' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#save(java.sql.PreparedStatement, long, java.lang.Object)
	 */
	public void save(final PreparedStatement saveStatement,
	                 final long id,
	                 final DepotGraph entity) {
		Requires.notNull(saveStatement);
		Requires.notNull(entity);
		
		try {
			
			int index = 0;
			
			final PreparedStatement edgeStmt = this.edgeAdapter.prepareSaveStatement();
			final PreparedStatement edgeNextIdStmt = this.edgeAdapter.prepareNextIdStatement();
			final PreparedStatement branchStmt = this.branchAdapter.prepareSaveStatement();
			final PreparedStatement branchNextIdStmt = this.branchAdapter.prepareNextIdStatement();
			final PreparedStatement integrationStmt = this.integrationAdapter.prepareSaveStatement();
			final PreparedStatement integrationNextIdStmt = this.integrationAdapter.prepareNextIdStatement();
			
			saveStatement.setLong(++index, id);
			saveStatement.setLong(++index, entity.getDepot().id());
			
			saveStatement.executeUpdate();
			
			entity.id(id);
			
			GraphEdge gEdge;
			BranchEdge bEdge, iEdge;
			
			for (final Edge edge : entity.getEdges()) {
				gEdge = new GraphEdge(entity.getDepot().id(), edge.getSourceId(), edge.getTargetId(), edge.getType());
				this.edgeAdapter.save(edgeStmt, edgeNextIdStmt, gEdge);
				
				for (final long branchId : edge.getBranchIds()) {
					bEdge = new BranchEdge(entity.getDepot().id(), gEdge.id(), branchId);
					this.branchAdapter.save(branchStmt, branchNextIdStmt, bEdge);
				}
				
				for (final long branchId : edge.getIntegrationPathIds()) {
					iEdge = new BranchEdge(entity.getDepot().id(), gEdge.id(), branchId);
					this.integrationAdapter.save(integrationStmt, integrationNextIdStmt, iEdge);
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
	public void update(final DepotGraph... objects) {
		// TODO Auto-generated method stub
		//
		throw new RuntimeException("Method 'update' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
}
