
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 6

> INSERT  1 : 11  @  5

+ 
+ import org.json.JSONException;
+ 
+ import com.google.common.collect.Lists;
+ 
+ import net.lax1dude.eaglercraft.v1_8.netty.Unpooled;
+ import net.lax1dude.eaglercraft.v1_8.Keyboard;
+ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
+ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;

> DELETE  11  @  1 : 5

> DELETE  16  @  20 : 23

> CHANGE  139 : 140  @  142 : 143

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> CHANGE  39 : 40  @  39 : 41

~ 	protected void keyTyped(char parChar1, int parInt1) {

> DELETE  7  @  8 : 9

> CHANGE  129 : 130  @  130 : 131

~ 					} catch (JSONException var13) {

> CHANGE  35 : 36  @  35 : 36

~ 	protected void mouseClicked(int parInt1, int parInt2, int parInt3) {

> EOF
