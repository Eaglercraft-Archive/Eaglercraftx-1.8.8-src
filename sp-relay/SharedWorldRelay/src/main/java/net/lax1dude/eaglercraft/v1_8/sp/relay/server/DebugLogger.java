package net.lax1dude.eaglercraft.v1_8.sp.relay.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.lax1dude.eaglercraft.v1_8.sp.relay.pkt.IRelayLogger;

/**
 * Copyright (c) 2022 lax1dude. All Rights Reserved.
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
public class DebugLogger implements IRelayLogger {
	
	private static Level debugLoggingLevel = Level.INFO;
	
	public static void enableDebugLogging(Level level) {
		if(level == null) {
			level = Level.NONE;
		}
		debugLoggingLevel = level;
	}
	
	public static boolean debugLoggingEnabled() {
		return debugLoggingLevel != Level.NONE;
	}
	
	private static final Map<String,DebugLogger> loggers = new HashMap();
	
	public static DebugLogger getLogger(String name) {
		synchronized(loggers) {
			DebugLogger ret = loggers.get(name);
			if(ret == null) {
				ret = new DebugLogger(name);
				loggers.put(name, ret);
			}
			return ret;
		}
	}
	
	private final String name;
	
	private DebugLogger(String name) {
		this.name = name;
	}
	
	public static enum Level {
		
		NONE("NONE", 0, System.out), DEBUG("DEBUG", 4, System.out), INFO("INFO", 3, System.out),
		WARN("WARN", 2, System.err), ERROR("ERROR", 1, System.err);
		
		public final String label;
		public final int level;
		public final PrintStream output;
		
		private Level(String label, int level, PrintStream output) {
			this.label = label;
			this.level = level;
			this.output = output;
		}
		
	}
	
	private class LogStream extends OutputStream {
		
		private final Level logLevel;
		private final ByteArrayOutputStream lineBuffer = new ByteArrayOutputStream();
		
		private LogStream(Level logLevel) {
			this.logLevel = logLevel;
		}

		@Override
		public void write(int b) throws IOException {
			if(b == (int)'\r') {
				return;
			}else if(b == (int)'\n') {
				byte[] line = lineBuffer.toByteArray();
				lineBuffer.reset();
				log(logLevel, new String(line, StandardCharsets.UTF_8));
			}else {
				lineBuffer.write(b);
			}
		}
		
	}

	private OutputStream infoOutputStream = null;
	private PrintStream infoPrintStream = null;
	
	private OutputStream warnOutputStream = null;
	private PrintStream warnPrintStream = null;
	
	private OutputStream errorOutputStream = null;
	private PrintStream errorPrintStream = null;
	
	public OutputStream getOutputStream(Level lvl) {
		switch(lvl) {
		case INFO:
		default:
			if(infoOutputStream == null) {
				infoOutputStream = new LogStream(Level.INFO);
			}
			return infoOutputStream;
		case WARN:
			if(warnOutputStream == null) {
				warnOutputStream = new LogStream(Level.WARN);
			}
			return warnOutputStream;
		case ERROR:
			if(errorOutputStream == null) {
				errorOutputStream = new LogStream(Level.ERROR);
			}
			return errorOutputStream;
		}
	}
	
	public PrintStream getPrintStream(Level lvl) {
		switch(lvl) {
		case INFO:
		default:
			if(infoPrintStream == null) {
				infoPrintStream = new PrintStream(getOutputStream(Level.INFO));
			}
			return infoPrintStream;
		case WARN:
			if(warnPrintStream == null) {
				warnPrintStream = new PrintStream(getOutputStream(Level.WARN));
			}
			return warnPrintStream;
		case ERROR:
			if(errorPrintStream == null) {
				errorPrintStream = new PrintStream(getOutputStream(Level.ERROR));
			}
			return errorPrintStream;
		}
	}
	
	private static final SimpleDateFormat fmt = new SimpleDateFormat("hh:mm:ss+SSS");
	private final Date dateInstance = new Date();
	
	public static String formatParams(String msg, Object... params) {
		if(params.length > 0) {
			StringBuilder builtString = new StringBuilder();
			for(int i = 0; i < params.length; ++i) {
				int idx = msg.indexOf("{}");
				if(idx != -1) {
					builtString.append(msg.substring(0, idx));
					if(params[i] instanceof InetSocketAddress) {
						params[i] = Util.sock2String((InetSocketAddress)params[i]);
					}
					builtString.append(params[i]);
					msg = msg.substring(idx + 2);
				}else {
					break;
				}
			}
			builtString.append(msg);
			return builtString.toString();
		}else {
			return msg;
		}
	}
	
	public void log(Level lvl, String msg, Object... params) {
		if(debugLoggingLevel.level >= lvl.level) {
			synchronized(this) {
				dateInstance.setTime(System.currentTimeMillis());
				System.out.println("[" + fmt.format(dateInstance) + "][" + Thread.currentThread().getName() + "/" + lvl.label + "][" + name + "]: " + 
						(params.length == 0 ? msg : formatParams(msg, params)));
			}
		}
	}
	
	public void log(Level lvl, Throwable stackTrace) {
		synchronized(this) {
			stackTrace.printStackTrace(getPrintStream(lvl));
		}
	}
	
	public void debug(String msg) {
		if(debugLoggingLevel.level >= Level.DEBUG.level) {
			log(Level.DEBUG, msg);
		}
	}
	
	public void debug(String msg, Object... params) {
		if(debugLoggingLevel.level >= Level.DEBUG.level) {
			log(Level.DEBUG, msg, params);
		}
	}
	
	public void debug(Throwable t) {
		if(debugLoggingLevel.level >= Level.DEBUG.level) {
			log(Level.DEBUG, t);
		}
	}
	
	public void info(String msg) {
		if(debugLoggingLevel.level >= Level.INFO.level) {
			log(Level.INFO, msg);
		}
	}
	
	public void info(String msg, Object... params) {
		if(debugLoggingLevel.level >= Level.INFO.level) {
			log(Level.INFO, msg, params);
		}
	}
	
	public void info(Throwable t) {
		if(debugLoggingLevel.level >= Level.INFO.level) {
			log(Level.INFO, t);
		}
	}
	
	public void warn(String msg) {
		if(debugLoggingLevel.level >= Level.WARN.level) {
			log(Level.WARN, msg);
		}
	}
	
	public void warn(String msg, Object... params) {
		if(debugLoggingLevel.level >= Level.WARN.level) {
			log(Level.WARN, msg, params);
		}
	}
	
	public void warn(Throwable t) {
		if(debugLoggingLevel.level >= Level.WARN.level) {
			log(Level.WARN, t);
		}
	}
	
	public void error(String msg) {
		if(debugLoggingLevel.level >= Level.ERROR.level) {
			log(Level.ERROR, msg);
		}
	}
	
	public void error(String msg, Object... params) {
		if(debugLoggingLevel.level >= Level.ERROR.level) {
			log(Level.ERROR, msg, params);
		}
	}
	
	public void error(Throwable t) {
		if(debugLoggingLevel.level >= Level.ERROR.level) {
			log(Level.ERROR, t);
		}
	}

}
