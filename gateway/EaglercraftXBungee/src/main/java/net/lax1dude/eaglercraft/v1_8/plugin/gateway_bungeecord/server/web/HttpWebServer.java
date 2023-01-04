package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.web;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.EaglerXBungee;

/**
 * Copyright (c) 2022-2023 LAX1DUDE. All Rights Reserved.
 * 
 * WITH THE EXCEPTION OF PATCH FILES, MINIFIED JAVASCRIPT, AND ALL FILES
 * NORMALLY FOUND IN AN UNMODIFIED MINECRAFT RESOURCE PACK, YOU ARE NOT ALLOWED
 * TO SHARE, DISTRIBUTE, OR REPURPOSE ANY FILE USED BY OR PRODUCED BY THE
 * SOFTWARE IN THIS REPOSITORY WITHOUT PRIOR PERMISSION FROM THE PROJECT AUTHOR.
 * 
 * NOT FOR COMMERCIAL OR MALICIOUS USE
 * 
 * (please read the 'LICENSE' file this repo's root directory for more info)
 * 
 */
public class HttpWebServer {
	
	public final File directory;
	public final Map<String,HttpContentType> contentTypes;
	private final Map<String,HttpMemoryCache> filesCache;
	private final List<String> index;
	private final String page404;
	private static HttpMemoryCache default404Page;
	private static HttpMemoryCache default404UpgradePage;
	private static final Object cacheClearLock = new Object();
	
	public HttpWebServer(File directory, Map<String,HttpContentType> contentTypes, List<String> index, String page404) {
		this.directory = directory;
		this.contentTypes = contentTypes;
		this.filesCache = new HashMap();
		this.index = index;
		this.page404 = page404;
	}
	
	public void flushCache() {
		long millis = System.currentTimeMillis();
		synchronized(cacheClearLock) {
			synchronized(filesCache) {
				Iterator<HttpMemoryCache> itr = filesCache.values().iterator();
				while(itr.hasNext()) {
					HttpMemoryCache i = itr.next();
					if(i.contentType.fileBrowserCacheTTL != Long.MAX_VALUE && millis - i.lastCacheHit > 900000l) {
						i.fileData.release();
						itr.remove();
					}
				}
			}
		}
	}
	
	public HttpMemoryCache retrieveFile(String path) {
		try {
			String[] pathSplit = path.split("(\\\\|\\/)+");
			
			List<String> pathList = pathSplit.length == 0 ? null : new ArrayList();
			for(int i = 0; i < pathSplit.length; ++i) {
				pathSplit[i] = pathSplit[i].trim();
				if(pathSplit[i].length() > 0) {
					if(!pathSplit[i].equals(".") && !pathSplit[i].startsWith("..")) {
						pathList.add(pathSplit[i]);
					}
				}
			}
			
			HttpMemoryCache cached;
			
			if(pathList == null || pathList.size() == 0) {
				for(int i = 0, l = index.size(); i < l; ++i) {
					cached = retrieveFile(index.get(i));
					if(cached != null) {
						return cached;
					}
				}
				return null;
			}
			
			String joinedPath = String.join("/", pathList);
	
			synchronized(cacheClearLock) {
				synchronized(filesCache) {
					cached = filesCache.get(joinedPath);
				}
				
				if(cached != null) {
					cached = validateCache(cached);
					if(cached != null) {
						return cached;
					}else {
						synchronized(filesCache) {
							filesCache.remove(joinedPath);
						}
					}
				}
				
				File f = new File(directory, joinedPath);
				
				if(!f.exists()) {
					if(page404 == null || path.equals(page404)) {
						return default404Page;
					}else {
						return retrieveFile(page404);
					}
				}
				
				if(f.isDirectory()) {
					for(int i = 0, l = index.size(); i < l; ++i) {
						String p = joinedPath + "/" + index.get(i);
						synchronized(filesCache) {
							cached = filesCache.get(p);
						}
						if(cached != null) {
							cached = validateCache(cached);
							if(cached != null) {
								synchronized(filesCache) {
									filesCache.put(joinedPath, cached);
								}
							}else {
								synchronized(filesCache) {
									filesCache.remove(p);
								}
								if(page404 == null || path.equals(page404)) {
									return default404Page;
								}else {
									return retrieveFile(page404);
								}
							}
							return cached;
						}
					}
					for(int i = 0, l = index.size(); i < l; ++i) {
						String p = joinedPath + "/" + index.get(i);
						File ff = new File(directory, p);
						if(ff.isFile()) {
							HttpMemoryCache memCache = retrieveFile(ff, p);
							if(memCache != null) {
								synchronized(filesCache) {
									filesCache.put(joinedPath, memCache);
								}
								return memCache;
							}
						}
					}
					if(page404 == null || path.equals(page404)) {
						return default404Page;
					}else {
						return retrieveFile(page404);
					}
				}else {
					HttpMemoryCache memCache = retrieveFile(f, joinedPath);
					if(memCache != null) {
						synchronized(filesCache) {
							filesCache.put(joinedPath, memCache);
						}
						return memCache;
					}else {
						if(page404 == null || path.equals(page404)) {
							return default404Page;
						}else {
							return retrieveFile(page404);
						}
					}
				}
			}
		}catch(Throwable t) {
			return default404Page;
		}
	}
	
