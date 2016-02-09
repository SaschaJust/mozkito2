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
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.io.IOUtils;

import org.mozkito.skeleton.io.CompressionUtils.ArchiveType;

/**
 * @author Sascha Just <sascha.just@mozkito.org>
 * 
 */
public class JarDecompressor extends ArchiveDecompressor {
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.mozkito.skeleton.io.decompressors.IDecompressor#decompress(java.io.File, java.io.File)
	 */
	@Override
	public File decompress(final File archive,
	                       final File target) throws IOException {
		final File outputDirectory = prepareOutputDirectory(archive, target, ArchiveType.JAR);
		
		SANITY: {
			assert outputDirectory != null;
		}
		
		try (final JarFile jarFile = new JarFile(archive)) {
			final Enumeration<JarEntry> enumeration = jarFile.entries();
			
			while (enumeration.hasMoreElements()) {
				final JarEntry entry = enumeration.nextElement();
				final InputStream content = jarFile.getInputStream(entry);
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
					
					try (FileOutputStream fileOutputStream = new FileOutputStream(targetFile)) {
						IOUtils.copy(content, fileOutputStream);
					}
				}
			}
		}
		
		return outputDirectory;
	}
	
}
