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

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
	
	/**
	 * The Class Entry.
	 *
	 * @param <K>
	 *            the key type
	 * @param <V>
	 *            the value type
	 */
	public static class Entry<K, V> implements Map.Entry<K, V> {
		
		private final K key;
		private final V value;
		
		/**
		 * Instantiates a new entry.
		 *
		 * @param key
		 *            the key
		 * @param value
		 *            the value
		 */
		public Entry(final K key, final V value) {
			this.key = key;
			this.value = value;
		}
		
		/**
		 * {@inheritDoc}
		 * 
		 * @see java.util.Map.Entry#getKey()
		 */
		public K getKey() {
			return this.key;
		}
		
		/**
		 * {@inheritDoc}
		 * 
		 * @see java.util.Map.Entry#getValue()
		 */
		public V getValue() {
			return this.value;
		}
		
		/**
		 * {@inheritDoc}
		 * 
		 * @see java.util.Map.Entry#setValue(java.lang.Object)
		 */
		public V setValue(final V value) {
			throw new UnsupportedOperationException();
		}
		
	}
	
	/** The from map. */
	private final Map<K, Set<V>> fromMap = new HashMap<>();
	
	/** The to map. */
	private final Map<V, Set<K>> toMap   = new HashMap<>();
	
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
	public boolean containsKey(final K key) {
		
		return this.fromMap.containsKey(key);
		
	}
	
	/**
	 * Contains value.
	 * 
	 * @param value
	 *            the value
	 * @return true, if successful
	 */
	public boolean containsValue(final V value) {
		
		return this.toMap.containsKey(value);
		
	}
	
	/**
	 * Entry set.
	 *
	 * @return the sets the
	 */
	public Set<Entry<K, V>> entrySet() {
		final HashSet<Entry<K, V>> set = new HashSet<>();
		for (final Map.Entry<K, Set<V>> entry : this.fromMap.entrySet()) {
			for (final V value : entry.getValue()) {
				set.add(new Entry<>(entry.getKey(), value));
			}
		}
		
		return set;
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
	public Set<K> getKey(final V value) {
		
		return this.toMap.get(value);
		
	}
	
	/**
	 * Gets the tos.
	 * 
	 * @param key
	 *            the key
	 * @return the tos
	 */
	public Set<V> getValue(final K key) {
		
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
	 * From entry set.
	 * 
	 * @return the sets the
	 */
	public Set<java.util.Map.Entry<K, Set<V>>> keyEntrySet() {
		
		return this.fromMap.entrySet();
		
	}
	
	/**
	 * From key set.
	 * 
	 * @return the sets the
	 */
	public Set<K> keySet() {
		
		return this.fromMap.keySet();
		
	}
	
	/**
	 * From size.
	 * 
	 * @return the int
	 */
	public int keySize() {
		
		return this.fromMap.size();
		
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
	public boolean put(final K key,
	                   final V value) {
		try {
			
			if (!this.fromMap.containsKey(key)) {
				this.fromMap.put(key, new HashSet<>());
				
			}
			
			final boolean add = this.fromMap.get(key).add(value);
			if (!add) {
				return false;
			}
			
			if (!this.toMap.containsKey(value)) {
				this.toMap.put(value, new HashSet<>());
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
	public void putAll(final BidirectionalMultiMap<K, V> m) {
		try {
			for (final Map.Entry<K, Set<V>> entry : m.keyEntrySet()) {
				if (!this.fromMap.containsKey(entry.getKey())) {
					this.fromMap.put(entry.getKey(), new HashSet<>());
				}
				
				this.fromMap.get(entry.getKey()).addAll(entry.getValue());
			}
			
			for (final Map.Entry<V, Set<K>> entry : m.valueEntrySet()) {
				if (!this.toMap.containsKey(entry.getKey())) {
					this.toMap.put(entry.getKey(), new HashSet<>());
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
	public Set<V> removeKey(final K key) {
		
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
	public Set<K> removeValue(final V value) {
		
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
	 * To key set.
	 * 
	 * @return the sets the
	 */
	public Set<V> toKeySet() {
		
		return this.toMap.keySet();
		
	}
	
	/**
	 * To values.
	 * 
	 * @return the collection
	 */
	public Collection<Set<K>> toValues() {
		
		return this.toMap.values();
		
	}
	
	/**
	 * To entry set.
	 * 
	 * @return the sets the
	 */
	public Set<java.util.Map.Entry<V, Set<K>>> valueEntrySet() {
		
		return this.toMap.entrySet();
		
	}
	
	/**
	 * Value set.
	 *
	 * @return the sets the
	 */
	public Set<V> valueSet() {
		return this.toMap.keySet();
	}
	
	/**
	 * To size.
	 * 
	 * @return the int
	 */
	public int valueSize() {
		
		return this.toMap.size();
		
	}
}
