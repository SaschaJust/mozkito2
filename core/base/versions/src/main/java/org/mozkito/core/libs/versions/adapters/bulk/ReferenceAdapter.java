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
 
package org.mozkito.core.libs.versions.adapters.bulk;

import org.mozkito.core.libs.versions.model.Reference;
import org.mozkito.libraries.sequel.Database;
import org.mozkito.libraries.sequel.bulk.AbstractAdapter;

/**
 * @author Sascha Just
 *
 */
public class ReferenceAdapter extends AbstractAdapter<Reference> {
	
	/**
	 * Instantiates a new PG branch adapter.
	 *
	 * @param type
	 *            the type
	 * @param mode
	 *            the mode
	 */
	public ReferenceAdapter(final Database.Type type, final Database.TxMode mode) {
		super(type, mode, "refs");
	}
	
	/**
	 * Save.
	 *
	 * @param reference
	 *            the reference
	 */
	@Override
	public void save(final Reference reference) {
		this.writer.write(reference.getId(), reference.getType().getValue(), reference.getDepotId(),
		                  truncate(reference.getName(), 900));
	}
}
