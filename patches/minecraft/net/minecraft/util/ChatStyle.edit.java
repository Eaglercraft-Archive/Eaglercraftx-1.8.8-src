
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 7  @  2 : 11

~ import org.json.JSONException;
~ import org.json.JSONObject;
~ 
~ import net.lax1dude.eaglercraft.v1_8.json.JSONTypeCodec;
~ import net.lax1dude.eaglercraft.v1_8.json.JSONTypeProvider;

> DELETE  2  @  2 : 4

> CHANGE  330 : 339  @  330 : 342

~ 	public static class Serializer implements JSONTypeCodec<ChatStyle, JSONObject> {
~ 		public ChatStyle deserialize(JSONObject jsonobject) throws JSONException {
~ 			ChatStyle chatstyle = new ChatStyle();
~ 			if (jsonobject == null) {
~ 				return null;
~ 			} else {
~ 				if (jsonobject.has("bold")) {
~ 					chatstyle.bold = jsonobject.getBoolean("bold");
~ 				}

> CHANGE  1 : 4  @  1 : 4

~ 				if (jsonobject.has("italic")) {
~ 					chatstyle.italic = jsonobject.getBoolean("italic");
~ 				}

> CHANGE  1 : 4  @  1 : 4

~ 				if (jsonobject.has("underlined")) {
~ 					chatstyle.underlined = jsonobject.getBoolean("underlined");
~ 				}

> CHANGE  1 : 4  @  1 : 4

~ 				if (jsonobject.has("strikethrough")) {
~ 					chatstyle.strikethrough = jsonobject.getBoolean("strikethrough");
~ 				}

> CHANGE  1 : 4  @  1 : 4

~ 				if (jsonobject.has("obfuscated")) {
~ 					chatstyle.obfuscated = jsonobject.getBoolean("obfuscated");
~ 				}

> CHANGE  1 : 4  @  1 : 5

~ 				if (jsonobject.has("color")) {
~ 					chatstyle.color = EnumChatFormatting.getValueByName(jsonobject.getString("color"));
~ 				}

> CHANGE  1 : 4  @  1 : 4

~ 				if (jsonobject.has("insertion")) {
~ 					chatstyle.insertion = jsonobject.getString("insertion");
~ 				}

> CHANGE  1 : 11  @  1 : 12

~ 				if (jsonobject.has("clickEvent")) {
~ 					JSONObject jsonobject1 = jsonobject.getJSONObject("clickEvent");
~ 					if (jsonobject1 != null) {
~ 						String jsonprimitive = jsonobject1.optString("action");
~ 						ClickEvent.Action clickevent$action = jsonprimitive == null ? null
~ 								: ClickEvent.Action.getValueByCanonicalName(jsonprimitive);
~ 						String jsonprimitive1 = jsonobject1.optString("value");
~ 						if (clickevent$action != null && jsonprimitive1 != null
~ 								&& clickevent$action.shouldAllowInChat()) {
~ 							chatstyle.chatClickEvent = new ClickEvent(clickevent$action, jsonprimitive1);

> INSERT  2 : 3  @  2

+ 				}

> CHANGE  1 : 12  @  1 : 13

~ 				if (jsonobject.has("hoverEvent")) {
~ 					JSONObject jsonobject2 = jsonobject.getJSONObject("hoverEvent");
~ 					if (jsonobject2 != null) {
~ 						String jsonprimitive2 = jsonobject2.getString("action");
~ 						HoverEvent.Action hoverevent$action = jsonprimitive2 == null ? null
~ 								: HoverEvent.Action.getValueByCanonicalName(jsonprimitive2);
~ 						IChatComponent ichatcomponent = JSONTypeProvider.deserializeNoCast(jsonobject2.get("value"),
~ 								IChatComponent.class);
~ 						if (hoverevent$action != null && ichatcomponent != null
~ 								&& hoverevent$action.shouldAllowInChat()) {
~ 							chatstyle.chatHoverEvent = new HoverEvent(hoverevent$action, ichatcomponent);

> DELETE  2  @  2 : 4

> CHANGE  1 : 3  @  1 : 3

~ 
~ 				return chatstyle;

> CHANGE  3 : 4  @  3 : 5

~ 		public JSONObject serialize(ChatStyle chatstyle) {

> CHANGE  3 : 4  @  3 : 4

~ 				JSONObject jsonobject = new JSONObject();

> CHANGE  1 : 2  @  1 : 2

~ 					jsonobject.put("bold", chatstyle.bold);

> CHANGE  3 : 4  @  3 : 4

~ 					jsonobject.put("italic", chatstyle.italic);

> CHANGE  3 : 4  @  3 : 4

~ 					jsonobject.put("underlined", chatstyle.underlined);

> CHANGE  3 : 4  @  3 : 4

~ 					jsonobject.put("strikethrough", chatstyle.strikethrough);

> CHANGE  3 : 4  @  3 : 4

~ 					jsonobject.put("obfuscated", chatstyle.obfuscated);

> CHANGE  3 : 4  @  3 : 4

~ 					jsonobject.put("color", (String) JSONTypeProvider.serialize(chatstyle.color));

> CHANGE  3 : 4  @  3 : 4

~ 					jsonobject.put("insertion", chatstyle.insertion);

> CHANGE  3 : 7  @  3 : 7

~ 					JSONObject jsonobject1 = new JSONObject();
~ 					jsonobject1.put("action", chatstyle.chatClickEvent.getAction().getCanonicalName());
~ 					jsonobject1.put("value", chatstyle.chatClickEvent.getValue());
~ 					jsonobject.put("clickEvent", jsonobject1);

> CHANGE  3 : 8  @  3 : 7

~ 					JSONObject jsonobject2 = new JSONObject();
~ 					jsonobject2.put("action", chatstyle.chatHoverEvent.getAction().getCanonicalName());
~ 					jsonobject2.put("value",
~ 							(JSONObject) JSONTypeProvider.serialize(chatstyle.chatHoverEvent.getValue()));
~ 					jsonobject.put("hoverEvent", jsonobject2);

> EOF
