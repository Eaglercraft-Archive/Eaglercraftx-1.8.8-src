package net.lax1dude.eaglercraft.v1_8.webview;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.HashKey;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketServerInfoDataChunkV4EAG;

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
public class ServerInfoCache {

	public static final int CACHE_MAX_SIZE = 0x200000; // 2 MB

	private static final Map<HashKey,CacheEntry> cache = new HashMap<>();
	private static int cacheSize = 0;

	private static class CacheEntry {

		private final byte[] data;
		private final HashKey hash;
		private long lastHit;

		private CacheEntry(byte[] data, HashKey hash) {
			this.data = data;
			this.hash = hash;
			this.lastHit = EagRuntime.steadyTimeMillis();
		}

	}

	protected static final List<byte[]> chunkRecieveBuffer = new LinkedList<>();
	protected static byte[] chunkRecieveHash = null;
	protected static int chunkCurrentSize = 0;
	protected static int chunkFinalSize = 0;
	protected static boolean hasLastChunk = false;

	public static void handleChunk(SPacketServerInfoDataChunkV4EAG chunk) {
		//System.out.println("p: " + chunk.seqId + " " + chunk.finalSize + " " + Base64.encodeBase64String(chunk.finalHash) + " " + chunk.lastChunk);
		if (chunkRecieveHash == null || hasLastChunk || !Arrays.equals(chunk.finalHash, chunkRecieveHash)
				|| chunk.seqId != chunkRecieveBuffer.size()) {
			chunkRecieveBuffer.clear();
			hasLastChunk = false;
			chunkRecieveHash = null;
			chunkCurrentSize = 0;
			chunkFinalSize = 0;
			if(chunk.seqId != 0) {
				return;
			}
			chunkRecieveHash = chunk.finalHash;
		}
		chunkRecieveBuffer.add(chunk.data);
		chunkCurrentSize += chunk.data.length;
		chunkFinalSize = chunk.finalSize;
		hasLastChunk = chunk.lastChunk;
	}

	public static void clearDownload() {
		chunkRecieveBuffer.clear();
		hasLastChunk = false;
		chunkRecieveHash = null;
		chunkCurrentSize = 0;
		chunkFinalSize = 0;
	}

	public static byte[] loadFromCache(byte[] hash) {
		if(hash == null || hash.length != 20) {
			return null;
		}
		CacheEntry etr = cache.get(new HashKey(hash));
		if(etr != null) {
			etr.lastHit = EagRuntime.steadyTimeMillis();
			return etr.data;
		}else {
			return null;
		}
	}

	public static void storeInCache(byte[] hash, byte[] data) {
		if(hash == null || hash.length != 20 || data == null) {
			return;
		}
		HashKey hashObj = new HashKey(hash);
		if(cache.containsKey(hashObj)) {
			return;
		}
		shrink(data.length);
		cache.put(hashObj, new CacheEntry(data, hashObj));
		cacheSize += data.length;
	}

	private static void shrink(int toAdd) {
		if(toAdd > CACHE_MAX_SIZE) {
			cache.clear();
			cacheSize = 0;
			return;
		}
		while(!cache.isEmpty() && cacheSize + toAdd > CACHE_MAX_SIZE) {
			CacheEntry oldest = null;
			for(CacheEntry e : cache.values()) {
				if(oldest == null || e.lastHit < oldest.lastHit) {
					oldest = e;
				}
			}
			if(cache.remove(oldest.hash) != null) {
				cacheSize -= oldest.data.length;
			}else {
				break; //wtf?
			}
		}
	}

}
