package net.lax1dude.eaglercraft.v1_8;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.lax1dude.eaglercraft.v1_8.internal.EnumPlatformType;
import net.lax1dude.eaglercraft.v1_8.internal.IAudioHandle;
import net.lax1dude.eaglercraft.v1_8.internal.IAudioResource;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformAudio;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.ISound.AttenuationType;
import net.minecraft.client.audio.ITickableSound;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.audio.SoundEventAccessorComposite;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundPoolEntry;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ITickable;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

/**
 * Copyright (c) 2022-2023 LAX1DUDE. All Rights Reserved.
 * 
 * WITH THE EXCEPTION OF PATCH FILES, MINIFIED JAVASCRIPT, AND ALL FILES
 * NORMALLY FOUND IN AN UNMODIFIED MINECRAFT RESOURCE PACK, YOU ARE NOT ALLOWED
 * TO SHARE, DISTRIBUTE, OR REPURPOSE ANY FILE USED BY OR PRODUCED BY THE
 * SOFTWARE IN THIS REPOSITORY WITHOUT PRIOR PERMISSION FROM THE PROJECT AUTHOR.
 * 
 * NOT FOR COMMERCIAL OR MALICIOUS USE
 * 
 * (please read the 'LICENSE' file this repo's root directory for more info)
 * 
 */
public class EaglercraftSoundManager {
	
	protected static class ActiveSoundEvent {

		protected final EaglercraftSoundManager manager;
		
		protected final ISound soundInstance;
		protected final SoundCategory soundCategory;
		protected final SoundPoolEntry soundConfig;
		protected IAudioHandle soundHandle;
		
		protected float activeX;
		protected float activeY;
		protected float activeZ;
		
		protected float activePitch;
		protected float activeGain;
		
		protected int repeatCounter = 0;
		protected boolean paused = false;
		
		protected ActiveSoundEvent(EaglercraftSoundManager manager, ISound soundInstance,
				SoundCategory soundCategory, SoundPoolEntry soundConfig, IAudioHandle soundHandle) {
			this.manager = manager;
			this.soundInstance = soundInstance;
			this.soundCategory = soundCategory;
			this.soundConfig = soundConfig;
			this.soundHandle = soundHandle;
			this.activeX = soundInstance.getXPosF();
			this.activeY = soundInstance.getYPosF();
			this.activeZ = soundInstance.getZPosF();
			this.activePitch = soundInstance.getPitch();
			this.activeGain = soundInstance.getVolume();
		}
		
		protected void updateLocation() {
			float x = soundInstance.getXPosF();
			float y = soundInstance.getYPosF();
			float z = soundInstance.getZPosF();
			float pitch = soundInstance.getPitch();
			float gain = soundInstance.getVolume();
			if(x != activeX || y != activeY || z != activeZ) {
				soundHandle.move(x, y, z);
				activeX = x;
				activeY = y;
				activeZ = z;
			}
			if(pitch != activePitch) {
				soundHandle.pitch(MathHelper.clamp_float(pitch * (float)soundConfig.getPitch(), 0.5f, 2.0f));
				activePitch = pitch;
			}
			if(gain != activeGain) {
				float attenuatedGain = gain * manager.categoryVolumes[SoundCategory.MASTER.getCategoryId()] *
						(soundCategory == SoundCategory.MASTER ? 1.0f : manager.categoryVolumes[soundCategory.getCategoryId()])
						* (float)soundConfig.getVolume();
				soundHandle.gain(MathHelper.clamp_float(attenuatedGain, 0.0f, 1.0f));
				activeGain = gain;
			}
		}
		
	}
	
	protected static class WaitingSoundEvent {
		
		protected final ISound playSound;
		protected int playTicks;
		protected boolean paused = false;
		
		private WaitingSoundEvent(ISound playSound, int playTicks) {
			this.playSound = playSound;
			this.playTicks = playTicks;
		}
		
	}
	
	private static final Logger logger = LogManager.getLogger("SoundManager");
	
	private final GameSettings settings;
	private final SoundHandler handler;
	private final float[] categoryVolumes;
	private final List<ActiveSoundEvent> activeSounds;
	private final List<WaitingSoundEvent> queuedSounds;

	public EaglercraftSoundManager(GameSettings settings, SoundHandler handler) {
		this.settings = settings;
		this.handler = handler;
		categoryVolumes = new float[] {
				settings.getSoundLevel(SoundCategory.MASTER), settings.getSoundLevel(SoundCategory.MUSIC),
				settings.getSoundLevel(SoundCategory.RECORDS), settings.getSoundLevel(SoundCategory.WEATHER),
				settings.getSoundLevel(SoundCategory.BLOCKS), settings.getSoundLevel(SoundCategory.MOBS),
				settings.getSoundLevel(SoundCategory.ANIMALS), settings.getSoundLevel(SoundCategory.PLAYERS),
				settings.getSoundLevel(SoundCategory.AMBIENT), settings.getSoundLevel(SoundCategory.VOICE)
		};
		activeSounds = new LinkedList();
		queuedSounds = new LinkedList();
	}

