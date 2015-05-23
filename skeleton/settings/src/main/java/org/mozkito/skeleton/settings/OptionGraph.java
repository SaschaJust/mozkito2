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

package org.mozkito.skeleton.settings;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DirectedMultigraph;

import org.mozkito.skeleton.settings.options.internal.CompositeOption;
import org.mozkito.skeleton.settings.options.internal.Option;
import org.mozkito.skeleton.settings.options.internal.SingleOption;

// TODO: Auto-generated Javadoc
/**
 * The Class OptionGraph.
 *
 * @author Sascha Just
 */
public class OptionGraph {
	
	/**
	 * The Class Conflict.
	 */
	public static class Conflict extends Edge {
		
	}
	
	/**
	 * The Class Dependency.
	 */
	public static class Dependency extends Edge {
		
	}
	
	/**
	 * The Class Edge.
	 */
	public static class Edge {
		
	}
	
	/**
	 * The Class Requirement.
	 */
	public static class Requirement extends Edge {
		
	}
	
	/** The root. */
	private final CompositeOption<?>                         root  = new CompositeOption("root",
	                                                                       "mozkito settings base", Object.class) {
		                                                               
		                                                               @Override
		                                                               public Object instantiate() {
			                                                               throw new RuntimeException(
			                                                                                          "Method 'instantiate' has not yet been implemented."); //$NON-NLS-1$
			                                                               
		                                                               }
	                                                               };
	
	/** The graph. */
	private final DirectedGraph<Option<?>, OptionGraph.Edge> graph = new DirectedMultigraph<Option<?>, OptionGraph.Edge>(
	                                                                                                                     Edge.class);
	
	/**
	 * Adds the composite.
	 *
	 * @param option
	 *            the option
	 */
	public void add(final CompositeOption<?> option) {
		
	}
	
	public void add(final SingleOption<?> option) {
		
	}
}
