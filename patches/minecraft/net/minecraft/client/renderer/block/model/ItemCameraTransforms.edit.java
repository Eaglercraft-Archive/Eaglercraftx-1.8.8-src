
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 4  @  2 : 10

~ import org.json.JSONException;
~ import org.json.JSONObject;

> INSERT  1 : 5  @  1

+ import net.lax1dude.eaglercraft.v1_8.json.JSONTypeDeserializer;
+ import net.lax1dude.eaglercraft.v1_8.json.JSONTypeProvider;
+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
+ 

> CHANGE  81 : 89  @  81 : 97

~ 	public static class Deserializer implements JSONTypeDeserializer<JSONObject, ItemCameraTransforms> {
~ 		public ItemCameraTransforms deserialize(JSONObject jsonobject) throws JSONException {
~ 			ItemTransformVec3f itemtransformvec3f = this.func_181683_a(jsonobject, "thirdperson");
~ 			ItemTransformVec3f itemtransformvec3f1 = this.func_181683_a(jsonobject, "firstperson");
~ 			ItemTransformVec3f itemtransformvec3f2 = this.func_181683_a(jsonobject, "head");
~ 			ItemTransformVec3f itemtransformvec3f3 = this.func_181683_a(jsonobject, "gui");
~ 			ItemTransformVec3f itemtransformvec3f4 = this.func_181683_a(jsonobject, "ground");
~ 			ItemTransformVec3f itemtransformvec3f5 = this.func_181683_a(jsonobject, "fixed");

> CHANGE  4 : 5  @  4 : 6

~ 		private ItemTransformVec3f func_181683_a(JSONObject parJsonObject, String parString1) {

> CHANGE  1 : 2  @  1 : 3

~ 					? JSONTypeProvider.deserialize(parJsonObject.get(parString1), ItemTransformVec3f.class)

> EOF
