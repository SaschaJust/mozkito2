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
 
package org.mozkito.core.libs.versions.model.enums;

/**
 * The Enum EdgeType.
 */
public enum BranchMarker {
	
	/** The branch parent. */
	BRANCH_PARENT (1),
	/** The merge parent. */
	MERGE_PARENT (2);
	
	/** The value. */
	private short value;
	
	/**
	 * Instantiates a new branch marker.
	 *
	 * @param value
	 *            the value
	 */
	BranchMarker(final int value) {
		this.value = (short) value;
	}
	
	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public final short getValue() {
		return this.value;
	}
	
}
