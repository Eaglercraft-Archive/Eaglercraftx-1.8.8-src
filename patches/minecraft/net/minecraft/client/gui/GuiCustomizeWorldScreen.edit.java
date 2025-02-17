
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  6 : 11  @  6 : 16

~ 
~ import net.lax1dude.eaglercraft.v1_8.HString;
~ import net.lax1dude.eaglercraft.v1_8.minecraft.EnumInputEvent;
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
~ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;

> DELETE  1  @  1 : 2

> INSERT  89 : 94  @  89

+ 	public void handleTouchInput() throws IOException {
+ 		super.handleTouchInput();
+ 		this.field_175349_r.handleTouchInput();
+ 	}
+ 

> CHANGE  258 : 259  @  258 : 259

~ 						HString.format("%5.3f", new Object[] { Float.valueOf(this.field_175336_F.mainNoiseScaleX) }),

> CHANGE  4 : 5  @  4 : 5

~ 						HString.format("%5.3f", new Object[] { Float.valueOf(this.field_175336_F.mainNoiseScaleY) }),

> CHANGE  4 : 5  @  4 : 5

~ 						HString.format("%5.3f", new Object[] { Float.valueOf(this.field_175336_F.mainNoiseScaleZ) }),

> CHANGE  4 : 5  @  4 : 5

~ 						HString.format("%5.3f", new Object[] { Float.valueOf(this.field_175336_F.depthNoiseScaleX) }),

> CHANGE  4 : 5  @  4 : 5

~ 						HString.format("%5.3f", new Object[] { Float.valueOf(this.field_175336_F.depthNoiseScaleZ) }),

> CHANGE  5 : 6  @  5 : 6

~ 						HString.format("%2.3f",

> CHANGE  5 : 6  @  5 : 6

~ 						HString.format("%2.3f", new Object[] { Float.valueOf(this.field_175336_F.baseSize) }), false,

> CHANGE  4 : 5  @  4 : 5

~ 						HString.format("%5.3f", new Object[] { Float.valueOf(this.field_175336_F.coordinateScale) }),

> CHANGE  4 : 5  @  4 : 5

~ 						HString.format("%5.3f", new Object[] { Float.valueOf(this.field_175336_F.heightScale) }), false,

> CHANGE  4 : 5  @  4 : 5

~ 						HString.format("%2.3f", new Object[] { Float.valueOf(this.field_175336_F.stretchY) }), false,

> CHANGE  4 : 5  @  4 : 5

~ 						HString.format("%5.3f", new Object[] { Float.valueOf(this.field_175336_F.upperLimitScale) }),

> CHANGE  4 : 5  @  4 : 5

~ 						HString.format("%5.3f", new Object[] { Float.valueOf(this.field_175336_F.lowerLimitScale) }),

> CHANGE  4 : 5  @  4 : 5

~ 						HString.format("%2.3f", new Object[] { Float.valueOf(this.field_175336_F.biomeDepthWeight) }),

> CHANGE  4 : 5  @  4 : 5

~ 						HString.format("%2.3f", new Object[] { Float.valueOf(this.field_175336_F.biomeDepthOffset) }),

> CHANGE  4 : 5  @  4 : 5

~ 						HString.format("%2.3f", new Object[] { Float.valueOf(this.field_175336_F.biomeScaleWeight) }),

> CHANGE  4 : 5  @  4 : 5

~ 						HString.format("%2.3f", new Object[] { Float.valueOf(this.field_175336_F.biomeScaleOffset) }),

> CHANGE  126 : 127  @  126 : 127

~ 			return HString.format("%5.3f", new Object[] { Float.valueOf(parFloat1) });

> CHANGE  14 : 15  @  14 : 15

~ 			return HString.format("%2.3f", new Object[] { Float.valueOf(parFloat1) });

> CHANGE  31 : 32  @  31 : 32

~ 			return HString.format("%d", new Object[] { Integer.valueOf((int) parFloat1) });

> CHANGE  316 : 317  @  316 : 317

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> CHANGE  74 : 75  @  74 : 75

~ 	private void func_175331_h() {

> CHANGE  35 : 36  @  35 : 36

~ 	protected void keyTyped(char parChar1, int parInt1) {

> CHANGE  44 : 45  @  44 : 45

~ 	protected void mouseClicked(int parInt1, int parInt2, int parInt3) {

> INSERT  59 : 70  @  59

+ 
+ 	@Override
+ 	public boolean showCopyPasteButtons() {
+ 		return field_175349_r.isTextFieldFocused();
+ 	}
+ 
+ 	@Override
+ 	public void fireInputEvent(EnumInputEvent event, String param) {
+ 		field_175349_r.fireInputEvent(event, param);
+ 	}
+ 

> EOF
