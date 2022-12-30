
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 5  @  2 : 8

~ import net.lax1dude.eaglercraft.v1_8.internal.buffer.ByteBuffer;
~ import net.lax1dude.eaglercraft.v1_8.internal.buffer.FloatBuffer;
~ import net.lax1dude.eaglercraft.v1_8.internal.buffer.IntBuffer;

> CHANGE  4 : 6  @  7 : 16

~ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
~ import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;

> CHANGE  3 : 6  @  10 : 15

~ public class GLAllocation {
~ 	public static int generateDisplayLists() {
~ 		return EaglercraftGPU.glGenLists();

> CHANGE  5 : 7  @  7 : 9

~ 	public static void deleteDisplayLists(int list) {
~ 		EaglercraftGPU.glDeleteLists(list);

> CHANGE  4 : 6  @  4 : 6

~ 	public static ByteBuffer createDirectByteBuffer(int capacity) {
~ 		return EagRuntime.allocateByteBuffer(capacity);

> DELETE  4  @  4 : 8

> CHANGE  1 : 2  @  5 : 6

~ 		return EagRuntime.allocateIntBuffer(capacity);

> CHANGE  4 : 5  @  4 : 5

~ 		return EagRuntime.allocateFloatBuffer(capacity);

> EOF
