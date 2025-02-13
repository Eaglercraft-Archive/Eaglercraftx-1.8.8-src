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

package net.lax1dude.eaglercraft.v1_8.plugin.bukkit_rpc_helper.impl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;

import org.bukkit.entity.Player;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder.ListMultimapBuilder;

import net.lax1dude.eaglercraft.v1_8.plugin.backend_rpc_protocol.EaglerBackendRPCProtocol;
import net.lax1dude.eaglercraft.v1_8.plugin.backend_rpc_protocol.pkt.EaglerBackendRPCHandler;
import net.lax1dude.eaglercraft.v1_8.plugin.backend_rpc_protocol.pkt.EaglerBackendRPCPacket;
import net.lax1dude.eaglercraft.v1_8.plugin.backend_rpc_protocol.pkt.client.*;
import net.lax1dude.eaglercraft.v1_8.plugin.backend_rpc_protocol.pkt.server.*;
import net.lax1dude.eaglercraft.v1_8.plugin.backend_rpc_protocol.util.PacketImageData;
import net.lax1dude.eaglercraft.v1_8.plugin.bukkit_rpc_helper.EaglerXBukkitAPIPlugin;
import net.lax1dude.eaglercraft.v1_8.plugin.bukkit_rpc_helper.api.EaglerRPCException;
import net.lax1dude.eaglercraft.v1_8.plugin.bukkit_rpc_helper.api.EnumSubscribeEvents;
import net.lax1dude.eaglercraft.v1_8.plugin.bukkit_rpc_helper.api.IEaglerRPCCloseHandler;
import net.lax1dude.eaglercraft.v1_8.plugin.bukkit_rpc_helper.api.IEaglerRPCEventHandler;
import net.lax1dude.eaglercraft.v1_8.plugin.bukkit_rpc_helper.api.IEaglerRPCFuture;
import net.lax1dude.eaglercraft.v1_8.plugin.bukkit_rpc_helper.api.IEaglerXBukkitAPI;
import net.lax1dude.eaglercraft.v1_8.plugin.bukkit_rpc_helper.api.event.*;
import net.lax1dude.eaglercraft.v1_8.plugin.bukkit_rpc_helper.api.response.*;

public class EaglerXBukkitImpl implements IEaglerXBukkitAPI {

	public static final int DEFAULT_TIMEOUT = 10000;

	public static EaglerRPCFutureImpl<IEaglerXBukkitAPI> getAPI(Player player) {
		if(player == null) {
			throw new NullPointerException("Player cannot be null!");
		}
		PlayerDataObj data = PlayerDataObj.getForPlayer(player);
		if(data == null) {
			throw new IllegalStateException("Player object is not ready yet for EaglerXBukkitAPI.createAPI()! (Try hooking PlayerJoinEvent instead of PlayerLoginEvent you hooker)");
		}
		boolean sendHello;
		synchronized(data) {
			if(data.openFuture != null) {
				return data.openFuture;
			}
			if(data.currentAPI != null) {
				EaglerRPCFutureImpl<IEaglerXBukkitAPI> completeImmedately = new EaglerRPCFutureImpl();
				completeImmedately.fireCompleteInternal(data.currentAPI);
				return completeImmedately;
			}
			data.openFuture = new EaglerRPCFutureImpl();
			sendHello = data.hasRecievedReady;
		}
		if(sendHello) {
			sendHelloPacket(data.pluginChName, player);
		}
		return data.openFuture;
	}

	protected static void sendHelloPacket(String channel, Player player) {
		player.sendPluginMessage(EaglerXBukkitAPIPlugin.getEagler(), channel, HelloPacketFactory.BASE_HELLO_PACKET);
	}

	protected static EaglerXBukkitImpl createFromHandshakeInternal(PlayerDataObj playerDataObj, SPacketRPCEnabledSuccess pkt) {
		return new EaglerXBukkitImpl(playerDataObj, EaglerBackendRPCProtocol.getByID(pkt.selectedRPCProtocol), pkt.playerClientProtocol);
	}

