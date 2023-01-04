
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 10

> DELETE  6  @  6 : 8

> CHANGE  1 : 10  @  1 : 2

~ import java.nio.charset.StandardCharsets;
~ 
~ import net.lax1dude.eaglercraft.v1_8.DecoderException;
~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
~ import net.lax1dude.eaglercraft.v1_8.EncoderException;
~ 
~ import net.lax1dude.eaglercraft.v1_8.netty.ByteBuf;
~ import net.lax1dude.eaglercraft.v1_8.netty.ByteBufInputStream;
~ import net.lax1dude.eaglercraft.v1_8.netty.ByteBufOutputStream;

> CHANGE  98 : 99  @  98 : 99

~ 	public void writeUuid(EaglercraftUUID uuid) {

> CHANGE  4 : 6  @  4 : 6

~ 	public EaglercraftUUID readUuid() {
~ 		return new EaglercraftUUID(this.readLong(), this.readLong());

> CHANGE  82 : 83  @  82 : 83

~ 			String s = new String(this.readBytes(i).array(), StandardCharsets.UTF_8);

> CHANGE  10 : 11  @  10 : 11

~ 		byte[] abyte = string.getBytes(StandardCharsets.UTF_8);

> DELETE  21  @  21 : 25

> CHANGE  153 : 158  @  153 : 154

~ 		if (parByteBuf instanceof PacketBuffer) {
~ 			return this.buf.getBytes(parInt1, ((PacketBuffer) parByteBuf).buf);
~ 		} else {
~ 			return this.buf.getBytes(parInt1, parByteBuf);
~ 		}

> CHANGE  3 : 8  @  3 : 4

~ 		if (bytebuf instanceof PacketBuffer) {
~ 			return this.buf.getBytes(i, ((PacketBuffer) bytebuf).buf, j);
~ 		} else {
~ 			return this.buf.getBytes(i, bytebuf, j);
~ 		}

> CHANGE  3 : 8  @  3 : 4

~ 		if (bytebuf instanceof PacketBuffer) {
~ 			return this.buf.getBytes(i, ((PacketBuffer) bytebuf).buf, j, k);
~ 		} else {
~ 			return this.buf.getBytes(i, bytebuf, j, k);
~ 		}

> DELETE  18  @  18 : 22

> CHANGE  41 : 46  @  41 : 42

~ 		if (bytebuf instanceof PacketBuffer) {
~ 			return this.buf.setBytes(i, ((PacketBuffer) bytebuf).buf, j);
~ 		} else {
~ 			return this.buf.setBytes(i, bytebuf, j);
~ 		}

> CHANGE  3 : 8  @  3 : 4

~ 		if (bytebuf instanceof PacketBuffer) {
~ 			return this.buf.setBytes(i, ((PacketBuffer) bytebuf).buf, j, k);
~ 		} else {
~ 			return this.buf.setBytes(i, bytebuf, j, k);
~ 		}

> DELETE  18  @  18 : 22

> CHANGE  65 : 70  @  65 : 66

~ 		if (bytebuf instanceof PacketBuffer) {
~ 			return this.buf.readBytes(((PacketBuffer) bytebuf).buf);
~ 		} else {
~ 			return this.buf.readBytes(bytebuf);
~ 		}

> CHANGE  3 : 8  @  3 : 4

~ 		if (bytebuf instanceof PacketBuffer) {
~ 			return this.buf.readBytes(((PacketBuffer) bytebuf).buf, i);
~ 		} else {
~ 			return this.buf.readBytes(bytebuf, i);
~ 		}

> CHANGE  3 : 8  @  3 : 4

~ 		if (bytebuf instanceof PacketBuffer) {
~ 			return this.buf.readBytes(((PacketBuffer) bytebuf).buf, i, j);
~ 		} else {
~ 			return this.buf.readBytes(bytebuf, i, j);
~ 		}

> DELETE  18  @  18 : 22

> CHANGE  41 : 46  @  41 : 42

~ 		if (parByteBuf instanceof PacketBuffer) {
~ 			return this.buf.writeBytes(((PacketBuffer) parByteBuf).buf);
~ 		} else {
~ 			return this.buf.writeBytes(parByteBuf);
~ 		}

> CHANGE  3 : 8  @  3 : 4

~ 		if (bytebuf instanceof PacketBuffer) {
~ 			return this.buf.writeBytes(((PacketBuffer) bytebuf).buf, i);
~ 		} else {
~ 			return this.buf.writeBytes(bytebuf, i);
~ 		}

> CHANGE  3 : 8  @  3 : 4

~ 		if (bytebuf instanceof PacketBuffer) {
~ 			return this.buf.writeBytes(((PacketBuffer) bytebuf).buf, i, j);
~ 		} else {
~ 			return this.buf.writeBytes(bytebuf, i, j);
~ 		}

> DELETE  18  @  18 : 22

> DELETE  20  @  20 : 36

> DELETE  88  @  88 : 107

> EOF
