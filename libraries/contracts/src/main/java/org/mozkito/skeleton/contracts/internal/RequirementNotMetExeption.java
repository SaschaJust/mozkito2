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

package org.mozkito.skeleton.contracts.internal;

/**
 * The Class RequirementNotMetExeption.
 *
 * @author Sascha Just
 */
public class RequirementNotMetExeption extends IllegalArgumentException {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3183508622022192717L;
	
	/**
	 * Instantiates a new requirement not met exeption.
	 */
	public RequirementNotMetExeption() {
		// default
	}
	
	/**
	 * Instantiates a new requirement not met exeption.
	 *
	 * @param s
	 *            the s
	 */
	public RequirementNotMetExeption(final String s) {
		super(s);
	}
	
	/**
	 * Instantiates a new requirement not met exeption.
	 *
	 * @param message
	 *            the message
	 * @param cause
	 *            the cause
	 */
	public RequirementNotMetExeption(final String message, final Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * Instantiates a new requirement not met exeption.
	 *
	 * @param cause
	 *            the cause
	 */
	public RequirementNotMetExeption(final Throwable cause) {
		super(cause);
	}
	
}
