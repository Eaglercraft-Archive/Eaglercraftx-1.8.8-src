
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 3  @  2 : 6

~ import org.json.JSONObject;

> INSERT  1 : 4  @  1

+ import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
+ 

> CHANGE  45 : 46  @  45 : 46

~ 			EaglercraftGPU.glBlendEquation(this.field_148112_f);

> CHANGE  42 : 43  @  42 : 43

~ 	public static JsonBlendingMode func_148110_a(JSONObject parJsonObject) {

> CHANGE  10 : 12  @  10 : 12

~ 			if (parJsonObject.get("func") instanceof String) {
~ 				i = func_148108_a(parJsonObject.getString("func"));

> CHANGE  5 : 7  @  5 : 7

~ 			if (parJsonObject.get("srcrgb") instanceof String) {
~ 				j = func_148107_b(parJsonObject.getString("srcrgb"));

> CHANGE  5 : 7  @  5 : 7

~ 			if (parJsonObject.get("dstrgb") instanceof String) {
~ 				k = func_148107_b(parJsonObject.getString("dstrgb"));

> CHANGE  5 : 7  @  5 : 7

~ 			if (parJsonObject.get("srcalpha") instanceof String) {
~ 				l = func_148107_b(parJsonObject.getString("srcalpha"));

> CHANGE  7 : 9  @  7 : 9

~ 			if (parJsonObject.get("dstalpha") instanceof String) {
~ 				i1 = func_148107_b(parJsonObject.getString("dstalpha"));

> EOF
