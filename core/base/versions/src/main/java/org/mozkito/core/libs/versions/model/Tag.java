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

import java.time.Instant;

import org.mozkito.skeleton.sequel.ISequelEntity;

/**
 * The Class Tag.
 *
 * @author Sascha Just
 */
public class Tag implements ISequelEntity {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The id. */
	private int               id;
	
	/** The commit hash. */
	private long              commitId;
	
	/** The name. */
	private String            name;
	
	/** The tagger id. */
	private int               taggerId;
	
	/** The tag time. */
	private Instant           tagTime;
	
	/** The message. */
	private String            description;
	
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
		final Tag other = (Tag) obj;
		if (this.commitId != other.commitId) {
			return false;
		}
		if (this.description == null) {
			if (other.description != null) {
				return false;
			}
		} else if (!this.description.equals(other.description)) {
			return false;
		}
		if (this.name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!this.name.equals(other.name)) {
			return false;
		}
		if (this.tagTime == null) {
			if (other.tagTime != null) {
				return false;
			}
		} else if (!this.tagTime.equals(other.tagTime)) {
			return false;
		}
		if (this.taggerId != other.taggerId) {
			return false;
		}
		return true;
	}
	
	/**
	 * @return the description
	 */
	public final String getDescription() {
		return this.description;
	}
	
	/**
	 * @return the name
	 */
	public final String getName() {
		return this.name;
	}
	
	/**
	 * Gets the tagger id.
	 *
	 * @return the taggerId
	 */
	public final int getTaggerId() {
		return this.taggerId;
	}
	
	/**
	 * Gets the tag time.
	 *
	 * @return the tagTime
	 */
	public final Instant getTagTime() {
		return this.tagTime;
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
		result = prime * result + (int) (this.commitId ^ this.commitId >>> 32);
		result = prime * result + (this.description == null
		                                                   ? 0
		                                                   : this.description.hashCode());
		result = prime * result + (this.name == null
		                                            ? 0
		                                            : this.name.hashCode());
		result = prime * result + (this.tagTime == null
		                                               ? 0
		                                               : this.tagTime.hashCode());
		result = prime * result + this.taggerId;
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelEntity#id()
	 */
	@Override
	public long id() {
		return this.id;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelEntity#id(long)
	 */
	@Override
	public void id(final long id) {
		this.id = (int) id;
	}
	
	/**
	 * @param description
	 *            the description to set
	 */
	public final void setDescription(final String description) {
		this.description = description;
	}
	
	/**
	 * @param name
	 *            the name to set
	 */
	public final void setName(final String name) {
		this.name = name;
	}
	
	/**
	 * Sets the tagger id.
	 *
	 * @param taggerId
	 *            the taggerId to set
	 */
	public final void setTaggerId(final int taggerId) {
		this.taggerId = taggerId;
	}
	
	/**
	 * Sets the tag time.
	 *
	 * @param tagTime
	 *            the tagTime to set
	 */
	public final void setTagTime(final Instant tagTime) {
		this.tagTime = tagTime;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("Tag [id=");
		builder.append(this.id);
		builder.append(", ");
		if (this.name != null) {
			builder.append("name=");
			builder.append(this.name);
			builder.append(", ");
		}
		builder.append("commitId=");
		builder.append(this.commitId);
		builder.append(", taggerId=");
		builder.append(this.taggerId);
		builder.append(", ");
		if (this.tagTime != null) {
			builder.append("tagTime=");
			builder.append(this.tagTime);
			builder.append(", ");
		}
		if (this.description != null) {
			builder.append("description=");
			builder.append(this.description);
		}
		builder.append("]");
		return builder.toString();
	}
	
}
