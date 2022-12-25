
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 3  @  2 : 4

~ import java.io.ByteArrayInputStream;

> DELETE  4  @  5 : 6

> CHANGE  7 : 15  @  9 : 14

~ 
~ import com.google.common.collect.Lists;
~ 
~ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
~ import net.lax1dude.eaglercraft.v1_8.vfs.SYS;
~ import net.lax1dude.eaglercraft.v1_8.internal.FileChooserResult;
~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

> DELETE  20  @  19 : 23

> CHANGE  35 : 37  @  38 : 39

~ 		GuiButton btn;
~ 		this.buttonList.add(btn = new GuiOptionButton(2, this.width / 2 - 154, this.height - 48,

> INSERT  38 : 39  @  40

+ 		btn.enabled = SYS.VFS != null;

> CHANGE  49 : 50  @  50 : 51

~ 			for (ResourcePackRepository.Entry resourcepackrepository$entry : (List<ResourcePackRepository.Entry>) arraylist) {

> CHANGE  94 : 95  @  95 : 96

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> CHANGE  97 : 100  @  98 : 135

~ 				if (SYS.VFS == null)
~ 					return;
~ 				EagRuntime.displayFileChooser("application/zip", "zip");

> CHANGE  115 : 116  @  150 : 151

~ 					for (ResourcePackRepository.Entry resourcepackrepository$entry : (List<ResourcePackRepository.Entry>) arraylist) {

> INSERT  122 : 124  @  157

+ 					this.mc.loadingScreen.eaglerShow(I18n.format("resourcePack.load.refreshing"),
+ 							I18n.format("resourcePack.load.pleaseWait"));

> DELETE  127  @  160 : 161

> CHANGE  133 : 173  @  167 : 168

~ 	public void updateScreen() {
~ 		FileChooserResult packFile = null;
~ 		if (EagRuntime.fileChooserHasResult()) {
~ 			packFile = EagRuntime.getFileChooserResult();
~ 		}
~ 		if (packFile == null)
~ 			return;
~ 		logger.info("Loading resource pack: {}", packFile.fileName);
~ 		mc.loadingScreen.eaglerShow(I18n.format("resourcePack.load.loading"), packFile.fileName);
~ 		SYS.loadResourcePack(packFile.fileName, new ByteArrayInputStream(packFile.fileData), null);
~ 
~ 		ArrayList arraylist = Lists.newArrayList();
~ 
~ 		for (ResourcePackListEntry resourcepacklistentry : this.selectedResourcePacks) {
~ 			if (resourcepacklistentry instanceof ResourcePackListEntryFound) {
~ 				arraylist.add(((ResourcePackListEntryFound) resourcepacklistentry).func_148318_i());
~ 			}
~ 		}
~ 
~ 		Collections.reverse(arraylist);
~ 		this.mc.getResourcePackRepository().setRepositories(arraylist);
~ 		this.mc.gameSettings.resourcePacks.clear();
~ 		this.mc.gameSettings.field_183018_l.clear();
~ 
~ 		for (ResourcePackRepository.Entry resourcepackrepository$entry : (List<ResourcePackRepository.Entry>) arraylist) {
~ 			this.mc.gameSettings.resourcePacks.add(resourcepackrepository$entry.getResourcePackName());
~ 			if (resourcepackrepository$entry.func_183027_f() != 1) {
~ 				this.mc.gameSettings.field_183018_l.add(resourcepackrepository$entry.getResourcePackName());
~ 			}
~ 		}
~ 
~ 		this.mc.gameSettings.saveOptions();
~ 
~ 		boolean wasChanged = this.changed;
~ 		this.changed = false;
~ 		this.initGui();
~ 		this.changed = wasChanged;
~ 	}
~ 
~ 	protected void mouseClicked(int parInt1, int parInt2, int parInt3) {

> EOF
