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

package org.mozkito.core.libs.versions;

import java.util.ArrayList;
import java.util.List;

/**
 * The Enum ChangeType.
 *
 * @author Sascha Just
 */
public enum ChangeType {
	
	/** The add. */
	ADD (1),
	
	/** The delete. */
	DELETE (2),
	
	/** The modified. */
	MODIFIED (4),
	
	/** The renamed. */
	RENAMED (8);
	
	/** The value. */
	private int value;
	
	/**
	 * Instantiates a new change type.
	 *
	 * @param i
	 *            the i
	 */
	ChangeType(final int i) {
		this.value = i;
	}
	
	/**
	 * From mask.
	 *
	 * @param mask
	 *            the mask
	 * @return the list
	 */
	public List<ChangeType> fromMask(final short mask) {
		final List<ChangeType> list = new ArrayList<ChangeType>(ChangeType.values().length);
		for (final ChangeType changeType : values()) {
			if ((mask & changeType.getValue()) != 0) {
				list.add(changeType);
			}
		}
		return list;
	}
	
	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public int getValue() {
		return this.value;
	}
	
	/**
	 * To mask.
	 *
	 * @param changeTypes
	 *            the change types
	 * @return the short
	 */
	public short toMask(final ChangeType... changeTypes) {
		short ret = 0;
		for (final ChangeType changeType : changeTypes) {
			ret |= changeType.getValue();
		}
		return ret;
	}
	
}
