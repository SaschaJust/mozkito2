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

package org.mozkito.skeleton.regex;

import java.util.Set;

/**
 * The Interface Match.
 * 
 * Used when {@link Regex} returns matches like in {@link Regex#find(String)}.
 * 
 * @author Sascha Just 
 */
public interface Match extends Iterable<Group> {
	
	/**
	 * Gets the {@link Group}s that corresponds to the <code>id</code>. Keep in mind that actual groups within a pattern
	 * are enumerated starting at 1, not at 0. Thus, 0 is not a valid <code>id</code>.
	 *
	 * @param id
	 *            the positive id
	 * @return the {@link Group}
	 * @since 0.1
	 * @deprecated use {@link Match#getGroup(int)} instead. This will be removed in the 0.2 release.
	 */
	@Deprecated
	Group get(final int id);
	
	/**
	 * Gets the full match of the pattern. This is guaranteed to not return null.
	 * 
	 * @return the full match
	 */
	Group getFullMatch();
	
	/**
	 * Gets the {@link Group}s that corresponds to the <code>id</code>. Keep in mind that actual groups within a pattern
	 * are enumerated starting at 1, not at 0. Thus, 0 is not a valid <code>id</code>.
	 * 
	 * @param id
	 *            the positive id
	 * @return the {@link Group} if <code>id</code> is valid; null otherwise
	 * @since 0.2
	 */
	Group getGroup(final int id);
	
	/**
	 * Gets the {@link Group}s that corresponds to the <code>name</code>. Returns <code>null</code> if there is no such
	 * group.
	 * 
	 * @param name
	 *            the name
	 * @return the {@link Group} if <code>name</code> is valid; null otherwise
	 * @since 0.2
	 */
	Group getGroup(final String name);
	
	/**
	 * Gets the number of {@link Group}s in the {@link Match}.
	 * 
	 * @return the number of {@link Group}s
	 * @since 0.2
	 */
	int getGroupCount();
	
	/**
	 * Gets name of all groups in the {@link Match}.
	 * 
	 * @return a {@link Set} containing all names of the {@link Group}s in the {@link Match}. Guaranteed to not be
	 *         <code>null</code>.
	 * @since 0.2
	 */
	Set<String> getGroupNames();
	
	/**
	 * Gets all {@link Group}s in the {@link Match}.
	 * 
	 * @return all {@link Group}s in the {@link Match}. Will return an empty array if there are none. Guaranteed to not
	 *         return <code>null</code>.
	 * @since 0.2
	 */
	Group[] getGroups();
	
	/**
	 * Gets the number of named {@link Group}s in the {@link Match}.
	 * 
	 * @return the number of named {@link Group}s
	 * @since 0.2
	 */
	int getNamedGroupCount();
	
	/**
	 * Gets all named {@link Group}s in the {@link Match}.
	 * 
	 * @return all named {@link Group}s in the {@link Match}. Will return an empty array if there are none. Guaranteed
	 *         to not return <code>null</code>.
	 * @since 0.2
	 */
	Group[] getNamedGroups();
	
	/**
	 * Checks for the group with <code>id</code>.
	 * 
	 * @param id
	 *            the id
	 * @return true, if successful
	 */
	boolean hasGroup(int id);
	
	/**
	 * Checks for any {@link Group}s in the {@link Match}.
	 * 
	 * @return true, if there is at least one.
	 * @since 0.2
	 */
	boolean hasGroups();
	
	/**
	 * Checks for the named group <code>name</code>.
	 * 
	 * @param name
	 *            the name
	 * @return true, if successful
	 */
	boolean hasNamedGroup(String name);
	
	/**
	 * Checks for any named {@link Group}s in the {@link Match}.
	 * 
	 * @return true, if there is at least one.
	 * @since 0.2
	 */
	boolean hasNamedGroups();
	
	/**
	 * Checks if is empty.
	 *
	 * @return true, if is empty
	 * @since 0.2
	 * @deprecated Use {@link Match#hasGroups()} or {@link Match#hasNamedGroups()} instead. This will be removed with
	 *             the 0.2 release.
	 */
	@Deprecated
	boolean isEmpty();
}
