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

import java.util.function.Supplier;

public class JDKBackports {
	
	public static long parseLong(CharSequence s, int beginIndex, int endIndex, int radix) throws NumberFormatException {
		if (beginIndex < 0 || beginIndex > endIndex || endIndex > s.length()) {
			throw new IndexOutOfBoundsException();
		}
		if (radix < Character.MIN_RADIX) {
			throw new NumberFormatException("radix " + radix + " less than Character.MIN_RADIX");
		}
		if (radix > Character.MAX_RADIX) {
			throw new NumberFormatException("radix " + radix + " greater than Character.MAX_RADIX");
		}

		boolean negative = false;
		int i = beginIndex;
		long limit = -Long.MAX_VALUE;

		if (i < endIndex) {
			char firstChar = s.charAt(i);
			if (firstChar < '0') { // Possible leading "+" or "-"
				if (firstChar == '-') {
					negative = true;
					limit = Long.MIN_VALUE;
				} else if (firstChar != '+') {
					throw new NumberFormatException();
				}
				i++;
			}
			if (i >= endIndex) { // Cannot have lone "+", "-" or ""
				throw new NumberFormatException();
			}
			long multmin = limit / radix;
			long result = 0;
			while (i < endIndex) {
				// Accumulating negatively avoids surprises near MAX_VALUE
				int digit = Character.digit(s.charAt(i), radix);
				if (digit < 0 || result < multmin) {
					throw new NumberFormatException();
				}
				result *= radix;
				if (result < limit + digit) {
					throw new NumberFormatException();
				}
				i++;
				result -= digit;
			}
			return negative ? result : -result;
		} else {
			throw new NumberFormatException("");
		}
	}
	
	public static <T> T javaUtilObject_requireNonNull(T obj, Supplier<String> messageSupplier) {
		if (obj == null)
			throw new NullPointerException(messageSupplier.get());
		return obj;
	}
	
	public static <T> T javaUtilObject_requireNonNull(T obj, String message) {
		if (obj == null)
			throw new NullPointerException(message);
		return obj;
	}
	
	public static <T> T javaUtilObject_requireNonNull(T obj) {
		if (obj == null)
			throw new NullPointerException();
		return obj;
	}

}