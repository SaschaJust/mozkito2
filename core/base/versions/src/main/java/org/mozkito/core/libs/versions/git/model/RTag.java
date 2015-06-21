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

package org.mozkito.core.libs.versions.git.model;

import java.time.Instant;

import org.mozkito.core.libs.versions.model.Identity;

/**
 * @author Sascha Just
 *
 */
public class RTag extends RObject {
	
	Identity tagger;
	Instant  taggerTime;
	String   subject;
	String   body;
	RObject  pointer;
}
