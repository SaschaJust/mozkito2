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

import java.sql.Timestamp;

import org.mozkito.core.libs.versions.model.Tag;
import org.mozkito.libraries.sequel.Database.TxMode;
import org.mozkito.libraries.sequel.Database.Type;
import org.mozkito.libraries.sequel.bulk.AbstractAdapter;

/**
 * The Class TagAdapter.
 *
 * @author Sascha Just
 */
public class TagAdapter extends AbstractAdapter<Tag> {
	
	/**
	 * Instantiates a new tag adapter.
	 *
	 * @param type
	 *            the type
	 * @param mode
	 *            the mode
	 */
	public TagAdapter(final Type type, final TxMode mode) {
		super(type, mode, "tags");
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.libraries.sequel.bulk.AbstractAdapter#save(org.mozkito.libraries.sequel.IEntity)
	 */
	@Override
	public void save(final Tag entity) {
		this.writer.write(entity.getId(), entity.getDepotId(), entity.getHeadId(), entity.getName(), entity.getHash(),
		                  entity.getMessage(), entity.getIdentityId(), Timestamp.from(entity.getTimestamp()));
	}
	
}
