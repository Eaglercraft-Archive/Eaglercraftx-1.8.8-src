package net.lax1dude.eaglercraft.v1_8.internal.vfs;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.lax1dude.eaglercraft.v1_8.EaglerInputStream;
import org.teavm.interop.Async;
import org.teavm.interop.AsyncCallback;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.dom.events.EventListener;
import net.lax1dude.eaglercraft.v1_8.internal.indexeddb.EventHandler;
import net.lax1dude.eaglercraft.v1_8.internal.indexeddb.IDBCountRequest;
import net.lax1dude.eaglercraft.v1_8.internal.indexeddb.IDBCursor;
import net.lax1dude.eaglercraft.v1_8.internal.indexeddb.IDBCursorRequest;
import net.lax1dude.eaglercraft.v1_8.internal.indexeddb.IDBDatabase;
import net.lax1dude.eaglercraft.v1_8.internal.indexeddb.IDBFactory;
import net.lax1dude.eaglercraft.v1_8.internal.indexeddb.IDBGetRequest;
import net.lax1dude.eaglercraft.v1_8.internal.indexeddb.IDBObjectStoreParameters;
import net.lax1dude.eaglercraft.v1_8.internal.indexeddb.IDBOpenDBRequest;
import net.lax1dude.eaglercraft.v1_8.internal.indexeddb.IDBRequest;
import net.lax1dude.eaglercraft.v1_8.internal.indexeddb.IDBTransaction;
import net.lax1dude.eaglercraft.v1_8.internal.indexeddb.IDBVersionChangeEvent;
import org.teavm.jso.typedarrays.ArrayBuffer;
import org.teavm.jso.typedarrays.Uint8Array;

/**
 * Copyright (c) 2022-2023 LAX1DUDE. All Rights Reserved.
 *
 * WITH THE EXCEPTION OF PATCH FILES, MINIFIED JAVASCRIPT, AND ALL FILES
 * NORMALLY FOUND IN AN UNMODIFIED MINECRAFT RESOURCE PACK, YOU ARE NOT ALLOWED
 * TO SHARE, DISTRIBUTE, OR REPURPOSE ANY FILE USED BY OR PRODUCED BY THE
 * SOFTWARE IN THIS REPOSITORY WITHOUT PRIOR PERMISSION FROM THE PROJECT AUTHOR.
 *
 * NOT FOR COMMERCIAL OR MALICIOUS USE
 *
 * (please read the 'LICENSE' file this repo's root directory for more info)
 *
 */

public class VirtualFilesystem {
	
	protected static class VirtualOutputStream extends ByteArrayOutputStream {
		private final VFSFile file;
		
		protected VirtualOutputStream(VFSFile file) {
			this.file = file;
		}

		public void close() throws IOException {
			if(!file.setAllBytes(super.toByteArray(), false)) {
				throw new IOException("Could not close stream and write to \"" + file.filePath + "\" on VFS \"" + file.virtualFilesystem.database + "\" (the file was probably deleted)");
			}
		}
	}
	
	public static class VFSFile {

		public final VirtualFilesystem virtualFilesystem;
		protected boolean cacheEnabled;
		protected String filePath;
		protected int fileSize = -1;
		protected boolean hasBeenDeleted = false;
		protected boolean hasBeenAccessed = false;
		protected boolean exists = false;

		protected byte[] cache = null;
		protected long cacheHit;
		
		protected VFSFile(VirtualFilesystem vfs, String filePath, boolean cacheEnabled) {
			this.virtualFilesystem = vfs;
			this.filePath = filePath;
			this.cacheHit = System.currentTimeMillis();
			if(cacheEnabled) {
				setCacheEnabled();
			}
		}
		
		public boolean equals(Object o) {
			return (o instanceof VFSFile) && ((VFSFile)o).filePath.equals(filePath);
		}
		
		public int hashCode() {
			return filePath.hashCode();
		}
		
		public String getPath() {
			return filePath;
		}
		
