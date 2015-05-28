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

package org.mozkito.skeleton.exec;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.collections4.collection.UnmodifiableCollection;

import org.mozkito.libraries.logging.Logger;
import org.mozkito.skeleton.contracts.Asserts;
import org.mozkito.skeleton.contracts.Contract;
import org.mozkito.skeleton.contracts.Requires;
import org.mozkito.skeleton.datastructures.CircularByteBuffer;

// TODO: Auto-generated Javadoc
/**
 * The Command class models a command line command execution.
 *
 * @author Sascha Just
 */
public class Command {
	
	/**
	 * The Class Runner.
	 */
	private static class Runner extends Thread {
		
		/** The command. */
		private final Command command;
		
		/** The process. */
		private Process       process;
		
		/**
		 * Instantiates a new runner.
		 *
		 * @param command
		 *            the command
		 */
		public Runner(final Command command) {
			super(Thread.currentThread().getName() + "$[Command:" + command.lineElements.iterator().next()
			        + "][Runner]");
			Requires.notNull(command);
			this.command = command;
		}
		
		/**
		 * {@inheritDoc}
		 * 
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			Asserts.notNull(this.command);
			Asserts.notNull(this.command.lineElements);
			Asserts.notNull(this.command.workingDir);
			Asserts.notNull(this.command.environment);
			
			final ProcessBuilder processBuilder = new ProcessBuilder(this.command.lineElements);
			
			/*
			 * Set the working directory to `dir`. If `dir` is null, the subsequent processes will use the current
			 * working directory of the executing java process.
			 */
			processBuilder.directory(this.command.workingDir);
			
			/*
			 * If a environment map has been supplied, manipulate the subprocesses environment with the corresponding
			 * mappings.
			 */
			for (final String environmentVariable : this.command.environment.keySet()) {
				processBuilder.environment()
				              .put(environmentVariable, this.command.environment.get(environmentVariable));
			}
			
