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
