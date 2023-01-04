package net.lax1dude.eaglercraft.v1_8.internal.vfs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.teavm.interop.Async;
import org.teavm.interop.AsyncCallback;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.dom.events.Event;
import org.teavm.jso.dom.events.EventListener;
import net.lax1dude.eaglercraft.v1_8.internal.indexeddb.IDBCursor;
import net.lax1dude.eaglercraft.v1_8.internal.indexeddb.IDBRequest;
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

/**
 * Do not use an instance of this class outside of the VFSIterator.next() method
 */
public class VIteratorFile extends VFile {
	
	static final VIteratorFile instance = new VIteratorFile();
	
	private VIteratorFile() {
		super("");
		this.idx = -1;
		this.cur = null;
		this.vfs = null;
	}
	
	private static class VirtualIteratorOutputStream extends ByteArrayOutputStream {
		
		private final VIteratorFile itr;
		
		protected VirtualIteratorOutputStream(VIteratorFile itr) {
			this.itr = itr;
		}
		
		public void close() throws IOException {
			if(!itr.setAllBytes(super.toByteArray(), false)) {
				throw new IOException("Could not close stream and write to \"" + itr.path + "\" on VFS \"" + itr.vfs.database + "\" (the file was probably deleted)");
			}
		}
		
	}
	
	private int idx;
	private IDBCursor cur;
	private VirtualFilesystem vfs;
	private boolean wasDeleted;
	
	@JSBody(params = { "k" }, script = "return ((typeof k) === \"string\") ? k : (((typeof k) === \"undefined\") ? null : (((typeof k[0]) === \"string\") ? k[0] : null));")
	private static native String readKey(JSObject k);
	
	static VIteratorFile create(int idx, VirtualFilesystem vfs, IDBCursor cur) {
		String k = readKey(cur.getKey());
		if(k == null) {
			return null;
		}
		instance.update(idx, k, vfs, cur);
		return instance;
	}
	
	public VFile makeVFile() {
		return new VFile(path);
	}
	
	private void update(int idx, String path, VirtualFilesystem vfs, IDBCursor cur) {
		this.idx = idx;
		this.path = path;
		this.vfs =  vfs;
		this.cur = cur;
		this.wasDeleted = false;
	}
	
	public InputStream getInputStream() {
		return !wasDeleted ? new ByteArrayInputStream(getAllBytes()) : null;
	}
	
	public OutputStream getOutputStream() {
		return !wasDeleted ? new VirtualIteratorOutputStream(this) : null;
	}
	
	public String toString() {
		return path;
	}
	
	public boolean isRelative() {
		return false;
	}
	
	public boolean canRead() {
		return !wasDeleted;
	}
	
	public String getPath() {
		return path;
	}
	
	public String getName() {
		if(path == null) {
			return null;
		}
		int i = path.indexOf(pathSeperator);
		return i == -1 ? path : path.substring(i + 1);
	}
	
	public boolean canWrite() {
		return !wasDeleted;
	}
	
	public String getParent() {
		if(path == null) {
			return null;
		}
		int i = path.indexOf(pathSeperator);
		return i == -1 ? ".." : path.substring(0, i);
	}
	
	public int hashCode() {
		return path == null ? 0 : path.hashCode();
	}
	
	public boolean equals(Object o) {
		return path != null && o != null && (o instanceof VFile) && path.equals(((VFile)o).path);
	}
	
	public boolean exists() {
		return !wasDeleted;
	}
	
	public boolean delete() {
		return wasDeleted = AsyncHandlers.awaitRequest(cur.delete()).bool;
	}
	
	public boolean renameTo(String p) {
		byte[] data = getAllBytes();
		String op = path;
		path = p;
		if(!setAllBytes(data)) {
			path = op;
			return false;
		}
		path = op;
		if(!delete()) {
			return false;
		}
		path = p;
		return true;
	}
	
