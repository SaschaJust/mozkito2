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

import java.sql.Connection;

import org.mozkito.core.libs.versions.model.Root;
import org.mozkito.libraries.sequel.Database.TxMode;
import org.mozkito.libraries.sequel.Database.Type;
import org.mozkito.libraries.sequel.bulk.AbstractAdapter;

/**
 * The Class RootAdapter.
 *
 * @author Sascha Just
 */
public class RootAdapter extends AbstractAdapter<Root> {
	
	/**
	 * Instantiates a new root adapter.
	 *
	 * @param type
	 *            the type
	 * @param mode
	 *            the mode
	 * @param connection
	 *            the connection
	 */
	public RootAdapter(final Type type, final TxMode mode, final Connection connection) {
		super(type, mode, "root", connection);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.bulk.AbstractAdapter#save(org.mozkito.libraries.sequel.IEntity)
	 */
	@Override
	public void save(final Root entity) {
		this.writer.write(entity.getId(), entity.getBranchId(), entity.getBranchId());
	}
	
}
