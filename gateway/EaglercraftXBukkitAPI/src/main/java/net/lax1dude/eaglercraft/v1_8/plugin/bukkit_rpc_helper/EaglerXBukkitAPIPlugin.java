package net.lax1dude.eaglercraft.v1_8.plugin.bukkit_rpc_helper;

import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

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
		getServer().getPluginManager().registerEvents(ls, this);
		getServer().getMessenger().registerOutgoingPluginChannel(this, EaglerBackendRPCProtocol.CHANNEL_NAME);
		getServer().getMessenger().registerIncomingPluginChannel(this, EaglerBackendRPCProtocol.CHANNEL_NAME, ls);
		getServer().getMessenger().registerIncomingPluginChannel(this, EaglerBackendRPCProtocol.CHANNEL_NAME_READY, ls);
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

}
