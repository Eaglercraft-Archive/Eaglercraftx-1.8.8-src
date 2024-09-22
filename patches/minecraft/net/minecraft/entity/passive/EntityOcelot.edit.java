
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  20  @  20 : 23

> CHANGE  215 : 216  @  215 : 217

~ 		return getNameImpl(false);

> INSERT  2 : 12  @  2

+ 	public String getNameProfanityFilter() {
+ 		return getNameImpl(true);
+ 	}
+ 
+ 	private String getNameImpl(boolean filter) {
+ 		return this.hasCustomName() ? (filter ? this.getCustomNameTagProfanityFilter() : this.getCustomNameTag())
+ 				: (this.isTamed() ? StatCollector.translateToLocal("entity.Cat.name")
+ 						: (filter ? super.getNameProfanityFilter() : super.getName()));
+ 	}
+ 

> EOF
