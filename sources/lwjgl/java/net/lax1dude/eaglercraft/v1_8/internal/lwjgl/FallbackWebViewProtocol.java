/*
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

package net.lax1dude.eaglercraft.v1_8.internal.lwjgl;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import net.lax1dude.eaglercraft.v1_8.internal.WebViewOptions;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.webview.PermissionsCache;

class FallbackWebViewProtocol {

	static final Logger logger = FallbackWebViewServer.logger;

	private static final Map<Integer,Class<? extends FallbackWebViewPacket>> packetIDToClass = new HashMap<>();
	private static final Map<Class<? extends FallbackWebViewPacket>,Integer> packetClassToID = new HashMap<>();

	private static final int CLIENT_TO_SERVER = 0;
	private static final int SERVER_TO_CLIENT = 1;

	static {
		register(0x00, CLIENT_TO_SERVER, CPacketClientHandshake.class);
		register(0x01, SERVER_TO_CLIENT, SPacketServerHandshake.class);
		register(0x02, SERVER_TO_CLIENT, SPacketServerError.class);
		register(0x03, CLIENT_TO_SERVER, CPacketWebViewChannelOpen.class);
		register(0x04, CLIENT_TO_SERVER, CPacketWebViewChannelClose.class);
		register(0x05, CLIENT_TO_SERVER, CPacketWebViewMessage.class);
		register(0x06, SERVER_TO_CLIENT, SPacketWebViewMessage.class);
		register(0x07, CLIENT_TO_SERVER, CPacketWebViewJSPermission.class);
	}

	private static void register(int id, int dir, Class<? extends FallbackWebViewPacket> packet) {
		if(dir == CLIENT_TO_SERVER) {
			packetIDToClass.put(id, packet);
		}else if(dir == SERVER_TO_CLIENT) {
			packetClassToID.put(packet, id);
		}else {
			throw new IllegalArgumentException();
		}
	}

	static String writePacket(FallbackWebViewPacket packet) {
		Class<? extends FallbackWebViewPacket> cls = packet.getClass();
		Integer id = packetClassToID.get(cls);
		if(id == null) {
			throw new RuntimeException("Tried to send unknown packet to client: " + cls.getSimpleName());
		}
		JSONObject json = new JSONObject();
		json.put("$", id);
		packet.writePacket(json);
		return json.toString();
	}

	static FallbackWebViewPacket readPacket(String data) {
		try {
			JSONObject json = new JSONObject(data);
			int id = json.getInt("$");
			Class<? extends FallbackWebViewPacket> cls = packetIDToClass.get(id);
			if(cls == null) {
				logger.error("Unknown packet ID {} recieved from webview controller", id);
				return null;
			}
			FallbackWebViewPacket ret;
			try {
				ret = cls.newInstance();
			}catch(Throwable t) {
				throw new RuntimeException("Failed to call packet constructor for \"" + cls.getSimpleName() + "\"! (is it defined?)");
			}
			ret.readPacket(json);
			return ret;
		}catch(Throwable ex) {
			logger.error("Failed to parse message from webview controller: \"{}\"", data);
			logger.error(ex);
			return null;
		}
	}

	static interface FallbackWebViewPacket {
		
		void readPacket(JSONObject json);
		
		void writePacket(JSONObject json);
		
	}

	static class CPacketClientHandshake implements FallbackWebViewPacket {

		public boolean cspSupport;

		public CPacketClientHandshake() {
		}

		@Override
		public void readPacket(JSONObject json) {
			cspSupport = json.getBoolean("cspSupport");
		}

		@Override
		public void writePacket(JSONObject json) {
			throw new UnsupportedOperationException("Client only!");
		}

	}

	static class CPacketWebViewChannelOpen implements FallbackWebViewPacket {

		public String messageChannel;

		public CPacketWebViewChannelOpen() {
		}

		public CPacketWebViewChannelOpen(String messageChannel) {
			this.messageChannel = messageChannel;
		}

		@Override
		public void readPacket(JSONObject json) {
			messageChannel = json.getString("channel");
			if(messageChannel.length() > 255) {
				throw new JSONException("Channel name too long!");
			}
		}

		@Override
		public void writePacket(JSONObject json) {
			throw new UnsupportedOperationException("Client only!");
		}

	}

	static class CPacketWebViewChannelClose implements FallbackWebViewPacket {

		public CPacketWebViewChannelClose() {
		}

		@Override
		public void readPacket(JSONObject json) {
			
		}

		@Override
		public void writePacket(JSONObject json) {
			throw new UnsupportedOperationException("Client only!");
		}

	}

	// for string messages, binary are sent as a binary frame
	static class CPacketWebViewMessage implements FallbackWebViewPacket {

		public String messageContent;

		public CPacketWebViewMessage() {
		}

		public CPacketWebViewMessage(String messageContent) {
			this.messageContent = messageContent;
		}

		@Override
		public void readPacket(JSONObject json) {
			messageContent = json.getString("msg");
		}

		@Override
		public void writePacket(JSONObject json) {
			throw new UnsupportedOperationException("Client only!");
		}

	}

	static class SPacketServerHandshake implements FallbackWebViewPacket {

		public WebViewOptions options;
		public EnumWebViewJSPermission hasApprovedJS;

		public SPacketServerHandshake() {
		}

		public SPacketServerHandshake(WebViewOptions options, EnumWebViewJSPermission hasApprovedJS) {
			this.options = options;
			this.hasApprovedJS = hasApprovedJS;
		}

		@Override
		public void readPacket(JSONObject json) {
			throw new UnsupportedOperationException("Server only!");
		}

		@Override
		public void writePacket(JSONObject json) {
			json.put("contentMode", options.contentMode.toString());
			json.put("fallbackTitle", options.fallbackTitle);
			json.put("scriptEnabled", options.scriptEnabled);
			json.put("strictCSPEnable", options.strictCSPEnable);
			json.put("serverMessageAPIEnabled", options.serverMessageAPIEnabled);
			json.put("url", options.url);
			json.put("blob", options.blob != null ? new String(options.blob, StandardCharsets.UTF_8) : null);
			json.put("hasApprovedJS", hasApprovedJS.toString());
		}

	}

	static class SPacketServerError implements FallbackWebViewPacket {

		public String errorMessage;

		public SPacketServerError() {
		}

		public SPacketServerError(String errorMessage) {
			this.errorMessage = errorMessage;
		}

		@Override
		public void readPacket(JSONObject json) {
			throw new UnsupportedOperationException("Server only!");
		}

		@Override
		public void writePacket(JSONObject json) {
			json.put("msg", errorMessage);
		}

	}

	static class SPacketWebViewMessage implements FallbackWebViewPacket {

		public String message;

		public SPacketWebViewMessage() {
		}

		public SPacketWebViewMessage(String message) {
			this.message = message;
		}

		@Override
		public void readPacket(JSONObject json) {
			throw new UnsupportedOperationException("Server only!");
		}

		@Override
		public void writePacket(JSONObject json) {
			json.put("msg", message);
		}

	}

	static enum EnumWebViewJSPermission {
		NOT_SET, ALLOW, BLOCK;

		static EnumWebViewJSPermission fromPermission(PermissionsCache.Permission perm) {
			if(perm != null) {
				return perm.choice ? ALLOW : BLOCK;
			}else {
				return NOT_SET;
			}
		}
	}

	static class CPacketWebViewJSPermission implements FallbackWebViewPacket {

		public EnumWebViewJSPermission permission;

		public CPacketWebViewJSPermission() {
		}

		@Override
		public void readPacket(JSONObject json) {
			permission = EnumWebViewJSPermission.valueOf(json.getString("perm"));
		}

		@Override
		public void writePacket(JSONObject json) {
			throw new UnsupportedOperationException("Client only!");
		}

	}

}