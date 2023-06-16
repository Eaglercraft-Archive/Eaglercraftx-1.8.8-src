
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 5  @  2 : 10

~ import org.json.JSONArray;
~ import org.json.JSONException;
~ import org.json.JSONObject;

> INSERT  1 : 3  @  1

+ import net.lax1dude.eaglercraft.v1_8.json.JSONTypeDeserializer;
+ 

> CHANGE  42 : 44  @  42 : 46

~ 	public static class Deserializer implements JSONTypeDeserializer<JSONObject, BlockFaceUV> {
~ 		public BlockFaceUV deserialize(JSONObject jsonobject) throws JSONException {

> CHANGE  5 : 7  @  5 : 7

~ 		protected int parseRotation(JSONObject parJsonObject) {
~ 			int i = parJsonObject.optInt("rotation", 0);

> CHANGE  3 : 4  @  3 : 4

~ 				throw new JSONException("Invalid rotation " + i + " found, only 0/90/180/270 allowed");

> CHANGE  3 : 4  @  3 : 4

~ 		private float[] parseUV(JSONObject parJsonObject) {

> CHANGE  3 : 6  @  3 : 6

~ 				JSONArray jsonarray = parJsonObject.getJSONArray("uv");
~ 				if (jsonarray.length() != 4) {
~ 					throw new JSONException("Expected 4 uv values, found: " + jsonarray.length());

> CHANGE  4 : 5  @  4 : 5

~ 						afloat[i] = jsonarray.getFloat(i);

> EOF
