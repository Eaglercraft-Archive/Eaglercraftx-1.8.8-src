
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 3  @  2 : 6

~ import java.io.IOException;

> CHANGE  2 : 3  @  5 : 6

~ import java.nio.charset.StandardCharsets;

> CHANGE  2 : 9  @  2 : 3

~ 
~ import org.json.JSONException;
~ import org.json.JSONObject;
~ 
~ import com.google.common.collect.Maps;
~ 
~ import net.lax1dude.eaglercraft.v1_8.IOUtils;

> DELETE  10  @  4 : 5

> CHANGE  9 : 10  @  10 : 11

~ 	private JSONObject mcmetaJson;

> DELETE  30  @  30 : 31

> CHANGE  2 : 6  @  3 : 5

~ 					this.mcmetaJson = new JSONObject(
~ 							IOUtils.inputStreamToString(this.mcmetaInputStream, StandardCharsets.UTF_8));
~ 				} catch (IOException e) {
~ 					throw new JSONException(e);

> CHANGE  5 : 6  @  3 : 4

~ 					IOUtils.closeQuietly(this.mcmetaInputStream);

> EOF
