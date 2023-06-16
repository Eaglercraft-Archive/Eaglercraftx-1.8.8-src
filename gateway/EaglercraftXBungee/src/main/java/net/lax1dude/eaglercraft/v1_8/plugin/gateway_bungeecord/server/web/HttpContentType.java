package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.web;

import java.util.HashSet;
import java.util.Set;

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
public class HttpContentType {
	
	public final Set<String> extensions;
	public final String mimeType;
	public final String charset;
	public final String httpHeader;
	public final String cacheControlHeader;
	public final long fileBrowserCacheTTL;
	
	public static final HttpContentType defaultType = new HttpContentType(new HashSet(), "application/octet-stream", null, 14400000l);
	
	public HttpContentType(Set<String> extensions, String mimeType, String charset, long fileBrowserCacheTTL) {
		this.extensions = extensions;
		this.mimeType = mimeType;
		this.charset = charset;
		this.fileBrowserCacheTTL = fileBrowserCacheTTL;
		if(charset == null) {
			this.httpHeader = mimeType;
		}else {
			this.httpHeader = mimeType + "; charset=" + charset;
		}
		if(fileBrowserCacheTTL > 0l) {
			this.cacheControlHeader = "max-age=" + (fileBrowserCacheTTL / 1000l);
		}else {
			this.cacheControlHeader = "no-cache";
		}
	}
	
}
