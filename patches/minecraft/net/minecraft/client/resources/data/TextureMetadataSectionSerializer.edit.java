
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 9

> DELETE  3  @  10 : 13

> INSERT  4 : 10  @  14

+ import org.json.JSONArray;
+ import org.json.JSONException;
+ import org.json.JSONObject;
+ 
+ import com.google.common.collect.Lists;
+ 

> CHANGE  11 : 14  @  15 : 20

~ 	public TextureMetadataSection deserialize(JSONObject jsonobject) throws JSONException {
~ 		boolean flag = jsonobject.optBoolean("blur", false);
~ 		boolean flag1 = jsonobject.optBoolean("clamp", false);

> CHANGE  17 : 18  @  23 : 24

~ 				JSONArray jsonarray = jsonobject.getJSONArray("mipmaps");

> CHANGE  19 : 22  @  25 : 28

~ 				for (int i = 0; i < jsonarray.length(); ++i) {
~ 					Object jsonelement = jsonarray.get(i);
~ 					if (jsonelement instanceof Number) {

> CHANGE  23 : 24  @  29 : 30

~ 							arraylist.add(((Number) jsonelement).intValue());

> CHANGE  25 : 26  @  31 : 32

~ 							throw new JSONException(

> CHANGE  29 : 31  @  35 : 37

~ 					} else if (jsonelement instanceof JSONObject) {
~ 						throw new JSONException(

> CHANGE  35 : 36  @  41 : 43

~ 				throw new JSONException("Invalid texture->mipmaps: expected array, was " + jsonobject.get("mipmaps"),

> EOF
