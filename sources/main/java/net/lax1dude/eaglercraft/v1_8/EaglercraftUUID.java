package net.lax1dude.eaglercraft.v1_8;

import net.lax1dude.eaglercraft.v1_8.crypto.MD5Digest;

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
public class EaglercraftUUID implements Comparable<EaglercraftUUID> {

	public final long msb;
	public final long lsb;

	public EaglercraftUUID(long msb, long lsb) {
		this.msb = msb;
		this.lsb = lsb;
	}

	public EaglercraftUUID(byte[] uuid) {
		long msb = 0;
		long lsb = 0;
		for (int i = 0; i < 8; i++)
			msb = (msb << 8) | (uuid[i] & 0xff);
		for (int i = 8; i < 16; i++)
			lsb = (lsb << 8) | (uuid[i] & 0xff);
		this.msb = msb;
		this.lsb = lsb;
	}

	public EaglercraftUUID(String uuid) {
		String[] components = uuid.split("-");
		if (components.length != 5)
			throw new IllegalArgumentException("Invalid UUID string: " + uuid);
		for (int i = 0; i < 5; i++)
			components[i] = "0x" + components[i];

		long mostSigBits = Long.decode(components[0]).longValue();
		mostSigBits <<= 16;
		mostSigBits |= Long.decode(components[1]).longValue();
		mostSigBits <<= 16;
		mostSigBits |= Long.decode(components[2]).longValue();

		long leastSigBits = Long.decode(components[3]).longValue();
		leastSigBits <<= 48;
		leastSigBits |= Long.decode(components[4]).longValue();

		this.msb = mostSigBits;
		this.lsb = leastSigBits;
	}

	private static byte long7(long x) {
		return (byte) (x >> 56);
	}

	private static byte long6(long x) {
		return (byte) (x >> 48);
	}

	private static byte long5(long x) {
		return (byte) (x >> 40);
	}

	private static byte long4(long x) {
		return (byte) (x >> 32);
	}

	private static byte long3(long x) {
		return (byte) (x >> 24);
	}

	private static byte long2(long x) {
		return (byte) (x >> 16);
	}

	private static byte long1(long x) {
		return (byte) (x >> 8);
	}

	private static byte long0(long x) {
		return (byte) (x);
	}

	public byte[] getBytes() {
		byte[] ret = new byte[16];
		ret[0] = long7(msb);
		ret[1] = long6(msb);
		ret[2] = long5(msb);
		ret[3] = long4(msb);
		ret[4] = long3(msb);
		ret[5] = long2(msb);
		ret[6] = long1(msb);
		ret[7] = long0(msb);
		ret[8] = long7(lsb);
		ret[9] = long6(lsb);
		ret[10] = long5(lsb);
		ret[11] = long4(lsb);
		ret[12] = long3(lsb);
		ret[13] = long2(lsb);
		ret[14] = long1(lsb);
		ret[15] = long0(lsb);
		return ret;
	}

	@Override
	public String toString() {
		return (digits(msb >> 32, 8) + "-" + digits(msb >> 16, 4) + "-" + digits(msb, 4) + "-" + digits(lsb >> 48, 4)
				+ "-" + digits(lsb, 12));
	}

	private static String digits(long val, int digits) {
		long hi = 1L << (digits * 4);
		return Long.toHexString(hi | (val & (hi - 1))).substring(1);
	}

	@Override
	public int hashCode() {
		long hilo = msb ^ lsb;
		return ((int) (hilo >> 32)) ^ (int) hilo;
	}

	@Override
	public boolean equals(Object o) {
		return (o instanceof EaglercraftUUID) && ((EaglercraftUUID) o).lsb == lsb && ((EaglercraftUUID) o).msb == msb;
	}

	public long getMostSignificantBits() {
		return msb;
	}

	public long getLeastSignificantBits() {
		return lsb;
	}

	private static final String HEX = "0123456789ABCDEF";

