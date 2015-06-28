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

package org.mozkito.core.libs.versions.graph;

import org.mozkito.core.libs.versions.model.Reference;
import org.mozkito.core.libs.versions.model.enums.BranchMarker;
import org.mozkito.core.libs.versions.model.enums.IntegrationMarker;
import org.mozkito.core.libs.versions.model.enums.NavigationMarker;

/**
 * The Class Label.
 */
public class Label {
	
	/** The branch. */
	public Reference         reference;
	
	/** The nagivation marker. */
	public NavigationMarker  navigationMarker;
	
	/** The integration marker. */
	public IntegrationMarker integrationMarker;
	
	/**
	 * Instantiates a new label.
	 *
	 * @param reference
	 *            the branch
	 * @param branchMarker
	 *            the type
	 */
	public Label(final Reference reference, final BranchMarker branchMarker) {
		this.reference = reference;
		this.navigationMarker = null;
		this.integrationMarker = IntegrationMarker.DIVERGE;
	}
	
}
