
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> INSERT  2 : 6  @  4

+ 
+ import com.google.common.collect.Lists;
+ import com.google.common.collect.Maps;
+ 

> DELETE  5  @  1 : 6

> CHANGE  34 : 35  @  39 : 40

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> INSERT  57 : 59  @  57

+ 			this.mc.loadingScreen.eaglerShow(I18n.format("resourcePack.load.refreshing"),
+ 					I18n.format("resourcePack.load.pleaseWait"));

> INSERT  11 : 12  @  9

+ 			GuiLanguage.this.mc.displayGuiScreen(GuiLanguage.this);

> EOF
