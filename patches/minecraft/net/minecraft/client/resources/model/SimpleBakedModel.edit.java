
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> INSERT  1 : 5  @  1

+ 
+ import com.google.common.collect.Lists;
+ 
+ import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;

> DELETE  4  @  4 : 6

> CHANGE  7 : 8  @  7 : 8

~ 	protected final EaglerTextureAtlasSprite texture;

> CHANGE  3 : 4  @  3 : 4

~ 			EaglerTextureAtlasSprite parTextureAtlasSprite, ItemCameraTransforms parItemCameraTransforms) {

> CHANGE  28 : 29  @  28 : 29

~ 	public EaglerTextureAtlasSprite getParticleTexture() {

> CHANGE  11 : 12  @  11 : 12

~ 		private EaglerTextureAtlasSprite builderTexture;

> CHANGE  7 : 8  @  7 : 8

~ 		public Builder(IBakedModel parIBakedModel, EaglerTextureAtlasSprite parTextureAtlasSprite) {

> CHANGE  4 : 7  @  4 : 6

~ 			EnumFacing[] facings = EnumFacing._VALUES;
~ 			for (int i = 0; i < facings.length; ++i) {
~ 				this.addFaceBreakingFours(parIBakedModel, parTextureAtlasSprite, facings[i]);

> CHANGE  5 : 6  @  5 : 6

~ 		private void addFaceBreakingFours(IBakedModel parIBakedModel, EaglerTextureAtlasSprite parTextureAtlasSprite,

> CHANGE  1 : 4  @  1 : 3

~ 			List<BakedQuad> quads = parIBakedModel.getFaceQuads(parEnumFacing);
~ 			for (int i = 0, l = quads.size(); i < l; ++i) {
~ 				this.addFaceQuad(parEnumFacing, new BreakingFour(quads.get(i), parTextureAtlasSprite));

> CHANGE  4 : 9  @  4 : 7

~ 		private void addGeneralBreakingFours(IBakedModel parIBakedModel,
~ 				EaglerTextureAtlasSprite parTextureAtlasSprite) {
~ 			List<BakedQuad> quads = parIBakedModel.getGeneralQuads();
~ 			for (int i = 0, l = quads.size(); i < l; ++i) {
~ 				this.addGeneralQuad(new BreakingFour(quads.get(i), parTextureAtlasSprite));

> CHANGE  8 : 9  @  8 : 9

~ 			for (int i = 0, l = EnumFacing._VALUES.length; i < l; ++i) {

> CHANGE  18 : 19  @  18 : 19

~ 		public SimpleBakedModel.Builder setTexture(EaglerTextureAtlasSprite parTextureAtlasSprite) {

> EOF
