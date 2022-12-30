
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 5

> INSERT  1 : 2  @  4

+ import java.util.Collection;

> INSERT  7 : 16  @  6

+ 
+ import com.google.common.collect.Lists;
+ import com.google.common.collect.Maps;
+ 
+ import net.lax1dude.eaglercraft.v1_8.HString;
+ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
+ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
+ import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ImageData;

> DELETE  11  @  2 : 8

> DELETE  9  @  15 : 17

> CHANGE  5 : 8  @  7 : 10

~ 	private final List<EaglerTextureAtlasSprite> listAnimatedSprites;
~ 	private final Map<String, EaglerTextureAtlasSprite> mapRegisteredSprites;
~ 	private final Map<String, EaglerTextureAtlasSprite> mapUploadedSprites;

> CHANGE  6 : 9  @  6 : 7

~ 	private final EaglerTextureAtlasSprite missingImage;
~ 	private int width;
~ 	private int height;

> CHANGE  12 : 13  @  10 : 11

~ 		this.missingImage = new EaglerTextureAtlasSprite("missingno");

> DELETE  18  @  18 : 19

> INSERT  3 : 4  @  4

+ 		destroyAnimationCaches();

> CHANGE  17 : 18  @  16 : 17

~ 			EaglerTextureAtlasSprite textureatlassprite = (EaglerTextureAtlasSprite) entry.getValue();

> CHANGE  6 : 7  @  6 : 7

~ 				ImageData[] abufferedimage = new ImageData[1 + this.mipmapLevels];

> CHANGE  7 : 9  @  7 : 9

~ 						int l = abufferedimage[0].width;
~ 						int i1 = abufferedimage[0].height;

> CHANGE  20 : 22  @  20 : 21

~ 										new Object[] { Integer.valueOf(i2), resourcelocation2 });
~ 								logger.error(ioexception);

> CHANGE  11 : 13  @  10 : 11

~ 				logger.error("Unable to parse metadata from " + resourcelocation1);
~ 				logger.error(runtimeexception);

> CHANGE  4 : 6  @  3 : 4

~ 				logger.error("Using missing texture, unable to load " + resourcelocation1);
~ 				logger.error(ioexception1);

> CHANGE  28 : 29  @  27 : 28

~ 		for (final EaglerTextureAtlasSprite textureatlassprite1 : this.mapRegisteredSprites.values()) {

> CHANGE  41 : 45  @  41 : 42

~ 		width = stitcher.getCurrentWidth();
~ 		height = stitcher.getCurrentHeight();
~ 
~ 		for (EaglerTextureAtlasSprite textureatlassprite2 : stitcher.getStichSlots()) {

> CHANGE  25 : 26  @  22 : 23

~ 		for (EaglerTextureAtlasSprite textureatlassprite3 : (Collection<EaglerTextureAtlasSprite>) hashmap.values()) {

> CHANGE  9 : 11  @  9 : 11

~ 						HString.format("%s/%s%s", new Object[] { this.basePath, location.getResourcePath(), ".png" }))
~ 				: new ResourceLocation(location.getResourceDomain(), HString.format("%s/mipmaps/%s.%d%s",

> CHANGE  5 : 7  @  5 : 7

~ 	public EaglerTextureAtlasSprite getAtlasSprite(String iconName) {
~ 		EaglerTextureAtlasSprite textureatlassprite = (EaglerTextureAtlasSprite) this.mapUploadedSprites.get(iconName);

> CHANGE  12 : 13  @  12 : 13

~ 		for (EaglerTextureAtlasSprite textureatlassprite : this.listAnimatedSprites) {

> CHANGE  6 : 13  @  6 : 7

~ 	private void destroyAnimationCaches() {
~ 		for (EaglerTextureAtlasSprite textureatlassprite : this.listAnimatedSprites) {
~ 			textureatlassprite.clearFramesTextureData();
~ 		}
~ 	}
~ 
~ 	public EaglerTextureAtlasSprite registerSprite(ResourceLocation location) {

> CHANGE  10 : 12  @  4 : 5

~ 			EaglerTextureAtlasSprite textureatlassprite = (EaglerTextureAtlasSprite) this.mapRegisteredSprites
~ 					.get(location);

> CHANGE  3 : 4  @  2 : 3

~ 				textureatlassprite = EaglerTextureAtlasSprite.makeAtlasSprite(location);

> CHANGE  16 : 17  @  16 : 17

~ 	public EaglerTextureAtlasSprite getMissingSprite() {

> INSERT  3 : 12  @  3

+ 
+ 	public int getWidth() {
+ 		return width;
+ 	}
+ 
+ 	public int getHeight() {
+ 		return height;
+ 	}
+ 

> EOF
