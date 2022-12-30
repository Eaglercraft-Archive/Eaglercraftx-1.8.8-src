
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 9

> DELETE  1  @  8 : 11

> INSERT  1 : 7  @  4

+ import org.json.JSONArray;
+ import org.json.JSONException;
+ import org.json.JSONObject;
+ 
+ import com.google.common.collect.Lists;
+ 

> CHANGE  7 : 10  @  1 : 6

~ 	public TextureMetadataSection deserialize(JSONObject jsonobject) throws JSONException {
~ 		boolean flag = jsonobject.optBoolean("blur", false);
~ 		boolean flag1 = jsonobject.optBoolean("clamp", false);

> CHANGE  6 : 7  @  8 : 9

~ 				JSONArray jsonarray = jsonobject.getJSONArray("mipmaps");

> CHANGE  2 : 5  @  2 : 5

~ 				for (int i = 0; i < jsonarray.length(); ++i) {
~ 					Object jsonelement = jsonarray.get(i);
~ 					if (jsonelement instanceof Number) {

> CHANGE  4 : 5  @  4 : 5

~ 							arraylist.add(((Number) jsonelement).intValue());

> CHANGE  2 : 3  @  2 : 3

~ 							throw new JSONException(

> CHANGE  4 : 6  @  4 : 6

~ 					} else if (jsonelement instanceof JSONObject) {
~ 						throw new JSONException(

> CHANGE  6 : 7  @  6 : 8

~ 				throw new JSONException("Invalid texture->mipmaps: expected array, was " + jsonobject.get("mipmaps"),

> EOF
