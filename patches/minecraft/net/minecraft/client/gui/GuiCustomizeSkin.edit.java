
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 5

> CHANGE  15 : 18  @  15 : 16

~ 		EnumPlayerModelParts[] parts = EnumPlayerModelParts._VALUES;
~ 		for (int k = 0; k < parts.length; ++k) {
~ 			EnumPlayerModelParts enumplayermodelparts = parts[k];

> CHANGE  14 : 15  @  14 : 15

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> CHANGE  27 : 32  @  27 : 28

~ 		/*
~ 		 * TODO: I changed this to getUnformattedText() from getFormattedText() because
~ 		 * the latter was returning a pink formatting code at the end for no reason
~ 		 */
~ 		return playerModelParts.func_179326_d().getUnformattedText() + ": " + s;

> EOF
