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
