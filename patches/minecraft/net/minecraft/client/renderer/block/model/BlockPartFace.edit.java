
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

> DELETE  6  @  8 : 9

> CHANGE  15 : 17  @  16 : 20

~ 	public static class Deserializer implements JSONTypeDeserializer<JSONObject, BlockPartFace> {
~ 		public BlockPartFace deserialize(JSONObject jsonobject) throws JSONException {

> CHANGE  5 : 6  @  7 : 9

~ 			BlockFaceUV blockfaceuv = JSONTypeProvider.deserialize(jsonobject, BlockFaceUV.class);

> CHANGE  4 : 6  @  5 : 7

~ 		protected int parseTintIndex(JSONObject parJsonObject) {
~ 			return parJsonObject.optInt("tintindex", -1);

> CHANGE  4 : 6  @  4 : 6

~ 		private String parseTexture(JSONObject parJsonObject) {
~ 			return parJsonObject.getString("texture");

> CHANGE  4 : 6  @  4 : 6

~ 		private EnumFacing parseCullFace(JSONObject parJsonObject) {
~ 			String s = parJsonObject.optString("cullface", "");

> EOF
