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

/**
 * The Interface MultiMatch.
 * 
 * This interface is used in {@link Regex} when returning multiple {@link Match}es like in {@link Regex#findAll(String)}
 * .
 * 
 * @author Sascha Just 
 */
public interface MultiMatch extends Iterable<Match> {
	
	/**
	 * Gets the match with the given index.
	 * 
	 * @param index
	 *            the index
	 * @return the match
	 * @deprecated use {@link MultiMatch#getMatch(int)} instead.
	 * @since 0.1
	 */
	@Deprecated
	Match get(final int index);
	
	/**
	 * Gets the {@link Group} with id <code>id</code> in the {@link Match} with index <code>index</code>. If you have a
	 * pattern <code>"(a) (b)"</code> and a string <code>"a b a b"</code>, <code>get(1, 1)</code> will return the
	 * 
	 * @param index
	 *            the index
	 * @param id
	 *            the id
	 * @return the {@link Group} with id <code>id</code> of the <code>index</code>th match or <code>null</code> if the
	 *         id is invalid. {@link Group} for the second a in the string since it is the 1st {@link Group} (
	 *         <code>id = 1</code>) within the 2nd {@link Match} (<code>index = 1</code>).
	 * 
	 *         Note: Please keep in mind that <code>id = 0</code> is not valid and won't return a {@link Group} that
	 *         corresponds to the complete match of the pattern. Requesting <code>id = 0</code> will result in the
	 *         function returning null (if the index is valid).
	 * @since 0.2
	 */
	Group get(final int index,
	          final int id);
	
	/**
	 * Gets the {@link Group} with name <code>name</code> in the {@link Match} with index <code>index</code>. If you
	 * have a pattern <code>"({x}a) ({y}b)"</code> and a string <code>"a b a b"</code>, <code>get(1, "x")</code> will
	 * return the {@link Group} for the second a in the string since it is the {@link Group} with name "x" within the
	 * 2nd {@link Match} (<code>index = 1</code>).
	 * 
	 * @param index
	 *            the index
	 * @param name
	 *            the name
	 * @return the {@link Group} with name <code>name</code> of the <code>index</code>th match or <code>null</code> if
	 *         the id is invalid.
	 * @since 0.2
	 */
	Group get(final int index,
	          final String name);
	
	/**
	 * Returns an array containing all {@link Group}s that corresponds to the given <code>id</code> of all {@link Match}
	 * es. Will return an empty array if the <code>id</code> is invalid.
	 * 
	 * @param id
	 *            the id
	 * @return all {@link Group}s for the given id.
	 * @since 0.2
	 */
	Group[] getGroup(final int id);
	
	/**
	 * Returns an array containing all {@link Group}s that corresponds to the given <code>name</code> of all.
	 * 
	 * @param name
	 *            the name
	 * @return all {@link Group}s for the given id. {@link Match}es. Will return an empty array if there are no
	 *         {@link Group}s with that <code>name</code> is invalid.
	 * @since 0.2
	 */
	Group[] getGroup(final String name);
	
	/**
	 * Gets the match with the given index.
	 * 
	 * @param index
	 *            the index
	 * @return the match with that corresponds to the index
	 * @since 0.2
	 */
	Match getMatch(final int index);
	
	/**
	 * Checks if there are any {@link Group}s in the pattern that matched.
	 * 
	 * @return true, if successful
	 * @since 0.2
	 */
	boolean hasGroups();
	
	/**
	 * Checks if there are any names {@link Group}s in the pattern that matched.
	 * 
	 * @return true, if successful
	 * @since 0.2
	 */
	boolean hasNamedGroups();
	
	/**
	 * Checks if there aren't any groups aside the full match of the pattern.
	 * 
	 * @return true, if is empty
	 * @since 0.1
	 * @deprecated You want to use {@link MultiMatch#hasGroups()} or {@link MultiMatch#hasNamedGroups()} instead. This
	 *             will be removed in the 0.2 release.
	 */
	@Deprecated
	boolean isEmpty();
	
	/**
	 * Returns the number of {@link Match}es in the {@link MultiMatch}.
	 * 
	 * @return the number of {@link Match}es.
	 * @since 0.1
	 */
	int size();
	
}
