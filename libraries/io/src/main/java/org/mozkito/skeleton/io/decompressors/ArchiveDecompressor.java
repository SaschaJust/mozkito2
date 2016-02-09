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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;

import org.apache.commons.io.FilenameUtils;

import org.mozkito.skeleton.io.CompressionUtils;
import org.mozkito.skeleton.io.CompressionUtils.ArchiveType;
import org.mozkito.skeleton.io.FileUtils;
import org.mozkito.skeleton.io.exceptions.FilePermissionException;
import org.mozkito.skeleton.io.exceptions.UnsupportedExtensionException;

/**
 * @author Sascha Just <sascha.just@mozkito.org>
 * 
 */
public abstract class ArchiveDecompressor implements IDecompressor {
	
	/**
	 * 
	 */
	public ArchiveDecompressor() {
		super();
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * Provides decompression for single file algorithms (like bzip2). Please note that this won't transitively call the
	 * appropriate decompressor on the inner archive, if there is any.
	 * 
	 * @param archive
	 *            the archive file object
	 * @param targetDirectory
	 *            the target directory the file is extracted to. If null, the file is extracted to the working
	 *            directory.
	 * @return the extracted file
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
	public abstract File decompress(final File archive,
	                                final File targetDirectory) throws NullPointerException,
	                                                           FileNotFoundException,
	                                                           UnsupportedExtensionException,
	                                                           FileAlreadyExistsException,
	                                                           FilePermissionException,
	                                                           IOException;
	
	protected File prepareOutputDirectory(final File archive,
	                                      final File targetDirectory,
	                                      final ArchiveType type) throws NullPointerException,
	                                                             FileNotFoundException,
	                                                             UnsupportedExtensionException,
	                                                             FileAlreadyExistsException,
	                                                             FilePermissionException,
	                                                             IOException {
		PRECONDITIONS: {
			assert type != null;
			
			if (archive == null) {
				throw new NullPointerException("Archive parameter must not be null");
			}
			
			if (!archive.exists()) {
				throw new FileNotFoundException("Archive cannot be found");
			}
			
			if (!archive.isFile()) {
				throw new IllegalArgumentException("Archive is not a file.");
			}
			
			final String extension = FilenameUtils.getExtension(archive.getName());
			
			if (extension == null) {
				throw new UnsupportedExtensionException("Archive does not have an extension.");
			}
			
			final ArchiveType archiveType = CompressionUtils.ArchiveType.forExtension(extension);
			
			SANITY: {
				assert archiveType != null;
			}
			
			if (!type.equals(archiveType)) {
				throw new UnsupportedExtensionException(getClass().getSimpleName() + " does not support '" + extension
				        + "'extensions. Please use '" + archiveType.getDecompressor().getClass().getSimpleName()
				        + "' to decompress '" + archive + "'.");
			}
			
			FileUtils.ensureFilePermissions(archive, FileUtils.READABLE_FILE);
		}
		
		// determine the target directory. if the target directory is null, we use the working directory
		final File outputDirectory = targetDirectory == null
		                                                    ? new File(".")
		                                                    : targetDirectory;
		
		// try to create output directory if it doesn't exist already
		if (!outputDirectory.exists()) {
			final boolean success = outputDirectory.mkdirs();
			
			if (!success) {
				throw new IOException("Could not create output directory '" + outputDirectory + "'.");
			}
		}
		
		// make sure that whatever happened until this point, the output directory is now an existing directory we can
		// write to
		FileUtils.ensureFilePermissions(outputDirectory, FileUtils.WRITABLE_DIR);
		
		return outputDirectory;
	}
	
}