	public void unloadSoundSystem() {
		// handled by PlatformApplication
	}
	
	public void reloadSoundSystem() {
		PlatformAudio.flushAudioCache();
	}
	
	public void setSoundCategoryVolume(SoundCategory category, float volume) {
		categoryVolumes[category.getCategoryId()] = volume;
		Iterator<ActiveSoundEvent> soundItr = activeSounds.iterator();
		while(soundItr.hasNext()) {
			ActiveSoundEvent evt = soundItr.next();
			if((category == SoundCategory.MASTER || evt.soundCategory == category)
					&& !evt.soundHandle.shouldFree()) {
				float newVolume = (evt.activeGain = evt.soundInstance.getVolume()) * categoryVolumes[SoundCategory.MASTER.getCategoryId()] *
					(evt.soundCategory == SoundCategory.MASTER ? 1.0f : categoryVolumes[evt.soundCategory.getCategoryId()])
					* (float)evt.soundConfig.getVolume();
				newVolume = MathHelper.clamp_float(newVolume, 0.0f, 1.0f);
				if(newVolume > 0.0f) {
					evt.soundHandle.gain(newVolume);
				}else {
					evt.soundHandle.end();
					soundItr.remove();
				}
			}
		}
	}
	
	public void stopAllSounds() {
		Iterator<ActiveSoundEvent> soundItr = activeSounds.iterator();
		while(soundItr.hasNext()) {
			ActiveSoundEvent evt = soundItr.next();
			if(!evt.soundHandle.shouldFree()) {
				evt.soundHandle.end();
			}
		}
		activeSounds.clear();
	}
	
	public void pauseAllSounds() {
		Iterator<ActiveSoundEvent> soundItr = activeSounds.iterator();
		while(soundItr.hasNext()) {
			ActiveSoundEvent evt = soundItr.next();
			if(!evt.soundHandle.shouldFree()) {
				evt.soundHandle.pause(true);
				evt.paused = true;
			}
		}
		Iterator<WaitingSoundEvent> soundItr2 = queuedSounds.iterator();
		while(soundItr2.hasNext()) {
			soundItr2.next().paused = true;
		}
	}
	
	public void resumeAllSounds() {
		Iterator<ActiveSoundEvent> soundItr = activeSounds.iterator();
		while(soundItr.hasNext()) {
			ActiveSoundEvent evt = soundItr.next();
			if(!evt.soundHandle.shouldFree()) {
				evt.soundHandle.pause(false);
				evt.paused = false;
			}
		}
		Iterator<WaitingSoundEvent> soundItr2 = queuedSounds.iterator();
		while(soundItr2.hasNext()) {
			soundItr2.next().paused = false;
		}
	}
	
	public void updateAllSounds() {
		Iterator<ActiveSoundEvent> soundItr = activeSounds.iterator();
		while(soundItr.hasNext()) {
			ActiveSoundEvent evt = soundItr.next();
			if(!evt.paused && (evt.soundInstance instanceof ITickable)) {
				boolean destroy = false;
				try {
					((ITickable)evt.soundInstance).update();
					if ((evt.soundInstance instanceof ITickableSound)
							&& ((ITickableSound) evt.soundInstance).isDonePlaying()) {
						destroy = true;
					}
				}catch(Throwable t) {
					logger.error("Error ticking sound: {}", t.toString());
					logger.error(t);
					destroy = true;
				}
				if(destroy) {
					if(!evt.soundHandle.shouldFree()) {
						evt.soundHandle.end();
					}
					soundItr.remove();
				}
			}
			if(evt.soundHandle.shouldFree()) {
				if(evt.soundInstance.canRepeat()) {
					if(!evt.paused && ++evt.repeatCounter > evt.soundInstance.getRepeatDelay()) {
						evt.repeatCounter = 0;
						evt.updateLocation();
						evt.soundHandle.restart();
					}
				}else {
					soundItr.remove();
				}
			}else {
				evt.updateLocation();
			}
		}
		Iterator<WaitingSoundEvent> soundItr2 = queuedSounds.iterator();
		while(soundItr2.hasNext()) {
			WaitingSoundEvent evt = soundItr2.next();
			if(!evt.paused && --evt.playTicks <= 0) {
				soundItr2.remove();
				playSound(evt.playSound);
			}
		}
		PlatformAudio.clearAudioCache();
	}
	
