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

import org.mozkito.skeleton.sequel.ISequelEntity;

/**
 * BranchHead is used to model the current head of each branch in the database.
 *
 * @author Sascha Just
 */
public class Endpoint implements ISequelEntity {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1010755719141984103L;
	
	/** The id. */
	private long              id;
	
	/** The depot id. */
	private final long        depotId;
	
	/** The head id. */
	private final long        headId;
	
	/** The root id. */
	private final long        rootId;
	
	/** The branch id. */
	private final long        branchId;
	
	/**
	 * Instantiates a new branch head.
	 *
	 * @param depot
	 *            the depot
	 * @param branch
	 *            the branch
	 * @param head
	 *            the head
	 * @param root
	 *            the root
	 */
	public Endpoint(final Depot depot, final Branch branch, final ChangeSet head, final ChangeSet root) {
		super();
		this.depotId = depot.id();
		this.branchId = branch.id();
		this.headId = head.id();
		this.rootId = root.id();
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
		final Endpoint other = (Endpoint) obj;
		if (this.branchId != other.branchId) {
			return false;
		}
		if (this.depotId != other.depotId) {
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
	 * Gets the depot id.
	 *
	 * @return the depotId
	 */
	public final long getDepotId() {
		return this.depotId;
	}
	
	/**
	 * @return the headId
	 */
	public final long getHeadId() {
		return this.headId;
	}
	
	/**
	 * @return the rootId
	 */
	public final long getRootId() {
		return this.rootId;
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
		result = prime * result + (int) (this.depotId ^ this.depotId >>> 32);
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