		public int getSize() {
			cacheHit = System.currentTimeMillis();
			if(fileSize < 0) {
				if(cacheEnabled) {
					byte[] b = getAllBytes(false);
					if(b != null) {
						fileSize = b.length;
					}
				}else {
					ArrayBuffer dat = AsyncHandlers.readWholeFile(virtualFilesystem.indexeddb, filePath);
					if(dat != null) {
						fileSize = dat.getByteLength();
					}
				}
			}
			return fileSize;
		}
		
		public InputStream getInputStream() {
			byte[] dat = getAllBytes(false);
			if(dat == null) {
				return null;
			}
			return new EaglerInputStream(dat);
		}
		
		public OutputStream getOutputStream() {
			return new VirtualOutputStream(this);
		}
		
		public void getBytes(int fileOffset, byte[] array, int offset, int length) {
			if(hasBeenDeleted) {
				throw new ArrayIndexOutOfBoundsException("file '" + filePath + "' has been deleted");
			}else if(hasBeenAccessed && !exists) {
				throw new ArrayIndexOutOfBoundsException("file '" + filePath + "' does not exist");
			}
			cacheHit = System.currentTimeMillis();
			if(cacheEnabled && cache != null) {
				System.arraycopy(cache, fileOffset, array, offset, length);
			}else {
				ArrayBuffer aa = AsyncHandlers.readWholeFile(virtualFilesystem.indexeddb, filePath);
				hasBeenAccessed = true;
				if(aa != null) {
					exists = true;
				}else {
					exists = false;
					throw new ArrayIndexOutOfBoundsException("file '" + filePath + "' does not exist");
				}
				Uint8Array a = Uint8Array.create(aa);
				this.fileSize = a.getByteLength();
				if(cacheEnabled) {
					cache = new byte[fileSize];
					for(int i = 0; i < fileSize; ++i) {
						cache[i] = (byte)a.get(i);
					}
				}
				if(a.getLength() < fileOffset + length) {
					throw new ArrayIndexOutOfBoundsException("file '" + filePath + "' size was "+a.getLength()+" but user tried to read index "+(fileOffset + length - 1));
				}
				for(int i = 0; i < length; ++i) {
					array[i + offset] = (byte)a.get(i + fileOffset);
				}
			}
		}
		
		public void setCacheEnabled() {
			if(!cacheEnabled && !hasBeenDeleted && !(hasBeenAccessed && !exists)) {
				cacheHit = System.currentTimeMillis();
				cache = getAllBytes(false);
				cacheEnabled = true;
			}
		}
		
		public byte[] getAllBytes() {
			return getAllBytes(false);
		}
		
		public String getAllChars() {
			return utf8(getAllBytes(false));
		}
		
		public String[] getAllLines() {
			return lines(getAllChars());
		}
		
		public byte[] getAllBytes(boolean copy) {
			if(hasBeenDeleted || (hasBeenAccessed && !exists)) {
				return null;
			}
			cacheHit = System.currentTimeMillis();
			if(cacheEnabled && cache != null) {
				byte[] b = cache;
				if(copy) {
					b = new byte[cache.length];
					System.arraycopy(cache, 0, b, 0, cache.length);
				}
				return b;
			}else {
				hasBeenAccessed = true;
				ArrayBuffer b = AsyncHandlers.readWholeFile(virtualFilesystem.indexeddb, filePath);
				if(b != null) {
					exists = true;
				}else {
					exists = false;
					return null;
				}
				Uint8Array a = Uint8Array.create(b);
				this.fileSize = a.getByteLength();
				byte[] array = new byte[fileSize];
				for(int i = 0; i < a.getByteLength(); ++i) {
					array[i] = (byte)a.get(i);
				}
				if(cacheEnabled) {
					if(copy) {
						cache = new byte[fileSize];
						System.arraycopy(b, 0, cache, 0, cache.length);
					}else {
						cache = array;
					}
				}
				return array;
			}
		}
		
		public boolean setAllChars(String bytes) {
			return setAllBytes(utf8(bytes), true);
		}
		