	private HttpMemoryCache retrieveFile(File path, String requestCachePath) {
		int fileSize = (int)path.length();
		try(FileInputStream is = new FileInputStream(path)) {
			ByteBuf file = Unpooled.buffer(fileSize, fileSize);
			file.writeBytes(is, fileSize);
			String ext = path.getName();
			HttpContentType ct = null;
			int i = ext.lastIndexOf('.');
			if(i != -1) {
				ct = contentTypes.get(ext.substring(i + 1));
			}
			if(ct == null) {
				ct = HttpContentType.defaultType;
			}
			long millis = System.currentTimeMillis();
			return new HttpMemoryCache(path, requestCachePath, file, ct, millis, millis, path.lastModified());
		}catch(Throwable t) {
			return null;
		}
	}
	
	private HttpMemoryCache validateCache(HttpMemoryCache file) {
		if(file.fileObject == null) {
			return file;
		}
		long millis = System.currentTimeMillis();
		file.lastCacheHit = millis;
		if(millis - file.lastDiskReload > 4000l) {
			File f = file.fileObject;
			if(!f.isFile()) {
				return null;
			}else {
				long lastMod = f.lastModified();
				if(lastMod != file.lastDiskModified) {
					int fileSize = (int)f.length();
					try(FileInputStream is = new FileInputStream(f)) {
						file.fileData = Unpooled.buffer(fileSize, fileSize);
						file.fileData.writeBytes(is, fileSize);
						file.lastDiskReload = millis;
						file.lastDiskModified = lastMod;
						return file;
					}catch(Throwable t) {
						return null;
					}
				}else {
					return file;
				}
			}
		}else {
			return file;
		}
	}
	
	public static void regenerate404Pages() {
		if(default404Page != null) {
			default404Page.fileData.release();
		}
		default404Page = regenerateDefault404();
		if(default404UpgradePage != null) {
			default404UpgradePage.fileData.release();
		}
		default404UpgradePage = regenerateDefaultUpgrade404();
	}
	
	public static HttpMemoryCache getHTTP404() {
		return default404Page;
	}
	
	public static HttpMemoryCache getWebSocket404() {
		return default404UpgradePage;
	}
	
	private static HttpMemoryCache regenerateDefault404() {
		EaglerXBungee plugin = EaglerXBungee.getEagler();
		byte[] src = ("<!DOCTYPE html><html><head><title>" + htmlEntities(plugin.getConfig().getServerName()) + "</title><script type=\"text/javascript\">"
				+ "window.addEventListener(\"load\",()=>document.getElementById(\"addr\").innerText=window.location.href);</script></head>"
				+ "<body style=\"font-family:sans-serif;text-align:center;\"><h1>404 Not Found</h1><hr /><p style=\"font-size:1.2em;\">"
				+ "The requested resource <span id=\"addr\" style=\"font-family:monospace;font-weight:bold;background-color:#EEEEEE;padding:3px 4px;\">"
				+ "</span> could not be found on this server!</p><p>" + htmlEntities(plugin.getDescription().getName()) + "/"
				+ htmlEntities(plugin.getDescription().getVersion()) + "</p></body></html>").getBytes(StandardCharsets.UTF_8);
		HttpContentType htmlContentType = new HttpContentType(new HashSet(Arrays.asList("html")), "text/html", "utf-8", 120000l);
		long millis = System.currentTimeMillis();
		return new HttpMemoryCache(null, "~404", Unpooled.wrappedBuffer(src), htmlContentType, millis, millis, millis);
	}
	
	private static HttpMemoryCache regenerateDefaultUpgrade404() {
		EaglerXBungee plugin = EaglerXBungee.getEagler();
		String name = htmlEntities(plugin.getConfig().getServerName());
		byte[] src = ("<!DOCTYPE html><html><head><meta charset=\"UTF-8\" /><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" /><title>" + name +
				"</title><script type=\"text/javascript\">window.addEventListener(\"load\",()=>{var src=window.location.href;const gEI=(i)=>document.getElementById(i);"
				+ "if(src.startsWith(\"http:\")){src=\"ws:\"+src.substring(5);}else if(src.startsWith(\"https:\")){src=\"wss:\"+src.substring(6);}else{return;}"
				+ "gEI(\"wsUri\").innerHTML=\"<span id=\\\"wsField\\\" style=\\\"font-family:monospace;font-weight:bold;background-color:#EEEEEE;padding:3px 4px;\\\">"
				+ "</span>\";gEI(\"wsField\").innerText=src;});</script></head><body style=\"font-family:sans-serif;margin:0px;padding:12px;\"><h1 style=\"margin-block-start:0px;\">"
				+ "404 'Websocket Upgrade Failure' (rip)</h1><h3>The URL you have requested is the physical WebSocket address of '" + name + "'</h3><p style=\"font-size:1.2em;"
				+ "line-height:1.3em;\">To correctly join this server, load the latest EaglercraftX 1.8 client, click the 'Direct Connect' button<br />on the 'Multiplayer' screen, "
				+ "and enter <span id=\"wsUri\">this URL</span> as the server address</p></body></html>").getBytes(StandardCharsets.UTF_8);
		HttpContentType htmlContentType = new HttpContentType(new HashSet(Arrays.asList("html")), "text/html", "utf-8", 14400000l);
		long millis = System.currentTimeMillis();
		return new HttpMemoryCache(null, "~404", Unpooled.wrappedBuffer(src), htmlContentType, millis, millis, millis);
	}
	
	public static String htmlEntities(String input) {
		return input.replace("<", "&lt;").replace(">", "&gt;");
	}

}
