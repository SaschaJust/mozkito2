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
 
package org.mozkito.core.libs.versions;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.collection.UnmodifiableCollection;

import org.mozkito.core.libs.versions.model.ChangeSet;
import org.mozkito.core.libs.versions.model.Depot;
import org.mozkito.core.libs.versions.model.Handle;
import org.mozkito.core.libs.versions.model.Renaming;
import org.mozkito.libraries.sequel.IDumper;
import org.mozkito.skeleton.contracts.Asserts;
import org.mozkito.skeleton.contracts.Requires;

/**
 * The Class FileTracker.
 *
 * @author Sascha Just
 */
public class FileCache {
	
	/**
	 * The Class Add.
	 */
	private static abstract class Add {
		
		/**
		 * Adds the.
		 */
		public abstract void add();
	}
	
	/**
	 * The Class Delete.
	 */
	private static abstract class Delete {
		
		/**
		 * Delete.
		 */
		public abstract void delete();
	}
	
	private final IDumper<Handle> dumper;
	
	/** The depot. */
	private final Depot                  depot;
	
	/** The cache. */
	private final Map<String, Handle>    cache         = new HashMap<>();
	
	/** The renamings. */
	private final Map<String, Renaming>  renamings     = new HashMap<>();
	
	/** The in transaction. */
	private boolean                      inTransaction = false;
	
	/** The deletes. */
	private final List<Delete>           delActions    = new LinkedList<>();
	
	/** The adds. */
	private final List<Add>              addActions    = new LinkedList<>();
	
	/**
	 * Instantiates a new file cache.
	 *
	 * @param depot
	 *            the depot
	 * @param handleDumper
	 *            the handle dumper
	 */
	public FileCache(final Depot depot, final IDumper<Handle> handleDumper) {
		this.dumper = handleDumper;
		this.depot = depot;
	}
	
	/**
	 * Adds the.
	 *
	 * @param path
	 *            the path
	 * @return the handle
	 */
	public Handle add(final String path) {
		Requires.notNull(path);
		
		// add to the cache
		final Handle handle = new Handle(this.depot, path);
		this.dumper.saveLater(handle);
		
		this.addActions.add(new Add() {
			
			@Override
			public void add() {
				FileCache.this.cache.put(path, handle);
			}
		});
		
		return handle;
	}
	
	/**
	 * Begin transaction.
	 */
	public void beginTransaction() {
		if (this.inTransaction) {
			throw new IllegalStateException();
		}
		this.inTransaction = true;
	}
	
	/**
	 * Commit.
	 */
	public void commit() {
		if (!this.inTransaction) {
			throw new IllegalStateException();
		}
		
		for (final Delete delete : this.delActions) {
			delete.delete();
		}
		this.delActions.clear();
		
		for (final Add add : this.addActions) {
			add.add();
		}
		this.addActions.clear();
		
		this.inTransaction = false;
	}
	
	/**
	 * Copy.
	 *
	 * @param similarity
	 *            the similarity
	 * @param source
	 *            the source
	 * @param target
	 *            the target
	 * @param where
	 *            the where
	 * @return the handle
	 */
	public Handle copy(final short similarity,
	                   final String source,
	                   final String target,
	                   final ChangeSet where) {
		Requires.notNull(source);
		Requires.notNull(target);
		Requires.notNull(where);
		Requires.notNegative(similarity);
		Requires.lessOrEqual(similarity, 100);
		
		final Handle handle = new Handle(this.depot, target);
		this.dumper.saveLater(handle);
		
		this.addActions.add(new Add() {
			
			@Override
			public void add() {
				FileCache.this.cache.put(target, handle);
			}
		});
		
		return handle;
	}
	
	/**
	 * Delete.
	 *
	 * @param path
	 *            the path
	 * @return the handle
	 */
	public Handle delete(final String path) {
		Requires.notNull(path);
		
		if (this.cache.containsKey(path)) {
			final Handle handle = this.cache.get(path);
			this.delActions.add(new Delete() {
				
				@Override
				public void delete() {
					FileCache.this.cache.remove(path);
				}
			});
			return handle;
		} else {
			final Handle handle = new Handle(this.depot, path);
			this.dumper.saveLater(handle);
			return handle;
		}
		
	}
	
	/**
	 * Gets the.
	 *
	 * @param path
	 *            the path
	 * @return the handle
	 */
	public Handle get(final String path) {
		return this.cache.get(path);
	}
	
	/**
	 * Gets the renamings.
	 *
	 * @return the renamings
	 */
	public Collection<Renaming> getRenamings() {
		return UnmodifiableCollection.unmodifiableCollection(this.renamings.values());
	}
	
	/**
	 * Modify.
	 *
	 * @param path
	 *            the path
	 * @return the handle
	 */
	public Handle modify(final String path) {
		Requires.notNull(path);
		
		if (!this.cache.containsKey(path)) {
			final Handle handle = new Handle(this.depot, path);
			this.dumper.saveLater(handle);
			this.addActions.add(new Add() {
				
				@Override
				public void add() {
					FileCache.this.cache.put(path, handle);
				}
			});
			return handle;
		}
		
		return this.cache.get(path);
	}
	
	/**
	 * Rename.
	 *
	 * @param similarity
	 *            the similarity
	 * @param source
	 *            the source
	 * @param target
	 *            the target
	 * @param where
	 *            the where
	 * @return the handle
	 */
	public Handle rename(final short similarity,
	                     final String source,
	                     final String target,
	                     final ChangeSet where) {
		Requires.notNull(source);
		Requires.notNull(target);
		Requires.notNull(where);
		Requires.notNegative(similarity);
		Requires.lessOrEqual(similarity, 100);
		
		if (!this.cache.containsKey(source)) {
			final Handle handle = new Handle(this.depot, source);
			this.dumper.saveLater(handle);
			this.cache.put(source, handle);
		}
		
		Asserts.containsKey(this.cache, source);
		
		final Handle from = this.cache.get(source);
		final Handle to = new Handle(this.depot, target);
		this.dumper.saveLater(to);
		
		Renaming renaming;
		if (this.renamings.containsKey(source)) {
			renaming = this.renamings.get(source);
		} else {
			renaming = new Renaming();
		}
		renaming.add(similarity, from, to, where);
		
		this.delActions.add(new Delete() {
			
			@Override
			public void delete() {
				FileCache.this.renamings.remove(source);
				FileCache.this.cache.remove(source);
			}
		});
		
		this.addActions.add(new Add() {
			
			@Override
			public void add() {
				FileCache.this.renamings.put(target, renaming);
				FileCache.this.cache.put(target, to);
			}
		});
		
		return to;
	}
}