		public boolean setAllBytes(byte[] bytes) {
			return setAllBytes(bytes, true);
		}
		
		public boolean setAllBytes(byte[] bytes, boolean copy) {
			if(hasBeenDeleted || bytes == null) {
				return false;
			}
			cacheHit = System.currentTimeMillis();
			this.fileSize = bytes.length;
			if(cacheEnabled) {
				byte[] copz = bytes;
				if(copy) {
					copz = new byte[bytes.length];
					System.arraycopy(bytes, 0, copz, 0, bytes.length);
				}
				cache = copz;
				return sync();
			}else {
				ArrayBuffer a = ArrayBuffer.create(bytes.length);
				Uint8Array ar = Uint8Array.create(a);
				ar.set(bytes);
				boolean s = AsyncHandlers.writeWholeFile(virtualFilesystem.indexeddb, filePath, a).bool;
				hasBeenAccessed = true;
				exists = exists || s;
				return s;
			}
		}
		
		public boolean sync() {
			if(cacheEnabled && cache != null && !hasBeenDeleted) {
				cacheHit = System.currentTimeMillis();
				ArrayBuffer a = ArrayBuffer.create(cache.length);
				Uint8Array ar = Uint8Array.create(a);
				ar.set(cache);
				boolean tryWrite = AsyncHandlers.writeWholeFile(virtualFilesystem.indexeddb, filePath, a).bool;
				hasBeenAccessed = true;
				exists = exists || tryWrite;
				return tryWrite;
			}
			return false;
		}
		
		public boolean delete() {
			if(!hasBeenDeleted && !(hasBeenAccessed && !exists)) {
				cacheHit = System.currentTimeMillis();
				if(!AsyncHandlers.deleteFile(virtualFilesystem.indexeddb, filePath).bool) {
					hasBeenAccessed = true;
					return false;
				}
				virtualFilesystem.fileMap.remove(filePath);
				hasBeenDeleted = true;
				hasBeenAccessed = true;
				exists = false;
				return true;
			}
			return false;
		}
		
		public boolean rename(String newName, boolean copy) {
			if(!hasBeenDeleted && !(hasBeenAccessed && !exists)) {
				cacheHit = System.currentTimeMillis();
				ArrayBuffer arr = AsyncHandlers.readWholeFile(virtualFilesystem.indexeddb, filePath);
				hasBeenAccessed = true;
				if(arr != null) {
					exists = true;
					if(!AsyncHandlers.writeWholeFile(virtualFilesystem.indexeddb, newName, arr).bool) {
						return false;
					}
					if(!copy && !AsyncHandlers.deleteFile(virtualFilesystem.indexeddb, filePath).bool) {
						return false;
					}
				}else {
					exists = false;
				}
				if(!copy) {
					virtualFilesystem.fileMap.remove(filePath);
					filePath = newName;
					virtualFilesystem.fileMap.put(newName, this);
				}
				return true;
			}
			return false;
		}
		
		public boolean exists() {
			if(hasBeenDeleted) {
				return false;
			}
			cacheHit = System.currentTimeMillis();
			if(hasBeenAccessed) {
				return exists;
			}
			exists = AsyncHandlers.fileExists(virtualFilesystem.indexeddb, filePath).bool;
			hasBeenAccessed = true;
			return exists;
		}
		
	}

	private final HashMap<String, VFSFile> fileMap = new HashMap();
	
	public final String database;
	private final IDBDatabase indexeddb;
	
	public static class VFSHandle {
		
		public final boolean failedInit;
		public final boolean failedLocked;
		public final String failedError;
		public final VirtualFilesystem vfs;
		
		public VFSHandle(boolean init, boolean locked, String error, VirtualFilesystem db) {
			failedInit = init;
			failedLocked = locked;
			failedError = error;
			vfs = db;
		}
		
