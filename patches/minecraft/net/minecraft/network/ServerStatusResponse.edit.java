
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

> DELETE  11  @  14 : 15

> CHANGE  68 : 73  @  72 : 79

~ 				implements JSONTypeCodec<ServerStatusResponse.MinecraftProtocolVersionIdentifier, JSONObject> {
~ 			public ServerStatusResponse.MinecraftProtocolVersionIdentifier deserialize(JSONObject jsonobject)
~ 					throws JSONException {
~ 				return new ServerStatusResponse.MinecraftProtocolVersionIdentifier(jsonobject.getString("name"),
~ 						jsonobject.getInt("protocol"));

> CHANGE  75 : 80  @  81 : 87

~ 			public JSONObject serialize(
~ 					ServerStatusResponse.MinecraftProtocolVersionIdentifier serverstatusresponse$minecraftprotocolversionidentifier) {
~ 				JSONObject jsonobject = new JSONObject();
~ 				jsonobject.put("name", serverstatusresponse$minecraftprotocolversionidentifier.getName());
~ 				jsonobject.put("protocol",

> CHANGE  112 : 114  @  119 : 124

~ 		public static class Serializer implements JSONTypeCodec<ServerStatusResponse.PlayerCountData, JSONObject> {
~ 			public ServerStatusResponse.PlayerCountData deserialize(JSONObject jsonobject) throws JSONException {

> CHANGE  115 : 120  @  125 : 130

~ 						jsonobject.getInt("max"), jsonobject.getInt("online"));
~ 				JSONArray jsonarray = jsonobject.optJSONArray("sample");
~ 				if (jsonarray != null) {
~ 					if (jsonarray.length() > 0) {
~ 						GameProfile[] agameprofile = new GameProfile[jsonarray.length()];

> CHANGE  122 : 126  @  132 : 136

~ 							JSONObject jsonobject1 = jsonarray.getJSONObject(i);
~ 							String s = jsonobject1.getString("id");
~ 							agameprofile[i] = new GameProfile(EaglercraftUUID.fromString(s),
~ 									jsonobject1.getString("name"));

> CHANGE  135 : 140  @  145 : 151

~ 			public JSONObject serialize(ServerStatusResponse.PlayerCountData serverstatusresponse$playercountdata)
~ 					throws JSONException {
~ 				JSONObject jsonobject = new JSONObject();
~ 				jsonobject.put("max", Integer.valueOf(serverstatusresponse$playercountdata.getMaxPlayers()));
~ 				jsonobject.put("online", Integer.valueOf(serverstatusresponse$playercountdata.getOnlinePlayerCount()));

> CHANGE  142 : 143  @  153 : 154

~ 					JSONArray jsonarray = new JSONArray();

> CHANGE  145 : 150  @  156 : 161

~ 						JSONObject jsonobject1 = new JSONObject();
~ 						EaglercraftUUID uuid = serverstatusresponse$playercountdata.getPlayers()[i].getId();
~ 						jsonobject1.put("id", uuid == null ? "" : uuid.toString());
~ 						jsonobject1.put("name", serverstatusresponse$playercountdata.getPlayers()[i].getName());
~ 						jsonarray.put(jsonobject1);

> CHANGE  152 : 153  @  163 : 164

~ 					jsonobject.put("sample", jsonarray);

> CHANGE  160 : 162  @  171 : 176

~ 	public static class Serializer implements JSONTypeCodec<ServerStatusResponse, JSONObject> {
~ 		public ServerStatusResponse deserialize(JSONObject jsonobject) throws JSONException {

> CHANGE  164 : 165  @  178 : 179

~ 				serverstatusresponse.setServerDescription((IChatComponent) JSONTypeProvider

> CHANGE  169 : 171  @  183 : 186

~ 				serverstatusresponse.setPlayerCountData((ServerStatusResponse.PlayerCountData) JSONTypeProvider
~ 						.deserialize(jsonobject.get("players"), ServerStatusResponse.PlayerCountData.class));

> CHANGE  175 : 178  @  190 : 193

~ 						(ServerStatusResponse.MinecraftProtocolVersionIdentifier) JSONTypeProvider.deserialize(
~ 								jsonobject.get("version"),
~ 								ServerStatusResponse.MinecraftProtocolVersionIdentifier.class));

> CHANGE  181 : 182  @  196 : 197

~ 				serverstatusresponse.setFavicon(jsonobject.getString("favicon"));

> CHANGE  187 : 189  @  202 : 205

~ 		public JSONObject serialize(ServerStatusResponse serverstatusresponse) {
~ 			JSONObject jsonobject = new JSONObject();

> CHANGE  190 : 192  @  206 : 208

~ 				jsonobject.put("description",
~ 						(Object) JSONTypeProvider.serialize(serverstatusresponse.getServerDescription()));

> CHANGE  195 : 197  @  211 : 213

~ 				jsonobject.put("players",
~ 						(Object) JSONTypeProvider.serialize(serverstatusresponse.getPlayerCountData()));

> CHANGE  200 : 202  @  216 : 218

~ 				jsonobject.put("version",
~ 						(Object) JSONTypeProvider.serialize(serverstatusresponse.getProtocolVersionInfo()));

> CHANGE  205 : 206  @  221 : 222

~ 				jsonobject.put("favicon", serverstatusresponse.getFavicon());

> EOF
