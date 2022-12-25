
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 10

> INSERT  3 : 5  @  11

+ import org.json.JSONException;
+ import org.json.JSONObject;

> CHANGE  7 : 8  @  13 : 16

~ 	public FontMetadataSection deserialize(JSONObject jsonobject) throws JSONException {

> CHANGE  15 : 17  @  23 : 25

~ 			if (!(jsonobject.get("characters") instanceof JSONObject)) {
~ 				throw new JSONException(

> CHANGE  20 : 21  @  28 : 29

~ 			JSONObject jsonobject1 = jsonobject.getJSONObject("characters");

> CHANGE  22 : 24  @  30 : 32

~ 				if (!(jsonobject1.get("default") instanceof JSONObject)) {
~ 					throw new JSONException(

> CHANGE  27 : 29  @  35 : 37

~ 				JSONObject jsonobject2 = jsonobject1.getJSONObject("default");
~ 				f = jsonobject2.optFloat("width", f);

> CHANGE  30 : 31  @  38 : 39

~ 				f1 = jsonobject2.optFloat("spacing", f1);

> CHANGE  32 : 33  @  40 : 41

~ 				f2 = jsonobject2.optFloat("left", f1);

> CHANGE  37 : 38  @  45 : 46

~ 				JSONObject jsonobject3 = jsonobject1.optJSONObject(Integer.toString(i));

> CHANGE  41 : 43  @  49 : 52

~ 				if (jsonobject3 != null) {
~ 					f3 = jsonobject3.optFloat("width", f);

> CHANGE  44 : 45  @  53 : 54

~ 					f4 = jsonobject3.optFloat("spacing", f1);

> CHANGE  46 : 47  @  55 : 56

~ 					f5 = jsonobject3.optFloat("left", f2);

> EOF
