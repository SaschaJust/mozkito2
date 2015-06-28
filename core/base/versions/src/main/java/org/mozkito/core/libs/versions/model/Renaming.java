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
