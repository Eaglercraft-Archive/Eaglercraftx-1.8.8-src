package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.command;

import java.nio.charset.StandardCharsets;

import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.auth.SHA1Digest;
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
public class CommandConfirmCode extends Command {

	public static String confirmHash = null;

	public CommandConfirmCode() {
		super("confirm-code", "eaglercraft.command.confirmcode", "confirmcode");
	}

	@Override
	public void execute(CommandSender var1, String[] var2) {
		if(var2.length != 1) {
			var1.sendMessage(new TextComponent(ChatColor.RED + "How to use: " + ChatColor.WHITE + "/confirm-code <code>"));
		}else {
			var1.sendMessage(new TextComponent(ChatColor.YELLOW + "Server list 2FA code has been set to: " + ChatColor.GREEN + var2[0]));
			var1.sendMessage(new TextComponent(ChatColor.YELLOW + "You can now return to the server list site and continue"));
			byte[] bts = var2[0].getBytes(StandardCharsets.US_ASCII);
			SHA1Digest dg = new SHA1Digest();
			dg.update(bts, 0, bts.length);
			byte[] f = new byte[20];
			dg.doFinal(f, 0);
			confirmHash = SHA1Digest.hash2string(f);
		}
	}

}
