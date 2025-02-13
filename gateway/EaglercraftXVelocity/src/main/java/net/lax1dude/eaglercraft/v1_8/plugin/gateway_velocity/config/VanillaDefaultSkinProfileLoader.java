/*
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

package net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.config;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.velocitypowered.api.util.GameProfile.Property;

import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.EaglerXVelocity;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.skins.BinaryHttpClient;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.skins.BinaryHttpClient.Response;

class VanillaDefaultSkinProfileLoader implements Consumer<Response> {

	private class ProfileSkinConsumerImpl implements Consumer<Response> {

		private final String uuid;

		private ProfileSkinConsumerImpl(String uuid) {
			this.uuid = uuid;
		}

		@Override
		public void accept(Response response) {
			if(response == null) {
				EaglerXVelocity.logger().error("Request error (null)");
				doNotify();
			}else if(response.exception != null) {
				EaglerXVelocity.logger().error("Exception loading vanilla default profile!", response.exception);
				doNotify();
			}else if(response.code != 200) {
				EaglerXVelocity.logger().error("Recieved code {} while looking up profile of {}", response.code, uuid);
				doNotify();
			}else if (response.data == null) {
				EaglerXVelocity.logger().error("Recieved null payload while looking up profile of {}", uuid);
				doNotify();
			}else {
				try {
					JsonObject json = JsonParser.parseString(new String(response.data, StandardCharsets.UTF_8)).getAsJsonObject();
					JsonElement propsElement = json.get("properties");
					if(propsElement != null) {
						JsonArray properties = propsElement.getAsJsonArray();
						if(properties.size() > 0) {
							for(int i = 0, l = properties.size(); i < l; ++i) {
								JsonElement prop = properties.get(i);
								if(prop.isJsonObject()) {
									JsonObject propObj = prop.getAsJsonObject();
									if(propObj.get("name").getAsString().equals("textures")) {
										JsonElement value = propObj.get("value");
										JsonElement signature = propObj.get("signature");
										if(value != null) {
											Property newProp = new Property("textures", value.getAsString(),
													signature != null ? signature.getAsString() : "");
											config.eaglerPlayersVanillaSkinCached = new Property[] { newProp, EaglerVelocityConfig.isEaglerProperty };
										}
										EaglerXVelocity.logger().info("Loaded vanilla profile: " + config.getEaglerPlayersVanillaSkin());
										doNotify();
										return;
									}
								}
							}
						}
					}
					EaglerXVelocity.logger().warn("No skin was found for: {}", config.getEaglerPlayersVanillaSkin());
				}catch(Throwable t) {
					EaglerXVelocity.logger().error("Exception processing name to UUID lookup response!", t);
				}
				doNotify();
			}
		}

	}

	private final EaglerVelocityConfig config;
	private volatile boolean isLocked = true;
	private final Object lock = new Object();

	private VanillaDefaultSkinProfileLoader(EaglerVelocityConfig config) {
		this.config = config;
	}

	@Override
	public void accept(Response response) {
		if(response == null) {
			EaglerXVelocity.logger().error("Request error (null)");
			doNotify();
		}else if(response.exception != null) {
			EaglerXVelocity.logger().error("Exception loading vanilla default profile!", response.exception);
			doNotify();
		}else if(response.code != 200) {
			EaglerXVelocity.logger().error("Recieved code {} while looking up UUID of {}", response.code, config.getEaglerPlayersVanillaSkin());
			doNotify();
		}else if (response.data == null) {
			EaglerXVelocity.logger().error("Recieved null payload while looking up UUID of {}", config.getEaglerPlayersVanillaSkin());
			doNotify();
		}else {
			try {
				JsonObject json = JsonParser.parseString(new String(response.data, StandardCharsets.UTF_8)).getAsJsonObject();
				String uuid = json.get("id").getAsString();
				URI requestURI = URI.create("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
				BinaryHttpClient.asyncRequest("GET", requestURI, new ProfileSkinConsumerImpl(uuid));
			}catch(Throwable t) {
				EaglerXVelocity.logger().error("Exception processing name to UUID lookup response!", t);
				doNotify();
			}
		}
	}

	private void doNotify() {
		synchronized(lock) {
			if(isLocked) {
				isLocked = false;
				lock.notify();
			}
		}
	}

	static void lookupVanillaSkinUser(EaglerVelocityConfig config) {
		String user = config.getEaglerPlayersVanillaSkin();
		EaglerXVelocity.logger().info("Loading vanilla profile: " + user);
		URI requestURI = URI.create("https://api.mojang.com/users/profiles/minecraft/" + user);
		VanillaDefaultSkinProfileLoader loader = new VanillaDefaultSkinProfileLoader(config);
		synchronized(loader.lock) {
			BinaryHttpClient.asyncRequest("GET", requestURI, loader);
			if(loader.isLocked) {
				try {
					loader.lock.wait(5000l);
				} catch (InterruptedException e) {
				}
				if(loader.isLocked) {
					EaglerXVelocity.logger().warn("Profile load timed out");
				}
			}
		}
	}

}