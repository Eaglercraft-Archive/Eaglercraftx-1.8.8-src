package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.command;

import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.EaglerXBungee;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.config.EaglerBungeeConfig;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.config.EaglerListenerConfig;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.config.EaglerRateLimiter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
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
public class CommandRatelimit extends Command {

	public CommandRatelimit() {
		super("ratelimit", "eaglercraft.command.ratelimit");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if((args.length != 1 && args.length != 2) || !args[0].equalsIgnoreCase("reset")) {
			TextComponent comp = new TextComponent("Usage: /ratelimit reset [ip|login|motd|query]"); //TODO: allow reset ratelimit on specific listeners
			comp.setColor(ChatColor.RED);
			sender.sendMessage(comp);
		}else {
			int resetNum = 0;
			if(args.length == 2) {
				if(args[1].equalsIgnoreCase("ip")) {
					resetNum = 1;
				}else if(args[1].equalsIgnoreCase("login")) {
					resetNum = 2;
				}else if(args[1].equalsIgnoreCase("motd")) {
					resetNum = 3;
				}else if(args[1].equalsIgnoreCase("query")) {
					resetNum = 4;
				}else {
					TextComponent comp = new TextComponent("Unknown ratelimit '" + args[1] + "'!");
					comp.setColor(ChatColor.RED);
					sender.sendMessage(comp);
					return;
				}
			}
			EaglerBungeeConfig conf = EaglerXBungee.getEagler().getConfig();
			for(EaglerListenerConfig listener : conf.getServerListeners()) {
				if(resetNum == 0 || resetNum == 1) {
					EaglerRateLimiter limiter = listener.getRatelimitIp();
					if(limiter != null) {
						limiter.reset();
					}
				}
				if(resetNum == 0 || resetNum == 2) {
					EaglerRateLimiter limiter = listener.getRatelimitLogin();
					if(limiter != null) {
						limiter.reset();
					}
				}
				if(resetNum == 0 || resetNum == 3) {
					EaglerRateLimiter limiter = listener.getRatelimitMOTD();
					if(limiter != null) {
						limiter.reset();
					}
				}
				if(resetNum == 0 || resetNum == 4) {
					EaglerRateLimiter limiter = listener.getRatelimitQuery();
					if(limiter != null) {
						limiter.reset();
					}
				}
			}
			sender.sendMessage(new TextComponent("Ratelimits reset."));
		}
	}

}