		public String toString() {
			if(failedInit) {
				return "IDBFactory threw an exception, IndexedDB is most likely not supported in this browser." + (failedError == null ? "" : "\n\n" + failedError);
			}
			if(failedLocked) {
				return "The filesystem requested is already in use on a different tab.";
			}
			if(failedError != null) {
				return "The IDBFactory.open() request failed, reason: " + failedError;
			}
			return "Virtual Filesystem Object: " + vfs.database;
		}
		
	}
	
	public static VFSHandle openVFS(String db) {
		DatabaseOpen evt = AsyncHandlers.openDB(db);
		if(evt.failedInit) {
			return new VFSHandle(true, false, evt.failedError, null);
		}
		if(evt.failedLocked) {
			return new VFSHandle(false, true, null, null);
		}
		if(evt.failedError != null) {
			return new VFSHandle(false, false, evt.failedError, null);
		}
		return new VFSHandle(false, false, null, new VirtualFilesystem(db, evt.database));
	}
	
	private VirtualFilesystem(String db, IDBDatabase idb) {
		database = db;
		indexeddb = idb;
	}
	
	public void close() {
		indexeddb.close();
	}
	
	public VFSFile getFile(String path) {
		return getFile(path, false);
	}
	
	public VFSFile getFile(String path, boolean cache) {
		VFSFile f = fileMap.get(path);
		if(f == null) {
			fileMap.put(path, f = new VFSFile(this, path, cache));
		}else {
			if(cache) {
				f.setCacheEnabled();
			}
		}
		return f;
	}
	
	public boolean renameFile(String oldName, String newName, boolean copy) {
		return getFile(oldName).rename(newName, copy);
	}
	
	public boolean deleteFile(String path) {
		return getFile(path).delete();
	}
	
	public boolean fileExists(String path) {
		return getFile(path).exists();
	}
	
	public List<String> listFiles(String prefix) {
		final ArrayList<String> list = new ArrayList();
		AsyncHandlers.iterateFiles(indexeddb, this, prefix, false, (v) -> {
			list.add(v.getPath());
		});
		return list;
	}
	
	public int deleteFiles(String prefix) {
		return AsyncHandlers.deleteFiles(indexeddb, prefix);
	}
	
	public int iterateFiles(String prefix, boolean rw, VFSIterator itr) {
		return AsyncHandlers.iterateFiles(indexeddb, this, prefix, rw, itr);
	}
	
	public int renameFiles(String oldPrefix, String newPrefix, boolean copy) {
		List<String> filesToCopy = listFiles(oldPrefix);
		int i = 0;
		for(String str : filesToCopy) {
			String f = VFile.createPath(newPrefix, str.substring(oldPrefix.length()));
			if(!renameFile(str, f, copy)) {
				System.err.println("Could not " + (copy ? "copy" : "rename") + " file \"" + str + "\" to \"" + f + "\" for some reason");
			}else {
				++i;
			}
		}
		return i;
	}
	
	public void flushCache(long age) {
		long curr = System.currentTimeMillis();
		Iterator<VFSFile> files = fileMap.values().iterator();
		while(files.hasNext()) {
			if(curr - files.next().cacheHit > age) {
				files.remove();
			}
		}
	}
	
	protected static class DatabaseOpen {
		
		protected final boolean failedInit;
		protected final boolean failedLocked;
		protected final String failedError;
		
		protected final IDBDatabase database;
		
		protected DatabaseOpen(boolean init, boolean locked, String error, IDBDatabase db) {
			failedInit = init;
			failedLocked = locked;
			failedError = error;
			database = db;
		}
		
	}
	
	@JSBody(script = "return ((typeof indexedDB) !== 'undefined') ? indexedDB : null;")
	protected static native IDBFactory createIDBFactory();
	
	protected static class AsyncHandlers {
		
		@Async
		protected static native DatabaseOpen openDB(String name);
		
