package net.lax1dude.eaglercraft.v1_8;

import net.minecraft.client.settings.KeyBinding;

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
public class ArrayUtils {

	public static KeyBinding[] clone(KeyBinding[] keyBinding) {
		KeyBinding[] clone = new KeyBinding[keyBinding.length];
		System.arraycopy(keyBinding, 0, clone, 0, keyBinding.length);
		return clone;
	}

	public static KeyBinding[] addAll(KeyBinding[] arr1, KeyBinding[] arr2) {
		KeyBinding[] clone = new KeyBinding[arr1.length + arr2.length];
		System.arraycopy(arr1, 0, clone, 0, arr1.length);
		System.arraycopy(arr2, 0, clone, arr1.length, arr2.length);
		return clone;
	}

	public static String[] subarray(String[] stackTrace, int i, int j) {
		String[] ret = new String[j - i];
		System.arraycopy(stackTrace, i, ret, 0, j - i);
		return ret;
	}
	
	public static String asciiString(byte[] bytes) {
		char[] str = new char[bytes.length];
		for(int i = 0; i < bytes.length; ++i) {
			str[i] = (char)((int) bytes[i] & 0xFF);
		}
		return new String(str);
	}
	
	public static byte[] asciiString(String string) {
		byte[] str = new byte[string.length()];
		for(int i = 0; i < str.length; ++i) {
			str[i] = (byte)string.charAt(i);
		}
		return str;
	}

}
