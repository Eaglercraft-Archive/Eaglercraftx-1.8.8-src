
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> CHANGE  1 : 4  @  1 : 2

~ 
~ import com.google.common.collect.Lists;
~ 

> CHANGE  1 : 2  @  1 : 3

~ import net.minecraft.util.EnumWorldBlockLayer;

> DELETE  3  @  3 : 4

> INSERT  6 : 8  @  6

+ 	public long goddamnFuckingTimeout = 0l;
+ 	public long time = 0;

> CHANGE  31 : 32  @  31 : 39

~ 		this.status = statusIn;

> CHANGE  3 : 7  @  3 : 4

~ 		if (this.type == ChunkCompileTaskGenerator.Type.REBUILD_CHUNK
~ 				&& this.status != ChunkCompileTaskGenerator.Status.DONE) {
~ 			this.renderChunk.setNeedsUpdate(true);
~ 		}

> CHANGE  1 : 3  @  1 : 6

~ 		this.finished = true;
~ 		this.status = ChunkCompileTaskGenerator.Status.DONE;

> CHANGE  1 : 3  @  1 : 9

~ 		for (Runnable runnable : this.listFinishRunnables) {
~ 			runnable.run();

> DELETE  1  @  1 : 2

> CHANGE  3 : 6  @  3 : 12

~ 		this.listFinishRunnables.add(parRunnable);
~ 		if (this.finished) {
~ 			parRunnable.run();

> DELETE  1  @  1 : 2

> DELETE  2  @  2 : 6

> INSERT  8 : 16  @  8

+ 	public boolean canExecuteYet() {
+ 		if (this.type == ChunkCompileTaskGenerator.Type.RESORT_TRANSPARENCY) {
+ 			return !this.renderChunk.getCompiledChunk().isLayerEmpty(EnumWorldBlockLayer.TRANSLUCENT);
+ 		} else {
+ 			return true;
+ 		}
+ 	}
+ 

> EOF
