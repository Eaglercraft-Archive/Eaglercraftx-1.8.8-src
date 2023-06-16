package net.lax1dude.eaglercraft.v1_8.internal;

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
public class GLObjectMap<T> {
	private Object[] values;
	private int size;
	private int insertIndex;
	public int allocatedObjects;
	
	public GLObjectMap(int initialSize) {
		this.values = new Object[initialSize];
		this.size = initialSize;
		this.insertIndex = 0;
		this.allocatedObjects = 0;
	}

	public int register(T obj) {
		int start = insertIndex;
		do {
			++insertIndex;
			if(insertIndex >= size) {
				insertIndex = 0;
			}
			if(insertIndex == start) {
				resize();
				return register(obj);
			}
		}while(values[insertIndex] != null);
		values[insertIndex] = obj;
		++allocatedObjects;
		return insertIndex + 1;
	}
	
	public T free(int obj) {
		--obj;
		if(obj >= size || obj < 0) return null;
		Object ret = values[obj];
		values[obj] = null;
		--allocatedObjects;
		return (T) ret;
	}
	
	public T get(int obj) {
		--obj;
		if(obj >= size || obj < 0) return null;
		return (T) values[obj];
	}
	
	private void resize() {
		int oldSize = size;
		size += size / 2;
		Object[] oldValues = values;
		values = new Object[size];
		System.arraycopy(oldValues, 0, values, 0, oldSize);
	}
}
