
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 3  @  2 : 6

~ import java.io.IOException;

> CHANGE  4 : 5  @  7 : 8

~ import java.nio.charset.StandardCharsets;

> CHANGE  6 : 13  @  9 : 10

~ 
~ import org.json.JSONException;
~ import org.json.JSONObject;
~ 
~ import com.google.common.collect.Maps;
~ 
~ import net.lax1dude.eaglercraft.v1_8.IOUtils;

> DELETE  16  @  13 : 14

> CHANGE  25 : 26  @  23 : 24

~ 	private JSONObject mcmetaJson;

> DELETE  55  @  53 : 54

> CHANGE  57 : 61  @  56 : 58

~ 					this.mcmetaJson = new JSONObject(
~ 							IOUtils.inputStreamToString(this.mcmetaInputStream, StandardCharsets.UTF_8));
~ 				} catch (IOException e) {
~ 					throw new JSONException(e);

> CHANGE  62 : 63  @  59 : 60

~ 					IOUtils.closeQuietly(this.mcmetaInputStream);

> EOF
