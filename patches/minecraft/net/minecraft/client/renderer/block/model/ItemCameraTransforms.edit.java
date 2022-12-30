
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 4  @  2 : 10

~ import org.json.JSONException;
~ import org.json.JSONObject;

> INSERT  3 : 7  @  9

+ import net.lax1dude.eaglercraft.v1_8.json.JSONTypeDeserializer;
+ import net.lax1dude.eaglercraft.v1_8.json.JSONTypeProvider;
+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
+ 

> CHANGE  85 : 93  @  81 : 97

~ 	public static class Deserializer implements JSONTypeDeserializer<JSONObject, ItemCameraTransforms> {
~ 		public ItemCameraTransforms deserialize(JSONObject jsonobject) throws JSONException {
~ 			ItemTransformVec3f itemtransformvec3f = this.func_181683_a(jsonobject, "thirdperson");
~ 			ItemTransformVec3f itemtransformvec3f1 = this.func_181683_a(jsonobject, "firstperson");
~ 			ItemTransformVec3f itemtransformvec3f2 = this.func_181683_a(jsonobject, "head");
~ 			ItemTransformVec3f itemtransformvec3f3 = this.func_181683_a(jsonobject, "gui");
~ 			ItemTransformVec3f itemtransformvec3f4 = this.func_181683_a(jsonobject, "ground");
~ 			ItemTransformVec3f itemtransformvec3f5 = this.func_181683_a(jsonobject, "fixed");

> CHANGE  12 : 13  @  20 : 22

~ 		private ItemTransformVec3f func_181683_a(JSONObject parJsonObject, String parString1) {

> CHANGE  2 : 3  @  3 : 5

~ 					? JSONTypeProvider.deserialize(parJsonObject.get(parString1), ItemTransformVec3f.class)

> EOF
