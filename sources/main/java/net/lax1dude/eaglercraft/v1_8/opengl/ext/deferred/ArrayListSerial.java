package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred;

import java.util.ArrayList;

/**
 * Copyright (c) 2023 LAX1DUDE. All Rights Reserved.
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
public class ArrayListSerial<E> extends ArrayList<E> implements ListSerial<E> {

	protected int modCountEagler = 0;
	protected int mark = 0;

	public ArrayListSerial() {
		super();
	}

	public ArrayListSerial(int initialSize) {
		super(initialSize);
	}

	public E set(int index, E element) {
		++modCountEagler;
		return super.set(index, element);
	}

	public int getEaglerSerial() {
		return (modCount << 8) + modCountEagler;
	}

	public void eaglerIncrSerial() {
		++modCountEagler;
	}

	public void eaglerResetCheck() {
		mark = getEaglerSerial();
	}

	public boolean eaglerCheck() {
		return mark != getEaglerSerial();
	}

}
