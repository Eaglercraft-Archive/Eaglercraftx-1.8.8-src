
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

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

> DELETE  1  @  1 : 2

> CHANGE  57 : 62  @  57 : 64

~ 				implements JSONTypeCodec<ServerStatusResponse.MinecraftProtocolVersionIdentifier, JSONObject> {
~ 			public ServerStatusResponse.MinecraftProtocolVersionIdentifier deserialize(JSONObject jsonobject)
~ 					throws JSONException {
~ 				return new ServerStatusResponse.MinecraftProtocolVersionIdentifier(jsonobject.getString("name"),
~ 						jsonobject.getInt("protocol"));

> CHANGE  2 : 7  @  2 : 8

~ 			public JSONObject serialize(
~ 					ServerStatusResponse.MinecraftProtocolVersionIdentifier serverstatusresponse$minecraftprotocolversionidentifier) {
~ 				JSONObject jsonobject = new JSONObject();
~ 				jsonobject.put("name", serverstatusresponse$minecraftprotocolversionidentifier.getName());
~ 				jsonobject.put("protocol",

> CHANGE  32 : 34  @  32 : 37

~ 		public static class Serializer implements JSONTypeCodec<ServerStatusResponse.PlayerCountData, JSONObject> {
~ 			public ServerStatusResponse.PlayerCountData deserialize(JSONObject jsonobject) throws JSONException {

> CHANGE  1 : 6  @  1 : 6

~ 						jsonobject.getInt("max"), jsonobject.getInt("online"));
~ 				JSONArray jsonarray = jsonobject.optJSONArray("sample");
~ 				if (jsonarray != null) {
~ 					if (jsonarray.length() > 0) {
~ 						GameProfile[] agameprofile = new GameProfile[jsonarray.length()];

> CHANGE  2 : 6  @  2 : 6

~ 							JSONObject jsonobject1 = jsonarray.getJSONObject(i);
~ 							String s = jsonobject1.getString("id");
~ 							agameprofile[i] = new GameProfile(EaglercraftUUID.fromString(s),
~ 									jsonobject1.getString("name"));

> CHANGE  9 : 14  @  9 : 15

~ 			public JSONObject serialize(ServerStatusResponse.PlayerCountData serverstatusresponse$playercountdata)
~ 					throws JSONException {
~ 				JSONObject jsonobject = new JSONObject();
~ 				jsonobject.put("max", Integer.valueOf(serverstatusresponse$playercountdata.getMaxPlayers()));
~ 				jsonobject.put("online", Integer.valueOf(serverstatusresponse$playercountdata.getOnlinePlayerCount()));

> CHANGE  2 : 3  @  2 : 3

~ 					JSONArray jsonarray = new JSONArray();

> CHANGE  2 : 7  @  2 : 7

~ 						JSONObject jsonobject1 = new JSONObject();
~ 						EaglercraftUUID uuid = serverstatusresponse$playercountdata.getPlayers()[i].getId();
~ 						jsonobject1.put("id", uuid == null ? "" : uuid.toString());
~ 						jsonobject1.put("name", serverstatusresponse$playercountdata.getPlayers()[i].getName());
~ 						jsonarray.put(jsonobject1);

> CHANGE  2 : 3  @  2 : 3

~ 					jsonobject.put("sample", jsonarray);

> CHANGE  7 : 9  @  7 : 12

~ 	public static class Serializer implements JSONTypeCodec<ServerStatusResponse, JSONObject> {
~ 		public ServerStatusResponse deserialize(JSONObject jsonobject) throws JSONException {

> CHANGE  2 : 3  @  2 : 3

~ 				serverstatusresponse.setServerDescription((IChatComponent) JSONTypeProvider

> CHANGE  4 : 6  @  4 : 7

~ 				serverstatusresponse.setPlayerCountData((ServerStatusResponse.PlayerCountData) JSONTypeProvider
~ 						.deserialize(jsonobject.get("players"), ServerStatusResponse.PlayerCountData.class));

> CHANGE  4 : 7  @  4 : 7

~ 						(ServerStatusResponse.MinecraftProtocolVersionIdentifier) JSONTypeProvider.deserialize(
~ 								jsonobject.get("version"),
~ 								ServerStatusResponse.MinecraftProtocolVersionIdentifier.class));

> CHANGE  3 : 4  @  3 : 4

~ 				serverstatusresponse.setFavicon(jsonobject.getString("favicon"));

> CHANGE  5 : 7  @  5 : 8

~ 		public JSONObject serialize(ServerStatusResponse serverstatusresponse) {
~ 			JSONObject jsonobject = new JSONObject();

> CHANGE  1 : 3  @  1 : 3

~ 				jsonobject.put("description",
~ 						(Object) JSONTypeProvider.serialize(serverstatusresponse.getServerDescription()));

> CHANGE  3 : 5  @  3 : 5

~ 				jsonobject.put("players",
~ 						(Object) JSONTypeProvider.serialize(serverstatusresponse.getPlayerCountData()));

> CHANGE  3 : 5  @  3 : 5

~ 				jsonobject.put("version",
~ 						(Object) JSONTypeProvider.serialize(serverstatusresponse.getProtocolVersionInfo()));

> CHANGE  3 : 4  @  3 : 4

~ 				jsonobject.put("favicon", serverstatusresponse.getFavicon());

> EOF
