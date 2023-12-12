package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Copyright (c) 2023 lax1dude. All Rights Reserved.
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
public class DynamicLightManager {

	static final Map<String, DynamicLightInstance> lightRenderers = new HashMap();
	static final List<DynamicLightInstance> lightRenderList = new LinkedList();
	static long renderTimeout = 5000l;
	static boolean isRenderLightsPass = false;

	private static long lastTick = 0l;

	public static void renderDynamicLight(String lightName, double posX, double posY, double posZ, float red,
			float green, float blue, boolean shadows) {
		if(isRenderLightsPass) {
			DynamicLightInstance dl = lightRenderers.get(lightName);
			if(dl == null) {
				lightRenderers.put(lightName, dl = new DynamicLightInstance(lightName, shadows));
			}
			dl.updateLight(posX, posY, posZ, red, green, blue);
			lightRenderList.add(dl);
		}
	}

	public static boolean isRenderingLights() {
		return isRenderLightsPass;
	}

	public static void setIsRenderingLights(boolean b) {
		isRenderLightsPass = b;
	}

	static void updateTimers() {
		long millis = System.currentTimeMillis();
		if(millis - lastTick > 1000l) {
			lastTick = millis;
			Iterator<DynamicLightInstance> itr = lightRenderers.values().iterator();
			while(itr.hasNext()) {
				DynamicLightInstance dl = itr.next();
				if(millis - dl.lastCacheHit > renderTimeout) {
					dl.destroy();
					itr.remove();
				}
			}
		}
	}

	static void destroyAll() {
		Iterator<DynamicLightInstance> itr = lightRenderers.values().iterator();
		while(itr.hasNext()) {
			itr.next().destroy();
		}
		lightRenderers.clear();
	}

}
