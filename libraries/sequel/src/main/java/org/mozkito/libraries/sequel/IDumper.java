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

package org.mozkito.libraries.sequel;

/**
 * The Interface IDumper.
 *
 * @author Sascha Just
 * @param <T>
 *            the generic type
 */
public interface IDumper<T extends IEntity> extends Runnable {
	
	/**
	 * Join.
	 *
	 * @throws InterruptedException
	 *             the interrupted exception
	 */
	public void join() throws InterruptedException;
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Thread#run()
	 */
	public abstract void run();
	
	/**
	 * Save later.
	 *
	 * @param entity
	 *            the entity
	 */
	public abstract void saveLater(T entity);
	
	/**
	 * Start.
	 */
	public void start();
	
	/**
	 * Terminate.
	 */
	public abstract void terminate();
	
}
