
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  5 : 8  @  5 : 8

~ import net.minecraft.nbt.NBTTagCompound;
~ import net.minecraft.nbt.NBTTagList;
~ import net.minecraft.nbt.NBTUtil;

> CHANGE  57 : 59  @  57 : 59

~ 			buffer.writeItemStackToBuffer_server(merchantrecipe.getItemToBuy());
~ 			buffer.writeItemStackToBuffer_server(merchantrecipe.getItemToSell());

> CHANGE  3 : 4  @  3 : 4

~ 				buffer.writeItemStackToBuffer_server(itemstack);

> CHANGE  14 : 16  @  14 : 16

~ 			ItemStack itemstack = buffer.readItemStackFromBuffer_server();
~ 			ItemStack itemstack1 = buffer.readItemStackFromBuffer_server();

> CHANGE  2 : 3  @  2 : 3

~ 				itemstack2 = buffer.readItemStackFromBuffer_server();

> EOF
