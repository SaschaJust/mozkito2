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

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections4.list.UnmodifiableList;

import org.mozkito.skeleton.contracts.Requires;
import org.mozkito.skeleton.sequel.ISequelEntity;

/**
 * The Class Roots.
 *
 * @author Sascha Just
 */
public class Roots implements ISequelEntity {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3326771349421256531L;
	
	/** The id. */
	private long              id;
	
	/** The branch id. */
	private final long        branchId;
	
	/** The change set ids. */
	private final List<Long>  changeSetIds     = new LinkedList<>();
	
	/**
	 * Instantiates a new roots.
	 *
	 * @param branchId
	 *            the branch id
	 */
	public Roots(final long branchId) {
		Requires.positive(branchId);
		
		this.branchId = branchId;
	}
	
	/**
	 * Adds the.
	 *
	 * @param changeSetId
	 *            the change set id
	 */
	public void add(final long changeSetId) {
		this.changeSetIds.add(changeSetId);
	}
	
	/**
	 * Gets the branch id.
	 *
	 * @return the branchId
	 */
	public final long getBranchId() {
		return this.branchId;
	}
	
	/**
	 * Gets the change set ids.
	 *
	 * @return the changeSetIds
	 */
	public final List<Long> getChangeSetIds() {
		return UnmodifiableList.unmodifiableList(this.changeSetIds);
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
