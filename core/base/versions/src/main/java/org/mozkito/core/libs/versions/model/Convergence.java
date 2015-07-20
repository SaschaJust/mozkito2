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

package org.mozkito.core.libs.versions.model;

import org.mozkito.libraries.sequel.IEntity;

// TODO: Auto-generated Javadoc
/**
 * The Class ConvergenceEdge.
 *
 * @author Sascha Just
 */
public class Convergence implements IEntity {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3190680196331117675L;
	
	/** The id. */
	private long              id;
	
	/** The branch id. */
	private final long        branchId;
	
	/** The source id. */
	private final long        sourceId;
	
	/** The converge id. */
	private final long        convergeId;
	
	/**
	 * Instantiates a new convergence edge.
	 *
	 * @param branchId
	 *            the branch id
	 * @param sourceId
	 *            the source id
	 * @param convergeId
	 *            the converge id
	 */
	public Convergence(final long branchId, final long sourceId, final long convergeId) {
		super();
		this.branchId = branchId;
		this.sourceId = sourceId;
		this.convergeId = convergeId;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Convergence other = (Convergence) obj;
		if (this.branchId != other.branchId) {
			return false;
		}
		if (this.convergeId != other.convergeId) {
			return false;
		}
		if (this.sourceId != other.sourceId) {
			return false;
		}
		return true;
	}
	
	/**
	 * Gets the branch id.
	 *
	 * @return the branchId
	 */
	public final long getBranchId() {
		return this.branchId;
	}
	
	/**
	 * Gets the converge id.
	 *
	 * @return the convergeId
	 */
	public final long getConvergeId() {
		return this.convergeId;
	}
	
	/**
	 * Gets the source id.
	 *
	 * @return the sourceId
	 */
	public final long getSourceId() {
		return this.sourceId;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (this.branchId ^ this.branchId >>> 32);
		result = prime * result + (int) (this.convergeId ^ this.convergeId >>> 32);
		result = prime * result + (int) (this.sourceId ^ this.sourceId >>> 32);
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.IEntity#getId()
	 */
	public long getId() {
		return this.id;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.IEntity#setId(long)
	 */
	public void setId(final long id) {
		this.id = id;
	}
	
}
