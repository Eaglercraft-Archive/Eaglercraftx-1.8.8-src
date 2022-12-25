
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 6

> DELETE  5  @  9 : 10

> INSERT  6 : 11  @  11

+ 
+ import com.google.common.collect.ImmutableSet;
+ 
+ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ImageData;

> DELETE  12  @  12 : 14

> CHANGE  17 : 18  @  19 : 21

~ 	public static final Set<String> defaultResourceDomains = ImmutableSet.of("minecraft", "eagler");

> DELETE  19  @  22 : 26

> CHANGE  34 : 35  @  41 : 43

~ 		return null;

> CHANGE  38 : 40  @  46 : 48

~ 		return EagRuntime
~ 				.getResourceStream("/assets/" + location.getResourceDomain() + "/" + location.getResourcePath());

> CHANGE  43 : 44  @  51 : 53

~ 		return this.getResourceStream(resourcelocation) != null;

> CHANGE  53 : 55  @  62 : 64

~ 			return AbstractResourcePack.readMetadata(parIMetadataSerializer,
~ 					EagRuntime.getResourceStream("pack.mcmeta"), parString1);

> DELETE  57  @  66 : 68

> CHANGE  60 : 62  @  71 : 74

~ 	public ImageData getPackImage() throws IOException {
~ 		return TextureUtil.readBufferedImage(EagRuntime.getResourceStream("pack.png"));

> EOF
