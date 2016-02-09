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

import org.mozkito.core.libs.versions.model.enums.ChangeType;
import org.mozkito.libraries.sequel.IEntity;
import org.mozkito.skeleton.contracts.Requires;

/**
 * The Class Revision.
 *
 * @author Sascha Just
 */
public class Revision implements IEntity {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2497422551900780440L;
	
	/** The id. */
	private long              id;
	
	/** The change set id. */
	private final long        changeSetId;
	
	/** The change type. */
	private final short       changeType;
	
	/** The source id. */
	private final long        sourceId;
	
	/** The target id. */
	private final long        targetId;
	
	/** The confidence. */
	private final short       confidence;
	
	/** The old mode. */
	private final int         oldMode;
	
	/** The new mode. */
	private final int         newMode;
	
	/** The old hash. */
	private final String      oldHash;
	
	/** The new hash. */
	private final String      newHash;
	
	/** The lines in. */
	private int               linesIn;
	
	/** The lines out. */
	private int               linesOut;
	
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
	 * @param oldMode
	 *            the old mode
	 * @param newMode
	 *            the new mode
	 * @param oldHash
	 *            the old hash
	 * @param newHash
	 *            the new hash
	 */
	public Revision(final ChangeSet changeSet, final ChangeType changeType, final Handle source, final Handle target,
	        final short confidence, final int oldMode, final int newMode, final String oldHash, final String newHash) {
		Requires.notNull(changeType);
		Requires.positive(changeSet.getId());
		Requires.notNull(changeType);
		Requires.notNull(source);
		Requires.positive(source.getId());
		Requires.notNull(target);
		Requires.positive(target.getId());
		Requires.greaterOrEqual(confidence, 50);
		Requires.notNegative(oldMode);
		Requires.notNegative(newMode);
		Requires.notNull(oldHash);
		Requires.notNull(newHash);
		
		this.changeSetId = changeSet.getId();
		this.changeType = changeType.toMask();
		this.sourceId = source.getId();
		this.targetId = target.getId();
		this.confidence = confidence;
		this.oldMode = oldMode;
		this.newMode = newMode;
		this.oldHash = oldHash;
		this.newHash = newHash;
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
		if (this.sourceId != other.sourceId) {
			return false;
		}
		if (this.targetId != other.targetId) {
			return false;
		}
		return true;
	}
	
	/**
	 * Gets the change set id.
	 *
	 * @return the changeSetId
	 */
	public final long getChangeSetId() {
		return this.changeSetId;
	}
	
	/**
	 * Gets the change type.
	 *
	 * @return the changeType
	 */
	public final short getChangeType() {
		return this.changeType;
	}
	
	/**
	 * Gets the confidence.
	 *
	 * @return the confidence
	 */
	public final short getConfidence() {
		return this.confidence;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see org.mozkito.libraries.sequel.IEntity#getId()
	 */
	@Override
	public long getId() {
		return this.id;
	}
	
	/**
	 * @return the linesIn
	 */
	public final int getLinesIn() {
		return this.linesIn;
	}
	
	/**
	 * @return the linesOut
	 */
	public final int getLinesOut() {
		return this.linesOut;
	}
	
	/**
	 * @return the newHash
	 */
	public final String getNewHash() {
		return this.newHash;
	}
	
	/**
	 * @return the newMode
	 */
	public final int getNewMode() {
		return this.newMode;
	}
	
	/**
	 * @return the oldHash
	 */
	public final String getOldHash() {
		return this.oldHash;
	}
	
	/**
	 * @return the oldMode
	 */
	public final int getOldMode() {
		return this.oldMode;
	}
	
	/**
	 * Gets the source id.
	 *
	 * @return the sourceId
	 */
	public final long getSourceId() {
		return this.sourceId;
	}
	
	/**
	 * Gets the target id.
	 *
	 * @return the targetId
	 */
	public final long getTargetId() {
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
		result = prime * result + (int) (this.sourceId ^ this.sourceId >>> 32);
		result = prime * result + (int) (this.targetId ^ this.targetId >>> 32);
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see org.mozkito.libraries.sequel.IEntity#setId(long)
	 */
	@Override
	public void setId(final long id) {
		this.id = id;
	}
	
	/**
	 * @param linesIn
	 *            the linesIn to set
	 */
	public final void setLinesIn(final int linesIn) {
		this.linesIn = linesIn;
	}
	
	/**
	 * @param linesOut
	 *            the linesOut to set
	 */
	public final void setLinesOut(final int linesOut) {
		this.linesOut = linesOut;
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
		builder.append(", sourceId=");
		builder.append(this.sourceId);
		builder.append(", targetId=");
		builder.append(this.targetId);
		builder.append(", confidence=");
		builder.append(this.confidence);
		builder.append(", oldMode=");
		builder.append(this.oldMode);
		builder.append(", newMode=");
		builder.append(this.newMode);
		builder.append(", oldHash=");
		builder.append(this.oldHash);
		builder.append(", newHash=");
		builder.append(this.newHash);
		builder.append(", linesIn=");
		builder.append(this.linesIn);
		builder.append(", linesOut=");
		builder.append(this.linesOut);
		builder.append("]");
		return builder.toString();
	}
	
}
