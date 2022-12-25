
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 7  @  2 : 11

~ import org.json.JSONException;
~ import org.json.JSONObject;
~ 
~ import net.lax1dude.eaglercraft.v1_8.json.JSONTypeSerializer;
~ import net.lax1dude.eaglercraft.v1_8.json.JSONTypeProvider;

> DELETE  8  @  12 : 13

> CHANGE  10 : 14  @  15 : 21

~ 		implements JSONTypeSerializer<PackMetadataSection, JSONObject> {
~ 	public PackMetadataSection deserialize(JSONObject jsonobject) throws JSONException {
~ 		IChatComponent ichatcomponent = JSONTypeProvider.deserialize(jsonobject.get("description"),
~ 				IChatComponent.class);

> CHANGE  15 : 16  @  22 : 23

~ 			throw new JSONException("Invalid/missing description!");

> CHANGE  17 : 18  @  24 : 25

~ 			int i = jsonobject.getInt("pack_format");

> CHANGE  22 : 27  @  29 : 34

~ 	public JSONObject serialize(PackMetadataSection packmetadatasection) {
~ 		JSONObject jsonobject = new JSONObject();
~ 		jsonobject.put("pack_format", packmetadatasection.getPackFormat());
~ 		jsonobject.put("description",
~ 				(JSONObject) JSONTypeProvider.serialize(packmetadatasection.getPackDescription()));

> EOF
