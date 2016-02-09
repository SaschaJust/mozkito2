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

package org.mozkito.skeleton.io.decompressors;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;

import org.apache.commons.compress.compressors.pack200.Pack200CompressorInputStream;
import org.apache.commons.io.IOUtils;

import org.mozkito.skeleton.io.CompressionUtils.ArchiveType;
import org.mozkito.skeleton.io.exceptions.FilePermissionException;
import org.mozkito.skeleton.io.exceptions.UnsupportedExtensionException;

/**
 * The Class Pack200Decompressor.
 * 
 * @author Sascha Just <sascha.just@mozkito.org>
 */
public class Pack200Decompressor extends CompressionDecompressor {
	
	/** The Constant NEW_SUFFIX. */
	private static final String NEW_SUFFIX = "jar";
	
	/**
	 * Instantiates a new pack200 decompressor.
	 */
	public Pack200Decompressor() {
		super();
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.io.decompressors.CompressionDecompressor#decompress(java.io.File, java.io.File)
	 */
	@Override
	public File decompress(final File archive,
	                       final File targetDirectory) throws NullPointerException,
	                                                  FileNotFoundException,
	                                                  UnsupportedExtensionException,
	                                                  FileAlreadyExistsException,
	                                                  FilePermissionException,
	                                                  IOException {
		
		File outputFile = prepareOutputFile(archive, targetDirectory, ArchiveType.PACK200);
		outputFile = new File(outputFile.getAbsolutePath() + "." + NEW_SUFFIX);
		
		// open compressor input stream and a file output stream and copy data between the two
		try (Pack200CompressorInputStream pack200InputStream = new Pack200CompressorInputStream(
		                                                                                        new BufferedInputStream(
		                                                                                                                new FileInputStream(
		                                                                                                                                    archive)))) {
			try (final FileOutputStream fileOutputStream = new FileOutputStream(outputFile)) {
				IOUtils.copy(pack200InputStream, fileOutputStream);
			}
		}
		
		// return the output file after successful write
		return outputFile;
	}
	
}
