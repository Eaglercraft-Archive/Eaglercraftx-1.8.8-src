
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 3  @  2 : 7

~ import net.lax1dude.eaglercraft.v1_8.EagRuntime;

> INSERT  8 : 14  @  8

+ 			GameSettings.Options.CHAT_WIDTH, GameSettings.Options.REDUCED_DEBUG_INFO,
+ 			GameSettings.Options.EAGLER_PROFANITY_FILTER };
+ 	private static final GameSettings.Options[] no_profanity_filter = new GameSettings.Options[] {
+ 			GameSettings.Options.CHAT_VISIBILITY, GameSettings.Options.CHAT_COLOR, GameSettings.Options.CHAT_LINKS,
+ 			GameSettings.Options.CHAT_OPACITY, GameSettings.Options.CHAT_LINKS_PROMPT, GameSettings.Options.CHAT_SCALE,
+ 			GameSettings.Options.CHAT_HEIGHT_FOCUSED, GameSettings.Options.CHAT_HEIGHT_UNFOCUSED,

> CHANGE  14 : 18  @  14 : 15

~ 		boolean profanityFilterForce = EagRuntime.getConfiguration().isForceProfanityFilter();
~ 		GameSettings.Options[] opts = profanityFilterForce ? no_profanity_filter : field_146399_a;
~ 		for (int j = 0; j < opts.length; ++j) {
~ 			GameSettings.Options gamesettings$options = opts[j];

> CHANGE  12 : 14  @  12 : 14

~ 		this.buttonList.add(new GuiButton(200, this.width / 2 - 100,
~ 				this.height / 6 + (profanityFilterForce ? 130 : 154), I18n.format("gui.done", new Object[0])));

> CHANGE  2 : 3  @  2 : 3

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> EOF
