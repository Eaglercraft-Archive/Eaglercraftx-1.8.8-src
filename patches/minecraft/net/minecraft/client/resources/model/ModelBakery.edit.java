
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 8

> INSERT  6 : 7  @  12

+ import java.nio.charset.StandardCharsets;

> DELETE  9  @  8 : 9

> INSERT  1 : 13  @  2

+ import java.util.Set;
+ 
+ import com.google.common.base.Charsets;
+ import com.google.common.base.Joiner;
+ import com.google.common.collect.Lists;
+ import com.google.common.collect.Maps;
+ import com.google.common.collect.Sets;
+ 
+ import net.lax1dude.eaglercraft.v1_8.IOUtils;
+ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
+ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
+ import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;

> DELETE  21  @  9 : 10

> DELETE  3  @  4 : 10

> DELETE  7  @  13 : 16

> CHANGE  20 : 21  @  23 : 24

~ 	private final Map<ResourceLocation, EaglerTextureAtlasSprite> sprites = Maps.newHashMap();

> INSERT  61 : 62  @  61

+ 					LOGGER.warn(var6);

> CHANGE  3 : 5  @  2 : 3

~ 				LOGGER.warn("Unable to load definition " + modelresourcelocation);
~ 				LOGGER.warn(exception);

> CHANGE  41 : 42  @  40 : 41

~ 			modelblockdefinition = new ModelBlockDefinition((ArrayList<ModelBlockDefinition>) arraylist);

> CHANGE  23 : 25  @  23 : 24

~ 								+ modelresourcelocation + "\'");
~ 						LOGGER.warn(exception);

> CHANGE  20 : 21  @  19 : 20

~ 			String str;

> CHANGE  3 : 5  @  3 : 5

~ 				str = (String) BUILT_IN_MODELS.get(s1);
~ 				if (str == null) {

> DELETE  4  @  4 : 6

> CHANGE  2 : 5  @  4 : 5

~ 				try (InputStream is = iresource.getInputStream()) {
~ 					str = IOUtils.inputStreamToString(is, StandardCharsets.UTF_8);
~ 				}

> CHANGE  5 : 7  @  3 : 11

~ 			ModelBlock modelblock = ModelBlock.deserialize(str);
~ 			modelblock.name = parResourceLocation.toString();

> CHANGE  3 : 4  @  9 : 10

~ 			return modelblock;

> CHANGE  22 : 24  @  22 : 23

~ 								+ Item.itemRegistry.getNameForObject(item) + "\'");
~ 						LOGGER.warn(exception);

> CHANGE  190 : 191  @  189 : 190

~ 		for (ModelResourceLocation modelresourcelocation : (List<ModelResourceLocation>) arraylist) {

> CHANGE  20 : 21  @  20 : 21

~ 		EaglerTextureAtlasSprite textureatlassprite = (EaglerTextureAtlasSprite) this.sprites

> CHANGE  8 : 9  @  8 : 9

~ 				EaglerTextureAtlasSprite textureatlassprite1 = (EaglerTextureAtlasSprite) this.sprites

> CHANGE  17 : 18  @  17 : 18

~ 			EaglerTextureAtlasSprite parTextureAtlasSprite, EnumFacing parEnumFacing, ModelRotation parModelRotation,

> CHANGE  18 : 19  @  18 : 19

~ 		List arraydeque = Lists.newLinkedList();

> CHANGE  12 : 13  @  12 : 13

~ 			ResourceLocation resourcelocation2 = (ResourceLocation) arraydeque.remove(0);

> CHANGE  15 : 17  @  15 : 16

~ 						+ "; unable to load model: \'" + resourcelocation2 + "\'");
~ 				LOGGER.warn(exception);

> CHANGE  52 : 54  @  51 : 53

~ 				for (ResourceLocation resourcelocation : (Set<ResourceLocation>) set) {
~ 					EaglerTextureAtlasSprite textureatlassprite = texturemap.registerSprite(resourcelocation);

> CHANGE  23 : 24  @  23 : 24

~ 							EaglerTextureAtlasSprite.setLocationNameCompass(resourcelocation2.toString());

> CHANGE  3 : 4  @  3 : 4

~ 							EaglerTextureAtlasSprite.setLocationNameClock(resourcelocation2.toString());

> CHANGE  53 : 54  @  53 : 54

~ 		for (EaglerTextureAtlasSprite textureatlassprite : this.sprites.values()) {

> EOF
