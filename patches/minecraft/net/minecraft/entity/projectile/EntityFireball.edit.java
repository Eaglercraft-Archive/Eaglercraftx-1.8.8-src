
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  3 : 4  @  3

+ 

> INSERT  52 : 56  @  52

+ 		if (d0 == 0.0) {
+ 			this.accelerationX = this.accelerationY = this.accelerationZ = 0.0D;
+ 			return;
+ 		}

> INSERT  16 : 20  @  16

+ 		if (d0 == 0.0) {
+ 			this.accelerationX = this.accelerationY = this.accelerationZ = 0.0D;
+ 			return;
+ 		}

> CHANGE  46 : 47  @  46 : 47

~ 			for (int i = 0, l = list.size(); i < l; ++i) {

> INSERT  156 : 160  @  156

+ 	protected float getEaglerDynamicLightsValueSimple(float partialTicks) {
+ 		return 1.0f;
+ 	}
+ 

> EOF
