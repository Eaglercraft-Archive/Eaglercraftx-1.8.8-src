package net.lax1dude.eaglercraft.v1_8.internal.teavm;

import org.teavm.interop.Async;
import org.teavm.interop.AsyncCallback;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;
import org.teavm.jso.dom.events.EventListener;
import org.teavm.jso.indexeddb.EventHandler;
import org.teavm.jso.indexeddb.IDBCountRequest;
import org.teavm.jso.indexeddb.IDBCursor;
import org.teavm.jso.indexeddb.IDBCursorRequest;
import org.teavm.jso.indexeddb.IDBDatabase;
import org.teavm.jso.indexeddb.IDBFactory;
import org.teavm.jso.indexeddb.IDBGetRequest;
import org.teavm.jso.indexeddb.IDBObjectStoreParameters;
import org.teavm.jso.indexeddb.IDBOpenDBRequest;
import org.teavm.jso.indexeddb.IDBRequest;
import org.teavm.jso.indexeddb.IDBTransaction;
import org.teavm.jso.indexeddb.IDBVersionChangeEvent;
import org.teavm.jso.typedarrays.ArrayBuffer;
import org.teavm.jso.typedarrays.Int8Array;

import net.lax1dude.eaglercraft.v1_8.internal.IEaglerFilesystem;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformFilesystem.FilesystemDatabaseInitializationException;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformFilesystem.FilesystemDatabaseLockedException;
import net.lax1dude.eaglercraft.v1_8.internal.VFSFilenameIterator;
import net.lax1dude.eaglercraft.v1_8.internal.VFSFilenameIteratorNonRecursive;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.EaglerArrayBufferAllocator;
import net.lax1dude.eaglercraft.v1_8.internal.vfs2.EaglerFileSystemException;
import net.lax1dude.eaglercraft.v1_8.internal.vfs2.VFSIterator2;