	protected final PlayerDataObj playerDataObj;
	protected final Player playerObj;
	protected final EaglerBackendRPCProtocol protocol;
	protected final int gameProtocol;
	protected boolean open;
	protected final Map<String,Object> metadata = new ConcurrentHashMap<>(4);
	protected final EaglerXBukkitAPIHandler packetHandler;
	protected int subscribedEvents = 0;
	protected final ListMultimap<EnumSubscribeEvents, IEaglerRPCEventHandler<? extends IEaglerRPCEvent>> eventHandlers = ListMultimapBuilder
			.hashKeys().arrayListValues().build();
	protected final List<IEaglerRPCCloseHandler> closeHandlers = new ArrayList<>(4);
	protected final Map<Integer,EaglerRPCFutureImpl<? extends IEaglerRPCResponse>> waitingRequests = new ConcurrentHashMap<>();
	protected int baseTimeout = DEFAULT_TIMEOUT;
	protected final AtomicInteger requestIDGenerator = new AtomicInteger();
	private final ReentrantLock inputStreamLock = new ReentrantLock();
	private final ReentrantLock outputStreamLock = new ReentrantLock();
	private final ReusableByteArrayInputStream reusableInputStream = new ReusableByteArrayInputStream();
	private final ReusableByteArrayOutputStream reusableOutputStream = new ReusableByteArrayOutputStream();
	private final DataInputStream dataInputStream = new DataInputStream(reusableInputStream);
	private final DataOutputStream dataOutputStream = new DataOutputStream(reusableOutputStream);

	protected EaglerXBukkitImpl(PlayerDataObj playerDataObj, EaglerBackendRPCProtocol protocol, int gameProtocol) {
		this.playerDataObj = playerDataObj;
		this.playerObj = playerDataObj.player;
		this.protocol = protocol;
		this.gameProtocol = gameProtocol;
		this.open = true;
		this.packetHandler = new EaglerXBukkitAPIHandler();
	}

	@Override
	public EaglerBackendRPCProtocol getRPCProtocolVersion() {
		return protocol;
	}

	@Override
	public int getEaglerProtocolVersion() {
		return gameProtocol;
	}

	@Override
	public boolean isOpen() {
		return open;
	}

	@Override
	public void closeAPI() {
		if(open) {
			try {
				sendRPCPacket(new CPacketRPCDisabled());
			}finally {
				fireAPIClosedEventInternal();
			}
		}
	}

	protected void fireAPIClosedEventInternal() {
		if(!open) return;
		open = false;
		synchronized(closeHandlers) {
			for(int i = 0, l = closeHandlers.size(); i < l; ++i) {
				IEaglerRPCCloseHandler hd = closeHandlers.get(i);
				try {
					hd.handleEvent(this);
				}catch(Throwable t) {
					EaglerXBukkitAPIPlugin.logger().log(Level.SEVERE,
							"[" + playerObj.getName() + "] caught exception while firing close handler " + hd, t);
				}
			}
		}
	}

	protected void fireAPIPacketRecievedInternal(EaglerBackendRPCPacket ret) {
		ret.handlePacket(packetHandler);
	}

	protected <T extends IEaglerRPCEvent> void fireEventHandlers(T eventObj) {
		EnumSubscribeEvents type = eventObj.getType();
		List<IEaglerRPCEventHandler<? extends IEaglerRPCEvent>> lst;
		synchronized(eventHandlers) {
			lst = eventHandlers.get(type);
		}
		for(int i = 0, l = lst.size(); i < l; ++i) {
			IEaglerRPCEventHandler<T> handler = (IEaglerRPCEventHandler<T>) lst.get(i);
			try {
				handler.handleEvent(this, type, eventObj);
			}catch(Throwable t) {
				EaglerXBukkitAPIPlugin.logger().log(Level.SEVERE,
						"[" + playerObj.getName() + "] caught exception while processing event type "
								+ type + " using handler " + handler, t);
			}
		}
	}

	protected class EaglerXBukkitAPIHandler implements EaglerBackendRPCHandler {

		public void handleServer(SPacketRPCResponseTypeNull packet) {
			EaglerRPCFutureImpl<? extends IEaglerRPCResponse> future = waitingRequests.remove(packet.requestID);
			if(future != null) {
				future.fireCompleteInternal(null);
			}
		}

