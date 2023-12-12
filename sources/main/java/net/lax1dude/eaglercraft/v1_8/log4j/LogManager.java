package net.lax1dude.eaglercraft.v1_8.log4j;

import java.util.HashMap;
import java.util.Map;

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
public class LogManager {
	
	private static final Map<String,Logger> loggerInstances = new HashMap();
	
	public static final Object logLock = new Object();
	public static Level logLevel = Level.DEBUG;
	public static ILogRedirector logRedirector = null;
	
	public static Logger getLogger() {
		return getLogger("Minecraft");
	}
	
	public static Logger getLogger(String name) {
		Logger ret;
		synchronized(loggerInstances) {
			ret = loggerInstances.get(name);
			if(ret == null) {
				ret = new Logger(name);
			}
		}
		return ret;
	}
	
	public static void setLevel(Level lv) {
		logLevel = lv;
	}

}
