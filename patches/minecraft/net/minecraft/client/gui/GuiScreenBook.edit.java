
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 6

> INSERT  1 : 12  @  1

+ 
+ import org.json.JSONException;
+ 
+ import com.google.common.collect.Lists;
+ 
+ import net.lax1dude.eaglercraft.v1_8.netty.Unpooled;
+ import net.lax1dude.eaglercraft.v1_8.Keyboard;
+ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
+ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
+ import net.lax1dude.eaglercraft.v1_8.minecraft.GuiScreenVisualViewport;
+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;

> DELETE  1  @  1 : 5

> DELETE  16  @  16 : 19

> CHANGE  1 : 2  @  1 : 2

~ public class GuiScreenBook extends GuiScreenVisualViewport {

> CHANGE  47 : 49  @  47 : 49

~ 	public void updateScreen0() {
~ 		super.updateScreen0();

> CHANGE  88 : 89  @  88 : 89

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> CHANGE  38 : 39  @  38 : 40

~ 	protected void keyTyped(char parChar1, int parInt1) {

> DELETE  6  @  6 : 7

> CHANGE  78 : 79  @  78 : 79

~ 	public void drawScreen0(int i, int j, float f) {

> CHANGE  50 : 51  @  50 : 51

~ 					} catch (JSONException var13) {

> CHANGE  31 : 32  @  31 : 32

~ 		super.drawScreen0(i, j, f);

> CHANGE  2 : 3  @  2 : 3

~ 	protected void mouseClicked0(int parInt1, int parInt2, int parInt3) {

> CHANGE  7 : 8  @  7 : 8

~ 		super.mouseClicked0(parInt1, parInt2, parInt3);

> CHANGE  2 : 3  @  2 : 3

~ 	public boolean handleComponentClick(IChatComponent ichatcomponent) {

> INSERT  91 : 95  @  91

+ 
+ 	public boolean blockPTTKey() {
+ 		return this.bookIsUnsigned;
+ 	}

> EOF
