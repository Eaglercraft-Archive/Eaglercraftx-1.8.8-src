
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 7  @  2 : 13

~ import org.json.JSONException;
~ import org.json.JSONObject;
~ 
~ import net.lax1dude.eaglercraft.v1_8.json.JSONTypeCodec;
~ import net.lax1dude.eaglercraft.v1_8.json.JSONTypeProvider;

> DELETE  164  @  164 : 167

> CHANGE  84 : 85  @  84 : 86

~ 					return JSONTypeProvider.deserialize(parString1, ChunkProviderSettings.Factory.class);

> CHANGE  7 : 8  @  7 : 8

~ 			return JSONTypeProvider.serialize(this).toString();

> CHANGE  365 : 367  @  365 : 370

~ 	public static class Serializer implements JSONTypeCodec<ChunkProviderSettings.Factory, JSONObject> {
~ 		public ChunkProviderSettings.Factory deserialize(JSONObject jsonobject) throws JSONException {

> CHANGE  3 : 4  @  3 : 4

~ 				chunkprovidersettings$factory.coordinateScale = jsonobject.optFloat("coordinateScale",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.heightScale = jsonobject.optFloat("heightScale",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.lowerLimitScale = jsonobject.optFloat("lowerLimitScale",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.upperLimitScale = jsonobject.optFloat("upperLimitScale",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.depthNoiseScaleX = jsonobject.optFloat("depthNoiseScaleX",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.depthNoiseScaleZ = jsonobject.optFloat("depthNoiseScaleZ",

> CHANGE  1 : 4  @  1 : 4

~ 				chunkprovidersettings$factory.depthNoiseScaleExponent = jsonobject.optFloat("depthNoiseScaleExponent",
~ 						chunkprovidersettings$factory.depthNoiseScaleExponent);
~ 				chunkprovidersettings$factory.mainNoiseScaleX = jsonobject.optFloat("mainNoiseScaleX",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.mainNoiseScaleY = jsonobject.optFloat("mainNoiseScaleY",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.mainNoiseScaleZ = jsonobject.optFloat("mainNoiseScaleZ",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.baseSize = jsonobject.optFloat("baseSize",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.stretchY = jsonobject.optFloat("stretchY",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.biomeDepthWeight = jsonobject.optFloat("biomeDepthWeight",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.biomeDepthOffset = jsonobject.optFloat("biomeDepthOffset",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.biomeScaleWeight = jsonobject.optFloat("biomeScaleWeight",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.biomeScaleOffset = jsonobject.optFloat("biomeScaleOffset",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.seaLevel = jsonobject.optInt("seaLevel",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.useCaves = jsonobject.optBoolean("useCaves",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.useDungeons = jsonobject.optBoolean("useDungeons",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.dungeonChance = jsonobject.optInt("dungeonChance",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.useStrongholds = jsonobject.optBoolean("useStrongholds",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.useVillages = jsonobject.optBoolean("useVillages",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.useMineShafts = jsonobject.optBoolean("useMineShafts",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.useTemples = jsonobject.optBoolean("useTemples",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.useMonuments = jsonobject.optBoolean("useMonuments",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.useRavines = jsonobject.optBoolean("useRavines",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.useWaterLakes = jsonobject.optBoolean("useWaterLakes",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.waterLakeChance = jsonobject.optInt("waterLakeChance",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.useLavaLakes = jsonobject.optBoolean("useLavaLakes",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.lavaLakeChance = jsonobject.optInt("lavaLakeChance",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.useLavaOceans = jsonobject.optBoolean("useLavaOceans",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.fixedBiome = jsonobject.optInt("fixedBiome",

> CHANGE  9 : 10  @  9 : 10

~ 				chunkprovidersettings$factory.biomeSize = jsonobject.optInt("biomeSize",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.riverSize = jsonobject.optInt("riverSize",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.dirtSize = jsonobject.optInt("dirtSize",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.dirtCount = jsonobject.optInt("dirtCount",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.dirtMinHeight = jsonobject.optInt("dirtMinHeight",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.dirtMaxHeight = jsonobject.optInt("dirtMaxHeight",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.gravelSize = jsonobject.optInt("gravelSize",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.gravelCount = jsonobject.optInt("gravelCount",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.gravelMinHeight = jsonobject.optInt("gravelMinHeight",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.gravelMaxHeight = jsonobject.optInt("gravelMaxHeight",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.graniteSize = jsonobject.optInt("graniteSize",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.graniteCount = jsonobject.optInt("graniteCount",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.graniteMinHeight = jsonobject.optInt("graniteMinHeight",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.graniteMaxHeight = jsonobject.optInt("graniteMaxHeight",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.dioriteSize = jsonobject.optInt("dioriteSize",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.dioriteCount = jsonobject.optInt("dioriteCount",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.dioriteMinHeight = jsonobject.optInt("dioriteMinHeight",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.dioriteMaxHeight = jsonobject.optInt("dioriteMaxHeight",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.andesiteSize = jsonobject.optInt("andesiteSize",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.andesiteCount = jsonobject.optInt("andesiteCount",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.andesiteMinHeight = jsonobject.optInt("andesiteMinHeight",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.andesiteMaxHeight = jsonobject.optInt("andesiteMaxHeight",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.coalSize = jsonobject.optInt("coalSize",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.coalCount = jsonobject.optInt("coalCount",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.coalMinHeight = jsonobject.optInt("coalMinHeight",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.coalMaxHeight = jsonobject.optInt("coalMaxHeight",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.ironSize = jsonobject.optInt("ironSize",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.ironCount = jsonobject.optInt("ironCount",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.ironMinHeight = jsonobject.optInt("ironMinHeight",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.ironMaxHeight = jsonobject.optInt("ironMaxHeight",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.goldSize = jsonobject.optInt("goldSize",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.goldCount = jsonobject.optInt("goldCount",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.goldMinHeight = jsonobject.optInt("goldMinHeight",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.goldMaxHeight = jsonobject.optInt("goldMaxHeight",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.redstoneSize = jsonobject.optInt("redstoneSize",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.redstoneCount = jsonobject.optInt("redstoneCount",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.redstoneMinHeight = jsonobject.optInt("redstoneMinHeight",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.redstoneMaxHeight = jsonobject.optInt("redstoneMaxHeight",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.diamondSize = jsonobject.optInt("diamondSize",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.diamondCount = jsonobject.optInt("diamondCount",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.diamondMinHeight = jsonobject.optInt("diamondMinHeight",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.diamondMaxHeight = jsonobject.optInt("diamondMaxHeight",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.lapisSize = jsonobject.optInt("lapisSize",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.lapisCount = jsonobject.optInt("lapisCount",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.lapisCenterHeight = jsonobject.optInt("lapisCenterHeight",

> CHANGE  1 : 2  @  1 : 2

~ 				chunkprovidersettings$factory.lapisSpread = jsonobject.optInt("lapisSpread",

> CHANGE  8 : 88  @  8 : 89

~ 		public JSONObject serialize(ChunkProviderSettings.Factory parFactory) throws JSONException {
~ 			JSONObject jsonobject = new JSONObject();
~ 			jsonobject.put("coordinateScale", Float.valueOf(parFactory.coordinateScale));
~ 			jsonobject.put("heightScale", Float.valueOf(parFactory.heightScale));
~ 			jsonobject.put("lowerLimitScale", Float.valueOf(parFactory.lowerLimitScale));
~ 			jsonobject.put("upperLimitScale", Float.valueOf(parFactory.upperLimitScale));
~ 			jsonobject.put("depthNoiseScaleX", Float.valueOf(parFactory.depthNoiseScaleX));
~ 			jsonobject.put("depthNoiseScaleZ", Float.valueOf(parFactory.depthNoiseScaleZ));
~ 			jsonobject.put("depthNoiseScaleExponent", Float.valueOf(parFactory.depthNoiseScaleExponent));
~ 			jsonobject.put("mainNoiseScaleX", Float.valueOf(parFactory.mainNoiseScaleX));
~ 			jsonobject.put("mainNoiseScaleY", Float.valueOf(parFactory.mainNoiseScaleY));
~ 			jsonobject.put("mainNoiseScaleZ", Float.valueOf(parFactory.mainNoiseScaleZ));
~ 			jsonobject.put("baseSize", Float.valueOf(parFactory.baseSize));
~ 			jsonobject.put("stretchY", Float.valueOf(parFactory.stretchY));
~ 			jsonobject.put("biomeDepthWeight", Float.valueOf(parFactory.biomeDepthWeight));
~ 			jsonobject.put("biomeDepthOffset", Float.valueOf(parFactory.biomeDepthOffset));
~ 			jsonobject.put("biomeScaleWeight", Float.valueOf(parFactory.biomeScaleWeight));
~ 			jsonobject.put("biomeScaleOffset", Float.valueOf(parFactory.biomeScaleOffset));
~ 			jsonobject.put("seaLevel", Integer.valueOf(parFactory.seaLevel));
~ 			jsonobject.put("useCaves", Boolean.valueOf(parFactory.useCaves));
~ 			jsonobject.put("useDungeons", Boolean.valueOf(parFactory.useDungeons));
~ 			jsonobject.put("dungeonChance", Integer.valueOf(parFactory.dungeonChance));
~ 			jsonobject.put("useStrongholds", Boolean.valueOf(parFactory.useStrongholds));
~ 			jsonobject.put("useVillages", Boolean.valueOf(parFactory.useVillages));
~ 			jsonobject.put("useMineShafts", Boolean.valueOf(parFactory.useMineShafts));
~ 			jsonobject.put("useTemples", Boolean.valueOf(parFactory.useTemples));
~ 			jsonobject.put("useMonuments", Boolean.valueOf(parFactory.useMonuments));
~ 			jsonobject.put("useRavines", Boolean.valueOf(parFactory.useRavines));
~ 			jsonobject.put("useWaterLakes", Boolean.valueOf(parFactory.useWaterLakes));
~ 			jsonobject.put("waterLakeChance", Integer.valueOf(parFactory.waterLakeChance));
~ 			jsonobject.put("useLavaLakes", Boolean.valueOf(parFactory.useLavaLakes));
~ 			jsonobject.put("lavaLakeChance", Integer.valueOf(parFactory.lavaLakeChance));
~ 			jsonobject.put("useLavaOceans", Boolean.valueOf(parFactory.useLavaOceans));
~ 			jsonobject.put("fixedBiome", Integer.valueOf(parFactory.fixedBiome));
~ 			jsonobject.put("biomeSize", Integer.valueOf(parFactory.biomeSize));
~ 			jsonobject.put("riverSize", Integer.valueOf(parFactory.riverSize));
~ 			jsonobject.put("dirtSize", Integer.valueOf(parFactory.dirtSize));
~ 			jsonobject.put("dirtCount", Integer.valueOf(parFactory.dirtCount));
~ 			jsonobject.put("dirtMinHeight", Integer.valueOf(parFactory.dirtMinHeight));
~ 			jsonobject.put("dirtMaxHeight", Integer.valueOf(parFactory.dirtMaxHeight));
~ 			jsonobject.put("gravelSize", Integer.valueOf(parFactory.gravelSize));
~ 			jsonobject.put("gravelCount", Integer.valueOf(parFactory.gravelCount));
~ 			jsonobject.put("gravelMinHeight", Integer.valueOf(parFactory.gravelMinHeight));
~ 			jsonobject.put("gravelMaxHeight", Integer.valueOf(parFactory.gravelMaxHeight));
~ 			jsonobject.put("graniteSize", Integer.valueOf(parFactory.graniteSize));
~ 			jsonobject.put("graniteCount", Integer.valueOf(parFactory.graniteCount));
~ 			jsonobject.put("graniteMinHeight", Integer.valueOf(parFactory.graniteMinHeight));
~ 			jsonobject.put("graniteMaxHeight", Integer.valueOf(parFactory.graniteMaxHeight));
~ 			jsonobject.put("dioriteSize", Integer.valueOf(parFactory.dioriteSize));
~ 			jsonobject.put("dioriteCount", Integer.valueOf(parFactory.dioriteCount));
~ 			jsonobject.put("dioriteMinHeight", Integer.valueOf(parFactory.dioriteMinHeight));
~ 			jsonobject.put("dioriteMaxHeight", Integer.valueOf(parFactory.dioriteMaxHeight));
~ 			jsonobject.put("andesiteSize", Integer.valueOf(parFactory.andesiteSize));
~ 			jsonobject.put("andesiteCount", Integer.valueOf(parFactory.andesiteCount));
~ 			jsonobject.put("andesiteMinHeight", Integer.valueOf(parFactory.andesiteMinHeight));
~ 			jsonobject.put("andesiteMaxHeight", Integer.valueOf(parFactory.andesiteMaxHeight));
~ 			jsonobject.put("coalSize", Integer.valueOf(parFactory.coalSize));
~ 			jsonobject.put("coalCount", Integer.valueOf(parFactory.coalCount));
~ 			jsonobject.put("coalMinHeight", Integer.valueOf(parFactory.coalMinHeight));
~ 			jsonobject.put("coalMaxHeight", Integer.valueOf(parFactory.coalMaxHeight));
~ 			jsonobject.put("ironSize", Integer.valueOf(parFactory.ironSize));
~ 			jsonobject.put("ironCount", Integer.valueOf(parFactory.ironCount));
~ 			jsonobject.put("ironMinHeight", Integer.valueOf(parFactory.ironMinHeight));
~ 			jsonobject.put("ironMaxHeight", Integer.valueOf(parFactory.ironMaxHeight));
~ 			jsonobject.put("goldSize", Integer.valueOf(parFactory.goldSize));
~ 			jsonobject.put("goldCount", Integer.valueOf(parFactory.goldCount));
~ 			jsonobject.put("goldMinHeight", Integer.valueOf(parFactory.goldMinHeight));
~ 			jsonobject.put("goldMaxHeight", Integer.valueOf(parFactory.goldMaxHeight));
~ 			jsonobject.put("redstoneSize", Integer.valueOf(parFactory.redstoneSize));
~ 			jsonobject.put("redstoneCount", Integer.valueOf(parFactory.redstoneCount));
~ 			jsonobject.put("redstoneMinHeight", Integer.valueOf(parFactory.redstoneMinHeight));
~ 			jsonobject.put("redstoneMaxHeight", Integer.valueOf(parFactory.redstoneMaxHeight));
~ 			jsonobject.put("diamondSize", Integer.valueOf(parFactory.diamondSize));
~ 			jsonobject.put("diamondCount", Integer.valueOf(parFactory.diamondCount));
~ 			jsonobject.put("diamondMinHeight", Integer.valueOf(parFactory.diamondMinHeight));
~ 			jsonobject.put("diamondMaxHeight", Integer.valueOf(parFactory.diamondMaxHeight));
~ 			jsonobject.put("lapisSize", Integer.valueOf(parFactory.lapisSize));
~ 			jsonobject.put("lapisCount", Integer.valueOf(parFactory.lapisCount));
~ 			jsonobject.put("lapisCenterHeight", Integer.valueOf(parFactory.lapisCenterHeight));
~ 			jsonobject.put("lapisSpread", Integer.valueOf(parFactory.lapisSpread));

> EOF
