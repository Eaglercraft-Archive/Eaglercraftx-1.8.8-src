
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> INSERT  2 : 6  @  2

+ 
+ import com.google.common.collect.Lists;
+ import com.google.common.collect.Maps;
+ 

> DELETE  1  @  1 : 6

> CHANGE  34 : 40  @  34 : 35

~ 	public void handleTouchInput() throws IOException {
~ 		super.handleTouchInput();
~ 		this.list.handleTouchInput();
~ 	}
~ 
~ 	protected void actionPerformed(GuiButton parGuiButton) {

> CHANGE  12 : 13  @  12 : 13

~ 					ScaledResolution scaledresolution = this.mc.scaledResolution;

> INSERT  43 : 44  @  43

+ 			this.mc.loadingScreen.eaglerShowRefreshResources();

> INSERT  9 : 10  @  9

+ 			GuiLanguage.this.mc.displayGuiScreen(GuiLanguage.this);

> EOF
