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
 * @author Sascha Just
 *
 */
public class Branch implements ISequelEntity {
	
	/**
	 *
	 */
	private static final long serialVersionUID = 6406059989807143676L;
	private int               depotId;
	private int               id;
	private String            name;
	private long              headChangeSetId;
	
	/**
	 * The base change set id. This is the first commit to this branch, i.e. where it was spawned.
	 * */
	private long              baseChangeSetId;
	
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
		final Branch other = (Branch) obj;
		if (this.depotId != other.depotId) {
			return false;
		}
		if (this.name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!this.name.equals(other.name)) {
			return false;
		}
		return true;
	}
	
	/**
	 * @return the baseChangeSetId
	 */
	public final long getBaseChangeSetId() {
		return this.baseChangeSetId;
	}
	
	/**
	 * @return the depotId
	 */
	public final int getDepotId() {
		return this.depotId;
	}
	
	/**
	 * @return the headChangeSetId
	 */
	public final long getHeadChangeSetId() {
		return this.headChangeSetId;
	}
	
	/**
	 * @return the name
	 */
	public final String getName() {
		return this.name;
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
		result = prime * result + this.depotId;
		result = prime * result + (this.name == null
		                                            ? 0
		                                            : this.name.hashCode());
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see org.mozkito.skeleton.sequel.ISequelEntity#id()
	 */
	@Override
	public Object id() {
		// TODO Auto-generated method stub
		// return null;
		throw new RuntimeException("Method 'id' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see org.mozkito.skeleton.sequel.ISequelEntity#id(java.lang.Object)
	 */
	@Override
	public void id(final Object id) {
		// TODO Auto-generated method stub
		//
		throw new RuntimeException("Method 'id' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * @param baseChangeSetId
	 *            the baseChangeSetId to set
	 */
	public final void setBaseChangeSetId(final long baseChangeSetId) {
		this.baseChangeSetId = baseChangeSetId;
	}
	
	/**
	 * @param depotId
	 *            the depotId to set
	 */
	public final void setDepotId(final int depotId) {
		this.depotId = depotId;
	}
	
	/**
	 * @param headChangeSetId
	 *            the headChangeSetId to set
	 */
	public final void setHeadChangeSetId(final long headChangeSetId) {
		this.headChangeSetId = headChangeSetId;
	}
	
	/**
	 * @param name
	 *            the name to set
	 */
	public final void setName(final String name) {
		this.name = name;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("Branch [id=");
		builder.append(this.id);
		builder.append(", depotId=");
		builder.append(this.depotId);
		builder.append(", name=");
		builder.append(this.name);
		builder.append(", headChangeSetId=");
		builder.append(this.headChangeSetId);
		builder.append(", baseChangeSetId=");
		builder.append(this.baseChangeSetId);
		builder.append("]");
		return builder.toString();
	}
	
}
