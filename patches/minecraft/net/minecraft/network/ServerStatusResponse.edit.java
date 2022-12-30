
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 10  @  2 : 13

~ import org.json.JSONArray;
~ import org.json.JSONException;
~ import org.json.JSONObject;
~ 
~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
~ import net.lax1dude.eaglercraft.v1_8.json.JSONTypeCodec;
~ import net.lax1dude.eaglercraft.v1_8.json.JSONTypeProvider;
~ import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;

> DELETE  9  @  12 : 13

> CHANGE  57 : 62  @  58 : 65

~ 				implements JSONTypeCodec<ServerStatusResponse.MinecraftProtocolVersionIdentifier, JSONObject> {
~ 			public ServerStatusResponse.MinecraftProtocolVersionIdentifier deserialize(JSONObject jsonobject)
~ 					throws JSONException {
~ 				return new ServerStatusResponse.MinecraftProtocolVersionIdentifier(jsonobject.getString("name"),
~ 						jsonobject.getInt("protocol"));

> CHANGE  7 : 12  @  9 : 15

~ 			public JSONObject serialize(
~ 					ServerStatusResponse.MinecraftProtocolVersionIdentifier serverstatusresponse$minecraftprotocolversionidentifier) {
~ 				JSONObject jsonobject = new JSONObject();
~ 				jsonobject.put("name", serverstatusresponse$minecraftprotocolversionidentifier.getName());
~ 				jsonobject.put("protocol",

> CHANGE  37 : 39  @  38 : 43

~ 		public static class Serializer implements JSONTypeCodec<ServerStatusResponse.PlayerCountData, JSONObject> {
~ 			public ServerStatusResponse.PlayerCountData deserialize(JSONObject jsonobject) throws JSONException {

> CHANGE  3 : 8  @  6 : 11

~ 						jsonobject.getInt("max"), jsonobject.getInt("online"));
~ 				JSONArray jsonarray = jsonobject.optJSONArray("sample");
~ 				if (jsonarray != null) {
~ 					if (jsonarray.length() > 0) {
~ 						GameProfile[] agameprofile = new GameProfile[jsonarray.length()];

> CHANGE  7 : 11  @  7 : 11

~ 							JSONObject jsonobject1 = jsonarray.getJSONObject(i);
~ 							String s = jsonobject1.getString("id");
~ 							agameprofile[i] = new GameProfile(EaglercraftUUID.fromString(s),
~ 									jsonobject1.getString("name"));

> CHANGE  13 : 18  @  13 : 19

~ 			public JSONObject serialize(ServerStatusResponse.PlayerCountData serverstatusresponse$playercountdata)
~ 					throws JSONException {
~ 				JSONObject jsonobject = new JSONObject();
~ 				jsonobject.put("max", Integer.valueOf(serverstatusresponse$playercountdata.getMaxPlayers()));
~ 				jsonobject.put("online", Integer.valueOf(serverstatusresponse$playercountdata.getOnlinePlayerCount()));

> CHANGE  7 : 8  @  8 : 9

~ 					JSONArray jsonarray = new JSONArray();

> CHANGE  3 : 8  @  3 : 8

~ 						JSONObject jsonobject1 = new JSONObject();
~ 						EaglercraftUUID uuid = serverstatusresponse$playercountdata.getPlayers()[i].getId();
~ 						jsonobject1.put("id", uuid == null ? "" : uuid.toString());
~ 						jsonobject1.put("name", serverstatusresponse$playercountdata.getPlayers()[i].getName());
~ 						jsonarray.put(jsonobject1);

> CHANGE  7 : 8  @  7 : 8

~ 					jsonobject.put("sample", jsonarray);

> CHANGE  8 : 10  @  8 : 13

~ 	public static class Serializer implements JSONTypeCodec<ServerStatusResponse, JSONObject> {
~ 		public ServerStatusResponse deserialize(JSONObject jsonobject) throws JSONException {

> CHANGE  4 : 5  @  7 : 8

~ 				serverstatusresponse.setServerDescription((IChatComponent) JSONTypeProvider

> CHANGE  5 : 7  @  5 : 8

~ 				serverstatusresponse.setPlayerCountData((ServerStatusResponse.PlayerCountData) JSONTypeProvider
~ 						.deserialize(jsonobject.get("players"), ServerStatusResponse.PlayerCountData.class));

> CHANGE  6 : 9  @  7 : 10

~ 						(ServerStatusResponse.MinecraftProtocolVersionIdentifier) JSONTypeProvider.deserialize(
~ 								jsonobject.get("version"),
~ 								ServerStatusResponse.MinecraftProtocolVersionIdentifier.class));

> CHANGE  6 : 7  @  6 : 7

~ 				serverstatusresponse.setFavicon(jsonobject.getString("favicon"));

> CHANGE  6 : 8  @  6 : 9

~ 		public JSONObject serialize(ServerStatusResponse serverstatusresponse) {
~ 			JSONObject jsonobject = new JSONObject();

> CHANGE  3 : 5  @  4 : 6

~ 				jsonobject.put("description",
~ 						(Object) JSONTypeProvider.serialize(serverstatusresponse.getServerDescription()));

> CHANGE  5 : 7  @  5 : 7

~ 				jsonobject.put("players",
~ 						(Object) JSONTypeProvider.serialize(serverstatusresponse.getPlayerCountData()));

> CHANGE  5 : 7  @  5 : 7

~ 				jsonobject.put("version",
~ 						(Object) JSONTypeProvider.serialize(serverstatusresponse.getProtocolVersionInfo()));

> CHANGE  5 : 6  @  5 : 6

~ 				jsonobject.put("favicon", serverstatusresponse.getFavicon());

> EOF
