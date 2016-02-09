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

package jregex;

import java.util.Set;

import org.mozkito.skeleton.contracts.Asserts;
import org.mozkito.skeleton.contracts.Requires;

/**
 * The Class NamedPattern.
 * 
 * @author Sascha Just 
 */
public class NamedPattern extends Pattern {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 658555765816034510L;
	
	/**
	 * Instantiates a new named pattern.
	 * 
	 * @param pattern
	 *            the pattern
	 */
	public NamedPattern(final String pattern) {
		super(pattern, 0);
	}
	
	/**
	 * Instantiates a new named pattern.
	 * 
	 * @param pattern
	 *            the pattern
	 * @param flags
	 *            the flags
	 */
	public NamedPattern(final String pattern, final int flags) {
		super(pattern, flags);
	}
	
	/**
	 * Gets the group name.
	 * 
	 * @param index
	 *            the index
	 * @return the group name
	 */
	public String getGroupName(final int index) {
		Requires.notNegative(index);
		
		Asserts.less(index, this.memregs, "Index of group has to be less then the number of active memory registers.");
		
		@SuppressWarnings ("unchecked")
		final Set<String> keySet = this.namedGroupMap.keySet();
		for (final String groupName : keySet) {
			if (this.namedGroupMap.get(groupName).equals(index)) {
				return groupName;
			}
		}
		return "";
		
	}
	
	/**
	 * Gets the groups names.
	 * 
	 * @return the groups names
	 */
	@SuppressWarnings ("unchecked")
	public Set<String> getGroupsNames() {
		return this.namedGroupMap.keySet();
	}
}
