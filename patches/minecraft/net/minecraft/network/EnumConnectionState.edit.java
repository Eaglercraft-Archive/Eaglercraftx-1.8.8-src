
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 5  @  2

+ import java.util.Collection;
+ import java.util.Map;
+ 

> CHANGE  6 : 8  @  3 : 6

~ 
~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;

> DELETE  107  @  108 : 109

> CHANGE  147 : 148  @  148 : 149

~ 			this.directionMaps.put(direction, (BiMap<Integer, Class<? extends Packet>>) object);

> CHANGE  46 : 48  @  46 : 47

~ 				for (Class oclass : (Collection<Class>) ((BiMap) enumconnectionstate.directionMaps
~ 						.get(enumpacketdirection)).values()) {

> EOF
