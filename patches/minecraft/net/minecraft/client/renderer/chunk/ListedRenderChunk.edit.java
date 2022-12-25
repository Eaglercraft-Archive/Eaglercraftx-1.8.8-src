
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 3  @  2

+ import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;

> DELETE  5  @  4 : 6

> CHANGE  10 : 11  @  11 : 12

~ 	private final int[] baseDisplayList;

> INSERT  14 : 18  @  15

+ 		this.baseDisplayList = new int[EnumWorldBlockLayer.values().length];
+ 		for (int i = 0; i < this.baseDisplayList.length; ++i) {
+ 			this.baseDisplayList[i] = GLAllocation.generateDisplayLists();
+ 		}

> CHANGE  21 : 22  @  18 : 19

~ 		return !parCompiledChunk.isLayerEmpty(layer) ? this.baseDisplayList[layer.ordinal()] : -1;

> CHANGE  26 : 29  @  23 : 24

~ 		for (int i = 0; i < this.baseDisplayList.length; ++i) {
~ 			GLAllocation.deleteDisplayLists(this.baseDisplayList[i]);
~ 		}

> INSERT  30 : 40  @  25

+ 
+ 	public void rebuildChunk(float x, float y, float z, ChunkCompileTaskGenerator generator) {
+ 		super.rebuildChunk(x, y, z, generator);
+ 		EnumWorldBlockLayer[] layers = EnumWorldBlockLayer.values();
+ 		for (int i = 0; i < layers.length; ++i) {
+ 			if (generator.getCompiledChunk().isLayerEmpty(layers[i])) {
+ 				EaglercraftGPU.flushDisplayList(this.baseDisplayList[i]);
+ 			}
+ 		}
+ 	}

> EOF
