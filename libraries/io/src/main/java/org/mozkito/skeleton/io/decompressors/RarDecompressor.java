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
import java.io.FileOutputStream;
import java.io.IOException;

import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.impl.FileVolumeManager;
import com.github.junrar.rarfile.FileHeader;

import org.mozkito.skeleton.io.CompressionUtils.ArchiveType;

/**
 * @author Sascha Just <sascha.just@mozkito.org>
 * 
 */
public class RarDecompressor extends ArchiveDecompressor {
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.io.decompressors.IDecompressor#decompress(java.io.File, java.io.File)
	 */
	@Override
	public File decompress(final File archive,
	                       final File target) throws IOException {
		PRECONDITIONS: {
			// none
		}
		
		try {
			final File outputDirectory = prepareOutputDirectory(archive, target, ArchiveType.RAR);
			
			Archive a = null;
			try {
				a = new Archive(new FileVolumeManager(archive));
				FileHeader entry = null;
				
				while ((entry = a.nextFileHeader()) != null) {
					final File targetFile = new File(outputDirectory, entry.getFileNameString().replace("\\",
					                                                                                    File.separator));
					
					if (entry.isDirectory()) {
						if (!targetFile.exists() && !targetFile.mkdirs() || !targetFile.isDirectory()) {
							throw new IOException("Failed creating directory: " + targetFile.getAbsolutePath());
						}
					} else {
						// junrar does not explicitly extract parent directories...
						if (!targetFile.getParentFile().exists() && !targetFile.getParentFile().mkdirs()
						        || !targetFile.getParentFile().isDirectory()) {
							throw new IOException("Failed creating directory: " + targetFile.getAbsolutePath());
						}
						
						final FileOutputStream os = new FileOutputStream(targetFile);
						a.extractFile(entry, os);
						os.close();
					}
				}
				
			} catch (final RarException e) {
				throw new IOException(e);
			} finally {
				if (a != null) {
					a.close();
				}
			}
			
			return outputDirectory;
		} finally {
			POSTCONDITIONS: {
				// none
			}
		}
	}
	
}
