
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 5

> DELETE  5  @  8 : 9

> INSERT  7 : 8  @  11

+ import java.nio.charset.StandardCharsets;

> DELETE  10  @  13 : 14

> CHANGE  11 : 24  @  15 : 25

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

> DELETE  31  @  32 : 36

> CHANGE  34 : 35  @  39 : 41

~ 

> CHANGE  51 : 52  @  57 : 58

~ 	private final EaglercraftSoundManager sndManager;

> CHANGE  56 : 57  @  62 : 63

~ 		this.sndManager = new EaglercraftSoundManager(gameSettingsIn, this);

> CHANGE  69 : 70  @  75 : 76

~ 						for (Entry entry : (Set<Entry>) map.entrySet()) {

> INSERT  84 : 94  @  90

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

> CHANGE  95 : 96  @  91 : 92

~ 		Map<String, SoundList> map = null;

> CHANGE  97 : 101  @  93 : 94

~ 			map = JSONTypeProvider.deserialize(IOUtils.inputStreamToString(stream, StandardCharsets.UTF_8),
~ 					SoundMap.class).soundMap;
~ 		} catch (IOException e) {
~ 			throw new RuntimeException("Exception caught reading JSON", e);

> INSERT  223 : 227  @  216

+ 		if (category == SoundCategory.VOICE) {
+ 			PlatformAudio.setMicVol(volume);
+ 		}
+ 

> CHANGE  240 : 246  @  229 : 231

~ 			SoundCategory cat = soundeventaccessorcomposite.getSoundCategory();
~ 			for (int i = 0; i < categories.length; ++i) {
~ 				if (cat == categories[i]) {
~ 					arraylist.add(soundeventaccessorcomposite);
~ 					break;
~ 				}

> CHANGE  252 : 253  @  237 : 238

~ 			return (SoundEventAccessorComposite) arraylist.get((new EaglercraftRandom()).nextInt(arraylist.size()));

> EOF
