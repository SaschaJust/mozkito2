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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.mozkito.core.libs.versions.Graph;
import org.mozkito.core.libs.versions.Graph.Edge;
import org.mozkito.core.libs.versions.model.BranchEdge;
import org.mozkito.core.libs.versions.model.GraphEdge;
import org.mozkito.core.libs.versions.model.Head;
import org.mozkito.core.libs.versions.model.IntegrationEdge;
import org.mozkito.core.libs.versions.model.Roots;
import org.mozkito.skeleton.contracts.Requires;
import org.mozkito.skeleton.sequel.AbstractSequelAdapter;
import org.mozkito.skeleton.sequel.ISequelAdapter;
import org.mozkito.skeleton.sequel.SequelDatabase;

/**
 * The Class GraphAdapter.
 *
 * @author Sascha Just
 */
public class GraphAdapter extends AbstractSequelAdapter<Graph> {
	
	/** The edge adapter. */
	private final ISequelAdapter<GraphEdge>       edgeAdapter;
	
	/** The branch adapter. */
	private final ISequelAdapter<BranchEdge>      branchAdapter;
	
	/** The integration adapter. */
	private final ISequelAdapter<IntegrationEdge> integrationAdapter;
	
	/** The end point adapter. */
	private final ISequelAdapter<Head>            headAdapter;
	
	private final ISequelAdapter<Roots>           rootsAdapter;
	
	/**
	 * Instantiates a new graph adapter.
	 *
	 * @param database
	 *            the database
	 */
	public GraphAdapter(final SequelDatabase database) {
		super(database, "graph");
		
		database.register(GraphEdge.class, new GraphEdgeAdapter(database));
		this.edgeAdapter = database.getAdapter(GraphEdge.class);
		
		database.register(BranchEdge.class, new BranchEdgeAdapter(database));
		this.branchAdapter = database.getAdapter(BranchEdge.class);
		
		database.register(IntegrationEdge.class, new IntegrationEdgeAdapter(database));
		this.integrationAdapter = database.getAdapter(IntegrationEdge.class);
		
		database.register(Head.class, new HeadAdapter(database));
		this.headAdapter = database.getAdapter(Head.class);
		
		database.register(Roots.class, new RootsAdapter(database));
		this.rootsAdapter = database.getAdapter(Roots.class);
		
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#create(java.sql.ResultSet)
	 */
	public Graph create(final ResultSet result) {
		// TODO Auto-generated method stub
		// return null;
		throw new RuntimeException("Method 'create' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#delete(java.lang.Object)
	 */
	public void delete(final Graph object) {
		// TODO Auto-generated method stub
		//
		throw new RuntimeException("Method 'delete' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#load()
	 */
	public Iterator<Graph> load() {
		// TODO Auto-generated method stub
		// return null;
		throw new RuntimeException("Method 'load' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#load(long[])
	 */
	public List<Graph> load(final long... ids) {
		// TODO Auto-generated method stub
		// return null;
		throw new RuntimeException("Method 'load' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#load(long)
	 */
	public Graph load(final long id) {
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
	                 final Graph entity) {
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
			final PreparedStatement headStmt = this.headAdapter.prepareSaveStatement();
			final PreparedStatement headNextIdStmt = this.headAdapter.prepareNextIdStatement();
			final PreparedStatement rootsStmt = this.rootsAdapter.prepareSaveStatement();
			final PreparedStatement rootsNextIdStmt = this.rootsAdapter.prepareNextIdStatement();
			
			saveStatement.setLong(++index, id);
			saveStatement.setLong(++index, entity.getDepot().id());
			
			saveStatement.executeUpdate();
			
			entity.id(id);
			
			GraphEdge gEdge;
			BranchEdge bEdge;
			IntegrationEdge iEdge;
			
			final Collection<Edge> edges = entity.getEdges();
			int batchCounter = 0;
			final int batchSize = 10000;
			
			for (final Edge edge : edges) {
				++batchCounter;
				gEdge = new GraphEdge(entity.getDepot().id(), edge.getSourceId(), edge.getTargetId(), edge.getType());
				this.edgeAdapter.save(edgeStmt, edgeNextIdStmt, gEdge);
				
				for (final long branchId : edge.getBranchIds()) {
					++batchCounter;
					bEdge = new BranchEdge(gEdge.id(), branchId);
					this.branchAdapter.save(branchStmt, branchNextIdStmt, bEdge);
					if (batchCounter >= batchSize) {
						this.database.commit();
						batchCounter = 0;
					}
				}
				
				for (final long branchId : edge.getIntegrationPathIds()) {
					++batchCounter;
					iEdge = new IntegrationEdge(gEdge.id(), branchId);
					this.integrationAdapter.save(integrationStmt, integrationNextIdStmt, iEdge);
					if (batchCounter >= batchSize) {
						this.database.commit();
						batchCounter = 0;
					}
				}
				
				if (batchCounter >= batchSize) {
					this.database.commit();
					batchCounter = 0;
				}
			}
			
			for (final Head head : entity.getHeads()) {
				this.headAdapter.save(headStmt, headNextIdStmt, head);
			}
			
			for (final Roots roots : entity.getRoots()) {
				this.rootsAdapter.save(rootsStmt, rootsNextIdStmt, roots);
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
	public void update(final Graph... objects) {
		// TODO Auto-generated method stub
		//
		throw new RuntimeException("Method 'update' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
}
