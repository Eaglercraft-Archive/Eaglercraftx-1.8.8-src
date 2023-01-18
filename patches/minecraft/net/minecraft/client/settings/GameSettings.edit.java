
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 7

> CHANGE  1 : 4  @  1 : 4

~ import java.io.ByteArrayOutputStream;
~ import java.io.InputStreamReader;
~ import java.io.OutputStreamWriter;

> DELETE  1  @  1 : 3

> INSERT  3 : 21  @  3

+ 
+ import org.json.JSONArray;
+ 
+ import com.google.common.collect.ImmutableSet;
+ import com.google.common.collect.Lists;
+ import com.google.common.collect.Maps;
+ import com.google.common.collect.Sets;
+ 
+ import net.lax1dude.eaglercraft.v1_8.ArrayUtils;
+ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
+ import net.lax1dude.eaglercraft.v1_8.EaglerInputStream;
+ import net.lax1dude.eaglercraft.v1_8.EaglerZLIB;
+ import net.lax1dude.eaglercraft.v1_8.HString;
+ import net.lax1dude.eaglercraft.v1_8.Keyboard;
+ import net.lax1dude.eaglercraft.v1_8.Mouse;
+ import net.lax1dude.eaglercraft.v1_8.internal.KeyboardConstants;
+ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
+ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

> DELETE  5  @  5 : 7

> DELETE  5  @  5 : 11

> DELETE  3  @  3 : 8

> DELETE  1  @  1 : 9

> CHANGE  23 : 26  @  23 : 26

~ 	public int clouds = 1;
~ 	public boolean fancyGraphics = false;
~ 	public int ambientOcclusion = 0;

> DELETE  8  @  8 : 9

> DELETE  1  @  1 : 2

> CHANGE  36 : 37  @  36 : 37

~ 	public KeyBinding keyBindSprint = new KeyBinding("key.sprint", KeyboardConstants.KEY_R, "key.categories.movement");

> CHANGE  10 : 16  @  10 : 17

~ 	public KeyBinding keyBindSmoothCamera = new KeyBinding("key.smoothCamera", KeyboardConstants.KEY_M,
~ 			"key.categories.misc");
~ 	public KeyBinding keyBindZoomCamera = new KeyBinding("key.zoomCamera", KeyboardConstants.KEY_C,
~ 			"key.categories.misc");
~ 	public KeyBinding keyBindFunction = new KeyBinding("key.function", KeyboardConstants.KEY_F, "key.categories.misc");
~ 	public KeyBinding keyBindClose = new KeyBinding("key.close", KeyboardConstants.KEY_GRAVE, "key.categories.misc");

> DELETE  12  @  12 : 13

> CHANGE  12 : 13  @  12 : 13

~ 	public int guiScale = 3;

> INSERT  3 : 12  @  3

+ 	public boolean hudFps = true;
+ 	public boolean hudCoords = true;
+ 	public boolean hudPlayer = false;
+ 	public boolean hudWorld = false;
+ 	public boolean hudStats = false;
+ 	public boolean hud24h = false;
+ 	public boolean chunkFix = true;
+ 	public boolean fog = true;
+ 	public int fxaa = 0;

> CHANGE  1 : 2  @  1 : 2

~ 	public GameSettings(Minecraft mcIn) {

> CHANGE  4 : 6  @  4 : 7

~ 				this.keyBindTogglePerspective, this.keyBindSmoothCamera, this.keyBindZoomCamera, this.keyBindFunction,
~ 				this.keyBindClose }, this.keyBindsHotbar);

> CHANGE  3 : 5  @  3 : 4

~ 		this.gammaSetting = 1.0F;
~ 		this.language = EagRuntime.getConfiguration().getDefaultLocale();

> CHANGE  2 : 3  @  2 : 8

~ 		GameSettings.Options.RENDER_DISTANCE.setValueMax(18.0F);

> CHANGE  1 : 2  @  1 : 2

~ 		this.renderDistanceChunks = 4;

> DELETE  3  @  3 : 18

> CHANGE  3 : 4  @  3 : 4

~ 						: HString.format("%c", new Object[] { Character.valueOf((char) (parInt1 - 256)) })

> DELETE  83  @  83 : 84

> DELETE  4  @  4 : 5

> INSERT  45 : 47  @  45

+ 			this.mc.loadingScreen.eaglerShow(I18n.format("resourcePack.load.refreshing"),
+ 					I18n.format("resourcePack.load.pleaseWait"));

> DELETE  58  @  58 : 75

> INSERT  13 : 53  @  13

