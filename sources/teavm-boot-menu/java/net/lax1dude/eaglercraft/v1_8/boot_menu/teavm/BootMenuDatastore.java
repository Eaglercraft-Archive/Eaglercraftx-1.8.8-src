package net.lax1dude.eaglercraft.v1_8.boot_menu.teavm;

import java.util.function.Consumer;

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

import net.lax1dude.eaglercraft.v1_8.internal.teavm.BooleanResult;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.TeaVMUtils;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

/**
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
public class BootMenuDatastore {

	private static final Logger logger = LogManager.getLogger("BootMenuDatastore");

	public static final String bootMenuDatabaseName = BootMenuConstants.bootMenuDatabaseName;

	private static final Object instanceLock = new Object();
	private static BootMenuDatastore instance = null;

	public static class DatastoreLockedException extends RuntimeException {
		public DatastoreLockedException(String message) {
			super(message);
		}
	}

	public static class DatastoreInitializationException extends RuntimeException {
		public DatastoreInitializationException(String message) {
			super(message);
		}
	}

	public static class DatastoreOperationException extends RuntimeException {
		public DatastoreOperationException(String message) {
			super(message);
		}
	}

	public static BootMenuDatastore openDatastore() {
		synchronized(instanceLock) {
			if(instance != null) {
				++instance.openCount;
				return instance;
			}
			return instance = openDatastore0();
		}
	}

	private static BootMenuDatastore openDatastore0() {
		DatabaseOpen openResult = AsyncHandlers
				.openDB(bootMenuDatabaseName + (BootMenuEntryPoint.isSignedClient() ? "_sig" : "_unsig")
						+ (IBootMenuConfigAdapter.instance.isBootMenuBlocksUnsignedClients() ? "_1" : "_0"));
		if(openResult.failedLocked) {
			throw new DatastoreLockedException(openResult.failedError);
		}
		if(openResult.failedInit) {
			throw new DatastoreInitializationException(openResult.failedError);
		}
		if(openResult.database == null) {
			throw new NullPointerException("IDBDatabase is null!");
		}
		return new BootMenuDatastore(openResult.database);
	}

	private IDBDatabase database;
	private int openCount;

	private BootMenuDatastore(IDBDatabase database) {
		this.database = database;
		this.openCount = 1;
	}

	public byte[] getItem(String key) {
		if(database != null) {
			return TeaVMUtils.wrapByteArrayBuffer(AsyncHandlers.readWholeFile(database, key));
		}else {
			return null;
		}
	}

	public void setItem(String key, byte[] data) {
		if(database != null) {
			if(data != null) {
				if(!AsyncHandlers.writeWholeFile(database, key, TeaVMUtils.unwrapArrayBuffer(data)).bool) {
					throw new DatastoreOperationException("Failed to write to datastore: \"" + key + "\" (" + data.length + " bytes)");
				}
			}else {
				AsyncHandlers.deleteFile(database, key);
			}
		}
	}

	public boolean containsKey(String key) {
		if(database != null) {
			return AsyncHandlers.fileExists(database, key).bool;
		}else {
			return false;
		}
	}

	public boolean deleteItem(String key) {
		if(database != null) {
			return AsyncHandlers.deleteFile(database, key).bool;
		}else {
			return false;
		}
	}

	public void closeDatastore() {
		if(--openCount == 0) {
			synchronized(instanceLock) {
				if(instance == this) {
					instance = null;
				}
			}
			closeDatastore0();
		}
	}

	private void closeDatastore0() {
		if(database != null) {
			database.close();
		}
	}

	public void iterateAllKeys(Consumer<String> itr) {
		if(database != null) {
			AsyncHandlers.iterateFiles(database, itr);
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
		
		@JSBody(params = { "k" }, script = "return ((typeof k) === \"string\") ? k : (((typeof k) === \"undefined\") ? null : (((typeof k[0]) === \"string\") ? k[0] : null));")
		private static native String readKey(JSObject k);
		
		@Async
		protected static native Integer iterateFiles(IDBDatabase db, final Consumer<String> itr);
		
		private static void iterateFiles(IDBDatabase db, final Consumer<String> itr, final AsyncCallback<Integer> cb) {
			IDBTransaction tx = db.transaction("filesystem", "readonly");
			final IDBCursorRequest r = tx.objectStore("filesystem").openCursor();
			final int[] res = new int[1];
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
						++res[0];
						itr.accept(k);
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
