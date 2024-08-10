package net.lax1dude.eaglercraft.v1_8.plugin.bukkit_rpc_helper;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;

import net.lax1dude.eaglercraft.v1_8.plugin.backend_rpc_protocol.EaglerBackendRPCProtocol;
import net.lax1dude.eaglercraft.v1_8.plugin.bukkit_rpc_helper.impl.PlayerDataObj;

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
public class EaglerXBukkitAPIListener implements Listener, PluginMessageListener {

	@EventHandler
	public void onLoginEvent(PlayerLoginEvent evt) {
		PlayerDataObj.setupPlayer(evt.getPlayer());
	}

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] data) {
		if(EaglerBackendRPCProtocol.CHANNEL_NAME.equals(channel)) {
			PlayerDataObj dataObj = PlayerDataObj.getForPlayer(player);
			if(dataObj != null) {
				dataObj.firePluginMsgRecievedInternal(data);
			}
		}else if(EaglerBackendRPCProtocol.CHANNEL_NAME_READY.equals(channel)) {
			PlayerDataObj dataObj = PlayerDataObj.getForPlayer(player);
			if(dataObj != null) {
				dataObj.firePluginReadyMsgRecieved();
			}
		}
	}

	@EventHandler
	public void onQuitEvent(PlayerQuitEvent evt) {
		PlayerDataObj dataObj = PlayerDataObj.getForPlayer(evt.getPlayer());
		if(dataObj != null) {
			dataObj.firePlayerQuitEventInternal();
		}
	}

}
