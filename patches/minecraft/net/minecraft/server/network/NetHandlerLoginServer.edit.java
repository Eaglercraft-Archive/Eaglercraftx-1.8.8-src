
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 7  @  3 : 15

~ import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;
~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
~ import net.lax1dude.eaglercraft.v1_8.sp.server.EaglerMinecraftServer;

> CHANGE  1 : 2  @  1 : 2

~ import net.minecraft.network.EnumConnectionState;

> DELETE  4  @  4 : 5

> DELETE  1  @  1 : 2

> DELETE  2  @  2 : 3

> INSERT  2 : 5  @  2

+ import net.lax1dude.eaglercraft.v1_8.sp.server.socket.IntegratedServerPlayerNetworkManager;
+ import net.lax1dude.eaglercraft.v1_8.sp.server.voice.IntegratedVoiceService;
+ 

> CHANGE  1 : 3  @  1 : 3

~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

> DELETE  2  @  2 : 3

> CHANGE  1 : 2  @  1 : 2

~ 	private static final EaglercraftRandom RANDOM = new EaglercraftRandom();

> CHANGE  2 : 3  @  2 : 3

~ 	public final IntegratedServerPlayerNetworkManager networkManager;

> INSERT  3 : 5  @  3

+ 	private byte[] loginSkinPacket;
+ 	private byte[] loginCapePacket;

> DELETE  1  @  1 : 2

> CHANGE  2 : 4  @  2 : 3

~ 	public NetHandlerLoginServer(MinecraftServer parMinecraftServer,
~ 			IntegratedServerPlayerNetworkManager parNetworkManager) {

> INSERT  15 : 25  @  15

+ 				((EaglerMinecraftServer) field_181025_l.mcServer).getSkinService()
+ 						.processLoginPacket(this.loginSkinPacket, field_181025_l);
+ 				if (this.loginCapePacket != null) {
+ 					((EaglerMinecraftServer) field_181025_l.mcServer).getCapeService()
+ 							.processLoginPacket(this.loginCapePacket, field_181025_l);
+ 				}
+ 				IntegratedVoiceService svc = ((EaglerMinecraftServer) field_181025_l.mcServer).getVoiceService();
+ 				if (svc != null) {
+ 					svc.handlePlayerLoggedIn(entityplayermp);
+ 				}

> CHANGE  23 : 24  @  23 : 29

~ 		String s = this.server.getConfigurationManager().allowUserToConnect(this.loginGameProfile);

> DELETE  4  @  4 : 15

> INSERT  1 : 2  @  1

+ 			this.networkManager.setConnectionState(EnumConnectionState.PLAY);

> CHANGE  6 : 18  @  6 : 8

~ 				entityplayermp = this.server.getConfigurationManager().createPlayerForUser(this.loginGameProfile);
~ 				this.server.getConfigurationManager().initializeConnectionToPlayer(this.networkManager, entityplayermp);
~ 				((EaglerMinecraftServer) entityplayermp.mcServer).getSkinService()
~ 						.processLoginPacket(this.loginSkinPacket, entityplayermp);
~ 				if (this.loginCapePacket != null) {
~ 					((EaglerMinecraftServer) entityplayermp.mcServer).getCapeService()
~ 							.processLoginPacket(this.loginCapePacket, entityplayermp);
~ 				}
~ 				IntegratedVoiceService svc = ((EaglerMinecraftServer) entityplayermp.mcServer).getVoiceService();
~ 				if (svc != null) {
~ 					svc.handlePlayerLoggedIn(entityplayermp);
~ 				}

> CHANGE  11 : 13  @  11 : 13

~ 				? this.loginGameProfile.toString() + " (channel:" + this.networkManager.playerChannel + ")"
~ 				: ("channel:" + this.networkManager.playerChannel);

> CHANGE  5 : 9  @  5 : 14

~ 		this.loginGameProfile = this.getOfflineProfile(c00packetloginstart.getProfile());
~ 		this.loginSkinPacket = c00packetloginstart.getSkin();
~ 		this.loginCapePacket = c00packetloginstart.getCape();
~ 		this.currentLoginState = NetHandlerLoginServer.LoginState.READY_TO_ACCEPT;

> DELETE  3  @  3 : 15

> DELETE  1  @  1 : 42

> CHANGE  3 : 5  @  3 : 4

~ 		EaglercraftUUID uuid = EaglercraftUUID
~ 				.nameUUIDFromBytes(("OfflinePlayer:" + original.getName()).getBytes(Charsets.UTF_8));

> EOF
