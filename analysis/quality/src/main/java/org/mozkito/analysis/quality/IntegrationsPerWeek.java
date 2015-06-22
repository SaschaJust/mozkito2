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

package org.mozkito.analysis.quality;

import java.time.Instant;

import org.mozkito.skeleton.sequel.Database;
import org.mozkito.skeleton.sequel.Database.Type;
import org.mozkito.skeleton.sequel.DatabaseManager;
import org.mozkito.skeleton.sequel.EntityFactory;

/**
 * @author Sascha Just
 *
 */
public class IntegrationsPerWeek implements EntityFactory<IntegrationsPerWeek> {
	
	private Type type;
	
	public IntegrationsPerWeek(final Database.Type type) {
		this.type = type;
	}
	
	/**
	 * @param handle_id
	 * @param week
	 * @param effective_integrations
	 */
	public IntegrationsPerWeek(final long handle_id, final Instant week, final int effective_integrations) {
	}
	
	public IntegrationsPerWeek create(final long handle_id,
	                                  final Instant week,
	                                  final int effective_integrations) {
		return new IntegrationsPerWeek(handle_id, week, effective_integrations);
	}
	
	public String query() {
		return DatabaseManager.loadStatement(this.type, "query_integrations_per_week");
	}
}
