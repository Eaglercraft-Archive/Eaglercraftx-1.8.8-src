
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 8

> INSERT  8 : 9  @  14

+ import java.nio.charset.StandardCharsets;

> DELETE  17  @  22 : 23

> INSERT  18 : 30  @  24

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

> DELETE  39  @  33 : 34

> DELETE  42  @  37 : 43

> DELETE  49  @  50 : 53

> CHANGE  69 : 70  @  73 : 74

~ 	private final Map<ResourceLocation, EaglerTextureAtlasSprite> sprites = Maps.newHashMap();

> INSERT  130 : 131  @  134

+ 					LOGGER.warn(var6);

> CHANGE  133 : 135  @  136 : 137

~ 				LOGGER.warn("Unable to load definition " + modelresourcelocation);
~ 				LOGGER.warn(exception);

> CHANGE  174 : 175  @  176 : 177

~ 			modelblockdefinition = new ModelBlockDefinition((ArrayList<ModelBlockDefinition>) arraylist);

> CHANGE  197 : 199  @  199 : 200

~ 								+ modelresourcelocation + "\'");
~ 						LOGGER.warn(exception);

> CHANGE  217 : 218  @  218 : 219

~ 			String str;

> CHANGE  220 : 222  @  221 : 223

~ 				str = (String) BUILT_IN_MODELS.get(s1);
~ 				if (str == null) {

> DELETE  224  @  225 : 227

> CHANGE  226 : 229  @  229 : 230

~ 				try (InputStream is = iresource.getInputStream()) {
~ 					str = IOUtils.inputStreamToString(is, StandardCharsets.UTF_8);
~ 				}

> CHANGE  231 : 233  @  232 : 240

~ 			ModelBlock modelblock = ModelBlock.deserialize(str);
~ 			modelblock.name = parResourceLocation.toString();

> CHANGE  234 : 235  @  241 : 242

~ 			return modelblock;

> CHANGE  256 : 258  @  263 : 264

~ 								+ Item.itemRegistry.getNameForObject(item) + "\'");
~ 						LOGGER.warn(exception);

> CHANGE  446 : 447  @  452 : 453

~ 		for (ModelResourceLocation modelresourcelocation : (List<ModelResourceLocation>) arraylist) {

> CHANGE  466 : 467  @  472 : 473

~ 		EaglerTextureAtlasSprite textureatlassprite = (EaglerTextureAtlasSprite) this.sprites

> CHANGE  474 : 475  @  480 : 481

~ 				EaglerTextureAtlasSprite textureatlassprite1 = (EaglerTextureAtlasSprite) this.sprites

> CHANGE  491 : 492  @  497 : 498

~ 			EaglerTextureAtlasSprite parTextureAtlasSprite, EnumFacing parEnumFacing, ModelRotation parModelRotation,

> CHANGE  509 : 510  @  515 : 516

~ 		List arraydeque = Lists.newLinkedList();

> CHANGE  521 : 522  @  527 : 528

~ 			ResourceLocation resourcelocation2 = (ResourceLocation) arraydeque.remove(0);

> CHANGE  536 : 538  @  542 : 543

~ 						+ "; unable to load model: \'" + resourcelocation2 + "\'");
~ 				LOGGER.warn(exception);

> CHANGE  588 : 590  @  593 : 595

~ 				for (ResourceLocation resourcelocation : (Set<ResourceLocation>) set) {
~ 					EaglerTextureAtlasSprite textureatlassprite = texturemap.registerSprite(resourcelocation);

> CHANGE  611 : 612  @  616 : 617

~ 							EaglerTextureAtlasSprite.setLocationNameCompass(resourcelocation2.toString());

> CHANGE  614 : 615  @  619 : 620

~ 							EaglerTextureAtlasSprite.setLocationNameClock(resourcelocation2.toString());

> CHANGE  667 : 668  @  672 : 673

~ 		for (EaglerTextureAtlasSprite textureatlassprite : this.sprites.values()) {

> EOF