	public int length() {
		JSObject obj = cur.getValue();
		
		if(obj == null) {
			throw new RuntimeException("Value of entry is missing");
		}
		
		ArrayBuffer arr = readRow(obj);
	
		if(arr == null) {
			throw new RuntimeException("Value of the fucking value of the entry is missing");
		}
		
		return arr.getByteLength();
	}
	
	public void getBytes(int fileOffset, byte[] array, int offset, int length) {
		JSObject obj = cur.getValue();
		
		if(obj == null) {
			throw new ArrayIndexOutOfBoundsException("Value of entry is missing");
		}
		
		ArrayBuffer arr = readRow(obj);

		if(arr == null) {
			throw new ArrayIndexOutOfBoundsException("Value of the fucking value of the entry is missing");
		}
		
		Uint8Array a = Uint8Array.create(arr);
		
		if(a.getLength() < fileOffset + length) {
			throw new ArrayIndexOutOfBoundsException("file '" + path + "' size was "+a.getLength()+" but user tried to read index "+(fileOffset + length - 1));
		}
		
		for(int i = 0; i < length; ++i) {
			array[i + offset] = (byte)a.get(i + fileOffset);
		}
	}
	
	public void setCacheEnabled() {
		// no
	}
	
	@JSBody(params = { "obj" }, script = "return (typeof obj === 'undefined') ? null : ((typeof obj.data === 'undefined') ? null : obj.data);")
	private static native ArrayBuffer readRow(JSObject obj);
	
	public byte[] getAllBytes() {
		JSObject obj = cur.getValue();
		
		if(obj == null) {
			return null;
		}
		
		ArrayBuffer arr = readRow(obj);

		if(arr == null) {
			return null;
		}
		
		Uint8Array a = Uint8Array.create(arr);
		int ii = a.getByteLength();
		
		byte[] array = new byte[ii];
		for(int i = 0; i < ii; ++i) {
			array[i] = (byte)a.get(i);
		}
		
		return array;
	}
	
	public String getAllChars() {
		return VirtualFilesystem.utf8(getAllBytes());
	}
	
	public String[] getAllLines() {
		return VirtualFilesystem.lines(VirtualFilesystem.utf8(getAllBytes()));
	}
	
	public byte[] getAllBytes(boolean copy) {
		return getAllBytes();
	}
	
	public boolean setAllChars(String bytes) {
		return setAllBytes(VirtualFilesystem.utf8(bytes));
	}
	
	public List<String> list() {
		throw new RuntimeException("Cannot perform list all in VFS callback");
	}
	
	public int deleteAll() {
		throw new RuntimeException("Cannot perform delete all in VFS callback");
	}
	
	@JSBody(params = { "pat", "dat" }, script = "return { path: pat, data: dat };")
	private static native JSObject writeRow(String name, ArrayBuffer data);
	
	public boolean setAllBytes(byte[] bytes) {
		ArrayBuffer a = ArrayBuffer.create(bytes.length);
		Uint8Array ar = Uint8Array.create(a);
		ar.set(bytes);
		JSObject obj = writeRow(path, a);
		BooleanResult r = AsyncHandlers.awaitRequest(cur.update(obj));
		return r.bool;
	}
	
	public boolean setAllBytes(byte[] bytes, boolean copy) {
		return setAllBytes(bytes);
	}
	
	public static class AsyncHandlers {
		
		@Async
		public static native BooleanResult awaitRequest(IDBRequest r);
		
		private static void awaitRequest(IDBRequest r, final AsyncCallback<BooleanResult> cb) {
			r.addEventListener("success", new EventListener<Event>() {
				@Override
				public void handleEvent(Event evt) {
					cb.complete(BooleanResult._new(true));
				}
			});
			r.addEventListener("error", new EventListener<Event>() {
				@Override
				public void handleEvent(Event evt) {
					cb.complete(BooleanResult._new(false));
				}
			});
		}
		
	}
	
}
