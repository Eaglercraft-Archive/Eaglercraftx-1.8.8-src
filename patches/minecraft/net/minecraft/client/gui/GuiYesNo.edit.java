
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> CHANGE  1 : 4  @  1 : 5

~ 
~ import com.google.common.collect.Lists;
~ 

> INSERT  11 : 12  @  11

+ 	private boolean opaqueBackground = false;

> CHANGE  28 : 29  @  28 : 29

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> CHANGE  4 : 9  @  4 : 5

~ 		if (opaqueBackground) {
~ 			this.drawBackground(0);
~ 		} else {
~ 			this.drawDefaultBackground();
~ 		}

> INSERT  29 : 34  @  29

+ 
+ 	public GuiYesNo withOpaqueBackground() {
+ 		opaqueBackground = true;
+ 		return this;
+ 	}

> EOF