		public void handleServer(SPacketRPCResponseTypeBytes packet) {
			EaglerRPCFutureImpl<ResponseBytes> future = (EaglerRPCFutureImpl<ResponseBytes>)waitingRequests.remove(packet.requestID);
			if(future != null) {
				future.fireCompleteInternal(new ResponseBytes(EaglerXBukkitImpl.this, packet.requestID, packet.response));
			}
		}

		public void handleServer(SPacketRPCResponseTypeString packet) {
			EaglerRPCFutureImpl<ResponseString> future = (EaglerRPCFutureImpl<ResponseString>)waitingRequests.remove(packet.requestID);
			if(future != null) {
				future.fireCompleteInternal(new ResponseString(EaglerXBukkitImpl.this, packet.requestID, packet.response));
			}
		}

		public void handleServer(SPacketRPCResponseTypeUUID packet) {
			EaglerRPCFutureImpl<ResponseUUID> future = (EaglerRPCFutureImpl<ResponseUUID>)waitingRequests.remove(packet.requestID);
			if(future != null) {
				future.fireCompleteInternal(new ResponseUUID(EaglerXBukkitImpl.this, packet.requestID, packet.uuid));
			}
		}

		public void handleServer(SPacketRPCResponseTypeCookie packet) {
			EaglerRPCFutureImpl<ResponseCookie> future = (EaglerRPCFutureImpl<ResponseCookie>)waitingRequests.remove(packet.requestID);
			if(future != null) {
				future.fireCompleteInternal(new ResponseCookie(EaglerXBukkitImpl.this, packet.requestID, packet.cookiesEnabled, packet.cookieData));
			}
		}

		public void handleServer(SPacketRPCResponseTypeVoiceStatus packet) {
			EaglerRPCFutureImpl<ResponseVoiceStatus> future = (EaglerRPCFutureImpl<ResponseVoiceStatus>)waitingRequests.remove(packet.requestID);
			if(future != null) {
				future.fireCompleteInternal(new ResponseVoiceStatus(EaglerXBukkitImpl.this, packet.requestID, translateVoiceState2(packet.voiceState)));
			}
		}

		public void handleServer(SPacketRPCResponseTypeWebViewStatus packet) {
			EaglerRPCFutureImpl<ResponseWebViewStatus> future = (EaglerRPCFutureImpl<ResponseWebViewStatus>)waitingRequests.remove(packet.requestID);
			if(future != null) {
				future.fireCompleteInternal(new ResponseWebViewStatus(EaglerXBukkitImpl.this, packet.requestID, translateWebViewState(packet.webviewState), packet.channelName));
			}
		}

		public void handleServer(SPacketRPCResponseTypeError packet) {
			EaglerRPCFutureImpl<ResponseWebViewStatus> future = (EaglerRPCFutureImpl<ResponseWebViewStatus>)waitingRequests.remove(packet.requestID);
			if(future != null) {
				future.fireExceptionInternal(new EaglerRPCResponseException(packet.errorMessage));
			}
		}

		public void handleServer(SPacketRPCEventWebViewOpenClose packet) {
			if((subscribedEvents & EnumSubscribeEvents.EVENT_WEBVIEW_OPEN_CLOSE.bit) != 0) {
				fireEventHandlers(new EventWebViewOpenClose(packet.channelName, packet.channelOpen));
			}
		}

		public void handleServer(SPacketRPCEventWebViewMessage packet) {
			if((subscribedEvents & EnumSubscribeEvents.EVENT_WEBVIEW_MESSAGE.bit) != 0) {
				EventWebViewMessage.MessageType mt;
				switch(packet.messageType) {
				case SPacketRPCEventWebViewMessage.MESSAGE_TYPE_STRING:
					mt = EventWebViewMessage.MessageType.STRING;
					break;
				case SPacketRPCEventWebViewMessage.MESSAGE_TYPE_BINARY:
					mt = EventWebViewMessage.MessageType.BINARY;
					break;
				default:
					return;
				}
				fireEventHandlers(new EventWebViewMessage(packet.channelName, mt, packet.messageContent));
			}
		}

