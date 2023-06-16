
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 5  @  2 : 8

~ import net.lax1dude.eaglercraft.v1_8.internal.buffer.ByteBuffer;
~ import net.lax1dude.eaglercraft.v1_8.internal.buffer.FloatBuffer;
~ import net.lax1dude.eaglercraft.v1_8.internal.buffer.IntBuffer;

> CHANGE  1 : 3  @  1 : 10

~ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
~ import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;

> CHANGE  1 : 4  @  1 : 6

~ public class GLAllocation {
~ 	public static int generateDisplayLists() {
~ 		return EaglercraftGPU.glGenLists();

> CHANGE  2 : 4  @  2 : 4

~ 	public static void deleteDisplayLists(int list) {
~ 		EaglercraftGPU.glDeleteLists(list);

> CHANGE  2 : 4  @  2 : 4

~ 	public static ByteBuffer createDirectByteBuffer(int capacity) {
~ 		return EagRuntime.allocateByteBuffer(capacity);

> DELETE  2  @  2 : 6

> CHANGE  1 : 2  @  1 : 2

~ 		return EagRuntime.allocateIntBuffer(capacity);

> CHANGE  3 : 4  @  3 : 4

~ 		return EagRuntime.allocateFloatBuffer(capacity);

> EOF
