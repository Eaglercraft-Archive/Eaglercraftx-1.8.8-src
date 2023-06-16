
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 14

> DELETE  1  @  1 : 9

> INSERT  1 : 8  @  1

+ import org.json.JSONArray;
+ import org.json.JSONException;
+ import org.json.JSONObject;
+ 
+ import net.lax1dude.eaglercraft.v1_8.json.JSONTypeCodec;
+ import net.lax1dude.eaglercraft.v1_8.json.JSONTypeProvider;
+ 

> CHANGE  19 : 20  @  19 : 21

~ 	public static class Serializer implements JSONTypeCodec<IChatComponent, Object> {

> CHANGE  1 : 7  @  1 : 8

~ 		public IChatComponent deserialize(Object parJsonElement) throws JSONException {
~ 			if (parJsonElement instanceof String) {
~ 				return new ChatComponentText((String) parJsonElement);
~ 			} else if (!(parJsonElement instanceof JSONObject)) {
~ 				if (parJsonElement instanceof JSONArray) {
~ 					JSONArray jsonarray1 = (JSONArray) parJsonElement;

> CHANGE  2 : 4  @  2 : 5

~ 					for (Object jsonelement : jsonarray1) {
~ 						IChatComponent ichatcomponent1 = this.deserialize(jsonelement);

> CHANGE  9 : 11  @  9 : 11

~ 					throw new JSONException("Don\'t know how to turn " + parJsonElement.getClass().getSimpleName()
~ 							+ " into a Component");

> CHANGE  2 : 3  @  2 : 3

~ 				JSONObject jsonobject = (JSONObject) parJsonElement;

> CHANGE  2 : 3  @  2 : 3

~ 					object = new ChatComponentText(jsonobject.getString("text"));

> CHANGE  1 : 2  @  1 : 2

~ 					String s = jsonobject.getString("translate");

> CHANGE  1 : 3  @  1 : 3

~ 						JSONArray jsonarray = jsonobject.getJSONArray("with");
~ 						Object[] aobject = new Object[jsonarray.length()];

> CHANGE  2 : 3  @  2 : 3

~ 							aobject[i] = this.deserialize(jsonarray.get(i));

> CHANGE  14 : 15  @  14 : 15

~ 					JSONObject jsonobject1 = jsonobject.getJSONObject("score");

> CHANGE  1 : 2  @  1 : 2

~ 						throw new JSONException("A score component needs a least a name and an objective");

> CHANGE  2 : 3  @  2 : 4

~ 					object = new ChatComponentScore(jsonobject1.getString("name"), jsonobject1.getString("objective"));

> CHANGE  1 : 2  @  1 : 2

~ 						((ChatComponentScore) object).setValue(jsonobject1.getString("value"));

> CHANGE  3 : 4  @  3 : 4

~ 						throw new JSONException(

> CHANGE  3 : 4  @  3 : 4

~ 					object = new ChatComponentSelector(jsonobject.getString("selector"));

> CHANGE  3 : 6  @  3 : 6

~ 					JSONArray jsonarray2 = jsonobject.getJSONArray("extra");
~ 					if (jsonarray2.length() <= 0) {
~ 						throw new JSONException("Unexpected empty array of components");

> CHANGE  2 : 4  @  2 : 5

~ 					for (int j = 0; j < jsonarray2.length(); ++j) {
~ 						((IChatComponent) object).appendSibling(this.deserialize(jsonarray2.get(j)));

> CHANGE  3 : 4  @  3 : 5

~ 				((IChatComponent) object).setChatStyle(JSONTypeProvider.deserialize(parJsonElement, ChatStyle.class));

> CHANGE  4 : 8  @  4 : 12

~ 		private void serializeChatStyle(ChatStyle style, JSONObject object) {
~ 			JSONObject jsonelement = JSONTypeProvider.serialize(style);
~ 			for (String entry : jsonelement.keySet()) {
~ 				object.put(entry, jsonelement.get(entry));

> DELETE  1  @  1 : 2

> CHANGE  2 : 3  @  2 : 4

~ 		public Object serialize(IChatComponent ichatcomponent) {

> CHANGE  2 : 3  @  2 : 3

~ 				return ((ChatComponentText) ichatcomponent).getChatComponentText_TextValue();

> CHANGE  1 : 2  @  1 : 2

~ 				JSONObject jsonobject = new JSONObject();

> CHANGE  1 : 2  @  1 : 2

~ 					this.serializeChatStyle(ichatcomponent.getChatStyle(), jsonobject);

> CHANGE  3 : 4  @  3 : 4

~ 					JSONArray jsonarray = new JSONArray();

> CHANGE  2 : 3  @  2 : 4

~ 						jsonarray.put(this.serialize(ichatcomponent1));

> CHANGE  2 : 3  @  2 : 3

~ 					jsonobject.put("extra", jsonarray);

> CHANGE  3 : 4  @  3 : 5

~ 					jsonobject.put("text", ((ChatComponentText) ichatcomponent).getChatComponentText_TextValue());

> CHANGE  2 : 3  @  2 : 3

~ 					jsonobject.put("translate", chatcomponenttranslation.getKey());

> CHANGE  2 : 3  @  2 : 3

~ 						JSONArray jsonarray1 = new JSONArray();

> CHANGE  3 : 4  @  3 : 5

~ 								jsonarray1.put(this.serialize((IChatComponent) object));

> CHANGE  1 : 2  @  1 : 2

~ 								jsonarray1.put(String.valueOf(object));

> CHANGE  3 : 4  @  3 : 4

~ 						jsonobject.put("with", jsonarray1);

> CHANGE  3 : 8  @  3 : 8

~ 					JSONObject jsonobject1 = new JSONObject();
~ 					jsonobject1.put("name", chatcomponentscore.getName());
~ 					jsonobject1.put("objective", chatcomponentscore.getObjective());
~ 					jsonobject1.put("value", chatcomponentscore.getUnformattedTextForChat());
~ 					jsonobject.put("score", jsonobject1);

> CHANGE  7 : 8  @  7 : 8

~ 					jsonobject.put("selector", chatcomponentselector.getSelector());

> INSERT  6 : 9  @  6

+ 		/**
+ 		 * So sorry for this implementation
+ 		 */

> CHANGE  1 : 7  @  1 : 2

~ 			if (component instanceof ChatComponentText) {
~ 				String escaped = new JSONObject().put("E", component.getUnformattedTextForChat()).toString();
~ 				return escaped.substring(5, escaped.length() - 1);
~ 			} else {
~ 				return JSONTypeProvider.serialize(component).toString();
~ 			}

> CHANGE  3 : 4  @  3 : 4

~ 			return (IChatComponent) JSONTypeProvider.deserialize(json, IChatComponent.class);

> INSERT  1 : 2  @  1

+ 	}

> CHANGE  1 : 14  @  1 : 7

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

> INSERT  1 : 3  @  1

+ 
+ 		return chatcomponenttext;

> EOF
