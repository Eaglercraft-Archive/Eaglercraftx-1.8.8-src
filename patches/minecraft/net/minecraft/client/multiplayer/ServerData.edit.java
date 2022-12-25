
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 13  @  2

+ import java.io.IOException;
+ 
+ import org.json.JSONArray;
+ import org.json.JSONObject;
+ 
+ import net.lax1dude.eaglercraft.v1_8.internal.IServerQuery;
+ import net.lax1dude.eaglercraft.v1_8.internal.QueryResponse;
+ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
+ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
+ import net.lax1dude.eaglercraft.v1_8.profile.EaglerSkinTexture;
+ import net.minecraft.client.Minecraft;

> INSERT  16 : 17  @  5

+ import net.minecraft.util.ResourceLocation;

> CHANGE  21 : 24  @  9 : 12

~ 	public String populationInfo = "";
~ 	public String serverMOTD = "";
~ 	public long pingToServer = -1l;

> CHANGE  29 : 30  @  17 : 18

~ 	public boolean hideAddress = false;

> INSERT  31 : 39  @  19

+ 	public IServerQuery currentQuery = null;
+ 	public final ResourceLocation iconResourceLocation;
+ 	public EaglerSkinTexture iconTextureObject = null;
+ 	public long pingSentTime = -1l;
+ 	public boolean serverIconDirty = false;
+ 	public boolean hasPing = false;
+ 	public boolean serverIconEnabled = false;
+ 	public boolean isDefault = false;

> INSERT  40 : 44  @  20

+ 	private static final Logger logger = LogManager.getLogger("MOTDQuery");
+ 
+ 	private static int serverTextureId = 0;
+ 

> INSERT  48 : 49  @  24

+ 		this.iconResourceLocation = new ResourceLocation("eagler:servers/icons/tex_" + serverTextureId++);

> DELETE  55  @  30 : 33

> INSERT  62 : 64  @  40

+ 		nbttagcompound.setBoolean("hideAddress", this.hideAddress);
+ 

> DELETE  77  @  53 : 56

> INSERT  88 : 94  @  67

+ 		if (nbtCompound.hasKey("hideAddress", 1)) {
+ 			serverdata.hideAddress = nbtCompound.getBoolean("hideAddress");
+ 		} else {
+ 			serverdata.hideAddress = false;
+ 		}
+ 

> DELETE  97  @  70 : 78

> CHANGE  105 : 106  @  86 : 87

~ 		this.hideAddress = serverDataIn.hideAddress;

> INSERT  122 : 188  @  103

+ 
+ 	public void setMOTDFromQuery(QueryResponse pkt) {
+ 		try {
+ 			if (pkt.isResponseJSON()) {
+ 				JSONObject motdData = pkt.getResponseJSON();
+ 				JSONArray motd = motdData.getJSONArray("motd");
+ 				this.serverMOTD = motd.length() > 0
+ 						? (motd.length() > 1 ? motd.getString(0) + "\n" + motd.getString(1) : motd.getString(0))
+ 						: "";
+ 				this.populationInfo = "" + motdData.getInt("online") + "/" + motdData.getInt("max");
+ 				this.playerList = null;
+ 				JSONArray players = motdData.optJSONArray("players");
+ 				if (players.length() > 0) {
+ 					StringBuilder builder = new StringBuilder();
+ 					for (int i = 0, l = players.length(); i < l; ++i) {
+ 						if (i > 0) {
+ 							builder.append('\n');
+ 						}
+ 						builder.append(players.getString(i));
+ 					}
+ 					this.playerList = builder.toString();
+ 				}
+ 				serverIconEnabled = motdData.getBoolean("icon");
+ 				if (!serverIconEnabled) {
+ 					if (iconTextureObject != null) {
+ 						Minecraft.getMinecraft().getTextureManager().deleteTexture(iconResourceLocation);
+ 						iconTextureObject = null;
+ 					}
+ 				}
+ 			} else {
+ 				throw new IOException("Response was not JSON!");
+ 			}
+ 		} catch (Throwable t) {
+ 			pingToServer = -1l;
+ 			logger.error("Could not decode QueryResponse from: {}", serverIP);
+ 			logger.error(t);
+ 		}
+ 	}
+ 
+ 	public void setIconPacket(byte[] pkt) {
+ 		try {
+ 			if (!serverIconEnabled) {
+ 				throw new IOException("Unexpected icon packet on text-only MOTD");
+ 			}
+ 			if (pkt.length != 16384) {
+ 				throw new IOException("MOTD icon packet is the wrong size!");
+ 			}
+ 			int[] pixels = new int[4096];
+ 			for (int i = 0, j; i < 4096; ++i) {
+ 				j = i << 2;
+ 				pixels[i] = ((int) pkt[j] & 0xFF) | (((int) pkt[j + 1] & 0xFF) << 8) | (((int) pkt[j + 2] & 0xFF) << 16)
+ 						| (((int) pkt[j + 3] & 0xFF) << 24);
+ 			}
+ 			if (iconTextureObject != null) {
+ 				iconTextureObject.copyPixelsIn(pixels);
+ 			} else {
+ 				iconTextureObject = new EaglerSkinTexture(pixels, 64, 64);
+ 				Minecraft.getMinecraft().getTextureManager().loadTexture(iconResourceLocation, iconTextureObject);
+ 			}
+ 		} catch (Throwable t) {
+ 			pingToServer = -1l;
+ 			logger.error("Could not decode MOTD icon from: {}", serverIP);
+ 			logger.error(t);
+ 		}
+ 	}
+ 

> EOF
