
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

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

> CHANGE  3 : 5  @  3 : 5

~ 		for (int l = 0, m = this.field_175298_s.size(); l < m; ++l) {
~ 			this.drawCenteredString(this.fontRendererObj, this.field_175298_s.get(l), this.width / 2, k, 16777215);

> CHANGE  9 : 11  @  9 : 11

~ 		for (int l = 0, m = this.buttonList.size(); l < m; ++l) {
~ 			this.buttonList.get(l).enabled = false;

> CHANGE  7 : 9  @  7 : 9

~ 			for (int l = 0, m = this.buttonList.size(); l < m; ++l) {
~ 				this.buttonList.get(l).enabled = true;

> INSERT  4 : 9  @  4

+ 
+ 	public GuiYesNo withOpaqueBackground() {
+ 		opaqueBackground = true;
+ 		return this;
+ 	}

> EOF
