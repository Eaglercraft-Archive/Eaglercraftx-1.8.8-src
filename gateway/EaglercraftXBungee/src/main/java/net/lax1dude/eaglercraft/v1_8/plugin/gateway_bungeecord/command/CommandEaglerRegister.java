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
 * Copyright (c) 2022-2023 LAX1DUDE. All Rights Reserved.
 * 
 * WITH THE EXCEPTION OF PATCH FILES, MINIFIED JAVASCRIPT, AND ALL FILES
 * NORMALLY FOUND IN AN UNMODIFIED MINECRAFT RESOURCE PACK, YOU ARE NOT ALLOWED
 * TO SHARE, DISTRIBUTE, OR REPURPOSE ANY FILE USED BY OR PRODUCED BY THE
 * SOFTWARE IN THIS REPOSITORY WITHOUT PRIOR PERMISSION FROM THE PROJECT AUTHOR.
 * 
 * NOT FOR COMMERCIAL OR MALICIOUS USE
 * 
 * (please read the 'LICENSE' file this repo's root directory for more info)
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
