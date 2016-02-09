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
