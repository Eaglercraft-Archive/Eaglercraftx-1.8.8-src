
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> CHANGE  3 : 6  @  5 : 9

~ 
~ import com.google.common.collect.Lists;
~ 

> INSERT  17 : 18  @  20

+ 	private boolean opaqueBackground = false;

> CHANGE  46 : 47  @  48 : 49

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> CHANGE  51 : 56  @  53 : 54

~ 		if (opaqueBackground) {
~ 			this.drawBackground(0);
~ 		} else {
~ 			this.drawDefaultBackground();
~ 		}

> INSERT  85 : 90  @  83

+ 
+ 	public GuiYesNo withOpaqueBackground() {
+ 		opaqueBackground = true;
+ 		return this;
+ 	}

> EOF
