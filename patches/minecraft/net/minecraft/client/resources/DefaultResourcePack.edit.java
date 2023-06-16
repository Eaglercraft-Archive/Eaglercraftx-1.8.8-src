
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 6

> DELETE  3  @  3 : 4

> INSERT  1 : 6  @  1

+ 
+ import com.google.common.collect.ImmutableSet;
+ 
+ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ImageData;

> DELETE  1  @  1 : 3

> CHANGE  5 : 6  @  5 : 7

~ 	public static final Set<String> defaultResourceDomains = ImmutableSet.of("minecraft", "eagler");

> DELETE  1  @  1 : 5

> CHANGE  15 : 16  @  15 : 17

~ 		return null;

> CHANGE  3 : 5  @  3 : 5

~ 		return EagRuntime
~ 				.getResourceStream("/assets/" + location.getResourceDomain() + "/" + location.getResourcePath());

> CHANGE  3 : 4  @  3 : 5

~ 		return this.getResourceStream(resourcelocation) != null;

> CHANGE  9 : 11  @  9 : 11

~ 			return AbstractResourcePack.readMetadata(parIMetadataSerializer,
~ 					EagRuntime.getResourceStream("pack.mcmeta"), parString1);

> DELETE  2  @  2 : 4

> CHANGE  3 : 5  @  3 : 6

~ 	public ImageData getPackImage() throws IOException {
~ 		return TextureUtil.readBufferedImage(EagRuntime.getResourceStream("pack.png"));

> EOF
