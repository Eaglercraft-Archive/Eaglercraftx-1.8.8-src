package net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.server.query;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.EaglerXVelocity;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.api.query.EaglerQuerySimpleHandler;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.config.EaglerListenerConfig;

/**
 * Copyright (c) 2022-2024 lax1dude. All Rights Reserved.
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
public class VersionQueryHandler extends EaglerQuerySimpleHandler {

	@Override
	protected void begin(String queryType) {
		JsonObject responseObj = new JsonObject();
		JsonArray handshakeVersions = new JsonArray();
		EaglerListenerConfig cfg = this.getListener();
		if(cfg.isAllowV3()) {
			handshakeVersions.add(2);
			handshakeVersions.add(3);
		}
		if(cfg.isAllowV4()) {
			handshakeVersions.add(4);
		}
		responseObj.add("handshakeVersions", handshakeVersions);
		JsonObject protocolVersions = new JsonObject();
		protocolVersions.addProperty("min", cfg.getMinMCProtocol());
		protocolVersions.addProperty("max", cfg.getMaxMCProtocol());
		responseObj.add("protocolVersions", protocolVersions);
		JsonObject proxyInfo = new JsonObject();
		proxyInfo.addProperty("brand", EaglerXVelocity.proxy().getVersion().getName());
		proxyInfo.addProperty("vers", EaglerXVelocity.proxy().getVersion().getVersion());
		responseObj.add("proxyVersions", proxyInfo);
		sendJsonResponseAndClose("version", responseObj);
	}

}
