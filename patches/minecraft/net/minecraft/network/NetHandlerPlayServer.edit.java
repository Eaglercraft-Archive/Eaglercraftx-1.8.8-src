
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  5 : 6  @  5 : 9

~ import net.lax1dude.eaglercraft.v1_8.netty.Unpooled;

> CHANGE  3 : 4  @  3 : 4

~ import java.util.List;

> INSERT  2 : 4  @  2

+ 
+ import net.lax1dude.eaglercraft.v1_8.sp.server.EaglerMinecraftServer;

> DELETE  25  @  25 : 29

> INSERT  33 : 34  @  33

+ import net.minecraft.network.play.server.S3FPacketCustomPayload;

> DELETE  2  @  2 : 3

> INSERT  16 : 18  @  16

+ import net.lax1dude.eaglercraft.v1_8.sp.server.socket.IntegratedServerPlayerNetworkManager;
+ 

> CHANGE  1 : 3  @  1 : 3

~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

> INSERT  2 : 3  @  2

+ 

> CHANGE  1 : 2  @  1 : 2

~ 	public final IntegratedServerPlayerNetworkManager netManager;

> INSERT  16 : 17  @  16

+ 	private boolean hasDisconnected = false;

> CHANGE  1 : 3  @  1 : 2

~ 	public NetHandlerPlayServer(MinecraftServer server, IntegratedServerPlayerNetworkManager networkManagerIn,
~ 			EntityPlayerMP playerIn) {

> CHANGE  35 : 36  @  35 : 36

~ 	public IntegratedServerPlayerNetworkManager getNetworkManager() {

> CHANGE  5 : 7  @  5 : 17

~ 		this.netManager.sendPacket(new S40PacketDisconnect(chatcomponenttext));
~ 		this.netManager.closeChannel(chatcomponenttext);

> DELETE  3  @  3 : 4

> DELETE  12  @  12 : 13

> DELETE  228  @  228 : 229

> DELETE  62  @  62 : 64

> DELETE  64  @  64 : 65

> CHANGE  3 : 6  @  3 : 4

~ 			WorldServer[] srv = this.serverController.worldServers;
~ 			for (int i = 0; i < srv.length; ++i) {
~ 				WorldServer worldserver = srv[i];

> CHANGE  47 : 61  @  47 : 59

~ 		if (!hasDisconnected) {
~ 			hasDisconnected = true;
~ 			logger.info(this.playerEntity.getName() + " lost connection: " + ichatcomponent);
~ 			this.serverController.refreshStatusNextTick();
~ 			ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation("multiplayer.player.left",
~ 					new Object[] { this.playerEntity.getDisplayName() });
~ 			chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.YELLOW);
~ 			this.serverController.getConfigurationManager().sendChatMsg(chatcomponenttranslation);
~ 			this.playerEntity.mountEntityAndWakeUp();
~ 			this.serverController.getConfigurationManager().playerLoggedOut(this.playerEntity);
~ 			if (this.playerEntity.getName().equals(this.serverController.getServerOwner())) {
~ 				logger.info("Stopping singleplayer server as player logged out");
~ 				this.serverController.initiateShutdown();
~ 			}

> DELETE  1  @  1 : 2

> DELETE  30  @  30 : 31

> DELETE  10  @  10 : 11

> INSERT  20 : 24  @  20

+ 				if (this.serverController.worldServers[0].getWorldInfo().getGameRulesInstance()
+ 						.getBoolean("colorCodes")) {
+ 					s = net.minecraft.util.StringUtils.translateControlCodesAlternate(s);
+ 				}

> DELETE  19  @  19 : 20

> DELETE  5  @  5 : 6

> DELETE  35  @  35 : 36

> DELETE  32  @  32 : 33

> DELETE  14  @  14 : 17

> DELETE  22  @  22 : 23

> DELETE  4  @  4 : 5

> DELETE  41  @  41 : 42

> DELETE  10  @  10 : 12

> DELETE  48  @  48 : 50

> DELETE  11  @  11 : 12

> CHANGE  19 : 25  @  19 : 21

~ 				String s = EnumChatFormatting.getTextWithoutFormattingCodes(aichatcomponent[i].getUnformattedText());
~ 				if (this.serverController.worldServers[0].getWorldInfo().getGameRulesInstance()
~ 						.getBoolean("colorCodes")) {
~ 					s = net.minecraft.util.StringUtils.translateControlCodesAlternate(s);
~ 				}
~ 				tileentitysign.signText[i] = new ChatComponentText(s);

> DELETE  21  @  21 : 22

> CHANGE  5 : 10  @  5 : 11

~ 		List<String> lst = this.serverController.getTabCompletions(this.playerEntity, c14packettabcomplete.getMessage(),
~ 				c14packettabcomplete.getTargetBlock());
~ 		String[] fuckOff = new String[lst.size()];
~ 		for (int i = 0; i < fuckOff.length; ++i) {
~ 			fuckOff[i] = lst.get(i);

> CHANGE  2 : 3  @  2 : 4

~ 		this.playerEntity.playerNetServerHandler.sendPacket(new S3APacketTabComplete(fuckOff));

> DELETE  3  @  3 : 4

> DELETE  4  @  4 : 5

> CHANGE  1 : 2  @  1 : 3

~ 			PacketBuffer packetbuffer3 = c17packetcustompayload.getBufferData();

> INSERT  21 : 22  @  21

+ 				logger.error(exception3);

> DELETE  1  @  1 : 3

> CHANGE  4 : 5  @  4 : 6

~ 			PacketBuffer packetbuffer2 = c17packetcustompayload.getBufferData();

> INSERT  24 : 25  @  24

+ 				logger.error(exception4);

> DELETE  1  @  1 : 3

> DELETE  51  @  51 : 53

> INSERT  31 : 35  @  31

+ 					if (this.serverController.worldServers[0].getWorldInfo().getGameRulesInstance()
+ 							.getBoolean("colorCodes")) {
+ 						s = net.minecraft.util.StringUtils.translateControlCodesAlternate(s);
+ 					}

> INSERT  5 : 24  @  5

+ 		} else if ("EAG|Skins-1.8".equals(c17packetcustompayload.getChannelName())) {
+ 			byte[] r = new byte[c17packetcustompayload.getBufferData().readableBytes()];
+ 			c17packetcustompayload.getBufferData().readBytes(r);
+ 			((EaglerMinecraftServer) serverController).getSkinService().processPacket(r, playerEntity);
+ 		} else if ("EAG|MyUpdCert-1.8".equals(c17packetcustompayload.getChannelName())) {
+ 			if (playerEntity.updateCertificate == null) {
+ 				PacketBuffer pb = c17packetcustompayload.getBufferData();
+ 				byte[] cert = new byte[pb.readableBytes()];
+ 				pb.readBytes(cert);
+ 				playerEntity.updateCertificate = cert;
+ 				List<EntityPlayerMP> lst = playerEntity.mcServer.getConfigurationManager().func_181057_v();
+ 				for (int i = 0, l = lst.size(); i < l; ++i) {
+ 					EntityPlayerMP player = lst.get(i);
+ 					if (player != playerEntity) {
+ 						player.playerNetServerHandler.sendPacket(new S3FPacketCustomPayload("EAG|UpdateCert-1.8",
+ 								new PacketBuffer(Unpooled.buffer(cert, cert.length).writerIndex(cert.length))));
+ 					}
+ 				}
+ 			}

> DELETE  1  @  1 : 2

> EOF
