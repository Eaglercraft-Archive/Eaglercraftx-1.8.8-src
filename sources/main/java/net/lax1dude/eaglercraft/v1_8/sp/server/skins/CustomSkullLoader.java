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

package net.lax1dude.eaglercraft.v1_8.sp.server.skins;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.crypto.SHA1Digest;
import net.lax1dude.eaglercraft.v1_8.internal.vfs2.VFile2;
import net.lax1dude.eaglercraft.v1_8.sp.server.WorldsDB;

public class CustomSkullLoader {

	private static final byte[] skullNotFoundTexture = new byte[4096];

	static {
		for(int y = 0; y < 16; ++y) {
			for(int x = 0; x < 64; ++x) {
				int i = (y << 8) | (x << 2);
				byte j = ((x + y) & 1) == 1 ? (byte)255 : 0;
				skullNotFoundTexture[i] = (byte)255;
				skullNotFoundTexture[i + 1] = j;
				skullNotFoundTexture[i + 2] = 0;
				skullNotFoundTexture[i + 3] = j;
			}
		}
	}

	private final VFile2 folder;

	private final Map<String,CustomSkullData> customSkulls = new HashMap<>(64);

	private long lastFlush = 0l;

	public CustomSkullLoader(VFile2 folder) {
		this.folder = folder;
	}

	private CustomSkullData loadSkullData0(String urlStr) {
		byte[] data = WorldsDB.newVFile(folder, urlStr).getAllBytes();
		if(data == null) {
			return new CustomSkullData(skullNotFoundTexture);
		}else {
			return new CustomSkullData(data);
		}
	}

	public CustomSkullData loadSkullData(String url) {
		CustomSkullData sk = customSkulls.get(url);
		if(sk == null) {
			customSkulls.put(url, sk = loadSkullData0(url));
		}else {
			sk.lastHit = EagRuntime.steadyTimeMillis();
		}
		return sk;
	}

	private static final String hex = "0123456789abcdef";

	public String installNewSkull(byte[] skullData) {
		// set to 16384 to save a full 64x64 skin
		if(skullData.length > 4096) {
			byte[] tmp = skullData;
			skullData = new byte[4096];
			System.arraycopy(tmp, 0, skullData, 0, 4096);
		}
		SHA1Digest sha = new SHA1Digest();
		sha.update(skullData, 0, skullData.length);
		byte[] hash = new byte[20];
		sha.doFinal(hash, 0);
		char[] hashText = new char[40];
		for(int i = 0; i < 20; ++i) {
			hashText[i << 1] = hex.charAt((hash[i] & 0xF0) >> 4);
			hashText[(i << 1) + 1] = hex.charAt(hash[i] & 0x0F);
		}
		String str = "skin-" + new String(hashText) + ".bmp";
		customSkulls.put(str, new CustomSkullData(skullData));
		WorldsDB.newVFile(folder, str).setAllBytes(skullData);
		return str;
	}

	public void flushCache() {
		long cur = EagRuntime.steadyTimeMillis();
		if(cur - lastFlush > 300000l) {
			lastFlush = cur;
			Iterator<CustomSkullData> customSkullsItr = customSkulls.values().iterator();
			while(customSkullsItr.hasNext()) {
				if(cur - customSkullsItr.next().lastHit > 900000l) {
					customSkullsItr.remove();
				}
			}
		}
	}

}
