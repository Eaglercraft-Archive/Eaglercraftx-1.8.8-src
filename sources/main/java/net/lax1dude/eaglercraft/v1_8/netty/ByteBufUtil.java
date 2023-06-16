/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package net.lax1dude.eaglercraft.v1_8.netty;

import net.lax1dude.eaglercraft.v1_8.EagUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.StandardCharsets;

import static net.lax1dude.eaglercraft.v1_8.netty.ObjectUtil.checkNotNull;

/**
 * A collection of utility methods that is related with handling
 * {@link ByteBuf}, such as the generation of hex dump and swapping an integer's
 * byte order.
 */
public final class ByteBufUtil {

	private static final byte WRITE_UTF_UNKNOWN = (byte) '?';
	private static final int MAX_BYTES_PER_CHAR_UTF8 = 3;

	/**
	 * Decode a 2-digit hex byte from within a string.
	 */
	public static byte decodeHexByte(CharSequence s, int pos) {
		return (byte)EagUtils.decodeHexByte(s, pos);
	}

	/**
	 * Calculates the hash code of the specified buffer. This method is useful when
	 * implementing a new buffer type.
	 */
	public static int hashCode(ByteBuf buffer) {
		final int aLen = buffer.readableBytes();
		final int intCount = aLen >>> 2;
		final int byteCount = aLen & 3;

		int hashCode = 1;
		int arrayIndex = buffer.readerIndex();
		if (buffer.order() == ByteOrder.BIG_ENDIAN) {
			for (int i = intCount; i > 0; i--) {
				hashCode = 31 * hashCode + buffer.getInt(arrayIndex);
				arrayIndex += 4;
			}
		} else {
			for (int i = intCount; i > 0; i--) {
				hashCode = 31 * hashCode + swapInt(buffer.getInt(arrayIndex));
				arrayIndex += 4;
			}
		}

		for (int i = byteCount; i > 0; i--) {
			hashCode = 31 * hashCode + buffer.getByte(arrayIndex++);
		}

		if (hashCode == 0) {
			hashCode = 1;
		}

		return hashCode;
	}

	/**
	 * Returns {@code true} if and only if the two specified buffers are identical
	 * to each other as described in {@code ChannelBuffer#equals(Object)}. This
	 * method is useful when implementing a new buffer type.
	 */
	public static boolean equals(ByteBuf bufferA, ByteBuf bufferB) {
		final int aLen = bufferA.readableBytes();
		if (aLen != bufferB.readableBytes()) {
			return false;
		}

		final int longCount = aLen >>> 3;
		final int byteCount = aLen & 7;

		int aIndex = bufferA.readerIndex();
		int bIndex = bufferB.readerIndex();

		if (bufferA.order() == bufferB.order()) {
			for (int i = longCount; i > 0; i--) {
				if (bufferA.getLong(aIndex) != bufferB.getLong(bIndex)) {
					return false;
				}
				aIndex += 8;
				bIndex += 8;
			}
		} else {
			for (int i = longCount; i > 0; i--) {
				if (bufferA.getLong(aIndex) != swapLong(bufferB.getLong(bIndex))) {
					return false;
				}
				aIndex += 8;
				bIndex += 8;
			}
		}

		for (int i = byteCount; i > 0; i--) {
			if (bufferA.getByte(aIndex) != bufferB.getByte(bIndex)) {
				return false;
			}
			aIndex++;
			bIndex++;
		}

		return true;
	}

	/**
	 * Compares the two specified buffers as described in
	 * {@link ByteBuf#compareTo(ByteBuf)}. This method is useful when implementing a
	 * new buffer type.
	 */
	public static int compare(ByteBuf bufferA, ByteBuf bufferB) {
		final int aLen = bufferA.readableBytes();
		final int bLen = bufferB.readableBytes();
		final int minLength = Math.min(aLen, bLen);
		final int uintCount = minLength >>> 2;
		final int byteCount = minLength & 3;
		int aIndex = bufferA.readerIndex();
		int bIndex = bufferB.readerIndex();

		if (uintCount > 0) {
			boolean bufferAIsBigEndian = bufferA.order() == ByteOrder.BIG_ENDIAN;
			final long res;
			int uintCountIncrement = uintCount << 2;

			if (bufferA.order() == bufferB.order()) {
				res = bufferAIsBigEndian ? compareUintBigEndian(bufferA, bufferB, aIndex, bIndex, uintCountIncrement)
						: compareUintLittleEndian(bufferA, bufferB, aIndex, bIndex, uintCountIncrement);
			} else {
				res = bufferAIsBigEndian ? compareUintBigEndianA(bufferA, bufferB, aIndex, bIndex, uintCountIncrement)
						: compareUintBigEndianB(bufferA, bufferB, aIndex, bIndex, uintCountIncrement);
			}
			if (res != 0) {
				// Ensure we not overflow when cast
				return (int) Math.min(Integer.MAX_VALUE, Math.max(Integer.MIN_VALUE, res));
			}
			aIndex += uintCountIncrement;
			bIndex += uintCountIncrement;
		}

		for (int aEnd = aIndex + byteCount; aIndex < aEnd; ++aIndex, ++bIndex) {
			int comp = bufferA.getUnsignedByte(aIndex) - bufferB.getUnsignedByte(bIndex);
			if (comp != 0) {
				return comp;
			}
		}

		return aLen - bLen;
	}

