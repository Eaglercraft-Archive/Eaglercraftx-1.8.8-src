
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> CHANGE  1 : 4  @  2 : 3

~ 
~ import com.google.common.collect.Lists;
~ 

> CHANGE  4 : 5  @  2 : 4

~ import net.minecraft.util.EnumWorldBlockLayer;

> DELETE  4  @  5 : 6

> INSERT  6 : 8  @  7

+ 	public long goddamnFuckingTimeout = 0l;
+ 	public long time = 0;

> CHANGE  33 : 34  @  31 : 39

~ 		this.status = statusIn;

> CHANGE  4 : 8  @  11 : 12

~ 		if (this.type == ChunkCompileTaskGenerator.Type.REBUILD_CHUNK
~ 				&& this.status != ChunkCompileTaskGenerator.Status.DONE) {
~ 			this.renderChunk.setNeedsUpdate(true);
~ 		}

> CHANGE  5 : 7  @  2 : 7

~ 		this.finished = true;
~ 		this.status = ChunkCompileTaskGenerator.Status.DONE;

> CHANGE  3 : 5  @  6 : 14

~ 		for (Runnable runnable : this.listFinishRunnables) {
~ 			runnable.run();

> DELETE  3  @  9 : 10

> CHANGE  3 : 6  @  4 : 13

~ 		this.listFinishRunnables.add(parRunnable);
~ 		if (this.finished) {
~ 			parRunnable.run();

> DELETE  4  @  10 : 11

> DELETE  2  @  3 : 7

> INSERT  8 : 16  @  12

+ 	public boolean canExecuteYet() {
+ 		if (this.type == ChunkCompileTaskGenerator.Type.RESORT_TRANSPARENCY) {
+ 			return !this.renderChunk.getCompiledChunk().isLayerEmpty(EnumWorldBlockLayer.TRANSLUCENT);
+ 		} else {
+ 			return true;
+ 		}
+ 	}
+ 

> EOF
