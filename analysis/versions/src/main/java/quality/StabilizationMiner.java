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
 
package quality;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import org.mozkito.analysis.quality.IntegrationsPerTime;
import org.mozkito.libraries.sequel.bulk.BulkReader;
import org.mozkito.skeleton.contracts.Asserts;
import org.mozkito.skeleton.contracts.Requires;

/**
 * The Class StabilizationMiner.
 *
 * @author Sascha Just
 */
public class StabilizationMiner implements Runnable {
	
	/**
	 * The Class Stats.
	 */
	private static class Stats {
		
		/** The min. */
		int    min    = Integer.MAX_VALUE;
		
		/** The max. */
		int    max    = 0;
		
		/** The mean. */
		double mean   = 0;
		
		/** The median. */
		double median = 0;
		
		/** The count. */
		int    count  = 0;
	}
	
	/** The Constant SEP. */
	private final static char                     SEP     = ';';
	
	/** The reader. */
	private final BulkReader<IntegrationsPerTime> reader;
	
	/** The latest. */
	Map<Long, Instant>                            latest  = new HashMap<>();
	
	/** The buckets. */
	Map<Long, ArrayList<List<Integer>>>           buckets = new HashMap<>();
	
	/** The max delta. */
	private final int                             maxDelta;
	
	/** The project name. */
	private final String                          projectName;
	
	/**
	 * Instantiates a new stabilization miner.
	 *
	 * @param ipwReader
	 *            the ipw reader
	 * @param projectName
	 *            the project name
	 * @param maxDelta
	 *            the max delta
	 */
	public StabilizationMiner(final BulkReader<IntegrationsPerTime> ipwReader, final String projectName,
	        final int maxDelta) {
		Requires.notNull(ipwReader);
		this.reader = ipwReader;
		this.projectName = projectName;
		this.maxDelta = maxDelta;
	}
	
	/**
	 * Compute stats.
	 *
	 * @param bList
	 *            the b list
	 * @return the stats
	 */
	private Stats computeStats(final List<Integer> bList) {
		Requires.notNull(bList);
		
		final Stats stats = new Stats();
		int sum = 0;
		final int middle = bList.size() / 2;
		int lMiddle = 0, uMiddle = 0;
		
		for (final Integer i : bList) {
			sum += i;
			stats.min = Math.min(stats.min, i);
			stats.max = Math.max(stats.max, i);
			if (stats.count == middle) {
				lMiddle = i;
			}
			if (stats.count == middle + 1) {
				uMiddle = i;
			}
			++stats.count;
		}
		
		if (bList.size() % 2 == 1) {
			stats.median = lMiddle;
		} else {
			stats.mean = (lMiddle + uMiddle) / 2d;
		}
		
		stats.mean = sum / (double) stats.count;
		
		Asserts.equalTo(stats.count, bList.size());
		
		return stats;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		Asserts.notNull(this.reader);
		Asserts.notNull(this.latest);
		
		IntegrationsPerTime ipt;
		List<Integer> list;
		ListIterator<Integer> lastBucketIt;
		Integer lastBucketSize;
		
		// bulk read into IntegrationsPerTime objects
		while ((ipt = this.reader.read()) != null) {
			// not the first entry for that file
			if (this.latest.containsKey(ipt.handleId)) {
				// compute actual delta
				final Duration duration = Duration.between(this.latest.get(ipt.handleId), ipt.integrationTime);
				
				// ceil
				final int days = (int) Math.ceil(duration.getSeconds() / (3600d * 24d));
				
				// fix equal timestamps
				int limit = Math.max(1, days);
				
				final ArrayList<List<Integer>> deltas = this.buckets.get(ipt.handleId);
				
				for (int i = limit; i <= this.maxDelta; ++i) {
					list = deltas.get(i);
					lastBucketIt = list.listIterator(list.size());
					lastBucketSize = lastBucketIt.previous();
					lastBucketIt.set(lastBucketSize + 1);
				}
				
				limit = Math.min(this.maxDelta + 1, days);
				for (int i = 0; i < limit; ++i) {
					list = deltas.get(i);
					list.add(1);
				}
				
				this.latest.put(ipt.handleId, ipt.integrationTime);
			} else {
				// first entry
				// add to cache
				this.latest.put(ipt.handleId, ipt.integrationTime);
				
				// create bucket for for each delta
				// and add 1 to the bucket
				this.buckets.put(ipt.handleId, new ArrayList<List<Integer>>());
				for (int i = 0; i <= this.maxDelta; ++i) {
					list = new LinkedList<Integer>();
					list.add(1);
					this.buckets.get(ipt.handleId).add(list);
				}
			}
		}
		
		List<Integer> bList;
		StringBuilder builder;
		
		final File f = new File(this.projectName + "_stabilization_" + this.maxDelta + "_community.csv");
		try (PrintWriter writer = new PrintWriter(f)) {
			builder = new StringBuilder();
			builder.append("fileId");
			builder.append(SEP).append("delta");
			builder.append(SEP).append("measure");
			builder.append(SEP).append("value");
			writer.println(builder.toString());
			
			for (final Entry<Long, ArrayList<List<Integer>>> entry : this.buckets.entrySet()) {
				
				for (int i = 0; i < entry.getValue().size(); ++i) {
					bList = entry.getValue().get(i);
					
					Collections.sort(bList);
					final Stats stats = computeStats(bList);
					
					builder = new StringBuilder();
					builder.append(entry.getKey());
					builder.append(SEP).append(i);
					builder.append(SEP).append("min");
					builder.append(SEP).append(stats.min);
					writer.println(builder.toString());
					
					builder = new StringBuilder();
					builder.append(entry.getKey());
					builder.append(SEP).append(i);
					builder.append(SEP).append("max");
					builder.append(SEP).append(stats.max);
					writer.println(builder.toString());
					
					builder = new StringBuilder();
					builder.append(entry.getKey());
					builder.append(SEP).append(i);
					builder.append(SEP).append("mean");
					builder.append(SEP).append(stats.mean);
					writer.println(builder.toString());
					
					builder = new StringBuilder();
					builder.append(entry.getKey());
					builder.append(SEP).append(i);
					builder.append(SEP).append("median");
					builder.append(SEP).append(stats.median);
					writer.println(builder.toString());
					
					builder = new StringBuilder();
					builder.append(entry.getKey());
					builder.append(SEP).append(i);
					builder.append(SEP).append("count");
					builder.append(SEP).append(stats.count);
					writer.println(builder.toString());
					
				}
			}
			
			writer.flush();
		} catch (final FileNotFoundException e) {
			e.printStackTrace(System.err);
		}
	}
}
