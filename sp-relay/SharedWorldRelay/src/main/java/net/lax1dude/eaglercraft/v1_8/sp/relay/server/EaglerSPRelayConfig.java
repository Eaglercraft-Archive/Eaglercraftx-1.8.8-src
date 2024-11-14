package net.lax1dude.eaglercraft.v1_8.sp.relay.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Copyright (c) 2022-2024 lax1dude. All Rights Reserved.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 */
public class EaglerSPRelayConfig {

	private String address = "0.0.0.0";
	private int port = 6699;
	private int codeLength = 5;
	private String codeChars = "abcdefghijklmnopqrstuvwxyz0123456789";
	private boolean codeMixCase = false;
	
	private int connectionsPerIP = 128;
	private int worldsPerIP = 32;
	
	private boolean openRateLimitEnable = true;
	private int openRateLimitPeriod = 192;
	private int openRateLimitLimit = 32;
	private int openRateLimitLockoutLimit = 48;
	private int openRateLimitLockoutDuration = 600;
	
	private boolean pingRateLimitEnable = true;
	private int pingRateLimitPeriod = 256;
	private int pingRateLimitLimit = 128;
	private int pingRateLimitLockoutLimit = 192;
	private int pingRateLimitLockoutDuration = 300;
	
	private String originWhitelist = "";
	private String[] originWhitelistArray = new String[0];
	private boolean enableRealIpHeader = false;
	private String realIpHeaderName = "X-Real-IP";
	private boolean enableShowLocals = true;
	private String serverComment = "Eags. Shared World Relay";

