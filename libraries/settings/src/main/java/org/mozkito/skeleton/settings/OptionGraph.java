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
