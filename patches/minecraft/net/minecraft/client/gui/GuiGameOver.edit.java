
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 3  @  2 : 8

~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;

> DELETE  2  @  7 : 8

> CHANGE  33 : 34  @  34 : 35

~ 	protected void keyTyped(char parChar1, int parInt1) {

> CHANGE  3 : 4  @  3 : 4

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> INSERT  8 : 10  @  8

+ 				this.mc.theWorld.sendQuittingDisconnectingPacket();
+ 				this.mc.loadWorld((WorldClient) null);

> EOF
