package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.command;

import java.util.logging.Level;

import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.EaglerXBungee;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.auth.DefaultAuthSystem;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.auth.DefaultAuthSystem.AuthException;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.config.EaglerAuthConfig;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.command.ConsoleCommandSender;

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
public class CommandEaglerPurge extends Command {

	public CommandEaglerPurge(String name) {
		super(name + "-purge", "eaglercraft.command.purge");
	}

	@Override
	public void execute(CommandSender var1, String[] var2) {
		if(var1 instanceof ConsoleCommandSender) {
			if(var2.length != 1) {
				TextComponent comp = new TextComponent("Use /" + getName() + " <maxAge>");
				comp.setColor(ChatColor.RED);
				var1.sendMessage(comp);
				return;
			}
			int mx;
			try {
				mx = Integer.parseInt(var2[0]);
			}catch(NumberFormatException ex) {
				TextComponent comp = new TextComponent("'" + var2[0] + "' is not an integer!");
				comp.setColor(ChatColor.RED);
				var1.sendMessage(comp);
				return;
			}
			EaglerAuthConfig authConf = EaglerXBungee.getEagler().getConfig().getAuthConfig();
			if(authConf.isEnableAuthentication() && authConf.isUseBuiltInAuthentication()) {
				DefaultAuthSystem srv = EaglerXBungee.getEagler().getAuthService();
				if(srv != null) {
					int cnt;
					try {
						EaglerXBungee.logger().warning("Console is attempting to purge all accounts with " + mx + " days of inactivity");
						cnt = srv.pruneUsers(System.currentTimeMillis() - mx * 86400000l);
					}catch(AuthException ex) {
						EaglerXBungee.logger().log(Level.SEVERE, "Failed to purge accounts", ex);
						TextComponent comp = new TextComponent("Failed to purge, check log! Reason: " + ex.getMessage());
						comp.setColor(ChatColor.AQUA);
						var1.sendMessage(comp);
						return;
					}
					EaglerXBungee.logger().warning("Console purged " + cnt + " accounts from auth database");
					TextComponent comp = new TextComponent("Purged " + cnt + " old accounts from the database");
					comp.setColor(ChatColor.AQUA);
					var1.sendMessage(comp);
				}
			}
		}else {
			TextComponent comp = new TextComponent("This command can only be run from the console!");
			comp.setColor(ChatColor.RED);
			var1.sendMessage(comp);
		}
	}

}
