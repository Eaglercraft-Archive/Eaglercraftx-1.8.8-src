
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 5

> CHANGE  30 : 31  @  30 : 31

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> CHANGE  27 : 32  @  27 : 28

~ 		/*
~ 		 * TODO: I changed this to getUnformattedText() from getFormattedText() because
~ 		 * the latter was returning a pink formatting code at the end for no reason
~ 		 */
~ 		return playerModelParts.func_179326_d().getUnformattedText() + ": " + s;

> EOF
