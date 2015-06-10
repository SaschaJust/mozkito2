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

import org.mozkito.core.libs.versions.Graph.EdgeType;
import org.mozkito.skeleton.sequel.ISequelEntity;

/**
 * The Class BranchEdge.
 */
public class BranchEdge implements ISequelEntity {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6075643512649538152L;
	
	/** The id. */
	private long              id;
	
	/** The edge id. */
	private final long        edgeId;
	
	/** The branch id. */
	private final long        branchId;
	
	private final short       type;
	
	/**
	 * Instantiates a new branch edge.
	 *
	 * @param edgeId
	 *            the edge id
	 * @param branchId
	 *            the branch id
	 */
	public BranchEdge(final long edgeId, final long branchId, final EdgeType type) {
		super();
		this.edgeId = edgeId;
		this.branchId = branchId;
		this.type = (short) type.ordinal();
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
		final BranchEdge other = (BranchEdge) obj;
		if (this.branchId != other.branchId) {
			return false;
		}
		if (this.edgeId != other.edgeId) {
			return false;
		}
		if (this.type != other.type) {
			return false;
		}
		return true;
	}
	
	/**
	 * @return the branchId
	 */
	public final long getBranchId() {
		return this.branchId;
	}
	
	/**
	 * @return the edgeId
	 */
	public final long getEdgeId() {
		return this.edgeId;
	}
	
	/**
	 * @return
	 */
	public short getType() {
		return this.type;
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
		result = prime * result + (int) (this.edgeId ^ this.edgeId >>> 32);
		result = prime * result + this.type;
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelEntity#id()
	 */
	public long id() {
		return this.id;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelEntity#id(long)
	 */
	public void id(final long id) {
		this.id = id;
	}
	
}
