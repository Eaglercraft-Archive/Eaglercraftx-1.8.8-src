
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 6

> INSERT  3 : 13  @  7

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

> DELETE  14  @  8 : 12

> DELETE  30  @  28 : 31

> CHANGE  169 : 170  @  170 : 171

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> CHANGE  208 : 209  @  209 : 211

~ 	protected void keyTyped(char parChar1, int parInt1) {

> DELETE  215  @  217 : 218

> CHANGE  344 : 345  @  347 : 348

~ 					} catch (JSONException var13) {

> CHANGE  379 : 380  @  382 : 383

~ 	protected void mouseClicked(int parInt1, int parInt2, int parInt3) {

> EOF
