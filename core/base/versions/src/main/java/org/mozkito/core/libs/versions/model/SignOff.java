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

/**
 * The Class SignOff.
 *
 * @author Sascha Just
 */
public class SignOff implements IEntity {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -102713467156676248L;
	
	/** The change set id. */
	private final long        changeSetId;
	
	/** The id. */
	private long              id;
	
	/** The identity id. */
	private final long        identityId;
	
	/**
	 * Instantiates a new sign off.
	 *
	 * @param changeSetId
	 *            the change set id
	 * @param identityId
	 *            the identity id
	 */
	public SignOff(final long changeSetId, final long identityId) {
		this.changeSetId = changeSetId;
		this.identityId = identityId;
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
		final SignOff other = (SignOff) obj;
		if (this.changeSetId != other.changeSetId) {
			return false;
		}
		if (this.identityId != other.identityId) {
			return false;
		}
		return true;
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
	 * @see org.mozkito.libraries.sequel.IEntity#getId()
	 */
	public long getId() {
		return this.id;
	}
	
	/**
	 * @return the identityId
	 */
	public final long getIdentityId() {
		return this.identityId;
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
		result = prime * result + (int) (this.changeSetId ^ this.changeSetId >>> 32);
		result = prime * result + (int) (this.identityId ^ this.identityId >>> 32);
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.IEntity#setId(long)
	 */
	public void setId(final long id) {
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
		builder.append("SignOff [id=");
		builder.append(this.id);
		builder.append(", changeSetId=");
		builder.append(this.changeSetId);
		builder.append(", identityId=");
		builder.append(this.identityId);
		builder.append("]");
		return builder.toString();
	}
	
}
