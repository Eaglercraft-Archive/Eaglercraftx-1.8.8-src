/*
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

package net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.server.web;

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
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.EaglerXVelocityVersion;

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
		this.server = EaglerXVelocityVersion.ID + "/" + EaglerXVelocityVersion.VERSION;
	}
	
	public DefaultFullHttpResponse createHTTPResponse() {
		return createHTTPResponse(HttpResponseStatus.OK);
	}
	
	public DefaultFullHttpResponse createHTTPResponse(HttpResponseStatus code) {
		DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, code, Unpooled.wrappedBuffer(fileData.retain()));
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