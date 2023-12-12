package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.command;

import java.util.logging.Level;

import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.EaglerXBungee;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.auth.DefaultAuthSystem;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.config.EaglerAuthConfig;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.EaglerInitialHandler;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
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
public class CommandEaglerRegister extends Command {

	public CommandEaglerRegister(String name) {
		super(name);
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(sender instanceof ProxiedPlayer) {
			ProxiedPlayer player = (ProxiedPlayer)sender;
			if(args.length != 1) {
				TextComponent comp = new TextComponent("Use: /" + getName() + " <password>");
				comp.setColor(ChatColor.RED);
				player.sendMessage(comp);
				return;
			}
			EaglerAuthConfig authConf = EaglerXBungee.getEagler().getConfig().getAuthConfig();
			if(authConf.isEnableAuthentication() && authConf.isUseBuiltInAuthentication()) {
				DefaultAuthSystem srv = EaglerXBungee.getEagler().getAuthService();
				if(srv != null) {
					if(!(player.getPendingConnection() instanceof EaglerInitialHandler)) {
						try {
							srv.processSetPassword(player, args[0]);
							sender.sendMessage(new TextComponent(authConf.getCommandSuccessText()));
						}catch(DefaultAuthSystem.TooManyRegisteredOnIPException ex) {
							String tooManyReg = authConf.getTooManyRegistrationsMessage();
							sender.sendMessage(new TextComponent(tooManyReg));
						}catch(DefaultAuthSystem.AuthException ex) {
							EaglerXBungee.logger().log(Level.SEVERE, "Internal exception while processing password change from \"" + player.getName() + "\"", ex);
							TextComponent comp = new TextComponent("Internal error, check console logs");
							comp.setColor(ChatColor.RED);
							sender.sendMessage(comp);
						}
					}else {
						player.sendMessage(new TextComponent(authConf.getNeedVanillaToRegisterMessage()));
					}
				}
			}
		}else {
			TextComponent comp = new TextComponent("You must be a player to use this command!");
			comp.setColor(ChatColor.RED);
			sender.sendMessage(comp);
		}
	}

}
