
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 5  @  2 : 10

~ import org.json.JSONObject;
~ 
~ import net.lax1dude.eaglercraft.v1_8.json.JSONTypeProvider;

> DELETE  5  @  5 : 7

> DELETE  1  @  1 : 7

> DELETE  4  @  4 : 6

> CHANGE  2 : 3  @  2 : 3

~ 	public <T extends IMetadataSection> T parseMetadataSection(String parString1, JSONObject parJsonObject) {

> CHANGE  4 : 5  @  4 : 5

~ 		} else if (parJsonObject.optJSONObject(parString1) == null) {

> CHANGE  8 : 9  @  8 : 9

~ 				return (T) ((IMetadataSection) JSONTypeProvider.deserialize(parJsonObject.getJSONObject(parString1),

> DELETE  5  @  5 : 13

> EOF
