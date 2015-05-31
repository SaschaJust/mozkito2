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
import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;

import org.mozkito.core.libs.users.model.Identity;
import org.mozkito.core.libs.versions.Graph;
import org.mozkito.core.libs.versions.model.Branch;
import org.mozkito.core.libs.versions.model.ChangeSet;
import org.mozkito.core.libs.versions.model.Depot;
import org.mozkito.core.libs.versions.model.Handle;
import org.mozkito.core.libs.versions.model.Revision;
import org.mozkito.libraries.logging.Logger;
import org.mozkito.skeleton.commons.URIUtils;
import org.mozkito.skeleton.exec.Command;
import org.mozkito.skeleton.sequel.DatabaseDumper;

/**
 * The Class TaskRunner.
 *
 * @author Sascha Just
 */
public class TaskRunner implements Runnable {
	
	/**
	 * The Enum Task.
	 */
	public static enum Task {
		
		/** The branches. */
		BRANCHES,
		/** The changesets. */
		CHANGESETS,
		/** The graph. */
		GRAPH,
		/** The integration. */
		INTEGRATION,
		/** The endpoints. */
		ENDPOINTS;
	}
	
	/**
	 * Sanitize.
	 *
	 * @param fileName
	 *            the file name
	 * @return the string
	 */
	private static String sanitize(final String fileName) {
		return fileName.replace(File.separator, "_").replace('-', '_').replace(".git", "").trim().toLowerCase()
		               .replaceAll("[^0-9a-z_]", "").replaceAll("(^_*|_*$)", "");
	}
	
	/** The work dir. */
	private final File                      workDir;
	
	/** The clone dir. */
	private final File                      cloneDir;
	
	/** The graph. */
	private final Graph                     graph;
	
	/** The uri. */
	private final URI                       uri;
	
	/** The tasks. */
	private final Task[]                    tasks;
	
	/** The depot. */
	private final Depot                     depot;
	
	/** The clone name. */
	private final String                    cloneName;
	
	/** The change set dumper. */
	private final DatabaseDumper<ChangeSet> changeSetDumper;
	
	/** The identity dumper. */
	private final DatabaseDumper<Identity>  identityDumper;
	
	/** The revision dumper. */
	private final DatabaseDumper<Revision>  revisionDumper;
	
	/** The branch dumper. */
	private final DatabaseDumper<Branch>    branchDumper;
	
	/** The handle dumper. */
	private final DatabaseDumper<Handle>    handleDumper;
	
	/** The graph dumper. */
	private final DatabaseDumper<Graph>     graphDumper;
	
	/** The depot dumper. */
	private final DatabaseDumper<Depot>     depotDumper;
	
	/**
	 * Instantiates a new task runner.
	 *
	 * @param baseDir
	 *            the base dir
	 * @param workDir
	 *            the work dir
	 * @param depotURI
	 *            the depot uri
	 * @param tasks
	 *            the tasks
	 * @param identityDumper
	 *            the identity dumper
	 * @param changeSetDumper
	 *            the change set dumper
	 * @param revisionDumper
	 *            the revision dumper
	 * @param branchDumper
	 *            the branch dumper
	 * @param handleDumper
	 *            the handle dumper
	 * @param graphDumper
	 *            the graph dumper
	 * @param depotDumper
	 *            the depot dumper
	 */
	public TaskRunner(final File baseDir, final File workDir, final URI depotURI, final Task[] tasks,
	        final DatabaseDumper<Identity> identityDumper, final DatabaseDumper<ChangeSet> changeSetDumper,
	        final DatabaseDumper<Revision> revisionDumper, final DatabaseDumper<Branch> branchDumper,
	        final DatabaseDumper<Handle> handleDumper, final DatabaseDumper<Graph> graphDumper,
	        final DatabaseDumper<Depot> depotDumper) {
		Thread.setDefaultUncaughtExceptionHandler(new MozkitoHandler());
		
		this.identityDumper = identityDumper;
		this.changeSetDumper = changeSetDumper;
		this.revisionDumper = revisionDumper;
		this.branchDumper = branchDumper;
		this.handleDumper = handleDumper;
		this.graphDumper = graphDumper;
		this.depotDumper = depotDumper;
		
		this.workDir = workDir;
		this.uri = depotURI;
		this.tasks = tasks;
		final URI baseURI = baseDir.toURI();
		if (depotURI.equals(baseURI)) {
			this.cloneName = sanitize(baseDir.getName());
		} else {
			
			this.cloneName = sanitize(baseURI.relativize(depotURI).getPath());
		}
		this.cloneDir = new File(workDir, this.cloneName);
		this.depot = new Depot(this.cloneName, depotURI, Instant.now());
		this.depotDumper.saveLater(this.depot);
		this.graph = new Graph(this.depot);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		Thread.currentThread().setName("Runner:" + this.depot.getName());
		
		Logger.info("Cloning depot '%s'.", this.cloneName);
		final Command command = Command.execute("git",
		                                        new String[] { "clone", "-n", "-q", URIUtils.uri2String(this.uri),
		                                                this.cloneDir.getAbsolutePath() }, this.workDir);
		command.waitFor();
		
		if (command.exitValue() != 0) {
			String resLine;
			if (Logger.logError()) {
				while ((resLine = command.nextErrput()) != null) {
					Logger.error.println(resLine);
				}
			}
			return;
		}
		
		Map<String, Branch> branchHeads;
		if (ArrayUtils.contains(this.tasks, Task.BRANCHES)) {
			Logger.info("Spawning BranchMiner.");
			
			final BranchMiner branchMiner = new BranchMiner(this.cloneDir, this.depot, this.branchDumper);
			branchMiner.run();
			branchHeads = branchMiner.getBranchHeads();
			this.graph.setBranchHeads(branchHeads);
		} else {
			// TODO load from DB
			branchHeads = new HashMap<String, Branch>();
		}
		
		Map<String, ChangeSet> changeSets = new HashMap<String, ChangeSet>();
		
		if (ArrayUtils.contains(this.tasks, Task.CHANGESETS)) {
			Logger.info("Spawning ChangeSetMiner.");
			final ChangeSetMiner changeSetMiner = new ChangeSetMiner(this.cloneDir, this.depot, this.graph,
			                                                         this.identityDumper, this.changeSetDumper,
			                                                         this.revisionDumper, this.handleDumper);
			changeSetMiner.run();
			changeSets = changeSetMiner.getChangeSets();
			this.graph.setChangeSets(changeSets);
		} else {
			// TODO load changesets and add to graph and the hashmap
		}
		
		if (ArrayUtils.contains(this.tasks, Task.ENDPOINTS)) {
			Logger.info("Spawning EndPointMiner.");
			final EndPointMiner endPointMiner = new EndPointMiner(this.cloneDir, this.depot, branchHeads, changeSets,
			                                                      this.graph);
			endPointMiner.run();
		}
		
		if (ArrayUtils.contains(this.tasks, Task.GRAPH)) {
			Logger.info("Spawning GraphBuilder.");
			final GraphMiner graphMiner = new GraphMiner(this.cloneDir, this.graph, changeSets);
			graphMiner.run();
		}
		
		if (ArrayUtils.contains(this.tasks, Task.INTEGRATION)) {
			Logger.info("Spawning IntegrationBuilder.");
			
			final IntegrationMiner integrationMiner = new IntegrationMiner(this.graph);
			integrationMiner.run();
			this.graphDumper.saveLater(this.graph);
			
		}
		
	}
	
}
