
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 8  @  2 : 10

~ import org.json.JSONArray;
~ import org.json.JSONException;
~ import org.json.JSONObject;
~ 
~ import net.lax1dude.eaglercraft.v1_8.json.JSONTypeDeserializer;
~ import net.lax1dude.eaglercraft.v1_8.vector.Vector3f;

> DELETE  1  @  1 : 2

> CHANGE  34 : 35  @  34 : 35

~ 	public static class Deserializer implements JSONTypeDeserializer<JSONObject, ItemTransformVec3f> {

> CHANGE  4 : 5  @  4 : 7

~ 		public ItemTransformVec3f deserialize(JSONObject jsonobject) throws JSONException {

> CHANGE  13 : 14  @  13 : 14

~ 		private Vector3f parseVector3f(JSONObject jsonObject, String key, Vector3f defaultValue) {

> CHANGE  3 : 6  @  3 : 6

~ 				JSONArray jsonarray = jsonObject.getJSONArray(key);
~ 				if (jsonarray.length() != 3) {
~ 					throw new JSONException("Expected 3 " + key + " values, found: " + jsonarray.length());

> CHANGE  4 : 5  @  4 : 5

~ 						afloat[i] = jsonarray.getFloat(i);

> EOF
