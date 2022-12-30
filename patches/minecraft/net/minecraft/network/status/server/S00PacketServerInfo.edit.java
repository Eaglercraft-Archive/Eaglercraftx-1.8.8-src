
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> INSERT  1 : 6  @  3

+ 
+ import org.json.JSONException;
+ import org.json.JSONObject;
+ 
+ import net.lax1dude.eaglercraft.v1_8.json.JSONTypeProvider;

> DELETE  10  @  5 : 6

> CHANGE  3 : 4  @  4 : 13

~ 

> CHANGE  11 : 17  @  19 : 21

~ 		try {
~ 			this.response = (ServerStatusResponse) JSONTypeProvider.deserialize(
~ 					new JSONObject(parPacketBuffer.readStringFromBuffer(32767)), ServerStatusResponse.class);
~ 		} catch (JSONException exc) {
~ 			throw new IOException("Invalid ServerStatusResponse JSON payload", exc);
~ 		}

> CHANGE  9 : 14  @  5 : 6

~ 		try {
~ 			parPacketBuffer.writeString(((JSONObject) JSONTypeProvider.serialize(this.response)).toString());
~ 		} catch (JSONException exc) {
~ 			throw new IOException("Invalid ServerStatusResponse JSON payload", exc);
~ 		}

> EOF
