
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 6

> INSERT  1 : 11  @  1

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

> DELETE  1  @  1 : 5

> DELETE  16  @  16 : 19

> CHANGE  139 : 140  @  139 : 140

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> CHANGE  38 : 39  @  38 : 40

~ 	protected void keyTyped(char parChar1, int parInt1) {

> DELETE  6  @  6 : 7

> CHANGE  129 : 130  @  129 : 130

~ 					} catch (JSONException var13) {

> CHANGE  34 : 35  @  34 : 35

~ 	protected void mouseClicked(int parInt1, int parInt2, int parInt3) {

> EOF
