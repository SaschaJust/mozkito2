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

package org.mozkito.core.libs.versions.adapters.bulk;

import org.mozkito.core.libs.versions.model.Reference;
import org.mozkito.libraries.sequel.Database;
import org.mozkito.libraries.sequel.bulk.AbstractAdapter;

/**
 * @author Sascha Just
 *
 */
public class ReferenceAdapter extends AbstractAdapter<Reference> {
	
	/**
	 * Instantiates a new PG branch adapter.
	 *
	 * @param type
	 *            the type
	 * @param mode
	 *            the mode
	 */
	public ReferenceAdapter(final Database.Type type, final Database.TxMode mode) {
		super(type, mode, "refs");
	}
	
	/**
	 * Save.
	 *
	 * @param reference
	 *            the reference
	 */
	@Override
	public void save(final Reference reference) {
		this.writer.write(reference.getId(), reference.getType(), reference.getDepotId(), reference.getName());
	}
}
