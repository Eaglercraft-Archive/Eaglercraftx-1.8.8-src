
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 10

> CHANGE  3 : 13  @  11 : 13

~ 
~ import org.json.JSONArray;
~ import org.json.JSONException;
~ import org.json.JSONObject;
~ 
~ import com.google.common.collect.Maps;
~ 
~ import net.lax1dude.eaglercraft.v1_8.json.JSONTypeDeserializer;
~ import net.lax1dude.eaglercraft.v1_8.json.JSONTypeProvider;
~ import net.lax1dude.eaglercraft.v1_8.vector.Vector3f;

> DELETE  11  @  3 : 4

> DELETE  1  @  2 : 3

> CHANGE  50 : 52  @  51 : 55

~ 	public static class Deserializer implements JSONTypeDeserializer<JSONObject, BlockPart> {
~ 		public BlockPart deserialize(JSONObject jsonobject) throws JSONException {

> CHANGE  5 : 8  @  7 : 10

~ 			Map map = this.parseFacesCheck(jsonobject);
~ 			if (jsonobject.has("shade") && !(jsonobject.get("shade") instanceof Boolean)) {
~ 				throw new JSONException("Expected shade to be a Boolean");

> CHANGE  4 : 5  @  4 : 5

~ 				boolean flag = jsonobject.optBoolean("shade", true);

> CHANGE  5 : 6  @  5 : 6

~ 		private BlockPartRotation parseRotation(JSONObject parJsonObject) {

> CHANGE  3 : 4  @  3 : 4

~ 				JSONObject jsonobject = parJsonObject.getJSONObject("rotation");

> CHANGE  5 : 6  @  5 : 6

~ 				boolean flag = jsonobject.optBoolean("rescale", false);

> CHANGE  7 : 9  @  7 : 9

~ 		private float parseAngle(JSONObject parJsonObject) {
~ 			float f = parJsonObject.getFloat("angle");

> CHANGE  3 : 4  @  3 : 4

~ 				throw new JSONException("Invalid rotation " + f + " found, only -45/-22.5/0/22.5/45 allowed");

> CHANGE  6 : 8  @  6 : 8

~ 		private EnumFacing.Axis parseAxis(JSONObject parJsonObject) {
~ 			String s = parJsonObject.getString("axis");

> CHANGE  4 : 5  @  4 : 5

~ 				throw new JSONException("Invalid rotation axis: " + s);

> CHANGE  6 : 8  @  6 : 9

~ 		private Map<EnumFacing, BlockPartFace> parseFacesCheck(JSONObject parJsonObject) {
~ 			Map map = this.parseFaces(parJsonObject);

> CHANGE  3 : 4  @  4 : 5

~ 				throw new JSONException("Expected between 1 and 6 unique faces, got 0");

> CHANGE  6 : 7  @  6 : 8

~ 		private Map<EnumFacing, BlockPartFace> parseFaces(JSONObject parJsonObject) {

> CHANGE  2 : 3  @  3 : 4

~ 			JSONObject jsonobject = parJsonObject.getJSONObject("faces");

> CHANGE  2 : 6  @  2 : 6

~ 			for (String entry : jsonobject.keySet()) {
~ 				EnumFacing enumfacing = this.parseEnumFacing(entry);
~ 				enummap.put(enumfacing,
~ 						JSONTypeProvider.deserialize(jsonobject.getJSONObject(entry), BlockPartFace.class));

> CHANGE  12 : 13  @  12 : 13

~ 				throw new JSONException("Unknown facing: " + name);

> CHANGE  6 : 7  @  6 : 7

~ 		private Vector3f parsePositionTo(JSONObject parJsonObject) {

> CHANGE  6 : 7  @  6 : 7

~ 				throw new JSONException("\'to\' specifier exceeds the allowed boundaries: " + vector3f);

> CHANGE  4 : 5  @  4 : 5

~ 		private Vector3f parsePositionFrom(JSONObject parJsonObject) {

> CHANGE  6 : 7  @  6 : 7

~ 				throw new JSONException("\'from\' specifier exceeds the allowed boundaries: " + vector3f);

> CHANGE  4 : 8  @  4 : 8

~ 		private Vector3f parsePosition(JSONObject parJsonObject, String parString1) {
~ 			JSONArray jsonarray = parJsonObject.getJSONArray(parString1);
~ 			if (jsonarray.length() != 3) {
~ 				throw new JSONException("Expected 3 " + parString1 + " values, found: " + jsonarray.length());

> CHANGE  8 : 9  @  8 : 9

~ 					afloat[i] = jsonarray.getFloat(i);

> EOF
