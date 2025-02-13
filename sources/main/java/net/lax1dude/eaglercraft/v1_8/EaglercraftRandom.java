/*
 * Copyright (c) 2022-2024 lax1dude. All Rights Reserved.
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

package net.lax1dude.eaglercraft.v1_8;

import net.lax1dude.eaglercraft.v1_8.internal.PlatformRuntime;

public class EaglercraftRandom {

	private static final long multiplier = 0x5DEECE66DL;
	private static final long addend = 0xBL;
	private static final long mask = (1L << 48) - 1;

	private static final double DOUBLE_UNIT = 0x1.0p-53;
	private long seed = 69;
	private final boolean enableScramble;

	public EaglercraftRandom() {
		this(PlatformRuntime.randomSeed());
	}

	public EaglercraftRandom(long seed) {
		this(seed, true);
	}

	public EaglercraftRandom(boolean scramble) {
		this(PlatformRuntime.randomSeed(), scramble);
	}

	/**
	 * Older versions of EaglercraftX (and Eaglercraft) are missing the
	 * "initialScramble" function from their setSeed function, which was what caused
	 * world generation to not match vanilla. The "enableScramble" boolean is used
	 * when players play on an old world created before the bug was fixed.
	 */
	public EaglercraftRandom(long seed, boolean scramble) {
		enableScramble = scramble;
		setSeed(seed);
	}

	private static long initialScramble(long seed) {
		return (seed ^ multiplier) & mask;
	}

	public void setSeed(long yeed) {
		if(enableScramble) {
			seed = initialScramble(yeed);
		}else {
			seed = yeed;
		}
		haveNextNextGaussian = true;
	}

	public boolean isScramble() {
		return enableScramble;
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