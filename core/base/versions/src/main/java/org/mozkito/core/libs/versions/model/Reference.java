/***********************************************************************************************************************
 * MIT License
 *  
 * Copyright (c) 2015 mozkito.org
 *  
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *  
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *  
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * 
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
