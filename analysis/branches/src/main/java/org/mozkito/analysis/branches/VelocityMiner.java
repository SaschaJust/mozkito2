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

package org.mozkito.analysis.branches;

import java.sql.SQLException;
import java.util.Iterator;

import org.mozkito.core.libs.versions.adapters.BranchAdapter;
import org.mozkito.core.libs.versions.adapters.ChangeSetAdapter;
import org.mozkito.core.libs.versions.graph.Graph;
import org.mozkito.core.libs.versions.model.Branch;
import org.mozkito.core.libs.versions.model.ChangeSet;
import org.mozkito.skeleton.sequel.Database;
import org.mozkito.skeleton.sequel.Database.Type;

/**
 * @author Sascha Just
 *
 */
public class VelocityMiner {
	
	public static void main(final String[] args) throws SQLException {
		final String monitoredBranchString = "master";
		
		final Database database = new Database(Type.DERBY, "test", null, null, null, null);
		final Graph graph = Graph.load(database);
		
		final ChangeSetAdapter changeSetAdapter = new ChangeSetAdapter(database.getType());
		final BranchAdapter branchAdapter = new BranchAdapter(database.getType());
		final Iterator<ChangeSet> iterator = changeSetAdapter.load(database.getConnection());
		final ChangeSet changeSet = null;
		final Branch branch = null;
		// final Branch monitoredBranch = branchAdapter.loadByName(monitoredBranchString);
		
		// while (iterator.hasNext()) {
		// changeSet = iterator.next();
		// branch = branchAdapter.load(changeSet.getOrigin());
		// final List<ChangeSet> integrationPath = graph.getIntegrationPath(changeSet, branch);
		//
		// ChangeSet level1Integration = null;
		// ChangeSet targetIntegration = null;
		//
		// INTEGRATION_PATH: for (final ChangeSet cs : integrationPath) {
		// if (level1Integration == null && changeSet.getOrigin() != cs.getOrigin()) {
		// level1Integration = cs;
		// }
		// if (targetIntegration == null && cs.getOrigin() == monitoredBranch.id()) {
		// targetIntegration = cs;
		// }
		// if (targetIntegration != null) {
		// break INTEGRATION_PATH;
		// }
		// }
		//
		// Logger.info(MessageFormat.format("ChangeSet {0} Level1 {1} Monitor {2}", changeSet, level1Integration,
		// targetIntegration));
		// }
	}
}
