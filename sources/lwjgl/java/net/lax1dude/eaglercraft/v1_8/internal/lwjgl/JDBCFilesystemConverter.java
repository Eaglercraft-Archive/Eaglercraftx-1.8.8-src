/*
 * Copyright (c) 2024 lax1dude. All Rights Reserved.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 */

package net.lax1dude.eaglercraft.v1_8.internal.lwjgl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import net.lax1dude.eaglercraft.v1_8.internal.IEaglerFilesystem;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformRuntime;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.vfs2.EaglerFileSystemException;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

public class JDBCFilesystemConverter {

	private static final Logger logger = LogManager.getLogger("JDBCFilesystemConverter");

	public static void convertFilesystem(String title, File oldFS, IEaglerFilesystem newFS, boolean deleteOld) {
		FilesystemConvertingDialog progressDialog = new FilesystemConvertingDialog(title);
		try {
			progressDialog.setProgressIndeterminate(true);
			progressDialog.setLocationRelativeTo(null);
			progressDialog.setVisible(true);
			
			String slug = oldFS.getAbsolutePath();
			List<String> filesToCopy = new ArrayList<>();
			logger.info("Discovering files to convert...");
			iterateFolder(slug.length(), oldFS, filesToCopy);
			logger.info("Found {} files in the old directory", filesToCopy.size());
			
			progressDialog.setProgressIndeterminate(false);
			progressDialog.setProgressValue(0);
			
			int progCounter = 0;
			int lastProgUpdate = 0;
			byte[] copyArray = new byte[4096];
			
			int l = filesToCopy.size();
			for(int i = 0; i < l; ++i) {
				String str = filesToCopy.get(i);
				File f = new File(oldFS, str);
				try(InputStream is = new FileInputStream(f)) {
					ByteBuffer copyBuffer = PlatformRuntime.allocateByteBuffer((int)f.length());
					try {
						int j;
						while(copyBuffer.hasRemaining() && (j = is.read(copyArray, 0, copyArray.length)) != -1) {
							copyBuffer.put(copyArray, 0, j);
						}
						copyBuffer.flip();
						progCounter += copyBuffer.remaining();
						newFS.eaglerWrite(str, copyBuffer);
					}finally {
						PlatformRuntime.freeByteBuffer(copyBuffer);
					}
					if(progCounter - lastProgUpdate > 25000) {
						lastProgUpdate = progCounter;
						logger.info("Converted {}/{} files, {} bytes to JDBC format...", (i + 1), l, progCounter);
					}
				}catch(IOException ex) {
					throw new EaglerFileSystemException("Failed to convert file: \"" + f.getAbsolutePath() + "\"", ex);
				}
				progressDialog.setProgressValue(i * 512 / (l - 1));
			}

			logger.info("Converted {}/{} files successfully!", l, l);

			if(deleteOld) {
				logger.info("Deleting old filesystem...");
				progressDialog.setProgressIndeterminate(true);
				deleteOldFolder(oldFS);
				logger.info("Delete complete!");
			}
		}finally {
			progressDialog.setVisible(false);
			progressDialog.dispose();
		}
	}

	private static void iterateFolder(int slug, File file, List<String> ret) {
		File[] f = file.listFiles();
		if(f == null) {
			return;
		}
		for(int i = 0; i < f.length; ++i) {
			File ff = f[i];
			if(ff.isDirectory()) {
				iterateFolder(slug, ff, ret);
			}else {
				String str = ff.getAbsolutePath();
				if(str.length() > slug) {
					str = str.substring(slug).replace('\\', '/');
					if(str.startsWith("/")) {
						str = str.substring(1);
					}
					ret.add(str);
				}
			}
		}
	}

	private static void deleteOldFolder(File file) {
		File[] f = file.listFiles();
		for(int i = 0; i < f.length; ++i) {
			if(f[i].isDirectory()) {
				deleteOldFolder(f[i]);
			}else {
				f[i].delete();
			}
		}
		file.delete();
	}
}