package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.query;

import com.google.gson.JsonObject;

import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.api.event.EaglercraftRevokeSessionQueryEvent;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.api.event.EaglercraftRevokeSessionQueryEvent.EnumSessionRevokeStatus;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.api.query.EaglerQueryHandler;
import net.md_5.bungee.BungeeCord;

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
public class RevokeSessionQueryHandler extends EaglerQueryHandler {

	@Override
	protected void begin(String queryType) {
		this.setKeepAlive(true);
		this.acceptBinary();
		this.setMaxAge(5000l);
		this.sendStringResponse("revoke_session_token", "ready");
	}

	@Override
	protected void processString(String str) {
		this.close();
	}

	@Override
	protected void processJson(JsonObject obj) {
		this.close();
	}

	@Override
	protected void processBytes(byte[] bytes) {
		if(bytes.length > 255) {
			JsonObject response = new JsonObject();
			response.addProperty("status", "error");
			response.addProperty("code", 3);
			response.addProperty("delete", false);
			sendJsonResponseAndClose("revoke_session_token", response);
			return;
		}
		this.setMaxAge(30000l);
		EaglercraftRevokeSessionQueryEvent evt = new EaglercraftRevokeSessionQueryEvent(this.getAddress(), this.getOrigin(), bytes, this);
		BungeeCord.getInstance().getPluginManager().callEvent(evt);
		JsonObject response = new JsonObject();
		EnumSessionRevokeStatus stat = evt.getResultStatus();
		response.addProperty("status", stat.status);
		if(stat.code != -1) {
			response.addProperty("code", stat.code);
		}
		if(stat != EnumSessionRevokeStatus.SUCCESS) {
			response.addProperty("delete", evt.getShouldDeleteCookie());
		}
		sendJsonResponseAndClose("revoke_session_token", response);
	}

	@Override
	protected void closed() {
	}

}
