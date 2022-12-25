
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 5  @  2

+ import java.util.Collection;
+ import java.util.Map;
+ 

> CHANGE  8 : 10  @  5 : 8

~ 
~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;

> DELETE  115  @  113 : 114

> CHANGE  262 : 263  @  261 : 262

~ 			this.directionMaps.put(direction, (BiMap<Integer, Class<? extends Packet>>) object);

> CHANGE  308 : 310  @  307 : 308

~ 				for (Class oclass : (Collection<Class>) ((BiMap) enumconnectionstate.directionMaps
~ 						.get(enumpacketdirection)).values()) {

> EOF
