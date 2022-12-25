
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 10

> DELETE  8  @  16 : 18

> CHANGE  9 : 18  @  19 : 20

~ import java.nio.charset.StandardCharsets;
~ 
~ import net.lax1dude.eaglercraft.v1_8.DecoderException;
~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
~ import net.lax1dude.eaglercraft.v1_8.EncoderException;
~ 
~ import net.lax1dude.eaglercraft.v1_8.netty.ByteBuf;
~ import net.lax1dude.eaglercraft.v1_8.netty.ByteBufInputStream;
~ import net.lax1dude.eaglercraft.v1_8.netty.ByteBufOutputStream;

> CHANGE  116 : 117  @  118 : 119

~ 	public void writeUuid(EaglercraftUUID uuid) {

> CHANGE  121 : 123  @  123 : 125

~ 	public EaglercraftUUID readUuid() {
~ 		return new EaglercraftUUID(this.readLong(), this.readLong());

> CHANGE  205 : 206  @  207 : 208

~ 			String s = new String(this.readBytes(i).array(), StandardCharsets.UTF_8);

> CHANGE  216 : 217  @  218 : 219

~ 		byte[] abyte = string.getBytes(StandardCharsets.UTF_8);

> DELETE  238  @  240 : 244

> CHANGE  391 : 396  @  397 : 398

~ 		if (parByteBuf instanceof PacketBuffer) {
~ 			return this.buf.getBytes(parInt1, ((PacketBuffer) parByteBuf).buf);
~ 		} else {
~ 			return this.buf.getBytes(parInt1, parByteBuf);
~ 		}

> CHANGE  399 : 404  @  401 : 402

~ 		if (bytebuf instanceof PacketBuffer) {
~ 			return this.buf.getBytes(i, ((PacketBuffer) bytebuf).buf, j);
~ 		} else {
~ 			return this.buf.getBytes(i, bytebuf, j);
~ 		}

> CHANGE  407 : 412  @  405 : 406

~ 		if (bytebuf instanceof PacketBuffer) {
~ 			return this.buf.getBytes(i, ((PacketBuffer) bytebuf).buf, j, k);
~ 		} else {
~ 			return this.buf.getBytes(i, bytebuf, j, k);
~ 		}

> DELETE  430  @  424 : 428

> CHANGE  471 : 476  @  469 : 470

~ 		if (bytebuf instanceof PacketBuffer) {
~ 			return this.buf.setBytes(i, ((PacketBuffer) bytebuf).buf, j);
~ 		} else {
~ 			return this.buf.setBytes(i, bytebuf, j);
~ 		}

> CHANGE  479 : 484  @  473 : 474

~ 		if (bytebuf instanceof PacketBuffer) {
~ 			return this.buf.setBytes(i, ((PacketBuffer) bytebuf).buf, j, k);
~ 		} else {
~ 			return this.buf.setBytes(i, bytebuf, j, k);
~ 		}

> DELETE  502  @  492 : 496

> CHANGE  567 : 572  @  561 : 562

~ 		if (bytebuf instanceof PacketBuffer) {
~ 			return this.buf.readBytes(((PacketBuffer) bytebuf).buf);
~ 		} else {
~ 			return this.buf.readBytes(bytebuf);
~ 		}

> CHANGE  575 : 580  @  565 : 566

~ 		if (bytebuf instanceof PacketBuffer) {
~ 			return this.buf.readBytes(((PacketBuffer) bytebuf).buf, i);
~ 		} else {
~ 			return this.buf.readBytes(bytebuf, i);
~ 		}

> CHANGE  583 : 588  @  569 : 570

~ 		if (bytebuf instanceof PacketBuffer) {
~ 			return this.buf.readBytes(((PacketBuffer) bytebuf).buf, i, j);
~ 		} else {
~ 			return this.buf.readBytes(bytebuf, i, j);
~ 		}

> DELETE  606  @  588 : 592

> CHANGE  647 : 652  @  633 : 634

~ 		if (parByteBuf instanceof PacketBuffer) {
~ 			return this.buf.writeBytes(((PacketBuffer) parByteBuf).buf);
~ 		} else {
~ 			return this.buf.writeBytes(parByteBuf);
~ 		}

> CHANGE  655 : 660  @  637 : 638

~ 		if (bytebuf instanceof PacketBuffer) {
~ 			return this.buf.writeBytes(((PacketBuffer) bytebuf).buf, i);
~ 		} else {
~ 			return this.buf.writeBytes(bytebuf, i);
~ 		}

> CHANGE  663 : 668  @  641 : 642

~ 		if (bytebuf instanceof PacketBuffer) {
~ 			return this.buf.writeBytes(((PacketBuffer) bytebuf).buf, i, j);
~ 		} else {
~ 			return this.buf.writeBytes(bytebuf, i, j);
~ 		}

> DELETE  686  @  660 : 664

> DELETE  706  @  684 : 700

> DELETE  794  @  788 : 807

> EOF
