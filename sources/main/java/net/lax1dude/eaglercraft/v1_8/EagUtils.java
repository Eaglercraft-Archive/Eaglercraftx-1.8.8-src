package net.lax1dude.eaglercraft.v1_8;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import net.lax1dude.eaglercraft.v1_8.internal.PlatformRuntime;

/**
 * Copyright (c) 2022-2023 lax1dude, ayunami2000. All Rights Reserved.
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
public class EagUtils {
	
	private static final String hex = "0123456789ABCDEF";
	public static final Pattern splitPattern = Pattern.compile("(\\r\\n|\\n|\\r)");
	
	public static String hexString(int value, int digits) {
		String ret = "";
		for(int i = 0, l = digits << 2; i < l; i += 4) {
			ret = hex.charAt((value >> i) & 0xF) + ret;
		}
		return ret;
	}

	public static String[] linesArray(String input) {
		return splitPattern.split(input);
	}
	
	public static List<String> linesList(String input) {
		return Arrays.asList(splitPattern.split(input));
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
	
	public static void sleep(int millis) {
		PlatformRuntime.sleep(millis);
	}
	
	public static void sleep(long millis) {
		int reduced = (int)millis;
		if(reduced != millis) throw new IllegalArgumentException();
		PlatformRuntime.sleep(reduced);
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

	public static EaglercraftUUID makeClientBrandUUID(String name) {
		return EaglercraftUUID.nameUUIDFromBytes(("EaglercraftXClient:" + name).getBytes(StandardCharsets.UTF_8));
	}

	public static EaglercraftUUID makeClientBrandUUIDLegacy(String name) {
		return EaglercraftUUID.nameUUIDFromBytes(("EaglercraftXClientOld:" + name).getBytes(StandardCharsets.UTF_8));
	}

	public static void sleepPrint(String string) {
		System.out.println(string);
		PlatformRuntime.sleep(500);
	}

}
