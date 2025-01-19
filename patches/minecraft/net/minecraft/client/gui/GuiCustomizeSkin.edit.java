
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 5

> INSERT  7 : 9  @  7

+ 	private GuiButton enableFNAWSkinsButton;
+ 

> CHANGE  8 : 11  @  8 : 9

~ 		EnumPlayerModelParts[] parts = EnumPlayerModelParts._VALUES;
~ 		for (int k = 0; k < parts.length; ++k) {
~ 			EnumPlayerModelParts enumplayermodelparts = parts[k];

> CHANGE  10 : 14  @  10 : 11

~ 		this.buttonList.add(enableFNAWSkinsButton = new GuiButton(201, this.width / 2 - 100,
~ 				this.height / 6 + 10 + 24 * (i >> 1), I18n.format("options.skinCustomisation.enableFNAWSkins") + ": "
~ 						+ I18n.format(mc.gameSettings.enableFNAWSkins ? "options.on" : "options.off")));
~ 		this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 40 + 24 * (i >> 1),

> CHANGE  3 : 4  @  3 : 4

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> INSERT  4 : 9  @  4

+ 			} else if (parGuiButton.id == 201) {
+ 				mc.gameSettings.enableFNAWSkins = !mc.gameSettings.enableFNAWSkins;
+ 				mc.getRenderManager().setEnableFNAWSkins(mc.getEnableFNAWSkins());
+ 				enableFNAWSkinsButton.displayString = I18n.format("options.skinCustomisation.enableFNAWSkins") + ": "
+ 						+ I18n.format(mc.gameSettings.enableFNAWSkins ? "options.on" : "options.off");

> CHANGE  23 : 28  @  23 : 24

~ 		/*
~ 		 * TODO: I changed this to getUnformattedText() from getFormattedText() because
~ 		 * the latter was returning a pink formatting code at the end for no reason
~ 		 */
~ 		return playerModelParts.func_179326_d().getUnformattedText() + ": " + s;

> EOF
