
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

> DELETE  9  @  11 : 12

> CHANGE  43 : 44  @  46 : 47

~ 	public static class Deserializer implements JSONTypeDeserializer<JSONObject, ItemTransformVec3f> {

> CHANGE  48 : 49  @  51 : 54

~ 		public ItemTransformVec3f deserialize(JSONObject jsonobject) throws JSONException {

> CHANGE  62 : 63  @  67 : 68

~ 		private Vector3f parseVector3f(JSONObject jsonObject, String key, Vector3f defaultValue) {

> CHANGE  66 : 69  @  71 : 74

~ 				JSONArray jsonarray = jsonObject.getJSONArray(key);
~ 				if (jsonarray.length() != 3) {
~ 					throw new JSONException("Expected 3 " + key + " values, found: " + jsonarray.length());

> CHANGE  73 : 74  @  78 : 79

~ 						afloat[i] = jsonarray.getFloat(i);

> EOF
