/*
 * Copyright (c) 2025 lax1dude. All Rights Reserved.
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

import jdk_internal.bidi.Bidi;

public class EaglerBidiReorder {

	/**
	 * Taken from minecraft 1.6
	 */
	public static String bidiReorder(String par1Str) {
		if (par1Str != null && Bidi.requiresBidi(par1Str.toCharArray(), 0, par1Str.length())) {
			Bidi bidi = new Bidi(par1Str, -2);
			byte[] abyte = new byte[bidi.getRunCount()];
			String[] astring = new String[abyte.length];
			int i;

			for (int j = 0; j < abyte.length; ++j) {
				int k = bidi.getRunStart(j);
				i = bidi.getRunLimit(j);
				int l = bidi.getRunLevel(j);
				String s1 = par1Str.substring(k, i);
				abyte[j] = (byte) l;
				astring[j] = s1;
			}

			String[] astring1 = (String[]) astring.clone();
			Bidi.reorderVisually(abyte, 0, astring, 0, abyte.length);
			StringBuilder stringbuilder = new StringBuilder();
			i = 0;

			while (i < astring.length) {
				byte b0 = abyte[i];
				int i1 = 0;

				while (true) {
					if (i1 < astring1.length) {
						if (!astring1[i1].equals(astring[i])) {
							++i1;
							continue;
						}

						b0 = abyte[i1];
					}

					if ((b0 & 1) == 0) {
						stringbuilder.append(astring[i]);
					} else {
						for (i1 = astring[i].length() - 1; i1 >= 0; --i1) {
							char c0 = astring[i].charAt(i1);

							if (c0 == 40) {
								c0 = 41;
							} else if (c0 == 41) {
								c0 = 40;
							}

							stringbuilder.append(c0);
						}
					}

					++i;
					break;
				}
			}

			return stringbuilder.toString();
		} else {
			return par1Str;
		}
	}

}