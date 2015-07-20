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
