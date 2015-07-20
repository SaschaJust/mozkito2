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

import org.mozkito.core.libs.versions.model.Handle;
import org.mozkito.libraries.sequel.Database.TxMode;
import org.mozkito.libraries.sequel.Database.Type;
import org.mozkito.libraries.sequel.bulk.AbstractAdapter;

/**
 * The Class HandleAdapter.
 *
 * @author Sascha Just
 */
public class HandleAdapter extends AbstractAdapter<Handle> {
	
	/**
	 * Instantiates a new handle adapter.
	 *
	 * @param type
	 *            the type
	 * @param mode
	 *            the mode
	 */
	public HandleAdapter(final Type type, final TxMode mode) {
		super(type, mode, "handles");
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.bulk.AbstractAdapter#save(org.mozkito.libraries.sequel.IEntity)
	 */
	@Override
	public void save(final Handle entity) {
		this.writer.write(entity.getId(), entity.getDepotId(), truncate(entity.getPath(), 900));
	}
	
}
