
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 10

> CHANGE  5 : 15  @  13 : 15

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

> DELETE  16  @  16 : 17

> DELETE  17  @  18 : 19

> CHANGE  67 : 69  @  69 : 73

~ 	public static class Deserializer implements JSONTypeDeserializer<JSONObject, BlockPart> {
~ 		public BlockPart deserialize(JSONObject jsonobject) throws JSONException {

> CHANGE  72 : 75  @  76 : 79

~ 			Map map = this.parseFacesCheck(jsonobject);
~ 			if (jsonobject.has("shade") && !(jsonobject.get("shade") instanceof Boolean)) {
~ 				throw new JSONException("Expected shade to be a Boolean");

> CHANGE  76 : 77  @  80 : 81

~ 				boolean flag = jsonobject.optBoolean("shade", true);

> CHANGE  81 : 82  @  85 : 86

~ 		private BlockPartRotation parseRotation(JSONObject parJsonObject) {

> CHANGE  84 : 85  @  88 : 89

~ 				JSONObject jsonobject = parJsonObject.getJSONObject("rotation");

> CHANGE  89 : 90  @  93 : 94

~ 				boolean flag = jsonobject.optBoolean("rescale", false);

> CHANGE  96 : 98  @  100 : 102

~ 		private float parseAngle(JSONObject parJsonObject) {
~ 			float f = parJsonObject.getFloat("angle");

> CHANGE  99 : 100  @  103 : 104

~ 				throw new JSONException("Invalid rotation " + f + " found, only -45/-22.5/0/22.5/45 allowed");

> CHANGE  105 : 107  @  109 : 111

~ 		private EnumFacing.Axis parseAxis(JSONObject parJsonObject) {
~ 			String s = parJsonObject.getString("axis");

> CHANGE  109 : 110  @  113 : 114

~ 				throw new JSONException("Invalid rotation axis: " + s);

> CHANGE  115 : 117  @  119 : 122

~ 		private Map<EnumFacing, BlockPartFace> parseFacesCheck(JSONObject parJsonObject) {
~ 			Map map = this.parseFaces(parJsonObject);

> CHANGE  118 : 119  @  123 : 124

~ 				throw new JSONException("Expected between 1 and 6 unique faces, got 0");

> CHANGE  124 : 125  @  129 : 131

~ 		private Map<EnumFacing, BlockPartFace> parseFaces(JSONObject parJsonObject) {

> CHANGE  126 : 127  @  132 : 133

~ 			JSONObject jsonobject = parJsonObject.getJSONObject("faces");

> CHANGE  128 : 132  @  134 : 138

~ 			for (String entry : jsonobject.keySet()) {
~ 				EnumFacing enumfacing = this.parseEnumFacing(entry);
~ 				enummap.put(enumfacing,
~ 						JSONTypeProvider.deserialize(jsonobject.getJSONObject(entry), BlockPartFace.class));

> CHANGE  140 : 141  @  146 : 147

~ 				throw new JSONException("Unknown facing: " + name);

> CHANGE  146 : 147  @  152 : 153

~ 		private Vector3f parsePositionTo(JSONObject parJsonObject) {

> CHANGE  152 : 153  @  158 : 159

~ 				throw new JSONException("\'to\' specifier exceeds the allowed boundaries: " + vector3f);

> CHANGE  156 : 157  @  162 : 163

~ 		private Vector3f parsePositionFrom(JSONObject parJsonObject) {

> CHANGE  162 : 163  @  168 : 169

~ 				throw new JSONException("\'from\' specifier exceeds the allowed boundaries: " + vector3f);

> CHANGE  166 : 170  @  172 : 176

~ 		private Vector3f parsePosition(JSONObject parJsonObject, String parString1) {
~ 			JSONArray jsonarray = parJsonObject.getJSONArray(parString1);
~ 			if (jsonarray.length() != 3) {
~ 				throw new JSONException("Expected 3 " + parString1 + " values, found: " + jsonarray.length());

> CHANGE  174 : 175  @  180 : 181

~ 					afloat[i] = jsonarray.getFloat(i);

> EOF
