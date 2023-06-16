package net.lax1dude.eaglercraft.v1_8;

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
public class EagUtils {
	
	private static final String hex = "0123456789ABCDEF";
	
	public static String hexString(int value, int digits) {
		String ret = "";
		for(int i = 0, l = digits << 2; i < l; i += 4) {
			ret = hex.charAt((value >> i) & 0xF) + ret;
		}
		return ret;
	}
	
	public static int decodeHex(CharSequence num) {
		int ret = 0;
		for(int i = 0, l = num.length(); i < l; ++i) {
			ret = ret << 4;
			int v = hex.indexOf(num.charAt(i));
			if(v >= 0) {
				ret |= v;
			}
		}
		return ret;
	}
	
	public static int decodeHexByte(CharSequence str, int off) {
		return str.length() < off + 2 ? decodeHex(str.subSequence(off, 2)) : 0;
	}
	
	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		}catch(InterruptedException ex) {
		}
	}
	
	public static String toASCIIEagler(String str) {
		char[] ascii = new char[str.length()];
		for(int i = 0; i < ascii.length; ++i) {
			int c = (int)str.charAt(i);
			if(c < 32 || c > 126) {
				ascii[i] = '_';
			}else {
				ascii[i] = (char)c;
			}
		}
		return new String(ascii);
	}
	
	public static void validateASCIIEagler(String str) {
		for(int i = 0, l = str.length(); i < l; ++i) {
			int c = (int)str.charAt(i);
			if(c < 32 || c > 126) {
				throw new IllegalArgumentException("invalid ascii");
			}
		}
	}

}
