/*******************************************************************************
 * Copyright 2012 Kim Herzig, Sascha Just
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR RequiresS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 ******************************************************************************/
package org.mozkito.skeleton.regex;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.mozkito.skeleton.contracts.Asserts;
import org.mozkito.skeleton.contracts.Requires;

/**
 * The Class MatchImpl.
 * 
 * @author Sascha Just 
 */
class MatchImpl implements Match {
	
	/** The map. */
	private final Map<Integer, Group>  map     = new HashMap<Integer, Group>();
	
	/** The name map. */
	private final Map<String, Integer> nameMap = new HashMap<String, Integer>();
	
	/** The full match. */
	private final Group                fullMatch;
	
	/**
	 * Instantiates a new match impl.
	 * 
	 * @param fullMatch
	 *            the full match
	 */
	MatchImpl(final Group fullMatch) {
		Requires.notNull(fullMatch);
		Requires.equalTo(fullMatch.getIndex(), 0);
		Requires.isNull(fullMatch.getName());
		
		this.fullMatch = fullMatch;
	}
	
	/**
	 * Adds the.
	 * 
	 * @param group
	 *            the group
	 */
	void add(final Group group) {
		Requires.notNull(group);
		Asserts.notNull(this.fullMatch, "Field '%s' in '%s'.", "fullMatch", getHandle()); //$NON-NLS-1$ //$NON-NLS-2$
		Asserts.notNull(this.map, "Field '%s' in '%s'.", "map", getHandle()); //$NON-NLS-1$ //$NON-NLS-2$
		Asserts.notNull(this.nameMap, "Field '%s' in '%s'.", "nameMap", getHandle()); //$NON-NLS-1$ //$NON-NLS-2$
		Requires.positive(group.getIndex(),
		                  "Parameter '%s' in '%s:%s'.", "group.getIndex()", getHandle(), "add(RegexGroup)");//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		
		this.map.put(group.getIndex(), group);
		
		if (group.getName() != null) {
			this.nameMap.put(group.getName(), group.getIndex());
		}
		
		// POSTRequiresS
		Requires.containsKey(this.map, group.getIndex(), "Field '%s' in '%s' lags key '%s'.", "map", getHandle(),
		                     "group.getIndex()");
		if (group.getName() != null) {
			Requires.containsKey(this.nameMap, group.getName(), "Field '%s' in '%s' lags key '%s'.", "nameMap",
			                     getHandle(), "group.getName()");
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see net.ownhero.dev.regex.Match#get(int)
	 */
	@Override
	public Group get(final int id) {
		Requires.positive(id);
		
		return getGroup(id);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see net.ownhero.dev.regex.Match#getFullMatch()
	 */
	@Override
	public Group getFullMatch() {
		return this.fullMatch;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see net.ownhero.dev.regex.IMatch#get(int)
	 */
	@Override
	public Group getGroup(final int id) {
		Requires.positive(id);
		Requires.notNull(this.map, "Field '%s' in '%s'.", "map", getHandle()); //$NON-NLS-1$ //$NON-NLS-2$
		
		return this.map.get(id);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see net.ownhero.dev.regex.IMatch#get(java.lang.String)
	 */
	@Override
	public Group getGroup(final String name) {
		Requires.notNull(name);
		Requires.notEmpty(name);
		
		// PRERequiresS
		Requires.notNull(this.map, "Field '%s' in '%s'.", "map", getHandle()); //$NON-NLS-1$ //$NON-NLS-2$
		
		final Integer index = this.nameMap.get(name);
		return index == null
		                    ? null
		                    : this.map.get(index);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see net.ownhero.dev.regex.Match#size()
	 */
	@Override
	public int getGroupCount() {
		Requires.notNull(this.map, "Field '%s' in '%s'.", "map", getHandle()); //$NON-NLS-1$ //$NON-NLS-2$
		
		return this.map.size();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see net.ownhero.dev.regex.IMatch#getGroupNames()
	 */
	@Override
	public Set<String> getGroupNames() {
		Requires.notNull(this.map, "Field '%s' in '%s'.", "map", getHandle()); //$NON-NLS-1$ //$NON-NLS-2$
		return this.nameMap.keySet();
		
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see net.ownhero.dev.regex.IMatch#getGroups()
	 */
	@Override
	public Group[] getGroups() {
		Requires.notNull(this.map, "Field '%s' in '%s'.", "map", getHandle()); //$NON-NLS-1$ //$NON-NLS-2$
		
		return this.map.values().toArray(new Group[0]);
	}
	
	/**
	 * Gets the handle.
	 * 
	 * @return the handle
	 */
	public final String getHandle() {
		return getClass().getSimpleName();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see net.ownhero.dev.regex.Match#getNamedGroupCount()
	 */
	@Override
	public int getNamedGroupCount() {
		Requires.notNull(this.nameMap, "Field '%s' in '%s'.", "nameMap", getHandle()); //$NON-NLS-1$ //$NON-NLS-2$
		return this.nameMap.size();
	}
	
	/**
	 * Gets the named groups.
	 * 
	 * @return the named groups
	 */
	@Override
	public Group[] getNamedGroups() {
		final LinkedList<Group> list = new LinkedList<Group>();
		
		for (final Group group : this) {
			if (group.getName() != null) {
				list.add(group);
			}
		}
		
		return list.toArray(new Group[0]);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see net.ownhero.dev.regex.Match#hasGroup(int)
	 */
	@Override
	public boolean hasGroup(final int id) {
		Requires.positive(id);
		Requires.notNull(this.map, "Field '%s' in '%s'.", "map", getHandle()); //$NON-NLS-1$ //$NON-NLS-2$
		
		return this.map.containsKey(id);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see net.ownhero.dev.regex.Match#hasGroups()
	 */
	@Override
	public boolean hasGroups() {
		Requires.notNull(this.map, "Field '%s' in '%s'.", "map", getHandle()); //$NON-NLS-1$ //$NON-NLS-2$
		
		return !this.map.isEmpty();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see net.ownhero.dev.regex.Match#hasNamedGroup(java.lang.String)
	 */
	@Override
	public boolean hasNamedGroup(final String name) {
		Requires.notNull(name);
		Requires.notEmpty(name);
		
		Requires.notNull(this.nameMap, "Field '%s' in '%s'.", "nameMap", getHandle()); //$NON-NLS-1$ //$NON-NLS-2$
		
		return this.nameMap.containsKey(name);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see net.ownhero.dev.regex.Match#hasNamesGroups()
	 */
	@Override
	public boolean hasNamedGroups() {
		Requires.notNull(this.nameMap, "Field '%s' in '%s'.", "nameMap", getHandle()); //$NON-NLS-1$ //$NON-NLS-2$
		
		return !this.nameMap.isEmpty();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see net.ownhero.dev.regex.Match#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		Requires.notNull(this.map, "Field '%s' in '%s'.", "map", getHandle()); //$NON-NLS-1$ //$NON-NLS-2$
		
		return this.map.isEmpty();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Group> iterator() {
		return new Iterator<Group>() {
			
			private final Iterator<Group> groupIterator = MatchImpl.this.map.values().iterator();
			
			@Override
			public boolean hasNext() {
				Requires.notNull(this.groupIterator, "Field '%s' in '%s'.", "groupIterator", getHandle()); //$NON-NLS-1$ //$NON-NLS-2$
				
				return this.groupIterator.hasNext();
			}
			
			@Override
			public Group next() {
				Requires.notNull(this.groupIterator, "Field '%s' in '%s'.", "groupIterator", getHandle()); //$NON-NLS-1$ //$NON-NLS-2$
				
				return this.groupIterator.next();
			}
			
			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		Asserts.notNull(this.map);
		Asserts.notNull(this.nameMap);
		
		final StringBuilder builder = new StringBuilder();
		builder.append("MatchImpl [map={");
		
		StringBuilder builder2 = new StringBuilder();
		for (final Integer key : this.map.keySet()) {
			if (builder2.length() > 0) {
				builder2.append(", ");
			}
			
			builder2.append(key).append(" => ").append(this.map.get(key));
		}
		builder.append(builder2);
		builder.append("}, names={");
		
		builder2 = new StringBuilder();
		for (final String key : this.nameMap.keySet()) {
			if (builder2.length() > 0) {
				builder2.append(", ");
			}
			
			builder2.append(key);
			builder2.append(" => ");
			builder2.append(this.map.get(this.nameMap.get(key)));
		}
		builder.append(builder2);
		builder.append("}]");
		
		return builder.toString();
	}
}
