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

package net.lax1dude.eaglercraft.v1_8.plugin.bukkit_rpc_helper;

import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;

import net.lax1dude.eaglercraft.v1_8.plugin.backend_rpc_protocol.EaglerBackendRPCProtocol;
import net.lax1dude.eaglercraft.v1_8.plugin.bukkit_rpc_helper.impl.PlayerDataObj;

public class EaglerXBukkitAPIPlugin extends JavaPlugin {

	private static EaglerXBukkitAPIPlugin instance = null;

	private Timer timeoutHandler;

	public EaglerXBukkitAPIPlugin() {
		instance = this;
	}

	@Override
	public void onLoad() {
		
	}

	@Override
	public void onEnable() {
		EaglerXBukkitAPIListener ls = new EaglerXBukkitAPIListener();
		Server svr = getServer();
		svr.getPluginManager().registerEvents(ls, this);
		Messenger msgr = svr.getMessenger();
		boolean registerLegacy = !isPost_v1_13();
		if(registerLegacy) {
			try {
				msgr.registerOutgoingPluginChannel(this, EaglerBackendRPCProtocol.CHANNEL_NAME);
			}catch(Throwable t) {
				registerLegacy = false;
			}
		}
		if(!registerLegacy) {
			getLogger().warning("Note: Only the modernized plugin channel names can be used for this server!");
			getLogger().warning("Make sure to set \"use_modernized_channel_names: true\" in bungee/velocity plugin settings.yml");
		}
		msgr.registerOutgoingPluginChannel(this, EaglerBackendRPCProtocol.CHANNEL_NAME_MODERN);
		if(registerLegacy) {
			msgr.registerIncomingPluginChannel(this, EaglerBackendRPCProtocol.CHANNEL_NAME, ls);
		}
		msgr.registerIncomingPluginChannel(this, EaglerBackendRPCProtocol.CHANNEL_NAME_MODERN, ls);
		if(registerLegacy) {
			msgr.registerIncomingPluginChannel(this, EaglerBackendRPCProtocol.CHANNEL_NAME_READY, ls);
		}
		msgr.registerIncomingPluginChannel(this, EaglerBackendRPCProtocol.CHANNEL_NAME_READY_MODERN, ls);
		if(timeoutHandler == null) {
			timeoutHandler = new Timer("EaglerXBukkitAPI: Timeout cleanup thread");
			timeoutHandler.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					Collection<? extends Player> pp = EaglerXBukkitAPIPlugin.this.getServer().getOnlinePlayers();
					if(!pp.isEmpty()) {
						long now = System.nanoTime() / 1000000l;
						for(Player p : pp) {
							PlayerDataObj.getForPlayer(p).fireCheckRequestTimeoutsInternal(now);
						}
					}
				}
			}, 0l, 5000l);
		}
	}

	@Override
	public void onDisable() {
		getServer().getMessenger().unregisterOutgoingPluginChannel(this);
		getServer().getMessenger().unregisterIncomingPluginChannel(this);
		if(timeoutHandler != null) {
			timeoutHandler.cancel();
			timeoutHandler = null;
		}
	}

	public static EaglerXBukkitAPIPlugin getEagler() {
		return instance;
	}

	public static Logger logger() {
		return instance.getLogger();
	}

	private boolean isPost_v1_13() {
		String[] ver = getServer().getVersion().split("[\\.\\-]");
		if(ver.length >= 2) {
			try {
				return Integer.parseInt(ver[0]) >= 1 || Integer.parseInt(ver[1]) >= 13;
			}catch(NumberFormatException ex) {
			}
		}
		return false;
	}

}