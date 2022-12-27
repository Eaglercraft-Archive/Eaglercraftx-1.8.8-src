
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 6

> CHANGE  6 : 13  @  10 : 12

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 
~ import net.lax1dude.eaglercraft.v1_8.HString;
~ import net.lax1dude.eaglercraft.v1_8.IOUtils;
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
~ import net.lax1dude.eaglercraft.v1_8.opengl.ImageData;
~ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;

> DELETE  14  @  13 : 14

> DELETE  15  @  15 : 16

> DELETE  22  @  23 : 25

> CHANGE  24 : 26  @  27 : 29

~ 	protected static final ResourceLocation[] unicodePageLocations = new ResourceLocation[256];
~ 	protected int[] charWidth = new int[256];

> CHANGE  27 : 46  @  30 : 49

~ 	public EaglercraftRandom fontRandom = new EaglercraftRandom();
~ 	protected byte[] glyphWidth = new byte[65536];
~ 	protected int[] colorCode = new int[32];
~ 	protected final ResourceLocation locationFontTexture;
~ 	protected final TextureManager renderEngine;
~ 	protected float posX;
~ 	protected float posY;
~ 	protected boolean unicodeFlag;
~ 	protected boolean bidiFlag;
~ 	protected float red;
~ 	protected float blue;
~ 	protected float green;
~ 	protected float alpha;
~ 	protected int textColor;
~ 	protected boolean randomStyle;
~ 	protected boolean boldStyle;
~ 	protected boolean italicStyle;
~ 	protected boolean underlineStyle;
~ 	protected boolean strikethroughStyle;

> CHANGE  89 : 90  @  92 : 93

~ 		ImageData bufferedimage;

> CHANGE  97 : 100  @  100 : 104

~ 		int i = bufferedimage.width;
~ 		int j = bufferedimage.height;
~ 		int[] aint = bufferedimage.pixels;

> CHANGE  168 : 187  @  172 : 182

~ 		Tessellator tessellator = Tessellator.getInstance();
~ 		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
~ 
~ 		worldrenderer.begin(Tessellator.GL_TRIANGLE_STRIP, DefaultVertexFormats.POSITION_TEX);
~ 
~ 		worldrenderer.pos(this.posX + (float) k, this.posY, 0.0F).tex((float) i / 128.0F, (float) j / 128.0F)
~ 				.endVertex();
~ 
~ 		worldrenderer.pos(this.posX - (float) k, this.posY + 7.99F, 0.0F)
~ 				.tex((float) i / 128.0F, ((float) j + 7.99F) / 128.0F).endVertex();
~ 
~ 		worldrenderer.pos(this.posX + f - 1.0F + (float) k, this.posY, 0.0F)
~ 				.tex(((float) i + f - 1.0F) / 128.0F, (float) j / 128.0F).endVertex();
~ 
~ 		worldrenderer.pos(this.posX + f - 1.0F - (float) k, this.posY + 7.99F, 0.0F)
~ 				.tex(((float) i + f - 1.0F) / 128.0F, ((float) j + 7.99F) / 128.0F).endVertex();
~ 
~ 		tessellator.draw();
~ 

> CHANGE  193 : 194  @  188 : 189

~ 					HString.format("textures/font/unicode_page_%02x.png", new Object[] { Integer.valueOf(parInt1) }));

> CHANGE  217 : 235  @  212 : 222

~ 			Tessellator tessellator = Tessellator.getInstance();
~ 			WorldRenderer worldrenderer = tessellator.getWorldRenderer();
~ 
~ 			worldrenderer.begin(Tessellator.GL_TRIANGLE_STRIP, DefaultVertexFormats.POSITION_TEX);
~ 
~ 			worldrenderer.pos(this.posX + f5, this.posY, 0.0F).tex(f2 / 256.0F, f3 / 256.0F).endVertex();
~ 
~ 			worldrenderer.pos(this.posX - f5, this.posY + 7.99F, 0.0F).tex(f2 / 256.0F, (f3 + 15.98F) / 256.0F)
~ 					.endVertex();
~ 
~ 			worldrenderer.pos(this.posX + f4 / 2.0F + f5, this.posY, 0.0F).tex((f2 + f4) / 256.0F, f3 / 256.0F)
~ 					.endVertex();
~ 
~ 			worldrenderer.pos(this.posX + f4 / 2.0F - f5, this.posY + 7.99F, 0.0F)
~ 					.tex((f2 + f4) / 256.0F, (f3 + 15.98F) / 256.0F).endVertex();
~ 
~ 			tessellator.draw();
~ 

> CHANGE  262 : 270  @  249 : 256

~ //		try {
~ //			Bidi bidi = new Bidi((new ArabicShaping(8)).shape(parString1), 127);
~ //			bidi.setReorderingMode(0);
~ //			return bidi.writeReordered(2);
~ //		} catch (ArabicShapingException var3) {
~ //			return parString1;
~ //		}
~ 		return parString1;

> CHANGE  272 : 273  @  258 : 259

~ 	protected void resetStyles() {

> CHANGE  280 : 281  @  266 : 267

~ 	protected void renderStringAtPos(String parString1, boolean parFlag) {

> CHANGE  284 : 285  @  270 : 271

~ 				int i1 = "0123456789abcdefklmnor".indexOf(Character.toLowerCase(parString1.charAt(i + 1)));

> CHANGE  418 : 419  @  404 : 405

~ 	private int renderStringAligned(String text, int x, int y, int wrapWidth, int color, boolean parFlag) {

> CHANGE  421 : 422  @  407 : 408

~ 			x = x + wrapWidth - i;

> CHANGE  424 : 425  @  410 : 411

~ 		return this.renderString(text, (float) x, (float) y, color, parFlag);

> CHANGE  429 : 431  @  415 : 416

~ 			this.posX = x;
~ 			this.posY = y;

> DELETE  452  @  437 : 438

> INSERT  453 : 454  @  439

+ 		return (int) this.posX;

> INSERT  573 : 576  @  558

+ 		if ((textColor & -67108864) == 0) {
+ 			textColor |= -16777216;
+ 		}

> CHANGE  606 : 607  @  588 : 589

~ 		return Arrays.asList(this.wrapFormattedStringToWidth(str, wrapWidth, 0).split("\n"));

> CHANGE  609 : 613  @  591 : 592

~ 	String wrapFormattedStringToWidth(String str, int wrapWidth, int depthCheck) { // TODO: fix recursive
~ 		if (depthCheck > 20) {
~ 			return str;
~ 		}

> CHANGE  621 : 622  @  600 : 601

~ 			return s + "\n" + this.wrapFormattedStringToWidth(s1, wrapWidth, ++depthCheck);

> EOF
