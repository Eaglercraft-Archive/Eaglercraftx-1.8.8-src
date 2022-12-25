
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 5  @  2 : 10

~ import org.json.JSONObject;
~ 
~ import net.lax1dude.eaglercraft.v1_8.json.JSONTypeProvider;

> DELETE  10  @  15 : 17

> DELETE  11  @  18 : 24

> DELETE  15  @  28 : 30

> CHANGE  17 : 18  @  32 : 33

~ 	public <T extends IMetadataSection> T parseMetadataSection(String parString1, JSONObject parJsonObject) {

> CHANGE  22 : 23  @  37 : 38

~ 		} else if (parJsonObject.optJSONObject(parString1) == null) {

> CHANGE  31 : 32  @  46 : 47

~ 				return (T) ((IMetadataSection) JSONTypeProvider.deserialize(parJsonObject.getJSONObject(parString1),

> DELETE  37  @  52 : 60

> EOF
