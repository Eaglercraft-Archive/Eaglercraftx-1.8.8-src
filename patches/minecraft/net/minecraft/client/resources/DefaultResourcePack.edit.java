
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 6

> DELETE  3  @  7 : 8

> INSERT  1 : 6  @  2

+ 
+ import com.google.common.collect.ImmutableSet;
+ 
+ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ImageData;

> DELETE  6  @  1 : 3

> CHANGE  5 : 6  @  7 : 9

~ 	public static final Set<String> defaultResourceDomains = ImmutableSet.of("minecraft", "eagler");

> DELETE  2  @  3 : 7

> CHANGE  15 : 16  @  19 : 21

~ 		return null;

> CHANGE  4 : 6  @  5 : 7

~ 		return EagRuntime
~ 				.getResourceStream("/assets/" + location.getResourceDomain() + "/" + location.getResourcePath());

> CHANGE  5 : 6  @  5 : 7

~ 		return this.getResourceStream(resourcelocation) != null;

> CHANGE  10 : 12  @  11 : 13

~ 			return AbstractResourcePack.readMetadata(parIMetadataSerializer,
~ 					EagRuntime.getResourceStream("pack.mcmeta"), parString1);

> DELETE  4  @  4 : 6

> CHANGE  3 : 5  @  5 : 8

~ 	public ImageData getPackImage() throws IOException {
~ 		return TextureUtil.readBufferedImage(EagRuntime.getResourceStream("pack.png"));

> EOF
