package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.command;

import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.EaglerInitialHandler;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

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
public class CommandDomain extends Command {

	public CommandDomain() {
		super("domain", "eaglercraft.command.domain");
	}

	@Override
	public void execute(CommandSender var1, String[] var2) {
		if(var2.length != 1) {
			var1.sendMessage(new TextComponent(ChatColor.RED + "How to use: " + ChatColor.WHITE + "/domain <player>"));
		}else {
			ProxiedPlayer player = ProxyServer.getInstance().getPlayer(var2[0]);
			if(player == null) {
				var1.sendMessage(new TextComponent(ChatColor.RED + "That user is not online"));
				return;
			}
			PendingConnection conn = player.getPendingConnection();
			if(!(conn instanceof EaglerInitialHandler)) {
				var1.sendMessage(new TextComponent(ChatColor.RED + "That user is not using Eaglercraft"));
				return;
			}
			String origin = ((EaglerInitialHandler)conn).origin;
			if(origin != null) {
				var1.sendMessage(new TextComponent(ChatColor.BLUE + "Domain of " + var2[0] + " is '" + origin + "'"));
			}else {
				var1.sendMessage(new TextComponent(ChatColor.RED + "That user's browser did not send an origin header"));
			}
		}
	}

}
