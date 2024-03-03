
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> DELETE  1  @  1 : 2

> CHANGE  3 : 12  @  3 : 8

~ 
~ import com.google.common.collect.Lists;
~ 
~ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
~ import net.lax1dude.eaglercraft.v1_8.internal.FileChooserResult;
~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
~ import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerFolderResourcePack;
~ import net.lax1dude.eaglercraft.v1_8.minecraft.GuiScreenGenericErrorMessage;

> DELETE  5  @  5 : 9

> CHANGE  24 : 25  @  24 : 25

~ 			List arraylist = Lists.newArrayList(resourcepackrepository.getRepositoryEntriesAll());

> CHANGE  2 : 5  @  2 : 4

~ 			for (int i = 0, l = arraylist.size(); i < l; ++i) {
~ 				this.availableResourcePacks
~ 						.add(new ResourcePackListEntryFound(this, (ResourcePackRepository.Entry) arraylist.get(i)));

> CHANGE  2 : 6  @  2 : 5

~ 			arraylist = Lists.reverse(resourcepackrepository.getRepositoryEntries());
~ 			for (int i = 0, l = arraylist.size(); i < l; ++i) {
~ 				this.selectedResourcePacks
~ 						.add(new ResourcePackListEntryFound(this, (ResourcePackRepository.Entry) arraylist.get(i)));

> CHANGE  38 : 39  @  38 : 39

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> CHANGE  2 : 3  @  2 : 39

~ 				EagRuntime.displayFileChooser("application/zip", "zip");

> CHANGE  2 : 3  @  2 : 3

~ 					ArrayList<ResourcePackRepository.Entry> arraylist = Lists.newArrayList();

> CHANGE  1 : 3  @  1 : 2

~ 					for (int i = 0, l = this.selectedResourcePacks.size(); i < l; ++i) {
~ 						ResourcePackListEntry resourcepacklistentry = this.selectedResourcePacks.get(i);

> CHANGE  10 : 12  @  10 : 11

~ 					for (int i = 0, l = arraylist.size(); i < l; ++i) {
~ 						ResourcePackRepository.Entry resourcepackrepository$entry = arraylist.get(i);

> INSERT  6 : 8  @  6

+ 					this.mc.loadingScreen.eaglerShow(I18n.format("resourcePack.load.refreshing"),
+ 							I18n.format("resourcePack.load.pleaseWait"));

> DELETE  3  @  3 : 4

> CHANGE  6 : 57  @  6 : 7

~ 	public void updateScreen() {
~ 		FileChooserResult packFile = null;
~ 		if (EagRuntime.fileChooserHasResult()) {
~ 			packFile = EagRuntime.getFileChooserResult();
~ 		}
~ 		if (packFile == null) {
~ 			return;
~ 		}
~ 		mc.loadingScreen.eaglerShow(I18n.format("resourcePack.load.loading"), packFile.fileName);
~ 		try {
~ 			EaglerFolderResourcePack.importResourcePack(packFile.fileName, EaglerFolderResourcePack.RESOURCE_PACKS,
~ 					packFile.fileData);
~ 		} catch (IOException e) {
~ 			logger.error("Could not load resource pack: {}", packFile.fileName);
~ 			logger.error(e);
~ 			mc.displayGuiScreen(new GuiScreenGenericErrorMessage("resourcePack.importFailed.1",
~ 					"resourcePack.importFailed.2", parentScreen));
~ 			return;
~ 		}
~ 
~ 		ArrayList<ResourcePackRepository.Entry> arraylist = Lists.newArrayList();
~ 
~ 		for (int i = 0, l = this.selectedResourcePacks.size(); i < l; ++i) {
~ 			ResourcePackListEntry resourcepacklistentry = this.selectedResourcePacks.get(i);
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
~ 		for (int i = 0, l = arraylist.size(); i < l; ++i) {
~ 			ResourcePackRepository.Entry resourcepackrepository$entry = arraylist.get(i);
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
