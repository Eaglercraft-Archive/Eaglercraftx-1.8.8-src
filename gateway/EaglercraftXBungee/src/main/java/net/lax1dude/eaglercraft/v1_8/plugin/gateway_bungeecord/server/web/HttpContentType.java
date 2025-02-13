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

package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.web;

import java.util.HashSet;
import java.util.Set;

public class HttpContentType {
	
	public final Set<String> extensions;
	public final String mimeType;
	public final String charset;
	public final String httpHeader;
	public final String cacheControlHeader;
	public final long fileBrowserCacheTTL;
	
	public static final HttpContentType defaultType = new HttpContentType(new HashSet<>(), "application/octet-stream", null, 14400000l);
	
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