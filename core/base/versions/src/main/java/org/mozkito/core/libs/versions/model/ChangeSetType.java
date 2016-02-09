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

import org.mozkito.core.libs.versions.model.enums.IntegrationType;
import org.mozkito.libraries.sequel.IEntity;

/**
 * The Class ChangeSetIntegration.
 *
 * @author Sascha Just
 */
public class ChangeSetType implements IEntity {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5384066977342816045L;
	
	/** The change set id. */
	private long              changeSetId;
	
	/** The integration type. */
	private final short       integrationType;
	
	/**
	 * Instantiates a new change set integration.
	 *
	 * @param changeSetId
	 *            the change set id
	 * @param integrationType
	 *            the integration type
	 */
	public ChangeSetType(final long changeSetId, final IntegrationType integrationType) {
		super();
		this.changeSetId = changeSetId;
		this.integrationType = integrationType.getValue();
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
		final ChangeSetType other = (ChangeSetType) obj;
		if (this.changeSetId != other.changeSetId) {
			return false;
		}
		if (this.integrationType != other.integrationType) {
			return false;
		}
		return true;
	}
	
	/**
	 * @return the integrationType
	 */
	public final short getIntegrationType() {
		return this.integrationType;
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
		result = prime * result + this.integrationType;
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.IEntity#getId()
	 */
	public long getId() {
		return this.changeSetId;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.IEntity#setId(long)
	 */
	public void setId(final long id) {
		this.changeSetId = id;
	}
	
}
