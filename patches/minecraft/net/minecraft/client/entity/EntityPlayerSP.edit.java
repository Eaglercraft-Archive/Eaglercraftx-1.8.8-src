
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 5  @  2

+ import net.lax1dude.eaglercraft.v1_8.EaglercraftVersion;
+ import net.lax1dude.eaglercraft.v1_8.sp.lan.LANClientNetworkManager;
+ import net.lax1dude.eaglercraft.v1_8.sp.socket.ClientIntegratedServerNetworkManager;

> DELETE  3  @  3 : 4

> DELETE  51  @  51 : 52

> INSERT  22 : 23  @  22

+ 	private StatFileWriter statWriter;

> CHANGE  1 : 2  @  1 : 2

~ 	public EntityPlayerSP(Minecraft mcIn, World worldIn, NetHandlerPlayClient netHandler, StatFileWriter statWriter) {

> INSERT  1 : 2  @  1

+ 		this.clientBrandUUIDCache = EaglercraftVersion.clientBrandUUID;

> DELETE  1  @  1 : 2

> INSERT  2 : 3  @  2

+ 		this.statWriter = statWriter;

> CHANGE  116 : 123  @  116 : 117

~ 		if (((sendQueue.getNetworkManager() instanceof ClientIntegratedServerNetworkManager)
~ 				|| (sendQueue.getNetworkManager() instanceof LANClientNetworkManager))
~ 				&& message.startsWith("/eagskull")) {
~ 			this.mc.eagskullCommand.openFileChooser();
~ 		} else {
~ 			this.sendQueue.addToSendQueue(new C01PacketChatMessage(message));
~ 		}

> EOF
