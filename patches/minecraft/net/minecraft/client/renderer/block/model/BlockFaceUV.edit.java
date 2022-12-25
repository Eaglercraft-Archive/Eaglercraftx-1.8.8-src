
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 5  @  2 : 10

~ import org.json.JSONArray;
~ import org.json.JSONException;
~ import org.json.JSONObject;

> INSERT  6 : 8  @  11

+ import net.lax1dude.eaglercraft.v1_8.json.JSONTypeDeserializer;
+ 

> CHANGE  50 : 52  @  53 : 57

~ 	public static class Deserializer implements JSONTypeDeserializer<JSONObject, BlockFaceUV> {
~ 		public BlockFaceUV deserialize(JSONObject jsonobject) throws JSONException {

> CHANGE  57 : 59  @  62 : 64

~ 		protected int parseRotation(JSONObject parJsonObject) {
~ 			int i = parJsonObject.optInt("rotation", 0);

> CHANGE  62 : 63  @  67 : 68

~ 				throw new JSONException("Invalid rotation " + i + " found, only 0/90/180/270 allowed");

> CHANGE  66 : 67  @  71 : 72

~ 		private float[] parseUV(JSONObject parJsonObject) {

> CHANGE  70 : 73  @  75 : 78

~ 				JSONArray jsonarray = parJsonObject.getJSONArray("uv");
~ 				if (jsonarray.length() != 4) {
~ 					throw new JSONException("Expected 4 uv values, found: " + jsonarray.length());

> CHANGE  77 : 78  @  82 : 83

~ 						afloat[i] = jsonarray.getFloat(i);

> EOF
