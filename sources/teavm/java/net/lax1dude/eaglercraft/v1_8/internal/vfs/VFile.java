package net.lax1dude.eaglercraft.v1_8.internal.vfs;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

public class VFile {

	public static final String pathSeperator = "/";
	public static final String[] altPathSeperator = new String[] { "\\" };
	
	public static String normalizePath(String p) {
		for(int i = 0; i < altPathSeperator.length; ++i) {
			p = p.replace(altPathSeperator[i], pathSeperator);
		}
		if(p.startsWith(pathSeperator)) {
			p = p.substring(1);
		}
		if(p.endsWith(pathSeperator)) {
			p = p.substring(0, p.length() - pathSeperator.length());
		}
		return p;
	}
	
	public static String[] splitPath(String p) {
		String[] pth = normalizePath(p).split(pathSeperator);
		for(int i = 0; i < pth.length; ++i) {
			pth[i] = pth[i].trim();
		}
		return pth;
	}
	
	protected String path;
	
	public static String createPath(Object... p) {
		ArrayList<String> r = new ArrayList();
		for(int i = 0; i < p.length; ++i) {
			if(p[i] == null) {
				continue;
			}
			String gg = p[i].toString();
			if(gg == null) {
				continue;
			}
			String[] parts = splitPath(gg);
			for(int j = 0; j < parts.length; ++j) {
				if(parts[j] == null || parts[j].equals(".")) {
					continue;
				}else if(parts[j].equals("..") && r.size() > 0) {
					int k = r.size() - 1;
					if(!r.get(k).equals("..")) {
						r.remove(k);
					}else {
						r.add("..");
					}
				}else {
					r.add(parts[j]);
				}
			}
		}
		if(r.size() > 0) {
			StringBuilder s = new StringBuilder();
			for(int i = 0; i < r.size(); ++i) {
				if(i > 0) {
					s.append(pathSeperator);
				}
				s.append(r.get(i));
			}
			return s.toString();
		}else {
			return null;
		}
	}
	
	public VFile(Object... p) {
		this.path = createPath(p);
	}
	
	public InputStream getInputStream() {
		return isRelative() ? null : SYS.VFS.getFile(path).getInputStream();
	}
	
	public OutputStream getOutputStream() {
		return isRelative() ? null : SYS.VFS.getFile(path).getOutputStream();
	}
	
	public String toString() {
		return path;
	}
	
	public boolean isRelative() {
		return path == null || path.contains("..");
	}
	
	public boolean canRead() {
		return !isRelative() && SYS.VFS.fileExists(path);
	}
	
	public String getPath() {
		return path.equals("unnamed") ? null : path;
	}
	
	public String getName() {
		if(path == null) {
			return null;
		}
		int i = path.indexOf(pathSeperator);
		return i == -1 ? path : path.substring(i + 1);
	}
	
	public boolean canWrite() {
		return !isRelative();
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
		return !isRelative() && SYS.VFS.fileExists(path);
	}
	
	public boolean delete() {
		return !isRelative() && SYS.VFS.deleteFile(path);
	}
	
	public boolean renameTo(String p, boolean copy) {
		if(!isRelative() && SYS.VFS.renameFile(path, p, copy)) {
			path = p;
			return true;
		}
		return false;
	}
	
	public int length() {
		return isRelative() ? -1 : SYS.VFS.getFile(path).getSize();
	}
	
	public void getBytes(int fileOffset, byte[] array, int offset, int length) {
		if(isRelative()) {
			throw new ArrayIndexOutOfBoundsException("File is relative");
		}
		SYS.VFS.getFile(path).getBytes(fileOffset, array, offset, length);
	}
	
	public void setCacheEnabled() {
		if(isRelative()) {
			throw new RuntimeException("File is relative");
		}
		SYS.VFS.getFile(path).setCacheEnabled();
	}
	
	public byte[] getAllBytes() {
		if(isRelative()) {
			return null;
		}
		return SYS.VFS.getFile(path).getAllBytes();
	}
	
	public String getAllChars() {
		if(isRelative()) {
			return null;
		}
		return SYS.VFS.getFile(path).getAllChars();
	}
	
	public String[] getAllLines() {
		if(isRelative()) {
			return null;
		}
		return SYS.VFS.getFile(path).getAllLines();
	}
	
	public byte[] getAllBytes(boolean copy) {
		if(isRelative()) {
			return null;
		}
		return SYS.VFS.getFile(path).getAllBytes(copy);
	}
	
	public boolean setAllChars(String bytes) {
		if(isRelative()) {
			return false;
		}
		return SYS.VFS.getFile(path).setAllChars(bytes);
	}
	
	public boolean setAllBytes(byte[] bytes) {
		if(isRelative()) {
			return false;
		}
		return SYS.VFS.getFile(path).setAllBytes(bytes);
	}
	
	public boolean setAllBytes(byte[] bytes, boolean copy) {
		if(isRelative()) {
			return false;
		}
		return SYS.VFS.getFile(path).setAllBytes(bytes, copy);
	}
	
	public List<String> list() {
		if(isRelative()) {
			return Arrays.asList(path);
		}
		return SYS.VFS.listFiles(path);
	}
	
	public int deleteAll() {
		return isRelative() ? 0 : SYS.VFS.deleteFiles(path);
	}
	
}
