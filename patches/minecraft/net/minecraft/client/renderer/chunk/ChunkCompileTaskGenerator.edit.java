
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> CHANGE  3 : 6  @  4 : 5

~ 
~ import com.google.common.collect.Lists;
~ 

> CHANGE  7 : 8  @  6 : 8

~ import net.minecraft.util.EnumWorldBlockLayer;

> DELETE  11  @  11 : 12

> INSERT  17 : 19  @  18

+ 	public long goddamnFuckingTimeout = 0l;
+ 	public long time = 0;

> CHANGE  50 : 51  @  49 : 57

~ 		this.status = statusIn;

> CHANGE  54 : 58  @  60 : 61

~ 		if (this.type == ChunkCompileTaskGenerator.Type.REBUILD_CHUNK
~ 				&& this.status != ChunkCompileTaskGenerator.Status.DONE) {
~ 			this.renderChunk.setNeedsUpdate(true);
~ 		}

> CHANGE  59 : 61  @  62 : 67

~ 		this.finished = true;
~ 		this.status = ChunkCompileTaskGenerator.Status.DONE;

> CHANGE  62 : 64  @  68 : 76

~ 		for (Runnable runnable : this.listFinishRunnables) {
~ 			runnable.run();

> DELETE  65  @  77 : 78

> CHANGE  68 : 71  @  81 : 90

~ 		this.listFinishRunnables.add(parRunnable);
~ 		if (this.finished) {
~ 			parRunnable.run();

> DELETE  72  @  91 : 92

> DELETE  74  @  94 : 98

> INSERT  82 : 90  @  106

+ 	public boolean canExecuteYet() {
+ 		if (this.type == ChunkCompileTaskGenerator.Type.RESORT_TRANSPARENCY) {
+ 			return !this.renderChunk.getCompiledChunk().isLayerEmpty(EnumWorldBlockLayer.TRANSLUCENT);
+ 		} else {
+ 			return true;
+ 		}
+ 	}
+ 

> EOF
