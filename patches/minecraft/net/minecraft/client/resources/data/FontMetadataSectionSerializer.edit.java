
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 10

> INSERT  1 : 3  @  9

+ import org.json.JSONException;
+ import org.json.JSONObject;

> CHANGE  4 : 5  @  2 : 5

~ 	public FontMetadataSection deserialize(JSONObject jsonobject) throws JSONException {

> CHANGE  8 : 10  @  10 : 12

~ 			if (!(jsonobject.get("characters") instanceof JSONObject)) {
~ 				throw new JSONException(

> CHANGE  5 : 6  @  5 : 6

~ 			JSONObject jsonobject1 = jsonobject.getJSONObject("characters");

> CHANGE  2 : 4  @  2 : 4

~ 				if (!(jsonobject1.get("default") instanceof JSONObject)) {
~ 					throw new JSONException(

> CHANGE  5 : 7  @  5 : 7

~ 				JSONObject jsonobject2 = jsonobject1.getJSONObject("default");
~ 				f = jsonobject2.optFloat("width", f);

> CHANGE  3 : 4  @  3 : 4

~ 				f1 = jsonobject2.optFloat("spacing", f1);

> CHANGE  2 : 3  @  2 : 3

~ 				f2 = jsonobject2.optFloat("left", f1);

> CHANGE  5 : 6  @  5 : 6

~ 				JSONObject jsonobject3 = jsonobject1.optJSONObject(Integer.toString(i));

> CHANGE  4 : 6  @  4 : 7

~ 				if (jsonobject3 != null) {
~ 					f3 = jsonobject3.optFloat("width", f);

> CHANGE  3 : 4  @  4 : 5

~ 					f4 = jsonobject3.optFloat("spacing", f1);

> CHANGE  2 : 3  @  2 : 3

~ 					f5 = jsonobject3.optFloat("left", f2);

> EOF
