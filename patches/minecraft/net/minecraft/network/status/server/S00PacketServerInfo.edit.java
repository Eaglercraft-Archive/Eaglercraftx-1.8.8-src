
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> INSERT  1 : 6  @  1

+ 
+ import org.json.JSONException;
+ import org.json.JSONObject;
+ 
+ import net.lax1dude.eaglercraft.v1_8.json.JSONTypeProvider;

> DELETE  5  @  5 : 6

> CHANGE  3 : 4  @  3 : 12

~ 

> CHANGE  10 : 16  @  10 : 12

~ 		try {
~ 			this.response = (ServerStatusResponse) JSONTypeProvider.deserialize(
~ 					new JSONObject(parPacketBuffer.readStringFromBuffer(32767)), ServerStatusResponse.class);
~ 		} catch (JSONException exc) {
~ 			throw new IOException("Invalid ServerStatusResponse JSON payload", exc);
~ 		}

> CHANGE  3 : 8  @  3 : 4

~ 		try {
~ 			parPacketBuffer.writeString(((JSONObject) JSONTypeProvider.serialize(this.response)).toString());
~ 		} catch (JSONException exc) {
~ 			throw new IOException("Invalid ServerStatusResponse JSON payload", exc);
~ 		}

> EOF
