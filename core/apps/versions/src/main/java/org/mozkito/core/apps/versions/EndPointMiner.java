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
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections4.MultiMap;

import org.mozkito.core.libs.versions.graph.Graph;
import org.mozkito.core.libs.versions.graph.Vertex;
import org.mozkito.core.libs.versions.model.Depot;
import org.mozkito.core.libs.versions.model.Head;
import org.mozkito.core.libs.versions.model.Reference;
import org.mozkito.core.libs.versions.model.Root;
import org.mozkito.core.libs.versions.model.enums.ReferenceType;
import org.mozkito.libraries.sequel.DatabaseDumper;
import org.mozkito.skeleton.contracts.Asserts;
import org.mozkito.skeleton.exec.Command;

/**
 * The Class EndPointMiner.
 *
 * @author Sascha Just
 */
public class EndPointMiner extends Task implements Runnable {
	
	/** The clone dir. */
	private final File                        cloneDir;
	
	/** The branch heads. */
	private final MultiMap<String, Reference> refs;
	
	/** The change sets. */
	private final Map<String, Vertex>         vertices;
	
	/** The graph. */
	private final Graph                       graph;
	
	/** The head dumper. */
	private final DatabaseDumper<Head>        headDumper;
	
	/** The root dumper. */
	private final DatabaseDumper<Root>        rootDumper;
	
	/**
	 * Instantiates a new end point miner.
	 *
	 * @param depot
	 *            the depot
	 * @param cloneDir
	 *            the clone dir
	 * @param heads
	 *            the heads
	 * @param vertices
	 *            the vertices
	 * @param graph
	 *            the graph
	 * @param headDumper
	 *            the head dumper
	 * @param rootDumper
	 *            the root dumper
	 */
	public EndPointMiner(final Depot depot, final File cloneDir, final MultiMap<String, Reference> heads,
	        final Map<String, Vertex> vertices, final Graph graph, final DatabaseDumper<Head> headDumper,
	        final DatabaseDumper<Root> rootDumper) {
		super(depot);
		this.cloneDir = cloneDir;
		this.refs = heads;
		this.vertices = vertices;
		this.graph = graph;
		this.headDumper = headDumper;
		this.rootDumper = rootDumper;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@SuppressWarnings ("unchecked")
	public void run() {
		for (final Entry<String, Object> entry : this.refs.entrySet()) {
			for (final Reference ref : (Collection<Reference>) entry.getValue()) {
				final Command command = Command.execute("git", new String[] { "log", "--max-parents=0", "--format=%H",
				        ReferenceType.BRANCH.equals(ref.getType())
				                                                  ? IntegrationMiner.ORIGIN + ref.getName()
				                                                  : TagMiner.PREFIX + ref.getName() }, this.cloneDir);
				String root;
				
				Asserts.containsKey(this.vertices, entry.getKey());
				this.headDumper.saveLater(this.graph.addHead(ref, this.vertices.get(entry.getKey())));
				
				while ((root = command.nextOutput()) != null) {
					Asserts.containsKey(this.vertices, root);
					this.rootDumper.saveLater(this.graph.addRoot(ref, this.vertices.get(root)));
				}
			}
		}
	}
}
