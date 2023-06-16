
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> INSERT  4 : 11  @  4

+ 
+ import net.lax1dude.eaglercraft.v1_8.HString;
+ import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;
+ 
+ import com.google.common.collect.Lists;
+ import com.google.common.collect.Sets;
+ 

> DELETE  1  @  1 : 2

> CHANGE  29 : 30  @  29 : 30

~ 	public void addSprite(EaglerTextureAtlasSprite parTextureAtlasSprite) {

> CHANGE  15 : 16  @  15 : 16

~ 				String s = HString.format("Unable to fit: %s - size: %dx%d - Maybe try a lowerresolution resourcepack?",

> CHANGE  14 : 15  @  14 : 15

~ 	public List<EaglerTextureAtlasSprite> getStichSlots() {

> CHANGE  8 : 9  @  8 : 9

~ 		for (Stitcher.Slot stitcher$slot1 : (List<Stitcher.Slot>) arraylist) {

> CHANGE  1 : 2  @  1 : 2

~ 			EaglerTextureAtlasSprite textureatlassprite = stitcher$holder.getAtlasSprite();

> CHANGE  90 : 91  @  90 : 91

~ 		private final EaglerTextureAtlasSprite theTexture;

> CHANGE  6 : 7  @  6 : 7

~ 		public Holder(EaglerTextureAtlasSprite parTextureAtlasSprite, int parInt1) {

> CHANGE  8 : 9  @  8 : 9

~ 		public EaglerTextureAtlasSprite getAtlasSprite() {

> EOF
