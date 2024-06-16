package net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.server.query;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;

import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.EaglerXVelocity;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.EaglerXVelocityVersion;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.config.EaglerVelocityConfig;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.server.HttpServerQueryHandler;

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
public class QueryManager {

	private static final Map<String, Class<? extends HttpServerQueryHandler>> queryTypes = new HashMap();

	static {
		queryTypes.put("motd", MOTDQueryHandler.class);
		queryTypes.put("motd.cache", MOTDQueryHandler.class);
		queryTypes.put("version", VersionQueryHandler.class);
	}

	public static HttpServerQueryHandler createQueryHandler(String type) {
		Class<? extends HttpServerQueryHandler> clazz;
		synchronized(queryTypes) {
			clazz = queryTypes.get(type);
		}
		if(clazz != null) {
			HttpServerQueryHandler obj = null;
			try {
				obj = clazz.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				EaglerXVelocity.logger().error("Exception creating query handler for '" + type + "'!", e);
			}
			if(obj != null) {
				return obj;
			}
		}
		return null;
	}

	public static void registerQueryType(String name, Class<? extends HttpServerQueryHandler> clazz) {
		synchronized(queryTypes) {
			if(queryTypes.put(name, clazz) != null) {
				EaglerXVelocity.logger().warn("Query type '" + name + "' was registered twice, probably by two different plugins!");
				Thread.dumpStack();
			}
		}
	}

	public static void unregisterQueryType(String name) {
		synchronized(queryTypes) {
			queryTypes.remove(name);
		}
	}

	private static JsonObject createBaseResponse() {
		EaglerXVelocity plugin = EaglerXVelocity.getEagler();
		EaglerVelocityConfig conf = plugin.getConfig();
		JsonObject json = new JsonObject();
		json.addProperty("name", conf.getServerName());
		json.addProperty("brand", "lax1dude");
		json.addProperty("vers", EaglerXVelocityVersion.ID + "/" + EaglerXVelocityVersion.VERSION);
		json.addProperty("cracked", conf.isCracked());
		json.addProperty("time", System.currentTimeMillis());
		json.addProperty("uuid", conf.getServerUUID().toString());
		return json;
	}

	public static JsonObject createStringResponse(String type, String str) {
		JsonObject ret = createBaseResponse();
		ret.addProperty("type", type);
		ret.addProperty("data", str);
		return ret;
	}

	public static JsonObject createJsonObjectResponse(String type, JsonObject json) {
		JsonObject ret = createBaseResponse();
		ret.addProperty("type", type);
		ret.add("data", json);
		return ret;
	}

}
