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

package versions;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.management.ClassLoadingMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;

import org.mozkito.skeleton.io.FileUtils;
import org.mozkito.skeleton.logging.Logger;

/**
 * The Class MozkitoHandler.
 */
public class MozkitoHandler implements UncaughtExceptionHandler {
	
	private String getClassLoadingInformation() {
		final ClassLoadingMXBean bean = ManagementFactory.getClassLoadingMXBean();
		final StringBuilder builder = new StringBuilder();
		builder.append("Loaded classes: ");
		builder.append("total: ").append(bean.getTotalLoadedClassCount()).append(" ");
		builder.append("current: ").append(bean.getLoadedClassCount()).append(" ");
		builder.append("unloaded: ").append(bean.getUnloadedClassCount());
		builder.append(FileUtils.lineSeparator);
		return builder.toString();
	}
	
	private String getCrashReport(final Throwable e) {
		final StringBuilder body = new StringBuilder();
		
		try {
			body.append(">>> Crash Report >>>");
			body.append(FileUtils.lineSeparator);
			body.append(FileUtils.lineSeparator);
			
			StringWriter stack = new StringWriter();
			PrintWriter writer = new PrintWriter(stack);
			e.printStackTrace(writer);
			
			// body.append(FileUtils.lineSeparator);
			// body.append(FileUtils.lineSeparator);
			body.append("Stacktrace:");
			body.append(FileUtils.lineSeparator);
			body.append(FileUtils.lineSeparator);
			body.append(stack.toString());
			body.append(FileUtils.lineSeparator);
			
			Throwable t = e.getCause();
			while (t != null) {
				stack = new StringWriter();
				writer = new PrintWriter(stack);
				t.printStackTrace(writer);
				
				body.append(FileUtils.lineSeparator);
				body.append("Cause Stacktrace:");
				body.append(FileUtils.lineSeparator);
				body.append(FileUtils.lineSeparator);
				body.append(stack.toString());
				body.append(FileUtils.lineSeparator);
				
				t = t.getCause();
			}
			
			body.append("<<< Crash Report <<<");
			body.append(FileUtils.lineSeparator);
			body.append(FileUtils.lineSeparator);
		} catch (final Throwable ignore) {
			// ignore
		}
		
		try {
			body.append(">>> System Information >>>");
			body.append(FileUtils.lineSeparator);
			body.append(FileUtils.lineSeparator);
			body.append(getSystemInformation());
			body.append(getClassLoadingInformation());
			body.append(getRuntimeInformation());
			body.append(getJavaInformation());
			body.append(FileUtils.lineSeparator);
			body.append("<<< System Information <<<");
			body.append(FileUtils.lineSeparator);
			body.append(FileUtils.lineSeparator);
		} catch (final Throwable ignore) {
			// ignore
		}
		
		try {
			body.append(">>> Active Threads >>>");
			body.append(FileUtils.lineSeparator);
			body.append(FileUtils.lineSeparator);
			body.append(getThreadInformation());
			body.append("<<< Active Threads <<<");
			body.append(FileUtils.lineSeparator);
			body.append(FileUtils.lineSeparator);
		} catch (final Throwable ignore) {
			// ignore
		}
		
		try {
			body.append(">>> Memory Usage >>>");
			body.append(FileUtils.lineSeparator);
			body.append(FileUtils.lineSeparator);
			body.append(getMemoryInformation());
			body.append("<<< Memory Usage <<<");
			body.append(FileUtils.lineSeparator);
			body.append(FileUtils.lineSeparator);
		} catch (final Throwable ignore) {
			// ignore
		}
		
		return body.toString();
	}
	
