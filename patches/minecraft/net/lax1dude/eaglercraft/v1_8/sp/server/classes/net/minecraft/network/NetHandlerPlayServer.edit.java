
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  5 : 6  @  5 : 9

~ import net.lax1dude.eaglercraft.v1_8.netty.Unpooled;

> INSERT  6 : 8  @  6

+ 
+ import net.lax1dude.eaglercraft.v1_8.sp.server.EaglerMinecraftServer;

> CHANGE  2 : 4  @  2 : 4

~ import net.minecraft.crash.CrashReport;
~ import net.minecraft.crash.CrashReportCategory;

> CHANGE  19 : 21  @  19 : 22

~ import net.minecraft.nbt.NBTTagCompound;
~ import net.minecraft.nbt.NBTTagString;

> DELETE  2  @  2 : 3

> INSERT  33 : 34  @  33

+ import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.network.play.server.S3FPacketCustomPayload;

> DELETE  2  @  2 : 3

> INSERT  16 : 18  @  16

+ import net.lax1dude.eaglercraft.v1_8.sp.server.socket.IntegratedServerPlayerNetworkManager;
+ 

> CHANGE  1 : 3  @  1 : 3

~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

> CHANGE  10 : 11  @  10 : 11

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

> CHANGE  51 : 65  @  51 : 63

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

> INSERT  20 : 25  @  20

+ 				if (this.serverController.worldServers[0].getWorldInfo().getGameRulesInstance()
+ 						.getBoolean("colorCodes")) {
+ 					s = net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.util.StringUtils
+ 							.translateControlCodesAlternate(s);
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

> CHANGE  19 : 26  @  19 : 21

~ 				String s = EnumChatFormatting.getTextWithoutFormattingCodes(aichatcomponent[i].getUnformattedText());
~ 				if (this.serverController.worldServers[0].getWorldInfo().getGameRulesInstance()
~ 						.getBoolean("colorCodes")) {
~ 					s = net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.util.StringUtils
~ 							.translateControlCodesAlternate(s);
~ 				}
~ 				tileentitysign.signText[i] = new ChatComponentText(s);

> DELETE  21  @  21 : 22

> DELETE  5  @  5 : 6

> DELETE  12  @  12 : 13

> DELETE  4  @  4 : 5

> CHANGE  1 : 2  @  1 : 3

~ 			PacketBuffer packetbuffer3 = c17packetcustompayload.getBufferData();

> CHANGE  2 : 3  @  2 : 3

~ 				ItemStack itemstack1 = packetbuffer3.readItemStackFromBuffer_server();

> INSERT  18 : 19  @  18

+ 				logger.error(exception3);

> DELETE  1  @  1 : 3

> CHANGE  4 : 5  @  4 : 6

~ 			PacketBuffer packetbuffer2 = c17packetcustompayload.getBufferData();

> CHANGE  2 : 3  @  2 : 3

~ 				ItemStack itemstack = packetbuffer2.readItemStackFromBuffer_server();

> INSERT  21 : 22  @  21

+ 				logger.error(exception4);

> DELETE  1  @  1 : 3

> DELETE  51  @  51 : 53

> INSERT  31 : 36  @  31

+ 					if (this.serverController.worldServers[0].getWorldInfo().getGameRulesInstance()
+ 							.getBoolean("colorCodes")) {
+ 						s = net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.util.StringUtils
+ 								.translateControlCodesAlternate(s);
+ 					}

> INSERT  5 : 22  @  5

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
+ 				for (EntityPlayerMP player : playerEntity.mcServer.getConfigurationManager().func_181057_v()) {
+ 					if (player != playerEntity) {
+ 						player.playerNetServerHandler.sendPacket(new S3FPacketCustomPayload("EAG|UpdateCert-1.8",
+ 								new PacketBuffer(Unpooled.buffer(cert, cert.length).writerIndex(cert.length))));
+ 					}
+ 				}
+ 			}

> DELETE  1  @  1 : 2

> EOF
