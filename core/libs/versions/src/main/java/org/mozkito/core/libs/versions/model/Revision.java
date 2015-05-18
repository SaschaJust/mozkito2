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
import org.mozkito.skeleton.sequel.ISequelEntity;

/**
 * The Class Revision.
 *
 * @author Sascha Just
 */
public class Revision implements ISequelEntity {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2497422551900780440L;
	
	/** The id. */
	private long              id;
	
	/** The change set id. */
	private long              changeSetId;
	
	/** The file id. */
	private long              fileId;
	
	/** The change type. */
	private short             changeType;
	
	/** The patch hash. */
	private short             patchHash;
	
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
		final Revision other = (Revision) obj;
		if (this.changeSetId != other.changeSetId) {
			return false;
		}
		if (this.fileId != other.fileId) {
			return false;
		}
		return true;
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
		result = prime * result + (int) (this.fileId ^ this.fileId >>> 32);
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see org.mozkito.skeleton.sequel.ISequelEntity#id()
	 */
	@Override
	public Object id() {
		return this.id;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see org.mozkito.skeleton.sequel.ISequelEntity#id(java.lang.Object)
	 */
	@Override
	public void id(final Object id) {
		Requires.notNull(id);
		Requires.isLong(id);
		
		this.id = (long) id;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("Revision [id=");
		builder.append(this.id);
		builder.append(", changeSetId=");
		builder.append(this.changeSetId);
		builder.append(", fileId=");
		builder.append(this.fileId);
		builder.append(", changeType=");
		builder.append(this.changeType);
		builder.append(", patchHash=");
		builder.append(this.patchHash);
		builder.append("]");
		return builder.toString();
	}
	
}
