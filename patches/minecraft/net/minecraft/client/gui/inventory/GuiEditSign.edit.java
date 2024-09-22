
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 7  @  2 : 3

~ import net.lax1dude.eaglercraft.v1_8.Display;
~ import net.lax1dude.eaglercraft.v1_8.Keyboard;
~ import net.lax1dude.eaglercraft.v1_8.PointerInputAbstraction;
~ import net.lax1dude.eaglercraft.v1_8.minecraft.GuiScreenVisualViewport;
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;

> DELETE  4  @  4 : 5

> INSERT  1 : 2  @  1

+ import net.minecraft.client.renderer.tileentity.TileEntitySignRenderer;

> DELETE  6  @  6 : 7

> CHANGE  1 : 2  @  1 : 2

~ public class GuiEditSign extends GuiScreenVisualViewport {

> CHANGE  28 : 29  @  28 : 29

~ 	public void updateScreen0() {

> CHANGE  3 : 4  @  3 : 4

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> CHANGE  9 : 10  @  9 : 10

~ 	protected void keyTyped(char parChar1, int parInt1) {

> CHANGE  25 : 26  @  25 : 26

~ 	public void drawScreen0(int i, int j, float f) {

> CHANGE  37 : 47  @  37 : 38

~ 		try {
~ 			TileEntitySignRenderer.disableProfanityFilter = true;
~ 			TileEntityRendererDispatcher.instance.renderTileEntityAt(this.tileSign, -0.5D,
~ 					(PointerInputAbstraction.isTouchMode() && (Display.getVisualViewportH() / mc.displayHeight) < 0.75f)
~ 							? -0.25D
~ 							: -0.75D,
~ 					-0.5D, 0.0F);
~ 		} finally {
~ 			TileEntitySignRenderer.disableProfanityFilter = false;
~ 		}

> CHANGE  2 : 3  @  2 : 3

~ 		super.drawScreen0(i, j, f);

> INSERT  1 : 6  @  1

+ 
+ 	public boolean blockPTTKey() {
+ 		return true;
+ 	}
+ 

> EOF
