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

package org.mozkito.core.apps.versions;

import java.io.File;
import java.util.Map;

import org.mozkito.core.libs.versions.IntegrationType;
import org.mozkito.core.libs.versions.model.ChangeSet;
import org.mozkito.core.libs.versions.model.ChangeSetIntegration;
import org.mozkito.core.libs.versions.model.Depot;
import org.mozkito.skeleton.contracts.Asserts;
import org.mozkito.skeleton.exec.Command;
import org.mozkito.skeleton.sequel.DatabaseDumper;

/**
 * The Class IntegrationMiner.
 *
 * @author Sascha Just
 */
public class IntegrationMiner extends Task implements Runnable {
	
	/** The clone dir. */
	private final File                                 cloneDir;
	
	/** The change sets. */
	private final Map<String, ChangeSet>               changeSets;
	
	/** The integration type dumper. */
	private final DatabaseDumper<ChangeSetIntegration> integrationTypeDumper;
	
	/**
	 * Instantiates a new integration miner.
	 *
	 * @param depot
	 *            the depot
	 * @param cloneDir
	 *            the clone dir
	 * @param changeSets
	 *            the change sets
	 * @param integrationTypeDumper
	 *            the integration type dumper
	 */
	public IntegrationMiner(final Depot depot, final File cloneDir, final Map<String, ChangeSet> changeSets,
	        final DatabaseDumper<ChangeSetIntegration> integrationTypeDumper) {
		super(depot);
		this.cloneDir = cloneDir;
		this.changeSets = changeSets;
		this.integrationTypeDumper = integrationTypeDumper;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		final Command command = Command.execute("git", new String[] { "log", "--no-abbrev", "--format=%H %P",
		        "--branches", "--remotes" }, this.cloneDir);
		
		ChangeSet current;
		ChangeSetIntegration csi;
		
		String line;
		while ((line = command.nextOutput()) != null) {
			final String[] split = line.trim().split("\\s+");
			current = this.changeSets.get(split[0]);
			if (split.length == 1) {
				// found root
				csi = new ChangeSetIntegration(current, IntegrationType.EDIT);
				this.integrationTypeDumper.saveLater(csi);
			} else {
				Asserts.greater(split.length, 1, "There has to be a parent at this point.");
				
				if (split.length > 2) {
					csi = new ChangeSetIntegration(current, IntegrationType.MERGE);
				} else {
					csi = new ChangeSetIntegration(current, IntegrationType.EDIT);
				}
				this.integrationTypeDumper.saveLater(csi);
			}
		}
	}
	
	/**
	 * Compute integration graph.
	 *
	 * @param branch
	 *            the branch
	 */
	
}
