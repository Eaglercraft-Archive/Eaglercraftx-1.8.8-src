
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 5  @  2 : 13

~ import org.json.JSONArray;
~ import org.json.JSONException;
~ import org.json.JSONObject;

> INSERT  1 : 3  @  1

+ import com.carrotsearch.hppc.IntArrayList;
+ 

> CHANGE  1 : 5  @  1 : 7

~ 	public TextureMetadataSection deserialize(JSONObject jsonobject) throws JSONException {
~ 		boolean flag = jsonobject.optBoolean("blur", false);
~ 		boolean flag1 = jsonobject.optBoolean("clamp", false);
~ 		IntArrayList arraylist = new IntArrayList();

> CHANGE  2 : 3  @  2 : 3

~ 				JSONArray jsonarray = jsonobject.getJSONArray("mipmaps");

> CHANGE  1 : 4  @  1 : 4

~ 				for (int i = 0; i < jsonarray.length(); ++i) {
~ 					Object jsonelement = jsonarray.get(i);
~ 					if (jsonelement instanceof Number) {

> CHANGE  1 : 2  @  1 : 2

~ 							arraylist.add(((Number) jsonelement).intValue());

> CHANGE  1 : 2  @  1 : 2

~ 							throw new JSONException(

> CHANGE  3 : 5  @  3 : 5

~ 					} else if (jsonelement instanceof JSONObject) {
~ 						throw new JSONException(

> CHANGE  4 : 5  @  4 : 6

~ 				throw new JSONException("Invalid texture->mipmaps: expected array, was " + jsonobject.get("mipmaps"),

> EOF
