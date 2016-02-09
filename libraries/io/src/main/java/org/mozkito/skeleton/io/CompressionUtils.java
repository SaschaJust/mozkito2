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

package org.mozkito.skeleton.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.commons.io.FilenameUtils;

import org.mozkito.skeleton.commons.JavaUtils;
import org.mozkito.skeleton.io.decompressors.Bzip2Decompressor;
import org.mozkito.skeleton.io.decompressors.GzipDecompressor;
import org.mozkito.skeleton.io.decompressors.IDecompressor;
import org.mozkito.skeleton.io.decompressors.JarDecompressor;
import org.mozkito.skeleton.io.decompressors.Pack200Decompressor;
import org.mozkito.skeleton.io.decompressors.RarDecompressor;
import org.mozkito.skeleton.io.decompressors.SevenZipDecompressor;
import org.mozkito.skeleton.io.decompressors.TarDecompressor;
import org.mozkito.skeleton.io.decompressors.XzipDecompressor;
import org.mozkito.skeleton.io.decompressors.ZipDecompressor;
import org.mozkito.skeleton.io.exceptions.UnsupportedExtensionException;

/**
 * The Class CompressionUtils.
 * 
 * @author Sascha Just <sascha.just@mozkito.org>
 */
public class CompressionUtils {
	
	/**
	 * The Enum ArchiveType.
	 */
	public static enum ArchiveType {
		
		/** The tar. */
		TAR (new TarDecompressor(), "tar"),
		/** The zip. */
		ZIP (new ZipDecompressor(), "zip"),
		/** The jar. */
		JAR (new JarDecompressor(), "jar"),
		/** The BZI p2. */
		BZIP2 (new Bzip2Decompressor(), "bz", "bz2", "bzip2"),
		/** The gzip. */
		GZIP (new GzipDecompressor(), "gz", "gzip"),
		/** The xz. */
		XZ (new XzipDecompressor(), "xz", "xzip"),
		/** The sevenz. */
		SEVENZ (new SevenZipDecompressor(), "7z"),
		/** The rar. */
		RAR (new RarDecompressor(), "rar"),
		/** The PAC k200. */
		PACK200 (new Pack200Decompressor(), "pack");
		
		/**
		 * For extension.
		 * 
		 * @param extension
		 *            the extension
		 * @return the archive type
		 * @throws UnsupportedExtensionException
		 *             the unsupported extension exception
		 */
		public static ArchiveType forExtension(final String extension) throws UnsupportedExtensionException {
			PRECONDITIONS: {
				if (extension == null) {
					throw new NullPointerException();
				}
			}
			
			for (final ArchiveType type : values()) {
				SANITY: {
					assert type.extensions != null;
				}
				for (final String archiveExtension : type.extensions) {
					if (extension.equalsIgnoreCase(archiveExtension)) {
						return type;
					}
				}
			}
			throw new UnsupportedExtensionException("Extension '" + extension
			        + "' not supported. Current supported archive types: " + JavaUtils.arrayToString(values()));
		}
		
		/** The decompressor. */
		private IDecompressor iDecompressor;
		
		/** The extensions. */
		private String[]      extensions;
		
		/**
		 * Instantiates a new archive type.
		 * 
		 * @param iDecompressor
		 *            the decompressor
		 * @param extensions
		 *            the extensions
		 */
		private ArchiveType(final IDecompressor iDecompressor, final String... extensions) {
			PRECONDITIONS: {
				if (iDecompressor == null) {
					throw new NullPointerException();
				}
				if (extensions == null) {
					throw new NullPointerException();
				}
				if (extensions.length == 0) {
					throw new IllegalArgumentException();
				}
			}
			
			this.iDecompressor = iDecompressor;
			this.extensions = extensions;
		}
		
		/**
		 * Gets the decompressor.
		 * 
		 * @return the decompressor
		 */
		public final IDecompressor getDecompressor() {
			PRECONDITIONS: {
				assert this.iDecompressor != null;
			}
			
			return this.iDecompressor;
		}
	}
	
	/** The Constant X_COMPRESS. */
	private static final String X_COMPRESS = "x-compress";
	
	/**
	 * Decompress.
	 * 
	 * @param archive
	 *            the archive
	 * @param targetDirectory
	 *            the target directory
	 * @return the file
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static File decompress(final File archive,
	                              final File targetDirectory) throws IOException {
		final String extension = FilenameUtils.getExtension(archive.getName());
		final ArchiveType type = ArchiveType.forExtension(extension);
		final IDecompressor decompressor = type.getDecompressor();
		return decompressor.decompress(archive, targetDirectory);
	}
	
	/**
	 * Checks if is archive.
	 * 
	 * @param file
	 *            the file
	 * @return true, if is archive
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static boolean isArchive(final File file) throws IOException {
		final String mimeType = Files.probeContentType(file.toPath());
		return X_COMPRESS.equals(mimeType.toUpperCase());
	}
	
	/**
	 * Checks if is supported type.
	 * 
	 * @param archive
	 *            the archive
	 * @return true, if is supported type
	 */
	public static boolean isSupportedType(final File archive) {
		try {
			ArchiveType.forExtension(FilenameUtils.getExtension(archive.getName()));
			return true;
		} catch (final UnsupportedExtensionException e) {
			return false;
		}
		
	}
	
	/**
	 * Instantiates a new compression utils.
	 * 
	 * @param ignore
	 *            the ignore
	 */
	private CompressionUtils(final Void ignore) {
		// avoid instantion
	}
}
