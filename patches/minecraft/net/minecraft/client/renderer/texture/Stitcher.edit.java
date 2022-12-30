
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> INSERT  4 : 11  @  6

+ 
+ import net.lax1dude.eaglercraft.v1_8.HString;
+ import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;
+ 
+ import com.google.common.collect.Lists;
+ import com.google.common.collect.Sets;
+ 

> DELETE  8  @  1 : 2

> CHANGE  29 : 30  @  30 : 31

~ 	public void addSprite(EaglerTextureAtlasSprite parTextureAtlasSprite) {

> CHANGE  16 : 17  @  16 : 17

~ 				String s = HString.format("Unable to fit: %s - size: %dx%d - Maybe try a lowerresolution resourcepack?",

> CHANGE  15 : 16  @  15 : 16

~ 	public List<EaglerTextureAtlasSprite> getStichSlots() {

> CHANGE  9 : 10  @  9 : 10

~ 		for (Stitcher.Slot stitcher$slot1 : (List<Stitcher.Slot>) arraylist) {

> CHANGE  2 : 3  @  2 : 3

~ 			EaglerTextureAtlasSprite textureatlassprite = stitcher$holder.getAtlasSprite();

> CHANGE  91 : 92  @  91 : 92

~ 		private final EaglerTextureAtlasSprite theTexture;

> CHANGE  7 : 8  @  7 : 8

~ 		public Holder(EaglerTextureAtlasSprite parTextureAtlasSprite, int parInt1) {

> CHANGE  9 : 10  @  9 : 10

~ 		public EaglerTextureAtlasSprite getAtlasSprite() {

> EOF
