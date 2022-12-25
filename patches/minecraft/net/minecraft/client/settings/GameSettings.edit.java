
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 7

> CHANGE  3 : 6  @  8 : 11

~ import java.io.ByteArrayOutputStream;
~ import java.io.InputStreamReader;
~ import java.io.OutputStreamWriter;

> DELETE  7  @  12 : 14

> INSERT  10 : 28  @  17

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

> DELETE  33  @  22 : 24

> DELETE  38  @  29 : 35

> DELETE  41  @  38 : 43

> DELETE  42  @  44 : 52

> CHANGE  65 : 68  @  75 : 78

~ 	public int clouds = 1;
~ 	public boolean fancyGraphics = false;
~ 	public int ambientOcclusion = 0;

> DELETE  76  @  86 : 87

> DELETE  77  @  88 : 89

> CHANGE  113 : 114  @  125 : 126

~ 	public KeyBinding keyBindSprint = new KeyBinding("key.sprint", KeyboardConstants.KEY_R, "key.categories.movement");

> CHANGE  124 : 130  @  136 : 143

~ 	public KeyBinding keyBindSmoothCamera = new KeyBinding("key.smoothCamera", KeyboardConstants.KEY_M,
~ 			"key.categories.misc");
~ 	public KeyBinding keyBindZoomCamera = new KeyBinding("key.zoomCamera", KeyboardConstants.KEY_C,
~ 			"key.categories.misc");
~ 	public KeyBinding keyBindFunction = new KeyBinding("key.function", KeyboardConstants.KEY_F, "key.categories.misc");
~ 	public KeyBinding keyBindClose = new KeyBinding("key.close", KeyboardConstants.KEY_GRAVE, "key.categories.misc");

> DELETE  142  @  155 : 156

> CHANGE  154 : 155  @  168 : 169

~ 	public int guiScale = 3;

> INSERT  158 : 166  @  172

+ 	public boolean hudFps = true;
+ 	public boolean hudCoords = true;
+ 	public boolean hudPlayer = true;
+ 	public boolean hudWorld = false;
+ 	public boolean hudStats = false;
+ 	public boolean hud24h = false;
+ 	public boolean chunkFix = true;
+ 	public boolean fog = true;

> CHANGE  167 : 168  @  173 : 174

~ 	public GameSettings(Minecraft mcIn) {

> CHANGE  172 : 174  @  178 : 181

~ 				this.keyBindTogglePerspective, this.keyBindSmoothCamera, this.keyBindZoomCamera, this.keyBindFunction,
~ 				this.keyBindClose }, this.keyBindsHotbar);

> CHANGE  177 : 179  @  184 : 185

~ 		this.gammaSetting = 1.0F;
~ 		this.language = EagRuntime.getConfiguration().getDefaultLocale();

> CHANGE  181 : 182  @  187 : 193

~ 		GameSettings.Options.RENDER_DISTANCE.setValueMax(18.0F);

> CHANGE  183 : 184  @  194 : 195

~ 		this.renderDistanceChunks = 4;

> DELETE  187  @  198 : 213

> CHANGE  190 : 191  @  216 : 217

~ 						: HString.format("%c", new Object[] { Character.valueOf((char) (parInt1 - 256)) })

> DELETE  274  @  300 : 301

> DELETE  278  @  305 : 306

> INSERT  323 : 325  @  351

+ 			this.mc.loadingScreen.eaglerShow(I18n.format("resourcePack.load.refreshing"),
+ 					I18n.format("resourcePack.load.pleaseWait"));

> DELETE  383  @  409 : 426

> INSERT  396 : 428  @  439

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

> DELETE  482  @  493 : 499

> INSERT  494 : 510  @  511

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

> CHANGE  553 : 556  @  554 : 558

