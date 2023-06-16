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
public class EaglercraftRandom {

	private static final long multiplier = 0x5DEECE66DL;
	private static final long addend = 0xBL;
	private static final long mask = (1L << 48) - 1;

	private static final double DOUBLE_UNIT = 0x1.0p-53;
	private long seed = 69;

	private static int yee = 0;

	public EaglercraftRandom() {
		this(System.currentTimeMillis() + (++yee));
	}

	public EaglercraftRandom(long seed) {
		setSeed(seed);
	}

	public void setSeed(long yeed) {
		seed = yeed;
	}

	protected int next(int bits) {
		seed = (seed * multiplier + addend) & mask;
		return (int) (seed >>> (48 - bits));
	}

	public void nextBytes(byte[] bytes) {
		for (int i = 0, len = bytes.length; i < len;)
			for (int rnd = nextInt(), n = Math.min(len - i, Integer.SIZE / Byte.SIZE); n-- > 0; rnd >>= Byte.SIZE)
				bytes[i++] = (byte) rnd;
	}

	public int nextInt() {
		return next(32);
	}

	public int nextInt(int bound) {
		int r = next(31);
		int m = bound - 1;
		if ((bound & m) == 0) // i.e., bound is a power of 2
			r = (int) ((bound * (long) r) >> 31);
		else {
			for (int u = r; u - (r = u % bound) + m < 0; u = next(31))
				;
		}
		return r;
	}

	public long nextLong() {
		return ((long) (next(32)) << 32) + next(32);
	}

	public boolean nextBoolean() {
		return next(1) != 0;
	}

	public float nextFloat() {
		return next(24) / ((float) (1 << 24));
	}

	public double nextDouble() {
		return (((long) (next(26)) << 27) + next(27)) * DOUBLE_UNIT;
	}

	private double nextNextGaussian;
	private boolean haveNextNextGaussian = false;

	public double nextGaussian() {
		// See Knuth, ACP, Section 3.4.1 Algorithm C.
		if (haveNextNextGaussian) {
			haveNextNextGaussian = false;
			return nextNextGaussian;
		} else {
			double v1, v2, s;
			do {
				v1 = 2 * nextDouble() - 1; // between -1 and 1
				v2 = 2 * nextDouble() - 1; // between -1 and 1
				s = v1 * v1 + v2 * v2;
			} while (s >= 1 || s == 0);
			double multiplier = StrictMath.sqrt(-2 * StrictMath.log(s) / s);
			nextNextGaussian = v2 * multiplier;
			haveNextNextGaussian = true;
			return v1 * multiplier;
		}
	}
}