	public void load(File conf) {
		if(!conf.isFile()) {
			EaglerSPRelay.logger.info("Creating config file: {}", conf.getAbsoluteFile());
			save(conf);
		}else {
			EaglerSPRelay.logger.info("Loading config file: {}", conf.getAbsoluteFile());
			boolean gotPort = false, gotCodeLength = false, gotCodeChars = false;
			boolean gotCodeMixCase = false;
			boolean gotConnectionsPerIP = false, gotWorldsPerIP = false,
					gotOpenRateLimitEnable = false, gotOpenRateLimitPeriod = false,
					gotOpenRateLimitLimit = false, gotOpenRateLimitLockoutLimit = false,
					gotOpenRateLimitLockoutDuration = false;
			boolean gotPingRateLimitEnable = false, gotPingRateLimitPeriod = false,
					gotPingRateLimitLimit = false, gotPingRateLimitLockoutLimit = false,
					gotPingRateLimitLockoutDuration = false;
			boolean gotOriginWhitelist = false, gotEnableRealIpHeader = false,
					gotRealIpHeaderName = false, gotAddress = false, gotComment = false,
					gotShowLocals = false;
			
			Throwable t2 = null;
			try(BufferedReader reader = new BufferedReader(new FileReader(conf))) {
				String s;
				while((s = reader.readLine()) != null) {
					String[] ss = s.trim().split(":", 2);
					if(ss.length == 2) {
						ss[0] = ss[0].trim();
						ss[1] = ss[1].trim();
						if(ss[0].equalsIgnoreCase("port")) {
							try {
								port = Integer.parseInt(ss[1]);
								gotPort = true;
							}catch(Throwable t) {
								t2 = t;
								break;
							}
						}else if(ss[0].equalsIgnoreCase("address")) {
							address = ss[1];
							gotAddress = true;
						}else if(ss[0].equalsIgnoreCase("code-length")) {
							try {
								codeLength = Integer.parseInt(ss[1]);
								gotCodeLength = true;
							}catch(Throwable t) {
								EaglerSPRelay.logger.warn("Invalid code-length {} in conf {}", ss[1], conf.getAbsoluteFile());
								EaglerSPRelay.logger.warn(t);
								t2 = t;
								break;
							}
						}else if(ss[0].equalsIgnoreCase("code-chars")) {
							if(ss[1].length() < 2) {
								t2 = new IllegalArgumentException("not enough chars");
								EaglerSPRelay.logger.warn("Invalid code-chars {} in conf {}", ss[1], conf.getAbsoluteFile());
								EaglerSPRelay.logger.warn(t2);
							}else {
								codeChars = ss[1];
								gotCodeChars = true;
							}
						}else if(ss[0].equalsIgnoreCase("code-mix-case")) {
							try {
								codeMixCase = getBooleanValue(ss[1]);
								gotCodeMixCase = true;
							}catch(Throwable t) {
								EaglerSPRelay.logger.warn("Invalid code-mix-case {} in conf {}", ss[1], conf.getAbsoluteFile());
								EaglerSPRelay.logger.warn(t);
								t2 = t;
								break;
							}
						}else if(ss[0].equalsIgnoreCase("worlds-per-ip")) {
							try {
								worldsPerIP = Integer.parseInt(ss[1]);
								gotWorldsPerIP = true;
							}catch(Throwable t) {
								EaglerSPRelay.logger.warn("Invalid worlds-per-ip {} in conf {}", ss[1], conf.getAbsoluteFile());
								EaglerSPRelay.logger.warn(t);
								t2 = t;
								break;
							}
						}else if(ss[0].equalsIgnoreCase("world-ratelimit-enable")) {
							try {
								openRateLimitEnable = getBooleanValue(ss[1]);
								gotOpenRateLimitEnable = true;
							}catch(Throwable t) {
								EaglerSPRelay.logger.warn("Invalid world-ratelimit-enable {} in conf {}", ss[1], conf.getAbsoluteFile());
								EaglerSPRelay.logger.warn(t);
								t2 = t;
								break;
							}
						}else if(ss[0].equalsIgnoreCase("world-ratelimit-period")) {
							try {
								openRateLimitPeriod = Integer.parseInt(ss[1]);
								gotOpenRateLimitPeriod = true;
							}catch(Throwable t) {
								EaglerSPRelay.logger.warn("Invalid world-ratelimit-period {} in conf {}", ss[1], conf.getAbsoluteFile());
								EaglerSPRelay.logger.warn(t);
								t2 = t;
								break;
							}
						}else if(ss[0].equalsIgnoreCase("world-ratelimit-limit")) {
							try {
								openRateLimitLimit = Integer.parseInt(ss[1]);
								gotOpenRateLimitLimit = true;
							}catch(Throwable t) {
								EaglerSPRelay.logger.warn("Invalid world-ratelimit-limit {} in conf {}", ss[1], conf.getAbsoluteFile());
								EaglerSPRelay.logger.warn(t);
								t2 = t;
								break;
							}
						}else if(ss[0].equalsIgnoreCase("world-ratelimit-lockout-limit")) {
							try {
								openRateLimitLockoutLimit = Integer.parseInt(ss[1]);
								gotOpenRateLimitLockoutLimit = true;
							}catch(Throwable t) {
								EaglerSPRelay.logger.warn("Invalid world-ratelimit-lockout-limit {} in conf {}", ss[1], conf.getAbsoluteFile());
								EaglerSPRelay.logger.warn(t);
								t2 = t;
								break;
							}
						}else if(ss[0].equalsIgnoreCase("world-ratelimit-lockout-duration")) {
							try {
								openRateLimitLockoutDuration = Integer.parseInt(ss[1]);
								gotOpenRateLimitLockoutDuration = true;
							}catch(Throwable t) {
								EaglerSPRelay.logger.warn("Invalid world-ratelimit-lockout-duration {} in conf {}", ss[1], conf.getAbsoluteFile());
								EaglerSPRelay.logger.warn(t);
								t2 = t;
								break;
							}
						}else if(ss[0].equalsIgnoreCase("connections-per-ip")) {
							try {
								connectionsPerIP = Integer.parseInt(ss[1]);
								gotConnectionsPerIP = true;
							}catch(Throwable t) {
								EaglerSPRelay.logger.warn("Invalid connections-per-ip {} in conf {}", ss[1], conf.getAbsoluteFile());
								EaglerSPRelay.logger.warn(t);
								t2 = t;
								break;
							}
						}else if(ss[0].equalsIgnoreCase("ping-ratelimit-enable")) {
							try {
								pingRateLimitEnable = getBooleanValue(ss[1]);
								gotPingRateLimitEnable = true;
							}catch(Throwable t) {
								EaglerSPRelay.logger.warn("Invalid ping-ratelimit-enable {} in conf {}", ss[1], conf.getAbsoluteFile());
								EaglerSPRelay.logger.warn(t);
								t2 = t;
								break;
							}
						}else if(ss[0].equalsIgnoreCase("ping-ratelimit-period")) {
							try {
								pingRateLimitPeriod = Integer.parseInt(ss[1]);
								gotPingRateLimitPeriod = true;
							}catch(Throwable t) {
								EaglerSPRelay.logger.warn("Invalid ping-ratelimit-period {} in conf {}", ss[1], conf.getAbsoluteFile());
								EaglerSPRelay.logger.warn(t);
								t2 = t;
								break;
							}
						}else if(ss[0].equalsIgnoreCase("ping-ratelimit-limit")) {
							try {
								pingRateLimitLimit = Integer.parseInt(ss[1]);
								gotPingRateLimitLimit = true;
							}catch(Throwable t) {
								EaglerSPRelay.logger.warn("Invalid ping-ratelimit-limit {} in conf {}", ss[1], conf.getAbsoluteFile());
								EaglerSPRelay.logger.warn(t);
								t2 = t;
								break;
							}
						}else if(ss[0].equalsIgnoreCase("ping-ratelimit-lockout-limit")) {
							try {
								pingRateLimitLockoutLimit = Integer.parseInt(ss[1]);
								gotPingRateLimitLockoutLimit = true;
							}catch(Throwable t) {
								EaglerSPRelay.logger.warn("Invalid ping-ratelimit-lockout-limit {} in conf {}", ss[1], conf.getAbsoluteFile());
								EaglerSPRelay.logger.warn(t);
								t2 = t;
								break;
							}
						}else if(ss[0].equalsIgnoreCase("ping-ratelimit-lockout-duration")) {
							try {
								pingRateLimitLockoutDuration = Integer.parseInt(ss[1]);
								gotPingRateLimitLockoutDuration = true;
							}catch(Throwable t) {
								EaglerSPRelay.logger.warn("Invalid ping-ratelimit-lockout-duration {} in conf {}", ss[1], conf.getAbsoluteFile());
								EaglerSPRelay.logger.warn(t);
								t2 = t;
								break;
							}
						}else if(ss[0].equalsIgnoreCase("origin-whitelist")) {
							originWhitelist = ss[1];
							gotOriginWhitelist = true;
						}else if(ss[0].equalsIgnoreCase("enable-real-ip-header")) {
							try {
								enableRealIpHeader = getBooleanValue(ss[1]);
								gotEnableRealIpHeader = true;
							}catch(Throwable t) {
								EaglerSPRelay.logger.warn("Invalid enable-real-ip-header {} in conf {}", ss[1], conf.getAbsoluteFile());
								EaglerSPRelay.logger.warn(t);
								t2 = t;
								break;
							}
						}else if(ss[0].equalsIgnoreCase("real-ip-header-name")) {
							realIpHeaderName = ss[1];
							gotRealIpHeaderName = true;
						}else if(ss[0].equalsIgnoreCase("show-local-worlds")) {
							try {
								enableShowLocals = getBooleanValue(ss[1]);
								gotShowLocals = true;
							}catch(Throwable t) {
								EaglerSPRelay.logger.warn("Invalid show-local-worlds {} in conf {}", ss[1], conf.getAbsoluteFile());
								EaglerSPRelay.logger.warn(t);
								t2 = t;
								break;
							}
						}else if(ss[0].equalsIgnoreCase("server-comment")) {
							serverComment = ss[1];
							gotComment = true;
						}
					}
				}
			}catch(IOException t) {
				EaglerSPRelay.logger.error("Failed to load config file: {}", conf.getAbsoluteFile());
				EaglerSPRelay.logger.error(t);
			}catch(Throwable t) {
				EaglerSPRelay.logger.warn("Invalid config file: {}", conf.getAbsoluteFile());
				EaglerSPRelay.logger.warn(t);
				t2 = t;
			}
			if(t2 != null || !gotPort || !gotCodeLength || !gotCodeChars ||
					!gotCodeMixCase || !gotWorldsPerIP || !gotOpenRateLimitEnable ||
					!gotOpenRateLimitPeriod || !gotOpenRateLimitLimit ||
					!gotOpenRateLimitLockoutLimit || !gotOpenRateLimitLockoutDuration ||
					!gotConnectionsPerIP || !gotPingRateLimitEnable ||
					!gotPingRateLimitPeriod || !gotPingRateLimitLimit ||
					!gotPingRateLimitLockoutLimit || !gotPingRateLimitLockoutDuration ||
					!gotOriginWhitelist || !gotEnableRealIpHeader || !gotAddress ||
					!gotComment || !gotShowLocals || !gotRealIpHeaderName) {
				EaglerSPRelay.logger.warn("Updating config file: {}", conf.getAbsoluteFile());
				save(conf);
			}
			String[] splitted = originWhitelist.split(";");
			List<String> splittedList = new ArrayList<>();
			for(int i = 0; i < splitted.length; ++i) {
				splitted[i] = splitted[i].trim().toLowerCase();
				if(splitted[i].length() > 0) {
					splittedList.add(splitted[i]);
				}
			}
			originWhitelistArray = new String[splittedList.size()];
			for(int i = 0; i < originWhitelistArray.length; ++i) {
				originWhitelistArray[i] = splittedList.get(i);
			}
		}
	}
	
