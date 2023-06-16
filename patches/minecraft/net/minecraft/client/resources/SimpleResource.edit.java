
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 3  @  2 : 6

~ import java.io.IOException;

> CHANGE  1 : 2  @  1 : 2

~ import java.nio.charset.StandardCharsets;

> CHANGE  1 : 8  @  1 : 2

~ 
~ import org.json.JSONException;
~ import org.json.JSONObject;
~ 
~ import com.google.common.collect.Maps;
~ 
~ import net.lax1dude.eaglercraft.v1_8.IOUtils;

> DELETE  3  @  3 : 4

> CHANGE  9 : 10  @  9 : 10

~ 	private JSONObject mcmetaJson;

> DELETE  29  @  29 : 30

> CHANGE  2 : 6  @  2 : 4

~ 					this.mcmetaJson = new JSONObject(
~ 							IOUtils.inputStreamToString(this.mcmetaInputStream, StandardCharsets.UTF_8));
~ 				} catch (IOException e) {
~ 					throw new JSONException(e);

> CHANGE  1 : 2  @  1 : 2

~ 					IOUtils.closeQuietly(this.mcmetaInputStream);

> EOF
