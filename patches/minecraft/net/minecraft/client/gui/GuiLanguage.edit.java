
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> INSERT  4 : 8  @  6

+ 
+ import com.google.common.collect.Lists;
+ import com.google.common.collect.Maps;
+ 

> DELETE  9  @  7 : 12

> CHANGE  43 : 44  @  46 : 47

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> INSERT  100 : 102  @  103

+ 			this.mc.loadingScreen.eaglerShow(I18n.format("resourcePack.load.refreshing"),
+ 					I18n.format("resourcePack.load.pleaseWait"));

> INSERT  111 : 112  @  112

+ 			GuiLanguage.this.mc.displayGuiScreen(GuiLanguage.this);

> EOF
