
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> INSERT  2 : 9  @  2

+ import java.util.function.Supplier;
+ 
+ import com.google.common.collect.Maps;
+ 
+ import net.lax1dude.eaglercraft.v1_8.HString;
+ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
+ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

> DELETE  7  @  7 : 27

> DELETE  2  @  2 : 4

> INSERT  4 : 5  @  4

+ 	private static Map<String, Supplier<? extends TileEntity>> nameToCtorMap = Maps.newHashMap();

> CHANGE  7 : 8  @  7 : 8

~ 	private static void addMapping(Class<? extends TileEntity> cl, Supplier<? extends TileEntity> ct, String id) {

> INSERT  4 : 5  @  4

+ 			nameToCtorMap.put(id, ct);

> CHANGE  37 : 38  @  37 : 38

~ 			Supplier<? extends TileEntity> oclass = nameToCtorMap.get(nbt.getString("id"));

> CHANGE  1 : 2  @  1 : 2

~ 				tileentity = (TileEntity) oclass.get();

> CHANGE  2 : 4  @  2 : 3

~ 			logger.error("Could not create TileEntity");
~ 			logger.error(exception);

> CHANGE  84 : 85  @  84 : 85

~ 						+ TileEntity.this.getClass().getName();

> CHANGE  10 : 11  @  10 : 11

~ 						return HString.format("ID #%d (%s // %s)",

> CHANGE  1 : 2  @  1 : 2

~ 										Block.getBlockById(i).getClass().getName() });

> CHANGE  12 : 14  @  12 : 14

~ 						String s = HString.format("%4s", new Object[] { Integer.toBinaryString(i) }).replace(" ", "0");
~ 						return HString.format("%1$d / 0x%1$X / 0b%2$s", new Object[] { Integer.valueOf(i), s });

> CHANGE  15 : 36  @  15 : 36

~ 		addMapping(TileEntityFurnace.class, TileEntityFurnace::new, "Furnace");
~ 		addMapping(TileEntityChest.class, TileEntityChest::new, "Chest");
~ 		addMapping(TileEntityEnderChest.class, TileEntityEnderChest::new, "EnderChest");
~ 		addMapping(BlockJukebox.TileEntityJukebox.class, BlockJukebox.TileEntityJukebox::new, "RecordPlayer");
~ 		addMapping(TileEntityDispenser.class, TileEntityDispenser::new, "Trap");
~ 		addMapping(TileEntityDropper.class, TileEntityDropper::new, "Dropper");
~ 		addMapping(TileEntitySign.class, TileEntitySign::new, "Sign");
~ 		addMapping(TileEntityMobSpawner.class, TileEntityMobSpawner::new, "MobSpawner");
~ 		addMapping(TileEntityNote.class, TileEntityNote::new, "Music");
~ 		addMapping(TileEntityPiston.class, TileEntityPiston::new, "Piston");
~ 		addMapping(TileEntityBrewingStand.class, TileEntityBrewingStand::new, "Cauldron");
~ 		addMapping(TileEntityEnchantmentTable.class, TileEntityEnchantmentTable::new, "EnchantTable");
~ 		addMapping(TileEntityEndPortal.class, TileEntityEndPortal::new, "Airportal");
~ 		addMapping(TileEntityCommandBlock.class, TileEntityCommandBlock::new, "Control");
~ 		addMapping(TileEntityBeacon.class, TileEntityBeacon::new, "Beacon");
~ 		addMapping(TileEntitySkull.class, TileEntitySkull::new, "Skull");
~ 		addMapping(TileEntityDaylightDetector.class, TileEntityDaylightDetector::new, "DLDetector");
~ 		addMapping(TileEntityHopper.class, TileEntityHopper::new, "Hopper");
~ 		addMapping(TileEntityComparator.class, TileEntityComparator::new, "Comparator");
~ 		addMapping(TileEntityFlowerPot.class, TileEntityFlowerPot::new, "FlowerPot");
~ 		addMapping(TileEntityBanner.class, TileEntityBanner::new, "Banner");

> EOF
