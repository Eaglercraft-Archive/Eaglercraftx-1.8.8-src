package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.web;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.EaglerXBungee;

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
public class HttpMemoryCache {

	public File fileObject;
	public String filePath;
	public ByteBuf fileData;
	public HttpContentType contentType;
	public long lastCacheHit;
	public long lastDiskReload;
	public long lastDiskModified;
	private final String server;
	
	private static final SimpleDateFormat gmt;
	
	static {
		gmt = new SimpleDateFormat();
		gmt.setTimeZone(new SimpleTimeZone(0, "GMT"));
		gmt.applyPattern("dd MMM yyyy HH:mm:ss z");
	}
	
	public HttpMemoryCache(File fileObject, String filePath, ByteBuf fileData, HttpContentType contentType,
			long lastCacheHit, long lastDiskReload, long lastDiskModified) {
		this.fileObject = fileObject;
		this.filePath = filePath;
		this.fileData = fileData;
		this.contentType = contentType;
		this.lastCacheHit = lastCacheHit;
		this.lastDiskReload = lastDiskReload;
		this.lastDiskModified = lastDiskModified;
		this.server = "EaglerXBungee/" + EaglerXBungee.getEagler().getDescription().getVersion();
	}
	
	public DefaultFullHttpResponse createHTTPResponse() {
		return createHTTPResponse(HttpResponseStatus.OK);
	}
	
	public DefaultFullHttpResponse createHTTPResponse(HttpResponseStatus code) {
		DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, code, Unpooled.copiedBuffer(fileData));
		HttpHeaders responseHeaders = response.headers();
		Date d = new Date();
		responseHeaders.add(HttpHeaderNames.CONTENT_TYPE, contentType.httpHeader);
		responseHeaders.add(HttpHeaderNames.CONTENT_LENGTH, fileData.readableBytes());
		responseHeaders.add(HttpHeaderNames.CACHE_CONTROL, contentType.cacheControlHeader);
		responseHeaders.add(HttpHeaderNames.DATE, gmt.format(d));
		long l = contentType.fileBrowserCacheTTL;
		if(l > 0l && l != Long.MAX_VALUE) {
			d.setTime(d.getTime() + l);
			responseHeaders.add(HttpHeaderNames.EXPIRES, gmt.format(d));
		}
		d.setTime(lastDiskModified);
		responseHeaders.add(HttpHeaderNames.LAST_MODIFIED, gmt.format(d));
		responseHeaders.add(HttpHeaderNames.SERVER, server);
		return response;
	}

}