/**
 * Copyright (c) 2022-2024 lax1dude. All Rights Reserved.
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
public class IndexedDBFilesystem implements IEaglerFilesystem {

	public static IEaglerFilesystem createFilesystem(String dbName) {
		String filesystemDB = "_net_lax1dude_eaglercraft_v1_8_internal_PlatformFilesystem_1_8_8_" + dbName;
		DatabaseOpen dbOpen = AsyncHandlers.openDB(filesystemDB);
		
		if(dbOpen.failedLocked) {
			throw new FilesystemDatabaseLockedException(dbOpen.failedError);
		}
		
		if(dbOpen.failedInit) {
			throw new FilesystemDatabaseInitializationException(dbOpen.failedError);
		}
		
		if(dbOpen.database == null) {
			throw new NullPointerException("IDBDatabase is null!");
		}
		
		return new IndexedDBFilesystem(dbName, filesystemDB, dbOpen.database);
	}

	private final String name;
	private final String indexedDBName;
	private IDBDatabase database;

	private IndexedDBFilesystem(String name, String indexedDBName, IDBDatabase database) {
		this.name = name;
		this.indexedDBName = indexedDBName;
		this.database = database;
	}

	@Override
	public String getFilesystemName() {
		return name;
	}

	@Override
	public String getInternalDBName() {
		return "indexeddb:" + indexedDBName;
	}

	@Override
	public boolean isRamdisk() {
		return false;
	}

	@Override
	public boolean eaglerDelete(String pathName) {
		return AsyncHandlers.deleteFile(database, pathName).bool;
	}

	@Override
	public ByteBuffer eaglerRead(String pathName) {
		ArrayBuffer ar = AsyncHandlers.readWholeFile(database, pathName);
		if(ar == null) {
			return null;
		}
		return EaglerArrayBufferAllocator.wrapByteBufferTeaVM(Int8Array.create(ar));
	}

	@Override
	public void eaglerWrite(String pathName, ByteBuffer data) {
		if(!AsyncHandlers.writeWholeFile(database, pathName, EaglerArrayBufferAllocator.getDataView8Unsigned(data).getBuffer()).bool) {
			throw new EaglerFileSystemException("Failed to write " + data.remaining() + " byte file to indexeddb table: " + pathName);
		}
	}

	@Override
	public boolean eaglerExists(String pathName) {
		return AsyncHandlers.fileExists(database, pathName).bool;
	}

	@Override
	public boolean eaglerMove(String pathNameOld, String pathNameNew) {
		ArrayBuffer old = AsyncHandlers.readWholeFile(database, pathNameOld);
		return old != null && AsyncHandlers.writeWholeFile(database, pathNameNew, old).bool && AsyncHandlers.deleteFile(database, pathNameOld).bool;
	}

	@Override
	public int eaglerCopy(String pathNameOld, String pathNameNew) {
		ArrayBuffer old = AsyncHandlers.readWholeFile(database, pathNameOld);
		if(old != null && AsyncHandlers.writeWholeFile(database, pathNameNew, old).bool) {
			return old.getByteLength();
		}else {
			return -1;
		}
	}

	@Override
	public int eaglerSize(String pathName) {
		ArrayBuffer old = AsyncHandlers.readWholeFile(database, pathName);
		return old == null ? -1 : old.getByteLength();
	}

	@Override
	public void eaglerIterate(String pathName, VFSFilenameIterator itr, boolean recursive) {
		if(recursive) {
			AsyncHandlers.iterateFiles(database, pathName, false, itr);
		}else {
			AsyncHandlers.iterateFiles(database, pathName, false, new VFSFilenameIteratorNonRecursive(itr, VFSFilenameIteratorNonRecursive.countSlashes(pathName) + 1));
		}
	}

	@Override
	public void closeHandle() {
		if(database != null) {
			database.close();
			database = null;
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

	@JSFunctor
	protected static interface OpenErrorCallback extends JSObject {
		void call(String str);
	}

	@JSBody(params = { "factory", "name", "ii", "errCB" }, script = "try { return factory.open(name, ii); } catch(err) { errCB(\"\" + err); return null; }")
	protected static native IDBOpenDBRequest safeOpen(IDBFactory factory, String name, int i, OpenErrorCallback errCB);

	protected static class AsyncHandlers {
		
		@Async
		protected static native DatabaseOpen openDB(String name);
		
		private static void openDB(String name, final AsyncCallback<DatabaseOpen> cb) {
			IDBFactory i = createIDBFactory();
			if(i == null) {
				cb.complete(new DatabaseOpen(true, false, "window.indexedDB was null or undefined", null));
				return;
			}
			final String[] errorHolder = new String[] { null };
			final IDBOpenDBRequest f = safeOpen(i, name, 1, (e) -> errorHolder[0] = e);
			if(f == null || TeaVMUtils.isNotTruthy(f)) {
				cb.complete(new DatabaseOpen(true, false, errorHolder[0] != null ? errorHolder[0] : "database open request was null or undefined", null));
				return;
			}
			TeaVMUtils.addEventListener(f, "blocked", new EventHandler() {
				@Override
				public void handleEvent() {
					cb.complete(new DatabaseOpen(false, true, null, null));
				}
			});
			TeaVMUtils.addEventListener(f, "success", new EventHandler() {
				@Override
				public void handleEvent() {
					cb.complete(new DatabaseOpen(false, false, null, f.getResult()));
				}
			});
			TeaVMUtils.addEventListener(f, "error", new EventHandler() {
				@Override
				public void handleEvent() {
					cb.complete(new DatabaseOpen(true, false, "open error", null));
				}
			});
			TeaVMUtils.addEventListener(f, "upgradeneeded", new EventListener<IDBVersionChangeEvent>() {
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
			TeaVMUtils.addEventListener(r, "success", new EventHandler() {
				@Override
				public void handleEvent() {
					cb.complete(BooleanResult.TRUE);
				}
			});
			TeaVMUtils.addEventListener(r, "error", new EventHandler() {
				@Override
				public void handleEvent() {
					cb.complete(BooleanResult.FALSE);
				}
			});
		}
		
		@JSBody(params = { "obj" }, script = "return (typeof obj === \"undefined\") ? null : ((typeof obj.data === \"undefined\") ? null : obj.data);")
		protected static native ArrayBuffer readRow(JSObject obj);
		
		@JSBody(params = { "obj" }, script = "return [obj];")
		private static native JSObject makeTheFuckingKeyWork(String k);
		
		@Async
		protected static native ArrayBuffer readWholeFile(IDBDatabase db, String name);
		
		private static void readWholeFile(IDBDatabase db, String name, final AsyncCallback<ArrayBuffer> cb) {
			IDBTransaction tx = db.transaction("filesystem", "readonly");
			final IDBGetRequest r = tx.objectStore("filesystem").get(makeTheFuckingKeyWork(name));
			TeaVMUtils.addEventListener(r, "success", new EventHandler() {
				@Override
				public void handleEvent() {
					cb.complete(readRow(r.getResult()));
				}
			});
			TeaVMUtils.addEventListener(r, "error", new EventHandler() {
				@Override
				public void handleEvent() {
					cb.complete(null);
				}
			});
			
		}
		
		@JSBody(params = { "k" }, script = "return ((typeof k) === \"string\") ? k : (((typeof k) === \"undefined\") ? null : (((typeof k[0]) === \"string\") ? k[0] : null));")
		private static native String readKey(JSObject k);
		
		@Async
		protected static native Integer iterateFiles(IDBDatabase db, final String prefix, boolean rw, final VFSFilenameIterator itr);
		
		private static void iterateFiles(IDBDatabase db, final String prefix, boolean rw, final VFSFilenameIterator itr, final AsyncCallback<Integer> cb) {
			IDBTransaction tx = db.transaction("filesystem", rw ? "readwrite" : "readonly");
			final IDBCursorRequest r = tx.objectStore("filesystem").openCursor();
			final int[] res = new int[1];
			final boolean b = prefix.length() == 0;
			TeaVMUtils.addEventListener(r, "success", new EventHandler() {
				@Override
				public void handleEvent() {
					IDBCursor c = r.getResult();
					if(c == null || c.getKey() == null || c.getValue() == null) {
						cb.complete(res[0]);
						return;
					}
					String k = readKey(c.getKey());
					if(k != null) {
						if(b || k.startsWith(prefix)) {
							int ci = res[0]++;
							try {
								itr.next(k);
							}catch(VFSIterator2.BreakLoop ex) {
								cb.complete(res[0]);
								return;
							}
						}
					}
					c.doContinue();
				}
			});
			TeaVMUtils.addEventListener(r, "error", new EventHandler() {
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
			TeaVMUtils.addEventListener(r, "success", new EventHandler() {
				@Override
				public void handleEvent() {
					cb.complete(BooleanResult._new(r.getResult() > 0));
				}
			});
			TeaVMUtils.addEventListener(r, "error", new EventHandler() {
				@Override
				public void handleEvent() {
					cb.complete(BooleanResult.FALSE);
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
			TeaVMUtils.addEventListener(r, "success", new EventHandler() {
				@Override
				public void handleEvent() {
					cb.complete(BooleanResult.TRUE);
				}
			});
			TeaVMUtils.addEventListener(r, "error", new EventHandler() {
				@Override
				public void handleEvent() {
					cb.complete(BooleanResult.FALSE);
				}
			});
		}
		
	}

}
