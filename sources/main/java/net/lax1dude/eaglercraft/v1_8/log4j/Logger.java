package net.lax1dude.eaglercraft.v1_8.log4j;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformRuntime;

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
public class Logger {
	
	public final String loggerName;

	Logger(String name) {
		this.loggerName = name;
	}
	
	public void trace(String msg) {
		log(Level.TRACE, msg);
	}
	
	public void trace(String msg, Object... args) {
		log(Level.TRACE, msg, args);
	}
	
	public void trace(Throwable msg) {
		log(Level.WARN, msg);
	}
	
	public void debug(String msg) {
		log(Level.DEBUG, msg);
	}
	
	public void debug(String msg, Object... args) {
		log(Level.DEBUG, msg, args);
	}
	
	public void debug(Throwable msg) {
		log(Level.DEBUG, msg);
	}
	
	public void info(String msg) {
		log(Level.INFO, msg);
	}
	
	public void info(String msg, Object... args) {
		log(Level.INFO, msg, args);
	}
	
	public void info(Throwable msg) {
		log(Level.INFO, msg);
	}
	
	public void warn(String msg) {
		log(Level.WARN, msg);
	}
	
	public void warn(String msg, Object... args) {
		log(Level.WARN, msg, args);
	}
	
	public void warn(Throwable msg) {
		log(Level.WARN, msg);
	}
	
	public void error(String msg) {
		log(Level.ERROR, msg);
	}
	
	public void error(String msg, Object... args) {
		log(Level.ERROR, msg, args);
	}
	
	public void error(Throwable msg) {
		log(Level.ERROR, msg);
	}
	
	public void fatal(String msg) {
		log(Level.FATAL, msg);
	}
	
	public void fatal(String msg, Object... args) {
		log(Level.FATAL, msg, args);
	}
	
	public void fatal(Throwable msg) {
		log(Level.FATAL, msg);
	}
	
	private static final SimpleDateFormat fmt = new SimpleDateFormat("hh:mm:ss+SSS");
	private static final Date dateInstance = new Date();
	
	public void log(Level level, String msg) {
		if(level.levelInt >= LogManager.logLevel.levelInt) {
			synchronized(LogManager.logLock) {
				PrintStream ps = level.getPrintStream();
				dateInstance.setTime(System.currentTimeMillis());
				ps.println("[" + fmt.format(dateInstance) + "]" +
						"[" + Thread.currentThread().getName() + "/" + level.levelName + "]" +
						"[" + loggerName + "]: " + msg);
			}
		}
	}
	
	public void log(Level level, String msg, Object... args) {
		if(level.levelInt >= LogManager.logLevel.levelInt) {
			synchronized(LogManager.logLock) {
				PrintStream ps = level.getPrintStream();
				dateInstance.setTime(System.currentTimeMillis());
				ps.println("[" + fmt.format(dateInstance) + "]" +
						"[" + Thread.currentThread().getName() + "/" + level.levelName + "]" +
						"[" + loggerName + "]: " + formatParams(msg, args));
			}
		}
	}

	public static String formatParams(String msg, Object... args) {
		if(args.length > 0) {
			StringBuilder builtString = new StringBuilder();
			for(int i = 0; i < args.length; ++i) {
				int idx = msg.indexOf("{}");
				if(idx != -1) {
					builtString.append(msg.substring(0, idx));
					builtString.append(args[i]);
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

	public void log(Level level, Throwable msg) {
		logExcp(level, "Exception Thrown", msg);
	}
	
	private void logExcp(final Level level, String h, Throwable msg) {
		log(level, "{}: {}", h, msg.toString());
		EagRuntime.getStackTrace(msg, (e) -> log(level, "    at {}", e));
		PlatformRuntime.printJSExceptionIfBrowser(msg);
		Throwable cause = msg.getCause();
		if(cause != null) {
			logExcp(level, "Caused By", cause);
		}
	}

	public boolean isDebugEnabled() {
		return LogManager.logLevel.levelInt <= Level.DEBUG.levelInt;
	}
	
}
