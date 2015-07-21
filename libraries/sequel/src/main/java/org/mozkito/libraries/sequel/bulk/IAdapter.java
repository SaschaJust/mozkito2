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

package org.mozkito.libraries.sequel.bulk;

import java.sql.Connection;

import org.mozkito.libraries.sequel.IEntity;

/**
 * The Interface IAdapter.
 *
 * @author Sascha Just
 * @param <T>
 *            the generic type
 */
public interface IAdapter<T extends IEntity> {
	
	/**
	 * Close.
	 */
	public abstract void close();
	
	/**
	 * Creates the constraints.
	 *
	 * @param connection
	 *            the connection
	 */
	public abstract void createConstraints(Connection connection);
	
	/**
	 * Creates the foreign keys.
	 *
	 * @param connection
	 *            the connection
	 */
	public abstract void createForeignKeys(Connection connection);
	
	/**
	 * Creates the indexes.
	 *
	 * @param connection
	 *            the connection
	 */
	public abstract void createIndexes(Connection connection);
	
	/**
	 * Creates the primary keys.
	 *
	 * @param connection
	 *            the connection
	 */
	public abstract void createPrimaryKeys(Connection connection);
	
	/**
	 * Creates the scheme.
	 *
	 * @param connection
	 *            the connection
	 */
	public abstract void createScheme(Connection connection);
	
	/**
	 * Flush.
	 */
	public abstract void flush();
	
	/**
	 * Inits the.
	 *
	 * @param connection
	 *            the connection
	 */
	public void init(final Connection connection);
	
	/**
	 * Next id.
	 *
	 * @return the long
	 */
	public abstract long nextId();
	
	/**
	 * Save.
	 *
	 * @param entity
	 *            the entity
	 */
	public abstract void save(T entity);
	
}
