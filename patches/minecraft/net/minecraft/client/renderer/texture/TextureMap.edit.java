
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 5

> INSERT  3 : 4  @  6

+ import java.util.Collection;

> INSERT  10 : 19  @  12

+ 
+ import com.google.common.collect.Lists;
+ import com.google.common.collect.Maps;
+ 
+ import net.lax1dude.eaglercraft.v1_8.HString;
+ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
+ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
+ import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ImageData;

> DELETE  21  @  14 : 20

> DELETE  30  @  29 : 31

> CHANGE  35 : 38  @  36 : 39

~ 	private final List<EaglerTextureAtlasSprite> listAnimatedSprites;
~ 	private final Map<String, EaglerTextureAtlasSprite> mapRegisteredSprites;
~ 	private final Map<String, EaglerTextureAtlasSprite> mapUploadedSprites;

> CHANGE  41 : 44  @  42 : 43

~ 	private final EaglerTextureAtlasSprite missingImage;
~ 	private int width;
~ 	private int height;

> CHANGE  53 : 54  @  52 : 53

~ 		this.missingImage = new EaglerTextureAtlasSprite("missingno");

> DELETE  71  @  70 : 71

> INSERT  74 : 75  @  74

+ 		destroyAnimationCaches();

> CHANGE  91 : 92  @  90 : 91

~ 			EaglerTextureAtlasSprite textureatlassprite = (EaglerTextureAtlasSprite) entry.getValue();

> CHANGE  97 : 98  @  96 : 97

~ 				ImageData[] abufferedimage = new ImageData[1 + this.mipmapLevels];

> CHANGE  104 : 106  @  103 : 105

~ 						int l = abufferedimage[0].width;
~ 						int i1 = abufferedimage[0].height;

> CHANGE  124 : 126  @  123 : 124

~ 										new Object[] { Integer.valueOf(i2), resourcelocation2 });
~ 								logger.error(ioexception);

> CHANGE  135 : 137  @  133 : 134

~ 				logger.error("Unable to parse metadata from " + resourcelocation1);
~ 				logger.error(runtimeexception);

> CHANGE  139 : 141  @  136 : 137

~ 				logger.error("Using missing texture, unable to load " + resourcelocation1);
~ 				logger.error(ioexception1);

> CHANGE  167 : 168  @  163 : 164

~ 		for (final EaglerTextureAtlasSprite textureatlassprite1 : this.mapRegisteredSprites.values()) {

> CHANGE  208 : 212  @  204 : 205

~ 		width = stitcher.getCurrentWidth();
~ 		height = stitcher.getCurrentHeight();
~ 
~ 		for (EaglerTextureAtlasSprite textureatlassprite2 : stitcher.getStichSlots()) {

> CHANGE  233 : 234  @  226 : 227

~ 		for (EaglerTextureAtlasSprite textureatlassprite3 : (Collection<EaglerTextureAtlasSprite>) hashmap.values()) {

> CHANGE  242 : 244  @  235 : 237

~ 						HString.format("%s/%s%s", new Object[] { this.basePath, location.getResourcePath(), ".png" }))
~ 				: new ResourceLocation(location.getResourceDomain(), HString.format("%s/mipmaps/%s.%d%s",

> CHANGE  247 : 249  @  240 : 242

~ 	public EaglerTextureAtlasSprite getAtlasSprite(String iconName) {
~ 		EaglerTextureAtlasSprite textureatlassprite = (EaglerTextureAtlasSprite) this.mapUploadedSprites.get(iconName);

> CHANGE  259 : 260  @  252 : 253

~ 		for (EaglerTextureAtlasSprite textureatlassprite : this.listAnimatedSprites) {

> CHANGE  265 : 272  @  258 : 259

~ 	private void destroyAnimationCaches() {
~ 		for (EaglerTextureAtlasSprite textureatlassprite : this.listAnimatedSprites) {
~ 			textureatlassprite.clearFramesTextureData();
~ 		}
~ 	}
~ 
~ 	public EaglerTextureAtlasSprite registerSprite(ResourceLocation location) {

> CHANGE  275 : 277  @  262 : 263

~ 			EaglerTextureAtlasSprite textureatlassprite = (EaglerTextureAtlasSprite) this.mapRegisteredSprites
~ 					.get(location);

> CHANGE  278 : 279  @  264 : 265

~ 				textureatlassprite = EaglerTextureAtlasSprite.makeAtlasSprite(location);

> CHANGE  294 : 295  @  280 : 281

~ 	public EaglerTextureAtlasSprite getMissingSprite() {

> INSERT  297 : 306  @  283

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