		private static void openDB(String name, final AsyncCallback<DatabaseOpen> cb) {
			IDBFactory i = createIDBFactory();
			if(i == null) {
				cb.complete(new DatabaseOpen(false, false, "window.indexedDB was null or undefined", null));
				return;
			}
			final IDBOpenDBRequest f = i.open(name, 1);
			f.setOnBlocked(new EventHandler() {
				@Override
				public void handleEvent() {
					cb.complete(new DatabaseOpen(false, true, null, null));
				}
			});
			f.setOnSuccess(new EventHandler() {
				@Override
				public void handleEvent() {
					cb.complete(new DatabaseOpen(false, false, null, f.getResult()));
				}
			});
			f.setOnError(new EventHandler() {
				@Override
				public void handleEvent() {
					cb.complete(new DatabaseOpen(false, false, "open error", null));
				}
			});
			f.setOnUpgradeNeeded(new EventListener<IDBVersionChangeEvent>() {
				@Override
				public void handleEvent(IDBVersionChangeEvent evt) {
					f.getResult().createObjectStore("filesystem", IDBObjectStoreParameters.create().keyPath("path"));
				}
			});
		}
		
		@Async
		protected static native BooleanResult deleteFile(IDBDatabase db, String name);
		
		private static void deleteFile(IDBDatabase db, String name, final AsyncCallback<BooleanResult> cb) {
			IDBTransaction tx = db.transaction("filesystem", "readwrite");
			final IDBRequest r = tx.objectStore("filesystem").delete(makeTheFuckingKeyWork(name));
			
			r.setOnSuccess(new EventHandler() {
				@Override
				public void handleEvent() {
					cb.complete(BooleanResult._new(true));
				}
			});
			r.setOnError(new EventHandler() {
				@Override
				public void handleEvent() {
					cb.complete(BooleanResult._new(false));
				}
			});
		}
		
		@JSBody(params = { "obj" }, script = "return (typeof obj === 'undefined') ? null : ((typeof obj.data === 'undefined') ? null : obj.data);")
		protected static native ArrayBuffer readRow(JSObject obj);
		
		@JSBody(params = { "obj" }, script = "return [obj];")
		private static native JSObject makeTheFuckingKeyWork(String k);
		
		@Async
		protected static native ArrayBuffer readWholeFile(IDBDatabase db, String name);
		
		private static void readWholeFile(IDBDatabase db, String name, final AsyncCallback<ArrayBuffer> cb) {
			IDBTransaction tx = db.transaction("filesystem", "readonly");
			final IDBGetRequest r = tx.objectStore("filesystem").get(makeTheFuckingKeyWork(name));
			r.setOnSuccess(new EventHandler() {
				@Override
				public void handleEvent() {
					cb.complete(readRow(r.getResult()));
				}
			});
			r.setOnError(new EventHandler() {
				@Override
				public void handleEvent() {
					cb.complete(null);
				}
			});
			
		}
		
		@JSBody(params = { "k" }, script = "return ((typeof k) === \"string\") ? k : (((typeof k) === \"undefined\") ? null : (((typeof k[0]) === \"string\") ? k[0] : null));")
		private static native String readKey(JSObject k);
		
		@JSBody(params = { "k" }, script = "return ((typeof k) === \"undefined\") ? null : (((typeof k.path) === \"undefined\") ? null : (((typeof k.path) === \"string\") ? k[0] : null));")
		private static native String readRowKey(JSObject r);
		
		@Async
		protected static native Integer iterateFiles(IDBDatabase db, final VirtualFilesystem vfs, final String prefix, boolean rw, final VFSIterator itr);
		
		private static void iterateFiles(IDBDatabase db, final VirtualFilesystem vfs, final String prefix, boolean rw, final VFSIterator itr, final AsyncCallback<Integer> cb) {
			IDBTransaction tx = db.transaction("filesystem", rw ? "readwrite" : "readonly");
			final IDBCursorRequest r = tx.objectStore("filesystem").openCursor();
			final int[] res = new int[1];
			r.setOnSuccess(new EventHandler() {
				@Override
				public void handleEvent() {
					IDBCursor c = r.getResult();
					if(c == null || c.getKey() == null || c.getValue() == null) {
						cb.complete(res[0]);
						return;
					}
					String k = readKey(c.getKey());
					if(k != null) {
						if(k.startsWith(prefix)) {
							int ci = res[0]++;
							try {
								itr.next(VIteratorFile.create(ci, vfs, c));
							}catch(VFSIterator.BreakLoop ex) {
								cb.complete(res[0]);
								return;
							}
						}
					}
					c.doContinue();
				}
			});
			r.setOnError(new EventHandler() {
				@Override
				public void handleEvent() {
					cb.complete(res[0] > 0 ? res[0] : -1);
				}
			});
		}
		
