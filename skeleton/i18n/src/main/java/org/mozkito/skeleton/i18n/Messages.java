/***********************************************************************************************************************
 * Copyright 2014 Sascha Just
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

package org.mozkito.skeleton.i18n;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Class Messages.
 * 
 * @author Sascha Just
 */
public class Messages {
	
	/**
	 * Gets the string.
	 * 
	 * @param key
	 *            the key
	 * @param arguments
	 *            the arguments
	 * @return the string
	 */
	public static String get(final String key,
	                         final Object... arguments) {
		if (key == null) {
			throw new NullPointerException("Requesting an i18n string requires the key to be not (null)."); //$NON-NLS-1$
		}
		
		try {
			final String resource = Messages.RESOURCE_BUNDLE.getString(key);
			
			// this is only for debugging purposes. The reasoning behind this, is having a way to find defective i18n
			// strings.
			if (ASSERTIONS_ENABLED) {
				final Pattern p = Pattern.compile("\\{([0-9]+)\\}"); //$NON-NLS-1$
				final Matcher matcher = p.matcher(resource);
				int maxId = -1;
				
				while (matcher.find()) {
					final String id = matcher.group(1);
					maxId = Math.max(maxId, Integer.parseInt(id));
				}
				
				final StringBuilder builder = new StringBuilder();
				for (final Object o : arguments) {
					if (builder.length() > 0) {
						builder.append(',');
					}
					builder.append(o);
				}
				assert maxId + 1 == arguments.length : String.format("Expecting %s arguments, but received %s. Message format string '%s' did not match arguments: [%s]",
				                                                     maxId + 1, arguments.length, resource,
				                                                     builder.toString());
			}
			
			if (arguments.length > 0) {
				return MessageFormat.format(resource, arguments);
			} else {
				return resource;
			}
		} catch (final MissingResourceException e) {
			return '!' + key + '!';
		}
	}
	
	/**
	 * Load bundle.
	 * 
	 * @return the resource bundle
	 */
	private static ResourceBundle loadBundle(final Locale locale) {
		
		try {
			return ResourceBundle.getBundle(Messages.BUNDLE_NAME, locale);
		} catch (final MissingResourceException e) {
			try {
				return ResourceBundle.getBundle(Messages.BUNDLE_NAME, Locale.US);
			} catch (final MissingResourceException ex) {
				throw new RuntimeException("Could not load string table.", ex); //$NON-NLS-1$
			}
		}
	}
	
	/**
	 * Sets the locale.
	 *
	 * @param locale
	 *            the new locale
	 */
	public static void setLocale(final Locale locale) {
		if (locale == null) {
			throw new NullPointerException("Locale must not be (null).");
		}
		
		RESOURCE_BUNDLE = loadBundle(locale);
	}
	
	private static boolean        ASSERTIONS_ENABLED;
	
	static {
		try {
			assert false;
			ASSERTIONS_ENABLED = false;
		} catch (final AssertionError ae) {
			ASSERTIONS_ENABLED = true;
		}
	}
	
	/** The Constant BUNDLE_NAME. */
	private static final String   BUNDLE_NAME     = "messages";
	// Messages.class.getPackage().getName();
	
	/** The Constant RESOURCE_BUNDLE. */
	private static ResourceBundle RESOURCE_BUNDLE = Messages.loadBundle(Locale.getDefault());
	
	/**
	 * Instantiates a new messages.
	 */
	private Messages() {
		// avoid instantiation
	}
}
