
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 8  @  2 : 10

~ import org.json.JSONArray;
~ import org.json.JSONException;
~ import org.json.JSONObject;
~ 
~ import net.lax1dude.eaglercraft.v1_8.json.JSONTypeDeserializer;
~ import net.lax1dude.eaglercraft.v1_8.vector.Vector3f;

> DELETE  7  @  9 : 10

> CHANGE  34 : 35  @  35 : 36

~ 	public static class Deserializer implements JSONTypeDeserializer<JSONObject, ItemTransformVec3f> {

> CHANGE  5 : 6  @  5 : 8

~ 		public ItemTransformVec3f deserialize(JSONObject jsonobject) throws JSONException {

> CHANGE  14 : 15  @  16 : 17

~ 		private Vector3f parseVector3f(JSONObject jsonObject, String key, Vector3f defaultValue) {

> CHANGE  4 : 7  @  4 : 7

~ 				JSONArray jsonarray = jsonObject.getJSONArray(key);
~ 				if (jsonarray.length() != 3) {
~ 					throw new JSONException("Expected 3 " + key + " values, found: " + jsonarray.length());

> CHANGE  7 : 8  @  7 : 8

~ 						afloat[i] = jsonarray.getFloat(i);

> EOF