		public void handleServer(SPacketRPCEventToggledVoice packet) {
			if((subscribedEvents & EnumSubscribeEvents.EVENT_TOGGLE_VOICE.bit) != 0) {
				EventToggledVoice.VoiceState vsOld = translateVoiceState(packet.oldVoiceState);
				EventToggledVoice.VoiceState vsNew = translateVoiceState(packet.newVoiceState);
				if(vsOld == null || vsNew == null) {
					return;
				}
				fireEventHandlers(new EventToggledVoice(vsOld, vsNew));
			}
		}

	}

	private static EventToggledVoice.VoiceState translateVoiceState(int vs) {
		switch(vs) {
		case SPacketRPCResponseTypeVoiceStatus.VOICE_STATE_SERVER_DISABLE:
			return EventToggledVoice.VoiceState.SERVER_DISABLE;
		case SPacketRPCResponseTypeVoiceStatus.VOICE_STATE_DISABLED:
			return EventToggledVoice.VoiceState.DISABLED;
		case SPacketRPCResponseTypeVoiceStatus.VOICE_STATE_ENABLED:
			return EventToggledVoice.VoiceState.ENABLED;
		default:
			return null;
		}
	}

	private static ResponseVoiceStatus.VoiceState translateVoiceState2(int vs) {
		switch(vs) {
		case SPacketRPCEventToggledVoice.VOICE_STATE_SERVER_DISABLE:
		default:
			return ResponseVoiceStatus.VoiceState.SERVER_DISABLE;
		case SPacketRPCEventToggledVoice.VOICE_STATE_DISABLED:
			return ResponseVoiceStatus.VoiceState.DISABLED;
		case SPacketRPCEventToggledVoice.VOICE_STATE_ENABLED:
			return ResponseVoiceStatus.VoiceState.ENABLED;
		}
	}

	private static ResponseWebViewStatus.WebViewState translateWebViewState(int vs) {
		switch(vs) {
		case SPacketRPCResponseTypeWebViewStatus.WEBVIEW_STATE_NOT_SUPPORTED:
		default:
			return ResponseWebViewStatus.WebViewState.NOT_SUPPORTED;
		case SPacketRPCResponseTypeWebViewStatus.WEBVIEW_STATE_SERVER_DISABLE:
			return ResponseWebViewStatus.WebViewState.SERVER_DISABLE;
		case SPacketRPCResponseTypeWebViewStatus.WEBVIEW_STATE_CHANNEL_CLOSED:
			return ResponseWebViewStatus.WebViewState.CHANNEL_CLOSED;
		case SPacketRPCResponseTypeWebViewStatus.WEBVIEW_STATE_CHANNEL_OPEN:
			return ResponseWebViewStatus.WebViewState.CHANNEL_OPEN;
		}
	}

	@Override
	public Player getPlayer() {
		return playerObj;
	}

	@Override
	public void sendRPCPacket(EaglerBackendRPCPacket packet) {
		if(!open) {
			EaglerXBukkitAPIPlugin.logger().warning("[" + playerObj.getName() + "] Sent " + packet.getClass().getSimpleName() + " on a dead connection!");
			return;
		}
		if(packet == null) {
			throw new NullPointerException("Packet cannot be null!");
		}
		byte[] ret;
		int len = packet.length() + 1;
		if(outputStreamLock.tryLock()) {
			try {
				reusableOutputStream.feedBuffer(new byte[len > 0 ? len : 64]);
				try {
					protocol.writePacket(dataOutputStream, EaglerBackendRPCProtocol.CLIENT_TO_SERVER, packet);
				}catch(IOException ex) {
					throw new EaglerRPCException("Failed to serialize packet: " + packet.getClass().getSimpleName(), ex);
				}
				ret = reusableOutputStream.returnBuffer();
			}finally {
				outputStreamLock.unlock();
			}
		}else {
			ReusableByteArrayOutputStream bao = new ReusableByteArrayOutputStream();
			bao.feedBuffer(new byte[len > 0 ? len : 64]);
			try {
				protocol.writePacket(new DataOutputStream(bao), EaglerBackendRPCProtocol.CLIENT_TO_SERVER, packet);
			}catch(IOException ex) {
				throw new EaglerRPCException("Failed to serialize packet: " + packet.getClass().getSimpleName(), ex);
			}
			ret = bao.returnBuffer();
		}
		if(len > 0 && len != ret.length) {
			EaglerXBukkitAPIPlugin.logger()
					.warning("[" + playerObj.getName() + "] Packet type " + packet.getClass().getSimpleName()
							+ " was the wrong length after serialization: " + ret.length + " != " + len);
		}
		playerObj.sendPluginMessage(EaglerXBukkitAPIPlugin.getEagler(), playerDataObj.pluginChName, ret);
	}

