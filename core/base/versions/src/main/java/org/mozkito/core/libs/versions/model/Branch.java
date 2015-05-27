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

import org.mozkito.libraries.sequel.ISequelEntity;
import org.mozkito.skeleton.contracts.Requires;

/**
 * The Class Branch models a branch in a version archive.
 *
 * @author Sascha Just
 */
public class Branch implements ISequelEntity {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6406059989807143676L;
	
	/** The depot id. */
	private final int         depotId;
	
	/** The id. */
	private int               id;
	
	/** The name. */
	private final String      name;
	
	/** The head change set id. */
	private long              headId;
	
	/**
	 * The base change set id. This is the first commit to this branch, i.e. where it was spawned.
	 * */
	private long              rootId;
	
	/**
	 * Instantiates a new branch.
	 *
	 * @param depot
	 *            the depot
	 * @param name
	 *            the name
	 */
	public Branch(final Depot depot, final String name) {
		this.depotId = depot.id();
		this.name = name;
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
	 * Gets the depot id.
	 *
	 * @return the depotId
	 */
	public final int getDepotId() {
		return this.depotId;
	}
	
	/**
	 * Gets the head id.
	 *
	 * @return the headId
	 */
	public final long getHeadId() {
		return this.headId;
	}
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public final String getName() {
		return this.name;
	}
	
	/**
	 * Gets the root id.
	 *
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
		result = prime * result + this.depotId;
		result = prime * result + (this.name == null
		                                            ? 0
		                                            : this.name.hashCode());
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.ISequelEntity#id()
	 */
	public Integer id() {
		return this.id;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.ISequelEntity#id(java.lang.Object)
	 */
	public void id(final Object id) {
		Requires.notNull(id);
		Requires.isInteger(id);
		
		this.id = (int) id;
	}
	
	/**
	 * Sets the head id.
	 *
	 * @param headId
	 *            the headId to set
	 */
	public final void setHeadId(final long headId) {
		this.headId = headId;
	}
	
	/**
	 * Sets the root id.
	 *
	 * @param rootId
	 *            the rootId to set
	 */
	public final void setRootId(final long rootId) {
		this.rootId = rootId;
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
		builder.append(", ");
		if (this.name != null) {
			builder.append("name=");
			builder.append(this.name);
			builder.append(", ");
		}
		builder.append("headId=");
		builder.append(this.headId);
		builder.append(", rootId=");
		builder.append(this.rootId);
		builder.append("]");
		return builder.toString();
	}
	
}
