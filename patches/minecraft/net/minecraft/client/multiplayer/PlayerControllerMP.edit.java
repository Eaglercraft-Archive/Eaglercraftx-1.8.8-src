
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 5  @  2

+ import java.io.IOException;
+ 
+ import net.lax1dude.eaglercraft.v1_8.socket.EaglercraftNetworkManager;

> DELETE  11  @  8 : 9

> INSERT  27 : 28  @  25

+ import net.minecraft.util.ChatComponentText;

> CHANGE  256 : 267  @  253 : 254

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

> CHANGE  363 : 365  @  350 : 352

~ 	public EntityPlayerSP func_178892_a(World worldIn, StatFileWriter statWriter) {
~ 		return new EntityPlayerSP(this.mc, worldIn, this.netClientHandler, statWriter);

> EOF
