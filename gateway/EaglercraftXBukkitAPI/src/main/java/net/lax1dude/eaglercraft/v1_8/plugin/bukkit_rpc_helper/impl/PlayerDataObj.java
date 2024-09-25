package net.lax1dude.eaglercraft.v1_8.plugin.bukkit_rpc_helper.impl;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.bukkit.metadata.LazyMetadataValue;
import org.bukkit.metadata.MetadataValue;

import net.lax1dude.eaglercraft.v1_8.plugin.backend_rpc_protocol.EaglerBackendRPCProtocol;
import net.lax1dude.eaglercraft.v1_8.plugin.backend_rpc_protocol.pkt.EaglerBackendRPCPacket;
import net.lax1dude.eaglercraft.v1_8.plugin.backend_rpc_protocol.pkt.server.SPacketRPCEnabledFailure;
import net.lax1dude.eaglercraft.v1_8.plugin.backend_rpc_protocol.pkt.server.SPacketRPCEnabledSuccess;
import net.lax1dude.eaglercraft.v1_8.plugin.bukkit_rpc_helper.EaglerXBukkitAPIPlugin;
import net.lax1dude.eaglercraft.v1_8.plugin.bukkit_rpc_helper.api.EaglerRPCException;
import net.lax1dude.eaglercraft.v1_8.plugin.bukkit_rpc_helper.api.EaglerRPCInitException;
import net.lax1dude.eaglercraft.v1_8.plugin.bukkit_rpc_helper.api.IEaglerXBukkitAPI;
import net.lax1dude.eaglercraft.v1_8.plugin.bukkit_rpc_helper.api.response.EaglerRPCTimeoutException;

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
public class PlayerDataObj {

	public static final String METADATA_BASE = "EXRPC_PDataObj";

	public final Player player;

	public String pluginChName = null;

	public volatile boolean hasRecievedReady = false;
	public volatile boolean isSupported = true;
	public volatile EaglerXBukkitImpl currentAPI = null;

	public volatile EaglerRPCFutureImpl<IEaglerXBukkitAPI> openFuture = null;

	public static PlayerDataObj getForPlayer(Player player) {
		List<MetadataValue> vigg = player.getMetadata(METADATA_BASE);
		return !vigg.isEmpty() ? (PlayerDataObj)vigg.get(0).value() : null;
	}

	public static void setupPlayer(Player player) {
		player.setMetadata(METADATA_BASE, new LazyMetadataValue(EaglerXBukkitAPIPlugin.getEagler(), () -> new PlayerDataObj(player)));
	}

	protected PlayerDataObj(Player player) {
		this.player = player;
	}

	public void firePluginReadyMsgRecieved(boolean modern) {
		synchronized(this) {
			if(!hasRecievedReady) {
				hasRecievedReady = true;
				pluginChName = modern ? EaglerBackendRPCProtocol.CHANNEL_NAME_MODERN : EaglerBackendRPCProtocol.CHANNEL_NAME;
				if(openFuture != null) {
					EaglerXBukkitImpl.sendHelloPacket(pluginChName, player);
				}
			}
		}
	}

	public void firePluginMsgRecievedInternal(byte[] data) {
		EaglerXBukkitImpl apiObj = null;
		synchronized(this) {
			if(openFuture != null) {
				try {
					handleOpenResult(openFuture, data);
				}finally {
					openFuture = null;
				}
			}else if(currentAPI != null) {
				apiObj = currentAPI;
			}
		}
		if(apiObj != null) {
			handleAPIMessage(apiObj, data);
		}
	}

	public void firePlayerQuitEventInternal() {
		synchronized(this) {
			if(openFuture != null) {
				try {
					openFuture.fireExceptionInternal(new EaglerRPCException("Player quit before the connection could be established!"));
				}finally {
					openFuture = null;
				}
			}else if(currentAPI != null) {
				currentAPI.fireAPIClosedEventInternal();
			}
		}
	}

