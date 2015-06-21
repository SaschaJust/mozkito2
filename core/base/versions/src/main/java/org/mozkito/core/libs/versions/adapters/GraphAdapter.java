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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.mozkito.core.libs.versions.graph.Edge;
import org.mozkito.core.libs.versions.graph.Graph;
import org.mozkito.core.libs.versions.graph.Label;
import org.mozkito.core.libs.versions.model.BranchEdge;
import org.mozkito.core.libs.versions.model.ConvergenceEdge;
import org.mozkito.core.libs.versions.model.GraphEdge;
import org.mozkito.core.libs.versions.model.Head;
import org.mozkito.core.libs.versions.model.Root;
import org.mozkito.skeleton.contracts.Requires;
import org.mozkito.skeleton.sequel.AbstractAdapter;
import org.mozkito.skeleton.sequel.Database;
import org.mozkito.skeleton.sequel.IAdapter;

/**
 * The Class GraphAdapter.
 *
 * @author Sascha Just
 */
public class GraphAdapter extends AbstractAdapter<Graph> {
	
	private static long                     currentId = 0l;
	
	/** The edge adapter. */
	private final IAdapter<GraphEdge>       edgeAdapter;
	
	/** The branch adapter. */
	private final IAdapter<BranchEdge>      branchAdapter;
	
	/** The end point adapter. */
	private final IAdapter<Head>            headAdapter;
	
	/** The roots adapter. */
	private final IAdapter<Root>            rootsAdapter;
	/** The convergence adapter. */
	private final IAdapter<ConvergenceEdge> convergenceAdapter;
	
	/**
	 * Instantiates a new graph adapter.
	 *
	 * @param type
	 *            the type
	 * @param edgeAdapter
	 *            the edge adapter
	 * @param branchAdapter
	 *            the branch adapter
	 * @param headAdapter
	 *            the head adapter
	 * @param rootsAdapter
	 *            the roots adapter
	 * @param convergenceAdapter
	 *            the convergence adapter
	 */
	public GraphAdapter(final Database.Type type, final IAdapter<GraphEdge> edgeAdapter,
	        final IAdapter<BranchEdge> branchAdapter, final IAdapter<Head> headAdapter,
	        final IAdapter<Root> rootsAdapter, final IAdapter<ConvergenceEdge> convergenceAdapter) {
		super(type, "graph");
		
		this.edgeAdapter = edgeAdapter;
		this.branchAdapter = branchAdapter;
		this.convergenceAdapter = convergenceAdapter;
		this.headAdapter = headAdapter;
		this.rootsAdapter = rootsAdapter;
		
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.IAdapter#create(java.sql.ResultSet)
	 */
	public Graph create(final ResultSet result) {
		// TODO Auto-generated method stub
		// return null;
		throw new RuntimeException("Method 'create' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.IAdapter#delete(java.sql.Connection, java.lang.Object)
	 */
	public void delete(final Connection connection,
	                   final Graph object) {
		// TODO Auto-generated method stub
		//
		throw new RuntimeException("Method 'delete' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.IAdapter#load(java.sql.Connection)
	 */
	public Iterator<Graph> load(final Connection connection) {
		// TODO Auto-generated method stub
		// return null;
		throw new RuntimeException("Method 'load' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.IAdapter#load(java.sql.Connection, long[])
	 */
	public List<Graph> load(final Connection connection,
	                        final long... ids) {
		// TODO Auto-generated method stub
		// return null;
		throw new RuntimeException("Method 'load' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.IAdapter#load(java.sql.Connection, long)
	 */
	public Graph load(final Connection connection,
	                  final long id) {
		// TODO Auto-generated method stub
		// return null;
		throw new RuntimeException("Method 'load' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.IAdapter#nextId()
	 */
	public synchronized long nextId() {
		return ++currentId;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.IAdapter#save(java.sql.PreparedStatement, long, java.lang.Object)
	 */
	public void save(final PreparedStatement saveStatement,
	                 final long id,
	                 final Graph entity) {
		Requires.notNull(saveStatement);
		Requires.notNull(entity);
		
		try {
			
			int index = 0;
			final Connection connection = saveStatement.getConnection();
			final PreparedStatement edgeStmt = this.edgeAdapter.prepareSaveStatement(connection);
			final PreparedStatement branchStmt = this.branchAdapter.prepareSaveStatement(connection);
			final PreparedStatement headStmt = this.headAdapter.prepareSaveStatement(connection);
			final PreparedStatement rootsStmt = this.rootsAdapter.prepareSaveStatement(connection);
			final PreparedStatement convergenceStmt = this.convergenceAdapter.prepareSaveStatement(connection);
			
			saveStatement.setLong(++index, id);
			saveStatement.setLong(++index, entity.getDepot().id());
			// TODO add #vertices #edges
			
			saveStatement.executeUpdate();
			
			entity.id(id);
			
			GraphEdge gEdge;
			BranchEdge bEdge;
			
			final Collection<Edge> edges = entity.getEdges();
			int batchCounter = 0;
			final int batchSize = 1000000;
			Label label;
			for (final Edge edge : edges) {
				++batchCounter;
				gEdge = new GraphEdge(entity.getDepot().id(), edge.getSourceId(), edge.getTargetId());
				this.edgeAdapter.save(edgeStmt, this.edgeAdapter.nextId(), gEdge);
				
				for (final Entry<Long, Label> entry : edge.getLabels().entrySet()) {
					label = entry.getValue();
					++batchCounter;
					bEdge = new BranchEdge(gEdge.id(), entry.getKey(), label.branchMarker, label.navigationMarker,
					                       label.integrationMarker);
					this.branchAdapter.save(branchStmt, this.branchAdapter.nextId(), bEdge);
					if (batchCounter >= batchSize) {
						connection.commit();
						batchCounter = 0;
					}
				}
				
				if (batchCounter >= batchSize) {
					connection.commit();
					batchCounter = 0;
				}
			}
			
			for (final ConvergenceEdge cEdge : entity.getConvergence()) {
				this.convergenceAdapter.save(convergenceStmt, this.convergenceAdapter.nextId(), cEdge);
			}
			
			for (final Head head : entity.getHeads()) {
				this.headAdapter.save(headStmt, this.headAdapter.nextId(), head);
			}
			
			for (final Root roots : entity.getRoots()) {
				this.rootsAdapter.save(rootsStmt, this.rootsAdapter.nextId(), roots);
			}
			
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.IAdapter#update(java.sql.Connection, java.lang.Object[])
	 */
	public void update(final Connection connection,
	                   final Graph... objects) {
		// TODO Auto-generated method stub
		//
		throw new RuntimeException("Method 'update' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
}
