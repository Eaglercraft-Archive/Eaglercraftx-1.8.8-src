
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> CHANGE  4 : 17  @  4 : 8

~ 
~ import com.carrotsearch.hppc.IntObjectHashMap;
~ import com.carrotsearch.hppc.IntObjectMap;
~ import com.carrotsearch.hppc.ObjectIntHashMap;
~ import com.carrotsearch.hppc.ObjectIntMap;
~ import com.google.common.collect.Lists;
~ import com.google.common.collect.Maps;
~ 
~ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
~ import net.lax1dude.eaglercraft.v1_8.internal.EnumPlatformType;
~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
~ import net.lax1dude.eaglercraft.v1_8.minecraft.EntityConstructor;

> DELETE  66  @  66 : 68

> INSERT  4 : 6  @  4

+ 	private static final Map<String, EntityConstructor<? extends Entity>> stringToConstructorMapping = Maps
+ 			.newHashMap();

> CHANGE  1 : 8  @  1 : 5

~ 	private static final IntObjectMap<Class<? extends Entity>> idToClassMapping = new IntObjectHashMap<>();
~ 	private static final IntObjectMap<EntityConstructor<? extends Entity>> idToConstructorMapping = new IntObjectHashMap<>();
~ 	private static final ObjectIntMap<Class<? extends Entity>> classToIDMapping = new ObjectIntHashMap<>();
~ 	private static final Map<Class<? extends Entity>, EntityConstructor<? extends Entity>> classToConstructorMapping = Maps
~ 			.newHashMap();
~ 	private static final ObjectIntMap<String> stringToIDMapping = new ObjectIntHashMap<>();
~ 	public static final IntObjectMap<EntityList.EntityEggInfo> entityEggs = new IntObjectHashMap<>();

> CHANGE  1 : 3  @  1 : 2

