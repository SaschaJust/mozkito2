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

import org.mozkito.skeleton.contracts.Asserts;
import org.mozkito.skeleton.contracts.Requires;
import org.mozkito.skeleton.sequel.ISequelEntity;

/**
 * The Class Renaming.
 *
 * @author Sascha Just
 */
public class Renaming implements ISequelEntity {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 856906626581054545L;
	
	/** The id. */
	private long              id;
	
	/** The handles. */
	private final Set<Handle> handles          = new HashSet<>();
	
	/**
	 * Instantiates a new renaming.
	 */
	public Renaming() {
	}
	
	/**
	 * Instantiates a new renaming.
	 *
	 * @param handles
	 *            the handles
	 */
	public Renaming(final Collection<Handle> handles) {
		addAll(handles);
	}
	
	/**
	 * Adds the.
	 *
	 * @param handle
	 *            the handle
	 * @return true, if successful
	 */
	public boolean add(final Handle handle) {
		Requires.notNull(handle);
		Asserts.notNull(this.handles);
		return this.handles.add(handle);
	}
	
	/**
	 * Adds the all.
	 *
	 * @param handles
	 *            the handles
	 * @return true, if successful
	 */
	public boolean addAll(final Collection<Handle> handles) {
		Requires.notNull(this.handles);
		Asserts.notNull(this.handles);
		return this.handles.addAll(handles);
	}
	
	/**
	 * Gets the handles.
	 *
	 * @return the handles
	 */
	public Collection<Handle> getHandles() {
		return UnmodifiableCollection.unmodifiableCollection(this.handles);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelEntity#id()
	 */
	public long id() {
		return this.id;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelEntity#id(long)
	 */
	public void id(final long id) {
		this.id = id;
	}
	
}
