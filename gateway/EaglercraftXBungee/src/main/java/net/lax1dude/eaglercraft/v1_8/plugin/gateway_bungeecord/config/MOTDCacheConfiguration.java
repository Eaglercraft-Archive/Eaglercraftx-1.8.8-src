package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.config;

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
public class MOTDCacheConfiguration {
	
	public final int cacheTTL;
	public final boolean cacheServerListAnimation;
	public final boolean cacheServerListResults;
	public final boolean cacheServerListTrending;
	public final boolean cacheServerListPortfolios;
	
	public MOTDCacheConfiguration(int cacheTTL, boolean cacheServerListAnimation, boolean cacheServerListResults,
			boolean cacheServerListTrending, boolean cacheServerListPortfolios) {
		this.cacheTTL = cacheTTL;
		this.cacheServerListAnimation = cacheServerListAnimation;
		this.cacheServerListResults = cacheServerListResults;
		this.cacheServerListTrending = cacheServerListTrending;
		this.cacheServerListPortfolios = cacheServerListPortfolios;
	}

}