	protected EaglerBackendRPCPacket decodePacket(byte[] data) throws IOException {
		EaglerBackendRPCPacket ret;
		if(inputStreamLock.tryLock()) {
			try {
				reusableInputStream.feedBuffer(data);
				ret = protocol.readPacket(dataInputStream, EaglerBackendRPCProtocol.SERVER_TO_CLIENT);
			}finally {
				inputStreamLock.unlock();
			}
		}else {
			ReusableByteArrayInputStream bai = new ReusableByteArrayInputStream();
			bai.feedBuffer(data);
			ret = protocol.readPacket(new DataInputStream(bai), EaglerBackendRPCProtocol.SERVER_TO_CLIENT);
		}
		return ret;
	}

	@Override
	public void subscribeEvents(int events) {
		int newEvents = subscribedEvents | events;
		if(newEvents != subscribedEvents) {
			sendRPCPacket(new CPacketRPCSubscribeEvents(newEvents));
			subscribedEvents = events;
		}
	}

	@Override
	public void unsubscribeEvents(int events) {
		int newEvents = subscribedEvents & ~events;
		if(newEvents != subscribedEvents) {
			sendRPCPacket(new CPacketRPCSubscribeEvents(newEvents));
			subscribedEvents = events;
		}
	}

	@Override
	public void unsubscribeAllEvents() {
		if(subscribedEvents != 0) {
			sendRPCPacket(new CPacketRPCSubscribeEvents(0));
			subscribedEvents = 0;
		}
	}

	@Override
	public int getSubscribedEventsBits() {
		return subscribedEvents;
	}

	@Override
	public void addEventListener(EnumSubscribeEvents eventType,
			IEaglerRPCEventHandler<? extends IEaglerRPCEvent> handler) {
		synchronized(eventHandlers) {
			eventHandlers.put(eventType, handler);
		}
	}

	@Override
	public void removeEventListener(EnumSubscribeEvents eventType,
			IEaglerRPCEventHandler<? extends IEaglerRPCEvent> handler) {
		synchronized(eventHandlers) {
			eventHandlers.remove(eventType, handler);
		}
	}

	@Override
	public void removeEventListeners(EnumSubscribeEvents eventType) {
		synchronized(eventHandlers) {
			eventHandlers.removeAll(eventType);
		}
	}

	@Override
	public void addCloseListener(IEaglerRPCCloseHandler handler) {
		synchronized(closeHandlers) {
			closeHandlers.add(handler);
		}
	}

	@Override
	public void removeCloseListener(IEaglerRPCCloseHandler handler) {
		synchronized(closeHandlers) {
			closeHandlers.remove(handler);
		}
	}

	@Override
	public void removeCloseListeners() {
		synchronized(closeHandlers) {
			closeHandlers.clear();
		}
	}

	@Override
	public boolean redirectPlayerSupported() {
		return gameProtocol >= 4;
	}

	@Override
	public void redirectPlayerToWebSocket(String webSocketURI) {
		if(gameProtocol >= 4) {
			if(webSocketURI == null) {
				throw new NullPointerException("URI cannot be null!");
			}
			sendRPCPacket(new CPacketRPCRedirectPlayer(webSocketURI));
		}else {
			EaglerXBukkitAPIPlugin.logger()
					.warning("[" + playerObj.getName() + "] some plugin tried to redirect player to \"" + webSocketURI
							+ "\" but that player's client does not support websocket redirects!");
		}
	}

	@Override
	public void setBaseRequestTimeout(int seconds) {
		baseTimeout = seconds * 1000;
	}

