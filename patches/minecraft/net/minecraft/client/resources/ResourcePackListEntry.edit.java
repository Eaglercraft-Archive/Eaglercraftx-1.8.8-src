
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  3 : 8  @  3

+ 
+ import net.lax1dude.eaglercraft.v1_8.Keyboard;
+ import net.lax1dude.eaglercraft.v1_8.vfs.SYS;
+ import net.lax1dude.eaglercraft.v1_8.internal.KeyboardConstants;
+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;

> DELETE  11  @  6 : 8

> INSERT  128 : 157  @  130

+ 	private void proceedWithBs(int l, boolean deleteInstead) {
+ 		if (!deleteInstead && l != 1) {
+ 			String s1 = I18n.format("resourcePack.incompatible.confirm.title", new Object[0]);
+ 			String s = I18n.format("resourcePack.incompatible.confirm." + (l > 1 ? "new" : "old"), new Object[0]);
+ 			this.mc.displayGuiScreen(new GuiYesNo(new GuiYesNoCallback() {
+ 				public void confirmClicked(boolean flag, int var2) {
+ 					List list2 = ResourcePackListEntry.this.resourcePacksGUI
+ 							.getListContaining(ResourcePackListEntry.this);
+ 					ResourcePackListEntry.this.mc.displayGuiScreen(ResourcePackListEntry.this.resourcePacksGUI);
+ 					if (flag) {
+ 						list2.remove(ResourcePackListEntry.this);
+ 						ResourcePackListEntry.this.resourcePacksGUI.getSelectedResourcePacks().add(0,
+ 								ResourcePackListEntry.this);
+ 					}
+ 
+ 				}
+ 			}, s1, s, 0).withOpaqueBackground());
+ 		} else {
+ 			this.mc.displayGuiScreen(this.resourcePacksGUI);
+ 			this.resourcePacksGUI.getListContaining(this).remove(this);
+ 			if (deleteInstead) {
+ 				this.mc.loadingScreen.eaglerShow(I18n.format("resourcePack.load.deleting"), this.func_148312_b());
+ 				SYS.deleteResourcePack(this.func_148312_b());
+ 			} else {
+ 				this.resourcePacksGUI.getSelectedResourcePacks().add(0, this);
+ 			}
+ 		}
+ 	}
+ 

> CHANGE  34 : 38  @  5 : 9

~ 				if (Keyboard.isKeyDown(KeyboardConstants.KEY_LSHIFT)
~ 						|| Keyboard.isKeyDown(KeyboardConstants.KEY_RSHIFT)) {
~ 					proceedWithBs(l, false);
~ 				} else {

> CHANGE  6 : 7  @  6 : 14

~ 							proceedWithBs(l, flag);

> CHANGE  3 : 7  @  10 : 14

~ 					}, I18n.format("resourcePack.prompt.title", this.func_148312_b()),
~ 							I18n.format("resourcePack.prompt.text", new Object[0]),
~ 							I18n.format("resourcePack.prompt.delete", new Object[0]),
~ 							I18n.format("resourcePack.prompt.add", new Object[0]), 0).withOpaqueBackground());

> DELETE  5  @  5 : 6

> EOF
