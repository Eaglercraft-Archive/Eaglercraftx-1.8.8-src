package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.config;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;
import java.util.logging.Level;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.EaglerXBungee;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.skins.BinaryHttpClient;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.skins.BinaryHttpClient.Response;
import net.md_5.bungee.protocol.Property;

/**
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
class VanillaDefaultSkinProfileLoader implements Consumer<Response> {

	private class ProfileSkinConsumerImpl implements Consumer<Response> {

		private final String uuid;

		private ProfileSkinConsumerImpl(String uuid) {
			this.uuid = uuid;
		}

		@Override
		public void accept(Response response) {
			if(response == null) {
				EaglerXBungee.logger().severe("Request error (null)");
				doNotify();
			}else if(response.exception != null) {
				EaglerXBungee.logger().log(Level.SEVERE, "Exception loading vanilla default profile!", response.exception);
				doNotify();
			}else if(response.code != 200) {
				EaglerXBungee.logger().severe("Recieved code " + response.code + " while looking up profile of " + uuid);
				doNotify();
			}else if (response.data == null) {
				EaglerXBungee.logger().severe("Recieved null payload while looking up profile of " + uuid);
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
													signature != null ? signature.getAsString() : null);
											config.eaglerPlayersVanillaSkinCached = new Property[] { newProp, EaglerBungeeConfig.isEaglerProperty };
										}
										EaglerXBungee.logger().info("Loaded vanilla profile: " + config.getEaglerPlayersVanillaSkin());
										doNotify();
										return;
									}
								}
							}
						}
					}
					EaglerXBungee.logger().warning("No skin was found for: " + config.getEaglerPlayersVanillaSkin());
				}catch(Throwable t) {
					EaglerXBungee.logger().log(Level.SEVERE, "Exception processing name to UUID lookup response!", t);
				}
				doNotify();
			}
		}

	}

	private final EaglerBungeeConfig config;
	private volatile boolean isLocked = true;
	private final Object lock = new Object();

	private VanillaDefaultSkinProfileLoader(EaglerBungeeConfig config) {
		this.config = config;
	}

	@Override
	public void accept(Response response) {
		if(response == null) {
			EaglerXBungee.logger().severe("Request error (null)");
			doNotify();
		}else if(response.exception != null) {
			EaglerXBungee.logger().log(Level.SEVERE, "Exception loading vanilla default profile!", response.exception);
			doNotify();
		}else if(response.code != 200) {
			EaglerXBungee.logger().severe("Recieved code " + response.code + " while looking up UUID of " + config.getEaglerPlayersVanillaSkin());
			doNotify();
		}else if (response.data == null) {
			EaglerXBungee.logger().severe("Recieved null payload while looking up UUID of " + config.getEaglerPlayersVanillaSkin());
			doNotify();
		}else {
			try {
				JsonObject json = JsonParser.parseString(new String(response.data, StandardCharsets.UTF_8)).getAsJsonObject();
				String uuid = json.get("id").getAsString();
				URI requestURI = URI.create("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
				BinaryHttpClient.asyncRequest("GET", requestURI, new ProfileSkinConsumerImpl(uuid));
			}catch(Throwable t) {
				EaglerXBungee.logger().log(Level.SEVERE, "Exception processing name to UUID lookup response!", t);
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

	static void lookupVanillaSkinUser(EaglerBungeeConfig config) {
		String user = config.getEaglerPlayersVanillaSkin();
		EaglerXBungee.logger().info("Loading vanilla profile: " + user);
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
					EaglerXBungee.logger().warning("Profile load timed out");
				}
			}
		}
	}

}
