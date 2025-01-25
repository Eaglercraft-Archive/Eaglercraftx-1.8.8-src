
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 6

> CHANGE  3 : 5  @  3 : 4

~ import java.util.Collection;
~ import java.util.Collections;

> INSERT  1 : 8  @  1

+ 
+ import com.google.common.collect.ImmutableSet;
+ 
+ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
+ import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerFolderResourcePack;
+ import net.lax1dude.eaglercraft.v1_8.minecraft.ResourceIndex;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ImageData;

> DELETE  1  @  1 : 3

> CHANGE  4 : 6  @  4 : 7

~ public class DefaultResourcePack extends ResourceIndex implements IResourcePack {
~ 	public static final Set<String> defaultResourceDomains = ImmutableSet.of("minecraft", "eagler");

> CHANGE  1 : 13  @  1 : 3

~ 	private final Collection<String> propertyFilesIndex;
~ 
~ 	public DefaultResourcePack() {
~ 		String str = EagRuntime.getResourceString("/assets/minecraft/optifine/_property_files_index.json");
~ 		if (str != null) {
~ 			Collection<String> lst = EaglerFolderResourcePack.loadPropertyFileList(str);
~ 			if (lst != null) {
~ 				propertyFilesIndex = lst;
~ 				return;
~ 			}
~ 		}
~ 		propertyFilesIndex = Collections.emptyList();

> CHANGE  17 : 18  @  17 : 19

~ 		return null;

> CHANGE  3 : 5  @  3 : 5

~ 		return EagRuntime
~ 				.getResourceStream("/assets/" + location.getResourceDomain() + "/" + location.getResourcePath());

> CHANGE  2 : 5  @  2 : 5

~ 	public boolean resourceExists(ResourceLocation location) {
~ 		return EagRuntime
~ 				.getResourceExists("/assets/" + location.getResourceDomain() + "/" + location.getResourcePath());

> CHANGE  9 : 11  @  9 : 11

~ 			return AbstractResourcePack.readMetadata(parIMetadataSerializer,
~ 					EagRuntime.getRequiredResourceStream("pack.mcmeta"), parString1);

> DELETE  2  @  2 : 4

> CHANGE  3 : 5  @  3 : 6

~ 	public ImageData getPackImage() throws IOException {
~ 		return TextureUtil.readBufferedImage(EagRuntime.getRequiredResourceStream("pack.png"));

> INSERT  5 : 20  @  5

+ 
+ 	@Override
+ 	public ResourceIndex getEaglerFileIndex() {
+ 		return this;
+ 	}
+ 
+ 	@Override
+ 	protected Collection<String> getPropertiesFiles0() {
+ 		return propertyFilesIndex;
+ 	}
+ 
+ 	@Override
+ 	protected Collection<String> getCITPotionsFiles0() {
+ 		return Collections.emptyList();
+ 	}

> EOF
