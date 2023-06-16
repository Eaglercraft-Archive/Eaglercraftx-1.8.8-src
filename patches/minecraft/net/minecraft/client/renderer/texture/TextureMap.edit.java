
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 5

> INSERT  1 : 2  @  1

+ import java.util.Collection;

> INSERT  6 : 15  @  6

+ 
+ import com.google.common.collect.Lists;
+ import com.google.common.collect.Maps;
+ 
+ import net.lax1dude.eaglercraft.v1_8.HString;
+ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
+ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
+ import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ImageData;

> DELETE  2  @  2 : 8

> DELETE  9  @  9 : 11

> CHANGE  5 : 8  @  5 : 8

~ 	private final List<EaglerTextureAtlasSprite> listAnimatedSprites;
~ 	private final Map<String, EaglerTextureAtlasSprite> mapRegisteredSprites;
~ 	private final Map<String, EaglerTextureAtlasSprite> mapUploadedSprites;

> CHANGE  3 : 6  @  3 : 4

~ 	private final EaglerTextureAtlasSprite missingImage;
~ 	private int width;
~ 	private int height;

> CHANGE  9 : 10  @  9 : 10

~ 		this.missingImage = new EaglerTextureAtlasSprite("missingno");

> DELETE  17  @  17 : 18

> INSERT  3 : 4  @  3

+ 		destroyAnimationCaches();

> CHANGE  16 : 17  @  16 : 17

~ 			EaglerTextureAtlasSprite textureatlassprite = (EaglerTextureAtlasSprite) entry.getValue();

> CHANGE  5 : 6  @  5 : 6

~ 				ImageData[] abufferedimage = new ImageData[1 + this.mipmapLevels];

> CHANGE  6 : 8  @  6 : 8

~ 						int l = abufferedimage[0].width;
~ 						int i1 = abufferedimage[0].height;

> CHANGE  18 : 20  @  18 : 19

~ 										new Object[] { Integer.valueOf(i2), resourcelocation2 });
~ 								logger.error(ioexception);

> CHANGE  9 : 11  @  9 : 10

~ 				logger.error("Unable to parse metadata from " + resourcelocation1);
~ 				logger.error(runtimeexception);

> CHANGE  2 : 4  @  2 : 3

~ 				logger.error("Using missing texture, unable to load " + resourcelocation1);
~ 				logger.error(ioexception1);

> CHANGE  26 : 27  @  26 : 27

~ 		for (final EaglerTextureAtlasSprite textureatlassprite1 : this.mapRegisteredSprites.values()) {

> CHANGE  40 : 44  @  40 : 41

~ 		width = stitcher.getCurrentWidth();
~ 		height = stitcher.getCurrentHeight();
~ 
~ 		for (EaglerTextureAtlasSprite textureatlassprite2 : stitcher.getStichSlots()) {

> CHANGE  21 : 22  @  21 : 22

~ 		for (EaglerTextureAtlasSprite textureatlassprite3 : (Collection<EaglerTextureAtlasSprite>) hashmap.values()) {

> CHANGE  8 : 10  @  8 : 10

~ 						HString.format("%s/%s%s", new Object[] { this.basePath, location.getResourcePath(), ".png" }))
~ 				: new ResourceLocation(location.getResourceDomain(), HString.format("%s/mipmaps/%s.%d%s",

> CHANGE  3 : 5  @  3 : 5

~ 	public EaglerTextureAtlasSprite getAtlasSprite(String iconName) {
~ 		EaglerTextureAtlasSprite textureatlassprite = (EaglerTextureAtlasSprite) this.mapUploadedSprites.get(iconName);

> CHANGE  10 : 11  @  10 : 11

~ 		for (EaglerTextureAtlasSprite textureatlassprite : this.listAnimatedSprites) {

> CHANGE  5 : 12  @  5 : 6

~ 	private void destroyAnimationCaches() {
~ 		for (EaglerTextureAtlasSprite textureatlassprite : this.listAnimatedSprites) {
~ 			textureatlassprite.clearFramesTextureData();
~ 		}
~ 	}
~ 
~ 	public EaglerTextureAtlasSprite registerSprite(ResourceLocation location) {

> CHANGE  3 : 5  @  3 : 4

~ 			EaglerTextureAtlasSprite textureatlassprite = (EaglerTextureAtlasSprite) this.mapRegisteredSprites
~ 					.get(location);

> CHANGE  1 : 2  @  1 : 2

~ 				textureatlassprite = EaglerTextureAtlasSprite.makeAtlasSprite(location);

> CHANGE  15 : 16  @  15 : 16

~ 	public EaglerTextureAtlasSprite getMissingSprite() {

> INSERT  2 : 11  @  2

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
