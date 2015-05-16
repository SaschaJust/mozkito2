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
import java.sql.SQLException;
import java.util.Iterator;

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
	 * @throws SQLException
	 *             the SQL exception
	 */
	T create(ResultSet result) throws SQLException;

	/**
	 * Creates the scheme.
	 *
	 * @throws SQLException
	 *             the SQL exception
	 */
	void createScheme() throws SQLException;

	/**
	 * Delete.
	 *
	 * @param object
	 *            the object
	 * @throws SQLException
	 *             the SQL exception
	 */
	void delete(T object) throws SQLException;

	/**
	 * Load.
	 *
	 * @return the iterator
	 * @throws SQLException
	 *             the SQL exception
	 */
	Iterator<T> load() throws SQLException;

	/**
	 * Load.
	 *
	 * @param id
	 *            the id
	 * @param ids
	 *            the ids
	 * @return the t
	 * @throws SQLException
	 *             the SQL exception
	 */
	T load(Object id,
	       Object... ids) throws SQLException;

	/**
	 * Save.
	 *
	 * @param object
	 *            the object
	 * @throws SQLException
	 *             the SQL exception
	 */
	void save(T object) throws SQLException;

	/**
	 * Update.
	 *
	 * @param object
	 *            the object
	 * @throws SQLException
	 *             the SQL exception
	 */
	void update(T object) throws SQLException;

}
