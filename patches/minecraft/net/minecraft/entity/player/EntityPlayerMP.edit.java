
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  4 : 6  @  4 : 6

~ import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;
~ import net.lax1dude.eaglercraft.v1_8.netty.Unpooled;

> DELETE  17  @  17 : 18

> DELETE  51  @  51 : 52

> CHANGE  22 : 26  @  22 : 24

~ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

> INSERT  2 : 3  @  2

+ 

> CHANGE  18 : 19  @  18 : 19

~ 	private long playerLastActiveTime = EagRuntime.steadyTimeMillis();

> INSERT  5 : 7  @  5

+ 	public byte[] updateCertificate = null;
+ 	public EaglercraftUUID clientBrandUUID = null;

> CHANGE  87 : 88  @  87 : 88

~ 		if (!this.openContainer.canInteractWith(this)) {

> CHANGE  51 : 53  @  51 : 53

~ 				for (int i = 0, l = arraylist1.size(); i < l; ++i) {
~ 					this.sendTileEntityUpdate((TileEntity) arraylist1.get(i));

> CHANGE  2 : 4  @  2 : 4

~ 				for (int i = 0, l = arraylist.size(); i < l; ++i) {
~ 					this.getServerForPlayer().getEntityTracker().func_85172_a(this, (Chunk) arraylist.get(i));

> CHANGE  581 : 582  @  581 : 582

~ 		if ("seed".equals(s)) {

> CHANGE  2 : 3  @  2 : 10

~ 			return this.mcServer.getConfigurationManager().canSendCommands(this.getGameProfile());

> CHANGE  6 : 7  @  6 : 10

~ 		return "channel:" + this.playerNetServerHandler.netManager.playerChannel;

> INSERT  6 : 7  @  6

+ 		this.mcServer.getConfigurationManager().updatePlayerViewDistance(this, packetIn.getViewDistance());

> EOF
