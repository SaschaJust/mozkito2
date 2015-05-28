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

package org.mozkito.skeleton.sequel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.List;

/**
 * The Interface ISequelAdapter.
 *
 * @author Sascha Just
 * @param <T>
 *            the generic type
 */
public interface ISequelAdapter<T> {
	
	/**
	 * Creates the.
	 *
	 * @param result
	 *            the result
	 * @return the t
	 */
	T create(ResultSet result);
	
	/**
	 * Creates the constraints.
	 */
	void createConstraints();
	
	/**
	 * Creates the indexes.
	 */
	void createIndexes();
	
	/**
	 * Creates the scheme.
	 */
	void createScheme();
	
	/**
	 * Delete.
	 *
	 * @param object
	 *            the object
	 */
	void delete(T object);
	
	/**
	 * Load.
	 *
	 * @return the iterator
	 */
	Iterator<T> load();
	
	/**
	 * Load.
	 *
	 * @param ids
	 *            the ids
	 * @return the t
	 */
	List<T> load(long... ids);
	
	/**
	 * Load.
	 *
	 * @param id
	 *            the id
	 * @return the t
	 */
	T load(long id);
	
	/**
	 * Next id.
	 *
	 * @param nextIdStatement
	 *            the next id statement
	 * @return the object
	 */
	long nextId(PreparedStatement nextIdStatement);
	
	/**
	 * Prepare next id statement.
	 *
	 * @return the prepared statement
	 */
	PreparedStatement prepareNextIdStatement();
	
	/**
	 * Prepare save statement.
	 *
	 * @return the prepared statement
	 */
	PreparedStatement prepareSaveStatement();
	
	/**
	 * Save.
	 *
	 * @param saveStatement
	 *            the save statement
	 * @param id
	 *            the id
	 * @param entity
	 *            the entity
	 */
	void save(PreparedStatement saveStatement,
	          long id,
	          T entity);
	
	/**
	 * Save.
	 *
	 * @param saveStatement
	 *            the save statement
	 * @param idStatement
	 *            the id statement
	 * @param entity
	 *            the entity
	 */
	void save(PreparedStatement saveStatement,
	          PreparedStatement idStatement,
	          T entity);
	
	/**
	 * Save.
	 *
	 * @param objects
	 *            the objects
	 */
	void save(@SuppressWarnings ("unchecked") T... objects);
	
	/**
	 * Update.
	 *
	 * @param objects
	 *            the objects
	 */
	void update(@SuppressWarnings ("unchecked") T... objects);
	
}
