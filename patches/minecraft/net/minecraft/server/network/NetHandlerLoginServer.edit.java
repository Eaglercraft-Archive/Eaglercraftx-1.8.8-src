
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 9  @  3 : 15

~ import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;
~ import net.lax1dude.eaglercraft.v1_8.ClientUUIDLoadingCache;
~ import net.lax1dude.eaglercraft.v1_8.EaglerInputStream;
~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
~ import net.lax1dude.eaglercraft.v1_8.EaglercraftVersion;
~ import net.lax1dude.eaglercraft.v1_8.sp.server.EaglerMinecraftServer;

> CHANGE  1 : 2  @  1 : 2

~ import net.minecraft.network.EnumConnectionState;

> DELETE  4  @  4 : 5

> DELETE  1  @  1 : 2

> DELETE  2  @  2 : 3

> INSERT  2 : 8  @  2

+ import net.lax1dude.eaglercraft.v1_8.sp.server.socket.IntegratedServerPlayerNetworkManager;
+ import net.lax1dude.eaglercraft.v1_8.sp.server.voice.IntegratedVoiceService;
+ 
+ import java.io.DataInputStream;
+ import java.io.IOException;
+ 

> CHANGE  1 : 3  @  1 : 3

~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

> DELETE  2  @  2 : 3

> DELETE  1  @  1 : 3

> CHANGE  1 : 2  @  1 : 2

~ 	public final IntegratedServerPlayerNetworkManager networkManager;

> INSERT  3 : 7  @  3

+ 	private byte[] loginSkinPacket;
+ 	private byte[] loginCapePacket;
+ 	private int selectedProtocol = 3;
+ 	private EaglercraftUUID clientBrandUUID;

> DELETE  1  @  1 : 2

> CHANGE  2 : 4  @  2 : 3

~ 	public NetHandlerLoginServer(MinecraftServer parMinecraftServer,
~ 			IntegratedServerPlayerNetworkManager parNetworkManager) {

> DELETE  2  @  2 : 3

> CHANGE  11 : 23  @  11 : 12

~ 						this.field_181025_l, this.selectedProtocol, this.clientBrandUUID);
~ 				((EaglerMinecraftServer) field_181025_l.mcServer).getSkinService()
~ 						.processLoginPacket(this.loginSkinPacket, field_181025_l, 3); // singleplayer always sends V3
~ 																						// skin in handshake
~ 				if (this.loginCapePacket != null) {
~ 					((EaglerMinecraftServer) field_181025_l.mcServer).getCapeService()
~ 							.processLoginPacket(this.loginCapePacket, field_181025_l);
~ 				}
~ 				IntegratedVoiceService svc = ((EaglerMinecraftServer) field_181025_l.mcServer).getVoiceService();
~ 				if (svc != null) {
~ 					svc.handlePlayerLoggedIn(entityplayermp);
~ 				}

> CHANGE  23 : 24  @  23 : 29

~ 		String s = this.server.getConfigurationManager().allowUserToConnect(this.loginGameProfile);

> CHANGE  4 : 6  @  4 : 16

~ 			this.networkManager.sendPacket(new S02PacketLoginSuccess(this.loginGameProfile, this.selectedProtocol));
~ 			this.networkManager.setConnectionState(EnumConnectionState.PLAY);

> CHANGE  6 : 20  @  6 : 8

~ 				entityplayermp = this.server.getConfigurationManager().createPlayerForUser(this.loginGameProfile);
~ 				this.server.getConfigurationManager().initializeConnectionToPlayer(this.networkManager, entityplayermp,
~ 						this.selectedProtocol, this.clientBrandUUID);
~ 				((EaglerMinecraftServer) entityplayermp.mcServer).getSkinService()
~ 						.processLoginPacket(this.loginSkinPacket, entityplayermp, 3); // singleplayer always sends V3
~ 																						// skin in handshake
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

> CHANGE  5 : 25  @  5 : 10

~ 		if (c00packetloginstart.getProtocols() != null) {
~ 			try {
~ 				DataInputStream dis = new DataInputStream(new EaglerInputStream(c00packetloginstart.getProtocols()));
~ 				int maxSupported = -1;
~ 				int protocolCount = dis.readUnsignedShort();
~ 				for (int i = 0; i < protocolCount; ++i) {
~ 					int p = dis.readUnsignedShort();
~ 					if ((p == 3 || p == 4) && p > maxSupported) {
~ 						maxSupported = p;
~ 					}
~ 				}
~ 				if (maxSupported != -1) {
~ 					selectedProtocol = maxSupported;
~ 				} else {
~ 					this.closeConnection("Unknown protocol!");
~ 					return;
~ 				}
~ 			} catch (IOException ex) {
~ 				selectedProtocol = 3;
~ 			}

> CHANGE  1 : 2  @  1 : 2

~ 			selectedProtocol = 3;

> CHANGE  1 : 11  @  1 : 2

~ 		this.loginGameProfile = this.getOfflineProfile(c00packetloginstart.getProfile());
~ 		this.loginSkinPacket = c00packetloginstart.getSkin();
~ 		this.loginCapePacket = c00packetloginstart.getCape();
~ 		this.clientBrandUUID = selectedProtocol <= 3 ? EaglercraftVersion.legacyClientUUIDInSharedWorld
~ 				: c00packetloginstart.getBrandUUID();
~ 		if (ClientUUIDLoadingCache.PENDING_UUID.equals(clientBrandUUID)
~ 				|| ClientUUIDLoadingCache.VANILLA_UUID.equals(clientBrandUUID)) {
~ 			this.clientBrandUUID = null;
~ 		}
~ 		this.currentLoginState = NetHandlerLoginServer.LoginState.READY_TO_ACCEPT;

> DELETE  3  @  3 : 15

> DELETE  1  @  1 : 42

> CHANGE  3 : 5  @  3 : 4

~ 		EaglercraftUUID uuid = EaglercraftUUID
~ 				.nameUUIDFromBytes(("OfflinePlayer:" + original.getName()).getBytes(Charsets.UTF_8));

> EOF
