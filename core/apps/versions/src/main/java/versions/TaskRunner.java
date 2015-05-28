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

package versions;

import java.io.File;
import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;

import org.mozkito.core.libs.versions.DepotGraph;
import org.mozkito.core.libs.versions.adapters.GraphAdapter;
import org.mozkito.core.libs.versions.model.Branch;
import org.mozkito.core.libs.versions.model.ChangeSet;
import org.mozkito.core.libs.versions.model.Depot;
import org.mozkito.skeleton.commons.URIUtils;
import org.mozkito.skeleton.exec.Command;
import org.mozkito.skeleton.logging.Logger;
import org.mozkito.skeleton.sequel.SequelDatabase;

// TODO: Auto-generated Javadoc
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
		INTEGRATION;
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
	private final File           workDir;
	
	/** The clone dir. */
	private final File           cloneDir;
	
	/** The database. */
	private final SequelDatabase database;
	
	/** The graph. */
	private final DepotGraph     graph;
	
	/** The uri. */
	private final URI            uri;
	
	/** The tasks. */
	private final Task[]         tasks;
	
	/** The depot. */
	private final Depot          depot;
	
	/** The clone name. */
	private final String         cloneName;
	
	/**
	 * Instantiates a new task runner.
	 *
	 * @param baseDir
	 *            the base dir
	 * @param workDir
	 *            the work dir
	 * @param depotURI
	 *            the depot uri
	 * @param database
	 *            the database
	 * @param tasks
	 *            the tasks
	 */
	public TaskRunner(final File baseDir, final File workDir, final URI depotURI, final SequelDatabase database,
	        final Task[] tasks) {
		Thread.setDefaultUncaughtExceptionHandler(new MozkitoHandler());
		
		this.workDir = workDir;
		this.uri = depotURI;
		this.database = database;
		this.tasks = tasks;
		final URI baseURI = baseDir.toURI();
		if (depotURI.equals(baseURI)) {
			this.cloneName = sanitize(baseDir.getName());
		} else {
			
			this.cloneName = sanitize(baseURI.relativize(depotURI).getPath());
		}
		this.cloneDir = new File(workDir, this.cloneName);
		this.depot = new Depot(this.cloneName, depotURI, Instant.now());
		this.graph = new DepotGraph(this.depot);
		Thread.currentThread().setName("Miner:" + this.depot.getName());
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		this.database.getAdapter(Depot.class).save(this.depot);
		
		Logger.info("Cloning depot '%s'.", this.cloneName);
		final Command command = Command.execute("git",
		                                        new String[] { "clone", "-b", "master", "-n", "-q",
		                                                URIUtils.uri2String(this.uri), this.cloneDir.getAbsolutePath() },
		                                        this.workDir);
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
		
		try {
			Map<String, Branch> branchHeads;
			if (ArrayUtils.contains(this.tasks, Task.BRANCHES)) {
				Logger.info("Spawning BranchMiner.");
				
				final BranchMiner branchMiner = new BranchMiner(this.cloneDir, this.database, this.depot);
				final Thread bmThread = new Thread(branchMiner, "BranchMiner:" + this.depot.getName());
				bmThread.start();
				bmThread.join();
				branchHeads = branchMiner.getBranchHeads();
			} else {
				// TODO load from DB
				branchHeads = new HashMap<String, Branch>();
			}
			
			final GraphAdapter graphAdapter = new GraphAdapter(this.database);
			Map<String, ChangeSet> changeSets = new HashMap<String, ChangeSet>();
			
			if (ArrayUtils.contains(this.tasks, Task.CHANGESETS)) {
				Logger.info("Spawning ChangeSetMiner.");
				final ChangeSetMiner changeSetMiner = new ChangeSetMiner(this.cloneDir, this.database, this.depot,
				                                                         this.graph, branchHeads);
				final Thread csmThread = new Thread(changeSetMiner, "ChangeSetMiner:" + this.depot.getName());
				csmThread.start();
				csmThread.join();
				changeSets = changeSetMiner.getChangeSets();
			} else {
				// TODO load changesets and add to graph and the hashmap
			}
			
			if (ArrayUtils.contains(this.tasks, Task.GRAPH)) {
				Logger.info("Spawning GraphBuilder.");
				final GraphMiner graphMiner = new GraphMiner(this.cloneDir, this.database, this.graph, changeSets);
				final Thread gmThread = new Thread(graphMiner, "GraphMiner:" + this.depot.getName());
				gmThread.start();
				gmThread.join();
				graphAdapter.save(this.graph);
				this.database.commit();
			}
			
			if (ArrayUtils.contains(this.tasks, Task.INTEGRATION)) {
				Logger.info("Spawning IntegrationBuilder.");
				
				final IntegrationMiner integrationMiner = new IntegrationMiner(this.graph);
				final Thread imThread = new Thread(integrationMiner, "IntegrationMiner:" + this.depot.getName());
				imThread.start();
				imThread.join();
				
				// final ChangeSet invest = changeSets.get("d14d00ee51abf5f939651070e8eef4b3d9873aa0");
				//
				// final Branch monitored = this.graph.getBranches().stream().filter(b -> "master".equals(b.getName()))
				// .findFirst().get();
				// final Iterator<ChangeSet> monitoredChangeSets = this.graph.getChangeSets(monitored);
				//
				// while (monitoredChangeSets.hasNext()) {
				// Logger.info(monitoredChangeSets.next().getCommitHash());
				// }
				//
				// final List<ChangeSet> path = this.graph.getIntegrationPath(invest, monitored);
				// for (final ChangeSet changeSet : path) {
				// Logger.info(changeSet.toString());
				// }
			}
		} catch (final InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
	
}
