
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 3  @  2 : 6

~ import org.json.JSONObject;

> INSERT  4 : 7  @  7

+ import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
+ 

> CHANGE  52 : 53  @  52 : 53

~ 			EaglercraftGPU.glBlendEquation(this.field_148112_f);

> CHANGE  95 : 96  @  95 : 96

~ 	public static JsonBlendingMode func_148110_a(JSONObject parJsonObject) {

> CHANGE  106 : 108  @  106 : 108

~ 			if (parJsonObject.get("func") instanceof String) {
~ 				i = func_148108_a(parJsonObject.getString("func"));

> CHANGE  113 : 115  @  113 : 115

~ 			if (parJsonObject.get("srcrgb") instanceof String) {
~ 				j = func_148107_b(parJsonObject.getString("srcrgb"));

> CHANGE  120 : 122  @  120 : 122

~ 			if (parJsonObject.get("dstrgb") instanceof String) {
~ 				k = func_148107_b(parJsonObject.getString("dstrgb"));

> CHANGE  127 : 129  @  127 : 129

~ 			if (parJsonObject.get("srcalpha") instanceof String) {
~ 				l = func_148107_b(parJsonObject.getString("srcalpha"));

> CHANGE  136 : 138  @  136 : 138

~ 			if (parJsonObject.get("dstalpha") instanceof String) {
~ 				i1 = func_148107_b(parJsonObject.getString("dstalpha"));

> EOF
