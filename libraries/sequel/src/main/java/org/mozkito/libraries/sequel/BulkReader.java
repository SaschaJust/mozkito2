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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.mozkito.skeleton.contracts.Asserts;

/**
 * The Class BulkReader.
 *
 * @author Sascha Just
 * @param <T>
 *            the generic type
 */
public class BulkReader<T> {
	
	/** The connection. */
	private final Connection           connection;
	
	/** The results. */
	private final ResultSet            results;
	
	/** The method. */
	private Method                     method;
	
	/** The factory. */
	private final EntityFactory<T>     factory;
	
	/** The types. */
	private final Class<?>[]           types;
	
	/** The names. */
	private final Map<String, Integer> names = new HashMap<>();
	
	/** The named. */
	boolean                            named = true;
	
	/**
	 * Instantiates a new bulk reader.
	 *
	 * @param query
	 *            the query
	 * @param database
	 *            the database
	 * @param factory
	 *            the factory
	 * @throws SQLException
	 *             the SQL exception
	 */
	public BulkReader(final String query, final Database database, final EntityFactory<T> factory) throws SQLException {
		
		this.method = null;
		
		for (final Method m : factory.getClass().getMethods()) {
			if ("create".equals(m.getName())) {
				// if (m.getReturnType().equals(getClass().getMethod("read", new Class<?>[0]).getReturnType())) {
				if (m.getParameterTypes().length > 0) {
					this.method = m;
					break;
				}
				// }
			}
		}
		if (this.method == null) {
			throw new RuntimeException("No factory method found.");
		}
		
		this.factory = factory;
		this.types = this.method.getParameterTypes();
		Asserts.notNull(this.types);
		
		final Parameter[] parameters = this.method.getParameters();
		Asserts.notNull(parameters);
		
		Asserts.equalTo(this.types.length, parameters.length);
		
		for (int i = 0; i < parameters.length; ++i) {
			if (parameters[i].isNamePresent()) {
				this.names.put(parameters[i].getName().toLowerCase(), i);
			} else {
				this.named = false;
			}
		}
		
		this.connection = database.getConnection();
		this.results = this.connection.createStatement().executeQuery(query);
	}
	
	/**
	 * Map value.
	 *
	 * @param args
	 *            the args
	 * @param index
	 *            the index
	 * @param position
	 *            the position
	 * @throws SQLException
	 *             the SQL exception
	 */
	private void mapValue(final Object[] args,
	                      final int index,
	                      final int position) throws SQLException {
		if (this.types[position] == Short.class) {
			args[position] = this.results.getShort(index + 1);
		} else if (this.types[position] == Integer.class) {
			args[position] = this.results.getInt(index + 1);
		} else if (this.types[position] == Long.class) {
			args[position] = this.results.getLong(index + 1);
		} else if (this.types[position] == Float.class) {
			args[position] = this.results.getFloat(index + 1);
		} else if (this.types[position] == Double.class) {
			args[position] = this.results.getDouble(index + 1);
		} else if (this.types[position] == BigDecimal.class) {
			args[position] = this.results.getBigDecimal(index + 1);
		} else if (this.types[position] == Date.class) {
			args[position] = this.results.getDate(index + 1);
		} else if (this.types[position] == Time.class) {
			args[position] = this.results.getTime(index + 1);
		} else if (this.types[position] == String.class) {
			args[position] = this.results.getString(index + 1);
		} else if (this.types[position] == Boolean.class) {
			args[position] = this.results.getBoolean(index + 1);
		} else if (this.types[position] == Timestamp.class) {
			args[position] = this.results.getTimestamp(index + 1);
		} else if (this.types[position] == Instant.class) {
			args[position] = this.results.getTimestamp(index + 1) != null
			                                                             ? this.results.getTimestamp(index + 1)
			                                                                           .toInstant()
			                                                             : null;
		} else if (this.types[position].isArray() && this.types[position].getComponentType() == Byte.class) {
			args[position] = this.results.getBytes(index + 1);
		} else if (this.types[position] == Blob.class) {
			args[position] = this.results.getBlob(index + 1);
		} else if (this.types[position] == Byte.class) {
			args[position] = this.results.getByte(index + 1);
		} else if (this.types[position] == Clob.class) {
			args[position] = this.results.getClob(index + 1);
		} else if (this.types[position] == Array.class) {
			args[position] = this.results.getArray(index + 1);
		} else if (this.types[position] == URL.class) {
			args[position] = this.results.getURL(index + 1);
		} else {
			args[position] = this.results.getObject(index + 1);
		}
	}
	
	/**
	 * Read the next object.
	 *
	 * @param <T>
	 *            the generic type
	 * @return the t
	 */
	@SuppressWarnings ("unchecked")
	public <T> T read() {
		try {
			if (this.results.next()) {
				final ResultSetMetaData metaData = this.results.getMetaData();
				
				if (this.types.length != metaData.getColumnCount()) {
					throw new RuntimeException();
				}
				
				final Object[] args = new Object[this.types.length];
				for (int i = 0; i < metaData.getColumnCount(); ++i) {
					
					if (this.named) {
						mapValue(args, i, this.names.get(metaData.getColumnName(i + 1).toLowerCase()));
					} else {
						mapValue(args, i, i);
					}
				}
				
				try {
					return (T) this.method.invoke(this.factory, args);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					throw new RuntimeException(e);
				}
			} else {
				return null;
			}
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
}
