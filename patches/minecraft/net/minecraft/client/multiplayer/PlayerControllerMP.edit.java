
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 5  @  2

+ import java.io.IOException;
+ 
+ import net.lax1dude.eaglercraft.v1_8.socket.EaglercraftNetworkManager;

> DELETE  9  @  6 : 7

> INSERT  16 : 17  @  17

+ import net.minecraft.util.ChatComponentText;

> CHANGE  229 : 240  @  228 : 229

~ 			try {
~ 				this.netClientHandler.getNetworkManager().processReceivedPackets();
~ 			} catch (IOException ex) {
~ 				EaglercraftNetworkManager.logger
~ 						.fatal("Unhandled IOException was thrown " + "while processing multiplayer packets!");
~ 				EaglercraftNetworkManager.logger.fatal(ex);
~ 				EaglercraftNetworkManager.logger.fatal("Disconnecting...");
~ 				this.netClientHandler.getNetworkManager()
~ 						.closeChannel(new ChatComponentText("Exception thrown: " + ex.toString()));
~ 			}
~ 			this.netClientHandler.getSkinCache().flush();

> CHANGE  107 : 109  @  97 : 99

~ 	public EntityPlayerSP func_178892_a(World worldIn, StatFileWriter statWriter) {
~ 		return new EntityPlayerSP(this.mc, worldIn, this.netClientHandler, statWriter);

> EOF
