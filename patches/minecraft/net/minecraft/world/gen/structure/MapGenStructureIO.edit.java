
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  4 : 6  @  4

+ import java.util.function.Supplier;
+ 

> CHANGE  2 : 4  @  2 : 18

~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

> INSERT  4 : 5  @  4

+ 	private static Map<String, Supplier<? extends StructureStart>> startNameToSupplierMap = Maps.newHashMap();

> INSERT  2 : 3  @  2

+ 	private static Map<String, Supplier<? extends StructureComponent>> componentNameToSupplierMap = Maps.newHashMap();

> CHANGE  2 : 4  @  2 : 3

~ 	private static void registerStructure(Class<? extends StructureStart> startClass,
~ 			Supplier<? extends StructureStart> startSupplier, String structureName) {

> INSERT  1 : 2  @  1

+ 		startNameToSupplierMap.put(structureName, startSupplier);

> CHANGE  3 : 5  @  3 : 4

~ 	static void registerStructureComponent(Class<? extends StructureComponent> componentClass,
~ 			Supplier<? extends StructureComponent> startSupplier, String componentName) {

> INSERT  1 : 2  @  1

+ 		componentNameToSupplierMap.put(componentName, startSupplier);

> CHANGE  15 : 16  @  15 : 16

~ 			Supplier<? extends StructureStart> oclass = startNameToSupplierMap.get(tagCompound.getString("id"));

> CHANGE  1 : 2  @  1 : 2

~ 				structurestart = oclass.get();

> CHANGE  3 : 4  @  3 : 4

~ 			logger.warn(exception);

> CHANGE  15 : 16  @  15 : 16

~ 			Supplier<? extends StructureComponent> oclass = componentNameToSupplierMap.get(tagCompound.getString("id"));

> CHANGE  1 : 2  @  1 : 2

~ 				structurecomponent = oclass.get();

> CHANGE  3 : 4  @  3 : 4

~ 			logger.warn(exception);

> CHANGE  12 : 19  @  12 : 18

~ 		registerStructure(StructureMineshaftStart.class, StructureMineshaftStart::new, "Mineshaft");
~ 		registerStructure(MapGenVillage.Start.class, MapGenVillage.Start::new, "Village");
~ 		registerStructure(MapGenNetherBridge.Start.class, MapGenNetherBridge.Start::new, "Fortress");
~ 		registerStructure(MapGenStronghold.Start.class, MapGenStronghold.Start::new, "Stronghold");
~ 		registerStructure(MapGenScatteredFeature.Start.class, MapGenScatteredFeature.Start::new, "Temple");
~ 		registerStructure(StructureOceanMonument.StartMonument.class, StructureOceanMonument.StartMonument::new,
~ 				"Monument");

> EOF
