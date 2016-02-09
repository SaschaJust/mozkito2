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

package org.mozkito.skeleton.datastructures;

/**
 * The Class Tuple.
 *
 * @author Sascha Just
 * @param <K>
 *            the key type
 * @param <M>
 *            the generic type
 */
public class Tuple<K, M> {
	
	/** The first. */
	private K first;
	
	/** The second. */
	private M second;
	
	/**
	 * Instantiates a new tuple.
	 *
	 * @param f the f
	 * @param s the s
	 */
	public Tuple(K f, M s) {
		this.first = f;
		this.second = s;
	}
	
	/**
	 * Gets the first.
	 *
	 * @return the first
	 */
	public K getFirst() {
		return this.first;
	}
	
	/**
	 * Gets the class name.
	 *
	 * @return the simple class name
	 */
	public String getClassName() {
		return this.getClass().getSimpleName();
	}
	
	/**
	 * Gets the second.
	 *
	 * @return the second
	 */
	public M getSecond() {
		return this.second;
	}
	
	/**
	 * Sets the first.
	 *
	 * @param first the first to set
	 */
	public void setFirst(K first) {
		this.first = first;
	}
	
	/**
	 * Sets the second.
	 *
	 * @param second the second to set
	 */
	public void setSecond(M second) {
		this.second = second;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	/**
	 * {@inheritDoc}
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Tuple [first=" + this.first + ", second=" + this.second + "]";
	}
}
