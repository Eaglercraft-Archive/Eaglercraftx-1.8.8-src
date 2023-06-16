
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 5

> DELETE  3  @  3 : 4

> INSERT  2 : 3  @  2

+ import java.nio.charset.StandardCharsets;

> DELETE  2  @  2 : 3

> CHANGE  1 : 14  @  1 : 11

~ import java.util.Set;
~ 
~ import net.lax1dude.eaglercraft.v1_8.internal.PlatformAudio;
~ 
~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ import net.lax1dude.eaglercraft.v1_8.EaglercraftSoundManager;
~ 
~ import com.google.common.collect.Lists;
~ 
~ import net.lax1dude.eaglercraft.v1_8.IOUtils;
~ import net.lax1dude.eaglercraft.v1_8.json.JSONTypeProvider;
~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

> DELETE  7  @  7 : 11

> CHANGE  3 : 4  @  3 : 5

~ 

> CHANGE  16 : 17  @  16 : 17

~ 	private final EaglercraftSoundManager sndManager;

> CHANGE  4 : 5  @  4 : 5

~ 		this.sndManager = new EaglercraftSoundManager(gameSettingsIn, this);

> CHANGE  12 : 13  @  12 : 13

~ 						for (Entry entry : (Set<Entry>) map.entrySet()) {

> INSERT  14 : 24  @  14

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

> INSERT  122 : 126  @  122

+ 		if (category == SoundCategory.VOICE) {
+ 			PlatformAudio.setMicVol(volume);
+ 		}
+ 

> CHANGE  13 : 19  @  13 : 15

~ 			SoundCategory cat = soundeventaccessorcomposite.getSoundCategory();
~ 			for (int i = 0; i < categories.length; ++i) {
~ 				if (cat == categories[i]) {
~ 					arraylist.add(soundeventaccessorcomposite);
~ 					break;
~ 				}

> CHANGE  6 : 7  @  6 : 7

~ 			return (SoundEventAccessorComposite) arraylist.get((new EaglercraftRandom()).nextInt(arraylist.size()));

> EOF
