
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  3 : 7  @  3

+ 
+ import net.lax1dude.eaglercraft.v1_8.Keyboard;
+ import net.lax1dude.eaglercraft.v1_8.sp.SingleplayerServerController;
+ import net.lax1dude.eaglercraft.v1_8.sp.gui.GuiScreenIntegratedServerBusy;

> DELETE  6  @  6 : 7

> INSERT  5 : 6  @  5

+ 	private final boolean duplicate;

> INSERT  4 : 5  @  4

+ 		this.duplicate = false;

> INSERT  2 : 8  @  2

+ 	public GuiRenameWorld(GuiScreen parentScreenIn, String saveNameIn, boolean duplicate) {
+ 		this.parentScreen = parentScreenIn;
+ 		this.saveName = saveNameIn;
+ 		this.duplicate = duplicate;
+ 	}
+ 

> CHANGE  8 : 9  @  8 : 9

~ 				I18n.format(duplicate ? "selectWorld.duplicateButton" : "selectWorld.renameButton", new Object[0])));

> INSERT  5 : 8  @  5

+ 		if (duplicate) {
+ 			s += " copy";
+ 		}

> CHANGE  9 : 10  @  9 : 10

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> CHANGE  4 : 16  @  4 : 7

~ 				if (duplicate) {
~ 					SingleplayerServerController.duplicateWorld(this.saveName, this.field_146583_f.getText().trim());
~ 					this.mc.displayGuiScreen(
~ 							new GuiScreenIntegratedServerBusy(this.parentScreen, "singleplayer.busy.duplicating",
~ 									"singleplayer.failed.duplicating", () -> SingleplayerServerController.isReady()));
~ 				} else {
~ 					ISaveFormat isaveformat = this.mc.getSaveLoader();
~ 					isaveformat.renameWorld(this.saveName, this.field_146583_f.getText().trim());
~ 					this.mc.displayGuiScreen(
~ 							new GuiScreenIntegratedServerBusy(this.parentScreen, "singleplayer.busy.renaming",
~ 									"singleplayer.failed.renaming", () -> SingleplayerServerController.isReady()));
~ 				}

> DELETE  1  @  1 : 2

> CHANGE  3 : 4  @  3 : 4

~ 	protected void keyTyped(char parChar1, int parInt1) {

> CHANGE  8 : 9  @  8 : 9

~ 	protected void mouseClicked(int parInt1, int parInt2, int parInt3) {

> CHANGE  6 : 8  @  6 : 7

~ 		this.drawCenteredString(this.fontRendererObj,
~ 				I18n.format(duplicate ? "selectWorld.duplicate" : "selectWorld.renameTitle", new Object[0]),

> EOF
