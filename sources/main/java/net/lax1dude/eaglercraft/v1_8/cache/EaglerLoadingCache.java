package net.lax1dude.eaglercraft.v1_8.cache;

import java.util.HashMap;
import java.util.Map;

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
public class EaglerLoadingCache<K, V> {

	private final EaglerCacheProvider<K, V> provider;
	private final Map<K, V> cacheMap;

	public EaglerLoadingCache(EaglerCacheProvider<K, V> provider) {
		this.provider = provider;
		this.cacheMap = new HashMap();
	}

	public V get(K key) {
		V etr = cacheMap.get(key);
		if(etr == null) {
			etr = provider.create(key);
			cacheMap.put(key, etr);
		}
		return etr;
	}

}
