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
package org.mozkito.skeleton.regex;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.mozkito.skeleton.contracts.Asserts;
import org.mozkito.skeleton.contracts.Requires;

/**
 * The Class MultiMatchImpl.
 * 
 * @author Sascha Just 
 */
public class MultiMatchImpl implements MultiMatch {
	
	/** The matches. */
	private final List<Match> matches = new LinkedList<Match>();
	
	/**
	 * Instantiates a new {@link MultiMatchImpl}.
	 */
	MultiMatchImpl() {
		
	}
	
	/**
	 * Adds the match to the local list.
	 * 
	 * @param match
	 *            the match
	 */
	void add(final Match match) {
		Requires.notNull(match);
		Asserts.notNull(this.matches, "Field '%s' in '%s'.", "matches", getHandle()); //$NON-NLS-1$ //$NON-NLS-2$
		
		this.matches.add(match);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see net.ownhero.dev.regex.MultiMatch#get(int)
	 */
	/**
	 * {@inheritDoc}
	 * @see org.mozkito.skeleton.regex.MultiMatch#get(int)
	 */
	@Override
	public Match get(final int index) {
		Requires.notNegative(index);
		
		return getMatch(index);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see net.ownhero.dev.regex.MultiMatch#get(int, int)
	 */
	/**
	 * {@inheritDoc}
	 * @see org.mozkito.skeleton.regex.MultiMatch#get(int, int)
	 */
	@Override
	public Group get(final int index,
	                 final int id) {
		Requires.notNegative(index);
		Requires.notNegative(id);
		Asserts.notNull(this.matches, "Field '%s' in '%s'.", "matches", getHandle()); //$NON-NLS-1$ //$NON-NLS-2$
		
		final Match match = this.matches.get(index);
		if (match == null) {
			throw new ArrayIndexOutOfBoundsException("Invalid index: " + index);
		}
		
		return match.getGroup(id);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see net.ownhero.dev.regex.MultiMatch#get(int, java.lang.String)
	 */
	/**
	 * {@inheritDoc}
	 * @see org.mozkito.skeleton.regex.MultiMatch#get(int, java.lang.String)
	 */
	@Override
	public Group get(final int index,
	                 final String name) {
		Requires.notNegative(index);
		Requires.notNull(name);
		Requires.notEmpty(name);
		Asserts.notNull(this.matches, "Field '%s' in '%s'.", "matches", getHandle()); //$NON-NLS-1$ //$NON-NLS-2$
		
		final Match match = this.matches.get(index);
		if (match == null) {
			throw new ArrayIndexOutOfBoundsException("Invalid index: " + index);
		}
		
		return match.getGroup(name);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see net.ownhero.dev.regex.MultiMatch#getGroup(int)
	 */
	/**
	 * {@inheritDoc}
	 * @see org.mozkito.skeleton.regex.MultiMatch#getGroup(int)
	 */
	@Override
	public Group[] getGroup(final int id) {
		Requires.positive(id);
		Asserts.notNull(this.matches, "Field '%s' in '%s'.", "matches", getHandle()); //$NON-NLS-1$ //$NON-NLS-2$
		
		final List<Group> groups = new ArrayList<Group>(this.matches.size());
		
		for (final Match match : this.matches) {
			final Group group = match.getGroup(id);
			
			if (group != null) {
				groups.add(group);
			}
		}
		
		return groups.toArray(new Group[0]);
		
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see net.ownhero.dev.regex.MultiMatch#getGroup(java.lang.String)
	 */
	/**
	 * {@inheritDoc}
	 * @see org.mozkito.skeleton.regex.MultiMatch#getGroup(java.lang.String)
	 */
	@Override
	public Group[] getGroup(final String name) {
		Requires.notNull(name);
		Requires.notEmpty(name);
		Asserts.notNull(this.matches, "Field '%s' in '%s'.", "matches", getHandle()); //$NON-NLS-1$ //$NON-NLS-2$
		
		final List<Group> groups = new ArrayList<Group>(this.matches.size());
		
		for (final Match match : this.matches) {
			groups.add(match.getGroup(name));
		}
		
		return groups.toArray(new Group[0]);
		
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
	 * @see net.ownhero.dev.regex.MultiMatch#getMatch(int)
	 */
	/**
	 * {@inheritDoc}
	 * @see org.mozkito.skeleton.regex.MultiMatch#getMatch(int)
	 */
	@Override
	public Match getMatch(final int index) {
		Requires.notNegative(index);
		Asserts.notNull(this.matches, "Field '%s' in '%s'.", "matches", getHandle()); //$NON-NLS-1$ //$NON-NLS-2$
		
		return this.matches.get(index);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see net.ownhero.dev.regex.MultiMatch#hasGroups()
	 */
	/**
	 * {@inheritDoc}
	 * @see org.mozkito.skeleton.regex.MultiMatch#hasGroups()
	 */
	@Override
	public boolean hasGroups() {
		Asserts.notNull(this.matches, "Field '%s' in '%s'.", "matches", getHandle()); //$NON-NLS-1$ //$NON-NLS-2$
		
		return this.matches.isEmpty()
		                             ? false
		                             : this.matches.iterator().next().hasGroups();
		
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see net.ownhero.dev.regex.MultiMatch#hasNamedGroups()
	 */
	/**
	 * {@inheritDoc}
	 * @see org.mozkito.skeleton.regex.MultiMatch#hasNamedGroups()
	 */
	@Override
	public boolean hasNamedGroups() {
		Asserts.notNull(this.matches, "Field '%s' in '%s'.", "matches", getHandle()); //$NON-NLS-1$ //$NON-NLS-2$
		
		return this.matches.isEmpty()
		                             ? false
		                             : this.matches.iterator().next().hasNamedGroups();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see net.ownhero.dev.regex.MultiMatch#isEmpty()
	 */
	/**
	 * {@inheritDoc}
	 * @see org.mozkito.skeleton.regex.MultiMatch#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		Asserts.notNull(this.matches, "Field '%s' in '%s'.", "matches", getHandle()); //$NON-NLS-1$ //$NON-NLS-2$
		
		return this.matches.isEmpty();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	/**
	 * {@inheritDoc}
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Match> iterator() {
		Asserts.notNull(this.matches, "Field '%s' in '%s'.", "matches", getHandle()); //$NON-NLS-1$ //$NON-NLS-2$
		
		return new Iterator<Match>() {
			
			private final Iterator<Match> matchIterator = MultiMatchImpl.this.matches.iterator();
			
			@Override
			public boolean hasNext() {
				Asserts.notNull(this.matchIterator, "Field '%s' in '%s'.", "matchIterator", getHandle()); //$NON-NLS-1$ //$NON-NLS-2$
				
				return this.matchIterator.hasNext();
			}
			
			@Override
			public Match next() {
				Asserts.notNull(this.matchIterator, "Field '%s' in '%s'.", "matchIterator", getHandle()); //$NON-NLS-1$ //$NON-NLS-2$
				return this.matchIterator.next();
				
			}
			
			/**
			 * @throws UnsupportedOperationException
			 *             guaranteed.
			 */
			@Override
			public void remove() {
				throw new UnsupportedOperationException();
				
			}
		};
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see net.ownhero.dev.regex.MultiMatch#size()
	 */
	/**
	 * {@inheritDoc}
	 * @see org.mozkito.skeleton.regex.MultiMatch#size()
	 */
	@Override
	public int size() {
		Asserts.notNull(this.matches, "Field '%s' in '%s'.", "matches", getHandle()); //$NON-NLS-1$ //$NON-NLS-2$
		
		return this.matches.size();
	}
}
