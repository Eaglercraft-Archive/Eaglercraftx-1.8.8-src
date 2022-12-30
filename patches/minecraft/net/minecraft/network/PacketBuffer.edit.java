
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 10

> DELETE  6  @  14 : 16

> CHANGE  1 : 10  @  3 : 4

~ import java.nio.charset.StandardCharsets;
~ 
~ import net.lax1dude.eaglercraft.v1_8.DecoderException;
~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
~ import net.lax1dude.eaglercraft.v1_8.EncoderException;
~ 
~ import net.lax1dude.eaglercraft.v1_8.netty.ByteBuf;
~ import net.lax1dude.eaglercraft.v1_8.netty.ByteBufInputStream;
~ import net.lax1dude.eaglercraft.v1_8.netty.ByteBufOutputStream;

> CHANGE  107 : 108  @  99 : 100

~ 	public void writeUuid(EaglercraftUUID uuid) {

> CHANGE  5 : 7  @  5 : 7

~ 	public EaglercraftUUID readUuid() {
~ 		return new EaglercraftUUID(this.readLong(), this.readLong());

> CHANGE  84 : 85  @  84 : 85

~ 			String s = new String(this.readBytes(i).array(), StandardCharsets.UTF_8);

> CHANGE  11 : 12  @  11 : 12

~ 		byte[] abyte = string.getBytes(StandardCharsets.UTF_8);

> DELETE  22  @  22 : 26

> CHANGE  153 : 158  @  157 : 158

~ 		if (parByteBuf instanceof PacketBuffer) {
~ 			return this.buf.getBytes(parInt1, ((PacketBuffer) parByteBuf).buf);
~ 		} else {
~ 			return this.buf.getBytes(parInt1, parByteBuf);
~ 		}

> CHANGE  8 : 13  @  4 : 5

~ 		if (bytebuf instanceof PacketBuffer) {
~ 			return this.buf.getBytes(i, ((PacketBuffer) bytebuf).buf, j);
~ 		} else {
~ 			return this.buf.getBytes(i, bytebuf, j);
~ 		}

> CHANGE  8 : 13  @  4 : 5

~ 		if (bytebuf instanceof PacketBuffer) {
~ 			return this.buf.getBytes(i, ((PacketBuffer) bytebuf).buf, j, k);
~ 		} else {
~ 			return this.buf.getBytes(i, bytebuf, j, k);
~ 		}

> DELETE  23  @  19 : 23

> CHANGE  41 : 46  @  45 : 46

~ 		if (bytebuf instanceof PacketBuffer) {
~ 			return this.buf.setBytes(i, ((PacketBuffer) bytebuf).buf, j);
~ 		} else {
~ 			return this.buf.setBytes(i, bytebuf, j);
~ 		}

> CHANGE  8 : 13  @  4 : 5

~ 		if (bytebuf instanceof PacketBuffer) {
~ 			return this.buf.setBytes(i, ((PacketBuffer) bytebuf).buf, j, k);
~ 		} else {
~ 			return this.buf.setBytes(i, bytebuf, j, k);
~ 		}

> DELETE  23  @  19 : 23

> CHANGE  65 : 70  @  69 : 70

~ 		if (bytebuf instanceof PacketBuffer) {
~ 			return this.buf.readBytes(((PacketBuffer) bytebuf).buf);
~ 		} else {
~ 			return this.buf.readBytes(bytebuf);
~ 		}

> CHANGE  8 : 13  @  4 : 5

~ 		if (bytebuf instanceof PacketBuffer) {
~ 			return this.buf.readBytes(((PacketBuffer) bytebuf).buf, i);
~ 		} else {
~ 			return this.buf.readBytes(bytebuf, i);
~ 		}

> CHANGE  8 : 13  @  4 : 5

~ 		if (bytebuf instanceof PacketBuffer) {
~ 			return this.buf.readBytes(((PacketBuffer) bytebuf).buf, i, j);
~ 		} else {
~ 			return this.buf.readBytes(bytebuf, i, j);
~ 		}

> DELETE  23  @  19 : 23

> CHANGE  41 : 46  @  45 : 46

~ 		if (parByteBuf instanceof PacketBuffer) {
~ 			return this.buf.writeBytes(((PacketBuffer) parByteBuf).buf);
~ 		} else {
~ 			return this.buf.writeBytes(parByteBuf);
~ 		}

> CHANGE  8 : 13  @  4 : 5

~ 		if (bytebuf instanceof PacketBuffer) {
~ 			return this.buf.writeBytes(((PacketBuffer) bytebuf).buf, i);
~ 		} else {
~ 			return this.buf.writeBytes(bytebuf, i);
~ 		}

> CHANGE  8 : 13  @  4 : 5

~ 		if (bytebuf instanceof PacketBuffer) {
~ 			return this.buf.writeBytes(((PacketBuffer) bytebuf).buf, i, j);
~ 		} else {
~ 			return this.buf.writeBytes(bytebuf, i, j);
~ 		}

> DELETE  23  @  19 : 23

> DELETE  20  @  24 : 40

> DELETE  88  @  104 : 123

> EOF
