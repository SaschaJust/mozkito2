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
 
package org.mozkito.core.libs.versions.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections4.collection.UnmodifiableCollection;

import org.mozkito.libraries.sequel.IEntity;
import org.mozkito.skeleton.contracts.Asserts;
import org.mozkito.skeleton.contracts.Requires;

/**
 * The Class Renaming.
 *
 * @author Sascha Just
 */
public class Renaming implements IEntity {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 856906626581054545L;
	
	/** The id. */
	private long              id;
	
	/** The handles. */
	private final Set<Entry>  entries          = new HashSet<>();
	
	/**
	 * Instantiates a new renaming.
	 */
	public Renaming() {
	}
	
	/**
	 * Instantiates a new renaming.
	 *
	 * @param entries
	 *            the entries
	 */
	public Renaming(final Collection<Entry> entries) {
		addAll(entries);
	}
	
	/**
	 * Adds the.
	 *
	 * @param similarity
	 *            the similarity
	 * @param sourceHandle
	 *            the source handle
	 * @param targetHandle
	 *            the target handle
	 * @param where
	 *            the where
	 * @return true, if successful
	 */
	public boolean add(final short similarity,
	                   final Handle sourceHandle,
	                   final Handle targetHandle,
	                   final ChangeSet where) {
		Requires.notNull(sourceHandle);
		Requires.notNull(targetHandle);
		Requires.notNull(where);
		Requires.notNegative(similarity);
		Requires.lessOrEqual(similarity, 100);
		Requires.positive(sourceHandle.getId());
		Requires.positive(targetHandle.getId());
		
		Asserts.notNull(this.entries);
		return this.entries.add(new Entry(similarity, sourceHandle.getId(), targetHandle.getId(), where.getId()));
	}
	
	/**
	 * Adds the entry.
	 *
	 * @param similarity
	 *            the similarity
	 * @param from
	 *            the from
	 * @param to
	 *            the to
	 * @param where
	 *            the where
	 * @return true, if successful
	 */
	public boolean add(final short similarity,
	                   final long from,
	                   final long to,
	                   final long where) {
		Requires.notNegative(similarity);
		Requires.lessOrEqual(similarity, 100);
		Requires.positive(from);
		Requires.positive(to);
		Requires.positive(where);
		return this.entries.add(new Entry(similarity, from, to, where));
	}
	
	/**
	 * Adds the all.
	 *
	 * @param entries
	 *            the entries
	 * @return true, if successful
	 */
	public boolean addAll(final Collection<Entry> entries) {
		Requires.notNull(entries);
		Asserts.notNull(this.entries);
		return this.entries.addAll(entries);
	}
	
	/**
	 * Gets the handles.
	 *
	 * @return the handles
	 */
	public Collection<Entry> getEntries() {
		return UnmodifiableCollection.unmodifiableCollection(this.entries);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.IEntity#getId()
	 */
	public long getId() {
		return this.id;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.IEntity#setId(long)
	 */
	public void setId(final long id) {
		this.id = id;
	}
	
}
