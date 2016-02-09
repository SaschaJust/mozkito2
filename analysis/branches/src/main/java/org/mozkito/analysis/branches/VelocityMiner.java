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
