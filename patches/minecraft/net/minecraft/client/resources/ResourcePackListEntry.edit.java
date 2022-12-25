
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

> DELETE  14  @  9 : 11

> INSERT  142 : 171  @  139

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

> CHANGE  176 : 180  @  144 : 148

~ 				if (Keyboard.isKeyDown(KeyboardConstants.KEY_LSHIFT)
~ 						|| Keyboard.isKeyDown(KeyboardConstants.KEY_RSHIFT)) {
~ 					proceedWithBs(l, false);
~ 				} else {

> CHANGE  182 : 183  @  150 : 158

~ 							proceedWithBs(l, flag);

> CHANGE  185 : 189  @  160 : 164

~ 					}, I18n.format("resourcePack.prompt.title", this.func_148312_b()),
~ 							I18n.format("resourcePack.prompt.text", new Object[0]),
~ 							I18n.format("resourcePack.prompt.delete", new Object[0]),
~ 							I18n.format("resourcePack.prompt.add", new Object[0]), 0).withOpaqueBackground());

> DELETE  190  @  165 : 166

> EOF