~ 																	: (parOptions == GameSettings.Options.CHAT_SCALE
~ 																			? s + (int) (f * 90.0F + 10.0F) + "%"
~ 																			: (parOptions == GameSettings.Options.CHAT_HEIGHT_UNFOCUSED

> CHANGE  559 : 560  @  561 : 562

~ 																					: (parOptions == GameSettings.Options.CHAT_HEIGHT_FOCUSED

> CHANGE  561 : 562  @  563 : 564

~ 																									.calculateChatboxHeight(

> CHANGE  564 : 583  @  566 : 600

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

> CHANGE  618 : 620  @  635 : 636

~ 			byte[] options = EagRuntime.getStorage("g");
~ 			if (options == null) {

> CHANGE  623 : 625  @  639 : 640

~ 			BufferedReader bufferedreader = new BufferedReader(
~ 					new InputStreamReader(EaglerZLIB.newGZIPInputStream(new EaglerInputStream(options))));

> CHANGE  708 : 714  @  723 : 724

~ 						this.resourcePacks.clear();
~ 						for (Object o : (new JSONArray(s.substring(s.indexOf(58) + 1))).toList()) {
~ 							if (o instanceof String) {
~ 								this.resourcePacks.add((String) o);
~ 							}
~ 						}

> CHANGE  720 : 726  @  730 : 731

~ 						this.field_183018_l.clear();
~ 						for (Object o : (new JSONArray(s.substring(s.indexOf(58) + 1))).toList()) {
~ 							if (o instanceof String) {
~ 								this.field_183018_l.add((String) o);
~ 							}
~ 						}

> DELETE  764  @  769 : 773

> DELETE  768  @  777 : 781

> INSERT  884 : 916  @  897

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

> INSERT  922 : 924  @  903

+ 					Keyboard.setFunctionKeyModifier(keyBindFunction.getKeyCode());
+ 

> DELETE  941  @  920 : 921

> CHANGE  953 : 955  @  933 : 934

~ 			ByteArrayOutputStream bao = new ByteArrayOutputStream();
~ 			PrintWriter printwriter = new PrintWriter(new OutputStreamWriter(EaglerZLIB.newGZIPOutputStream(bao)));

> CHANGE  981 : 983  @  960 : 962

~ 			printwriter.println("resourcePacks:" + toJSONArray(this.resourcePacks));
~ 			printwriter.println("incompatibleResourcePacks:" + toJSONArray(this.field_183018_l));

> DELETE  991  @  970 : 971

> DELETE  992  @  972 : 973

> INSERT  1021 : 1029  @  1002

+ 			printwriter.println("hudFps:" + this.hudFps);
+ 			printwriter.println("hudWorld:" + this.hudWorld);
+ 			printwriter.println("hudCoords:" + this.hudCoords);
+ 			printwriter.println("hudPlayer:" + this.hudPlayer);
+ 			printwriter.println("hudStats:" + this.hudStats);
+ 			printwriter.println("hud24h:" + this.hud24h);
+ 			printwriter.println("chunkFix:" + this.chunkFix);
+ 			printwriter.println("fog:" + this.fog);

> INSERT  1034 : 1036  @  1007

+ 			Keyboard.setFunctionKeyModifier(keyBindFunction.getKeyCode());
+ 

> INSERT  1047 : 1049  @  1018

+ 
+ 			EagRuntime.setStorage("g", bao.toByteArray());

> CHANGE  1059 : 1060  @  1028 : 1029

~ 				: (parSoundCategory == SoundCategory.VOICE ? 0.0F : 1.0F);

> INSERT  1113 : 1121  @  1082

+ 	private String toJSONArray(List<String> e) {
+ 		JSONArray arr = new JSONArray();
+ 		for (String s : e) {
+ 			arr.put(s);
+ 		}
+ 		return arr.toString();
+ 	}
+ 

> CHANGE  1125 : 1126  @  1086 : 1087

~ 		RENDER_DISTANCE("options.renderDistance", true, false, 1.0F, 16.0F, 1.0F),

> CHANGE  1134 : 1136  @  1095 : 1099

~ 		TOUCHSCREEN("options.touchscreen", false, true), CHAT_SCALE("options.chat.scale", true, false),
~ 		CHAT_WIDTH("options.chat.width", true, false), CHAT_HEIGHT_FOCUSED("options.chat.height.focused", true, false),

> CHANGE  1150 : 1155  @  1113 : 1114

~ 		ENTITY_SHADOWS("options.entityShadows", false, true), HUD_FPS("options.hud.fps", false, true),
~ 		HUD_COORDS("options.hud.coords", false, true), HUD_STATS("options.hud.stats", false, true),
~ 		HUD_WORLD("options.hud.world", false, true), HUD_PLAYER("options.hud.player", false, true),
~ 		HUD_24H("options.hud.24h", false, true), CHUNK_FIX("options.chunkFix", false, true),
~ 		FOG("options.fog", false, true);

> EOF
