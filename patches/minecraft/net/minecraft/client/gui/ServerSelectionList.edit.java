
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> INSERT  1 : 7  @  1

+ 
+ import com.google.common.collect.Lists;
+ 
+ import net.lax1dude.eaglercraft.v1_8.sp.lan.LANServerController;
+ import net.lax1dude.eaglercraft.v1_8.sp.lan.LANServerList;
+ import net.lax1dude.eaglercraft.v1_8.sp.relay.RelayManager;

> DELETE  1  @  1 : 6

> CHANGE  1 : 2  @  1 : 2

~ import net.minecraft.client.resources.I18n;

> DELETE  4  @  4 : 6

> INSERT  2 : 4  @  2

+ 	private final ServerListEntryNormal serverListEntryLAN;
+ 

> INSERT  4 : 18  @  4

+ 		this.serverListEntryLAN = new ServerListEntryNormal(owner, null) {
+ 			@Override
+ 			public void drawEntry(int i, int j, int k, int l, int var5, int i1, int j1, boolean flag) {
+ 				//
+ 			}
+ 
+ 			@Override
+ 			public boolean mousePressed(int i, int var2, int var3, int var4, int j, int k) {
+ 				if (ServerSelectionList.this.selectedSlotIndex != i) {
+ 					super.field_148298_f = 0;
+ 				}
+ 				return super.mousePressed(i, var2, var3, var4, Math.max(j, 32), k);
+ 			}
+ 		};

> CHANGE  3 : 4  @  3 : 4

~ 		if (i < getOrigSize()) {

> DELETE  1  @  1 : 9

> INSERT  1 : 2  @  1

+ 		return serverListEntryLAN;

> INSERT  2 : 6  @  2

+ 	protected int getOrigSize() {
+ 		return this.field_148198_l.size();
+ 	}
+ 

> CHANGE  1 : 2  @  1 : 2

~ 		return this.field_148198_l.size() + GuiMultiplayer.getLanServerList().countServers();

> DELETE  23  @  23 : 32

> INSERT  7 : 69  @  7

+ 
+ 	@Override
+ 	protected void drawSelectionBox(int mouseXIn, int mouseYIn, int parInt3, int parInt4, int i) {
+ 		super.drawSelectionBox(mouseXIn, mouseYIn, parInt3, parInt4, i + 1);
+ 	}
+ 
+ 	@Override
+ 	protected void drawSlot(int entryID, int mouseXIn, int mouseYIn, int parInt4, int parInt5, int parInt6) {
+ 		if (entryID < getOrigSize()) {
+ 			super.drawSlot(entryID, mouseXIn, mouseYIn, parInt4, parInt5, parInt6);
+ 		} else if (entryID < getSize()) {
+ 			this.func_77248_b(entryID, mouseXIn, mouseYIn, parInt4);
+ 		} else {
+ 			this.func_77249_c(entryID, mouseXIn, mouseYIn, parInt4);
+ 		}
+ 	}
+ 
+ 	private void func_77248_b(int par1, int par2, int par3, int par4) {
+ 		LANServerList.LanServer var6 = GuiMultiplayer.getLanServerList().getServer(par1 - getOrigSize());
+ 		this.owner.drawString(this.owner.fontRendererObj, I18n.format("lanServer.title"), par2 + 2, par3 + 1, 16777215);
+ 		this.owner.drawString(this.owner.fontRendererObj, var6.getLanServerMotd(), par2 + 2, par3 + 12, 8421504);
+ 
+ 		if (this.owner.mc.gameSettings.hideServerAddress) {
+ 			this.owner.drawString(this.owner.fontRendererObj, I18n.format("selectServer.hiddenAddress"), par2 + 2,
+ 					par3 + 12 + 11, 3158064);
+ 		} else {
+ 			this.owner.drawString(this.owner.fontRendererObj, var6.getLanServerCode(), par2 + 2, par3 + 12 + 11,
+ 					0x558822);
+ 		}
+ 	}
+ 
+ 	private void func_77249_c(int par1, int par2, int par3, int par4) {
+ 		if (!LANServerController.supported())
+ 			return;
+ 		if (RelayManager.relayManager.count() == 0) {
+ 			this.owner.drawCenteredString(this.owner.fontRendererObj, I18n.format("noRelay.noRelay1"),
+ 					this.owner.width / 2, par3 + 6, 16777215);
+ 			this.owner.drawCenteredString(this.owner.fontRendererObj, I18n.format("noRelay.noRelay2"),
+ 					this.owner.width / 2, par3 + 18, 0xFFAAAAAA);
+ 		} else {
+ 			this.owner.drawCenteredString(this.owner.fontRendererObj, I18n.format("lanServer.scanning"),
+ 					this.owner.width / 2, par3 + 6, 16777215);
+ 			String var6;
+ 
+ 			switch (this.owner.ticksOpened / 3 % 4) {
+ 			case 0:
+ 			default:
+ 				var6 = "O o o";
+ 				break;
+ 
+ 			case 1:
+ 			case 3:
+ 				var6 = "o O o";
+ 				break;
+ 
+ 			case 2:
+ 				var6 = "o o O";
+ 			}
+ 
+ 			this.owner.drawCenteredString(this.owner.fontRendererObj, var6, this.owner.width / 2, par3 + 18, 8421504);
+ 		}
+ 	}

> EOF
