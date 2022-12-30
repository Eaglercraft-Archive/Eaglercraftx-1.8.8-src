
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 14

> DELETE  1  @  13 : 21

> INSERT  1 : 8  @  9

+ import org.json.JSONArray;
+ import org.json.JSONException;
+ import org.json.JSONObject;
+ 
+ import net.lax1dude.eaglercraft.v1_8.json.JSONTypeCodec;
+ import net.lax1dude.eaglercraft.v1_8.json.JSONTypeProvider;
+ 

> CHANGE  26 : 27  @  19 : 21

~ 	public static class Serializer implements JSONTypeCodec<IChatComponent, Object> {

> CHANGE  2 : 8  @  3 : 10

~ 		public IChatComponent deserialize(Object parJsonElement) throws JSONException {
~ 			if (parJsonElement instanceof String) {
~ 				return new ChatComponentText((String) parJsonElement);
~ 			} else if (!(parJsonElement instanceof JSONObject)) {
~ 				if (parJsonElement instanceof JSONArray) {
~ 					JSONArray jsonarray1 = (JSONArray) parJsonElement;

> CHANGE  8 : 10  @  9 : 12

~ 					for (Object jsonelement : jsonarray1) {
~ 						IChatComponent ichatcomponent1 = this.deserialize(jsonelement);

> CHANGE  11 : 13  @  12 : 14

~ 					throw new JSONException("Don\'t know how to turn " + parJsonElement.getClass().getSimpleName()
~ 							+ " into a Component");

> CHANGE  4 : 5  @  4 : 5

~ 				JSONObject jsonobject = (JSONObject) parJsonElement;

> CHANGE  3 : 4  @  3 : 4

~ 					object = new ChatComponentText(jsonobject.getString("text"));

> CHANGE  2 : 3  @  2 : 3

~ 					String s = jsonobject.getString("translate");

> CHANGE  2 : 4  @  2 : 4

~ 						JSONArray jsonarray = jsonobject.getJSONArray("with");
~ 						Object[] aobject = new Object[jsonarray.length()];

> CHANGE  4 : 5  @  4 : 5

~ 							aobject[i] = this.deserialize(jsonarray.get(i));

> CHANGE  15 : 16  @  15 : 16

~ 					JSONObject jsonobject1 = jsonobject.getJSONObject("score");

> CHANGE  2 : 3  @  2 : 3

~ 						throw new JSONException("A score component needs a least a name and an objective");

> CHANGE  3 : 4  @  3 : 5

~ 					object = new ChatComponentScore(jsonobject1.getString("name"), jsonobject1.getString("objective"));

> CHANGE  2 : 3  @  3 : 4

~ 						((ChatComponentScore) object).setValue(jsonobject1.getString("value"));

> CHANGE  4 : 5  @  4 : 5

~ 						throw new JSONException(

> CHANGE  4 : 5  @  4 : 5

~ 					object = new ChatComponentSelector(jsonobject.getString("selector"));

> CHANGE  4 : 7  @  4 : 7

~ 					JSONArray jsonarray2 = jsonobject.getJSONArray("extra");
~ 					if (jsonarray2.length() <= 0) {
~ 						throw new JSONException("Unexpected empty array of components");

> CHANGE  5 : 7  @  5 : 8

~ 					for (int j = 0; j < jsonarray2.length(); ++j) {
~ 						((IChatComponent) object).appendSibling(this.deserialize(jsonarray2.get(j)));

> CHANGE  5 : 6  @  6 : 8

~ 				((IChatComponent) object).setChatStyle(JSONTypeProvider.deserialize(parJsonElement, ChatStyle.class));

> CHANGE  5 : 9  @  6 : 14

~ 		private void serializeChatStyle(ChatStyle style, JSONObject object) {
~ 			JSONObject jsonelement = JSONTypeProvider.serialize(style);
~ 			for (String entry : jsonelement.keySet()) {
~ 				object.put(entry, jsonelement.get(entry));

> DELETE  5  @  9 : 10

> CHANGE  2 : 3  @  3 : 5

~ 		public Object serialize(IChatComponent ichatcomponent) {

> CHANGE  3 : 4  @  4 : 5

~ 				return ((ChatComponentText) ichatcomponent).getChatComponentText_TextValue();

> CHANGE  2 : 3  @  2 : 3

~ 				JSONObject jsonobject = new JSONObject();

> CHANGE  2 : 3  @  2 : 3

~ 					this.serializeChatStyle(ichatcomponent.getChatStyle(), jsonobject);

> CHANGE  4 : 5  @  4 : 5

~ 					JSONArray jsonarray = new JSONArray();

> CHANGE  3 : 4  @  3 : 5

~ 						jsonarray.put(this.serialize(ichatcomponent1));

> CHANGE  3 : 4  @  4 : 5

~ 					jsonobject.put("extra", jsonarray);

> CHANGE  4 : 5  @  4 : 6

~ 					jsonobject.put("text", ((ChatComponentText) ichatcomponent).getChatComponentText_TextValue());

> CHANGE  3 : 4  @  4 : 5

~ 					jsonobject.put("translate", chatcomponenttranslation.getKey());

> CHANGE  3 : 4  @  3 : 4

~ 						JSONArray jsonarray1 = new JSONArray();

> CHANGE  4 : 5  @  4 : 6

~ 								jsonarray1.put(this.serialize((IChatComponent) object));

> CHANGE  2 : 3  @  3 : 4

~ 								jsonarray1.put(String.valueOf(object));

> CHANGE  4 : 5  @  4 : 5

~ 						jsonobject.put("with", jsonarray1);

> CHANGE  4 : 9  @  4 : 9

~ 					JSONObject jsonobject1 = new JSONObject();
~ 					jsonobject1.put("name", chatcomponentscore.getName());
~ 					jsonobject1.put("objective", chatcomponentscore.getObjective());
~ 					jsonobject1.put("value", chatcomponentscore.getUnformattedTextForChat());
~ 					jsonobject.put("score", jsonobject1);

> CHANGE  12 : 13  @  12 : 13

~ 					jsonobject.put("selector", chatcomponentselector.getSelector());

> INSERT  7 : 10  @  7

+ 		/**
+ 		 * So sorry for this implementation
+ 		 */

> CHANGE  4 : 10  @  1 : 2

~ 			if (component instanceof ChatComponentText) {
~ 				String escaped = new JSONObject().put("E", component.getUnformattedTextForChat()).toString();
~ 				return escaped.substring(5, escaped.length() - 1);
~ 			} else {
~ 				return JSONTypeProvider.serialize(component).toString();
~ 			}

> CHANGE  9 : 10  @  4 : 5

~ 			return (IChatComponent) JSONTypeProvider.deserialize(json, IChatComponent.class);

> INSERT  2 : 3  @  2

+ 	}

> CHANGE  2 : 15  @  1 : 7

~ 	public static IChatComponent join(List<IChatComponent> components) {
~ 		ChatComponentText chatcomponenttext = new ChatComponentText("");
~ 
~ 		for (int i = 0; i < components.size(); ++i) {
~ 			if (i > 0) {
~ 				if (i == components.size() - 1) {
~ 					chatcomponenttext.appendText(" and ");
~ 				} else if (i > 0) {
~ 					chatcomponenttext.appendText(", ");
~ 				}
~ 			}
~ 
~ 			chatcomponenttext.appendSibling((IChatComponent) components.get(i));

> INSERT  14 : 16  @  7

+ 
+ 		return chatcomponenttext;

> EOF
