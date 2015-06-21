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

import org.mozkito.skeleton.contracts.Requires;
import org.mozkito.skeleton.sequel.IEntity;

/**
 * The Class Roots.
 *
 * @author Sascha Just
 */
public class Root implements IEntity {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3326771349421256531L;
	
	/** The id. */
	private long              id;
	
	/** The branch id. */
	private final long        branchId;
	
	/** The change set ids. */
	private final long        changeSetId;
	
	/**
	 * Instantiates a new roots.
	 *
	 * @param branchId
	 *            the branch id
	 * @param changeSetId
	 *            the change set id
	 */
	public Root(final long branchId, final long changeSetId) {
		Requires.positive(branchId);
		Requires.positive(changeSetId);
		
		this.branchId = branchId;
		this.changeSetId = changeSetId;
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
		final Root other = (Root) obj;
		if (this.branchId != other.branchId) {
			return false;
		}
		if (this.changeSetId != other.changeSetId) {
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
	 * @return the changeSetId
	 */
	public final long getChangeSetId() {
		return this.changeSetId;
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
		result = prime * result + (int) (this.changeSetId ^ this.changeSetId >>> 32);
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.IEntity#id()
	 */
	public long id() {
		return this.id;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.IEntity#id(long)
	 */
	public void id(final long id) {
		this.id = id;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("Root [id=");
		builder.append(this.id);
		builder.append(", branchId=");
		builder.append(this.branchId);
		builder.append(", changeSetId=");
		builder.append(this.changeSetId);
		builder.append("]");
		return builder.toString();
	}
	
}