	public boolean isSoundPlaying(ISound sound) {
		Iterator<ActiveSoundEvent> soundItr = activeSounds.iterator();
		while(soundItr.hasNext()) {
			ActiveSoundEvent evt = soundItr.next();
			if(evt.soundInstance == sound) {
				return !evt.soundHandle.shouldFree();
			}
		}
		return false;
	}
	
	public void stopSound(ISound sound) {
		Iterator<ActiveSoundEvent> soundItr = activeSounds.iterator();
		while(soundItr.hasNext()) {
			ActiveSoundEvent evt = soundItr.next();
			if(evt.soundInstance == sound) {
				if(!evt.soundHandle.shouldFree()) {
					evt.soundHandle.end();
					soundItr.remove();
					return;
				}
			}
		}
		Iterator<WaitingSoundEvent> soundItr2 = queuedSounds.iterator();
		while(soundItr2.hasNext()) {
			if(soundItr2.next().playSound == sound) {
				soundItr2.remove();
			}
		}
	}

	private final PlatformAudio.IAudioCacheLoader browserResourcePackLoader = filename -> {
		try {
			return EaglerInputStream.inputStreamToBytesQuiet(Minecraft.getMinecraft().getResourceManager()
					.getResource(new ResourceLocation(filename)).getInputStream());
		}catch(Throwable t) {
			return null;
		}
	};

	public void playSound(ISound sound) {
		if(!PlatformAudio.available()) {
			return;
		}
		if(sound != null && categoryVolumes[SoundCategory.MASTER.getCategoryId()] > 0.0f) {
			SoundEventAccessorComposite accessor = handler.getSound(sound.getSoundLocation());
			if(accessor == null) {
				logger.warn("Unable to play unknown soundEvent(1): {}", sound.getSoundLocation().toString());
			}else {
				SoundPoolEntry etr = accessor.cloneEntry();
				if (etr == SoundHandler.missing_sound) {
					logger.warn("Unable to play empty soundEvent(2): {}", etr.getSoundPoolEntryLocation().toString());
				}else {
					ResourceLocation lc = etr.getSoundPoolEntryLocation();
					IAudioResource trk;
					if(EagRuntime.getPlatformType() != EnumPlatformType.DESKTOP) {
						trk = PlatformAudio.loadAudioDataNew(lc.toString(), !etr.isStreamingSound(), browserResourcePackLoader);
					}else {
						trk = PlatformAudio.loadAudioData(
								"/assets/" + lc.getResourceDomain() + "/" + lc.getResourcePath(), !etr.isStreamingSound());
					}
					if(trk == null) {
						logger.warn("Unable to play unknown soundEvent(3): {}", sound.getSoundLocation().toString());
					}else {
						
						ActiveSoundEvent newSound = new ActiveSoundEvent(this, sound, accessor.getSoundCategory(), etr, null);

						float pitch = MathHelper.clamp_float(newSound.activePitch * (float)etr.getPitch(), 0.5f, 2.0f);
						float attenuatedGain = newSound.activeGain * categoryVolumes[SoundCategory.MASTER.getCategoryId()] *
								(accessor.getSoundCategory() == SoundCategory.MASTER ? 1.0f :
								categoryVolumes[accessor.getSoundCategory().getCategoryId()]) * (float)etr.getVolume();
						
						AttenuationType tp = sound.getAttenuationType();
						if(tp == AttenuationType.LINEAR) {
							newSound.soundHandle = PlatformAudio.beginPlayback(trk, newSound.activeX, newSound.activeY,
									newSound.activeZ, attenuatedGain, pitch);
						}else {
							newSound.soundHandle = PlatformAudio.beginPlaybackStatic(trk, attenuatedGain, pitch);
						}
						
						if(newSound.soundHandle == null) {
							logger.error("Unable to play soundEvent(4): {}", sound.getSoundLocation().toString());
						}else {
							activeSounds.add(newSound);
						}
					}
				}
			}
		}
	}
	
	public void playDelayedSound(ISound sound, int delay) {
		queuedSounds.add(new WaitingSoundEvent(sound, delay));
	}
	
	public void setListener(EntityPlayer player, float partialTicks) {
		if(!PlatformAudio.available()) {
			return;
		}
		if(player != null) {
			try {
				float f = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * partialTicks;
				float f1 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * partialTicks;
				double d0 = player.prevPosX + (player.posX - player.prevPosX) * (double) partialTicks;
				double d1 = player.prevPosY + (player.posY - player.prevPosY) * (double) partialTicks + (double) player.getEyeHeight();
				double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * (double) partialTicks;
				PlatformAudio.setListener((float)d0, (float)d1, (float)d2, f, f1);
			}catch(Throwable t) {
				// eaglercraft 1.5.2 had Infinity/NaN crashes for this function which
				// couldn't be resolved via if statement checks in the above variables
			}
		}
	}
	
}
