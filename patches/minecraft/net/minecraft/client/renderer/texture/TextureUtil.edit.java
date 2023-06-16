
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 4  @  2 : 3

~ import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;
~ 

> CHANGE  2 : 10  @  2 : 4

~ import net.lax1dude.eaglercraft.v1_8.internal.buffer.IntBuffer;
~ 
~ import net.lax1dude.eaglercraft.v1_8.IOUtils;
~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
~ import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
~ import net.lax1dude.eaglercraft.v1_8.opengl.ImageData;

> DELETE  2  @  2 : 4

> DELETE  2  @  2 : 6

> CHANGE  16 : 17  @  16 : 17

~ 	public static int uploadTextureImage(int parInt1, ImageData parBufferedImage) {

> CHANGE  120 : 122  @  120 : 121

~ 			EaglercraftGPU.glTexSubImage2D(GL_TEXTURE_2D, parInt1, parInt4, parInt5 + k, parInt2, l, GL_RGBA,
~ 					GL_UNSIGNED_BYTE, dataBuffer);

> CHANGE  4 : 5  @  4 : 5

~ 	public static int uploadTextureImageAllocate(int parInt1, ImageData parBufferedImage, boolean parFlag,

> CHANGE  1 : 2  @  1 : 2

~ 		allocateTexture(parInt1, parBufferedImage.width, parBufferedImage.height);

> CHANGE  8 : 9  @  8 : 9

~ 		// deleteTexture(parInt1); //TODO: why

> CHANGE  2 : 6  @  2 : 6

~ 			EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, '\u813d', parInt2);
~ 			EaglercraftGPU.glTexParameterf(GL_TEXTURE_2D, '\u813a', 0.0F);
~ 			EaglercraftGPU.glTexParameterf(GL_TEXTURE_2D, '\u813b', (float) parInt2);
~ 			// EaglercraftGPU.glTexParameterf(GL_TEXTURE_2D, '\u8501', 0.0F);

> CHANGE  3 : 5  @  3 : 4

~ 			EaglercraftGPU.glTexImage2D(GL_TEXTURE_2D, i, GL_RGBA, parInt3 >> i, parInt4 >> i, 0, GL_RGBA,
~ 					GL_UNSIGNED_BYTE, (IntBuffer) null);

> CHANGE  4 : 5  @  4 : 5

~ 	public static int uploadTextureImageSub(int textureId, ImageData parBufferedImage, int parInt2, int parInt3,

> CHANGE  6 : 10  @  6 : 10

~ 	private static void uploadTextureImageSubImpl(ImageData parBufferedImage, int parInt1, int parInt2, boolean parFlag,
~ 			boolean parFlag2) {
~ 		int i = parBufferedImage.width;
~ 		int j = parBufferedImage.height;

> CHANGE  11 : 13  @  11 : 12

~ 			EaglercraftGPU.glTexSubImage2D(GL_TEXTURE_2D, 0, parInt1, parInt2 + i1, i, j1, GL_RGBA, GL_UNSIGNED_BYTE,
~ 					dataBuffer);

> CHANGE  6 : 8  @  6 : 8

~ 			EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
~ 			EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

> CHANGE  1 : 3  @  1 : 3

~ 			EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
~ 			EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

> CHANGE  10 : 12  @  10 : 12

~ 			EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, parFlag2 ? 9987 : 9729);
~ 			EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

> CHANGE  1 : 3  @  1 : 3

~ 			EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, parFlag2 ? 9986 : 9728);
~ 			EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

> CHANGE  25 : 26  @  25 : 31

~ 		return readBufferedImage(resourceManager.getResource(imageLocation).getInputStream()).pixels;

> CHANGE  2 : 4  @  2 : 4

~ 	public static ImageData readBufferedImage(InputStream imageStream) throws IOException {
~ 		ImageData bufferedimage;

> CHANGE  1 : 2  @  1 : 2

~ 			bufferedimage = ImageData.loadImageFile(imageStream);

> INSERT  40 : 48  @  40

+ 	public static int[] convertComponentOrder(int[] arr) {
+ 		for (int i = 0; i < arr.length; ++i) {
+ 			int j = arr[i];
+ 			arr[i] = (j & 0xFF000000) | ((j >> 16) & 0xFF) | (j & 0xFF00) | ((j << 16) & 0xFF0000);
+ 		}
+ 		return arr;
+ 	}
+ 

> EOF
