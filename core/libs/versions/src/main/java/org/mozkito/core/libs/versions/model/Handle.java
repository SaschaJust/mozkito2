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

import org.mozkito.skeleton.contracts.Requires;
import org.mozkito.skeleton.sequel.ISequelEntity;

// TODO: Auto-generated Javadoc
/**
 * The Class File.
 *
 * @author Sascha Just
 */
public class Handle implements ISequelEntity {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 8059281977354410692L;
	
	/** The id. */
	private long              id;
	
	/** The path. */
	private final String      path;
	
	/**
	 * Instantiates a new handle.
	 *
	 * @param path
	 *            the path
	 */
	public Handle(final String path) {
		this.path = path;
	}
	
	/**
	 * Gets the path.
	 *
	 * @return the path
	 */
	public final String getPath() {
		return this.path;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelEntity#id()
	 */
	public Long id() {
		return this.id;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelEntity#id(java.lang.Object)
	 */
	public void id(final Object id) {
		Requires.notNull(id);
		Requires.isLong(id);
		this.id = (Long) id;
	}
	
}