	public void save(File conf) {
		try(PrintWriter w = new PrintWriter(new FileOutputStream(conf))) {
			w.println("[EaglerSPRelay]");
			w.println("address: " + address);
			w.println("port: " + port);
			w.println("code-length: " + codeLength);
			w.println("code-chars: " + codeChars);
			w.println("code-mix-case: " + codeMixCase);
			w.println("connections-per-ip: " + connectionsPerIP);
			w.println("ping-ratelimit-enable: " + pingRateLimitEnable);
			w.println("ping-ratelimit-period: " + pingRateLimitPeriod);
			w.println("ping-ratelimit-limit: " + pingRateLimitLimit);
			w.println("ping-ratelimit-lockout-limit: " + pingRateLimitLockoutLimit);
			w.println("ping-ratelimit-lockout-duration: " + pingRateLimitLockoutDuration);
			w.println("worlds-per-ip: " + worldsPerIP);
			w.println("world-ratelimit-enable: " + openRateLimitEnable);
			w.println("world-ratelimit-period: " + openRateLimitPeriod);
			w.println("world-ratelimit-limit: " + openRateLimitLimit);
			w.println("world-ratelimit-lockout-limit: " + openRateLimitLockoutLimit);
			w.println("world-ratelimit-lockout-duration: " + openRateLimitLockoutDuration);
			w.println("origin-whitelist: " + originWhitelist);
			w.println("real-ip-header-name: " + realIpHeaderName);
			w.println("enable-real-ip-header: " + enableRealIpHeader);
			w.println("show-local-worlds: " + isEnableShowLocals());
			w.print("server-comment: " + serverComment);
		}catch(IOException t) {
			EaglerSPRelay.logger.error("Failed to write config file: {}", conf.getAbsoluteFile());
			EaglerSPRelay.logger.error(t);
		}
	}
	
