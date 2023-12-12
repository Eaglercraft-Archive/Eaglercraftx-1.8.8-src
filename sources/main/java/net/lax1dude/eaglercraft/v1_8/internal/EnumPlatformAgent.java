package net.lax1dude.eaglercraft.v1_8.internal;

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
public enum EnumPlatformAgent {
	DESKTOP("LWJGL3"), CHROME("Chrome"), EDGE("Edge"), IE("IE"),
	FIREFOX("Firefox"), SAFARI("Safari"), OPERA("Opera"), WEBKIT("WebKit"),
	GECKO("Gecko"), UNKNOWN("Unknown");
	
	private final String name;
	
	private EnumPlatformAgent(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public String toString() {
		return name;
	}
	
	public static EnumPlatformAgent getFromUA(String ua) {
		ua = " " + ua.toLowerCase();
		if(ua.contains(" edg/")) {
			return EDGE;
		}else if(ua.contains(" opr/")) {
			return OPERA;
		}else if(ua.contains(" chrome/")) {
			return CHROME;
		}else if(ua.contains(" firefox/")) {
			return FIREFOX;
		}else if(ua.contains(" safari/")) {
			return SAFARI;
		}else if(ua.contains(" trident/") || ua.contains(" msie")) {
			return IE;
		}else if(ua.contains(" webkit/")) {
			return WEBKIT;
		}else if(ua.contains(" gecko/")) {
			return GECKO;
		}else if(ua.contains(" desktop/")) {
			return DESKTOP;
		}else {
			return UNKNOWN;
		}
	}
	
}
