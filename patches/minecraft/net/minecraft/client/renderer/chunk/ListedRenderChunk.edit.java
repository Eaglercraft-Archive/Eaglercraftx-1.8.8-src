
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 3  @  2

+ import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;

> DELETE  2  @  2 : 4

> CHANGE  5 : 6  @  5 : 6

~ 	private final int[] baseDisplayList;

> INSERT  3 : 7  @  3

+ 		this.baseDisplayList = new int[EnumWorldBlockLayer.values().length];
+ 		for (int i = 0; i < this.baseDisplayList.length; ++i) {
+ 			this.baseDisplayList[i] = GLAllocation.generateDisplayLists();
+ 		}

> CHANGE  3 : 4  @  3 : 4

~ 		return !parCompiledChunk.isLayerEmpty(layer) ? this.baseDisplayList[layer.ordinal()] : -1;

> CHANGE  4 : 7  @  4 : 5

~ 		for (int i = 0; i < this.baseDisplayList.length; ++i) {
~ 			GLAllocation.deleteDisplayLists(this.baseDisplayList[i]);
~ 		}

> INSERT  1 : 11  @  1

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
