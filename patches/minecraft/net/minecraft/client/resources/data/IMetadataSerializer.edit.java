
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 5  @  2 : 10

~ import org.json.JSONObject;
~ 
~ import net.lax1dude.eaglercraft.v1_8.json.JSONTypeProvider;

> DELETE  8  @  13 : 15

> DELETE  1  @  3 : 9

> DELETE  4  @  10 : 12

> CHANGE  2 : 3  @  4 : 5

~ 	public <T extends IMetadataSection> T parseMetadataSection(String parString1, JSONObject parJsonObject) {

> CHANGE  5 : 6  @  5 : 6

~ 		} else if (parJsonObject.optJSONObject(parString1) == null) {

> CHANGE  9 : 10  @  9 : 10

~ 				return (T) ((IMetadataSection) JSONTypeProvider.deserialize(parJsonObject.getJSONObject(parString1),

> DELETE  6  @  6 : 14

> EOF
