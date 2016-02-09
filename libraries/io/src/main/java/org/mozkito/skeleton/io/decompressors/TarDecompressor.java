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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;

import org.mozkito.skeleton.io.CompressionUtils.ArchiveType;

/**
 * @author Sascha Just <sascha.just@mozkito.org>
 * 
 */
public class TarDecompressor extends ArchiveDecompressor {
	
	@Override
	public File decompress(final File archive,
	                       final File targetDirectory) throws IOException {
		
		final File outputDirectory = prepareOutputDirectory(archive, targetDirectory, ArchiveType.TAR);
		
		SANITY: {
			assert outputDirectory != null;
		}
		
		try (TarArchiveInputStream tarIn = new TarArchiveInputStream(new FileInputStream(archive))) {
			TarArchiveEntry entry = null;
			while ((entry = tarIn.getNextTarEntry()) != null) {
				final File targetFile = new File(outputDirectory, entry.getName());
				
				if (entry.isDirectory()) {
					if (!targetFile.exists() && !targetFile.mkdirs() || !targetFile.isDirectory()) {
						throw new IOException("Failed creating directory: "
						        + targetFile.getParentFile().getAbsolutePath());
					}
				} else {
					if (targetFile.getParentFile() != null && !targetFile.getParentFile().exists()) {
						if (!targetFile.getParentFile().mkdirs()) {
							throw new IOException("Failed creating directory: "
							        + targetFile.getParentFile().getAbsolutePath());
						}
					}
					
					final long size = entry.getSize();
					byte[] content = null;
					try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(targetFile))) {
						
						while (size > Integer.MAX_VALUE) {
							content = new byte[Integer.MAX_VALUE];
							tarIn.read(content);
							outputStream.write(content);
						}
						
						SANITY: {
							assert size < Integer.MAX_VALUE;
						}
						
						if (size > 0) {
							content = new byte[(int) size];
							tarIn.read(content);
							outputStream.write(content);
						}
					}
				}
			}
		}
		
		// return the output file after successful write
		return outputDirectory;
		
	}
}
