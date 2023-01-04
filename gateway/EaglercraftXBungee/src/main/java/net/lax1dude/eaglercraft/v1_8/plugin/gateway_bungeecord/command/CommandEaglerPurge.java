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