~ 	private static void addMapping(Class<? extends Entity> entityClass,
~ 			EntityConstructor<? extends Entity> entityConstructor, String entityName, int id) {

> CHANGE  2 : 3  @  2 : 3

~ 		} else if (idToClassMapping.containsKey(id)) {

> INSERT  7 : 8  @  7

+ 			stringToConstructorMapping.put(entityName, entityConstructor);

> CHANGE  1 : 6  @  1 : 4

~ 			idToClassMapping.put(id, entityClass);
~ 			idToConstructorMapping.put(id, entityConstructor);
~ 			classToIDMapping.put(entityClass, id);
~ 			classToConstructorMapping.put(entityClass, entityConstructor);
~ 			stringToIDMapping.put(entityName, id);

> CHANGE  3 : 5  @  3 : 4

~ 	private static void addMapping(Class<? extends Entity> entityClass,
~ 			EntityConstructor<? extends Entity> entityConstructor, String entityName, int entityID, int baseColor,

> CHANGE  1 : 3  @  1 : 3

~ 		addMapping(entityClass, entityConstructor, entityName, entityID);
~ 		entityEggs.put(entityID, new EntityList.EntityEggInfo(entityID, baseColor, spotColor));

> CHANGE  6 : 9  @  6 : 10

~ 			EntityConstructor<? extends Entity> constructor = stringToConstructorMapping.get(entityName);
~ 			if (constructor != null) {
~ 				entity = constructor.createEntity(worldIn);

> CHANGE  2 : 3  @  2 : 3

~ 			logger.error("Could not create entity", exception);

> INSERT  5 : 28  @  5

+ 	public static Entity createEntityByClass(Class<? extends Entity> entityClass, World worldIn) {
+ 		Entity entity = null;
+ 
+ 		try {
+ 			EntityConstructor<? extends Entity> constructor = classToConstructorMapping.get(entityClass);
+ 			if (constructor != null) {
+ 				entity = constructor.createEntity(worldIn);
+ 			}
+ 		} catch (Exception exception) {
+ 			logger.error("Could not create entity", exception);
+ 		}
+ 
+ 		return entity;
+ 	}
+ 
+ 	public static Entity createEntityByClassUnsafe(Class<? extends Entity> entityClass, World worldIn) {
+ 		EntityConstructor<? extends Entity> constructor = classToConstructorMapping.get(entityClass);
+ 		if (constructor != null) {
+ 			return constructor.createEntity(worldIn);
+ 		}
+ 		return null;
+ 	}
+ 

> CHANGE  8 : 11  @  8 : 12

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

> CHANGE  10 : 12  @  10 : 12

~ 		int integer = classToIDMapping.getOrDefault(entityIn.getClass(), -1);
~ 		return integer == -1 ? 0 : integer;

> CHANGE  3 : 4  @  3 : 4

~ 		return idToClassMapping.get(entityID);

> INSERT  2 : 6  @  2

+ 	public static EntityConstructor<? extends Entity> getConstructorFromID(int entityID) {
+ 		return idToConstructorMapping.get(entityID);
+ 	}
+ 

> CHANGE  1 : 2  @  1 : 2

~ 		return classToStringMapping.get(entityIn.getClass());

> CHANGE  3 : 5  @  3 : 5

~ 		int integer = stringToIDMapping.getOrDefault(entityName, -1);
~ 		return integer == -1 ? 90 : integer;

> CHANGE  3 : 4  @  3 : 4

~ 		return classToStringMapping.get(getClassFromID(entityID));

> CHANGE  6 : 7  @  6 : 7

~ 		Set<String> set = stringToClassMapping.keySet();

> CHANGE  2 : 9  @  2 : 6

~ 		// TODO: Eventually TeaVM will support getModifiers
~ 		if (EagRuntime.getPlatformType() != EnumPlatformType.WASM_GC) {
~ 			for (String s : set) {
~ 				Class oclass = (Class) stringToClassMapping.get(s);
~ 				if ((oclass.getModifiers() & 1024) != 1024) {
~ 					arraylist.add(s);
~ 				}

> INSERT  1 : 3  @  1

+ 		} else {
+ 			arraylist.addAll(set);

> CHANGE  22 : 90  @  22 : 84

~ 		addMapping(EntityItem.class, EntityItem::new, "Item", 1);
~ 		addMapping(EntityXPOrb.class, EntityXPOrb::new, "XPOrb", 2);
~ 		addMapping(EntityEgg.class, EntityEgg::new, "ThrownEgg", 7);
~ 		addMapping(EntityLeashKnot.class, EntityLeashKnot::new, "LeashKnot", 8);
~ 		addMapping(EntityPainting.class, EntityPainting::new, "Painting", 9);
~ 		addMapping(EntityArrow.class, EntityArrow::new, "Arrow", 10);
~ 		addMapping(EntitySnowball.class, EntitySnowball::new, "Snowball", 11);
~ 		addMapping(EntityLargeFireball.class, EntityLargeFireball::new, "Fireball", 12);
~ 		addMapping(EntitySmallFireball.class, EntitySmallFireball::new, "SmallFireball", 13);
~ 		addMapping(EntityEnderPearl.class, EntityEnderPearl::new, "ThrownEnderpearl", 14);
~ 		addMapping(EntityEnderEye.class, EntityEnderEye::new, "EyeOfEnderSignal", 15);
~ 		addMapping(EntityPotion.class, EntityPotion::new, "ThrownPotion", 16);
~ 		addMapping(EntityExpBottle.class, EntityExpBottle::new, "ThrownExpBottle", 17);
~ 		addMapping(EntityItemFrame.class, EntityItemFrame::new, "ItemFrame", 18);
~ 		addMapping(EntityWitherSkull.class, EntityWitherSkull::new, "WitherSkull", 19);
~ 		addMapping(EntityTNTPrimed.class, EntityTNTPrimed::new, "PrimedTnt", 20);
~ 		addMapping(EntityFallingBlock.class, EntityFallingBlock::new, "FallingSand", 21);
~ 		addMapping(EntityFireworkRocket.class, EntityFireworkRocket::new, "FireworksRocketEntity", 22);
~ 		addMapping(EntityArmorStand.class, EntityArmorStand::new, "ArmorStand", 30);
~ 		addMapping(EntityBoat.class, EntityBoat::new, "Boat", 41);
~ 		addMapping(EntityMinecartEmpty.class, EntityMinecartEmpty::new,
~ 				EntityMinecart.EnumMinecartType.RIDEABLE.getName(), 42);
~ 		addMapping(EntityMinecartChest.class, EntityMinecartChest::new, EntityMinecart.EnumMinecartType.CHEST.getName(),
~ 				43);
~ 		addMapping(EntityMinecartFurnace.class, EntityMinecartFurnace::new,
~ 				EntityMinecart.EnumMinecartType.FURNACE.getName(), 44);
~ 		addMapping(EntityMinecartTNT.class, EntityMinecartTNT::new, EntityMinecart.EnumMinecartType.TNT.getName(), 45);
~ 		addMapping(EntityMinecartHopper.class, EntityMinecartHopper::new,
~ 				EntityMinecart.EnumMinecartType.HOPPER.getName(), 46);
~ 		addMapping(EntityMinecartMobSpawner.class, EntityMinecartMobSpawner::new,
~ 				EntityMinecart.EnumMinecartType.SPAWNER.getName(), 47);
~ 		addMapping(EntityMinecartCommandBlock.class, EntityMinecartCommandBlock::new,
~ 				EntityMinecart.EnumMinecartType.COMMAND_BLOCK.getName(), 40);
~ 		addMapping(EntityLiving.class, null, "Mob", 48);
~ 		addMapping(EntityMob.class, null, "Monster", 49);
~ 		addMapping(EntityCreeper.class, EntityCreeper::new, "Creeper", 50, 894731, 0);
~ 		addMapping(EntitySkeleton.class, EntitySkeleton::new, "Skeleton", 51, 12698049, 4802889);
~ 		addMapping(EntitySpider.class, EntitySpider::new, "Spider", 52, 3419431, 11013646);
~ 		addMapping(EntityGiantZombie.class, EntityGiantZombie::new, "Giant", 53);
~ 		addMapping(EntityZombie.class, EntityZombie::new, "Zombie", 54, '\uafaf', 7969893);
~ 		addMapping(EntitySlime.class, EntitySlime::new, "Slime", 55, 5349438, 8306542);
~ 		addMapping(EntityGhast.class, EntityGhast::new, "Ghast", 56, 16382457, 12369084);
~ 		addMapping(EntityPigZombie.class, EntityPigZombie::new, "PigZombie", 57, 15373203, 5009705);
~ 		addMapping(EntityEnderman.class, EntityEnderman::new, "Enderman", 58, 1447446, 0);
~ 		addMapping(EntityCaveSpider.class, EntityCaveSpider::new, "CaveSpider", 59, 803406, 11013646);
~ 		addMapping(EntitySilverfish.class, EntitySilverfish::new, "Silverfish", 60, 7237230, 3158064);
~ 		addMapping(EntityBlaze.class, EntityBlaze::new, "Blaze", 61, 16167425, 16775294);
~ 		addMapping(EntityMagmaCube.class, EntityMagmaCube::new, "LavaSlime", 62, 3407872, 16579584);
~ 		addMapping(EntityDragon.class, EntityDragon::new, "EnderDragon", 63);
~ 		addMapping(EntityWither.class, EntityWither::new, "WitherBoss", 64);
~ 		addMapping(EntityBat.class, EntityBat::new, "Bat", 65, 4996656, 986895);
~ 		addMapping(EntityWitch.class, EntityWitch::new, "Witch", 66, 3407872, 5349438);
~ 		addMapping(EntityEndermite.class, EntityEndermite::new, "Endermite", 67, 1447446, 7237230);
~ 		addMapping(EntityGuardian.class, EntityGuardian::new, "Guardian", 68, 5931634, 15826224);
~ 		addMapping(EntityPig.class, EntityPig::new, "Pig", 90, 15771042, 14377823);
~ 		addMapping(EntitySheep.class, EntitySheep::new, "Sheep", 91, 15198183, 16758197);
~ 		addMapping(EntityCow.class, EntityCow::new, "Cow", 92, 4470310, 10592673);
~ 		addMapping(EntityChicken.class, EntityChicken::new, "Chicken", 93, 10592673, 16711680);
~ 		addMapping(EntitySquid.class, EntitySquid::new, "Squid", 94, 2243405, 7375001);
~ 		addMapping(EntityWolf.class, EntityWolf::new, "Wolf", 95, 14144467, 13545366);
~ 		addMapping(EntityMooshroom.class, EntityMooshroom::new, "MushroomCow", 96, 10489616, 12040119);
~ 		addMapping(EntitySnowman.class, EntitySnowman::new, "SnowMan", 97);
~ 		addMapping(EntityOcelot.class, EntityOcelot::new, "Ozelot", 98, 15720061, 5653556);
~ 		addMapping(EntityIronGolem.class, EntityIronGolem::new, "VillagerGolem", 99);
~ 		addMapping(EntityHorse.class, EntityHorse::new, "EntityHorse", 100, 12623485, 15656192);
~ 		addMapping(EntityRabbit.class, EntityRabbit::new, "Rabbit", 101, 10051392, 7555121);
~ 		addMapping(EntityVillager.class, EntityVillager::new, "Villager", 120, 5651507, 12422002);
~ 		addMapping(EntityEnderCrystal.class, EntityEnderCrystal::new, "EnderCrystal", 200);

> EOF
