
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  4 : 6  @  4 : 6

~ import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;
~ import net.lax1dude.eaglercraft.v1_8.netty.Unpooled;

> DELETE  69  @  69 : 70

> CHANGE  22 : 24  @  22 : 24

~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

> INSERT  2 : 3  @  2

+ 

> INSERT  24 : 25  @  24

+ 	public byte[] updateCertificate = null;

> CHANGE  87 : 88  @  87 : 88

~ 		if (!this.openContainer.canInteractWith(this)) {

> CHANGE  51 : 52  @  51 : 52

~ 				for (TileEntity tileentity : (ArrayList<TileEntity>) arraylist1) {

> CHANGE  3 : 4  @  3 : 4

~ 				for (Chunk chunk1 : (ArrayList<Chunk>) arraylist) {

> CHANGE  582 : 583  @  582 : 583

~ 		if ("seed".equals(s)) {

> CHANGE  2 : 3  @  2 : 10

~ 			return this.mcServer.getConfigurationManager().canSendCommands(this.getGameProfile());

> CHANGE  6 : 7  @  6 : 10

~ 		return "channel:" + this.playerNetServerHandler.netManager.playerChannel;

> INSERT  6 : 7  @  6

+ 		this.mcServer.getConfigurationManager().updatePlayerViewDistance(this, packetIn.getViewDistance());

> EOF
