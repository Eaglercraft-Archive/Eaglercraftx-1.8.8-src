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

package net.lax1dude.eaglercraft.v1_8;

import java.util.HashMap;
import java.util.Map;

import net.lax1dude.eaglercraft.v1_8.internal.IEaglerFilesystem;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformFilesystem;
import net.lax1dude.eaglercraft.v1_8.internal.RamdiskFilesystemImpl;
import net.lax1dude.eaglercraft.v1_8.internal.VFSFilenameIterator;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

public class Filesystem {

	private static final Logger logger = LogManager.getLogger("PlatformFilesystem");

	private static final Map<String,FilesystemHandle> openFilesystems = new HashMap<>();

	public static IEaglerFilesystem getHandleFor(String dbName) {
		FilesystemHandle handle = openFilesystems.get(dbName);
		if(handle != null) {
			++handle.refCount;
			return new FilesystemHandleWrapper(handle);
		}
		IEaglerFilesystem handleImpl = null;
		if(!EagRuntime.getConfiguration().isRamdiskMode()) {
			handleImpl = PlatformFilesystem.initializePersist(dbName);
		}
		if(handleImpl == null) {
			handleImpl = new RamdiskFilesystemImpl(dbName);
		}
		if(handleImpl.isRamdisk()) {
			logger.warn("Using RAMDisk filesystem for database \"{}\", data will not be saved to local storage!", dbName);
		}
		handle = new FilesystemHandle(handleImpl);
		openFilesystems.put(dbName, handle);
		return new FilesystemHandleWrapper(handle);
	}

	public static void closeAllHandles() {
		for(FilesystemHandle handle : openFilesystems.values()) {
			handle.refCount = 0;
			handle.handle.closeHandle();
		}
		openFilesystems.clear();
	}

	private static class FilesystemHandle {

		private final IEaglerFilesystem handle;
		private int refCount;
		
		private FilesystemHandle(IEaglerFilesystem handle) {
			this.handle = handle;
			this.refCount = 1;
		}

	}

	private static class FilesystemHandleWrapper implements IEaglerFilesystem {

		private final FilesystemHandle handle;
		private final IEaglerFilesystem handleImpl;
		private boolean closed;

		private FilesystemHandleWrapper(FilesystemHandle handle) {
			this.handle = handle;
			this.handleImpl = handle.handle;
			this.closed = false;
		}

		@Override
		public String getFilesystemName() {
			return handleImpl.getFilesystemName();
		}

		@Override
		public String getInternalDBName() {
			return handleImpl.getInternalDBName();
		}

		@Override
		public boolean isRamdisk() {
			return handleImpl.isRamdisk();
		}

		@Override
		public boolean eaglerDelete(String pathName) {
			return handleImpl.eaglerDelete(pathName);
		}

		@Override
		public ByteBuffer eaglerRead(String pathName) {
			return handleImpl.eaglerRead(pathName);
		}

		@Override
		public void eaglerWrite(String pathName, ByteBuffer data) {
			handleImpl.eaglerWrite(pathName, data);
		}

		@Override
		public boolean eaglerExists(String pathName) {
			return handleImpl.eaglerExists(pathName);
		}

		@Override
		public boolean eaglerMove(String pathNameOld, String pathNameNew) {
			return handleImpl.eaglerMove(pathNameOld, pathNameNew);
		}

		@Override
		public int eaglerCopy(String pathNameOld, String pathNameNew) {
			return handleImpl.eaglerCopy(pathNameOld, pathNameNew);
		}

		@Override
		public int eaglerSize(String pathName) {
			return handleImpl.eaglerSize(pathName);
		}

		@Override
		public void eaglerIterate(String pathName, VFSFilenameIterator itr, boolean recursive) {
			handleImpl.eaglerIterate(pathName, itr, recursive);
		}

		@Override
		public void closeHandle() {
			if(!closed && handle.refCount > 0) {
				closed = true;
				--handle.refCount;
				if(handle.refCount <= 0) {
					logger.info("Releasing filesystem handle for: \"{}\"", handleImpl.getFilesystemName());
					handleImpl.closeHandle();
					openFilesystems.remove(handleImpl.getFilesystemName());
				}
			}
		}

	}

}