
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 11

> DELETE  3  @  12 : 14

> DELETE  4  @  15 : 18

> CHANGE  6 : 18  @  20 : 22

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

> DELETE  19  @  23 : 24

> DELETE  21  @  26 : 29

> DELETE  25  @  33 : 34

> DELETE  26  @  35 : 42

> DELETE  29  @  45 : 53

> DELETE  30  @  54 : 55

> DELETE  32  @  57 : 58

> CHANGE  36 : 37  @  62 : 64

~ 	public ResourcePackRepository(IResourcePack rprDefaultResourcePackIn, IMetadataSerializer rprMetadataSerializerIn,

> DELETE  38  @  65 : 67

> DELETE  40  @  69 : 70

> DELETE  63  @  93 : 111

> CHANGE  64 : 66  @  112 : 113

~ 		if (SYS.VFS == null)
~ 			return;

> CHANGE  67 : 70  @  114 : 115

~ 		List<ResourcePackRepository.Entry> list = Lists.<ResourcePackRepository.Entry>newArrayList();
~ 
~ 		for (String file1 : SYS.getResourcePackNames()) {

> INSERT  71 : 72  @  116

+ 

> CHANGE  75 : 76  @  119 : 120

~ 					list.add(resourcepackrepository$entry);

> CHANGE  77 : 81  @  121 : 122

~ 					logger.error("Failed to call \"updateResourcePack\" for resource pack \"{}\"",
~ 							resourcepackrepository$entry.resourcePackFile);
~ 					logger.error(var6);
~ 					list.remove(resourcepackrepository$entry);

> INSERT  84 : 85  @  125

+ 

> CHANGE  86 : 87  @  126 : 127

~ 					list.add(this.repositoryEntriesAll.get(i));

> CHANGE  91 : 92  @  131 : 132

~ 		this.repositoryEntriesAll.removeAll(list);

> CHANGE  97 : 98  @  137 : 138

~ 		this.repositoryEntriesAll = list;

> CHANGE  113 : 120  @  153 : 185

~ 	public void downloadResourcePack(String s1, String s2, Consumer<Boolean> cb) {
~ 		SYS.loadRemoteResourcePack(s1, s2, res -> {
~ 			if (res != null) {
~ 				ResourcePackRepository.this.resourcePackInstance = new FolderResourcePack(res, "srp/");
~ 				Minecraft.getMinecraft().scheduleResourcesRefresh();
~ 				cb.accept(true);
~ 				return;

> CHANGE  121 : 128  @  186 : 214

~ 			cb.accept(false);
~ 		}, runnable -> {
~ 			Minecraft.getMinecraft().addScheduledTask(runnable);
~ 		}, () -> {
~ 			Minecraft.getMinecraft().loadingScreen.eaglerShow(I18n.format("resourcePack.load.loading"),
~ 					"Server resource pack");
~ 		});

> DELETE  130  @  216 : 236

> CHANGE  135 : 138  @  241 : 255

~ 		if (this.resourcePackInstance != null) {
~ 			this.resourcePackInstance = null;
~ 			Minecraft.getMinecraft().scheduleResourcesRefresh();

> DELETE  139  @  256 : 257

> CHANGE  142 : 143  @  260 : 261

~ 		private final String resourcePackFile;

> CHANGE  145 : 146  @  263 : 264

~ 		private ImageData texturePackIcon;

> INSERT  147 : 148  @  265

+ 		private TextureManager iconTextureManager;

> CHANGE  149 : 150  @  266 : 267

~ 		private Entry(String resourcePackFileIn) {

> CHANGE  154 : 157  @  271 : 274

~ 			if (SYS.VFS == null)
~ 				return;
~ 			this.reResourcePack = (IResourcePack) new FolderResourcePack(this.resourcePackFile, "resourcepacks/");

> CHANGE  163 : 165  @  280 : 281

~ 				logger.error("Failed to load resource pack icon for \"{}\"!", resourcePackFile);
~ 				logger.error(var2);

> INSERT  176 : 177  @  292

+ 				this.iconTextureManager = textureManagerIn;

> INSERT  185 : 189  @  300

+ 			if (this.locationTexturePackIcon != null) {
+ 				this.iconTextureManager.deleteTexture(this.locationTexturePackIcon);
+ 				this.locationTexturePackIcon = null;
+ 			}

> CHANGE  224 : 225  @  335 : 339

~ 			return this.resourcePackFile;

> EOF
