
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 18

> CHANGE  1 : 3  @  1 : 3

~ 	// private static final Logger logger = LogManager.getLogger();
~ 	// private final Map<String, File> resourceMap = Maps.newHashMap();

> CHANGE  1 : 25  @  1 : 6

~ 	// public ResourceIndex(File parFile, String parString1) {
~ 	/*
~ 	 * if (parString1 != null) { File file1 = new File(parFile, "objects"); File
~ 	 * file2 = new File(parFile, "indexes/" + parString1 + ".json"); BufferedReader
~ 	 * bufferedreader = null;
~ 	 * 
~ 	 * try { bufferedreader = Files.newReader(file2, Charsets.UTF_8); JsonObject
~ 	 * jsonobject = (new JsonParser()).parse(bufferedreader).getAsJsonObject();
~ 	 * JsonObject jsonobject1 = JsonUtils.getJsonObject(jsonobject, "objects",
~ 	 * (JsonObject) null); if (jsonobject1 != null) { for (Entry entry :
~ 	 * jsonobject1.entrySet()) { JsonObject jsonobject2 = (JsonObject)
~ 	 * entry.getValue(); String s = (String) entry.getKey(); String[] astring =
~ 	 * s.split("/", 2); String s1 = astring.length == 1 ? astring[0] : astring[0] +
~ 	 * ":" + astring[1]; String s2 = JsonUtils.getString(jsonobject2, "hash"); File
~ 	 * file3 = new File(file1, s2.substring(0, 2) + "/" + s2);
~ 	 * this.resourceMap.put(s1, file3); } } } catch (JsonParseException var20) {
~ 	 * logger.error("Unable to parse resource index file: " + file2); } catch
~ 	 * (FileNotFoundException var21) {
~ 	 * logger.error("Can\'t find the resource index file: " + file2); } finally {
~ 	 * IOUtils.closeQuietly(bufferedreader); }
~ 	 * 
~ 	 * }
~ 	 */
~ 	// }

> CHANGE  1 : 4  @  1 : 30

~ 	// public Map<String, File> getResourceMap() {
~ 	// return this.resourceMap;
~ 	// }

> EOF
