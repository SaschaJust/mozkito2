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
import java.io.IOException;
import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;

import org.mozkito.core.libs.versions.FileCache;
import org.mozkito.core.libs.versions.IdentityCache;
import org.mozkito.core.libs.versions.graph.Graph;
import org.mozkito.core.libs.versions.graph.Vertex;
import org.mozkito.core.libs.versions.model.BranchEdge;
import org.mozkito.core.libs.versions.model.ChangeSet;
import org.mozkito.core.libs.versions.model.ChangeSetIntegration;
import org.mozkito.core.libs.versions.model.ConvergenceEdge;
import org.mozkito.core.libs.versions.model.Depot;
import org.mozkito.core.libs.versions.model.GraphEdge;
import org.mozkito.core.libs.versions.model.Handle;
import org.mozkito.core.libs.versions.model.Head;
import org.mozkito.core.libs.versions.model.Identity;
import org.mozkito.core.libs.versions.model.Reference;
import org.mozkito.core.libs.versions.model.Renaming;
import org.mozkito.core.libs.versions.model.Revision;
import org.mozkito.core.libs.versions.model.Root;
import org.mozkito.core.libs.versions.model.SignOff;
import org.mozkito.core.libs.versions.model.Tag;
import org.mozkito.libraries.logging.Logger;
import org.mozkito.libraries.sequel.IDumper;
import org.mozkito.libraries.sequel.MozkitoHandler;
import org.mozkito.skeleton.commons.URIUtils;
import org.mozkito.skeleton.datastructures.BidirectionalMultiMap;
import org.mozkito.skeleton.exec.Command;

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
		// don't use File.separator here. This comes from a URI. Hence we definitely got forward slashes.
		return fileName.replace("/", "_").replace('-', '_').replace(".git", "").trim().toLowerCase()
		               .replaceAll("[^0-9a-z_]", "").replaceAll("(^_*|_*$)", "");
	}
	
	/** The work dir. */
	private final File                          workDir;
	
	/** The clone dir. */
	private final File                          cloneDir;
	
	/** The graph. */
	private final Graph                         graph;
	
	/** The uri. */
	private final URI                           uri;
	
	/** The tasks. */
	private final Task[]                        tasks;
	
	/** The depot. */
	private final Depot                         depot;
	
	/** The clone name. */
	private final String                        cloneName;
	
	/** The change set dumper. */
	private final IDumper<ChangeSet>            changeSetDumper;
	
	/** The identity dumper. */
	private final IDumper<Identity>             identityDumper;
	
	/** The revision dumper. */
	private final IDumper<Revision>             revisionDumper;
	
	/** The branch dumper. */
	private final IDumper<Reference>            branchDumper;
	
	/** The handle dumper. */
	private final IDumper<Handle>               handleDumper;
	
	/** The graph dumper. */
	private final IDumper<Graph>                graphDumper;
	
	/** The depot dumper. */
	private final IDumper<Depot>                depotDumper;
	
	private final IDumper<Renaming>             renamingDumper;
	
	private final IdentityCache                 identityCache;
	
	private final FileCache                     fileCache;
	
	private final IDumper<ChangeSetIntegration> integrationDumper;
	
	private final IDumper<Tag>                  tagDumper;
	
	private final IDumper<GraphEdge>            graphEdgeDumper;
	
	private final IDumper<BranchEdge>           branchEdgeDumper;
	
	private final IDumper<ConvergenceEdge>      convergenceDumper;
	
	private final IDumper<Head>                 headDumper;
	
	private final IDumper<Root>                 rootDumper;
	
	private final IDumper<SignOff>              signedOffDumper;
	
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
	 * @param identityCache
	 *            the identity cache
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
	 * @param renamingDumper
	 *            the renaming dumper
	 * @param integrationDumper
	 *            the integration dumper
	 * @param tagDumper
	 *            the tag dumper
	 * @param graphEdgeDumper
	 *            the graph edge dumper
	 * @param branchEdgeDumper
	 *            the branch edge dumper
	 * @param headDumper
	 *            the head dumper
	 * @param rootDumper
	 *            the root dumper
	 * @param convergenceDumper
	 *            the convergence dumper
	 * @param signedOffDumper
	 *            the signed off dumper
	 */
	public TaskRunner(final File baseDir, final File workDir, final URI depotURI, final Task[] tasks,
	        final IdentityCache identityCache, final IDumper<Identity> identityDumper,
	        final IDumper<ChangeSet> changeSetDumper, final IDumper<Revision> revisionDumper,
	        final IDumper<Reference> branchDumper, final IDumper<Handle> handleDumper,
	        final IDumper<Graph> graphDumper, final IDumper<Depot> depotDumper, final IDumper<Renaming> renamingDumper,
	        final IDumper<ChangeSetIntegration> integrationDumper, final IDumper<Tag> tagDumper,
	        final IDumper<GraphEdge> graphEdgeDumper, final IDumper<BranchEdge> branchEdgeDumper,
	        final IDumper<Head> headDumper, final IDumper<Root> rootDumper,
	        final IDumper<ConvergenceEdge> convergenceDumper, final IDumper<SignOff> signedOffDumper) {
		Thread.setDefaultUncaughtExceptionHandler(new MozkitoHandler());
		
		this.identityDumper = identityDumper;
		this.changeSetDumper = changeSetDumper;
		this.revisionDumper = revisionDumper;
		this.branchDumper = branchDumper;
		this.handleDumper = handleDumper;
		this.graphDumper = graphDumper;
		this.depotDumper = depotDumper;
		this.renamingDumper = renamingDumper;
		this.integrationDumper = integrationDumper;
		this.tagDumper = tagDumper;
		this.graphEdgeDumper = graphEdgeDumper;
		this.branchEdgeDumper = branchEdgeDumper;
		this.convergenceDumper = convergenceDumper;
		this.headDumper = headDumper;
		this.rootDumper = rootDumper;
		this.signedOffDumper = signedOffDumper;
		
		this.identityCache = identityCache;
		
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
		this.fileCache = new FileCache(this.depot, handleDumper);
	}
	
	/**
	 * Reset name.
	 */
	private void resetName() {
		Thread.currentThread().setName(getClass().getSimpleName() + ":" + this.depot.getName());
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		resetName();
		
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
		
		Map<String, Vertex> vertices = new HashMap<>();
		
		if (ArrayUtils.contains(this.tasks, Task.CHANGESETS)) {
			Logger.info("Spawning ChangeSetMiner.");
			final ChangeSetMiner changeSetMiner = new ChangeSetMiner(this.cloneDir, this.depot, this.graph,
			                                                         this.identityCache, this.fileCache,
			                                                         this.identityDumper, this.changeSetDumper,
			                                                         this.revisionDumper, this.handleDumper,
			                                                         this.renamingDumper, this.signedOffDumper);
			changeSetMiner.run();
			resetName();
			vertices = changeSetMiner.getVertexes();
			this.graph.addVertices(vertices);
		} else {
			// TODO load changesets and add to graph and the hashmap
		}
		
		System.gc();
		
		final BidirectionalMultiMap<String, Reference> references = new BidirectionalMultiMap<>();
		if (ArrayUtils.contains(this.tasks, Task.BRANCHES)) {
			Logger.info("Spawning BranchMiner.");
			
			final BidirectionalMultiMap<String, Reference> branchRefs;
			final BranchMiner branchMiner = new BranchMiner(this.cloneDir, this.depot, vertices, this.branchDumper);
			branchMiner.run();
			resetName();
			branchRefs = branchMiner.getBranchRefs();
			this.graph.addRefs(branchRefs);
			references.putAll(branchRefs);
		} else {
			// TODO load from DB
		}
		
		{
			Logger.info("Spawning TagMiner");
			final BidirectionalMultiMap<String, Reference> tagRefs;
			final TagMiner tagMiner = new TagMiner(this.cloneDir, this.depot, vertices, this.identityCache,
			                                       this.tagDumper, this.branchDumper, this.identityDumper);
			tagMiner.run();
			resetName();
			tagRefs = tagMiner.getTagRefs();
			this.graph.addRefs(tagRefs);
			references.putAll(tagRefs);
		}
		
		if (ArrayUtils.contains(this.tasks, Task.ENDPOINTS)) {
			Logger.info("Spawning EndPointMiner.");
			final EndPointMiner endPointMiner = new EndPointMiner(this.depot, this.cloneDir, references, vertices,
			                                                      this.graph, this.headDumper, this.rootDumper,
			                                                      this.integrationDumper);
			endPointMiner.run();
			resetName();
		}
		
		System.gc();
		
		if (ArrayUtils.contains(this.tasks, Task.GRAPH)) {
			Logger.info("Spawning GraphBuilder.");
			final GraphMiner graphMiner = new GraphMiner(this.depot, this.cloneDir, this.graph, vertices,
			                                             this.graphDumper, this.graphEdgeDumper, this.integrationDumper);
			graphMiner.run();
			resetName();
		}
		
		if (ArrayUtils.contains(this.tasks, Task.INTEGRATION)) {
			Logger.info("Spawning IntegrationMiner.");
			final IntegrationMiner integrationMiner = new IntegrationMiner(this.depot, this.graph,
			                                                               this.branchEdgeDumper);
			integrationMiner.run();
			resetName();
			
			Logger.info("Spawning ConvergenceMiner.");
			
			final ConvergenceMiner convergenceMiner = new ConvergenceMiner(this.depot, this.graph,
			                                                               this.convergenceDumper);
			convergenceMiner.run();
			resetName();
		}
		
		try {
			FileUtils.deleteDirectory(this.cloneDir);
		} catch (final IOException e) {
			// ignore
		}
	}
	
}
