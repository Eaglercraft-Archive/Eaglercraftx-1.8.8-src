
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 3  @  2 : 8

~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;

> DELETE  4  @  9 : 10

> CHANGE  37 : 38  @  43 : 44

~ 	protected void keyTyped(char parChar1, int parInt1) {

> CHANGE  40 : 41  @  46 : 47

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> INSERT  48 : 50  @  54

+ 				this.mc.theWorld.sendQuittingDisconnectingPacket();
+ 				this.mc.loadWorld((WorldClient) null);

> EOF
