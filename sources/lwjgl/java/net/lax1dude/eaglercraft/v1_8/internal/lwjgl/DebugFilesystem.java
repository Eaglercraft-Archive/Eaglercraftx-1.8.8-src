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
import java.io.FileOutputStream;
import java.io.IOException;

import net.lax1dude.eaglercraft.v1_8.internal.IEaglerFilesystem;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformFilesystem;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformRuntime;
import net.lax1dude.eaglercraft.v1_8.internal.VFSFilenameIterator;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.vfs2.EaglerFileSystemException;
import net.lax1dude.eaglercraft.v1_8.internal.vfs2.VFSIterator2.BreakLoop;

public class DebugFilesystem implements IEaglerFilesystem {

	public static DebugFilesystem initialize(String fsName, File filesystemRoot) {
		if(!filesystemRoot.isDirectory() && !filesystemRoot.mkdirs()) {
			throw new EaglerFileSystemException("Could not create directory for virtual filesystem: " + filesystemRoot.getAbsolutePath());
		}
		return new DebugFilesystem(fsName, filesystemRoot);
	}

	private final File filesystemRoot;
	private final String fsName;

	private DebugFilesystem(String fsName, File root) {
		this.fsName = fsName;
		this.filesystemRoot = root;
	}

	@Override
	public String getFilesystemName() {
		return fsName;
	}

	@Override
	public String getInternalDBName() {
		return "desktopruntime:" + filesystemRoot.getAbsolutePath();
	}

	@Override
	public boolean eaglerDelete(String pathName) {
		File f = getJREFile(pathName);
		if(!f.exists()) {
			PlatformFilesystem.logger.warn("Tried to delete file that doesn't exist: \"{}\"", pathName);
			return false;
		}
		if(f.delete()) {
			deleteParentIfEmpty(f);
			return true;
		}
		return false;
	}

	@Override
	public ByteBuffer eaglerRead(String pathName) {
		File f = getJREFile(pathName);
		if(f.isFile()) {
			long fileSize = f.length();
			if(fileSize > 2147483647L) throw new EaglerFileSystemException("Too large: " + fileSize + " @ " + f.getAbsolutePath());
			ByteBuffer buf = PlatformRuntime.allocateByteBuffer((int)fileSize);
			try(FileInputStream is = new FileInputStream(f)) {
				byte[] copyBuffer = new byte[4096];
				int i;
				while((i = is.read(copyBuffer, 0, copyBuffer.length)) != -1) {
					buf.put(copyBuffer, 0, i);
				}
				if(buf.remaining() > 0) {
					throw new EaglerFileSystemException("ERROR: " + buf.remaining() + " bytes are remaining after reading: " + f.getAbsolutePath());
				}
				buf.flip();
				ByteBuffer tmp = buf;
				buf = null;
				return tmp;
			}catch (IOException e) {
				throw new EaglerFileSystemException("Failed to read: " + f.getAbsolutePath(), e);
			}catch(IndexOutOfBoundsException ex) {
				throw new EaglerFileSystemException("ERROR: Expected " + fileSize + " bytes, buffer overflow reading: " + f.getAbsolutePath(), ex);
			}finally {
				if(buf != null) {
					PlatformRuntime.freeByteBuffer(buf);
				}
			}
		}else {
			PlatformFilesystem.logger.warn("Tried to read file that doesn't exist: \"{}\"", f.getAbsolutePath());
			return null;
		}
	}

	@Override
	public void eaglerWrite(String pathName, ByteBuffer data) {
		File f = getJREFile(pathName);
		File p = f.getParentFile();
		if(!p.isDirectory()) {
			if(!p.mkdirs()) {
				throw new EaglerFileSystemException("Could not create parent directory: " + p.getAbsolutePath());
			}
		}
		try(FileOutputStream fos = new FileOutputStream(f)) {
			byte[] copyBuffer = new byte[Math.min(4096, data.remaining())];
			int i;
			while((i = data.remaining()) > 0) {
				if(i > copyBuffer.length) {
					i = copyBuffer.length;
				}
				data.get(copyBuffer, 0, i);
				fos.write(copyBuffer, 0, i);
			}
		}catch (IOException e) {
			throw new EaglerFileSystemException("Failed to write: " + f.getAbsolutePath(), e);
		}
	}