		@Async
		protected static native Integer deleteFiles(IDBDatabase db, final String prefix);
		
		private static void deleteFiles(IDBDatabase db, final String prefix, final AsyncCallback<Integer> cb) {
			IDBTransaction tx = db.transaction("filesystem", "readwrite");
			final IDBCursorRequest r = tx.objectStore("filesystem").openCursor();
			final int[] res = new int[1];
			r.setOnSuccess(new EventHandler() {
				@Override
				public void handleEvent() {
					IDBCursor c = r.getResult();
					if(c == null || c.getKey() == null || c.getValue() == null) {
						cb.complete(res[0]);
						return;
					}
					String k = readKey(c.getKey());
					if(k != null) {
						if(k.startsWith(prefix)) {
							c.delete();
							++res[0];
						}
					}
					c.doContinue();
				}
			});
			r.setOnError(new EventHandler() {
				@Override
				public void handleEvent() {
					cb.complete(res[0] > 0 ? res[0] : -1);
				}
			});
		}
		
		@Async
		protected static native BooleanResult fileExists(IDBDatabase db, String name);
		
		private static void fileExists(IDBDatabase db, String name, final AsyncCallback<BooleanResult> cb) {
			IDBTransaction tx = db.transaction("filesystem", "readonly");
			final IDBCountRequest r = tx.objectStore("filesystem").count(makeTheFuckingKeyWork(name));
			r.setOnSuccess(new EventHandler() {
				@Override
				public void handleEvent() {
					cb.complete(BooleanResult._new(r.getResult() > 0));
				}
			});
			r.setOnError(new EventHandler() {
				@Override
				public void handleEvent() {
					cb.complete(BooleanResult._new(false));
				}
			});
		}
		
		@JSBody(params = { "pat", "dat" }, script = "return { path: pat, data: dat };")
		protected static native JSObject writeRow(String name, ArrayBuffer data);
		
		@Async
		protected static native BooleanResult writeWholeFile(IDBDatabase db, String name, ArrayBuffer data);
		
		private static void writeWholeFile(IDBDatabase db, String name, ArrayBuffer data, final AsyncCallback<BooleanResult> cb) {
			IDBTransaction tx = db.transaction("filesystem", "readwrite");
			final IDBRequest r = tx.objectStore("filesystem").put(writeRow(name, data));
			
			r.setOnSuccess(new EventHandler() {
				@Override
				public void handleEvent() {
					cb.complete(BooleanResult._new(true));
				}
			});
			r.setOnError(new EventHandler() {
				@Override
				public void handleEvent() {
					cb.complete(BooleanResult._new(false));
				}
			});
		}
		
	}
	
	public static byte[] utf8(String str) {
		if(str == null) return null;
		return str.getBytes(Charset.forName("UTF-8"));
	}
	
	public static String utf8(byte[] str) {
		if(str == null) return null;
		return new String(str, Charset.forName("UTF-8"));
	}
	
	public static String CRLFtoLF(String str) {
		if(str == null) return null;
		str = str.indexOf('\r') != -1 ? str.replace("\r", "") : str;
		str = str.trim();
		if(str.endsWith("\n")) {
			str = str.substring(0, str.length() - 1);
		}
		if(str.startsWith("\n")) {
			str = str.substring(1);
		}
		return str;
	}
	
	public static String[] lines(String str) {
		if(str == null) return null;
		return CRLFtoLF(str).split("\n");
	}
	
}