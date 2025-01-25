
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 5

> INSERT  3 : 8  @  3

+ 
+ import com.google.common.base.Function;
+ import com.google.common.base.Joiner;
+ import com.google.common.collect.Iterables;
+ 

> CHANGE  2 : 3  @  2 : 3

~ import net.minecraft.util.ResourceLocation;

> CHANGE  16 : 17  @  16 : 17

~ 				(T) cyclePropertyValue(property.getAllowedValues(), this.getValue(property)));

> INSERT  30 : 59  @  30

+ 
+ 	private int blockId = -1;
+ 	private int blockStateId = -1;
+ 	private int metadata = -1;
+ 	private ResourceLocation blockLocation = null;
+ 
+ 	public int getBlockId() {
+ 		if (this.blockId < 0) {
+ 			this.blockId = Block.getIdFromBlock(this.getBlock());
+ 		}
+ 
+ 		return this.blockId;
+ 	}
+ 
+ 	public int getBlockStateId() {
+ 		if (this.blockStateId < 0) {
+ 			this.blockStateId = Block.getStateId(this);
+ 		}
+ 
+ 		return this.blockStateId;
+ 	}
+ 
+ 	public int getMetadata() {
+ 		if (this.metadata < 0) {
+ 			this.metadata = this.getBlock().getMetaFromState(this);
+ 		}
+ 
+ 		return this.metadata;
+ 	}

> EOF