	protected <T extends IEaglerRPCResponse> IEaglerRPCFuture<T> requestSendHelper(int type) {
		EaglerRPCFutureImpl<T> ret = new EaglerRPCFutureImpl();
		ret.setExpiresMSFromNow(baseTimeout);
		int rqid = requestIDGenerator.incrementAndGet();
		sendRPCPacket(new CPacketRPCRequestPlayerInfo(rqid, type));
		waitingRequests.put(rqid, ret);
		return ret;
	}

	@Override
	public IEaglerRPCFuture<ResponseUUID> requestPlayerProfileUUID() {
		return requestSendHelper(CPacketRPCRequestPlayerInfo.REQUEST_PLAYER_REAL_UUID);
	}

	@Override
	public IEaglerRPCFuture<ResponseString> requestPlayerRealIP() {
		return requestSendHelper(CPacketRPCRequestPlayerInfo.REQUEST_PLAYER_REAL_IP);
	}

	@Override
	public IEaglerRPCFuture<ResponseString> requestPlayerOrigin() {
		return requestSendHelper(CPacketRPCRequestPlayerInfo.REQUEST_PLAYER_ORIGIN);
	}

	@Override
	public IEaglerRPCFuture<ResponseString> requestPlayerUserAgent() {
		return requestSendHelper(CPacketRPCRequestPlayerInfo.REQUEST_PLAYER_USER_AGENT);
	}

	@Override
	public IEaglerRPCFuture<ResponseBytes> requestPlayerSkinData() {
		return requestSendHelper(CPacketRPCRequestPlayerInfo.REQUEST_PLAYER_SKIN_DATA);
	}

	@Override
	public IEaglerRPCFuture<ResponseBytes> requestPlayerCapeData() {
		return requestSendHelper(CPacketRPCRequestPlayerInfo.REQUEST_PLAYER_CAPE_DATA);
	}

	@Override
	public IEaglerRPCFuture<ResponseCookie> requestPlayerCookieData() {
		return requestSendHelper(CPacketRPCRequestPlayerInfo.REQUEST_PLAYER_COOKIE);
	}

	@Override
	public IEaglerRPCFuture<ResponseString> requestPlayerClientBrandStr() {
		return requestSendHelper(CPacketRPCRequestPlayerInfo.REQUEST_PLAYER_CLIENT_BRAND_STR);
	}

	@Override
	public IEaglerRPCFuture<ResponseString> requestPlayerClientVersionStr() {
		return requestSendHelper(CPacketRPCRequestPlayerInfo.REQUEST_PLAYER_CLIENT_VERSION_STR);
	}

	@Override
	public IEaglerRPCFuture<ResponseString> requestPlayerClientBrandAndVersionStr() {
		return requestSendHelper(CPacketRPCRequestPlayerInfo.REQUEST_PLAYER_CLIENT_BRAND_VERSION_STR);
	}

	@Override
	public IEaglerRPCFuture<ResponseUUID> requestPlayerClientBrandUUID() {
		return requestSendHelper(CPacketRPCRequestPlayerInfo.REQUEST_PLAYER_CLIENT_BRAND_UUID);
	}

	@Override
	public IEaglerRPCFuture<ResponseVoiceStatus> requestPlayerVoiceStatus() {
		return requestSendHelper(CPacketRPCRequestPlayerInfo.REQUEST_PLAYER_CLIENT_VOICE_STATUS);
	}

	@Override
	public IEaglerRPCFuture<ResponseWebViewStatus> requestPlayerWebViewStatus() {
		return requestSendHelper(CPacketRPCRequestPlayerInfo.REQUEST_PLAYER_CLIENT_WEBVIEW_STATUS);
	}

	protected void cleanupTimedOutRequests(long now) {
		if(!waitingRequests.isEmpty()) {
			List<EaglerRPCFutureImpl<? extends IEaglerRPCResponse>> expired = null;
			Iterator<EaglerRPCFutureImpl<? extends IEaglerRPCResponse>> itr = waitingRequests.values().iterator();
			while(itr.hasNext()) {
				EaglerRPCFutureImpl<? extends IEaglerRPCResponse> itm = itr.next();
				if(itm.hasExpiredBetter(now)) {
					if(expired == null) {
						expired = new ArrayList<>(4);
					}
					expired.add(itm);
					try {
						itr.remove();
					}catch(Throwable t) {
					}
				}
			}
			if(expired != null) {
				for(int i = 0, l = expired.size(); i < l; ++i) {
					try {
						EaglerRPCFutureImpl<? extends IEaglerRPCResponse> itm = expired.get(i);
						EaglerXBukkitAPIPlugin.logger().warning("[" + playerObj.getName() + "] An RPC request timed out before it could be completed!");
						itm.fireExceptionInternal(new EaglerRPCTimeoutException("The request was not completed in time!"));
					}catch(Throwable t) {
						EaglerXBukkitAPIPlugin.logger().log(Level.SEVERE, "[" + playerObj.getName() + "] An unhandled exception was thrown while firing request timeout signal!", t);
					}
				}
			}
		}
	}

