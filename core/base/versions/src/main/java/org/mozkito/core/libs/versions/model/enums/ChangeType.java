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

import java.util.ArrayList;
import java.util.List;

import org.mozkito.core.libs.versions.model.Handle;
import org.mozkito.core.libs.versions.model.Revision;

/**
 * The enum {@link ChangeType} is used to model file revisions in the depot log.
 *
 * @author Sascha Just
 * @see Revision
 * @see Handle
 */
public enum ChangeType {
	
	/** The add. */
	ADDED ((short) 1, 'A'),
	
	/** The delete. */
	DELETED ((short) 2, 'D'),
	
	/** The modified. */
	MODIFIED ((short) 4, 'M'),
	
	/** The renamed. */
	RENAMED ((short) 8, 'R'),
	
	/** The copied. */
	COPIED ((short) 16, 'C'),
	
	/** The type change. */
	TYPE_CHANGE ((short) 32, 'T'),
	
	/** The unmerged. */
	UNMERGED ((short) 64, 'U'),
	
	/** The unknown. */
	UNKNOWN ((short) 128, 'X'),
	
	/** The broken pairing. */
	BROKEN_PAIRING ((short) 256, 'B')
	
	;
	
	/**
	 * From.
	 *
	 * @param c
	 *            the c
	 * @return the change type
	 */
	public static ChangeType from(final char c) {
		switch (c) {
			case 'M':
				return MODIFIED;
			case 'A':
				return ADDED;
			case 'D':
				return DELETED;
			case 'R':
				return RENAMED;
			case 'C':
				return COPIED;
			case 'T':
				return TYPE_CHANGE;
			case 'U':
				return UNMERGED;
			case 'X':
				return UNKNOWN;
			case 'B':
				return BROKEN_PAIRING;
			default:
				return null;
		}
	}
	
	/**
	 * From mask.
	 *
	 * @param mask
	 *            the mask
	 * @return the list
	 */
	public static List<ChangeType> fromMask(final short mask) {
		final List<ChangeType> list = new ArrayList<ChangeType>(ChangeType.values().length);
		for (final ChangeType changeType : values()) {
			if ((mask & changeType.getValue()) != 0) {
				list.add(changeType);
			}
		}
		return list;
	}
	
	/**
	 * To mask.
	 *
	 * @param changeTypes
	 *            the change types
	 * @return the short
	 */
	public static short toMask(final ChangeType... changeTypes) {
		short ret = 0;
		for (final ChangeType changeType : changeTypes) {
			ret |= changeType.getValue();
		}
		return ret;
	}
	
	/** The value. */
	private short value;
	
	/** The tag. */
	private char  tag;
	
	/**
	 * Instantiates a new change type.
	 *
	 * @param i
	 *            the i
	 * @param tag
	 *            the tag
	 */
	ChangeType(final short i, final char tag) {
		this.value = i;
		this.tag = tag;
	}
	
	/**
	 * Gets the tag.
	 *
	 * @return the tag
	 */
	public char getTag() {
		return this.tag;
	}
	
	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public short getValue() {
		return this.value;
	}
	
	/**
	 * To mask.
	 *
	 * @return the short
	 */
	public short toMask() {
		return getValue();
	}
	
}
