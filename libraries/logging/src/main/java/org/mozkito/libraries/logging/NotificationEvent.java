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

package org.mozkito.libraries.logging;

import java.time.Instant;

/**
 * The Class NotificationEvent.
 *
 * @author Sascha Just
 */
public class NotificationEvent extends LogEvent {

	/**
	 * Instantiates a new notification event.
	 *
	 * @param timestamp
	 *            the timestamp
	 * @param level
	 *            the level
	 * @param throwable
	 *            the throwable
	 * @param message
	 *            the message
	 * @param args
	 *            the args
	 */
	public NotificationEvent(final Instant timestamp, final Level level, final Throwable throwable,
	                         final String message, final Object[] args) {
		super(timestamp, level, throwable, message, args);
	}

}