	private static int nibbleValue(char c) {
		int v = HEX.indexOf(Character.toUpperCase(c));
		if (v == -1) {
			return 0;
		} else {
			return v;
		}
	}

	private static long parse4Nibbles(String name, int pos) {
		int ch1 = nibbleValue(name.charAt(pos));
		int ch2 = nibbleValue(name.charAt(pos + 1));
		int ch3 = nibbleValue(name.charAt(pos + 2));
		int ch4 = nibbleValue(name.charAt(pos + 3));
		return (ch1 << 12) | (ch2 << 8) | (ch3 << 4) | ch4;
	}

	public static EaglercraftUUID fromString(String name) {
		if (name.length() == 36) {
			char ch1 = name.charAt(8);
			char ch2 = name.charAt(13);
			char ch3 = name.charAt(18);
			char ch4 = name.charAt(23);
			if (ch1 == '-' && ch2 == '-' && ch3 == '-' && ch4 == '-') {
				long msb1 = parse4Nibbles(name, 0);
				long msb2 = parse4Nibbles(name, 4);
				long msb3 = parse4Nibbles(name, 9);
				long msb4 = parse4Nibbles(name, 14);
				long lsb1 = parse4Nibbles(name, 19);
				long lsb2 = parse4Nibbles(name, 24);
				long lsb3 = parse4Nibbles(name, 28);
				long lsb4 = parse4Nibbles(name, 32);
				if ((msb1 | msb2 | msb3 | msb4 | lsb1 | lsb2 | lsb3 | lsb4) >= 0) {
					return new EaglercraftUUID(msb1 << 48 | msb2 << 32 | msb3 << 16 | msb4,
							lsb1 << 48 | lsb2 << 32 | lsb3 << 16 | lsb4);
				}
			}
		}
		return fromString1(name);
	}

	private static EaglercraftUUID fromString1(String name) {
		int len = name.length();
		if (len > 36) {
			throw new IllegalArgumentException("UUID string too large");
		}

		int dash1 = name.indexOf('-', 0);
		int dash2 = name.indexOf('-', dash1 + 1);
		int dash3 = name.indexOf('-', dash2 + 1);
		int dash4 = name.indexOf('-', dash3 + 1);
		int dash5 = name.indexOf('-', dash4 + 1);

		if (dash4 < 0 || dash5 >= 0) {
			throw new IllegalArgumentException("Invalid UUID string: " + name);
		}

		long mostSigBits = JDKBackports.parseLong(name, 0, dash1, 16) & 0xffffffffL;
		mostSigBits <<= 16;
		mostSigBits |= JDKBackports.parseLong(name, dash1 + 1, dash2, 16) & 0xffffL;
		mostSigBits <<= 16;
		mostSigBits |= JDKBackports.parseLong(name, dash2 + 1, dash3, 16) & 0xffffL;
		long leastSigBits = JDKBackports.parseLong(name, dash3 + 1, dash4, 16) & 0xffffL;
		leastSigBits <<= 48;
		leastSigBits |= JDKBackports.parseLong(name, dash4 + 1, len, 16) & 0xffffffffffffL;

		return new EaglercraftUUID(mostSigBits, leastSigBits);
	}

	public static EaglercraftUUID nameUUIDFromBytes(byte[] bytes) {
		MD5Digest dg = new MD5Digest();
		dg.update(bytes, 0, bytes.length);
		byte[] md5Bytes = new byte[16];
		dg.doFinal(md5Bytes, 0);
		md5Bytes[6] &= 0x0f;
		md5Bytes[6] |= 0x30;
		md5Bytes[8] &= 0x3f;
		md5Bytes[8] |= 0x80;
		return new EaglercraftUUID(md5Bytes);
	}

	@Override
	public int compareTo(EaglercraftUUID val) {
		return (this.msb < val.msb ? -1
				: (this.msb > val.msb ? 1 : (this.lsb < val.lsb ? -1 : (this.lsb > val.lsb ? 1 : 0))));
	}

}