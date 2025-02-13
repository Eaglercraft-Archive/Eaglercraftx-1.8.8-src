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

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IOUtils {

	public static List<String> readLines(InputStream parInputStream, Charset charset) {
		if(parInputStream instanceof EaglerInputStream) {
			return Arrays.asList(
					new String(((EaglerInputStream) parInputStream).getAsArray(), charset).split("(\\r\\n|\\n|\\r)"));
		}else {
			List<String> ret = new ArrayList<>();
			try(InputStream is = parInputStream) {
				BufferedReader rd = new BufferedReader(new InputStreamReader(is, charset));
				String s;
				while((s = rd.readLine()) != null) {
					ret.add(s);
				}
			}catch(IOException ex) {
				return null;
			}
			return ret;
		}
	}

	public static void closeQuietly(Closeable reResourcePack) {
		try {
			reResourcePack.close();
		}catch(Throwable t) {
		}
	}
	
	public static String inputStreamToString(InputStream is, Charset c) throws IOException {
		if(is instanceof EaglerInputStream) {
			return new String(((EaglerInputStream)is).getAsArray(), c);
		}else {
			try {
				StringBuilder b = new StringBuilder();
				BufferedReader rd = new BufferedReader(new InputStreamReader(is, c));
				String s;
				while((s = rd.readLine()) != null) {
					b.append(s).append('\n');
				}
				// Handle BOM
				if(c == StandardCharsets.UTF_8 && b.length() > 0 && b.charAt(0) == 65279) {
					return b.substring(1, b.length());
				}else {
					return b.toString();
				}
			}finally {
				is.close();
			}
		}
	}

	public static int readFully(InputStream is, byte[] out) throws IOException {
		int i = 0, j;
		while(i < out.length && (j = is.read(out, i, out.length - i)) != -1) {
			i += j;
		}
		return i;
	}

	public static long skipFully(InputStream is, long skip) throws IOException {
		long i = 0, j;
		while(i < skip && (j = is.skip(skip - i)) != 0) {
			i += j;
		}
		return i;
	}
}