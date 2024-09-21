package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.auth;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.api.EaglerXBungeeAPIHelper;

/**
 * Copyright (c) 2022-2023 lax1dude. All Rights Reserved.
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
public class AuthLoadingCache<K, V> {

	private static class CacheEntry<V> {
		
		private long lastHit;
		private V instance;
		
		private CacheEntry(V instance) {
			this.lastHit = EaglerXBungeeAPIHelper.steadyTimeMillis();
			this.instance = instance;
		}
		
	}

	public static interface CacheLoader<K, V> {
		V load(K key);
	}

	public static interface CacheVisitor<K, V> {
		boolean shouldEvict(K key, V value);
	}

	private final Map<K, CacheEntry<V>> cacheMap;
	private final CacheLoader<K, V> provider;
	private final long cacheTTL;

	private long cacheTimer;

	public AuthLoadingCache(CacheLoader<K, V> provider, long cacheTTL) {
		this.cacheMap = new HashMap<>();
		this.provider = provider;
		this.cacheTTL = cacheTTL;
	}

	public V get(K key) {
		CacheEntry<V> etr;
		synchronized(cacheMap) {
			etr = cacheMap.get(key);
		}
		if(etr == null) {
			V loaded = provider.load(key);
			synchronized(cacheMap) {
				cacheMap.put(key, new CacheEntry<>(loaded));
			}
			return loaded;
		}else {
			etr.lastHit = EaglerXBungeeAPIHelper.steadyTimeMillis();
			return etr.instance;
		}
	}

	public void evict(K key) {
		synchronized(cacheMap) {
			cacheMap.remove(key);
		}
	}

	public void evictAll(CacheVisitor<K, V> visitor) {
		synchronized(cacheMap) {
			Iterator<Entry<K,CacheEntry<V>>> itr = cacheMap.entrySet().iterator();
			while(itr.hasNext()) {
				Entry<K,CacheEntry<V>> etr = itr.next();
				if(visitor.shouldEvict(etr.getKey(), etr.getValue().instance)) {
					itr.remove();
				}
			}
		}
	}

	public void tick() {
		long millis = EaglerXBungeeAPIHelper.steadyTimeMillis();
		if(millis - cacheTimer > (cacheTTL / 2L)) {
			cacheTimer = millis;
			synchronized(cacheMap) {
				Iterator<CacheEntry<V>> mapItr = cacheMap.values().iterator();
				while(mapItr.hasNext()) {
					CacheEntry<V> etr = mapItr.next();
					if(millis - etr.lastHit > cacheTTL) {
						mapItr.remove();
					}
				}
			}
		}
	}

	public void flush() {
		synchronized(cacheMap) {
			cacheMap.clear();
		}
	}

}
