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

import org.mozkito.core.libs.versions.model.enums.ReferenceType;
import org.mozkito.libraries.sequel.IEntity;

/**
 * The Class Reference models a reference in a git archive.
 *
 * @author Sascha Just
 */
public abstract class Reference implements IEntity {
	
	/** The Constant serialVersionUID. */
	private static final long   serialVersionUID = 6406059989807143676L;
	
	/** The depot id. */
	private final long          depotId;
	
	/** The id. */
	private long                id;
	
	/** The name. */
	private final String        name;
	
	/** The head change set id. */
	private final long          headId;
	
	/** The type. */
	private final ReferenceType type;
	
	/**
	 * Instantiates a new branch.
	 *
	 * @param depot
	 *            the depot
	 * @param type
	 *            the type
	 * @param name
	 *            the name
	 * @param changeSetId
	 *            the change set id
	 */
	public Reference(final Depot depot, final ReferenceType type, final String name, final long changeSetId) {
		this.depotId = depot.getId();
		this.name = name;
		this.type = type;
		this.headId = changeSetId;
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
		final Reference other = (Reference) obj;
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
		if (this.type != other.type) {
			return false;
		}
		return true;
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
	 * Gets the type.
	 *
	 * @return the type
	 */
	public final ReferenceType getType() {
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
		result = prime * result + (int) (this.depotId ^ this.depotId >>> 32);
		result = prime * result + (this.name == null
		                                            ? 0
		                                            : this.name.hashCode());
		result = prime * result + (this.type == null
		                                            ? 0
		                                            : this.type.hashCode());
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
		this.id = (int) id;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("Reference [depotId=");
		builder.append(this.depotId);
		builder.append(", id=");
		builder.append(this.id);
		builder.append(", ");
		builder.append("name=");
		builder.append(this.name);
		builder.append(", ");
		builder.append("headId=");
		builder.append(this.headId);
		builder.append(", ");
		builder.append("type=");
		builder.append(this.type.name());
		builder.append("]");
		return builder.toString();
	}
	
}
