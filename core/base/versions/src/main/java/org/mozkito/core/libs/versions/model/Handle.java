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

import org.mozkito.libraries.sequel.IEntity;
import org.mozkito.skeleton.contracts.Requires;

/**
 * The Class File.
 *
 * @author Sascha Just
 */
public class Handle implements IEntity {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 8059281977354410692L;
	
	/** The id. */
	private long              id;
	
	/** The path. */
	private final String      path;
	
	/** The depot id. */
	private final long        depotId;
	
	/**
	 * Instantiates a new handle.
	 *
	 * @param depot
	 *            the depot
	 * @param path
	 *            the path
	 */
	public Handle(final Depot depot, final String path) {
		Requires.notNull(depot);
		Requires.positive(depot.getId());
		Requires.notNull(path);
		Requires.notEmpty(path);
		
		this.path = path;
		this.depotId = depot.getId();
	}
	
	/**
	 * Gets the depot id.
	 *
	 * @return the depotId
	 */
	public final long getDepotId() {
		return this.depotId;
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
