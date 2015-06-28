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

/**
 * The Class ChangeSetOrigin.
 *
 * @author Sascha Just
 */
public class ChangeSetOrigin implements IEntity {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -262697317026758489L;
	
	/** The id. */
	private long              id;
	
	/** The change set id. */
	private long              changeSetId;
	
	/** The branch id. */
	private long              branchId;
	
	/**
	 * @return the branchId
	 */
	public final long getBranchId() {
		return this.branchId;
	}
	
	/**
	 * @return the changeSetId
	 */
	public final long getChangeSetId() {
		return this.changeSetId;
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