	@Override
	public boolean eaglerExists(String pathName) {
		return getJREFile(pathName).isFile();
	}

	@Override
	public boolean eaglerMove(String pathNameOld, String pathNameNew) {
		File f1 = getJREFile(pathNameOld);
		File f2 = getJREFile(pathNameNew);
		if(f2.exists()) {
			PlatformFilesystem.logger.warn("Tried to rename file \"{}\" to \"{}\" which already exists! File will be replaced");
			if(!f2.delete()) {
				return false;
			}
		}
		if(f1.renameTo(f2)) {
			deleteParentIfEmpty(f1);
			return true;
		}
		return false;
	}

	@Override
	public int eaglerCopy(String pathNameOld, String pathNameNew) {
		File f1 = getJREFile(pathNameOld);
		File f2 = getJREFile(pathNameNew);
		if(!f1.isFile()) {
			return -1;
		}
		if(f2.isDirectory()) {
			throw new EaglerFileSystemException("Destination file is a directory: " + f2.getAbsolutePath());
		}
		File p = f2.getParentFile();
		if(!p.isDirectory()) {
			if(!p.mkdirs()) {
				throw new EaglerFileSystemException("Could not create parent directory: " + p.getAbsolutePath());
			}
		}
		int sz = 0;
		try(FileInputStream is = new FileInputStream(f1)) {
			try(FileOutputStream os = new FileOutputStream(f2)) {
				byte[] copyBuffer = new byte[4096];
				int i;
				while((i = is.read(copyBuffer, 0, copyBuffer.length)) != -1) {
					os.write(copyBuffer, 0, i);
					sz += i;
				}
			}
		}catch (IOException e) {
			throw new EaglerFileSystemException("Failed to copy \"" + f1.getAbsolutePath() + "\" to file \"" + f2.getAbsolutePath() + "\"", e);
		}
		return sz;
	}

	@Override
	public int eaglerSize(String pathName) {
		File f = getJREFile(pathName);
		if(f.isFile()) {
			long fileSize = f.length();
			if(fileSize > 2147483647L) throw new EaglerFileSystemException("Too large: " + fileSize + " @ " + f.getAbsolutePath());
			return (int)fileSize;
		}else {
			return -1;
		}
	}

	@Override
	public void eaglerIterate(String pathName, VFSFilenameIterator itr, boolean recursive) {
		try {
			iterateFile(pathName, getJREFile(pathName), itr, recursive);
		}catch(BreakLoop ex) {
		}
	}

	private void iterateFile(String pathName, File f, VFSFilenameIterator itr, boolean recursive) {
		if(!f.exists()) {
			return;
		}
		if(!f.isDirectory()) {
			itr.next(pathName);
			return;
		}
		File[] fa = f.listFiles();
		for(int i = 0; i < fa.length; ++i) {
			File ff = fa[i];
			String fn = pathName + "/" + ff.getName();
			if(ff.isDirectory()) {
				if(recursive) {
					iterateFile(fn, ff, itr, true);
				}
			}else {
				itr.next(fn);
			}
		}
	}

	private File getJREFile(String path) {
		return new File(filesystemRoot, path);
	}

	private void deleteParentIfEmpty(File f) {
		String[] s;
		while((f = f.getParentFile()) != null && (s = f.list()) != null && s.length == 0) {
			f.delete();
		}
	}

	@Override
	public boolean isRamdisk() {
		return false;
	}

	@Override
	public void closeHandle() {
		
	}

}