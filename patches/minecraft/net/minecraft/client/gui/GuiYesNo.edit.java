
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> CHANGE  1 : 4  @  3 : 7

~ 
~ import com.google.common.collect.Lists;
~ 

> INSERT  14 : 15  @  15

+ 	private boolean opaqueBackground = false;

> CHANGE  29 : 30  @  28 : 29

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> CHANGE  5 : 10  @  5 : 6

~ 		if (opaqueBackground) {
~ 			this.drawBackground(0);
~ 		} else {
~ 			this.drawDefaultBackground();
~ 		}

> INSERT  34 : 39  @  30

+ 
+ 	public GuiYesNo withOpaqueBackground() {
+ 		opaqueBackground = true;
+ 		return this;
+ 	}

> EOF
