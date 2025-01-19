
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 5

> CHANGE  3 : 4  @  3 : 6

~ import java.nio.charset.StandardCharsets;

> DELETE  2  @  2 : 3

> CHANGE  1 : 11  @  1 : 11

~ import java.util.Set;
~ 
~ import com.google.common.collect.Lists;
~ 
~ import net.lax1dude.eaglercraft.v1_8.EaglercraftSoundManager;
~ import net.lax1dude.eaglercraft.v1_8.IOUtils;
~ import net.lax1dude.eaglercraft.v1_8.ThreadLocalRandom;
~ import net.lax1dude.eaglercraft.v1_8.json.JSONTypeProvider;
~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

> DELETE  7  @  7 : 11

> CHANGE  3 : 4  @  3 : 18

~ 	private static final Logger tipLogger = LogManager.getLogger("EaglercraftX");

> CHANGE  3 : 4  @  3 : 4

~ 	private final EaglercraftSoundManager sndManager;

> CHANGE  4 : 5  @  4 : 5

~ 		this.sndManager = new EaglercraftSoundManager(gameSettingsIn, this);

> CHANGE  12 : 13  @  12 : 13

~ 						for (Entry entry : (Set<Entry>) map.entrySet()) {

> INSERT  12 : 16  @  12

+ 		if (this.sndRegistry.getObject(new ResourceLocation("minecraft:sounds/music/game/calm1.ogg")) == null) {
+ 			tipLogger.info(
+ 					"Download this resource pack if you want music: https://bafybeiayojww5jfyzvlmtuk7l5ufkt7nlfto7mhwmzf2vs4bvsjd5ouiuq.ipfs.nftstorage.link/?filename=Music_For_Eaglercraft.zip");
+ 		}

> INSERT  2 : 12  @  2

+ 	public static class SoundMap {
+ 
+ 		protected final Map<String, SoundList> soundMap;
+ 
+ 		public SoundMap(Map<String, SoundList> soundMap) {
+ 			this.soundMap = soundMap;
+ 		}
+ 
+ 	}
+ 

> CHANGE  1 : 2  @  1 : 2

~ 		Map<String, SoundList> map = null;

> CHANGE  1 : 5  @  1 : 2

~ 			map = JSONTypeProvider.deserialize(IOUtils.inputStreamToString(stream, StandardCharsets.UTF_8),
~ 					SoundMap.class).soundMap;
~ 		} catch (IOException e) {
~ 			throw new RuntimeException("Exception caught reading JSON", e);

> CHANGE  135 : 141  @  135 : 137

~ 			SoundCategory cat = soundeventaccessorcomposite.getSoundCategory();
~ 			for (int i = 0; i < categories.length; ++i) {
~ 				if (cat == categories[i]) {
~ 					arraylist.add(soundeventaccessorcomposite);
~ 					break;
~ 				}

> CHANGE  6 : 7  @  6 : 7

~ 			return (SoundEventAccessorComposite) arraylist.get(ThreadLocalRandom.current().nextInt(arraylist.size()));

> EOF
