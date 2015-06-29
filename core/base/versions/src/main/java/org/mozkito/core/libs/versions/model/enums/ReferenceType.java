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
 * The Enum ReferenceType.
 *
 * @author Sascha Just
 */
public enum ReferenceType {
	
	/** The branch. */
	BRANCH (1),
	/** The tag. */
	TAG (1);
	
	/** The value. */
	private short value;
	
	/**
	 * Instantiates a new reference type.
	 *
	 * @param value
	 *            the value
	 */
	ReferenceType(final int value) {
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