
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 10

> INSERT  1 : 3  @  1

+ import org.json.JSONException;
+ import org.json.JSONObject;

> CHANGE  2 : 3  @  2 : 5

~ 	public FontMetadataSection deserialize(JSONObject jsonobject) throws JSONException {

> CHANGE  7 : 9  @  7 : 9

~ 			if (!(jsonobject.get("characters") instanceof JSONObject)) {
~ 				throw new JSONException(

> CHANGE  3 : 4  @  3 : 4

~ 			JSONObject jsonobject1 = jsonobject.getJSONObject("characters");

> CHANGE  1 : 3  @  1 : 3

~ 				if (!(jsonobject1.get("default") instanceof JSONObject)) {
~ 					throw new JSONException(

> CHANGE  3 : 5  @  3 : 5

~ 				JSONObject jsonobject2 = jsonobject1.getJSONObject("default");
~ 				f = jsonobject2.optFloat("width", f);

> CHANGE  1 : 2  @  1 : 2

~ 				f1 = jsonobject2.optFloat("spacing", f1);

> CHANGE  1 : 2  @  1 : 2

~ 				f2 = jsonobject2.optFloat("left", f1);

> CHANGE  4 : 5  @  4 : 5

~ 				JSONObject jsonobject3 = jsonobject1.optJSONObject(Integer.toString(i));

> CHANGE  3 : 5  @  3 : 6

~ 				if (jsonobject3 != null) {
~ 					f3 = jsonobject3.optFloat("width", f);

> CHANGE  1 : 2  @  1 : 2

~ 					f4 = jsonobject3.optFloat("spacing", f1);

> CHANGE  1 : 2  @  1 : 2

~ 					f5 = jsonobject3.optFloat("left", f2);

> EOF
