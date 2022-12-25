
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> INSERT  6 : 13  @  8

+ 
+ import net.lax1dude.eaglercraft.v1_8.HString;
+ import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;
+ 
+ import com.google.common.collect.Lists;
+ import com.google.common.collect.Sets;
+ 

> DELETE  14  @  9 : 10

> CHANGE  43 : 44  @  39 : 40

~ 	public void addSprite(EaglerTextureAtlasSprite parTextureAtlasSprite) {

> CHANGE  59 : 60  @  55 : 56

~ 				String s = HString.format("Unable to fit: %s - size: %dx%d - Maybe try a lowerresolution resourcepack?",

> CHANGE  74 : 75  @  70 : 71

~ 	public List<EaglerTextureAtlasSprite> getStichSlots() {

> CHANGE  83 : 84  @  79 : 80

~ 		for (Stitcher.Slot stitcher$slot1 : (List<Stitcher.Slot>) arraylist) {

> CHANGE  85 : 86  @  81 : 82

~ 			EaglerTextureAtlasSprite textureatlassprite = stitcher$holder.getAtlasSprite();

> CHANGE  176 : 177  @  172 : 173

~ 		private final EaglerTextureAtlasSprite theTexture;

> CHANGE  183 : 184  @  179 : 180

~ 		public Holder(EaglerTextureAtlasSprite parTextureAtlasSprite, int parInt1) {

> CHANGE  192 : 193  @  188 : 189

~ 		public EaglerTextureAtlasSprite getAtlasSprite() {

> EOF
