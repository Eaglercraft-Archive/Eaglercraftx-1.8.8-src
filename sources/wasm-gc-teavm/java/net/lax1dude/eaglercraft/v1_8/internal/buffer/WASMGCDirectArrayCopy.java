package net.lax1dude.eaglercraft.v1_8.internal.buffer;

import org.teavm.interop.Address;
import org.teavm.interop.Unmanaged;

/**
 * Copyright (c) 2024 lax1dude. All Rights Reserved.
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
public class WASMGCDirectArrayCopy {

	@Unmanaged
	public static void memcpy(Address dest, byte[] src, int srcOffset, int count) {
		Address destEnd = dest.add(count);
		while(dest.isLessThan(destEnd)) {
			dest.putByte(src[srcOffset]);
			++srcOffset;
			dest = dest.add(1);
		}
	}

	@Unmanaged
	public static void memcpy(Address dest, short[] src, int srcOffset, int count) {
		Address destEnd = dest.add(count << 1);
		while(dest.isLessThan(destEnd)) {
			dest.putShort(src[srcOffset]);
			++srcOffset;
			dest = dest.add(2);
		}
	}

	@Unmanaged
	public static void memcpy(Address dest, char[] src, int srcOffset, int count) {
		Address destEnd = dest.add(count << 1);
		while(dest.isLessThan(destEnd)) {
			dest.putChar(src[srcOffset]);
			++srcOffset;
			dest = dest.add(2);
		}
	}

	@Unmanaged
	public static void memcpy(Address dest, int[] src, int srcOffset, int count) {
		Address destEnd = dest.add(count << 2);
		while(dest.isLessThan(destEnd)) {
			dest.putInt(src[srcOffset]);
			++srcOffset;
			dest = dest.add(4);
		}
	}

	@Unmanaged
	public static void memcpy(Address dest, float[] src, int srcOffset, int count) {
		Address destEnd = dest.add(count << 2);
		while(dest.isLessThan(destEnd)) {
			dest.putFloat(src[srcOffset]);
			++srcOffset;
			dest = dest.add(4);
		}
	}

	@Unmanaged
	public static void memcpy(byte[] dest, int destOffset, Address src, int count) {
		Address srcEnd = src.add(count);
		while(src.isLessThan(srcEnd)) {
			dest[destOffset] = src.getByte();
			++destOffset;
			src = src.add(1);
		}
	}

	@Unmanaged
	public static void memcpy(short[] dest, int destOffset, Address src, int count) {
		Address srcEnd = src.add(count << 1);
		while(src.isLessThan(srcEnd)) {
			dest[destOffset] = src.getShort();
			++destOffset;
			src = src.add(2);
		}
	}

	@Unmanaged
	public static void memcpy(char[] dest, int destOffset, Address src, int count) {
		Address srcEnd = src.add(count << 1);
		while(src.isLessThan(srcEnd)) {
			dest[destOffset] = src.getChar();
			++destOffset;
			src = src.add(2);
		}
	}

	@Unmanaged
	public static void memcpy(int[] dest, int destOffset, Address src, int count) {
		Address srcEnd = src.add(count << 2);
		while(src.isLessThan(srcEnd)) {
			dest[destOffset] = src.getInt();
			++destOffset;
			src = src.add(4);
		}
	}

	@Unmanaged
	public static void memcpy(float[] dest, int destOffset, Address src, int count) {
		Address srcEnd = src.add(count << 2);
		while(src.isLessThan(srcEnd)) {
			dest[destOffset] = src.getFloat();
			++destOffset;
			src = src.add(4);
		}
	}

}
