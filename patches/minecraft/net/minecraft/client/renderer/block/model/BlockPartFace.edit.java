
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 7  @  2 : 9

~ import org.json.JSONException;
~ import org.json.JSONObject;
~ 
~ import net.lax1dude.eaglercraft.v1_8.json.JSONTypeDeserializer;
~ import net.lax1dude.eaglercraft.v1_8.json.JSONTypeProvider;

> DELETE  8  @  10 : 11

> CHANGE  23 : 25  @  26 : 30

~ 	public static class Deserializer implements JSONTypeDeserializer<JSONObject, BlockPartFace> {
~ 		public BlockPartFace deserialize(JSONObject jsonobject) throws JSONException {

> CHANGE  28 : 29  @  33 : 35

~ 			BlockFaceUV blockfaceuv = JSONTypeProvider.deserialize(jsonobject, BlockFaceUV.class);

> CHANGE  32 : 34  @  38 : 40

~ 		protected int parseTintIndex(JSONObject parJsonObject) {
~ 			return parJsonObject.optInt("tintindex", -1);

> CHANGE  36 : 38  @  42 : 44

~ 		private String parseTexture(JSONObject parJsonObject) {
~ 			return parJsonObject.getString("texture");

> CHANGE  40 : 42  @  46 : 48

~ 		private EnumFacing parseCullFace(JSONObject parJsonObject) {
~ 			String s = parJsonObject.optString("cullface", "");

> EOF
