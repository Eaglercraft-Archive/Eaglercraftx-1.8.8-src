
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 4  @  2

+ import java.util.List;
+ 

> DELETE  2  @  2 : 3

> CHANGE  25 : 26  @  25 : 27

~ 			List<EntityCreature> lst = this.taskOwner.worldObj.getEntitiesWithinAABB(this.taskOwner.getClass(),

> CHANGE  2 : 5  @  2 : 3

~ 									.expand(d0, 10.0D, d0));
~ 			for (int i = 0, l = lst.size(); i < l; ++i) {
~ 				EntityCreature entitycreature = lst.get(i);

> CHANGE  4 : 6  @  4 : 6

~ 					for (int j = 0; j < this.targetClasses.length; ++j) {
~ 						if (entitycreature.getClass() == this.targetClasses[j]) {

> EOF
