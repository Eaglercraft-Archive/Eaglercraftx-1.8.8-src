
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 5  @  2 : 8

~ import net.lax1dude.eaglercraft.v1_8.internal.buffer.ByteBuffer;
~ import net.lax1dude.eaglercraft.v1_8.internal.buffer.FloatBuffer;
~ import net.lax1dude.eaglercraft.v1_8.internal.buffer.IntBuffer;

> CHANGE  6 : 8  @  9 : 18

~ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
~ import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;

> CHANGE  9 : 12  @  19 : 24

~ public class GLAllocation {
~ 	public static int generateDisplayLists() {
~ 		return EaglercraftGPU.glGenLists();

> CHANGE  14 : 16  @  26 : 28

~ 	public static void deleteDisplayLists(int list) {
~ 		EaglercraftGPU.glDeleteLists(list);

> CHANGE  18 : 20  @  30 : 32

~ 	public static ByteBuffer createDirectByteBuffer(int capacity) {
~ 		return EagRuntime.allocateByteBuffer(capacity);

> DELETE  22  @  34 : 38

> CHANGE  23 : 24  @  39 : 40

~ 		return EagRuntime.allocateIntBuffer(capacity);

> CHANGE  27 : 28  @  43 : 44

~ 		return EagRuntime.allocateFloatBuffer(capacity);

> EOF
