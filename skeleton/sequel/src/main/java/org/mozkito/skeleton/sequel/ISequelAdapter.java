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
	 * Gets the next id.
	 *
	 * @param type
	 *            the type
	 * @param sequenceName
	 *            the sequence name
	 * @return the next id
	 */
	public static String getNextId(final SequelDatabase.Type type,
	                               final String sequenceName) {
		switch (type) {
			case MSSQL:
				return "SELECT NEXT VALUE FOR " + sequenceName;
			case DERBY:
				return "VALUES (NEXT VALUE FOR " + sequenceName + ")";
			case POSTGRES:
				return "SELECT nextval'" + sequenceName + "'";
		}
		
		return "SELECT NEXT VALUE FOR " + sequenceName;
	}
	
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
	List<T> load(Object... ids);
	
	/**
	 * Load.
	 *
	 * @param id
	 *            the id
	 * @return the t
	 */
	T load(Object id);
	
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
