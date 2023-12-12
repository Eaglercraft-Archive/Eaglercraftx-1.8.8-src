
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  32 : 33  @  32 : 33

~ 		this.blockPos = parPacketBuffer.readBlockPos_server();

> CHANGE  3 : 4  @  3 : 4

~ 			this.lines[i] = parPacketBuffer.readChatComponent_server();

> CHANGE  5 : 6  @  5 : 6

~ 		parPacketBuffer.writeBlockPos_server(this.blockPos);

> CHANGE  2 : 3  @  2 : 3

~ 			parPacketBuffer.writeChatComponent_server(this.lines[i]);

> EOF
