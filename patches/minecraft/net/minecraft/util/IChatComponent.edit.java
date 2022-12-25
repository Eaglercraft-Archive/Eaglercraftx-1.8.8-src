
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 14

> DELETE  3  @  15 : 23

> INSERT  4 : 11  @  24

+ import org.json.JSONArray;
+ import org.json.JSONException;
+ import org.json.JSONObject;
+ 
+ import net.lax1dude.eaglercraft.v1_8.json.JSONTypeCodec;
+ import net.lax1dude.eaglercraft.v1_8.json.JSONTypeProvider;
+ 

> CHANGE  30 : 31  @  43 : 45

~ 	public static class Serializer implements JSONTypeCodec<IChatComponent, Object> {

> CHANGE  32 : 38  @  46 : 53

~ 		public IChatComponent deserialize(Object parJsonElement) throws JSONException {
~ 			if (parJsonElement instanceof String) {
~ 				return new ChatComponentText((String) parJsonElement);
~ 			} else if (!(parJsonElement instanceof JSONObject)) {
~ 				if (parJsonElement instanceof JSONArray) {
~ 					JSONArray jsonarray1 = (JSONArray) parJsonElement;

> CHANGE  40 : 42  @  55 : 58

~ 					for (Object jsonelement : jsonarray1) {
~ 						IChatComponent ichatcomponent1 = this.deserialize(jsonelement);

> CHANGE  51 : 53  @  67 : 69

~ 					throw new JSONException("Don\'t know how to turn " + parJsonElement.getClass().getSimpleName()
~ 							+ " into a Component");

> CHANGE  55 : 56  @  71 : 72

~ 				JSONObject jsonobject = (JSONObject) parJsonElement;

> CHANGE  58 : 59  @  74 : 75

~ 					object = new ChatComponentText(jsonobject.getString("text"));

> CHANGE  60 : 61  @  76 : 77

~ 					String s = jsonobject.getString("translate");

> CHANGE  62 : 64  @  78 : 80

~ 						JSONArray jsonarray = jsonobject.getJSONArray("with");
~ 						Object[] aobject = new Object[jsonarray.length()];

> CHANGE  66 : 67  @  82 : 83

~ 							aobject[i] = this.deserialize(jsonarray.get(i));

> CHANGE  81 : 82  @  97 : 98

~ 					JSONObject jsonobject1 = jsonobject.getJSONObject("score");

> CHANGE  83 : 84  @  99 : 100

~ 						throw new JSONException("A score component needs a least a name and an objective");

> CHANGE  86 : 87  @  102 : 104

~ 					object = new ChatComponentScore(jsonobject1.getString("name"), jsonobject1.getString("objective"));

> CHANGE  88 : 89  @  105 : 106

~ 						((ChatComponentScore) object).setValue(jsonobject1.getString("value"));

> CHANGE  92 : 93  @  109 : 110

~ 						throw new JSONException(

> CHANGE  96 : 97  @  113 : 114

~ 					object = new ChatComponentSelector(jsonobject.getString("selector"));

> CHANGE  100 : 103  @  117 : 120

~ 					JSONArray jsonarray2 = jsonobject.getJSONArray("extra");
~ 					if (jsonarray2.length() <= 0) {
~ 						throw new JSONException("Unexpected empty array of components");

> CHANGE  105 : 107  @  122 : 125

~ 					for (int j = 0; j < jsonarray2.length(); ++j) {
~ 						((IChatComponent) object).appendSibling(this.deserialize(jsonarray2.get(j)));

> CHANGE  110 : 111  @  128 : 130

~ 				((IChatComponent) object).setChatStyle(JSONTypeProvider.deserialize(parJsonElement, ChatStyle.class));

> CHANGE  115 : 119  @  134 : 142

~ 		private void serializeChatStyle(ChatStyle style, JSONObject object) {
~ 			JSONObject jsonelement = JSONTypeProvider.serialize(style);
~ 			for (String entry : jsonelement.keySet()) {
~ 				object.put(entry, jsonelement.get(entry));

> DELETE  120  @  143 : 144

> CHANGE  122 : 123  @  146 : 148

~ 		public Object serialize(IChatComponent ichatcomponent) {

> CHANGE  125 : 126  @  150 : 151

~ 				return ((ChatComponentText) ichatcomponent).getChatComponentText_TextValue();

> CHANGE  127 : 128  @  152 : 153

~ 				JSONObject jsonobject = new JSONObject();

> CHANGE  129 : 130  @  154 : 155

~ 					this.serializeChatStyle(ichatcomponent.getChatStyle(), jsonobject);

> CHANGE  133 : 134  @  158 : 159

~ 					JSONArray jsonarray = new JSONArray();

> CHANGE  136 : 137  @  161 : 163

~ 						jsonarray.put(this.serialize(ichatcomponent1));

> CHANGE  139 : 140  @  165 : 166

~ 					jsonobject.put("extra", jsonarray);

> CHANGE  143 : 144  @  169 : 171

~ 					jsonobject.put("text", ((ChatComponentText) ichatcomponent).getChatComponentText_TextValue());

> CHANGE  146 : 147  @  173 : 174

~ 					jsonobject.put("translate", chatcomponenttranslation.getKey());

> CHANGE  149 : 150  @  176 : 177

~ 						JSONArray jsonarray1 = new JSONArray();

> CHANGE  153 : 154  @  180 : 182

~ 								jsonarray1.put(this.serialize((IChatComponent) object));

> CHANGE  155 : 156  @  183 : 184

~ 								jsonarray1.put(String.valueOf(object));

> CHANGE  159 : 160  @  187 : 188

~ 						jsonobject.put("with", jsonarray1);

> CHANGE  163 : 168  @  191 : 196

~ 					JSONObject jsonobject1 = new JSONObject();
~ 					jsonobject1.put("name", chatcomponentscore.getName());
~ 					jsonobject1.put("objective", chatcomponentscore.getObjective());
~ 					jsonobject1.put("value", chatcomponentscore.getUnformattedTextForChat());
~ 					jsonobject.put("score", jsonobject1);

> CHANGE  175 : 176  @  203 : 204

~ 					jsonobject.put("selector", chatcomponentselector.getSelector());

> INSERT  182 : 185  @  210

+ 		/**
+ 		 * So sorry for this implementation
+ 		 */

> CHANGE  186 : 192  @  211 : 212

~ 			if (component instanceof ChatComponentText) {
~ 				String escaped = new JSONObject().put("E", component.getUnformattedTextForChat()).toString();
~ 				return escaped.substring(5, escaped.length() - 1);
~ 			} else {
~ 				return JSONTypeProvider.serialize(component).toString();
~ 			}

> CHANGE  195 : 196  @  215 : 216

~ 			return (IChatComponent) JSONTypeProvider.deserialize(json, IChatComponent.class);

> INSERT  197 : 198  @  217

+ 	}

> CHANGE  199 : 212  @  218 : 224

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

> INSERT  213 : 215  @  225

+ 
+ 		return chatcomponenttext;

> EOF
