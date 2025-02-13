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

package net.lax1dude.eaglercraft.v1_8.internal;

import java.util.Map;
import java.util.TreeMap;

import net.lax1dude.eaglercraft.v1_8.internal.buffer.ByteBuffer;

public class RamdiskFilesystemImpl implements IEaglerFilesystem {

	protected final String filesystemName;
	protected final Map<String,byte[]> filesystemMap = new TreeMap<>();

	public RamdiskFilesystemImpl(String filesystemName) {
		this.filesystemName = filesystemName;
	}

	@Override
	public String getFilesystemName() {
		return filesystemName;
	}

	@Override
	public String getInternalDBName() {
		return "ramdisk:" + filesystemName;
	}

	@Override
	public boolean isRamdisk() {
		return true;
	}

	@Override
	public boolean eaglerDelete(String pathName) {
		return filesystemMap.remove(pathName) != null;
	}

	@Override
	public ByteBuffer eaglerRead(String pathName) {
		byte[] data = filesystemMap.get(pathName);
		if(data != null) {
			ByteBuffer buf = PlatformRuntime.castPrimitiveByteArray(data);
			if(buf == null) {
				buf = PlatformRuntime.allocateByteBuffer(data.length);
				buf.put(data);
				buf.flip();
			}
			return buf;
		}else {
			return null;
		}
	}

	@Override
	public void eaglerWrite(String pathName, ByteBuffer data) {
		byte[] arr = PlatformRuntime.castNativeByteBuffer(data);
		if(arr == null) {
			arr = new byte[data.remaining()];
			int i = data.position();
			data.get(arr);
			data.position(i);
		}
		filesystemMap.put(pathName, arr);
	}

	@Override
	public boolean eaglerExists(String pathName) {
		return filesystemMap.containsKey(pathName);
	}

	@Override
	public boolean eaglerMove(String pathNameOld, String pathNameNew) {
		byte[] dat = filesystemMap.remove(pathNameOld);
		if(dat != null) {
			filesystemMap.put(pathNameNew, dat);
			return true;
		}
		return false;
	}

	@Override
	public int eaglerCopy(String pathNameOld, String pathNameNew) {
		byte[] dat = filesystemMap.get(pathNameOld);
		if(dat != null) {
			filesystemMap.put(pathNameNew, dat);
			return dat.length;
		}
		return -1;
	}

	@Override
	public int eaglerSize(String pathName) {
		byte[] dat = filesystemMap.get(pathName);
		return dat != null ? dat.length : -1;
	}

	@Override
	public void eaglerIterate(String pathName, VFSFilenameIterator itr, boolean recursive) {
		if(!recursive) {
			eaglerIterate(pathName, new VFSFilenameIteratorNonRecursive(itr,
					VFSFilenameIteratorNonRecursive.countSlashes(pathName) + 1), true);
		}else {
			boolean b = pathName.length() == 0;
			for(String key : filesystemMap.keySet()) {
				if(b || key.startsWith(pathName)) {
					itr.next(key);
				}
			}
		}
	}

	@Override
	public void closeHandle() {
		filesystemMap.clear();
	}

}