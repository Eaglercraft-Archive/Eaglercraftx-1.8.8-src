package net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.api.event;

import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.server.query.RevokeSessionQueryHandler;

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
public class EaglercraftRevokeSessionQueryEvent {

	private final InetAddress remoteAddress;
	private final String origin;
	private final byte[] cookieData;
	private final RevokeSessionQueryHandler queryHandler;
	private EnumSessionRevokeStatus revokeStatus;
	private boolean shouldDeleteCookie;

	public static enum EnumSessionRevokeStatus {
		SUCCESS("ok", -1), FAILED_NOT_SUPPORTED("error", 1), FAILED_NOT_ALLOWED("error", 2),
		FAILED_NOT_FOUND("error", 3), FAILED_SERVER_ERROR("error", 4);
		
		public final String status;
		public final int code;
		
		private EnumSessionRevokeStatus(String status, int code) {
			this.status = status;
			this.code = code;
		}
	}

	public EaglercraftRevokeSessionQueryEvent(InetAddress remoteAddress, String origin, byte[] cookieData,
			RevokeSessionQueryHandler queryHandler) {
		this.remoteAddress = remoteAddress;
		this.origin = origin;
		this.cookieData = cookieData;
		this.queryHandler = queryHandler;
		this.revokeStatus = EnumSessionRevokeStatus.FAILED_NOT_SUPPORTED;
		this.shouldDeleteCookie = false;
	}

	public InetAddress getRemoteAddress() {
		return remoteAddress;
	}

	public String getOrigin() {
		return origin;
	}

	public byte[] getCookieData() {
		return cookieData;
	}

	public String getCookieDataString() {
		return new String(cookieData, StandardCharsets.UTF_8);
	}

	public RevokeSessionQueryHandler getQuery() {
		return queryHandler;
	}

	public void setResultStatus(EnumSessionRevokeStatus revokeStatus) {
		this.revokeStatus = revokeStatus;
	}

	public EnumSessionRevokeStatus getResultStatus() {
		return revokeStatus;
	}

	public boolean getShouldDeleteCookie() {
		return shouldDeleteCookie;
	}

	public void setShouldDeleteCookie(boolean b) {
		this.shouldDeleteCookie = b;
	}
}
