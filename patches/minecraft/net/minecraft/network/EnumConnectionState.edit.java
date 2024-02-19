
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  5 : 7  @  5

+ 
+ import java.util.Collection;

> CHANGE  104 : 105  @  104 : 109

~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;

> DELETE  111  @  111 : 119

> CHANGE  28 : 29  @  28 : 29

~ 			this.directionMaps.put(direction, (BiMap<Integer, Class<? extends Packet>>) object);

> CHANGE  45 : 47  @  45 : 46

~ 				for (Class oclass : (Collection<Class>) ((BiMap) enumconnectionstate.directionMaps
~ 						.get(enumpacketdirection)).values()) {

> EOF
