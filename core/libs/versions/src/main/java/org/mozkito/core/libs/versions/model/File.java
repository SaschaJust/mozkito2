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
 * The Class File.
 *
 * @author Sascha Just
 */
public class File implements ISequelEntity {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 8059281977354410692L;
	
	/** The id. */
	private long              id;
	
	/** The path. */
	private String            path;
	
	/** The former file id. */
	private Long              formerFileId;
	
	/** The revision id. */
	private long              revisionId;
	
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
		final File other = (File) obj;
		if (this.revisionId != other.revisionId) {
			return false;
		}
		
		if (this.path == null) {
			if (other.path != null) {
				return false;
			}
		} else if (!this.path.equals(other.path)) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Gets the former file id.
	 *
	 * @return the formerFile
	 */
	public final Long getFormerFileId() {
		return this.formerFileId;
	}
	
	/**
	 * Gets the path.
	 *
	 * @return the path
	 */
	public final String getPath() {
		return this.path;
	}
	
	/**
	 * Gets the revision id.
	 *
	 * @return the revisionId
	 */
	public final long getRevisionId() {
		return this.revisionId;
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
		result = prime * result + (this.path == null
		                                            ? 0
		                                            : this.path.hashCode());
		result = prime * result + (int) (this.revisionId ^ this.revisionId >>> 32);
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
	 * Sets the former file id.
	 *
	 * @param formerFile
	 *            the formerFile to set
	 */
	public final void setFormerFileId(final Long formerFile) {
		this.formerFileId = formerFile;
	}
	
	/**
	 * Sets the path.
	 *
	 * @param path
	 *            the path to set
	 */
	public final void setPath(final String path) {
		this.path = path;
	}
	
	/**
	 * Sets the revision id.
	 *
	 * @param revisionId
	 *            the revisionId to set
	 */
	public final void setRevisionId(final long revisionId) {
		this.revisionId = revisionId;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("File [id=");
		builder.append(this.id);
		builder.append(", revisionId=");
		builder.append(this.revisionId);
		builder.append(", ");
		if (this.path != null) {
			builder.append("path=");
			builder.append(this.path);
			builder.append(", ");
		}
		if (this.formerFileId != null) {
			builder.append("formerFileId=");
			builder.append(this.formerFileId);
		}
		builder.append("]");
		return builder.toString();
	}
	
}
