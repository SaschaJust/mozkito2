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

package org.mozkito.skeleton.logging;

/**
 * LogLevel is used to set the level of information which is output by the {@link Logger}.
 *
 * @author Sascha Just
 */
public enum Level {

	/** No logging at all. */
	OFF,
	/** Logging restricted to fatal only. */
	FATAL,
	/** Logging restricted to errors only. */
	ERROR,
	/** Logging restricted to errors and warnings. */
	WARN,
	/** Logging restricted to errors, warning and info messages. */
	INFO,
	/** Logging errors, warnings, info and debug messages. */
	DEBUG,
	/** Logging in trace mode. This might produce a flood of output messages. */
	TRACE;
}
