
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 4  @  3 : 9

~ 

> CHANGE  12 : 17  @  12 : 15

~ 			GameSettings.Options.PARTICLES, GameSettings.Options.FXAA, GameSettings.Options.MIPMAP_LEVELS,
~ 			GameSettings.Options.BLOCK_ALTERNATIVES, GameSettings.Options.ENTITY_SHADOWS, GameSettings.Options.FOG,
~ 			GameSettings.Options.FULLSCREEN, GameSettings.Options.HUD_FPS, GameSettings.Options.HUD_COORDS,
~ 			GameSettings.Options.HUD_PLAYER, GameSettings.Options.HUD_STATS, GameSettings.Options.HUD_WORLD,
~ 			GameSettings.Options.HUD_24H, GameSettings.Options.CHUNK_FIX };

> CHANGE  11 : 13  @  11 : 14

~ 		GameSettings.Options[] agamesettings$options = new GameSettings.Options[videoOptions.length];
~ 		int i = 0;

> CHANGE  1 : 4  @  1 : 15

~ 		for (GameSettings.Options gamesettings$options : videoOptions) {
~ 			agamesettings$options[i] = gamesettings$options;
~ 			++i;

> INSERT  2 : 5  @  2

+ 		this.optionsRowList = new GuiOptionsRowList(this.mc, this.width, this.height, 32, this.height - 32, 25,
+ 				agamesettings$options);
+ 

> CHANGE  7 : 8  @  7 : 8

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> CHANGE  9 : 10  @  9 : 10

~ 	protected void mouseClicked(int parInt1, int parInt2, int parInt3) {

> EOF
