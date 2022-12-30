
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 11

> DELETE  1  @  10 : 12

> DELETE  1  @  3 : 6

> CHANGE  2 : 14  @  5 : 7

~ import java.util.function.Consumer;
~ 
~ import com.google.common.collect.ImmutableList;
~ import com.google.common.collect.Lists;
~ 
~ import net.lax1dude.eaglercraft.v1_8.IOUtils;
~ import net.lax1dude.eaglercraft.v1_8.vfs.FolderResourcePack;
~ import net.lax1dude.eaglercraft.v1_8.vfs.SYS;
~ import net.lax1dude.eaglercraft.v1_8.futures.ListenableFuture;
~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
~ import net.lax1dude.eaglercraft.v1_8.opengl.ImageData;

> DELETE  13  @  3 : 4

> DELETE  2  @  3 : 6

> DELETE  4  @  7 : 8

> DELETE  1  @  2 : 9

> DELETE  3  @  10 : 18

> DELETE  1  @  9 : 10

> DELETE  2  @  3 : 4

> CHANGE  4 : 5  @  5 : 7

~ 	public ResourcePackRepository(IResourcePack rprDefaultResourcePackIn, IMetadataSerializer rprMetadataSerializerIn,

> DELETE  2  @  3 : 5

> DELETE  2  @  4 : 5

> DELETE  23  @  24 : 42

> CHANGE  1 : 3  @  19 : 20

~ 		if (SYS.VFS == null)
~ 			return;

> CHANGE  3 : 6  @  2 : 3

~ 		List<ResourcePackRepository.Entry> list = Lists.<ResourcePackRepository.Entry>newArrayList();
~ 
~ 		for (String file1 : SYS.getResourcePackNames()) {

> INSERT  4 : 5  @  2

+ 

> CHANGE  4 : 5  @  3 : 4

~ 					list.add(resourcepackrepository$entry);

> CHANGE  2 : 6  @  2 : 3

~ 					logger.error("Failed to call \"updateResourcePack\" for resource pack \"{}\"",
~ 							resourcepackrepository$entry.resourcePackFile);
~ 					logger.error(var6);
~ 					list.remove(resourcepackrepository$entry);

> INSERT  7 : 8  @  4

+ 

> CHANGE  2 : 3  @  1 : 2

~ 					list.add(this.repositoryEntriesAll.get(i));

> CHANGE  5 : 6  @  5 : 6

~ 		this.repositoryEntriesAll.removeAll(list);

> CHANGE  6 : 7  @  6 : 7

~ 		this.repositoryEntriesAll = list;

> CHANGE  16 : 23  @  16 : 48

~ 	public void downloadResourcePack(String s1, String s2, Consumer<Boolean> cb) {
~ 		SYS.loadRemoteResourcePack(s1, s2, res -> {
~ 			if (res != null) {
~ 				ResourcePackRepository.this.resourcePackInstance = new FolderResourcePack(res, "srp/");
~ 				Minecraft.getMinecraft().scheduleResourcesRefresh();
~ 				cb.accept(true);
~ 				return;

> CHANGE  8 : 15  @  33 : 61

~ 			cb.accept(false);
~ 		}, runnable -> {
~ 			Minecraft.getMinecraft().addScheduledTask(runnable);
~ 		}, () -> {
~ 			Minecraft.getMinecraft().loadingScreen.eaglerShow(I18n.format("resourcePack.load.loading"),
~ 					"Server resource pack");
~ 		});

> DELETE  9  @  30 : 50

> CHANGE  5 : 8  @  25 : 39

~ 		if (this.resourcePackInstance != null) {
~ 			this.resourcePackInstance = null;
~ 			Minecraft.getMinecraft().scheduleResourcesRefresh();

> DELETE  4  @  15 : 16

> CHANGE  3 : 4  @  4 : 5

~ 		private final String resourcePackFile;

> CHANGE  3 : 4  @  3 : 4

~ 		private ImageData texturePackIcon;

> INSERT  2 : 3  @  2

+ 		private TextureManager iconTextureManager;

> CHANGE  2 : 3  @  1 : 2

~ 		private Entry(String resourcePackFileIn) {

> CHANGE  5 : 8  @  5 : 8

~ 			if (SYS.VFS == null)
~ 				return;
~ 			this.reResourcePack = (IResourcePack) new FolderResourcePack(this.resourcePackFile, "resourcepacks/");

> CHANGE  9 : 11  @  9 : 10

~ 				logger.error("Failed to load resource pack icon for \"{}\"!", resourcePackFile);
~ 				logger.error(var2);

> INSERT  13 : 14  @  12

+ 				this.iconTextureManager = textureManagerIn;

> INSERT  9 : 13  @  8

+ 			if (this.locationTexturePackIcon != null) {
+ 				this.iconTextureManager.deleteTexture(this.locationTexturePackIcon);
+ 				this.locationTexturePackIcon = null;
+ 			}

> CHANGE  39 : 40  @  35 : 39

~ 			return this.resourcePackFile;

> EOF
