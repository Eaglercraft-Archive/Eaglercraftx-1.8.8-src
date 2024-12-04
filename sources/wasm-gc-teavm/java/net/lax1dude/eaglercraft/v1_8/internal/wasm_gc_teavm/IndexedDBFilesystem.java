package net.lax1dude.eaglercraft.v1_8.internal.wasm_gc_teavm;

import org.teavm.interop.Import;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSString;
import org.teavm.jso.indexeddb.IDBDatabase;
import org.teavm.jso.typedarrays.ArrayBuffer;
import org.teavm.jso.typedarrays.Uint8Array;

import net.lax1dude.eaglercraft.v1_8.internal.IEaglerFilesystem;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformRuntime;
import net.lax1dude.eaglercraft.v1_8.internal.VFSFilenameIterator;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.WASMGCBufferAllocator;
import net.lax1dude.eaglercraft.v1_8.internal.vfs2.EaglerFileSystemException;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformFilesystem.FilesystemDatabaseInitializationException;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformFilesystem.FilesystemDatabaseLockedException;

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
public class IndexedDBFilesystem implements IEaglerFilesystem {

	public static IEaglerFilesystem createFilesystem(String dbName) {
		String filesystemDB = "_net_lax1dude_eaglercraft_v1_8_internal_PlatformFilesystem_1_8_8_" + dbName;
		JSDatabaseOpen dbOpen = openDB(BetterJSStringConverter.stringToJS(filesystemDB));

		if(dbOpen.getFailedLocked()) {
			throw new FilesystemDatabaseLockedException(dbOpen.getFailedError());
		}

		if(dbOpen.getFailedInit()) {
			throw new FilesystemDatabaseInitializationException(dbOpen.getFailedError());
		}

		IDBDatabase database = dbOpen.getDatabase();
		if(database == null) {
			throw new NullPointerException("IDBDatabase is null!");
		}

		return new IndexedDBFilesystem(dbName, filesystemDB, database);
	}

	private interface JSDatabaseOpen extends JSObject {

		@JSProperty
		boolean getFailedInit();

		@JSProperty
		boolean getFailedLocked();

		@JSProperty
		String getFailedError();

		@JSProperty
		IDBDatabase getDatabase();

	}

	@Import(module = "platformFilesystem", name = "openDB")
	private static native JSDatabaseOpen openDB(JSString filesystemDB);

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
		return eaglerDelete(database, BetterJSStringConverter.stringToJS(pathName));
	}

	@Import(module = "platformFilesystem", name = "eaglerDelete")
	private static native boolean eaglerDelete(IDBDatabase database, JSString pathName);

	@Override
	public ByteBuffer eaglerRead(String pathName) {
		ArrayBuffer ar = eaglerRead(database, BetterJSStringConverter.stringToJS(pathName));
		if(ar == null) {
			return null;
		}
		Uint8Array arr = new Uint8Array(ar);
		ByteBuffer buf = PlatformRuntime.allocateByteBuffer(arr.getLength());
		WASMGCBufferAllocator.getUnsignedByteBufferView(buf).set(arr);
		return buf;
	}

	@Import(module = "platformFilesystem", name = "eaglerRead")
	private static native ArrayBuffer eaglerRead(IDBDatabase database, JSString pathName);

	@Override
	public void eaglerWrite(String pathName, ByteBuffer data) {
		int len = data.remaining();
		Uint8Array arr = new Uint8Array(len);
		arr.set(WASMGCBufferAllocator.getByteBufferView(data));
		if(!eaglerWrite(database, BetterJSStringConverter.stringToJS(pathName), arr.getBuffer())) {
			throw new EaglerFileSystemException("Failed to write " + len + " byte file to indexeddb table: " + pathName);
		}
	}

	@Import(module = "platformFilesystem", name = "eaglerWrite")
	private static native boolean eaglerWrite(IDBDatabase database, JSString pathName, ArrayBuffer arr);

	@Override
	public boolean eaglerExists(String pathName) {
		return eaglerExists(database, BetterJSStringConverter.stringToJS(pathName));
	}

	@Import(module = "platformFilesystem", name = "eaglerExists")
	private static native boolean eaglerExists(IDBDatabase database, JSString pathName);

	@Override
	public boolean eaglerMove(String pathNameOld, String pathNameNew) {
		return eaglerMove(database, BetterJSStringConverter.stringToJS(pathNameOld), BetterJSStringConverter.stringToJS(pathNameNew));
	}

	@Import(module = "platformFilesystem", name = "eaglerMove")
	private static native boolean eaglerMove(IDBDatabase database, JSString pathNameOld, JSString pathNameNew);

	@Override
	public int eaglerCopy(String pathNameOld, String pathNameNew) {
		return eaglerCopy(database, BetterJSStringConverter.stringToJS(pathNameOld), BetterJSStringConverter.stringToJS(pathNameNew));
	}

	@Import(module = "platformFilesystem", name = "eaglerCopy")
	private static native int eaglerCopy(IDBDatabase database, JSString pathNameOld, JSString pathNameNew);

	@Override
	public int eaglerSize(String pathName) {
		return eaglerSize(database, BetterJSStringConverter.stringToJS(pathName));
	}

	@Import(module = "platformFilesystem", name = "eaglerSize")
	private static native int eaglerSize(IDBDatabase database, JSString pathName);

	private interface JSDatabaseIteratorResult extends JSObject {

		@JSProperty
		int getLength();

		String getRow(int idx);

	}

	@Override
	public void eaglerIterate(String pathName, VFSFilenameIterator itr, boolean recursive) {
		JSDatabaseIteratorResult result = eaglerIterate(database, BetterJSStringConverter.stringToJS(pathName), recursive);
		if(result != null) {
			for(int i = 0, len = result.getLength(); i < len; ++i) {
				itr.next(result.getRow(i));
			}
		}
	}

	@Import(module = "platformFilesystem", name = "eaglerIterate")
	private static native JSDatabaseIteratorResult eaglerIterate(IDBDatabase database, JSString pathName, boolean recursive);

	@Override
	public void closeHandle() {
		if(database != null) {
			database.close();
			database = null;
		}
	}

}
