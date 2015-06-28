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

import org.mozkito.core.libs.versions.model.enums.ReferenceType;

/**
 * The Class Branch.
 *
 * @author Sascha Just
 */
public class Branch extends Reference {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7009136564697082934L;
	
	/**
	 * Instantiates a new branch.
	 *
	 * @param depot
	 *            the depot
	 * @param name
	 *            the name
	 * @param changeSetId
	 *            the change set id
	 */
	public Branch(final Depot depot, final String name, final long changeSetId) {
		super(depot, ReferenceType.BRANCH, name, changeSetId);
	}
	
}
