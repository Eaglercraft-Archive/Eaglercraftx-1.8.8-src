
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 7  @  2 : 11

~ import org.json.JSONException;
~ import org.json.JSONObject;
~ 
~ import net.lax1dude.eaglercraft.v1_8.json.JSONTypeSerializer;
~ import net.lax1dude.eaglercraft.v1_8.json.JSONTypeProvider;

> DELETE  1  @  1 : 2

> CHANGE  2 : 6  @  2 : 8

~ 		implements JSONTypeSerializer<PackMetadataSection, JSONObject> {
~ 	public PackMetadataSection deserialize(JSONObject jsonobject) throws JSONException {
~ 		IChatComponent ichatcomponent = JSONTypeProvider.deserialize(jsonobject.get("description"),
~ 				IChatComponent.class);

> CHANGE  1 : 2  @  1 : 2

~ 			throw new JSONException("Invalid/missing description!");

> CHANGE  1 : 2  @  1 : 2

~ 			int i = jsonobject.getInt("pack_format");

> CHANGE  4 : 9  @  4 : 9

~ 	public JSONObject serialize(PackMetadataSection packmetadatasection) {
~ 		JSONObject jsonobject = new JSONObject();
~ 		jsonobject.put("pack_format", packmetadatasection.getPackFormat());
~ 		jsonobject.put("description",
~ 				(JSONObject) JSONTypeProvider.serialize(packmetadatasection.getPackDescription()));

> EOF
