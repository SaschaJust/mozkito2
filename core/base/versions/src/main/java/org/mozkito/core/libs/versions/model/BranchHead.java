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

/**
 * BranchHead is used to model the current head of each branch in the database.
 *
 * @author Sascha Just
 */
public class BranchHead {
	
	/** The id. */
	private int        id;
	
	/** The depot id. */
	private final int  depotId;
	
	/** The change set id. */
	private final long changeSetId;
	
	/** The branch id. */
	private final int  branchId;
	
	/**
	 * Instantiates a new branch head.
	 *
	 * @param depot
	 *            the depot
	 * @param branch
	 *            the branch
	 * @param changeSet
	 *            the change set
	 */
	public BranchHead(final Depot depot, final Branch branch, final ChangeSet changeSet) {
		super();
		this.depotId = depot.id();
		this.branchId = branch.id();
		this.changeSetId = changeSet.id();
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
		final BranchHead other = (BranchHead) obj;
		if (this.branchId != other.branchId) {
			return false;
		}
		if (this.changeSetId != other.changeSetId) {
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
	public final int getBranchId() {
		return this.branchId;
	}
	
	/**
	 * Gets the change set id.
	 *
	 * @return the changeSetId
	 */
	public final long getChangeSetId() {
		return this.changeSetId;
	}
	
	/**
	 * Gets the depot id.
	 *
	 * @return the depotId
	 */
	public final int getDepotId() {
		return this.depotId;
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
		result = prime * result + this.branchId;
		result = prime * result + (int) (this.changeSetId ^ this.changeSetId >>> 32);
		result = prime * result + this.depotId;
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("BranchHead [id=");
		builder.append(this.id);
		builder.append(", depotId=");
		builder.append(this.depotId);
		builder.append(", branchId=");
		builder.append(this.branchId);
		builder.append(", changeSetId=");
		builder.append(this.changeSetId);
		builder.append("]");
		return builder.toString();
	}
	
}
