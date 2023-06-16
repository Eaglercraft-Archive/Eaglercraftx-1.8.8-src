
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 3  @  2 : 8

~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;

> DELETE  1  @  1 : 2

> CHANGE  33 : 34  @  33 : 34

~ 	protected void keyTyped(char parChar1, int parInt1) {

> CHANGE  2 : 3  @  2 : 3

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> INSERT  7 : 9  @  7

+ 				this.mc.theWorld.sendQuittingDisconnectingPacket();
+ 				this.mc.loadWorld((WorldClient) null);

> EOF
