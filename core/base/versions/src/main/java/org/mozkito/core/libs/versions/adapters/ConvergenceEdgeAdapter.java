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

import org.mozkito.core.libs.versions.model.ConvergenceEdge;
import org.mozkito.skeleton.contracts.Requires;
import org.mozkito.skeleton.sequel.AbstractSequelAdapter;
import org.mozkito.skeleton.sequel.SequelDatabase;

/**
 * @author Sascha Just
 *
 */
public class ConvergenceEdgeAdapter extends AbstractSequelAdapter<ConvergenceEdge> {
	
	/**
	 * @param database
	 */
	public ConvergenceEdgeAdapter(final SequelDatabase database) {
		super(database, "convergence");
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#create(java.sql.ResultSet)
	 */
	public ConvergenceEdge create(final ResultSet result) {
		// TODO Auto-generated method stub
		// return null;
		throw new RuntimeException("Method 'create' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#delete(java.lang.Object)
	 */
	public void delete(final ConvergenceEdge object) {
		// TODO Auto-generated method stub
		//
		throw new RuntimeException("Method 'delete' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#load()
	 */
	public Iterator<ConvergenceEdge> load() {
		// TODO Auto-generated method stub
		// return null;
		throw new RuntimeException("Method 'load' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#load(long[])
	 */
	public List<ConvergenceEdge> load(final long... ids) {
		// TODO Auto-generated method stub
		// return null;
		throw new RuntimeException("Method 'load' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#load(long)
	 */
	public ConvergenceEdge load(final long id) {
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
	                 final ConvergenceEdge edge) {
		Requires.notNull(saveStatement);
		Requires.notNull(edge);
		
		try {
			
			int index = 0;
			saveStatement.setLong(++index, id);
			
			saveStatement.setLong(++index, edge.getBranchId());
			saveStatement.setLong(++index, edge.getSourceId());
			saveStatement.setLong(++index, edge.getConvergeId());
			
			saveStatement.executeUpdate();
			
			edge.id(id);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelAdapter#update(java.lang.Object[])
	 */
	public void update(final ConvergenceEdge... objects) {
		// TODO Auto-generated method stub
		//
		throw new RuntimeException("Method 'update' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
}
