
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 4  @  2

+ import java.io.IOException;
+ 

> CHANGE  2 : 17  @  2 : 15

~ 
~ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
~ import net.lax1dude.eaglercraft.v1_8.Keyboard;
~ import net.lax1dude.eaglercraft.v1_8.Mouse;
~ import net.lax1dude.eaglercraft.v1_8.cookie.ServerCookieDataStore;
~ import net.lax1dude.eaglercraft.v1_8.internal.EnumCursorType;
~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
~ import net.lax1dude.eaglercraft.v1_8.sp.gui.GuiNetworkSettingsButton;
~ import net.lax1dude.eaglercraft.v1_8.sp.gui.GuiScreenConnectOption;
~ import net.lax1dude.eaglercraft.v1_8.sp.gui.GuiScreenLANConnecting;
~ import net.lax1dude.eaglercraft.v1_8.sp.lan.LANServerList;
~ import net.lax1dude.eaglercraft.v1_8.sp.relay.RelayServer;
~ import net.minecraft.client.audio.PositionedSoundRecord;

> DELETE  3  @  3 : 5

> CHANGE  1 : 3  @  1 : 4

~ import net.minecraft.util.EnumChatFormatting;
~ import net.minecraft.util.ResourceLocation;

> DELETE  3  @  3 : 4

> INSERT  11 : 16  @  11

+ 
+ 	public ServerData getSelectedServer() {
+ 		return selectedServer;
+ 	}
+ 

> DELETE  1  @  1 : 3

> INSERT  1 : 2  @  1

+ 	private static long lastRefreshCommit = 0l;

> INSERT  1 : 7  @  1

+ 	private static LANServerList lanServerList = null;
+ 
+ 	public int ticksOpened;
+ 
+ 	private final GuiNetworkSettingsButton relaysButton;
+ 

> INSERT  2 : 6  @  2

+ 		this.relaysButton = new GuiNetworkSettingsButton(this);
+ 		if (lanServerList != null) {
+ 			lanServerList.forceRefresh();
+ 		}

> CHANGE  7 : 8  @  7 : 8

~ 			this.savedServerList = ServerList.getServerList();

> DELETE  1  @  1 : 10

> INSERT  3 : 7  @  3

+ 			if (lanServerList == null) {
+ 				lanServerList = new LANServerList();
+ 			}
+ 			lanServerList.forceRefresh();

> INSERT  12 : 17  @  12

+ 	public void handleTouchInput() throws IOException {
+ 		super.handleTouchInput();
+ 		this.serverListSelector.handleTouchInput();
+ 	}
+ 

> CHANGE  20 : 23  @  20 : 24

