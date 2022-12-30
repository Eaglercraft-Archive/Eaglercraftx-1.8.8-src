
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 4  @  2 : 3

~ import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;
~ 

> CHANGE  4 : 12  @  3 : 5

~ import net.lax1dude.eaglercraft.v1_8.internal.buffer.IntBuffer;
~ 
~ import net.lax1dude.eaglercraft.v1_8.IOUtils;
~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
~ import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
~ import net.lax1dude.eaglercraft.v1_8.opengl.ImageData;

> DELETE  10  @  4 : 6

> DELETE  2  @  4 : 8

> CHANGE  16 : 17  @  20 : 21

~ 	public static int uploadTextureImage(int parInt1, ImageData parBufferedImage) {

> CHANGE  121 : 123  @  121 : 122

~ 			EaglercraftGPU.glTexSubImage2D(GL_TEXTURE_2D, parInt1, parInt4, parInt5 + k, parInt2, l, GL_RGBA,
~ 					GL_UNSIGNED_BYTE, dataBuffer);

> CHANGE  6 : 7  @  5 : 6

~ 	public static int uploadTextureImageAllocate(int parInt1, ImageData parBufferedImage, boolean parFlag,

> CHANGE  2 : 3  @  2 : 3

~ 		allocateTexture(parInt1, parBufferedImage.width, parBufferedImage.height);

> CHANGE  9 : 10  @  9 : 10

~ 		// deleteTexture(parInt1); //TODO: why

> CHANGE  3 : 7  @  3 : 7

~ 			EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, '\u813d', parInt2);
~ 			EaglercraftGPU.glTexParameterf(GL_TEXTURE_2D, '\u813a', 0.0F);
~ 			EaglercraftGPU.glTexParameterf(GL_TEXTURE_2D, '\u813b', (float) parInt2);
~ 			// EaglercraftGPU.glTexParameterf(GL_TEXTURE_2D, '\u8501', 0.0F);

> CHANGE  7 : 9  @  7 : 8

~ 			EaglercraftGPU.glTexImage2D(GL_TEXTURE_2D, i, GL_RGBA, parInt3 >> i, parInt4 >> i, 0, GL_RGBA,
~ 					GL_UNSIGNED_BYTE, (IntBuffer) null);

> CHANGE  6 : 7  @  5 : 6

~ 	public static int uploadTextureImageSub(int textureId, ImageData parBufferedImage, int parInt2, int parInt3,

> CHANGE  7 : 11  @  7 : 11

~ 	private static void uploadTextureImageSubImpl(ImageData parBufferedImage, int parInt1, int parInt2, boolean parFlag,
~ 			boolean parFlag2) {
~ 		int i = parBufferedImage.width;
~ 		int j = parBufferedImage.height;

> CHANGE  15 : 17  @  15 : 16

~ 			EaglercraftGPU.glTexSubImage2D(GL_TEXTURE_2D, 0, parInt1, parInt2 + i1, i, j1, GL_RGBA, GL_UNSIGNED_BYTE,
~ 					dataBuffer);

> CHANGE  8 : 10  @  7 : 9

~ 			EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
~ 			EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

> CHANGE  3 : 5  @  3 : 5

~ 			EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
~ 			EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

> CHANGE  12 : 14  @  12 : 14

~ 			EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, parFlag2 ? 9987 : 9729);
~ 			EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

> CHANGE  3 : 5  @  3 : 5

~ 			EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, parFlag2 ? 9986 : 9728);
~ 			EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

> CHANGE  27 : 28  @  27 : 33

~ 		return readBufferedImage(resourceManager.getResource(imageLocation).getInputStream()).pixels;

> CHANGE  3 : 5  @  8 : 10

~ 	public static ImageData readBufferedImage(InputStream imageStream) throws IOException {
~ 		ImageData bufferedimage;

> CHANGE  3 : 4  @  3 : 4

~ 			bufferedimage = ImageData.loadImageFile(imageStream);

> INSERT  41 : 49  @  41

+ 	public static int[] convertComponentOrder(int[] arr) {
+ 		for (int i = 0; i < arr.length; ++i) {
+ 			int j = arr[i];
+ 			arr[i] = (j & 0xFF000000) | ((j >> 16) & 0xFF) | (j & 0xFF00) | ((j << 16) & 0xFF0000);
+ 		}
+ 		return arr;
+ 	}
+ 

> EOF
