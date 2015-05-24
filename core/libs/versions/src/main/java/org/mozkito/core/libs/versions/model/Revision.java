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

import org.mozkito.core.libs.versions.ChangeType;
import org.mozkito.skeleton.contracts.Requires;
import org.mozkito.skeleton.sequel.ISequelEntity;

// TODO: Auto-generated Javadoc
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
	private final long        changeSetId;
	
	/** The change type. */
	private final short       changeType;
	
	/** The source id. */
	private final Long        sourceId;
	
	/** The target id. */
	private final Long        targetId;
	
	/** The confidence. */
	private final short       confidence;
	
	/**
	 * Instantiates a new revision.
	 *
	 * @param changeSet
	 *            the change set
	 * @param changeType
	 *            the change type
	 * @param source
	 *            the source
	 * @param target
	 *            the target
	 * @param confidence
	 *            the confidence
	 */
	public Revision(final ChangeSet changeSet, final ChangeType changeType, final Handle source, final Handle target,
	        final short confidence) {
		Requires.notNull(changeType);
		Requires.positive(changeSet.id());
		Requires.notNull(changeType);
		Requires.notNull(source);
		Requires.positive(source.id());
		Requires.notNull(target);
		Requires.positive(target.id());
		Requires.greaterOrEqual(confidence, 50);
		
		this.changeSetId = changeSet.id();
		this.changeType = changeType.toMask();
		this.sourceId = source.id();
		this.targetId = target.id();
		this.confidence = confidence;
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
		final Revision other = (Revision) obj;
		if (this.changeSetId != other.changeSetId) {
			return false;
		}
		if (this.sourceId == null) {
			if (other.sourceId != null) {
				return false;
			}
		} else if (!this.sourceId.equals(other.sourceId)) {
			return false;
		}
		if (this.targetId == null) {
			if (other.targetId != null) {
				return false;
			}
		} else if (!this.targetId.equals(other.targetId)) {
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
	 * @return the changeType
	 */
	public final short getChangeType() {
		return this.changeType;
	}
	
	/**
	 * @return the confidence
	 */
	public final short getConfidence() {
		return this.confidence;
	}
	
	/**
	 * @return the sourceId
	 */
	public final Long getSourceId() {
		return this.sourceId;
	}
	
	/**
	 * @return the targetId
	 */
	public final Long getTargetId() {
		return this.targetId;
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
		result = prime * result + (this.sourceId == null
		                                                ? 0
		                                                : this.sourceId.hashCode());
		result = prime * result + (this.targetId == null
		                                                ? 0
		                                                : this.targetId.hashCode());
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
		builder.append(", changeType=");
		builder.append(this.changeType);
		builder.append(", ");
		if (this.sourceId != null) {
			builder.append("sourceId=");
			builder.append(this.sourceId);
			builder.append(", ");
		}
		if (this.targetId != null) {
			builder.append("targetId=");
			builder.append(this.targetId);
			builder.append(", ");
		}
		builder.append("confidence=");
		builder.append(this.confidence);
		builder.append("]");
		return builder.toString();
	}
	
}
