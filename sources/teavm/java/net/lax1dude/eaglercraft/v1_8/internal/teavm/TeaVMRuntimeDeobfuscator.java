package net.lax1dude.eaglercraft.v1_8.internal.teavm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.teavm.backend.javascript.spi.GeneratedBy;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSArrayReader;
import org.teavm.jso.core.JSString;

import com.google.common.collect.Lists;

import net.lax1dude.eaglercraft.v1_8.EagUtils;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformRuntime;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.generators.TeaVMRuntimeDeobfuscatorGenerator;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

/**
 * Copyright (c) 2024 lax1dude. All Rights Reserved.
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
public class TeaVMRuntimeDeobfuscator {

	private static final Logger logger = LogManager.getLogger("TeaVMRuntimeDeobfuscator");

	private static class DeobfNameEntry {

		private final String className;
		private final String functionName;

		private DeobfNameEntry(String className, String functionName) {
			this.className = className;
			this.functionName = functionName;
		}

	}

	private static final Object initLock = new Object();

	private static final Map<String,String> deobfClassNames = new HashMap<>();
	private static final Map<String,DeobfNameEntry> deobfFuncNames = new HashMap<>();

	private static boolean isInitialized = false;
	private static boolean isFailed = false;

	@GeneratedBy(TeaVMRuntimeDeobfuscatorGenerator.class)
	private static native JSArrayReader<JSObject> getAllClasses();

	private static void initialize0() {
		try {
			logger.info("Loading deobfuscation data, please wait...");
		}catch(Throwable t2) {
		}
		long time = PlatformRuntime.steadyTimeMillis();
		JSArrayReader<JSObject> classes = getAllClasses();
		if(classes.getLength() < 2) {
			return;
		}
		deobfClassNames.clear();
		deobfFuncNames.clear();
		JSArrayReader<JSString> stringReaderA = (JSArrayReader<JSString>)classes.get(0);
		JSArrayReader<JSString> stringReaderB = (JSArrayReader<JSString>)classes.get(1);
		String[] javaStringPoolA = new String[stringReaderA.getLength()];
		for(int i = 0; i < javaStringPoolA.length; ++i) {
			javaStringPoolA[i] = stringReaderA.get(i).stringValue();
		}
		String[] javaStringPoolB = new String[stringReaderB.getLength()];
		for(int i = 0; i < javaStringPoolB.length; ++i) {
			javaStringPoolB[i] = stringReaderB.get(i).stringValue();
		}
		for(int i = 2, l = classes.getLength() - 2; i < l; i += 3) {
			int[] lookupTblClsName = Base64VarIntArray.decodeVarIntArray((JSString)classes.get(i));
			StringBuilder classNameBuilder = new StringBuilder();
			boolean b = false;
			for(int j = 0; j < lookupTblClsName.length; ++j) {
				if(b) {
					classNameBuilder.append('.');
				}
				classNameBuilder.append(javaStringPoolA[lookupTblClsName[j]]);
				b = true;
			}
			String className = classNameBuilder.toString();
			String classObfName = ((JSString)classes.get(i + 1)).stringValue();
			deobfClassNames.put(classObfName, className);
			int[] lookupTbl = Base64VarIntArray.decodeVarIntArray((JSString)classes.get(i + 2));
			for(int j = 0, m = lookupTbl.length - 1; j < m; j += 2) {
				String obfName = javaStringPoolB[lookupTbl[j]];
				String deobfName = javaStringPoolB[lookupTbl[j + 1]];
				deobfFuncNames.put(obfName, new DeobfNameEntry(className, deobfName));
			}
		}
		try {
			time = PlatformRuntime.steadyTimeMillis() - time;
			logger.info("Indexed {} class names and {} function names after {}ms", deobfClassNames.size(), deobfFuncNames.size(), time);
		}catch(Throwable t2) {
		}
	}

	public static void initialize() {
		if(!isFailed) {
			synchronized(initLock) {
				if(!isInitialized) {
					try {
						initialize0();
						isInitialized = true;
					}catch(Throwable t) {
						isFailed = true;
						try {
							logger.error("Failed to initialize the tables!");
							logger.error(t);
						}catch(Throwable t2) {
						}
					}
				}
			}
		}
	}

	public static String deobfClassName(String clsName) {
		if(!isInitialized) return null;
		return deobfClassNames.get(clsName);
	}

	public static String deobfFunctionName(String funcName) {
		if(!isInitialized) return null;
		DeobfNameEntry ret = deobfFuncNames.get(funcName);
		return ret != null ? ret.functionName : null;
	}

	public static String deobfFunctionClass(String funcName) {
		if(!isInitialized) return null;
		DeobfNameEntry ret = deobfFuncNames.get(funcName);
		return ret != null ? ret.className : null;
	}

	public static String deobfFunctionFullName(String funcName) {
		if(!isInitialized) return null;
		DeobfNameEntry ret = deobfFuncNames.get(funcName);
		return ret != null ? (ret.className != null ? ret.className : "<unknown>") + "." + ret.functionName + "()" : null;
	}

	public static String deobfFullName(String funcName) {
		if(!isInitialized) return null;
		DeobfNameEntry ret = deobfFuncNames.get(funcName);
		return ret != null ? (ret.className != null ? ret.className : "<unknown>") + "." + ret.functionName + "()" : deobfClassNames.get(funcName);
	}

	private static int countLeadingWhitespace(String line) {
		for(int i = 0, l = line.length(); i < l; ++i) {
			char c = line.charAt(i);
			if(c != ' ' && c != '\t') {
				return i;
			}
		}
		return 0;
	}

	public static String deobfExceptionStack(String stackLines) {
		if(!isInitialized) return stackLines;
		try {
			List<String> lines = Lists.newArrayList(EagUtils.splitPattern.split(stackLines));
			deobfExceptionStack(lines);
			return String.join("\n", lines);
		}catch(Throwable t) {
			try {
				logger.error("Failed to deobfuscate stack trace!");
			}catch(Throwable t2) {
			}
			return stackLines;
		}
	}

	public static void deobfExceptionStack(List<String> stackLines) {
		if(!isInitialized) return;
		try {
			for(int i = 0, l = stackLines.size(); i < l; ++i) {
				String line = stackLines.get(i);
				int len = line.length();
				if(len == 0) continue;
				int leadingWs = countLeadingWhitespace(line);
				if(len > leadingWs + 3 && line.charAt(leadingWs) == 'a' && line.charAt(leadingWs + 1) == 't' && line.charAt(leadingWs + 2) == ' ') {
					leadingWs += 3;
				}
				int nextSpace = line.indexOf(' ', leadingWs);
				int nextDot = line.indexOf('.', leadingWs);
				String funcName2 = null;
				if(nextDot > 0 && nextDot < nextSpace) {
					funcName2 = line.substring(nextDot + 1, nextSpace);
					nextSpace = nextDot;
				}
				if(nextSpace == -1) {
					nextSpace = line.indexOf('@', leadingWs);
					if(nextSpace == -1 && nextSpace < leadingWs) {
						if(nextSpace == leadingWs + 1 && line.charAt(leadingWs) == '@') {
							continue;
						}
						nextSpace = len;
					}
				}
				if(nextSpace - leadingWs < 1) {
					continue;
				}
				String funcName = line.substring(leadingWs, nextSpace);
				String deobfName = deobfFunctionFullName(funcName);
				if(deobfName != null) {
					stackLines.set(i, line.substring(0, leadingWs) + deobfName + line.substring(nextSpace));
				}else {
					deobfName = deobfClassName(funcName);
					if(deobfName != null) {
						DeobfNameEntry deobfName2 = null;
						if(funcName2 != null && funcName2.indexOf('.') == -1) {
							deobfName2 = deobfFuncNames.get(funcName2);
						}
						if(deobfName2 != null && deobfName.equals(deobfName2.className)) {
							deobfName += "." + deobfName2.functionName + "()";
						}
						stackLines.set(i, line.substring(0, leadingWs) + deobfName + line.substring(nextSpace));
					}
				}
			}
		}catch(Throwable t) {
			try {
				logger.error("Failed to deobfuscate stack trace!");
			}catch(Throwable t2) {
			}
		}
	}

}