	@Override
	public void sendRawCustomPayloadPacket(String channel, byte[] data) {
		sendRPCPacket(new CPacketRPCSendRawMessage(channel, data));
	}

	@Override
	public boolean pauseMenuCustomizationSupported() {
		return gameProtocol >= 4;
	}

	@Override
	public void setPauseMenuCustomizationState(CPacketRPCSetPauseMenuCustom packet) {
		if(gameProtocol >= 4) {
			sendRPCPacket(packet);
		}else {
			EaglerXBukkitAPIPlugin.logger().warning("[" + playerObj.getName()
					+ "] some plugin tried to configure pause menu customization, but the player's client does not support that feature!");
		}
	}

	@Override
	public void sendWebViewMessageString(String channelName, String data) {
		if(gameProtocol >= 4) {
			if(channelName == null) {
				throw new NullPointerException("Channel cannot be null!");
			}
			if(data == null) {
				throw new NullPointerException("Data cannot be null!");
			}
			sendRPCPacket(new CPacketRPCSendWebViewMessage(channelName,
					CPacketRPCSendWebViewMessage.MESSAGE_TYPE_STRING, data.getBytes(StandardCharsets.UTF_8)));
		}else {
			EaglerXBukkitAPIPlugin.logger().warning("[" + playerObj.getName()
					+ "] some plugin tried to send a webview channel message, but the player's client does not support that feature!");
		}
	}

	@Override
	public void sendWebViewMessageBytes(String channelName, byte[] data) {
		if(gameProtocol >= 4) {
			if(channelName == null) {
				throw new NullPointerException("Channel cannot be null!");
			}
			if(data == null) {
				throw new NullPointerException("Data cannot be null!");
			}
			sendRPCPacket(new CPacketRPCSendWebViewMessage(channelName, CPacketRPCSendWebViewMessage.MESSAGE_TYPE_BINARY, data));
		}else {
			EaglerXBukkitAPIPlugin.logger().warning("[" + playerObj.getName()
					+ "] some plugin tried to send a webview channel message, but the player's client does not support that feature!");
		}
	}

	@Override
	public void forcePlayerSkin(byte[] skinData, boolean notifyOthers) {
		if(skinData == null) {
			throw new NullPointerException("Skin data cannot be null!");
		}
		if(skinData.length > 32720) {
			throw new IllegalArgumentException("Skin data cannot be more than 32720 bytes!");
		}
		sendRPCPacket(new CPacketRPCSetPlayerSkin(notifyOthers, skinData));
	}

	@Override
	public void forcePlayerCape(byte[] capeData, boolean notifyOthers) {
		if(capeData == null) {
			throw new NullPointerException("Cape data cannot be null!");
		}
		if(capeData.length > 32720) {
			throw new IllegalArgumentException("Cape data cannot be more than 32720 bytes!");
		}
		sendRPCPacket(new CPacketRPCSetPlayerCape(notifyOthers, capeData));
	}

	@Override
	public void setCookieData(byte[] cookieData, int expiresAfterSec, boolean revokeQuerySupported,
			boolean saveToDisk) {
		if(gameProtocol >= 4) {
			sendRPCPacket(new CPacketRPCSetPlayerCookie(revokeQuerySupported, saveToDisk, expiresAfterSec, cookieData));
		}else {
			EaglerXBukkitAPIPlugin.logger().warning("[" + playerObj.getName()
					+ "] some plugin tried to set a cookie, but the player's client does not support that feature!");
		}
	}

