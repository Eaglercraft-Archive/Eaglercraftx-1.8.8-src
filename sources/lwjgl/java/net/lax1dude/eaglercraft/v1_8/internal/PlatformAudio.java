package net.lax1dude.eaglercraft.v1_8.internal;

import java.net.URL;

import net.lax1dude.eaglercraft.v1_8.internal.paulscode.lwjgl3.LibraryLWJGLOpenAL;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.minecraft.util.MathHelper;
import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemLogger;
import paulscode.sound.codecs.CodecJOrbis;
import paulscode.sound.codecs.CodecWav;

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
public class PlatformAudio {
	
	protected static class PaulscodeAudioResource implements IAudioResource {
		
		protected final URL resourceLoc;
		
		protected PaulscodeAudioResource(URL resourceLoc) {
			this.resourceLoc = resourceLoc;
		}
		
	}
	
	protected static class PaulscodeAudioHandle implements IAudioHandle {
		
		protected final String sourceName;
		protected long stall;
		
		protected PaulscodeAudioHandle(String sourceName) {
			this.sourceName = sourceName;
			this.stall = System.currentTimeMillis();
		}

		@Override
		public void pause(boolean setPaused) {
			if(setPaused) {
				if(sndSystem.playing(sourceName)) {
					sndSystem.pause(sourceName);
				}
			}else {
				if(!sndSystem.playing(sourceName)) {
					sndSystem.play(sourceName);
				}
			}
		}

		@Override
		public void restart() {
			this.stall = System.currentTimeMillis();
			sndSystem.rewind(sourceName);
			sndSystem.play(sourceName);
		}

		@Override
		public void move(float x, float y, float z) {
			sndSystem.setPosition(sourceName, x, y, z);
		}

		@Override
		public void pitch(float f) {
			sndSystem.setPitch(sourceName, f);
		}

		@Override
		public void gain(float f) {
			sndSystem.setVolume(sourceName, f);
		}

		@Override
		public void end() {
			sndSystem.stop(sourceName);
		}

		@Override
		public boolean shouldFree() {
			return !sndSystem.playing(sourceName) && System.currentTimeMillis() - this.stall > 250l; //TODO: I hate this hack
		}
		
	}
	
	public static IAudioResource loadAudioData(String filename, boolean holdInCache) {
		URL ret = PlatformAssets.getDesktopResourceURL(filename);
		if(ret != null) {
			return new PaulscodeAudioResource(ret);
		}else {
			return null;
		}
	}
	
	public static void clearAudioCache() {
		// browser only
	}
	
	private static final Logger logger = LogManager.getLogger("EaglercraftPlatformAudio");
	private static SoundSystem sndSystem = null;
	
	static void platformInitialize() {
		logger.info("Eaglercraft still uses Paul Lamb's SoundSystem but with LWJGL3");
		logger.info("    \"Author: Paul Lamb, www.paulscode.com\"");
		try {
			SoundSystemConfig.addLibrary(LibraryLWJGLOpenAL.class);
			SoundSystemConfig.setCodec("ogg", CodecJOrbis.class);
			SoundSystemConfig.setCodec("wav", CodecWav.class);
			SoundSystemConfig.setLogger(new SoundSystemLogger() {
				public void message(String parString1, int parInt1) {
					if (!parString1.isEmpty()) {
						logger.info(parString1);
					}
				}
				public void importantMessage(String parString1, int parInt1) {
					if (!parString1.isEmpty()) {
						logger.warn(parString1);
					}
				}
				public void errorMessage(String parString1, String parString2, int parInt1) {
					if (!parString2.isEmpty()) {
						logger.error("Error in class \"{}\"!", parString1);
						logger.error(parString2);
					}
				}
			});
			sndSystem = new SoundSystem();
		}catch(Throwable t) {
			logger.error("Could not initialize Paulscode SoundSystem! Is this system's OpenAL installed correctly?");
			logger.error(t);
			sndSystem = null;
		}
	}
	
	static void platformShutdown() {
		if(sndSystem != null) {
			sndSystem.cleanup();
			sndSystem = null;
		}
	}
	
	public static boolean available() {
		return sndSystem != null;
	}
	
	private static int sourceCounter = 0;
	
	public static IAudioHandle beginPlayback(IAudioResource track, float x, float y, float z,
			float volume, float pitch) {
		if(sndSystem == null) {
			return null;
		}
		
		float f1 = 16.0F;
		if (volume > 1.0F) {
			f1 *= volume;
		}
		
		String srcName = "src" + ++sourceCounter;
		sndSystem.newSource(false, srcName, ((PaulscodeAudioResource)track).resourceLoc,
				((PaulscodeAudioResource)track).resourceLoc.getPath(), false, x, y, z, 2, f1);
		sndSystem.setTemporary(srcName, true);
		sndSystem.setPitch(srcName, pitch);
		sndSystem.setVolume(srcName, volume);
		sndSystem.play(srcName);
		
		return new PaulscodeAudioHandle(srcName);
	}
	
	public static IAudioHandle beginPlaybackStatic(IAudioResource track, float volume, float pitch) {
		if(sndSystem == null) {
			return null;
		}
		
		String srcName = "src" + ++sourceCounter;
		sndSystem.newSource(false, srcName, ((PaulscodeAudioResource)track).resourceLoc,
				((PaulscodeAudioResource)track).resourceLoc.getPath(), false, 0.0f, 0.0f, 0.0f, 0, 0.0f);
		sndSystem.setTemporary(srcName, true);
		sndSystem.setPitch(srcName, pitch);
		sndSystem.setVolume(srcName, volume);
		sndSystem.play(srcName);
		
		return new PaulscodeAudioHandle(srcName);
	}
	
	public static void setListener(float x, float y, float z, float pitchDegrees, float yawDegrees) {
		if(sndSystem == null) {
			return;
		}
		float f2 = MathHelper.cos((yawDegrees + 90.0F) * 0.017453292F);
		float f3 = MathHelper.sin((yawDegrees + 90.0F) * 0.017453292F);
		float f4 = MathHelper.cos(-pitchDegrees * 0.017453292F);
		float f5 = MathHelper.sin(-pitchDegrees * 0.017453292F);
		float f6 = MathHelper.cos((-pitchDegrees + 90.0F) * 0.017453292F);
		float f7 = MathHelper.sin((-pitchDegrees + 90.0F) * 0.017453292F);
		float f8 = f2 * f4;
		float f9 = f3 * f4;
		float f10 = f2 * f6;
		float f11 = f3 * f6;
		sndSystem.setListenerPosition(x, y, z);
		sndSystem.setListenerOrientation(f8, f5, f9, f10, f7, f11);
	}

	public static void setMicVol(float vol) {
		// nope
	}

}
