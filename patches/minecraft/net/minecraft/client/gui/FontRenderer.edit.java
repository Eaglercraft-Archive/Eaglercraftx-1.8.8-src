
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 6

> CHANGE  4 : 14  @  4 : 6

~ 
~ import net.lax1dude.eaglercraft.v1_8.EaglerBidiReorder;
~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 
~ import net.lax1dude.eaglercraft.v1_8.HString;
~ import net.lax1dude.eaglercraft.v1_8.IOUtils;
~ import net.lax1dude.eaglercraft.v1_8.minecraft.FontMappingHelper;
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

> INSERT  1 : 15  @  1

+ 	protected static char[] codepointLookup = new char[] { 192, 193, 194, 200, 202, 203, 205, 211, 212, 213, 218, 223,
+ 			227, 245, 287, 304, 305, 338, 339, 350, 351, 372, 373, 382, 519, 0, 0, 0, 0, 0, 0, 0, 32, 33, 34, 35, 36,
+ 			37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63,
+ 			64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90,
+ 			91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113,
+ 			114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126, 0, 199, 252, 233, 226, 228, 224, 229, 231,
+ 			234, 235, 232, 239, 238, 236, 196, 197, 201, 230, 198, 244, 246, 242, 251, 249, 255, 214, 220, 248, 163,
+ 			216, 215, 402, 225, 237, 243, 250, 241, 209, 170, 186, 191, 174, 172, 189, 188, 161, 171, 187, 9617, 9618,
+ 			9619, 9474, 9508, 9569, 9570, 9558, 9557, 9571, 9553, 9559, 9565, 9564, 9563, 9488, 9492, 9524, 9516, 9500,
+ 			9472, 9532, 9566, 9567, 9562, 9556, 9577, 9574, 9568, 9552, 9580, 9575, 9576, 9572, 9573, 9561, 9560, 9554,
+ 			9555, 9579, 9578, 9496, 9484, 9608, 9604, 9612, 9616, 9600, 945, 946, 915, 960, 931, 963, 956, 964, 934,
+ 			920, 937, 948, 8734, 8709, 8712, 8745, 8801, 177, 8805, 8804, 8992, 8993, 247, 8776, 176, 8729, 183, 8730,
+ 			8319, 178, 9632, 0 };
+ 

> CHANGE  42 : 43  @  42 : 43

~ 		ImageData bufferedimage;

> CHANGE  7 : 10  @  7 : 11

~ 		int i = bufferedimage.width;
~ 		int j = bufferedimage.height;
~ 		int[] aint = bufferedimage.pixels;

> CHANGE  54 : 55  @  54 : 56

~ 			int i = FontMappingHelper.lookupChar(parChar1, false);

> CHANGE  12 : 31  @  12 : 22

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

> CHANGE  26 : 27  @  26 : 37

~ 	protected void resetStyles() {

> CHANGE  7 : 8  @  7 : 8

~ 	protected void renderStringAtPos(String parString1, boolean parFlag) {

> CHANGE  3 : 4  @  3 : 4

~ 				int i1 = "0123456789abcdefklmnor".indexOf(Character.toLowerCase(parString1.charAt(i + 1)));

> CHANGE  39 : 40  @  39 : 41

~ 				int j = FontMappingHelper.lookupChar(c0, false);

> INSERT  2 : 3  @  2

+ 					char[] chars = FontRenderer.codepointLookup;

> CHANGE  3 : 5  @  3 : 8

~ 						j = this.fontRandom.nextInt(chars.length);
~ 						c1 = chars[j];

> CHANGE  82 : 83  @  82 : 83

~ 	private int renderStringAligned(String text, int x, int y, int wrapWidth, int color, boolean parFlag) {

> CHANGE  1 : 3  @  1 : 3

~ 			int i = this.getStringWidth(EaglerBidiReorder.bidiReorder(text));
~ 			x = x + wrapWidth - i;

> CHANGE  2 : 3  @  2 : 3

~ 		return this.renderString(text, (float) x, (float) y, color, parFlag);

> CHANGE  4 : 6  @  4 : 5

~ 			this.posX = x;
~ 			this.posY = y;

> CHANGE  2 : 3  @  2 : 3

~ 				text = EaglerBidiReorder.bidiReorder(text);

> DELETE  18  @  18 : 19

> INSERT  1 : 2  @  1

+ 		return (int) this.posX;

> CHANGE  42 : 43  @  42 : 44

~ 			int i = FontMappingHelper.lookupChar(character, false);

> INSERT  75 : 78  @  75

+ 		if ((textColor & -67108864) == 0) {
+ 			textColor |= -16777216;
+ 		}

> CHANGE  6 : 9  @  6 : 8

~ 		List<String> lst = this.listFormattedStringToWidth(str, wrapWidth);
~ 		for (int i = 0, l = lst.size(); i < l; ++i) {
~ 			this.renderStringAligned(lst.get(i), x, y, wrapWidth, this.textColor, addShadow);

> CHANGE  22 : 23  @  22 : 23

~ 		return Arrays.asList(this.wrapFormattedStringToWidth(str, wrapWidth, 0).split("\n"));

> CHANGE  2 : 6  @  2 : 3

~ 	String wrapFormattedStringToWidth(String str, int wrapWidth, int depthCheck) { // TODO: fix recursive
~ 		if (depthCheck > 20) {
~ 			return str;
~ 		}

> CHANGE  8 : 9  @  8 : 9

~ 			return s + "\n" + this.wrapFormattedStringToWidth(s1, wrapWidth, ++depthCheck);

> EOF