	private static boolean getBooleanValue(String str) {
		if(str.equalsIgnoreCase("true") || str.equals("1")) {
			return true;
		}else if(str.equalsIgnoreCase("false") || str.equals("0")) {
			return false;
		}else {
			throw new IllegalArgumentException("Not a boolean: " + str);
		}
	}

	public String getAddress() {
		return address;
	}

	public int getPort() {
		return port;
	}

	public int getCodeLength() {
		return codeLength;
	}

	public String getCodeChars() {
		return codeChars;
	}

	public boolean isCodeMixCase() {
		return codeMixCase;
	}

	public int getConnectionsPerIP() {
		return connectionsPerIP;
	}

	public boolean isPingRateLimitEnable() {
		return pingRateLimitEnable;
	}

	public int getPingRateLimitPeriod() {
		return pingRateLimitPeriod;
	}

	public int getPingRateLimitLimit() {
		return pingRateLimitLimit;
	}

	public int getPingRateLimitLockoutLimit() {
		return pingRateLimitLockoutLimit;
	}

	public int getPingRateLimitLockoutDuration() {
		return pingRateLimitLockoutDuration;
	}

	public int getWorldsPerIP() {
		return worldsPerIP;
	}

	public boolean isWorldRateLimitEnable() {
		return openRateLimitEnable;
	}

	public int getWorldRateLimitPeriod() {
		return openRateLimitPeriod;
	}

	public int getWorldRateLimitLimit() {
		return openRateLimitLimit;
	}

	public int getWorldRateLimitLockoutLimit() {
		return openRateLimitLockoutLimit;
	}

	public int getWorldRateLimitLockoutDuration() {
		return openRateLimitLockoutDuration;
	}

	public String getOriginWhitelist() {
		return originWhitelist;
	}

	public String[] getOriginWhitelistArray() {
		return originWhitelistArray;
	}
	
	public boolean getIsWhitelisted(String domain) {
		if(originWhitelistArray.length == 0) {
			return true;
		}else {
			if(domain == null) {
				domain = "null";
			}else {
				domain = domain.toLowerCase();
				if(domain.equals("null")) {
					domain = "offline";
				}else {
					if(domain.startsWith("http://")) {
						domain = domain.substring(7);
					}else if(domain.startsWith("https://")) {
						domain = domain.substring(8);
					}
				}
			}
			for(int i = 0; i < originWhitelistArray.length; ++i) {
				String etr = originWhitelistArray[i].toLowerCase();
				if(etr.startsWith("*")) {
					if(domain.endsWith(etr.substring(1))) {
						return true;
					}
				}else {
					if(domain.equals(etr)) {
						return true;
					}
				}
			}
			return false;
		}
	}

	public String getRealIPHeaderName() {
		return realIpHeaderName;
	}

	public boolean isEnableRealIpHeader() {
		return enableRealIpHeader;
	}

	public String getComment() {
		return serverComment;
	}
	
	public String generateCode() {
		Random r = new Random();
		char[] ret = new char[codeLength];
		for(int i = 0; i < codeLength; ++i) {
			ret[i] = codeChars.charAt(r.nextInt(codeChars.length()));
			if(codeMixCase) {
				if(r.nextBoolean()) {
					ret[i] = Character.toLowerCase(ret[i]);
				}else {
					ret[i] = Character.toUpperCase(ret[i]);
				}
			}
		}
		return new String(ret);
	}

	public boolean isEnableShowLocals() {
		return enableShowLocals;
	}
	
}
