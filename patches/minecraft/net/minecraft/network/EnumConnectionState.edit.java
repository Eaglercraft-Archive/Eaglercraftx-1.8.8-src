
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 5  @  2

+ import java.util.Collection;
+ import java.util.Map;
+ 

> CHANGE  3 : 5  @  3 : 6

~ 
~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;

> DELETE  105  @  105 : 106

> CHANGE  147 : 148  @  147 : 148

~ 			this.directionMaps.put(direction, (BiMap<Integer, Class<? extends Packet>>) object);

> CHANGE  45 : 47  @  45 : 46

~ 				for (Class oclass : (Collection<Class>) ((BiMap) enumconnectionstate.directionMaps
~ 						.get(enumpacketdirection)).values()) {

> EOF
