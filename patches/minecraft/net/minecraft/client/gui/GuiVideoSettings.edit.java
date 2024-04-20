
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 4  @  3 : 9

~ 

> INSERT  8 : 11  @  8

+ 	/**
+ 	 * + An array of all of GameSettings.Options's video options.
+ 	 */

> CHANGE  2 : 10  @  2 : 7

~ 			GameSettings.Options.FRAMERATE_LIMIT, GameSettings.Options.EAGLER_VSYNC, GameSettings.Options.ANAGLYPH,
~ 			GameSettings.Options.VIEW_BOBBING, GameSettings.Options.GUI_SCALE, GameSettings.Options.GAMMA,
~ 			GameSettings.Options.RENDER_CLOUDS, GameSettings.Options.PARTICLES, GameSettings.Options.FXAA,
~ 			GameSettings.Options.MIPMAP_LEVELS, GameSettings.Options.BLOCK_ALTERNATIVES,
~ 			GameSettings.Options.ENTITY_SHADOWS, GameSettings.Options.FOG, GameSettings.Options.FULLSCREEN,
~ 			GameSettings.Options.FNAW_SKINS, GameSettings.Options.HUD_FPS, GameSettings.Options.HUD_COORDS,
~ 			GameSettings.Options.HUD_PLAYER, GameSettings.Options.HUD_STATS, GameSettings.Options.HUD_WORLD,
~ 			GameSettings.Options.HUD_24H, GameSettings.Options.CHUNK_FIX };

> CHANGE  11 : 13  @  11 : 31

~ 		this.optionsRowList = new GuiOptionsRowList(this.mc, this.width, this.height, 32, this.height - 32, 25,
~ 				videoOptions);

> CHANGE  7 : 8  @  7 : 8

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> CHANGE  9 : 10  @  9 : 10

~ 	protected void mouseClicked(int parInt1, int parInt2, int parInt3) {

> INSERT  8 : 9  @  8

+ 			this.mc.voiceOverlay.setResolution(j, k);

> EOF
