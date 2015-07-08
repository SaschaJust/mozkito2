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

import org.mozkito.libraries.sequel.Database;
import org.mozkito.libraries.sequel.Database.Type;
import org.mozkito.libraries.sequel.DatabaseManager;
import org.mozkito.libraries.sequel.EntityFactory;

/**
 * The Class IntegrationsPerTime.
 *
 * @author Sascha Just
 */
public class IntegrationsPerTime implements EntityFactory<IntegrationsPerTime> {
	
	/** The type. */
	private Type   type;
	
	/** The integration time. */
	public Instant integrationTime;
	
	/** The handle id. */
	public long    handleId;
	
	/**
	 * Instantiates a new integrations per time.
	 *
	 * @param type
	 *            the type
	 */
	public IntegrationsPerTime(final Database.Type type) {
		this.type = type;
	}
	
	/**
	 * Instantiates a new integrations per time.
	 *
	 * @param handle_id
	 *            the handle_id
	 * @param commit_time
	 *            the commit_time
	 */
	public IntegrationsPerTime(final long handle_id, final Instant commit_time) {
		this.handleId = handle_id;
		this.integrationTime = commit_time;
	}
	
	/**
	 * Creates the.
	 *
	 * @param handle_id
	 *            the handle_id
	 * @param commit_time
	 *            the commit_time
	 * @return the integrations per time
	 */
	public IntegrationsPerTime create(final long handle_id,
	                                  final Instant commit_time) {
		return new IntegrationsPerTime(handle_id, commit_time);
	}
	
	/**
	 * Query.
	 *
	 * @return the string
	 */
	public String query() {
		return DatabaseManager.loadStatement(this.type, "query_integrations_per_week_microsoft_internal");
	}
}
