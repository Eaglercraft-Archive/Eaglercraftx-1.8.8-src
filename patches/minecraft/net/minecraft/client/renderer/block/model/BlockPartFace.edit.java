
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 7  @  2 : 9

~ import org.json.JSONException;
~ import org.json.JSONObject;
~ 
~ import net.lax1dude.eaglercraft.v1_8.json.JSONTypeDeserializer;
~ import net.lax1dude.eaglercraft.v1_8.json.JSONTypeProvider;

> DELETE  1  @  1 : 2

> CHANGE  15 : 17  @  15 : 19

~ 	public static class Deserializer implements JSONTypeDeserializer<JSONObject, BlockPartFace> {
~ 		public BlockPartFace deserialize(JSONObject jsonobject) throws JSONException {

> CHANGE  3 : 4  @  3 : 5

~ 			BlockFaceUV blockfaceuv = JSONTypeProvider.deserialize(jsonobject, BlockFaceUV.class);

> CHANGE  3 : 5  @  3 : 5

~ 		protected int parseTintIndex(JSONObject parJsonObject) {
~ 			return parJsonObject.optInt("tintindex", -1);

> CHANGE  2 : 4  @  2 : 4

~ 		private String parseTexture(JSONObject parJsonObject) {
~ 			return parJsonObject.getString("texture");

> CHANGE  2 : 4  @  2 : 4

~ 		private EnumFacing parseCullFace(JSONObject parJsonObject) {
~ 			String s = parJsonObject.optString("cullface", "");

> EOF