+ 		if (parOptions == GameSettings.Options.HUD_FPS) {
+ 			this.hudFps = !this.hudFps;
+ 		}
+ 
+ 		if (parOptions == GameSettings.Options.HUD_COORDS) {
+ 			this.hudCoords = !this.hudCoords;
+ 		}
+ 
+ 		if (parOptions == GameSettings.Options.HUD_PLAYER) {
+ 			this.hudPlayer = !this.hudPlayer;
+ 		}
+ 
+ 		if (parOptions == GameSettings.Options.HUD_STATS) {
+ 			this.hudStats = !this.hudStats;
+ 		}
+ 
+ 		if (parOptions == GameSettings.Options.HUD_WORLD) {
+ 			this.hudWorld = !this.hudWorld;
+ 		}
+ 
+ 		if (parOptions == GameSettings.Options.HUD_24H) {
+ 			this.hud24h = !this.hud24h;
+ 		}
+ 
+ 		if (parOptions == GameSettings.Options.CHUNK_FIX) {
+ 			this.chunkFix = !this.chunkFix;
+ 		}
+ 
+ 		if (parOptions == GameSettings.Options.FOG) {
+ 			this.fog = !this.fog;
+ 		}
+ 
+ 		if (parOptions == GameSettings.Options.FXAA) {
+ 			this.fxaa = (this.fxaa + parInt1) % 3;
+ 		}
+ 
+ 		if (parOptions == GameSettings.Options.FULLSCREEN) {
+ 			this.mc.toggleFullscreen();
+ 		}
+ 

> DELETE  54  @  54 : 60

> INSERT  12 : 30  @  12

+ 		case HUD_COORDS:
+ 			return this.hudCoords;
+ 		case HUD_FPS:
+ 			return this.hudFps;
+ 		case HUD_PLAYER:
+ 			return this.hudPlayer;
+ 		case HUD_STATS:
+ 			return this.hudStats;
+ 		case HUD_WORLD:
+ 			return this.hudWorld;
+ 		case HUD_24H:
+ 			return this.hud24h;
+ 		case CHUNK_FIX:
+ 			return this.chunkFix;
+ 		case FOG:
+ 			return this.fog;
+ 		case FULLSCREEN:
+ 			return this.mc.isFullScreen();

> CHANGE  43 : 46  @  43 : 47

~ 																	: (parOptions == GameSettings.Options.CHAT_SCALE
~ 																			? s + (int) (f * 90.0F + 10.0F) + "%"
~ 																			: (parOptions == GameSettings.Options.CHAT_HEIGHT_UNFOCUSED

> CHANGE  3 : 4  @  3 : 4

~ 																					: (parOptions == GameSettings.Options.CHAT_HEIGHT_FOCUSED

> CHANGE  1 : 2  @  1 : 2

~ 																									.calculateChatboxHeight(

> CHANGE  2 : 21  @  2 : 36

~ 																							: (parOptions == GameSettings.Options.CHAT_WIDTH
~ 																									? s + GuiNewChat
~ 																											.calculateChatboxWidth(
~ 																													f)
~ 																											+ "px"
~ 																									: (parOptions == GameSettings.Options.RENDER_DISTANCE
~ 																											? s + (int) f1
~ 																													+ (f1 == 1.0F
~ 																															? " chunk"
~ 																															: " chunks")
~ 																											: (parOptions == GameSettings.Options.MIPMAP_LEVELS
~ 																													? (f == 0.0F
~ 																															? s + I18n
~ 																																	.format("options.off",
~ 																																			new Object[0])
~ 																															: s + (int) (f
~ 																																	* 100.0F)
~ 																																	+ "%")
~ 																													: "yee"))))))))))));

> INSERT  28 : 36  @  28

