
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> CHANGE  4 : 10  @  4 : 8

~ 
~ import com.google.common.collect.Lists;
~ import com.google.common.collect.Maps;
~ 
~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

> DELETE  66  @  66 : 68

> INSERT  4 : 6  @  4

+ 	private static final Map<String, EntityConstructor<? extends Entity>> stringToConstructorMapping = Maps
+ 			.newHashMap();

> INSERT  2 : 3  @  2

+ 	private static final Map<Integer, EntityConstructor<? extends Entity>> idToConstructorMapping = Maps.newHashMap();

> CHANGE  4 : 6  @  4 : 5

~ 	private static void addMapping(Class<? extends Entity> entityClass,
~ 			EntityConstructor<? extends Entity> entityConstructor, String entityName, int id) {

> INSERT  10 : 11  @  10

+ 			stringToConstructorMapping.put(entityName, entityConstructor);

> INSERT  2 : 3  @  2

+ 			idToConstructorMapping.put(Integer.valueOf(id), entityConstructor);

> CHANGE  5 : 7  @  5 : 6

~ 	private static void addMapping(Class<? extends Entity> entityClass,
~ 			EntityConstructor<? extends Entity> entityConstructor, String entityName, int entityID, int baseColor,

> CHANGE  1 : 2  @  1 : 2

~ 		addMapping(entityClass, entityConstructor, entityName, entityID);

> CHANGE  7 : 10  @  7 : 11

~ 			EntityConstructor<? extends Entity> constructor = stringToConstructorMapping.get(entityName);
~ 			if (constructor != null) {
~ 				entity = constructor.createEntity(worldIn);

> CHANGE  2 : 3  @  2 : 3

~ 			logger.error("Could not create entity", exception);

> CHANGE  13 : 16  @  13 : 17

~ 			EntityConstructor<? extends Entity> constructor = stringToConstructorMapping.get(nbt.getString("id"));
~ 			if (constructor != null) {
~ 				entity = constructor.createEntity(worldIn);

> CHANGE  2 : 3  @  2 : 3

~ 			logger.error("Could not create entity", exception);

> CHANGE  15 : 18  @  15 : 19

~ 			EntityConstructor<? extends Entity> constructor = getConstructorFromID(entityID);
~ 			if (constructor != null) {
~ 				entity = constructor.createEntity(worldIn);

> CHANGE  2 : 3  @  2 : 3

~ 			logger.error("Could not create entity", exception);

> INSERT  18 : 22  @  18

+ 	public static EntityConstructor<? extends Entity> getConstructorFromID(int entityID) {
+ 		return idToConstructorMapping.get(Integer.valueOf(entityID));
+ 	}
+ 

> CHANGE  17 : 18  @  17 : 18

~ 		Set<String> set = stringToClassMapping.keySet();

> CHANGE  29 : 98  @  29 : 91

~ 		addMapping(EntityItem.class, (w) -> new EntityItem(w), "Item", 1);
~ 		addMapping(EntityXPOrb.class, (w) -> new EntityXPOrb(w), "XPOrb", 2);
~ 		addMapping(EntityEgg.class, (w) -> new EntityEgg(w), "ThrownEgg", 7);
~ 		addMapping(EntityLeashKnot.class, (w) -> new EntityLeashKnot(w), "LeashKnot", 8);
~ 		addMapping(EntityPainting.class, (w) -> new EntityPainting(w), "Painting", 9);
~ 		addMapping(EntityArrow.class, (w) -> new EntityArrow(w), "Arrow", 10);
~ 		addMapping(EntitySnowball.class, (w) -> new EntitySnowball(w), "Snowball", 11);
~ 		addMapping(EntityLargeFireball.class, (w) -> new EntityLargeFireball(w), "Fireball", 12);
~ 		addMapping(EntitySmallFireball.class, (w) -> new EntitySmallFireball(w), "SmallFireball", 13);
~ 		addMapping(EntityEnderPearl.class, (w) -> new EntityEnderPearl(w), "ThrownEnderpearl", 14);
~ 		addMapping(EntityEnderEye.class, (w) -> new EntityEnderEye(w), "EyeOfEnderSignal", 15);
~ 		addMapping(EntityPotion.class, (w) -> new EntityPotion(w), "ThrownPotion", 16);
~ 		addMapping(EntityExpBottle.class, (w) -> new EntityExpBottle(w), "ThrownExpBottle", 17);
~ 		addMapping(EntityItemFrame.class, (w) -> new EntityItem(w), "ItemFrame", 18);
~ 		addMapping(EntityWitherSkull.class, (w) -> new EntityWitherSkull(w), "WitherSkull", 19);
~ 		addMapping(EntityTNTPrimed.class, (w) -> new EntityTNTPrimed(w), "PrimedTnt", 20);
~ 		addMapping(EntityFallingBlock.class, (w) -> new EntityFallingBlock(w), "FallingSand", 21);
~ 		addMapping(EntityFireworkRocket.class, (w) -> new EntityFireworkRocket(w), "FireworksRocketEntity", 22);
~ 		addMapping(EntityArmorStand.class, (w) -> new EntityArmorStand(w), "ArmorStand", 30);
~ 		addMapping(EntityBoat.class, (w) -> new EntityBoat(w), "Boat", 41);
~ 		addMapping(EntityMinecartEmpty.class, (w) -> new EntityMinecartEmpty(w),
~ 				EntityMinecart.EnumMinecartType.RIDEABLE.getName(), 42);
~ 		addMapping(EntityMinecartChest.class, (w) -> new EntityMinecartChest(w),
~ 				EntityMinecart.EnumMinecartType.CHEST.getName(), 43);
~ 		addMapping(EntityMinecartFurnace.class, (w) -> new EntityMinecartFurnace(w),
~ 				EntityMinecart.EnumMinecartType.FURNACE.getName(), 44);
~ 		addMapping(EntityMinecartTNT.class, (w) -> new EntityMinecartTNT(w),
~ 				EntityMinecart.EnumMinecartType.TNT.getName(), 45);
~ 		addMapping(EntityMinecartHopper.class, (w) -> new EntityMinecartHopper(w),
~ 				EntityMinecart.EnumMinecartType.HOPPER.getName(), 46);
~ 		addMapping(EntityMinecartMobSpawner.class, (w) -> new EntityMinecartMobSpawner(w),
~ 				EntityMinecart.EnumMinecartType.SPAWNER.getName(), 47);
~ 		addMapping(EntityMinecartCommandBlock.class, (w) -> new EntityMinecartCommandBlock(w),
~ 				EntityMinecart.EnumMinecartType.COMMAND_BLOCK.getName(), 40);
~ 		addMapping(EntityLiving.class, null, "Mob", 48);
~ 		addMapping(EntityMob.class, null, "Monster", 49);
~ 		addMapping(EntityCreeper.class, (w) -> new EntityCreeper(w), "Creeper", 50, 894731, 0);
~ 		addMapping(EntitySkeleton.class, (w) -> new EntitySkeleton(w), "Skeleton", 51, 12698049, 4802889);
~ 		addMapping(EntitySpider.class, (w) -> new EntitySpider(w), "Spider", 52, 3419431, 11013646);
~ 		addMapping(EntityGiantZombie.class, (w) -> new EntityGiantZombie(w), "Giant", 53);
~ 		addMapping(EntityZombie.class, (w) -> new EntityZombie(w), "Zombie", 54, '\uafaf', 7969893);
~ 		addMapping(EntitySlime.class, (w) -> new EntitySlime(w), "Slime", 55, 5349438, 8306542);
~ 		addMapping(EntityGhast.class, (w) -> new EntityGhast(w), "Ghast", 56, 16382457, 12369084);
~ 		addMapping(EntityPigZombie.class, (w) -> new EntityPigZombie(w), "PigZombie", 57, 15373203, 5009705);
~ 		addMapping(EntityEnderman.class, (w) -> new EntityEnderman(w), "Enderman", 58, 1447446, 0);
~ 		addMapping(EntityCaveSpider.class, (w) -> new EntityCaveSpider(w), "CaveSpider", 59, 803406, 11013646);
~ 		addMapping(EntitySilverfish.class, (w) -> new EntitySilverfish(w), "Silverfish", 60, 7237230, 3158064);
~ 		addMapping(EntityBlaze.class, (w) -> new EntityBlaze(w), "Blaze", 61, 16167425, 16775294);
~ 		addMapping(EntityMagmaCube.class, (w) -> new EntityMagmaCube(w), "LavaSlime", 62, 3407872, 16579584);
~ 		addMapping(EntityDragon.class, (w) -> new EntityDragon(w), "EnderDragon", 63);
~ 		addMapping(EntityWither.class, (w) -> new EntityWither(w), "WitherBoss", 64);
~ 		addMapping(EntityBat.class, (w) -> new EntityBat(w), "Bat", 65, 4996656, 986895);
~ 		addMapping(EntityWitch.class, (w) -> new EntityWitch(w), "Witch", 66, 3407872, 5349438);
~ 		addMapping(EntityEndermite.class, (w) -> new EntityEndermite(w), "Endermite", 67, 1447446, 7237230);
~ 		addMapping(EntityGuardian.class, (w) -> new EntityGuardian(w), "Guardian", 68, 5931634, 15826224);
~ 		addMapping(EntityPig.class, (w) -> new EntityPig(w), "Pig", 90, 15771042, 14377823);
~ 		addMapping(EntitySheep.class, (w) -> new EntitySheep(w), "Sheep", 91, 15198183, 16758197);
~ 		addMapping(EntityCow.class, (w) -> new EntityCow(w), "Cow", 92, 4470310, 10592673);
~ 		addMapping(EntityChicken.class, (w) -> new EntityChicken(w), "Chicken", 93, 10592673, 16711680);
~ 		addMapping(EntitySquid.class, (w) -> new EntitySquid(w), "Squid", 94, 2243405, 7375001);
~ 		addMapping(EntityWolf.class, (w) -> new EntityWolf(w), "Wolf", 95, 14144467, 13545366);
~ 		addMapping(EntityMooshroom.class, (w) -> new EntityMooshroom(w), "MushroomCow", 96, 10489616, 12040119);
~ 		addMapping(EntitySnowman.class, (w) -> new EntitySnowman(w), "SnowMan", 97);
~ 		addMapping(EntityOcelot.class, (w) -> new EntityOcelot(w), "Ozelot", 98, 15720061, 5653556);
~ 		addMapping(EntityIronGolem.class, (w) -> new EntityIronGolem(w), "VillagerGolem", 99);
~ 		addMapping(EntityHorse.class, (w) -> new EntityHorse(w), "EntityHorse", 100, 12623485, 15656192);
~ 		addMapping(EntityRabbit.class, (w) -> new EntityRabbit(w), "Rabbit", 101, 10051392, 7555121);
~ 		addMapping(EntityVillager.class, (w) -> new EntityVillager(w), "Villager", 120, 5651507, 12422002);
~ 		addMapping(EntityEnderCrystal.class, (w) -> new EntityEnderCrystal(w), "EnderCrystal", 200);

> EOF
