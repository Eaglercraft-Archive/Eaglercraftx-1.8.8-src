
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 6  @  2

+ import java.util.Set;
+ 
+ import org.json.JSONArray;
+ 

> DELETE  8  @  4 : 9

> CHANGE  12 : 17  @  13 : 17

~ 	public void fromJson(Object jsonelement) {
~ 		if (jsonelement instanceof JSONArray) {
~ 			JSONArray arr = (JSONArray) jsonelement;
~ 			for (int i = 0; i < arr.length(); ++i) {
~ 				underlyingSet.add(arr.getString(i));

> CHANGE  22 : 24  @  22 : 24

~ 	public Object getSerializableElement() {
~ 		JSONArray jsonarray = new JSONArray();

> CHANGE  26 : 27  @  26 : 27

~ 			jsonarray.put(s);

> EOF