	@Override
	public void setFNAWSkinsEnabled(boolean enabled, boolean force) {
		sendRPCPacket(new CPacketRPCSetPlayerFNAWEn(enabled, force));
	}

	@Override
	public void setFNAWSkinsEnabled(boolean enabled) {
		sendRPCPacket(new CPacketRPCSetPlayerFNAWEn(enabled, false));
	}

	@Override
	public void resetForcedMulti(boolean resetSkin, boolean resetCape, boolean resetFNAWForce, boolean notifyOtherPlayers) {
		sendRPCPacket(new CPacketRPCResetPlayerMulti(resetSkin, resetCape, resetFNAWForce, notifyOtherPlayers));
	}

	@Override
	public void resetForcedSkin(boolean notifyOtherPlayers) {
		sendRPCPacket(new CPacketRPCResetPlayerMulti(true, false, false, notifyOtherPlayers));
	}

	@Override
	public void resetForcedCape(boolean notifyOtherPlayers) {
		sendRPCPacket(new CPacketRPCResetPlayerMulti(false, true, false, notifyOtherPlayers));
	}

	@Override
	public void resetForcedFNAW() {
		sendRPCPacket(new CPacketRPCResetPlayerMulti(false, false, true, false));
	}

	@Override
	public boolean notifSupported() {
		return gameProtocol >= 4;
	}

	@Override
	public void notifIconRegister(Map<UUID, PacketImageData> iconsToRegister) {
		if(gameProtocol >= 4) {
			sendRPCPacket(new CPacketRPCNotifIconRegister(iconsToRegister));
		}else {
			EaglerXBukkitAPIPlugin.logger().warning("[" + playerObj.getName()
					+ "] some plugin tried to register notification icons, but the player's client does not support that feature!");
		}
	}

	@Override
	public void notifIconRegister(UUID iconUUID, PacketImageData imageData) {
		if(gameProtocol >= 4) {
			Map<UUID, PacketImageData> toReg = new HashMap<>(1);
			toReg.put(iconUUID, imageData);
			sendRPCPacket(new CPacketRPCNotifIconRegister(toReg));
		}else {
			EaglerXBukkitAPIPlugin.logger().warning("[" + playerObj.getName()
					+ "] some plugin tried to register notification icons, but the player's client does not support that feature!");
		}
	}

	@Override
	public void notifIconRelease(Collection<UUID> iconsToRelease) {
		if(gameProtocol >= 4) {
			sendRPCPacket(new CPacketRPCNotifIconRelease(iconsToRelease));
		}else {
			EaglerXBukkitAPIPlugin.logger().warning("[" + playerObj.getName()
					+ "] some plugin tried to release notification icons, but the player's client does not support that feature!");
		}
	}

	@Override
	public void notifIconRelease(UUID iconUUID) {
		if(gameProtocol >= 4) {
			sendRPCPacket(new CPacketRPCNotifIconRelease(Arrays.asList(iconUUID)));
		}else {
			EaglerXBukkitAPIPlugin.logger().warning("[" + playerObj.getName()
					+ "] some plugin tried to release notification icons, but the player's client does not support that feature!");
		}
	}

	@Override
	public void notifBadgeShow(CPacketRPCNotifBadgeShow packet) {
		if(gameProtocol >= 4) {
			sendRPCPacket(packet);
		}else {
			EaglerXBukkitAPIPlugin.logger().warning("[" + playerObj.getName()
					+ "] some plugin tried to show notification badge, but the player's client does not support that feature!");
		}
	}

	@Override
	public void notifBadgeHide(UUID badgeUUID) {
		if(gameProtocol >= 4) {
			sendRPCPacket(new CPacketRPCNotifBadgeHide(badgeUUID));
		}else {
			EaglerXBukkitAPIPlugin.logger().warning("[" + playerObj.getName()
					+ "] some plugin tried to hide notification badge, but the player's client does not support that feature!");
		}
	}

	@Override
	public <T> void setMeta(String key, T value) {
		if(key == null) {
			throw new NullPointerException("Key cannot be null!");
		}
		metadata.put(key, value);
	}

	@Override
	public <T> T getMeta(String key) {
		if(key == null) {
			throw new NullPointerException("Key cannot be null!");
		}
		return (T)metadata.get(key);
	}

}