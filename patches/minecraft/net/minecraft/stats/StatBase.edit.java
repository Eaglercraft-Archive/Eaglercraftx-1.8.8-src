
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  5 : 7  @  5

+ import java.util.function.Supplier;
+ 

> DELETE  2  @  2 : 5

> INSERT  12 : 13  @  12

+ 	private Supplier<? extends IJsonSerializable> field_150956_d_ctor;

> CHANGE  114 : 120  @  114 : 115

~ 	public Supplier<? extends IJsonSerializable> func_150954_l_ctor() {
~ 		return this.field_150956_d_ctor;
~ 	}
~ 
~ 	public StatBase func_150953_b(Class<? extends IJsonSerializable> oclass,
~ 			Supplier<? extends IJsonSerializable> octor) {

> INSERT  1 : 2  @  1

+ 		this.field_150956_d_ctor = octor;

> EOF