			try {
				this.process = processBuilder.start();
				final Thread outputThread = new Thread(Thread.currentThread().getName() + "[STDOUT-Buffer]") {
					
					/**
					 * {@inheritDoc}
					 * 
					 * @see java.lang.Thread#run()
					 */
					@Override
					public void run() {
						try (BufferedReader reader = new BufferedReader(
						                                                new InputStreamReader(
						                                                                      Runner.this.process.getInputStream()))) {
							try (BufferedWriter writer = new BufferedWriter(
							                                                new OutputStreamWriter(
							                                                                       Runner.this.command.stdOut.getOutputStream()))) {
								String line;
								while ((line = reader.readLine()) != null) {
									writer.write(line);
									writer.newLine();
								}
								
							}
						} catch (final IOException e2) {
							Runner.this.command.errors.add(e2);
						}
					}
				};
				outputThread.start();
				
				final Thread errorThread = new Thread(Thread.currentThread().getName() + "[STDERR-Buffer]") {
					
					/**
					 * {@inheritDoc}
					 * 
					 * @see java.lang.Thread#run()
					 */
					@Override
					public void run() {
						try (BufferedReader reader = new BufferedReader(
						                                                new InputStreamReader(
						                                                                      Runner.this.process.getErrorStream()))) {
							try (BufferedWriter writer = new BufferedWriter(
							                                                new OutputStreamWriter(
							                                                                       Runner.this.command.stdErr.getOutputStream()))) {
								String line;
								while ((line = reader.readLine()) != null) {
									writer.write(line);
									writer.newLine();
								}
							}
						} catch (final IOException e2) {
							Runner.this.command.errors.add(e2);
						}
					}
				};
				errorThread.start();
				
				this.process.waitFor();
				this.command.terminated = true;
			} catch (final IOException | InterruptedException e) {
				this.command.errors.add(e);
			}
		}
	}
	
	/**
	 * Execute.
	 *
	 * @param command
	 *            the command
	 * @param arguments
	 *            the arguments
	 * @param workingDirectory
	 *            the working directory
	 * @return the command
	 */
	public static Command execute(final String command,
	                              final String[] arguments,
	                              final File workingDirectory) {
		Requires.notNull(command);
		Requires.notNull(arguments);
		
		final Command c = new Command(command, arguments, workingDirectory);
		c.logStdErr();
		c.run();
		
		return c;
	}
	
	/** The line elements. */
	private final List<String>                     lineElements;
	
	/** The std err. */
	private final CircularByteBuffer               stdErr       = new CircularByteBuffer(100000);
	
	/** The std out. */
	private final CircularByteBuffer               stdOut       = new CircularByteBuffer(1000000);
	
	/** The errors. */
	private final ConcurrentLinkedQueue<Exception> errors       = new ConcurrentLinkedQueue<Exception>();
	
	/** The working dir. */
	private File                                   workingDir   = new File(System.getProperty("java.io.tmpdir"));
	
	/** The environment. */
	final Map<String, String>                      environment  = new HashMap<>();
	
	/** The terminated. */
	private boolean                                terminated   = false;
	
	/** The runner. */
	private final Runner                           runner;
	
	/** The std out reader. */
	private final BufferedReader                   stdOutReader = new BufferedReader(
	                                                                                 new InputStreamReader(
	                                                                                                       this.stdOut.getInputStream()));
	
	/** The std err reader. */
	private final BufferedReader                   stdErrReader = new BufferedReader(
	                                                                                 new InputStreamReader(
	                                                                                                       this.stdErr.getInputStream()));
	
	private Thread                                 logger;
	
	/**
	 * Instantiates a new command.
	 *
	 * @param command
	 *            the command
	 * @param arguments
	 *            the arguments
	 * @param workingDirectory
	 *            the working directory
	 */
	public Command(final String command, final String[] arguments, final File workingDirectory) {
		Requires.notNull(command);
		Requires.notEmpty(command);
		Requires.notNull(arguments);
		
		this.lineElements = new ArrayList<String>(arguments.length + 1);
		this.lineElements.add(command);
		this.lineElements.addAll(Arrays.asList(arguments));
		
		if (workingDirectory != null) {
			Contract.requires(workingDirectory.isDirectory());
			Contract.requires(workingDirectory.canRead());
			Contract.requires(workingDirectory.canWrite());
			Contract.requires(workingDirectory.canExecute());
			
			this.workingDir = workingDirectory;
		}
		
		this.runner = new Runner(this);
		
		Asserts.notNull(this.lineElements);
		Asserts.notNull(this.workingDir);
		Asserts.notNull(this.runner);
	}
	
	/**
	 * Exit value.
	 *
	 * @return the int
	 */
	public int exitValue() {
		Asserts.notNull(this.runner);
		Asserts.notNull(this.runner.process);
		return this.runner.process.exitValue();
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		try {
			Asserts.notNull(this.stdOutReader);
			this.stdOutReader.close();
		} catch (final IOException e) {
			// ignore
		}
		
		try {
			Asserts.notNull(this.stdErrReader);
			this.stdErrReader.close();
		} catch (final IOException e) {
			// ignore
		}
		
		if (this.logger != null) {
			for (final Exception e : this.errors) {
				Logger.error(e);
			}
		}
	}
	
	/**
	 * Gets the error.
	 *
	 * @return the error
	 */
	public Collection<Exception> getError() {
		Asserts.notNull(this.errors);
		return UnmodifiableCollection.unmodifiableCollection(this.errors);
	}
	
	/**
	 * Gets the input.
	 *
	 * @return the input
	 */
	public OutputStream getInput() {
		Asserts.notNull(this.runner);
		Asserts.notNull(this.runner.process);
		return this.runner.process.getOutputStream();
	}
	
	/**
	 * Gets the std err.
	 *
	 * @return the std err
	 */
	public InputStream getStdErr() {
		Asserts.notNull(this.stdErr);
		return this.stdErr.getInputStream();
	}
	
	/**
	 * Gets the std out.
	 *
	 * @return the std out
	 */
	public InputStream getStdOut() {
		Asserts.notNull(this.stdOut);
		return this.stdOut.getInputStream();
	}
	
	/**
	 * Checks if is terminated.
	 *
	 * @return true, if is terminated
	 */
	public boolean isTerminated() {
		return this.terminated;
	}
	
	/**
     * 
     */
	public void logStdErr() {
		this.logger = new Thread(Thread.currentThread().getName() + "[Command:" + this.lineElements.iterator().next()
		        + "][Logger]") {
			
			/**
			 * {@inheritDoc}
			 * 
			 * @see java.lang.Thread#run()
			 */
			@Override
			public void run() {
				String line;
				while ((line = nextErrput()) != null) {
					Logger.warn.println(line);
				}
			}
		};
		this.logger.start();
	}
	
	/**
	 * Next errput.
	 *
	 * @return the string
	 */
	public String nextErrput() {
		try {
			Asserts.notNull(this.stdErrReader);
			return this.stdErrReader.readLine();
		} catch (final IOException e) {
			Asserts.notNull(this.errors);
			this.errors.add(e);
			return null;
		}
	}
	
	/**
	 * Next output.
	 *
	 * @return the string
	 */
	public String nextOutput() {
		Asserts.notNull(this.stdOutReader);
		
		try {
			return this.stdOutReader.readLine();
		} catch (final IOException e) {
			Asserts.notNull(this.errors);
			this.errors.add(e);
			return null;
		}
	}
	
	/**
	 * Run.
	 */
	public void run() {
		Asserts.notNull(this.runner);
		this.runner.start();
	}
	
	/**
	 * Wait for.
	 */
	public void waitFor() {
		Asserts.notNull(this.runner);
		if (this.runner.process == null) {
			try {
				this.runner.join();
			} catch (final InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
}
