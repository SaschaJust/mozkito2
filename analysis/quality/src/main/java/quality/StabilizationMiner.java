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
import org.mozkito.libraries.sequel.BulkReader;
import org.mozkito.skeleton.contracts.Asserts;
import org.mozkito.skeleton.contracts.Requires;

/**
 * The Class StabilizationMiner.
 *
 * @author Sascha Just
 */
public class StabilizationMiner implements Runnable {
	
	private static class Stats {
		
		int    min    = Integer.MAX_VALUE;
		int    max    = 0;
		double mean   = 0;
		double median = 0;
		int    count  = 0;
	}
	
	/** The Constant SEP. */
	private final static char                     SEP      = ';';
	
	/** The reader. */
	private final BulkReader<IntegrationsPerTime> reader;
	
	/** The latest. */
	Map<Long, Instant>                            latest   = new HashMap<>();
	
	/** The buckets. */
	Map<Long, ArrayList<List<Integer>>>           buckets  = new HashMap<>();
	
	/** The max delta. */
	private final int                             maxDelta = 30;
	
	/**
	 * Instantiates a new stabilization miner.
	 *
	 * @param ipwReader
	 *            the ipw reader
	 */
	public StabilizationMiner(final BulkReader<IntegrationsPerTime> ipwReader) {
		Requires.notNull(ipwReader);
		this.reader = ipwReader;
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
					list = deltas.get(i - 1);
					lastBucketIt = list.listIterator(list.size());
					lastBucketSize = lastBucketIt.previous();
					lastBucketIt.set(lastBucketSize + 1);
				}
				
				limit = Math.min(this.maxDelta + 1, days);
				for (int i = 1; i < limit; ++i) {
					list = deltas.get(i - 1);
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
				for (int i = 0; i < this.maxDelta; ++i) {
					list = new LinkedList<Integer>();
					list.add(1);
					this.buckets.get(ipt.handleId).add(list);
				}
			}
		}
		
		List<Integer> bList;
		StringBuilder builder;
		
		final File f = new File("aosp_stabilization.csv");
		try (PrintWriter writer = new PrintWriter(f)) {
			builder = new StringBuilder();
			builder.append("fileId");
			builder.append(';').append("delta");
			builder.append(';').append("measure");
			builder.append(';').append("value");
			writer.println(builder.toString());
			
			for (final Entry<Long, ArrayList<List<Integer>>> entry : this.buckets.entrySet()) {
				
				for (int i = 0; i < entry.getValue().size(); ++i) {
					bList = entry.getValue().get(i);
					
					Collections.sort(bList);
					final Stats stats = computeStats(bList);
					
					builder = new StringBuilder();
					builder.append(entry.getKey());
					builder.append(';').append(i + 1);
					builder.append(';').append("min");
					builder.append(';').append(stats.min);
					writer.println(builder.toString());
					
					builder = new StringBuilder();
					builder.append(entry.getKey());
					builder.append(';').append(i + 1);
					builder.append(';').append("max");
					builder.append(';').append(stats.max);
					writer.println(builder.toString());
					
					builder = new StringBuilder();
					builder.append(entry.getKey());
					builder.append(';').append(i + 1);
					builder.append(';').append("mean");
					builder.append(';').append(stats.mean);
					writer.println(builder.toString());
					
					builder = new StringBuilder();
					builder.append(entry.getKey());
					builder.append(';').append(i + 1);
					builder.append(';').append("median");
					builder.append(';').append(stats.median);
					writer.println(builder.toString());
					
					builder = new StringBuilder();
					builder.append(entry.getKey());
					builder.append(';').append(i + 1);
					builder.append(';').append("count");
					builder.append(';').append(stats.count);
					writer.println(builder.toString());
					
				}
			}
			
			writer.flush();
		} catch (final FileNotFoundException e) {
			e.printStackTrace(System.err);
		}
	}
}
