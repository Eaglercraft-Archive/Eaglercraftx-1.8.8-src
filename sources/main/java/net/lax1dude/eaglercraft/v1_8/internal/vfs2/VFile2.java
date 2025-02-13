/*
 * Copyright (c) 2023-2024 lax1dude. All Rights Reserved.
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

package net.lax1dude.eaglercraft.v1_8.internal.vfs2;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import net.lax1dude.eaglercraft.v1_8.EagUtils;
import net.lax1dude.eaglercraft.v1_8.internal.IEaglerFilesystem;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformRuntime;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.ByteBuffer;

public class VFile2 {

	public static final String pathSeperator = "/";
	public static final String[] altPathSeperator = new String[] { "\\" };
	
	static IEaglerFilesystem primaryFilesystem = null;
	
	public static void setPrimaryFilesystem(IEaglerFilesystem fs) {
		primaryFilesystem = fs;
	}
	
	public static String normalizePath(String p) {
		for(int i = 0; i < altPathSeperator.length; ++i) {
			p = p.replace(altPathSeperator[i], pathSeperator);
		}
		if(p.startsWith(pathSeperator)) {
			p = p.substring(1);
		}
		if(p.endsWith(pathSeperator)) {
			p = p.substring(0, p.length() - pathSeperator.length());
		}
		return p;
	}
	
	public static String[] splitPath(String p) {
		String[] pth = normalizePath(p).split(pathSeperator);
		for(int i = 0; i < pth.length; ++i) {
			pth[i] = pth[i].trim();
		}
		return pth;
	}
	
	protected String path;
	protected IEaglerFilesystem myFilesystem;
	protected Supplier<IEaglerFilesystem> myFilesystemProvider;
	
	public static String createPath(Object... p) {
		ArrayList<String> r = new ArrayList<>();
		for(int i = 0; i < p.length; ++i) {
			if(p[i] == null) {
				continue;
			}
			String gg = p[i].toString();
			if(gg == null) {
				continue;
			}
			String[] parts = splitPath(gg);
			for(int j = 0; j < parts.length; ++j) {
				if(parts[j] == null || parts[j].equals(".")) {
					continue;
				}else if(parts[j].equals("..") && r.size() > 0) {
					int k = r.size() - 1;
					if(!r.get(k).equals("..")) {
						r.remove(k);
					}else {
						r.add("..");
					}
				}else {
					r.add(parts[j]);
				}
			}
		}
		if(r.size() > 0) {
			StringBuilder s = new StringBuilder();
			for(int i = 0; i < r.size(); ++i) {
				if(i > 0) {
					s.append(pathSeperator);
				}
				s.append(r.get(i));
			}
			return s.toString();
		}else {
			return null;
		}
	}
	
	public static VFile2 create(IEaglerFilesystem fs, Object... path) {
		return new VFile2(createPath(path), fs);
	}
	
	public static VFile2 create(Supplier<IEaglerFilesystem> fs, Object... path) {
		return new VFile2(createPath(path), fs);
	}
	
	public VFile2(Object... path) {
		this(createPath(path), primaryFilesystem);
	}
	
	private VFile2(String path, IEaglerFilesystem fs) {
		this.path = path;
		this.myFilesystem = fs;
	}
	
	private VFile2(String path, Supplier<IEaglerFilesystem> fs) {
		this.path = path;
		this.myFilesystemProvider = fs;
	}
	
	protected IEaglerFilesystem getFS() {
		if(myFilesystem == null) {
			if(myFilesystemProvider != null) {
				myFilesystem = myFilesystemProvider.get();
			}else {
				myFilesystem = primaryFilesystem;
			}
			if(myFilesystem == null) {
				throw new IllegalStateException("The filesystem has not been initialized yet!");
			}
		}
		return myFilesystem;
	}
	
	public InputStream getInputStream() {
		assertNotRelative();
		return new VFileInputStream(getFS().eaglerRead(path));
	}
	
	public OutputStream getOutputStream() {
		assertNotRelative();
		return new VFileOutputStream(this);
	}
	
	public String toString() {
		return path;
	}
	
	public boolean isRelative() {
		return path == null || path.contains("..");
	}
	
	public void assertNotRelative() {
		if(isRelative()) throw new EaglerFileSystemException("Relative paths are not allowed: " + path);
	}
	
	public boolean canRead() {
		return !isRelative() && getFS().eaglerExists(path);
	}
	
	public String getPath() {
		return path.equals("unnamed") ? null : path;
	}
	
	public String getName() {
		int i = path.lastIndexOf(pathSeperator);
		return i == -1 ? path : path.substring(i + 1);
	}
	
	public static String getNameFromPath(String path) {
		path = normalizePath(path);
		int i = path.lastIndexOf(pathSeperator);
		return i == -1 ? path : path.substring(i + 1);
	}
	
	public boolean canWrite() {
		return !isRelative();
	}
	
	public String getParent() {
		if(path == null) {
			return null;
		}
		int i = path.lastIndexOf(pathSeperator);
		return i == -1 ? ".." : path.substring(0, i);
	}
	
	public int hashCode() {
		return path == null ? 0 : path.hashCode();
	}
	
	public boolean equals(Object o) {
		return path != null && o != null && (o instanceof VFile2) && path.equals(((VFile2)o).path);
	}
	
	public boolean exists() {
		return !isRelative() && getFS().eaglerExists(path);
	}
	
	public boolean delete() {
		return !isRelative() && getFS().eaglerDelete(path);
	}
	
	public boolean renameTo(String p) {
		if(!isRelative() && getFS().eaglerMove(path, p)) {
			return true;
		}
		return false;
	}
	
	public boolean renameTo(VFile2 p) {
		return renameTo(p.path);
	}
	
	public int length() {
		return isRelative() ? -1 : getFS().eaglerSize(path);
	}
	
	public byte[] getAllBytes() {
		assertNotRelative();
		if(!exists()) {
			return null;
		}
		ByteBuffer readBuffer = getFS().eaglerRead(path);
		byte[] copyBuffer = PlatformRuntime.castNativeByteBuffer(readBuffer);
		if(copyBuffer != null) {
			return copyBuffer;
		}
		try {
			copyBuffer = new byte[readBuffer.remaining()];
			readBuffer.get(copyBuffer);
			return copyBuffer;
		}finally {
			PlatformRuntime.freeByteBuffer(readBuffer);
		}
	}
	
	public String getAllChars() {
		assertNotRelative();
		if(!exists()) {
			return null;
		}
		return new String(getAllBytes(), StandardCharsets.UTF_8);
	}
	
	public String[] getAllLines() {
		assertNotRelative();
		if(!exists()) {
			return null;
		}
		return EagUtils.linesArray(new String(getAllBytes(), StandardCharsets.UTF_8));
	}
	
	public void setAllChars(String bytes) {
		setAllBytes(bytes.getBytes(StandardCharsets.UTF_8));
	}
	
	public void setAllBytes(byte[] bytes) {
		assertNotRelative();
		ByteBuffer copyBuffer = PlatformRuntime.castPrimitiveByteArray(bytes);
		if(copyBuffer != null) {
			getFS().eaglerWrite(path, copyBuffer);
			return;
		}
		copyBuffer = PlatformRuntime.allocateByteBuffer(bytes.length);
		try {
			copyBuffer.put(bytes);
			copyBuffer.flip();
			getFS().eaglerWrite(path, copyBuffer);
		}finally {
			PlatformRuntime.freeByteBuffer(copyBuffer);
		}
	}

	public void iterateFiles(VFSIterator2 itr, boolean recursive) {
		assertNotRelative();
		IEaglerFilesystem fs = getFS();
		fs.eaglerIterate(path, new VFSFilenameIteratorImpl(fs, itr), recursive);
	}

	public List<String> listFilenames(boolean recursive) {
		List<String> ret = new ArrayList<>();
		getFS().eaglerIterate(path, new VFSListFilenamesIteratorImpl(ret), recursive);
		return ret;
	}

	public List<VFile2> listFiles(boolean recursive) {
		List<VFile2> ret = new ArrayList<>();
		IEaglerFilesystem fs = getFS();
		fs.eaglerIterate(path, new VFSListFilesIteratorImpl(fs, ret), recursive);
		return ret;
	}

	public static int copyFile(VFile2 src, VFile2 dst) {
		src.assertNotRelative();
		dst.assertNotRelative();
		IEaglerFilesystem sfs = src.getFS();
		if(sfs != dst.getFS()) {
			throw new UnsupportedOperationException("Cannot copy file between filesystems!");
		}
		return sfs.eaglerCopy(src.path, dst.path);
	}
}