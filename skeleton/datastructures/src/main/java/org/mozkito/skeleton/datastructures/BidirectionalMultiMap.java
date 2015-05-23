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

package org.mozkito.skeleton.datastructures;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * The Class ReMapSet.
 *
 * @author Sascha Just
 * @param <K>
 *            the key type
 * @param <V>
 *            the value type
 */
public class BidirectionalMultiMap<K, V> {
	
	/** The from map. */
	private final Map<K, Set<V>> fromMap = new HashMap<>();
	
	/** The to map. */
	private final Map<V, Set<K>> toMap   = new HashMap<>();
	
	/** The k class. */
	@SuppressWarnings ("rawtypes")
	private Class<? extends Set> kClass;
	
	/** The v class. */
	@SuppressWarnings ("rawtypes")
	private Class<? extends Set> vClass;
	
	/**
	 * Instantiates a new bidirectional multi map.
	 */
	public BidirectionalMultiMap() {
		this(HashSet.class);
	}
	
	/**
	 * Instantiates a new re map set.
	 * 
	 * @param <X>
	 *            the generic type
	 * @param setClass
	 *            the set class
	 */
	@SuppressWarnings ("rawtypes")
	public <X extends Set> BidirectionalMultiMap(final Class<X> setClass) {
		this(setClass, setClass);
	}
	
	/**
	 * Instantiates a new re map set.
	 * 
	 * @param <X>
	 *            the generic type
	 * @param <Y>
	 *            the generic type
	 * @param kClass
	 *            the k class
	 * @param vClass
	 *            the v class
	 */
	@SuppressWarnings ("rawtypes")
	public <X extends Set, Y extends Set> BidirectionalMultiMap(final Class<X> kClass, final Class<Y> vClass) {
		
		this.kClass = kClass;
		this.vClass = vClass;
		
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#clear()
	 */
	/**
	 * Clear.
	 */
	public void clear() {
		
		this.fromMap.clear();
		this.toMap.clear();
		
	}
	
	/**
	 * Contains key.
	 * 
	 * @param key
	 *            the key
	 * @return true, if successful
	 */
	public boolean containsFrom(final K key) {
		
		return this.fromMap.containsKey(key);
		
	}
	
	/**
	 * Contains value.
	 * 
	 * @param value
	 *            the value
	 * @return true, if successful
	 */
	public boolean containsTo(final V value) {
		
		return this.toMap.containsKey(value);
		
	}
	
	/**
	 * From entry set.
	 * 
	 * @return the sets the
	 */
	public Set<java.util.Map.Entry<K, Set<V>>> fromEntrySet() {
		
		return this.fromMap.entrySet();
		
	}
	
	/**
	 * From key set.
	 * 
	 * @return the sets the
	 */
	public Set<K> fromKeySet() {
		
		return this.fromMap.keySet();
		
	}
	
	/**
	 * From size.
	 * 
	 * @return the int
	 */
	public int fromSize() {
		
		return this.fromMap.size();
		
	}
	
	/**
	 * From values.
	 * 
	 * @return the collection
	 */
	public Collection<Set<V>> fromValues() {
		
		return this.fromMap.values();
		
	}
	
	/**
	 * Gets the simple name of the class.
	 * 
	 * @return the simple name of the class.
	 */
	public final String getClassName() {
		return getClass().getSimpleName();
	}
	
	/**
	 * Gets the froms.
	 * 
	 * @param value
	 *            the value
	 * @return the froms
	 */
	public Set<K> getFroms(final V value) {
		
		return this.toMap.get(value);
		
	}
	
	/**
	 * Gets the tos.
	 * 
	 * @param key
	 *            the key
	 * @return the tos
	 */
	public Set<V> getTos(final K key) {
		
		return this.fromMap.get(key);
		
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#isEmpty()
	 */
	/**
	 * Checks if is empty.
	 * 
	 * @return true, if is empty
	 */
	public boolean isEmpty() {
		
		return this.fromMap.isEmpty();
		
	}
	
	/**
	 * Put.
	 * 
	 * @param key
	 *            the key
	 * @param value
	 *            the value
	 * @return true, if successful
	 */
	@SuppressWarnings ("unchecked")
	public boolean put(final K key,
	                   final V value) {
		try {
			
			if (!this.fromMap.containsKey(key)) {
				this.fromMap.put(key, this.vClass.newInstance());
				
			}
			
			final boolean add = this.fromMap.get(key).add(value);
			if (!add) {
				return false;
			}
			
			if (!this.toMap.containsKey(value)) {
				this.toMap.put(value, this.kClass.newInstance());
			}
			
			return this.toMap.get(value).add(key);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
		
	}
	
	/**
	 * Put all.
	 * 
	 * @param m
	 *            the m
	 */
	@SuppressWarnings ("unchecked")
	public void putAll(final BidirectionalMultiMap<K, V> m) {
		try {
			for (final Entry<K, Set<V>> entry : m.fromEntrySet()) {
				if (this.fromMap.containsKey(entry.getKey())) {
					this.fromMap.put(entry.getKey(), this.kClass.newInstance());
				}
				
				this.fromMap.get(entry.getKey()).addAll(entry.getValue());
			}
			
			for (final Entry<V, Set<K>> entry : m.toEntrySet()) {
				if (this.toMap.containsKey(entry.getKey())) {
					this.toMap.put(entry.getKey(), this.vClass.newInstance());
				}
				
				this.toMap.get(entry.getKey()).addAll(entry.getValue());
			}
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
		
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	/**
	 * Removes the.
	 * 
	 * @param key
	 *            the key
	 * @return the v
	 */
	public Set<V> removeFrom(final K key) {
		
		final Set<V> set = this.fromMap.get(key);
		for (final V value : set) {
			final Set<K> set2 = this.toMap.get(value);
			if (set2.size() == 1) {
				this.toMap.remove(value);
			} else {
				set2.remove(key);
			}
		}
		
		this.fromMap.remove(key);
		
		return set;
		
	}
	
	/**
	 * Removes the to.
	 * 
	 * @param value
	 *            the value
	 * @return the sets the
	 */
	public Set<K> removeTo(final V value) {
		
		final Set<K> set = this.toMap.get(value);
		for (final K key : set) {
			final Set<V> set2 = this.fromMap.get(key);
			if (set2.size() == 1) {
				this.fromMap.remove(key);
			} else {
				set2.remove(value);
			}
		}
		
		this.toMap.remove(value);
		
		return set;
		
	}
	
	/**
	 * To entry set.
	 * 
	 * @return the sets the
	 */
	public Set<java.util.Map.Entry<V, Set<K>>> toEntrySet() {
		
		return this.toMap.entrySet();
		
	}
	
	/**
	 * To key set.
	 * 
	 * @return the sets the
	 */
	public Set<V> toKeySet() {
		
		return this.toMap.keySet();
		
	}
	
	/**
	 * To size.
	 * 
	 * @return the int
	 */
	public int toSize() {
		
		return this.toMap.size();
		
	}
	
	/**
	 * To values.
	 * 
	 * @return the collection
	 */
	public Collection<Set<K>> toValues() {
		
		return this.toMap.values();
		
	}
}