	private static long compareUintBigEndian(ByteBuf bufferA, ByteBuf bufferB, int aIndex, int bIndex,
			int uintCountIncrement) {
		for (int aEnd = aIndex + uintCountIncrement; aIndex < aEnd; aIndex += 4, bIndex += 4) {
			long comp = bufferA.getUnsignedInt(aIndex) - bufferB.getUnsignedInt(bIndex);
			if (comp != 0) {
				return comp;
			}
		}
		return 0;
	}

	private static long compareUintLittleEndian(ByteBuf bufferA, ByteBuf bufferB, int aIndex, int bIndex,
			int uintCountIncrement) {
		for (int aEnd = aIndex + uintCountIncrement; aIndex < aEnd; aIndex += 4, bIndex += 4) {
			long comp = (swapInt(bufferA.getInt(aIndex)) & 0xFFFFFFFFL)
					- (swapInt(bufferB.getInt(bIndex)) & 0xFFFFFFFFL);
			if (comp != 0) {
				return comp;
			}
		}
		return 0;
	}

	private static long compareUintBigEndianA(ByteBuf bufferA, ByteBuf bufferB, int aIndex, int bIndex,
			int uintCountIncrement) {
		for (int aEnd = aIndex + uintCountIncrement; aIndex < aEnd; aIndex += 4, bIndex += 4) {
			long comp = bufferA.getUnsignedInt(aIndex) - (swapInt(bufferB.getInt(bIndex)) & 0xFFFFFFFFL);
			if (comp != 0) {
				return comp;
			}
		}
		return 0;
	}

	private static long compareUintBigEndianB(ByteBuf bufferA, ByteBuf bufferB, int aIndex, int bIndex,
			int uintCountIncrement) {
		for (int aEnd = aIndex + uintCountIncrement; aIndex < aEnd; aIndex += 4, bIndex += 4) {
			long comp = (swapInt(bufferA.getInt(aIndex)) & 0xFFFFFFFFL) - bufferB.getUnsignedInt(bIndex);
			if (comp != 0) {
				return comp;
			}
		}
		return 0;
	}

	/**
	 * The default implementation of {@link ByteBuf#indexOf(int, int, byte)}. This
	 * method is useful when implementing a new buffer type.
	 */
	public static int indexOf(ByteBuf buffer, int fromIndex, int toIndex, byte value) {
		if (fromIndex <= toIndex) {
			return firstIndexOf(buffer, fromIndex, toIndex, value);
		} else {
			return lastIndexOf(buffer, fromIndex, toIndex, value);
		}
	}

	/**
	 * Toggles the endianness of the specified 16-bit short integer.
	 */
	public static short swapShort(short value) {
		return Short.reverseBytes(value);
	}

	/**
	 * Toggles the endianness of the specified 24-bit medium integer.
	 */
	public static int swapMedium(int value) {
		int swapped = value << 16 & 0xff0000 | value & 0xff00 | value >>> 16 & 0xff;
		if ((swapped & 0x800000) != 0) {
			swapped |= 0xff000000;
		}
		return swapped;
	}

	/**
	 * Toggles the endianness of the specified 32-bit integer.
	 */
	public static int swapInt(int value) {
		return Integer.reverseBytes(value);
	}

	/**
	 * Toggles the endianness of the specified 64-bit long integer.
	 */
	public static long swapLong(long value) {
		return Long.reverseBytes(value);
	}

	/**
	 * Read the given amount of bytes into a new {@link ByteBuf} that is allocated
	 * from the {@link ByteBufAllocator}.
	 */
	public static ByteBuf readBytes(ByteBuf buffer, int length) {
		ByteBuf dst = Unpooled.buffer(length);
		buffer.readBytes(dst);
		return dst;
	}

