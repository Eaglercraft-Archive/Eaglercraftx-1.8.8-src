
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

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

> CHANGE  30 : 37  @  30 : 32

~ 	public byte[] readByteArray(int maxLen) {
~ 		int i = this.readVarIntFromBuffer();
~ 		if (i < 0 || i > maxLen) {
~ 			throw new DecoderException(
~ 					"The received array length is longer than maximum allowed (" + i + " > " + maxLen + ")");
~ 		}
~ 		byte[] abyte = new byte[i];

> CHANGE  66 : 67  @  66 : 67

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

> CHANGE  88 : 94  @  88 : 90

~ 	public byte[] toBytes() {
~ 		int readerIndex = buf.readerIndex();
~ 		int writerIndex = buf.writerIndex();
~ 		byte[] bytes = new byte[writerIndex - readerIndex];
~ 		buf.getBytes(readerIndex, bytes);
~ 		return bytes;

> DELETE  2  @  2 : 17

> EOF
