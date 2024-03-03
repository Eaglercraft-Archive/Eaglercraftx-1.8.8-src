
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

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

> CHANGE  13 : 15  @  13 : 14

~ 		for (int i = 0; i < astitcher$holder.length; ++i) {
~ 			Stitcher.Holder stitcher$holder = astitcher$holder[i];

> CHANGE  1 : 2  @  1 : 2

~ 				String s = HString.format("Unable to fit: %s - size: %dx%d - Maybe try a lowerresolution resourcepack?",

> CHANGE  14 : 16  @  14 : 16

~ 	public List<EaglerTextureAtlasSprite> getStichSlots() {
~ 		ArrayList<Slot> arraylist = Lists.newArrayList();

> CHANGE  1 : 3  @  1 : 3

~ 		for (int i = 0, l = this.stitchSlots.size(); i < l; ++i) {
~ 			this.stitchSlots.get(i).getAllStitchSlots(arraylist);

> CHANGE  2 : 3  @  2 : 3

~ 		ArrayList<EaglerTextureAtlasSprite> arraylist1 = Lists.newArrayList();

> CHANGE  1 : 3  @  1 : 2

~ 		for (int i = 0, l = arraylist.size(); i < l; ++i) {
~ 			Stitcher.Slot stitcher$slot1 = arraylist.get(i);

> CHANGE  1 : 2  @  1 : 2

~ 			EaglerTextureAtlasSprite textureatlassprite = stitcher$holder.getAtlasSprite();

> CHANGE  90 : 91  @  90 : 91

~ 		private final EaglerTextureAtlasSprite theTexture;

> CHANGE  6 : 7  @  6 : 7

~ 		public Holder(EaglerTextureAtlasSprite parTextureAtlasSprite, int parInt1) {

> CHANGE  8 : 9  @  8 : 9

~ 		public EaglerTextureAtlasSprite getAtlasSprite() {

> CHANGE  117 : 119  @  117 : 119

~ 						for (int m = 0, n = this.subSlots.size(); m < n; ++m) {
~ 							if (this.subSlots.get(m).addSlot(holderIn)) {

> CHANGE  16 : 18  @  16 : 18

~ 				for (int i = 0, l = this.subSlots.size(); i < l; ++i) {
~ 					this.subSlots.get(i).getAllStitchSlots(parList);

> EOF