+ 		} else if (parOptions == GameSettings.Options.FXAA) {
+ 			if (this.fxaa == 0) {
+ 				return s + I18n.format("options.fxaa.auto");
+ 			} else if (this.fxaa == 1) {
+ 				return s + I18n.format("options.on");
+ 			} else {
+ 				return s + I18n.format("options.off");
+ 			}

> CHANGE  7 : 9  @  7 : 8

~ 			byte[] options = EagRuntime.getStorage("g");
~ 			if (options == null) {

> CHANGE  3 : 5  @  3 : 4

~ 			BufferedReader bufferedreader = new BufferedReader(
~ 					new InputStreamReader(EaglerZLIB.newGZIPInputStream(new EaglerInputStream(options))));

> CHANGE  83 : 89  @  83 : 84

~ 						this.resourcePacks.clear();
~ 						for (Object o : (new JSONArray(s.substring(s.indexOf(58) + 1))).toList()) {
~ 							if (o instanceof String) {
~ 								this.resourcePacks.add((String) o);
~ 							}
~ 						}

> CHANGE  6 : 12  @  6 : 7

~ 						this.field_183018_l.clear();
~ 						for (Object o : (new JSONArray(s.substring(s.indexOf(58) + 1))).toList()) {
~ 							if (o instanceof String) {
~ 								this.field_183018_l.add((String) o);
~ 							}
~ 						}

> DELETE  38  @  38 : 42

> DELETE  4  @  4 : 8

> INSERT  116 : 153  @  116

+ 					if (astring[0].equals("hudFps")) {
+ 						this.hudFps = astring[1].equals("true");
+ 					}
+ 
+ 					if (astring[0].equals("hudWorld")) {
+ 						this.hudWorld = astring[1].equals("true");
+ 					}
+ 
+ 					if (astring[0].equals("hudCoords")) {
+ 						this.hudCoords = astring[1].equals("true");
+ 					}
+ 
+ 					if (astring[0].equals("hudPlayer")) {
+ 						this.hudPlayer = astring[1].equals("true");
+ 					}
+ 
+ 					if (astring[0].equals("hudStats")) {
+ 						this.hudStats = astring[1].equals("true");
+ 					}
+ 
+ 					if (astring[0].equals("hud24h")) {
+ 						this.hud24h = astring[1].equals("true");
+ 					}
+ 
+ 					if (astring[0].equals("chunkFix")) {
+ 						this.chunkFix = astring[1].equals("true");
+ 					}
+ 
+ 					if (astring[0].equals("fog")) {
+ 						this.fog = astring[1].equals("true");
+ 					}
+ 
+ 					if (astring[0].equals("fxaa")) {
+ 						this.fxaa = (astring[1].equals("true") || astring[1].equals("false")) ? 0
+ 								: Integer.parseInt(astring[1]);
+ 					}
+ 

> INSERT  6 : 8  @  6

+ 					Keyboard.setFunctionKeyModifier(keyBindFunction.getKeyCode());
+ 

> DELETE  17  @  17 : 18

> CHANGE  12 : 14  @  12 : 13

~ 			ByteArrayOutputStream bao = new ByteArrayOutputStream();
~ 			PrintWriter printwriter = new PrintWriter(new OutputStreamWriter(EaglerZLIB.newGZIPOutputStream(bao)));

> CHANGE  26 : 28  @  26 : 28

~ 			printwriter.println("resourcePacks:" + toJSONArray(this.resourcePacks));
~ 			printwriter.println("incompatibleResourcePacks:" + toJSONArray(this.field_183018_l));

> DELETE  8  @  8 : 9

> DELETE  1  @  1 : 2

> INSERT  29 : 38  @  29

+ 			printwriter.println("hudFps:" + this.hudFps);
+ 			printwriter.println("hudWorld:" + this.hudWorld);
+ 			printwriter.println("hudCoords:" + this.hudCoords);
+ 			printwriter.println("hudPlayer:" + this.hudPlayer);
+ 			printwriter.println("hudStats:" + this.hudStats);
+ 			printwriter.println("hud24h:" + this.hud24h);
+ 			printwriter.println("chunkFix:" + this.chunkFix);
+ 			printwriter.println("fog:" + this.fog);
+ 			printwriter.println("fxaa:" + this.fxaa);

> INSERT  5 : 7  @  5

+ 			Keyboard.setFunctionKeyModifier(keyBindFunction.getKeyCode());
+ 

> INSERT  11 : 13  @  11

+ 
+ 			EagRuntime.setStorage("g", bao.toByteArray());

> CHANGE  10 : 11  @  10 : 11

~ 				: (parSoundCategory == SoundCategory.VOICE ? 0.0F : 1.0F);

> INSERT  53 : 61  @  53

+ 	private String toJSONArray(List<String> e) {
+ 		JSONArray arr = new JSONArray();
+ 		for (String s : e) {
+ 			arr.put(s);
+ 		}
+ 		return arr.toString();
+ 	}
+ 

> CHANGE  4 : 5  @  4 : 5

~ 		RENDER_DISTANCE("options.renderDistance", true, false, 1.0F, 16.0F, 1.0F),

> CHANGE  8 : 10  @  8 : 12

~ 		TOUCHSCREEN("options.touchscreen", false, true), CHAT_SCALE("options.chat.scale", true, false),
~ 		CHAT_WIDTH("options.chat.width", true, false), CHAT_HEIGHT_FOCUSED("options.chat.height.focused", true, false),

> CHANGE  14 : 20  @  14 : 15

~ 		ENTITY_SHADOWS("options.entityShadows", false, true), HUD_FPS("options.hud.fps", false, true),
~ 		HUD_COORDS("options.hud.coords", false, true), HUD_STATS("options.hud.stats", false, true),
~ 		HUD_WORLD("options.hud.world", false, true), HUD_PLAYER("options.hud.player", false, true),
~ 		HUD_24H("options.hud.24h", false, true), CHUNK_FIX("options.chunkFix", false, true),
~ 		FOG("options.fog", false, true), FXAA("options.fxaa", false, false),
~ 		FULLSCREEN("options.fullscreen", false, true);

> EOF
