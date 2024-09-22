package net.lax1dude.eaglercraft.v1_8.internal.teavm;

import java.util.Arrays;
import java.util.List;

import org.teavm.jso.core.JSString;

import net.lax1dude.eaglercraft.v1_8.Base64;

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
public class Base64VarIntArray {

	public static String encodeVarIntArray(List<Integer> values) {
		StringBuilder ret = new StringBuilder();
		for(int i = 0, j, k, l = values.size(); i < l; ++i) {
			j = values.get(i);
			if(j < 0) j = 0;
			for(;;) {
				k = j & 31;
				if(j > 31) {
					j >>>= 5;
					ret.append(Base64.lookupIntChar(k | 32));
				}else {
					ret.append(Base64.lookupIntChar(k));
					break;
				}
			}
		}
		return ret.toString();
	}

	public static String encodeVarIntArray(int[] values) {
		StringBuilder ret = new StringBuilder();
		for(int i = 0, j, k; i < values.length; ++i) {
			j = values[i];
			if(j < 0) j = 0;
			for(;;) {
				k = j & 31;
				if(j > 31) {
					j >>>= 5;
					ret.append(Base64.lookupIntChar(k | 32));
				}else {
					ret.append(Base64.lookupIntChar(k));
					break;
				}
			}
		}
		return ret.toString();
	}

	public static int[] decodeVarIntArray(String values) {
		int[] ret = new int[8];
		int o = 0;
		for(int i = 0, j, k, m, l = values.length(); i < l;) {
			k = 0;
			m = 0;
			for(;;) {
				j = Base64.lookupCharInt(values.charAt(i++));
				if(j == -1) {
					return null;
				}
				k |= (j & 31) << m;
				if(j > 31) {
					if(i >= l) {
						return null;
					}
					m += 5;
				}else {
					break;
				}
			}
			j = ret.length;
			if(o >= j) {
				int[] newRet = new int[j << 1];
				System.arraycopy(ret, 0, newRet, 0, j);
				ret = newRet;
			}
			ret[o++] = k;
		}
		return o != ret.length ? Arrays.copyOf(ret, o) : ret;
	}

	public static int[] decodeVarIntArray(JSString values) {
		int[] ret = new int[8];
		int o = 0;
		for(int i = 0, j, k, m, l = values.getLength(); i < l;) {
			k = 0;
			m = 0;
			for(;;) {
				j = Base64.lookupCharInt((char)values.charCodeAt(i++));
				if(j == -1) {
					return null;
				}
				k |= (j & 31) << m;
				if(j > 31) {
					if(i >= l) {
						return null;
					}
					m += 5;
				}else {
					break;
				}
			}
			j = ret.length;
			if(o >= j) {
				int[] newRet = new int[j << 1];
				System.arraycopy(ret, 0, newRet, 0, j);
				ret = newRet;
			}
			ret[o++] = k;
		}
		return o != ret.length ? Arrays.copyOf(ret, o) : ret;
	}

}