	private static int firstIndexOf(ByteBuf buffer, int fromIndex, int toIndex, byte value) {
		fromIndex = Math.max(fromIndex, 0);
		if (fromIndex >= toIndex || buffer.capacity() == 0) {
			return -1;
		}
		for(int i = fromIndex; i < toIndex; ++i) {
			if(buffer.getByte(i) == value) {
				return i;
			}
		}
		return -1;
	}

	private static int lastIndexOf(ByteBuf buffer, int fromIndex, int toIndex, byte value) {
		fromIndex = Math.min(fromIndex, buffer.capacity());
		if (fromIndex < 0 || buffer.capacity() == 0) {
			return -1;
		}
		for(int i = fromIndex; i > toIndex; --i) {
			if(buffer.getByte(i) == value) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Encode a {@link CharSequence} in
	 * <a href="http://en.wikipedia.org/wiki/UTF-8">UTF-8</a> and write it to a
	 * {@link ByteBuf} allocated with {@code alloc}.
	 * 
	 * @param alloc The allocator used to allocate a new {@link ByteBuf}.
	 * @param seq   The characters to write into a buffer.
	 * @return The {@link ByteBuf} which contains the
	 *         <a href="http://en.wikipedia.org/wiki/UTF-8">UTF-8</a> encoded
	 *         result.
	 */
	public static ByteBuf writeUtf8(CharSequence seq) {
		// UTF-8 uses max. 3 bytes per char, so calculate the worst case.
		ByteBuf buf = Unpooled.buffer(seq.length() * MAX_BYTES_PER_CHAR_UTF8);
		writeUtf8(buf, seq);
		return buf;
	}

	/**
	 * Encode a {@link CharSequence} in
	 * <a href="http://en.wikipedia.org/wiki/UTF-8">UTF-8</a> and write it to a
	 * {@link ByteBuf}.
	 *
	 * This method returns the actual number of bytes written.
	 */
	public static int writeUtf8(ByteBuf buf, CharSequence seq) {
		final int len = seq.length();
		buf.ensureWritable(len * MAX_BYTES_PER_CHAR_UTF8);
		byte[] bytes = seq.toString().getBytes(StandardCharsets.UTF_8);
		buf.writeBytes(bytes);
		return bytes.length;
	}

	// Fast-Path implementation
	private static int writeUtf8(AbstractByteBuf buffer, CharSequence seq, int len) {
		int oldWriterIndex = buffer.writerIndex;
		int writerIndex = oldWriterIndex;

		// We can use the _set methods as these not need to do any index checks and
		// reference checks.
		// This is possible as we called ensureWritable(...) before.
		for (int i = 0; i < len; i++) {
			char c = seq.charAt(i);
			if (c < 0x80) {
				buffer._setByte(writerIndex++, (byte) c);
			} else if (c < 0x800) {
				buffer._setByte(writerIndex++, (byte) (0xc0 | (c >> 6)));
				buffer._setByte(writerIndex++, (byte) (0x80 | (c & 0x3f)));
			} else if (c >= '\uD800' && c <= '\uDFFF') { // isSurrogate(c)
				if (!Character.isHighSurrogate(c)) {
					buffer._setByte(writerIndex++, WRITE_UTF_UNKNOWN);
					continue;
				}
				final char c2;
				try {
					// Surrogate Pair consumes 2 characters. Optimistically try to get the next
					// character to avoid
					// duplicate bounds checking with charAt. If an IndexOutOfBoundsException is
					// thrown we will
					// re-throw a more informative exception describing the problem.
					c2 = seq.charAt(++i);
				} catch (IndexOutOfBoundsException e) {
					buffer._setByte(writerIndex++, WRITE_UTF_UNKNOWN);
					break;
				}
				if (!Character.isLowSurrogate(c2)) {
					buffer._setByte(writerIndex++, WRITE_UTF_UNKNOWN);
					buffer._setByte(writerIndex++, Character.isHighSurrogate(c2) ? WRITE_UTF_UNKNOWN : c2);
					continue;
				}
				int codePoint = Character.toCodePoint(c, c2);
				// See http://www.unicode.org/versions/Unicode7.0.0/ch03.pdf#G2630.
				buffer._setByte(writerIndex++, (byte) (0xf0 | (codePoint >> 18)));
				buffer._setByte(writerIndex++, (byte) (0x80 | ((codePoint >> 12) & 0x3f)));
				buffer._setByte(writerIndex++, (byte) (0x80 | ((codePoint >> 6) & 0x3f)));
				buffer._setByte(writerIndex++, (byte) (0x80 | (codePoint & 0x3f)));
			} else {
				buffer._setByte(writerIndex++, (byte) (0xe0 | (c >> 12)));
				buffer._setByte(writerIndex++, (byte) (0x80 | ((c >> 6) & 0x3f)));
				buffer._setByte(writerIndex++, (byte) (0x80 | (c & 0x3f)));
			}
		}
		// update the writerIndex without any extra checks for performance reasons
		buffer.writerIndex = writerIndex;
		return writerIndex - oldWriterIndex;
	}

	/**
	 * Encode the given {@link CharBuffer} using the given {@link Charset} into a
	 * new {@link ByteBuf} which is allocated via the {@link ByteBufAllocator}.
	 */
	public static ByteBuf encodeString(CharBuffer src, Charset charset) {
		return encodeString0(false, src, charset);
	}

	static ByteBuf encodeString0(boolean enforceHeap, CharBuffer src, Charset charset) {
		final CharsetEncoder encoder = charset.newEncoder();
		int length = (int) ((double) src.remaining() * encoder.maxBytesPerChar());
		final ByteBuf dst = Unpooled.buffer(length);
		try {
			final ByteBuffer dstBuf = dst.internalNioBuffer(dst.readerIndex(), length);
			final int pos = dstBuf.position();
			CoderResult cr = encoder.encode(src, dstBuf, true);
			if (!cr.isUnderflow()) {
				cr.throwException();
			}
			cr = encoder.flush(dstBuf);
			if (!cr.isUnderflow()) {
				cr.throwException();
			}
			dst.writerIndex(dst.writerIndex() + dstBuf.position() - pos);
			return dst;
		} catch (CharacterCodingException x) {
			throw new IllegalStateException(x);
		}
	}

	static String decodeString(ByteBuf src, int readerIndex, int len, Charset charset) {
		if (len == 0) {
			return "";
		}
		final CharsetDecoder decoder = charset.newDecoder();
		final int maxLength = (int) ((double) len * decoder.maxCharsPerByte());
		CharBuffer dst = CharBuffer.wrap(new char[maxLength]);
		decodeString(decoder, src.nioBuffer(readerIndex, len), dst);
		return dst.flip().toString();
	}

	private static void decodeString(CharsetDecoder decoder, ByteBuffer src, CharBuffer dst) {
		try {
			CoderResult cr = decoder.decode(src, dst, true);
			if (!cr.isUnderflow()) {
				cr.throwException();
			}
			cr = decoder.flush(dst);
			if (!cr.isUnderflow()) {
				cr.throwException();
			}
		} catch (CharacterCodingException x) {
			throw new IllegalStateException(x);
		}
	}

	/**
	 * Returns {@code true} if the given {@link ByteBuf} is valid text using the
	 * given {@link Charset}, otherwise return {@code false}.
	 *
	 * @param buf     The given {@link ByteBuf}.
	 * @param charset The specified {@link Charset}.
	 */
	public static boolean isText(ByteBuf buf, Charset charset) {
		return isText(buf, buf.readerIndex(), buf.readableBytes(), charset);
	}

	/**
	 * Returns {@code true} if the specified {@link ByteBuf} starting at
	 * {@code index} with {@code length} is valid text using the given
	 * {@link Charset}, otherwise return {@code false}.
	 *
	 * @param buf     The given {@link ByteBuf}.
	 * @param index   The start index of the specified buffer.
	 * @param length  The length of the specified buffer.
	 * @param charset The specified {@link Charset}.
	 *
	 * @throws IndexOutOfBoundsException if {@code index} + {@code length} is
	 *                                   greater than {@code buf.readableBytes}
	 */
	public static boolean isText(ByteBuf buf, int index, int length, Charset charset) {
		checkNotNull(buf, "buf");
		checkNotNull(charset, "charset");
		final int maxIndex = buf.readerIndex() + buf.readableBytes();
		if (index < 0 || length < 0 || index > maxIndex - length) {
			throw new IndexOutOfBoundsException("index: " + index + " length: " + length);
		}
		return isUtf8(buf, index, length);
	}

	/**
	 * Returns {@code true} if the specified {@link ByteBuf} starting at
	 * {@code index} with {@code length} is valid UTF8 text, otherwise return
	 * {@code false}.
	 *
	 * @param buf    The given {@link ByteBuf}.
	 * @param index  The start index of the specified buffer.
	 * @param length The length of the specified buffer.
	 *
	 * @see <a href=http://www.ietf.org/rfc/rfc3629.txt>UTF-8 Definition</a>
	 *
	 *      <pre>
	 * 1. Bytes format of UTF-8
	 *
	 * The table below summarizes the format of these different octet types.
	 * The letter x indicates bits available for encoding bits of the character number.
	 *
	 * Char. number range  |        UTF-8 octet sequence
	 *    (hexadecimal)    |              (binary)
	 * --------------------+---------------------------------------------
	 * 0000 0000-0000 007F | 0xxxxxxx
	 * 0000 0080-0000 07FF | 110xxxxx 10xxxxxx
	 * 0000 0800-0000 FFFF | 1110xxxx 10xxxxxx 10xxxxxx
	 * 0001 0000-0010 FFFF | 11110xxx 10xxxxxx 10xxxxxx 10xxxxxx
	 *      </pre>
	 *
	 *      <pre>
	 * 2. Syntax of UTF-8 Byte Sequences
	 *
	 * UTF8-octets = *( UTF8-char )
	 * UTF8-char   = UTF8-1 / UTF8-2 / UTF8-3 / UTF8-4
	 * UTF8-1      = %x00-7F
	 * UTF8-2      = %xC2-DF UTF8-tail
	 * UTF8-3      = %xE0 %xA0-BF UTF8-tail /
	 *               %xE1-EC 2( UTF8-tail ) /
	 *               %xED %x80-9F UTF8-tail /
	 *               %xEE-EF 2( UTF8-tail )
	 * UTF8-4      = %xF0 %x90-BF 2( UTF8-tail ) /
	 *               %xF1-F3 3( UTF8-tail ) /
	 *               %xF4 %x80-8F 2( UTF8-tail )
	 * UTF8-tail   = %x80-BF
	 *      </pre>
	 */
	private static boolean isUtf8(ByteBuf buf, int index, int length) {
		final int endIndex = index + length;
		while (index < endIndex) {
			byte b1 = buf.getByte(index++);
			byte b2, b3, b4;
			if ((b1 & 0x80) == 0) {
				// 1 byte
				continue;
			}
			if ((b1 & 0xE0) == 0xC0) {
				// 2 bytes
				//
				// Bit/Byte pattern
				// 110xxxxx 10xxxxxx
				// C2..DF 80..BF
				if (index >= endIndex) { // no enough bytes
					return false;
				}
				b2 = buf.getByte(index++);
				if ((b2 & 0xC0) != 0x80) { // 2nd byte not starts with 10
					return false;
				}
				if ((b1 & 0xFF) < 0xC2) { // out of lower bound
					return false;
				}
			} else if ((b1 & 0xF0) == 0xE0) {
				// 3 bytes
				//
				// Bit/Byte pattern
				// 1110xxxx 10xxxxxx 10xxxxxx
				// E0 A0..BF 80..BF
				// E1..EC 80..BF 80..BF
				// ED 80..9F 80..BF
				// E1..EF 80..BF 80..BF
				if (index > endIndex - 2) { // no enough bytes
					return false;
				}
				b2 = buf.getByte(index++);
				b3 = buf.getByte(index++);
				if ((b2 & 0xC0) != 0x80 || (b3 & 0xC0) != 0x80) { // 2nd or 3rd bytes not start with 10
					return false;
				}
				if ((b1 & 0x0F) == 0x00 && (b2 & 0xFF) < 0xA0) { // out of lower bound
					return false;
				}
				if ((b1 & 0x0F) == 0x0D && (b2 & 0xFF) > 0x9F) { // out of upper bound
					return false;
				}
			} else if ((b1 & 0xF8) == 0xF0) {
				// 4 bytes
				//
				// Bit/Byte pattern
				// 11110xxx 10xxxxxx 10xxxxxx 10xxxxxx
				// F0 90..BF 80..BF 80..BF
				// F1..F3 80..BF 80..BF 80..BF
				// F4 80..8F 80..BF 80..BF
				if (index > endIndex - 3) { // no enough bytes
					return false;
				}
				b2 = buf.getByte(index++);
				b3 = buf.getByte(index++);
				b4 = buf.getByte(index++);
				if ((b2 & 0xC0) != 0x80 || (b3 & 0xC0) != 0x80 || (b4 & 0xC0) != 0x80) {
					// 2nd, 3rd or 4th bytes not start with 10
					return false;
				}
				if ((b1 & 0xFF) > 0xF4 // b1 invalid
						|| (b1 & 0xFF) == 0xF0 && (b2 & 0xFF) < 0x90 // b2 out of lower bound
						|| (b1 & 0xFF) == 0xF4 && (b2 & 0xFF) > 0x8F) { // b2 out of upper bound
					return false;
				}
			} else {
				return false;
			}
		}
		return true;
	}

	private ByteBufUtil() {
	}
}
