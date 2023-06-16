package net.lax1dude.eaglercraft.v1_8.internal.teavm;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.typedarrays.ArrayBuffer;
import org.teavm.jso.typedarrays.Int8Array;

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
public class TeaVMUtils {

	@JSBody(params = { "url" }, script = "URL.revokeObjectURL(url);")
	public static native void freeDataURL(String url);
	
	@JSBody(params = { "buf", "mime" }, script = "return URL.createObjectURL(new Blob([buf], {type: mime}));")
	public static native String getDataURL(ArrayBuffer buf, String mime);
	
	@JSBody(params = { "obj", "name", "handler" }, script = "obj.addEventListener(name, handler);")
	public static native void addEventListener(JSObject obj, String name, JSObject handler);
	
	public static final byte[] arrayBufferToBytes(ArrayBuffer buf) {
		if(buf == null) {
			return null;
		}
		
		Int8Array arr = Int8Array.create(buf);
		byte[] ret = new byte[arr.getByteLength()];
		for(int i = 0; i < ret.length; ++i) {
			ret[i] = arr.get(i);
		}
		
		return ret;
	}

}
