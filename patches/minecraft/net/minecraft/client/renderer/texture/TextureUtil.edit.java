
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 4  @  2 : 3

~ import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;
~ 

> CHANGE  6 : 14  @  5 : 7

~ import net.lax1dude.eaglercraft.v1_8.internal.buffer.IntBuffer;
~ 
~ import net.lax1dude.eaglercraft.v1_8.IOUtils;
~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
~ import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
~ import net.lax1dude.eaglercraft.v1_8.opengl.ImageData;

> DELETE  16  @  9 : 11

> DELETE  18  @  13 : 17

> CHANGE  34 : 35  @  33 : 34

~ 	public static int uploadTextureImage(int parInt1, ImageData parBufferedImage) {

> CHANGE  155 : 157  @  154 : 155

~ 			EaglercraftGPU.glTexSubImage2D(GL_TEXTURE_2D, parInt1, parInt4, parInt5 + k, parInt2, l, GL_RGBA,
~ 					GL_UNSIGNED_BYTE, dataBuffer);

> CHANGE  161 : 162  @  159 : 160

~ 	public static int uploadTextureImageAllocate(int parInt1, ImageData parBufferedImage, boolean parFlag,

> CHANGE  163 : 164  @  161 : 162

~ 		allocateTexture(parInt1, parBufferedImage.width, parBufferedImage.height);

> CHANGE  172 : 173  @  170 : 171

~ 		// deleteTexture(parInt1); //TODO: why

> CHANGE  175 : 179  @  173 : 177

~ 			EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, '\u813d', parInt2);
~ 			EaglercraftGPU.glTexParameterf(GL_TEXTURE_2D, '\u813a', 0.0F);
~ 			EaglercraftGPU.glTexParameterf(GL_TEXTURE_2D, '\u813b', (float) parInt2);
~ 			// EaglercraftGPU.glTexParameterf(GL_TEXTURE_2D, '\u8501', 0.0F);

> CHANGE  182 : 184  @  180 : 181

~ 			EaglercraftGPU.glTexImage2D(GL_TEXTURE_2D, i, GL_RGBA, parInt3 >> i, parInt4 >> i, 0, GL_RGBA,
~ 					GL_UNSIGNED_BYTE, (IntBuffer) null);

> CHANGE  188 : 189  @  185 : 186

~ 	public static int uploadTextureImageSub(int textureId, ImageData parBufferedImage, int parInt2, int parInt3,

> CHANGE  195 : 199  @  192 : 196

~ 	private static void uploadTextureImageSubImpl(ImageData parBufferedImage, int parInt1, int parInt2, boolean parFlag,
~ 			boolean parFlag2) {
~ 		int i = parBufferedImage.width;
~ 		int j = parBufferedImage.height;

> CHANGE  210 : 212  @  207 : 208

~ 			EaglercraftGPU.glTexSubImage2D(GL_TEXTURE_2D, 0, parInt1, parInt2 + i1, i, j1, GL_RGBA, GL_UNSIGNED_BYTE,
~ 					dataBuffer);

> CHANGE  218 : 220  @  214 : 216

~ 			EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
~ 			EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

> CHANGE  221 : 223  @  217 : 219

~ 			EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
~ 			EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

> CHANGE  233 : 235  @  229 : 231

~ 			EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, parFlag2 ? 9987 : 9729);
~ 			EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

> CHANGE  236 : 238  @  232 : 234

~ 			EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, parFlag2 ? 9986 : 9728);
~ 			EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

> CHANGE  263 : 264  @  259 : 265

~ 		return readBufferedImage(resourceManager.getResource(imageLocation).getInputStream()).pixels;

> CHANGE  266 : 268  @  267 : 269

~ 	public static ImageData readBufferedImage(InputStream imageStream) throws IOException {
~ 		ImageData bufferedimage;

> CHANGE  269 : 270  @  270 : 271

~ 			bufferedimage = ImageData.loadImageFile(imageStream);

> INSERT  310 : 318  @  311

+ 	public static int[] convertComponentOrder(int[] arr) {
+ 		for (int i = 0; i < arr.length; ++i) {
+ 			int j = arr[i];
+ 			arr[i] = (j & 0xFF000000) | ((j >> 16) & 0xFF) | (j & 0xFF00) | ((j << 16) & 0xFF0000);
+ 		}
+ 		return arr;
+ 	}
+ 

> EOF
