
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 3  @  2 : 3

~ import java.util.Arrays;

> CHANGE  1 : 5  @  1 : 3

~ 
~ import com.google.common.collect.Lists;
~ 
~ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;

> CHANGE  5 : 6  @  5 : 6

~ 	public static final CompiledChunk DUMMY = new CompiledChunk(null) {

> CHANGE  9 : 10  @  9 : 10

~ 			return true;

> CHANGE  2 : 5  @  2 : 4

~ 	private final RenderChunk chunk;
~ 	private final boolean[] layersUsed = new boolean[EnumWorldBlockLayer._VALUES.length];
~ 	private final boolean[] layersStarted = new boolean[EnumWorldBlockLayer._VALUES.length];

> INSERT  4 : 5  @  4

+ 	private WorldRenderer.State stateWater;

> INSERT  1 : 15  @  1

+ 	public CompiledChunk(RenderChunk chunk) {
+ 		this.chunk = chunk;
+ 	}
+ 
+ 	public void reset() {
+ 		Arrays.fill(layersUsed, false);
+ 		Arrays.fill(layersStarted, false);
+ 		empty = true;
+ 		tileEntities.clear();
+ 		setVisibility.setAllVisible(false);
+ 		setState(null);
+ 		setStateRealisticWater(null);
+ 	}
+ 

> INSERT  42 : 45  @  42

+ 		if (this.state != stateIn && this.state != null) {
+ 			this.state.release();
+ 		}

> INSERT  2 : 13  @  2

+ 
+ 	public WorldRenderer.State getStateRealisticWater() {
+ 		return this.stateWater;
+ 	}
+ 
+ 	public void setStateRealisticWater(WorldRenderer.State stateIn) {
+ 		if (this.stateWater != stateIn && this.stateWater != null) {
+ 			this.stateWater.release();
+ 		}
+ 		this.stateWater = stateIn;
+ 	}

> EOF