	private String getJavaInformation() {
		final StringBuilder builder = new StringBuilder();
		
		builder.append("Java class path: ").append(System.getProperty("java.class.path"))
		       .append(FileUtils.lineSeparator);
		builder.append("Java class version: ").append(System.getProperty("java.class.version"))
		       .append(FileUtils.lineSeparator);
		builder.append("Java compiler: ").append(System.getProperty("java.compiler")).append(FileUtils.lineSeparator);
		builder.append("Java home: ").append(System.getProperty("java.home")).append(FileUtils.lineSeparator);
		builder.append("Java tempdir: ").append(System.getProperty("java.io.tmpdir")).append(FileUtils.lineSeparator);
		builder.append("Java version: ").append(System.getProperty("java.version")).append(FileUtils.lineSeparator);
		builder.append("Java vendor: ").append(System.getProperty("java.vendor")).append(FileUtils.lineSeparator);
		builder.append("OS name: ").append(System.getProperty("os.name")).append(FileUtils.lineSeparator);
		builder.append("OS architecture: ").append(System.getProperty("os.arch")).append(FileUtils.lineSeparator);
		builder.append("OS version: ").append(System.getProperty("os.version")).append(FileUtils.lineSeparator);
		return builder.toString();
	}
	
	/**
	 * @return
	 */
	private String getMemoryInformation() {
		final Runtime runtime = Runtime.getRuntime();
		final MemoryUsage memUsage = new MemoryUsage(4096 * 1024 * 1024, runtime.totalMemory() - runtime.freeMemory(),
		                                             runtime.totalMemory(), runtime.maxMemory());
		return memUsage.toString();
	}
	
	private String getRuntimeInformation() {
		final RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
		final StringBuilder builder = new StringBuilder();
		builder.append("VM: ");
		builder.append(bean.getVmVendor()).append(" ");
		builder.append(bean.getVmName()).append(" ");
		builder.append(bean.getVmVersion());
		builder.append(FileUtils.lineSeparator);
		return builder.toString();
		
	}
	
	private String getSystemInformation() {
		final OperatingSystemMXBean systemMXBean = ManagementFactory.getOperatingSystemMXBean();
		final StringBuilder builder = new StringBuilder();
		builder.append("Operating System: ");
		builder.append(systemMXBean.getName()).append(" ").append(systemMXBean.getVersion()).append(" ")
		       .append(systemMXBean.getArch());
		builder.append(FileUtils.lineSeparator);
		return builder.toString();
	}
	
	private String getThreadInformation() {
		ThreadGroup root = Thread.currentThread().getThreadGroup().getParent();
		while (root.getParent() != null) {
			root = root.getParent();
		}
		
		// Visit each thread group
		return visit(root, 0) + FileUtils.lineSeparator;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Thread.UncaughtExceptionHandler#uncaughtException(java.lang.Thread, java.lang.Throwable)
	 */
	public void uncaughtException(final Thread t,
	                              final Throwable e) {
		Logger.fatal(e, "%s: Unhandled exception. Terminated.", t.getName());
		Logger.fatal(getCrashReport(e));
	}
	
	private String visit(final ThreadGroup group,
	                     final int level) {
		// Get threads in `group'
		final StringBuilder builder = new StringBuilder();
		int numThreads = group.activeCount();
		final Thread[] threads = new Thread[numThreads * 2];
		numThreads = group.enumerate(threads, false);
		
		final StringBuilder indent = new StringBuilder();
		for (int i = 0; i < level; ++i) {
			indent.append("  ");
		}
		
		// Enumerate each thread in `group'
		for (int i = 0; i < numThreads; i++) {
			// Get thread
			final Thread thread = threads[i];
			builder.append(indent);
			builder.append("|-");
			builder.append(thread.getName()).append(" [");
			builder.append(thread.getClass().getSimpleName()).append("], ");
			builder.append(thread.getPriority()).append(", ");
			builder.append(thread.getState().name());
			builder.append(FileUtils.lineSeparator);
			for (final StackTraceElement element : thread.getStackTrace()) {
				builder.append(indent);
				builder.append("| ");
				builder.append(element.toString());
				builder.append(FileUtils.lineSeparator);
			}
			// builder.append(FileUtils.lineSeparator);
		}
		
		// Get thread subgroups of `group'
		int numGroups = group.activeGroupCount();
		final ThreadGroup[] groups = new ThreadGroup[numGroups * 2];
		numGroups = group.enumerate(groups, false);
		
		// Recursively visit each subgroup
		for (int i = 0; i < numGroups; i++) {
			builder.append(indent);
			builder.append(visit(groups[i], level + 1));
		}
		
		return builder.toString();
	}
}
