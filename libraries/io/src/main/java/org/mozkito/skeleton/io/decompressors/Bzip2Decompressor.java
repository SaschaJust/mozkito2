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

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.io.IOUtils;

import org.mozkito.skeleton.io.CompressionUtils.ArchiveType;
import org.mozkito.skeleton.io.exceptions.FilePermissionException;
import org.mozkito.skeleton.io.exceptions.UnsupportedExtensionException;

/**
 * @author Sascha Just <sascha.just@mozkito.org>
 * 
 */
public class Bzip2Decompressor extends CompressionDecompressor {
	
	/**
	 * Bunzip2 provides bzip2 decompression. Please note that this won't transitively call the appropriate decompressor
	 * on the inner archive, if there is any.
	 * 
	 * @param archive
	 *            the archive file object
	 * @param targetDirectory
	 *            the target directory the file is extracted to
	 * @throws NullPointerException
	 *             if the archive is null
	 * @throws FileNotFoundException
	 *             if the archive file cannot be found.
	 * @throws UnsupportedExtensionException
	 *             if extension is not any of the valid extensions for this decompressor.
	 * @throws FileAlreadyExistsException
	 *             if the targetFile already exists.
	 * @throws FilePermissionException
	 *             if the we do not have sufficient rights to read from the archive.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred (e.g. creating the result file, creating the target
	 *             directory (if not present), reading from the archive or writing to the file).
	 */
	@Override
	public File decompress(final File archive,
	                       final File targetDirectory) throws IOException {
		final File outputFile = prepareOutputFile(archive, targetDirectory, ArchiveType.BZIP2);
		
		// open compressor input stream and a file output stream and copy data between the two
		try (BZip2CompressorInputStream bz2InputStream = new BZip2CompressorInputStream(
		                                                                                new BufferedInputStream(
		                                                                                                        new FileInputStream(
		                                                                                                                            archive)))) {
			try (final FileOutputStream fileOutputStream = new FileOutputStream(outputFile)) {
				IOUtils.copy(bz2InputStream, fileOutputStream);
			}
		}
		
		// return the output file after successful write
		return outputFile;
		
	}
	
}
