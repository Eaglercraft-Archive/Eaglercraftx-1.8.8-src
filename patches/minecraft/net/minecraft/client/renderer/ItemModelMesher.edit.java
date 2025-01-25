
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> CHANGE  1 : 7  @  1 : 4

~ import com.carrotsearch.hppc.IntObjectHashMap;
~ import com.carrotsearch.hppc.IntObjectMap;
~ import com.carrotsearch.hppc.cursors.IntObjectCursor;
~ import com.google.common.collect.Maps;
~ 
~ import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;

> INSERT  5 : 8  @  5

+ import net.minecraft.util.ResourceLocation;
+ import net.optifine.Config;
+ import net.optifine.CustomItems;

> CHANGE  2 : 4  @  2 : 4

~ 	private final IntObjectMap<ModelResourceLocation> simpleShapes = new IntObjectHashMap<>();
~ 	private final IntObjectMap<IBakedModel> simpleShapesCache = new IntObjectHashMap<>();

> CHANGE  7 : 8  @  7 : 8

~ 	public EaglerTextureAtlasSprite getParticleIcon(Item item) {

> CHANGE  3 : 4  @  3 : 4

~ 	public EaglerTextureAtlasSprite getParticleIcon(Item item, int meta) {

> CHANGE  7 : 8  @  7 : 8

~ 			ItemMeshDefinition itemmeshdefinition = this.shapers.get(item);

> INSERT  9 : 13  @  9

+ 		if (Config.isCustomItems()) {
+ 			ibakedmodel = CustomItems.getCustomItemModel(stack, ibakedmodel, (ResourceLocation) null, true);
+ 		}
+ 

> CHANGE  8 : 9  @  8 : 9

~ 		return this.simpleShapesCache.get(this.getIndex(item, meta));

> CHANGE  7 : 9  @  7 : 9

~ 		this.simpleShapes.put(this.getIndex(item, meta), location);
~ 		this.simpleShapesCache.put(this.getIndex(item, meta), this.modelManager.getModel(location));

> CHANGE  13 : 15  @  13 : 16

~ 		for (IntObjectCursor<ModelResourceLocation> entry : this.simpleShapes) {
~ 			this.simpleShapesCache.put(entry.key, this.modelManager.getModel(entry.value));

> DELETE  1  @  1 : 2

> EOF
