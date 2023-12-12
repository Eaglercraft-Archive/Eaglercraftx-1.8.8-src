
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  43 : 44  @  43 : 44

~ 		this.position = parPacketBuffer.readBlockPos_server();

> CHANGE  1 : 2  @  1 : 2

~ 		this.stack = parPacketBuffer.readItemStackFromBuffer_server();

> CHANGE  6 : 7  @  6 : 7

~ 		parPacketBuffer.writeBlockPos_server(this.position);

> CHANGE  1 : 2  @  1 : 2

~ 		parPacketBuffer.writeItemStackToBuffer_server(this.stack);

> EOF
