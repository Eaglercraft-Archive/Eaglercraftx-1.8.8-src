
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 6

> CHANGE  4 : 11  @  4 : 6

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 
~ import net.lax1dude.eaglercraft.v1_8.HString;
~ import net.lax1dude.eaglercraft.v1_8.IOUtils;
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
~ import net.lax1dude.eaglercraft.v1_8.opengl.ImageData;
~ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;

> DELETE  1  @  1 : 2

> DELETE  1  @  1 : 2

> DELETE  7  @  7 : 9

> CHANGE  2 : 4  @  2 : 4

~ 	protected static final ResourceLocation[] unicodePageLocations = new ResourceLocation[256];
~ 	protected int[] charWidth = new int[256];

> CHANGE  1 : 20  @  1 : 20

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

> CHANGE  43 : 44  @  43 : 44

~ 		ImageData bufferedimage;

> CHANGE  7 : 10  @  7 : 11

~ 		int i = bufferedimage.width;
~ 		int j = bufferedimage.height;
~ 		int[] aint = bufferedimage.pixels;

> CHANGE  68 : 87  @  68 : 78

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

> CHANGE  6 : 7  @  6 : 7

~ 					HString.format("textures/font/unicode_page_%02x.png", new Object[] { Integer.valueOf(parInt1) }));

> CHANGE  23 : 41  @  23 : 33

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

> CHANGE  27 : 35  @  27 : 34

~ //		try {
~ //			Bidi bidi = new Bidi((new ArabicShaping(8)).shape(parString1), 127);
~ //			bidi.setReorderingMode(0);
~ //			return bidi.writeReordered(2);
~ //		} catch (ArabicShapingException var3) {
~ //			return parString1;
~ //		}
~ 		return parString1;

> CHANGE  2 : 3  @  2 : 3

~ 	protected void resetStyles() {

> CHANGE  7 : 8  @  7 : 8

~ 	protected void renderStringAtPos(String parString1, boolean parFlag) {

> CHANGE  3 : 4  @  3 : 4

~ 				int i1 = "0123456789abcdefklmnor".indexOf(Character.toLowerCase(parString1.charAt(i + 1)));

> CHANGE  133 : 134  @  133 : 134

~ 	private int renderStringAligned(String text, int x, int y, int wrapWidth, int color, boolean parFlag) {

> CHANGE  2 : 3  @  2 : 3

~ 			x = x + wrapWidth - i;

> CHANGE  2 : 3  @  2 : 3

~ 		return this.renderString(text, (float) x, (float) y, color, parFlag);

> CHANGE  4 : 6  @  4 : 5

~ 			this.posX = x;
~ 			this.posY = y;

> DELETE  21  @  21 : 22

> INSERT  1 : 2  @  1

+ 		return (int) this.posX;

> INSERT  119 : 122  @  119

+ 		if ((textColor & -67108864) == 0) {
+ 			textColor |= -16777216;
+ 		}

> CHANGE  30 : 31  @  30 : 31

~ 		return Arrays.asList(this.wrapFormattedStringToWidth(str, wrapWidth, 0).split("\n"));

> CHANGE  2 : 6  @  2 : 3

~ 	String wrapFormattedStringToWidth(String str, int wrapWidth, int depthCheck) { // TODO: fix recursive
~ 		if (depthCheck > 20) {
~ 			return str;
~ 		}

> CHANGE  8 : 9  @  8 : 9

~ 			return s + "\n" + this.wrapFormattedStringToWidth(s1, wrapWidth, ++depthCheck);

> EOF
