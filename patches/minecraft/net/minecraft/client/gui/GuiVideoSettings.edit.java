
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 4  @  3 : 9

~ 

> CHANGE  16 : 21  @  21 : 24

~ 			GameSettings.Options.PARTICLES, GameSettings.Options.FXAA, GameSettings.Options.MIPMAP_LEVELS,
~ 			GameSettings.Options.BLOCK_ALTERNATIVES, GameSettings.Options.ENTITY_SHADOWS, GameSettings.Options.FOG,
~ 			GameSettings.Options.HUD_FPS, GameSettings.Options.HUD_COORDS, GameSettings.Options.HUD_PLAYER,
~ 			GameSettings.Options.HUD_STATS, GameSettings.Options.HUD_WORLD, GameSettings.Options.HUD_24H,
~ 			GameSettings.Options.CHUNK_FIX };

> CHANGE  32 : 34  @  35 : 38

~ 		GameSettings.Options[] agamesettings$options = new GameSettings.Options[videoOptions.length];
~ 		int i = 0;

> CHANGE  35 : 38  @  39 : 53

~ 		for (GameSettings.Options gamesettings$options : videoOptions) {
~ 			agamesettings$options[i] = gamesettings$options;
~ 			++i;

> INSERT  40 : 43  @  55

+ 		this.optionsRowList = new GuiOptionsRowList(this.mc, this.width, this.height, 32, this.height - 32, 25,
+ 				agamesettings$options);
+ 

> CHANGE  50 : 51  @  62 : 63

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> CHANGE  60 : 61  @  72 : 73

~ 	protected void mouseClicked(int parInt1, int parInt2, int parInt3) {

> EOF
