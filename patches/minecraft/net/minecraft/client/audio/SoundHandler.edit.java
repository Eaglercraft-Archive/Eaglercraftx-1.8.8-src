
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 5

> DELETE  3  @  6 : 7

> INSERT  2 : 3  @  3

+ import java.nio.charset.StandardCharsets;

> DELETE  3  @  2 : 3

> CHANGE  1 : 14  @  2 : 12

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

> DELETE  20  @  17 : 21

> CHANGE  3 : 4  @  7 : 9

~ 

> CHANGE  17 : 18  @  18 : 19

~ 	private final EaglercraftSoundManager sndManager;

> CHANGE  5 : 6  @  5 : 6

~ 		this.sndManager = new EaglercraftSoundManager(gameSettingsIn, this);

> CHANGE  13 : 14  @  13 : 14

~ 						for (Entry entry : (Set<Entry>) map.entrySet()) {

> INSERT  15 : 25  @  15

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

> CHANGE  11 : 12  @  1 : 2

~ 		Map<String, SoundList> map = null;

> CHANGE  2 : 6  @  2 : 3

~ 			map = JSONTypeProvider.deserialize(IOUtils.inputStreamToString(stream, StandardCharsets.UTF_8),
~ 					SoundMap.class).soundMap;
~ 		} catch (IOException e) {
~ 			throw new RuntimeException("Exception caught reading JSON", e);

> INSERT  126 : 130  @  123

+ 		if (category == SoundCategory.VOICE) {
+ 			PlatformAudio.setMicVol(volume);
+ 		}
+ 

> CHANGE  17 : 23  @  13 : 15

~ 			SoundCategory cat = soundeventaccessorcomposite.getSoundCategory();
~ 			for (int i = 0; i < categories.length; ++i) {
~ 				if (cat == categories[i]) {
~ 					arraylist.add(soundeventaccessorcomposite);
~ 					break;
~ 				}

> CHANGE  12 : 13  @  8 : 9

~ 			return (SoundEventAccessorComposite) arraylist.get((new EaglercraftRandom()).nextInt(arraylist.size()));

> EOF
