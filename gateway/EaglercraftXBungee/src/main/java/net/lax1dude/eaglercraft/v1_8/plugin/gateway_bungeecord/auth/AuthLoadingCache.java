package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.auth;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

/**
 * Copyright (c) 2022-2023 LAX1DUDE. All Rights Reserved.
 * 
 * WITH THE EXCEPTION OF PATCH FILES, MINIFIED JAVASCRIPT, AND ALL FILES
 * NORMALLY FOUND IN AN UNMODIFIED MINECRAFT RESOURCE PACK, YOU ARE NOT ALLOWED
 * TO SHARE, DISTRIBUTE, OR REPURPOSE ANY FILE USED BY OR PRODUCED BY THE
 * SOFTWARE IN THIS REPOSITORY WITHOUT PRIOR PERMISSION FROM THE PROJECT AUTHOR.
 * 
 * NOT FOR COMMERCIAL OR MALICIOUS USE
 * 
 * (please read the 'LICENSE' file this repo's root directory for more info)
 * 
 */
public class AuthLoadingCache<K, V> {

	private static class CacheEntry<V> {
		
		private long lastHit;
		private V instance;
		
		private CacheEntry(V instance) {
			this.lastHit = System.currentTimeMillis();
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
		this.cacheMap = new HashMap();
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
			etr.lastHit = System.currentTimeMillis();
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
		long millis = System.currentTimeMillis();
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
