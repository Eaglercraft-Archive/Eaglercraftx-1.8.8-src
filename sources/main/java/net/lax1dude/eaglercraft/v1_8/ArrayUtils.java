/*
 * Copyright (c) 2022 lax1dude. All Rights Reserved.
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

import java.util.List;

import net.minecraft.client.settings.KeyBinding;

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
	
	private static final String hex = "0123456789abcdef";
	
	public static String hexString(byte[] bytesIn) {
		char[] ret = new char[bytesIn.length << 1];
		for(int i = 0; i < bytesIn.length; ++i) {
			ret[i << 1] = hex.charAt((bytesIn[i] >>> 4) & 15);
			ret[(i << 1) + 1] = hex.charAt(bytesIn[i] & 15);
		}
		return new String(ret);
	}

	public static <T> void eaglerShuffle(List<T> list, EaglercraftRandom rnd) {
		T k;
		for (int i = list.size() - 1, j; i > 0; --i) {
            j = rnd.nextInt(i + 1);
            k = list.get(j);
            list.set(j, list.get(i));
            list.set(i, k);
        }
    }

}