~ 		this.savedServerList.updateServerPing();
~ 		if (lanServerList.update()) {
~ 			this.selectServer(-1);

> CHANGE  1 : 2  @  1 : 3

~ 		++ticksOpened;

> DELETE  4  @  4 : 10

> CHANGE  2 : 3  @  2 : 3

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> CHANGE  19 : 21  @  19 : 22

~ 				this.selectedServer = new ServerData(I18n.format("selectServer.defaultName", new Object[0]), "", false);
~ 				this.mc.displayGuiScreen(new GuiScreenConnectOption(this));

> CHANGE  8 : 13  @  8 : 11

~ 				if (serverdata != null) {
~ 					this.selectedServer = new ServerData(serverdata.serverName, serverdata.serverIP, false);
~ 					this.selectedServer.copyFrom(serverdata);
~ 					this.mc.displayGuiScreen(new GuiScreenAddServer(this, this.selectedServer));
~ 				}

> CHANGE  3 : 8  @  3 : 4

~ 				long millis = EagRuntime.steadyTimeMillis();
~ 				if (millis - lastRefreshCommit > 700l) {
~ 					lastRefreshCommit = millis;
~ 					this.refreshServerList();
~ 				}

> CHANGE  5 : 6  @  5 : 6

~ 	public void refreshServerList() {

> CHANGE  14 : 19  @  14 : 16

~ 			long millis = EagRuntime.steadyTimeMillis();
~ 			if (millis - lastRefreshCommit > 700l) {
~ 				lastRefreshCommit = millis;
~ 				this.refreshServerList();
~ 			}

> INSERT  10 : 13  @  10

+ 				if (!this.selectedServer.enableCookies) {
+ 					ServerCookieDataStore.clearCookie(this.selectedServer.serverIP);
+ 				}

> CHANGE  5 : 10  @  5 : 7

~ 			long millis = EagRuntime.steadyTimeMillis();
~ 			if (millis - lastRefreshCommit > 700l) {
~ 				lastRefreshCommit = millis;
~ 				this.refreshServerList();
~ 			}

> INSERT  6 : 9  @  6

+ 				if (serverdata.enableCookies && !this.selectedServer.enableCookies) {
+ 					ServerCookieDataStore.clearCookie(this.selectedServer.serverIP);
+ 				}

> CHANGE  4 : 9  @  4 : 6

~ 			long millis = EagRuntime.steadyTimeMillis();
~ 			if (millis - lastRefreshCommit > 700l) {
~ 				lastRefreshCommit = millis;
~ 				this.refreshServerList();
~ 			}

> INSERT  1 : 2  @  1

+ 	}

> INSERT  1 : 3  @  1

+ 	public void cancelDirectConnect() {
+ 		this.directConnect = false;

> CHANGE  2 : 3  @  2 : 3

~ 	protected void keyTyped(char parChar1, int parInt1) {

> DELETE  18  @  18 : 27

> CHANGE  11 : 12  @  11 : 12

~ 					} else if (i < this.serverListSelector.getSize() - 1) {

> DELETE  2  @  2 : 11

> INSERT  22 : 24  @  22

+ 		relaysButton.drawScreen(i, j);
+ 		drawPluginDownloadLink(i, j);

> INSERT  2 : 3  @  2

+ 			GlStateManager.disableLighting();

> INSERT  1 : 2  @  1

+ 	}

> INSERT  1 : 17  @  1

+ 	private void drawPluginDownloadLink(int xx, int yy) {
+ 		GlStateManager.pushMatrix();
+ 		GlStateManager.scale(0.75f, 0.75f, 0.75f);
+ 		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
+ 
+ 		String text = "Download EaglerXServer";
+ 		int w = mc.fontRendererObj.getStringWidth(text);
+ 		boolean hover = xx > width - 5 - (w + 5) * 3 / 4 && yy > 1 && xx < width - 2 && yy < 12;
+ 		if (hover) {
+ 			Mouse.showCursor(EnumCursorType.HAND);
+ 		}
+ 
+ 		drawString(mc.fontRendererObj, EnumChatFormatting.UNDERLINE + text, (width - 1) * 4 / 3 - w - 5, 5,
+ 				hover ? 0xFFEEEE22 : 0xFFCCCCCC);
+ 
+ 		GlStateManager.popMatrix();

> CHANGE  3 : 12  @  3 : 13

~ 		if (this.serverListSelector.func_148193_k() < this.serverListSelector.getOrigSize()) {
~ 			GuiListExtended.IGuiListEntry guilistextended$iguilistentry = this.serverListSelector.func_148193_k() < 0
~ 					? null
~ 					: this.serverListSelector.getListEntry(this.serverListSelector.func_148193_k());
~ 			if (guilistextended$iguilistentry instanceof ServerListEntryNormal) {
~ 				this.connectToServer(((ServerListEntryNormal) guilistextended$iguilistentry).getServerData());
~ 			}
~ 		} else {
~ 			int par1 = this.serverListSelector.func_148193_k() - this.serverListSelector.getOrigSize();

> INSERT  1 : 7  @  1

+ 			if (par1 < lanServerList.countServers()) {
+ 				LANServerList.LanServer var2 = lanServerList.getServer(par1);
+ 				connectToLAN("Connecting to '" + var2.getLanServerMotd() + "'...", var2.getLanServerCode(),
+ 						var2.getLanServerRelay());
+ 			}
+ 		}

> INSERT  6 : 11  @  6

+ 	private void connectToLAN(String text, String code, RelayServer uri) {
+ 		this.mc.loadingScreen.resetProgressAndMessage(text);
+ 		this.mc.displayGuiScreen(new GuiScreenLANConnecting(this, code, uri));
+ 	}
+ 

> CHANGE  7 : 8  @  7 : 9

~ 		if (guilistextended$iguilistentry != null) {

> CHANGE  1 : 3  @  1 : 2

~ 			if (guilistextended$iguilistentry instanceof ServerListEntryNormal
~ 					&& ((ServerListEntryNormal) guilistextended$iguilistentry).getServerData() != null) {

> DELETE  4  @  4 : 5

> DELETE  2  @  2 : 6

> CHANGE  4 : 6  @  4 : 5

~ 	protected void mouseClicked(int parInt1, int parInt2, int parInt3) {
~ 		relaysButton.mouseClicked(parInt1, parInt2, parInt3);

> INSERT  2 : 9  @  2

+ 		String text = "Download EaglerXServer";
+ 		int w = mc.fontRendererObj.getStringWidth(text);
+ 		if (parInt1 > width - 5 - (w + 5) * 3 / 4 && parInt2 > 1 && parInt1 < width - 2 && parInt2 < 12) {
+ 			this.mc.getSoundHandler()
+ 					.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
+ 			EagRuntime.openLink("https://lax1dude.net/eaglerxserver");
+ 		}

> INSERT  11 : 15  @  11

+ 	static LANServerList getLanServerList() {
+ 		return lanServerList;
+ 	}
+ 

> EOF