	private void handleOpenResult(EaglerRPCFutureImpl<IEaglerXBukkitAPI> apiFuture, byte[] data) {
		EaglerBackendRPCPacket ret;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(data);
			ret = EaglerBackendRPCProtocol.INIT.readPacket(new DataInputStream(bis), EaglerBackendRPCProtocol.SERVER_TO_CLIENT);
			if(bis.available() > 0) {
				throw new IOException("There were " + bis.available() + " bytes available after reading packet \"" + ret.getClass().getSimpleName() + "\"!");
			}
		}catch(IOException ex) {
			EaglerXBukkitAPIPlugin.logger().log(Level.SEVERE, "[" + player.getName() + "] Could not parse incoming RPC packet from bungee/velocity server! (protocol: INIT)", ex);
			apiFuture.fireExceptionInternal(ex);
			return;
		}
		if(ret instanceof SPacketRPCEnabledSuccess) {
			SPacketRPCEnabledSuccess pkt = (SPacketRPCEnabledSuccess)ret;
			if(pkt.selectedRPCProtocol != EaglerBackendRPCProtocol.V1.vers) {
				try {
					// send raw CPacketRPCDisabled
					player.sendPluginMessage(EaglerXBukkitAPIPlugin.getEagler(), pluginChName, new byte[] { 0x03 });
				}finally {
					apiFuture.fireExceptionInternal(new EaglerRPCException("Server tried to select an unsupported protocol: " + pkt.selectedRPCProtocol));
				}
			}else {
				currentAPI = EaglerXBukkitImpl.createFromHandshakeInternal(this, pkt);
				apiFuture.fireCompleteInternal(currentAPI);
			}
		}else if(ret instanceof SPacketRPCEnabledFailure) {
			SPacketRPCEnabledFailure pkt = (SPacketRPCEnabledFailure)ret;
			String msg = "Server responded with failure code: ";
			switch(pkt.failureCode) {
			case SPacketRPCEnabledFailure.FAILURE_CODE_NOT_ENABLED:
				msg += "FAILURE_CODE_NOT_ENABLED";
				break;
			case SPacketRPCEnabledFailure.FAILURE_CODE_NOT_EAGLER_PLAYER:
				msg += "FAILURE_CODE_NOT_EAGLER_PLAYER";
				break;
			case SPacketRPCEnabledFailure.FAILURE_CODE_OUTDATED_SERVER:
				msg += "FAILURE_CODE_OUTDATED_SERVER";
				break;
			case SPacketRPCEnabledFailure.FAILURE_CODE_OUTDATED_CLIENT:
				msg += "FAILURE_CODE_OUTDATED_CLIENT";
				break;
			case SPacketRPCEnabledFailure.FAILURE_CODE_INTERNAL_ERROR:
				msg += "FAILURE_CODE_INTERNAL_ERROR";
				break;
			default:
				msg += pkt.failureCode;
				break;
			}
			apiFuture.fireExceptionInternal(new EaglerRPCInitException(pkt.failureCode, msg));
		}else {
			EaglerXBukkitAPIPlugin.logger().severe("[" + player.getName() + "] Unknown response type from bungee/velocity to API open request: " + ret.getClass().getSimpleName());
		}
	}

	private void handleAPIMessage(EaglerXBukkitImpl apiObj, byte[] data) {
		EaglerBackendRPCPacket ret;
		try {
			ret = apiObj.decodePacket(data);
		}catch(IOException ex) {
			EaglerXBukkitAPIPlugin.logger().log(Level.SEVERE, "[" + player.getName() + "] Could not parse incoming RPC packet from bungee/velocity server! (protocol: " + apiObj.getRPCProtocolVersion() + ")", ex);
			return;
		}
		apiObj.fireAPIPacketRecievedInternal(ret);
	}

	public void fireCheckRequestTimeoutsInternal(long now) {
		EaglerXBukkitImpl apiObj;
		synchronized(this) {
			if(openFuture != null) {
				if(openFuture.hasExpiredBetter(now)) {
					try {
						EaglerXBukkitAPIPlugin.logger().warning("[" + player.getName() + "] An RPC open request timed out before it could be completed!");
						openFuture.fireExceptionInternal(new EaglerRPCTimeoutException("The request was not completed in time!"));
					}catch(Throwable t) {
						EaglerXBukkitAPIPlugin.logger().log(Level.SEVERE, "[" + player.getName() + "] An unhandled exception was thrown while firing request timeout signal!", t);
					}finally {
						openFuture = null;
					}
				}
				return;
			}else {
				apiObj = currentAPI;
			}
		}
		if(apiObj != null) {
			apiObj.cleanupTimedOutRequests(now);
		}
	}

}
