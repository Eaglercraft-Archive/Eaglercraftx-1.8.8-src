
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 3  @  2 : 4

~ import org.json.JSONObject;

> CHANGE  1 : 4  @  1 : 2

~ import net.lax1dude.eaglercraft.v1_8.json.JSONTypeDeserializer;
~ 
~ public interface IMetadataSectionSerializer<T extends IMetadataSection> extends JSONTypeDeserializer<JSONObject, T> {

> EOF
