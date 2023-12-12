
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 4  @  2 : 11

~ import static net.lax1dude.eaglercraft.v1_8.sp.server.classes.ContextUtil.__checkIntegratedContextValid;
~ 

> CHANGE  1 : 3  @  1 : 9

~ 
~ import net.lax1dude.eaglercraft.v1_8.netty.ByteBuf;

> CHANGE  2 : 3  @  2 : 5

~ import net.minecraft.nbt.NBTTagCompound;

> CHANGE  3 : 4  @  3 : 4

~ public class PacketBuffer extends net.minecraft.network.PacketBuffer {

> DELETE  1  @  1 : 3

> DELETE  4  @  4 : 6

> CHANGE  1 : 2  @  1 : 2

~ 		super(wrapped);

> CHANGE  2 : 3  @  2 : 24

~ 	public BlockPos readBlockPos_server() {

> CHANGE  3 : 4  @  3 : 4

~ 	public void writeBlockPos_server(BlockPos pos) {

> CHANGE  3 : 4  @  3 : 4

~ 	public IChatComponent readChatComponent_server() throws IOException {

> CHANGE  3 : 4  @  3 : 4

~ 	public void writeChatComponent_server(IChatComponent component) throws IOException {

> CHANGE  3 : 4  @  3 : 101

~ 	public void writeItemStackToBuffer_server(ItemStack stack) {

> CHANGE  16 : 17  @  16 : 17

~ 	public ItemStack readItemStackFromBuffer_server() throws IOException {

> DELETE  12  @  12 : 620

> EOF
