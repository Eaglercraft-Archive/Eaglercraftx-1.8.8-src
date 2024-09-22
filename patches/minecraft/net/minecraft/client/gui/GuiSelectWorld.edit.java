
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  5 : 6  @  5

+ import java.util.ArrayList;

> CHANGE  2 : 8  @  2 : 3

~ 
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
~ import net.lax1dude.eaglercraft.v1_8.sp.gui.GuiScreenLANConnect;
~ import net.lax1dude.eaglercraft.v1_8.sp.gui.GuiScreenLANNotSupported;
~ import net.lax1dude.eaglercraft.v1_8.sp.ipc.IPCPacket1CIssueDetected;
~ import net.lax1dude.eaglercraft.v1_8.sp.lan.LANServerController;

> CHANGE  1 : 2  @  1 : 9

~ import net.minecraft.client.audio.PositionedSoundRecord;

> INSERT  2 : 3  @  2

+ import net.minecraft.util.ResourceLocation;

> DELETE  2  @  2 : 3

> DELETE  1  @  1 : 2

> DELETE  1  @  1 : 3

> INSERT  1 : 11  @  1

+ import net.lax1dude.eaglercraft.v1_8.Mouse;
+ import net.lax1dude.eaglercraft.v1_8.internal.EnumCursorType;
+ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
+ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
+ import net.lax1dude.eaglercraft.v1_8.sp.SingleplayerServerController;
+ import net.lax1dude.eaglercraft.v1_8.sp.gui.GuiScreenBackupWorldSelection;
+ import net.lax1dude.eaglercraft.v1_8.sp.gui.GuiScreenCreateWorldSelection;
+ import net.lax1dude.eaglercraft.v1_8.sp.gui.GuiScreenIntegratedServerBusy;
+ import net.lax1dude.eaglercraft.v1_8.sp.gui.GuiScreenLANInfo;
+ 

> INSERT  17 : 20  @  17

+ 	private boolean hasRequestedWorlds = false;
+ 	private boolean waitingForWorlds = false;
+ 	private boolean ramdiskMode = false;

> INSERT  3 : 4  @  3

+ 		this.field_146639_s = new ArrayList();

> INSERT  3 : 4  @  3

+ 		this.ramdiskMode = SingleplayerServerController.isIssueDetected(IPCPacket1CIssueDetected.ISSUE_RAMDISK_MODE);

> DELETE  1  @  1 : 10

> CHANGE  8 : 9  @  8 : 9

~ 		this.field_146638_t = new GuiSelectWorld.List(this.mc, ramdiskMode ? -10 : 0);

> INSERT  4 : 21  @  4

+ 	public void updateScreen() {
+ 		if (!hasRequestedWorlds && SingleplayerServerController.isReady()) {
+ 			hasRequestedWorlds = true;
+ 			waitingForWorlds = true;
+ 			this.mc.getSaveLoader().flushCache();
+ 			this.mc.displayGuiScreen(new GuiScreenIntegratedServerBusy(this, "singleplayer.busy.listingworlds",
+ 					"singleplayer.failed.listingworlds", SingleplayerServerController::isReady, (t, u) -> {
+ 						GuiScreenIntegratedServerBusy tt = (GuiScreenIntegratedServerBusy) t;
+ 						Minecraft.getMinecraft().displayGuiScreen(
+ 								GuiScreenIntegratedServerBusy.createException(parentScreen, tt.failMessage, u));
+ 					}));
+ 		} else if (waitingForWorlds && SingleplayerServerController.isReady()) {
+ 			waitingForWorlds = false;
+ 			this.func_146627_h();
+ 		}
+ 	}
+ 

> CHANGE  5 : 11  @  5 : 6

~ 	public void handleTouchInput() throws IOException {
~ 		super.handleTouchInput();
~ 		this.field_146638_t.handleTouchInput();
~ 	}
~ 
~ 	private void func_146627_h() {

> CHANGE  29 : 30  @  29 : 30

~ 				I18n.format("selectWorld.backup", new Object[0])));

> CHANGE  8 : 9  @  8 : 9

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> CHANGE  11 : 13  @  11 : 12

~ 				hasRequestedWorlds = false; // force refresh
~ 				this.mc.displayGuiScreen(new GuiScreenCreateWorldSelection(this));

> INSERT  1 : 2  @  1

+ 				hasRequestedWorlds = false; // force refresh

> CHANGE  4 : 8  @  4 : 11

~ 				hasRequestedWorlds = false; // force refresh
~ 				this.mc.displayGuiScreen(
~ 						new GuiScreenBackupWorldSelection(this, this.func_146621_a(this.field_146640_r),
~ 								((SaveFormatComparator) field_146639_s.get(this.field_146640_r)).levelDat));

> INSERT  32 : 33  @  32

+ 				hasRequestedWorlds = false; // force refresh

> DELETE  1  @  1 : 2

> CHANGE  1 : 5  @  1 : 7

~ 				this.mc.displayGuiScreen(new GuiScreenIntegratedServerBusy(this, "singleplayer.busy.deleting",
~ 						"singleplayer.failed.deleting", SingleplayerServerController::isReady));
~ 			} else {
~ 				this.mc.displayGuiScreen(this);

> DELETE  1  @  1 : 3

> INSERT  7 : 27  @  7

+ 
+ 		if (ramdiskMode) {
+ 			this.drawCenteredString(this.fontRendererObj, I18n.format("selectWorld.ramdiskWarning"), this.width / 2,
+ 					height - 68, 11184810);
+ 		}
+ 
+ 		GlStateManager.pushMatrix();
+ 		GlStateManager.scale(0.75f, 0.75f, 0.75f);
+ 
+ 		String text = I18n.format("directConnect.lanWorld");
+ 		int w = mc.fontRendererObj.getStringWidth(text);
+ 		boolean hover = i > 1 && j > 1 && i < (w * 3 / 4) + 7 && j < 12;
+ 		if (hover) {
+ 			Mouse.showCursor(EnumCursorType.HAND);
+ 		}
+ 
+ 		drawString(mc.fontRendererObj, EnumChatFormatting.UNDERLINE + text, 5, 5, hover ? 0xFFEEEE22 : 0xFFCCCCCC);
+ 
+ 		GlStateManager.popMatrix();
+ 

> INSERT  3 : 19  @  3

+ 	@Override
+ 	public void mouseClicked(int xx, int yy, int btn) {
+ 		String text = I18n.format("directConnect.lanWorld");
+ 		int w = mc.fontRendererObj.getStringWidth(text);
+ 		if (xx > 2 && yy > 2 && xx < (w * 3 / 4) + 5 && yy < 12) {
+ 			if (LANServerController.supported()) {
+ 				mc.displayGuiScreen(GuiScreenLANInfo.showLANInfoScreen(new GuiScreenLANConnect(this)));
+ 			} else {
+ 				mc.displayGuiScreen(new GuiScreenLANNotSupported(this));
+ 			}
+ 			mc.getSoundHandler()
+ 					.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
+ 		}
+ 		super.mouseClicked(xx, yy, btn);
+ 	}
+ 

> CHANGE  10 : 13  @  10 : 12

~ 		public List(Minecraft mcIn, int i) {
~ 			super(mcIn, GuiSelectWorld.this.width, GuiSelectWorld.this.height, 32, GuiSelectWorld.this.height - 64 + i,
~ 					36);

> EOF
