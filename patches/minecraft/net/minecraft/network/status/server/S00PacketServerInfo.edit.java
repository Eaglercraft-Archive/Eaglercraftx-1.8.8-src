
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> INSERT  3 : 8  @  5

+ 
+ import org.json.JSONException;
+ import org.json.JSONObject;
+ 
+ import net.lax1dude.eaglercraft.v1_8.json.JSONTypeProvider;

> DELETE  13  @  10 : 11

> CHANGE  16 : 17  @  14 : 23

~ 

> CHANGE  27 : 33  @  33 : 35

~ 		try {
~ 			this.response = (ServerStatusResponse) JSONTypeProvider.deserialize(
~ 					new JSONObject(parPacketBuffer.readStringFromBuffer(32767)), ServerStatusResponse.class);
~ 		} catch (JSONException exc) {
~ 			throw new IOException("Invalid ServerStatusResponse JSON payload", exc);
~ 		}

> CHANGE  36 : 41  @  38 : 39

~ 		try {
~ 			parPacketBuffer.writeString(((JSONObject) JSONTypeProvider.serialize(this.response)).toString());
~ 		} catch (JSONException exc) {
~ 			throw new IOException("Invalid ServerStatusResponse JSON payload", exc);
~ 		}

> EOF
