
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> CHANGE  1 : 5  @  1 : 3

~ 
~ import net.lax1dude.eaglercraft.v1_8.sp.SingleplayerServerController;
~ import net.lax1dude.eaglercraft.v1_8.sp.gui.GuiScreenIntegratedServerBusy;
~ import net.lax1dude.eaglercraft.v1_8.sp.ipc.IPCPacket15Crashed;

> INSERT  1 : 2  @  1

+ import net.minecraft.util.ChatComponentTranslation;

> CHANGE  15 : 16  @  15 : 16

~ 	protected void keyTyped(char parChar1, int parInt1) {

> CHANGE  12 : 13  @  12 : 13

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> CHANGE  12 : 15  @  12 : 14

~ 			for (int l = 0, m = this.multilineMessage.size(); l < m; ++l) {
~ 				this.drawCenteredString(this.fontRendererObj, this.multilineMessage.get(l), this.width / 2, k,
~ 						16777215);

> INSERT  6 : 18  @  6

+ 
+ 	public void updateScreen() {
+ 		IPCPacket15Crashed[] pkt = SingleplayerServerController.worldStatusErrors();
+ 		if (pkt != null && pkt.length > 0) {
+ 			mc.displayGuiScreen(
+ 					GuiScreenIntegratedServerBusy.createException(this, "singleplayer.failed.serverCrash", pkt));
+ 		}
+ 	}
+ 
+ 	public static GuiScreen createRateLimitKick(GuiScreen prev) {
+ 		return new GuiDisconnected(prev, "connect.failed", new ChatComponentTranslation("disconnect.tooManyRequests"));
+ 	}

> EOF
