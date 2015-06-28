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
