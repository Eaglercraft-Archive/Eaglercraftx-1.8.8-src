
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 7  @  2 : 11

~ import org.json.JSONException;
~ import org.json.JSONObject;
~ 
~ import net.lax1dude.eaglercraft.v1_8.json.JSONTypeCodec;
~ import net.lax1dude.eaglercraft.v1_8.json.JSONTypeProvider;

> DELETE  9  @  13 : 15

> CHANGE  339 : 348  @  345 : 357

~ 	public static class Serializer implements JSONTypeCodec<ChatStyle, JSONObject> {
~ 		public ChatStyle deserialize(JSONObject jsonobject) throws JSONException {
~ 			ChatStyle chatstyle = new ChatStyle();
~ 			if (jsonobject == null) {
~ 				return null;
~ 			} else {
~ 				if (jsonobject.has("bold")) {
~ 					chatstyle.bold = jsonobject.getBoolean("bold");
~ 				}

> CHANGE  349 : 352  @  358 : 361

~ 				if (jsonobject.has("italic")) {
~ 					chatstyle.italic = jsonobject.getBoolean("italic");
~ 				}

> CHANGE  353 : 356  @  362 : 365

~ 				if (jsonobject.has("underlined")) {
~ 					chatstyle.underlined = jsonobject.getBoolean("underlined");
~ 				}

> CHANGE  357 : 360  @  366 : 369

~ 				if (jsonobject.has("strikethrough")) {
~ 					chatstyle.strikethrough = jsonobject.getBoolean("strikethrough");
~ 				}

> CHANGE  361 : 364  @  370 : 373

~ 				if (jsonobject.has("obfuscated")) {
~ 					chatstyle.obfuscated = jsonobject.getBoolean("obfuscated");
~ 				}

> CHANGE  365 : 368  @  374 : 378

~ 				if (jsonobject.has("color")) {
~ 					chatstyle.color = EnumChatFormatting.getValueByName(jsonobject.getString("color"));
~ 				}

> CHANGE  369 : 372  @  379 : 382

~ 				if (jsonobject.has("insertion")) {
~ 					chatstyle.insertion = jsonobject.getString("insertion");
~ 				}

> CHANGE  373 : 383  @  383 : 394

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

> INSERT  385 : 386  @  396

+ 				}

> CHANGE  387 : 398  @  397 : 409

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

> DELETE  400  @  411 : 413

> CHANGE  401 : 403  @  414 : 416

~ 
~ 				return chatstyle;

> CHANGE  406 : 407  @  419 : 421

~ 		public JSONObject serialize(ChatStyle chatstyle) {

> CHANGE  410 : 411  @  424 : 425

~ 				JSONObject jsonobject = new JSONObject();

> CHANGE  412 : 413  @  426 : 427

~ 					jsonobject.put("bold", chatstyle.bold);

> CHANGE  416 : 417  @  430 : 431

~ 					jsonobject.put("italic", chatstyle.italic);

> CHANGE  420 : 421  @  434 : 435

~ 					jsonobject.put("underlined", chatstyle.underlined);

> CHANGE  424 : 425  @  438 : 439

~ 					jsonobject.put("strikethrough", chatstyle.strikethrough);

> CHANGE  428 : 429  @  442 : 443

~ 					jsonobject.put("obfuscated", chatstyle.obfuscated);

> CHANGE  432 : 433  @  446 : 447

~ 					jsonobject.put("color", (String) JSONTypeProvider.serialize(chatstyle.color));

> CHANGE  436 : 437  @  450 : 451

~ 					jsonobject.put("insertion", chatstyle.insertion);

> CHANGE  440 : 444  @  454 : 458

~ 					JSONObject jsonobject1 = new JSONObject();
~ 					jsonobject1.put("action", chatstyle.chatClickEvent.getAction().getCanonicalName());
~ 					jsonobject1.put("value", chatstyle.chatClickEvent.getValue());
~ 					jsonobject.put("clickEvent", jsonobject1);

> CHANGE  447 : 452  @  461 : 465

~ 					JSONObject jsonobject2 = new JSONObject();
~ 					jsonobject2.put("action", chatstyle.chatHoverEvent.getAction().getCanonicalName());
~ 					jsonobject2.put("value",
~ 							(JSONObject) JSONTypeProvider.serialize(chatstyle.chatHoverEvent.getValue()));
~ 					jsonobject.put("hoverEvent", jsonobject2);

> EOF
