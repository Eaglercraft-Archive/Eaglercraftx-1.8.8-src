
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 5  @  2 : 10

~ import org.json.JSONArray;
~ import org.json.JSONException;
~ import org.json.JSONObject;

> INSERT  4 : 6  @  9

+ import net.lax1dude.eaglercraft.v1_8.json.JSONTypeDeserializer;
+ 

> CHANGE  44 : 46  @  42 : 46

~ 	public static class Deserializer implements JSONTypeDeserializer<JSONObject, BlockFaceUV> {
~ 		public BlockFaceUV deserialize(JSONObject jsonobject) throws JSONException {

> CHANGE  7 : 9  @  9 : 11

~ 		protected int parseRotation(JSONObject parJsonObject) {
~ 			int i = parJsonObject.optInt("rotation", 0);

> CHANGE  5 : 6  @  5 : 6

~ 				throw new JSONException("Invalid rotation " + i + " found, only 0/90/180/270 allowed");

> CHANGE  4 : 5  @  4 : 5

~ 		private float[] parseUV(JSONObject parJsonObject) {

> CHANGE  4 : 7  @  4 : 7

~ 				JSONArray jsonarray = parJsonObject.getJSONArray("uv");
~ 				if (jsonarray.length() != 4) {
~ 					throw new JSONException("Expected 4 uv values, found: " + jsonarray.length());

> CHANGE  7 : 8  @  7 : 8

~ 						afloat[i] = jsonarray.getFloat(i);

> EOF
