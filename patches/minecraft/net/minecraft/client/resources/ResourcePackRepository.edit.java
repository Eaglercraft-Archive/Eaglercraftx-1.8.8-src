
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 11

> DELETE  1  @  1 : 3

> DELETE  1  @  1 : 4

> CHANGE  2 : 14  @  2 : 4

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

> DELETE  1  @  1 : 2

> DELETE  2  @  2 : 5

> DELETE  4  @  4 : 5

> DELETE  1  @  1 : 8

> DELETE  3  @  3 : 11

> DELETE  1  @  1 : 2

> DELETE  2  @  2 : 3

> CHANGE  4 : 5  @  4 : 6

~ 	public ResourcePackRepository(IResourcePack rprDefaultResourcePackIn, IMetadataSerializer rprMetadataSerializerIn,

> DELETE  1  @  1 : 3

> DELETE  2  @  2 : 3

> DELETE  23  @  23 : 41

> CHANGE  1 : 3  @  1 : 2

~ 		if (SYS.VFS == null)
~ 			return;

> CHANGE  1 : 4  @  1 : 2

~ 		List<ResourcePackRepository.Entry> list = Lists.<ResourcePackRepository.Entry>newArrayList();
~ 
~ 		for (String file1 : SYS.getResourcePackNames()) {

> INSERT  1 : 2  @  1

+ 

> CHANGE  3 : 4  @  3 : 4

~ 					list.add(resourcepackrepository$entry);

> CHANGE  1 : 5  @  1 : 2

~ 					logger.error("Failed to call \"updateResourcePack\" for resource pack \"{}\"",
~ 							resourcepackrepository$entry.resourcePackFile);
~ 					logger.error(var6);
~ 					list.remove(resourcepackrepository$entry);

> INSERT  3 : 4  @  3

+ 

> CHANGE  1 : 2  @  1 : 2

~ 					list.add(this.repositoryEntriesAll.get(i));

> CHANGE  4 : 5  @  4 : 5

~ 		this.repositoryEntriesAll.removeAll(list);

> CHANGE  5 : 6  @  5 : 6

~ 		this.repositoryEntriesAll = list;

> CHANGE  15 : 22  @  15 : 47

~ 	public void downloadResourcePack(String s1, String s2, Consumer<Boolean> cb) {
~ 		SYS.loadRemoteResourcePack(s1, s2, res -> {
~ 			if (res != null) {
~ 				ResourcePackRepository.this.resourcePackInstance = new FolderResourcePack(res, "srp/");
~ 				Minecraft.getMinecraft().scheduleResourcesRefresh();
~ 				cb.accept(true);
~ 				return;

> CHANGE  1 : 8  @  1 : 29

~ 			cb.accept(false);
~ 		}, runnable -> {
~ 			Minecraft.getMinecraft().addScheduledTask(runnable);
~ 		}, () -> {
~ 			Minecraft.getMinecraft().loadingScreen.eaglerShow(I18n.format("resourcePack.load.loading"),
~ 					"Server resource pack");
~ 		});

> DELETE  2  @  2 : 22

> CHANGE  5 : 8  @  5 : 19

~ 		if (this.resourcePackInstance != null) {
~ 			this.resourcePackInstance = null;
~ 			Minecraft.getMinecraft().scheduleResourcesRefresh();

> DELETE  1  @  1 : 2

> CHANGE  3 : 4  @  3 : 4

~ 		private final String resourcePackFile;

> CHANGE  2 : 3  @  2 : 3

~ 		private ImageData texturePackIcon;

> INSERT  1 : 2  @  1

+ 		private TextureManager iconTextureManager;

> CHANGE  1 : 2  @  1 : 2

~ 		private Entry(String resourcePackFileIn) {

> CHANGE  4 : 7  @  4 : 7

~ 			if (SYS.VFS == null)
~ 				return;
~ 			this.reResourcePack = (IResourcePack) new FolderResourcePack(this.resourcePackFile, "resourcepacks/");

> CHANGE  6 : 8  @  6 : 7

~ 				logger.error("Failed to load resource pack icon for \"{}\"!", resourcePackFile);
~ 				logger.error(var2);

> INSERT  11 : 12  @  11

+ 				this.iconTextureManager = textureManagerIn;

> INSERT  8 : 12  @  8

+ 			if (this.locationTexturePackIcon != null) {
+ 				this.iconTextureManager.deleteTexture(this.locationTexturePackIcon);
+ 				this.locationTexturePackIcon = null;
+ 			}

> CHANGE  35 : 36  @  35 : 39

~ 			return this.resourcePackFile;

> EOF
