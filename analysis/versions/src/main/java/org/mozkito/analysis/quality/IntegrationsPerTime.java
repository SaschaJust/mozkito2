/***********************************************************************************************************************
 * MIT License
 *  
 * Copyright (c) 2015 mozkito.org
 *  
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *  
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *  
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * 
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
		return DatabaseManager.loadStatement(this.type, "query_integrations_per_week_chromium_aggregated");
	}
}
