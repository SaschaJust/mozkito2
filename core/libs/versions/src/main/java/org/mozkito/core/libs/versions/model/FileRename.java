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

import org.mozkito.skeleton.sequel.ISequelEntity;

/**
 * @author Sascha Just
 *
 */
public class FileRename implements ISequelEntity {
	
	private long id;
	private long changeSetId;
	private long oldFileId;
	private long newFileId;
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelEntity#id()
	 */
	public Object id() {
		// TODO Auto-generated method stub
		// return null;
		throw new RuntimeException("Method 'id' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.sequel.ISequelEntity#id(java.lang.Object)
	 */
	public void id(final Object id) {
		// TODO Auto-generated method stub
		//
		throw new RuntimeException("Method 'id' has not yet been implemented."); //$NON-NLS-1$
		
	}
	
}
